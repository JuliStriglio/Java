package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.time.format.DateTimeParseException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.InstalacionDAO;
import dao.UsuarioDAO;
import dao.ReservaDAO;
import model.Instalacion;
import model.EstadoReserva;
import model.Reserva;
import model.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/reservas") 
public class ReservaServlet extends HttpServlet  {

    private static final long serialVersionUID = 1L;
    private ReservaDAO reservaDAO;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private InstalacionDAO instalacionDAO = new InstalacionDAO();

    public void init() {
        reservaDAO = new ReservaDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
            switch (action) {
            	case "mostrarFormulario": // <-- AÑADIR ESTE CASO
	                mostrarFormularioVacio(request, response);
	                break;
                case "cargar":
                    cargarReservaParaEditar(request, response);
                    break;
                case "eliminar":
                    eliminarReserva(request, response);
                    break;
                case "listar":
                default:
                    listarReservas(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listarReservas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Reserva> listaReservas;

        if (usuario == null) {
            listaReservas = new ArrayList<>();
        } else {
            listaReservas = reservaDAO.listarReservasPorUsuario(usuario.getId());
        }

       
        request.setAttribute("reservas", listaReservas);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user_dashboard.jsp");
        dispatcher.forward(request, response);
    }


    private void cargarReservaParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Obtener el ID del parámetro de la URL
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Usar el DAO para obtener el objeto Instalacion completo
        Reserva reservaExistente = reservaDAO.obtenerPorId(id); 
        
        // Poner el usuario en el request para que el formulario lo pueda leer
        request.setAttribute("reservaAEditar", reservaExistente);
        
        //Para que aparezca el select instalacion
        List<Instalacion> listaInstalacion = instalacionDAO.listarInstalacion();
        request.setAttribute("instalaciones", listaInstalacion);
        ;
        
        // Reenviar al mismo formulario que usas para registrar, pero ahora estará lleno
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroReserva.jsp");
        dispatcher.forward(request, response);
    }

    private void eliminarReserva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Reserva reserva = reservaDAO.obtenerPorId(id);

        if (reserva != null) {
            LocalDate hoy = LocalDate.now();

            if (reserva.getFecha().isAfter(hoy) && reserva.getEstado() != EstadoReserva.CANCELADA) {
                reservaDAO.actualizarEstadoReserva(id, EstadoReserva.CANCELADA);

            } else {
                reservaDAO.eliminarReserva(id);
            }
        }

        response.sendRedirect(request.getContextPath() + "/reservas?action=listar");
    }

    
    private void mostrarFormularioVacio(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
    	//paso usuario logueado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        request.setAttribute("usuario", usuario);
    	
        // Lista de usuarios
    	List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
    	request.setAttribute("usuarios", listaUsuarios);
        
    	//paso instalaciones 
    	List<Instalacion> listaInstalaciones = instalacionDAO.listarInstalacion();
    	request.setAttribute("instalaciones", listaInstalaciones);
    	

        
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/registroReserva.jsp");
        
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        try {
            
            switch (action) {
                case "registrar":
                    registrarReserva(request, response);
                    break;
                    
                case "actualizar":
                    actualizarReserva(request, response);
                    break;
                    
                default:
                   
                    response.sendRedirect(request.getContextPath() + "/instalaciones");
                    break;
            }
        } catch (SQLException e) {
        	  e.printStackTrace();
            throw new ServletException("Error en la base de datos", e);
        }
    }

    private void registrarReserva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogeado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogeado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 1️⃣ Obtener datos del formulario
            int idInstalacion = Integer.parseInt(request.getParameter("instalacion"));
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
            LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
            LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));

            // 2️⃣ Validar que la fecha no haya pasado
            if (fecha.isBefore(LocalDate.now())) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=fecha_pasada");
                return;
            }

            // 3️⃣ Validar que horaFin sea después de horaInicio y duración mínima 1 hora
            long duracionMinutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
            if (duracionMinutos < 60) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=duracion_minima");
                return;
            }

            // 4️⃣ Obtener usuario (admin puede elegir, usuario normal solo él mismo)
            int usuarioId = usuarioLogeado.getId();
            if (usuarioLogeado.getRol()) { // admin
                String usuarioParam = request.getParameter("usuarioId");
                if (usuarioParam != null && !usuarioParam.isEmpty()) {
                    usuarioId = Integer.parseInt(usuarioParam);
                }
            }
            Usuario usuarioSeleccionado = usuarioDAO.obtenerPorId(usuarioId);

            // 5️⃣ Obtener instalación y calcular monto
            Instalacion instalacion = instalacionDAO.obtenerPorId(idInstalacion);
            if (instalacion == null) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=instalacion_invalida");
                return;
            }
            double montoCalculado = (duracionMinutos / 60.0) * instalacion.getPrecioxhora();

            // 6️⃣ Verificar que la instalación esté disponible
            if (!reservaDAO.estaDisponible(idInstalacion, fecha, horaInicio, horaFin)) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=no_disponible");
                return;
            }

            // 7️⃣ Crear reserva y guardarla
            Reserva nuevaReserva = new Reserva(usuarioSeleccionado, instalacion, fecha, horaInicio, horaFin, montoCalculado);
            reservaDAO.agregarReserva(nuevaReserva);

            // ✅ Redirigir con éxito
            response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?exito=1");

        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            // Si hubo un error en parsing de número o fecha/hora
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=formato_invalido");
        }
    }
    
    private void actualizarReserva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        
        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int id = Integer.parseInt(request.getParameter("id"));
        Reserva reservaExistente = reservaDAO.obtenerPorId(id);
        
        if (reservaExistente == null) {
            response.sendRedirect(request.getContextPath() + "/reservas?action=listar&error=no_existe");
            return;
        }
        
        int idInstalacion = Integer.parseInt(request.getParameter("instalacion"));
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
        LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));
        double monto = Double.parseDouble(request.getParameter("monto"));
        
        Instalacion instalacion = new Instalacion();
        instalacion.setId(idInstalacion);
    	
        reservaExistente.setInstalacion(instalacion);
        reservaExistente.setFecha(fecha);
        reservaExistente.setHoraInicio(horaInicio);
        reservaExistente.setHoraFin(horaFin);
        reservaExistente.setMonto(monto);
        
        // Admin cambia el estado
        if (usuarioLogueado.getRol()) {
            try {
                EstadoReserva nuevoEstado = EstadoReserva.valueOf(request.getParameter("estado"));
                reservaExistente.setEstado(nuevoEstado);
            } catch (Exception e) {
                // si no viene estado o es inválido, lo dejamos igual
            }
        }
        
        reservaDAO.modificarReserva(reservaExistente);
        response.sendRedirect(request.getContextPath() + "/reservas?action=listar&actualizacion=exitosa");

  /*      int id = Integer.parseInt(request.getParameter("id"));
        
        int insId = Integer.parseInt(request.getParameter("insId"));
        Instalacion instalacion = new Instalacion(insId);
        
        String fechaStr = request.getParameter("fecha");
        LocalDate fecha = null;
        
        String horaInicioStr = request.getParameter("horaInicio");
        LocalTime horaInicio = null;
        
        if (horaInicioStr != null && !horaInicioStr.isEmpty()) {
        	horaInicio = LocalTime.parse(horaInicioStr); // convierte "14:30" a LocalTime
        }
        
        String horaFinStr = request.getParameter("horaFin");
        LocalTime horaFin = null;
        if (horaFinStr != null && !horaFinStr.isEmpty()) {
        	horaFin = LocalTime.parse(horaFinStr); // convierte "14:30" a LocalTime
        }
        
        String estadoStr = request.getParameter("estado");
        
        String montoStr = request.getParameter("monto"); // lo traés como String
        Double monto = null;

        if (montoStr != null && !montoStr.isEmpty()) {
            monto = Double.parseDouble(montoStr); // convertís a Double
        } 
        
        // Obtener usuario logueado desde sesión
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
        
        // Buscar instalación por nombre usando InstalacionDAO
        InstalacionDAO instalacionDAO = new InstalacionDAO();
        Instalacion instalacion = instalacionDAO.obtenerPorId(instalacion);
        
        
        EstadoReserva estado = EstadoReserva.valueOf(estadoStr.toUpperCase());
        
       

        
        Reserva reserva = new Reserva(id, usuarioLogueado,instalacion, fecha, horaInicio, horaFin, estado, monto);

        
        reservaDAO.modificarReserva(reserva); 

       
        response.sendRedirect(request.getContextPath() + "/reservas?action=listar&actualizacion=exitosa");*/
    
    }
}
