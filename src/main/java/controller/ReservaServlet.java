package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
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
            	case "mostrarFormulario": 
	                mostrarFormularioVacio(request, response);
	                break;
                case "cargar":
                    cargarReservaParaEditar(request, response);
                    break;
                case "eliminar":
                    eliminarReserva(request, response);
                    break;
                case "listar":
                	listarReservas(request, response);
                	break;
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

        if (usuario == null) {
            // Si no está logueado no puede ver reservas
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Reserva> listaReservas;
        String paginaDestino;
        
        System.out.println("--- DEBUG START ---");
        System.out.println("Usuario ID: " + usuario.getId());
        System.out.println("Es Admin: " + usuario.getRol());

        if (usuario.getRol()) {
        	System.out.println("Cargando todas las reservas (Modo Admin)...");
            listaReservas = reservaDAO.listarReservas();
            paginaDestino = "listadoReservas.jsp"; 
        } else {
        	System.out.println("Cargando reservas propias (Modo Usuario)...");
            listaReservas = reservaDAO.listarReservasPorUsuario(usuario.getId());
            paginaDestino = "user_dashboard.jsp"; 
        }
        
        if (listaReservas == null) {
            System.out.println("PELIGRO: La lista de reservas es NULL");
        } else {
            System.out.println("Tamaño de la lista encontrada: " + listaReservas.size());
        }

        request.setAttribute("reservas", listaReservas);
        
        System.out.println("Redirigiendo a: " + paginaDestino);
        System.out.println("--- DEBUG END ---");

        // Usamos la variable paginaDestino para el forward
        RequestDispatcher dispatcher = request.getRequestDispatcher(paginaDestino);
        dispatcher.forward(request, response);
    }



    private void cargarReservaParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogueado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        Reserva reservaExistente = reservaDAO.obtenerPorId(id);
        request.setAttribute("reservaAEditar", reservaExistente);

        request.setAttribute("usuarioLogueado", usuarioLogueado);

        if (usuarioLogueado.getRol()) {
            List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
            request.setAttribute("usuarios", listaUsuarios);
        }

        System.out.println("RESERVA A EDITAR: " + reservaExistente);

        List<Instalacion> listaInstalacion = instalacionDAO.listarInstalacion();
        request.setAttribute("instalaciones", listaInstalacion);

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
    	
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        request.setAttribute("usuarioLogueado", usuario);
    	
        
    	List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
    	request.setAttribute("usuarios", listaUsuarios);
        
    	
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
        throws SQLException, IOException, ServletException  {

    HttpSession session = request.getSession();
    Usuario usuarioLogeado = (Usuario) session.getAttribute("usuario");

    if (usuarioLogeado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    try {
        int idInstalacion = Integer.parseInt(request.getParameter("instalacion"));
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
        LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));

        // --- VALIDACIONES ---
        if (fecha.isBefore(LocalDate.now())) {
            reenviarConError("fecha_pasada", request, response);
            return;
        }

        if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
            reenviarConError("horario_invalido", request, response);
            return;
        }

        long duracionMinutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
        if (duracionMinutos < 60) {
            reenviarConError("duracion_minima", request, response);
            return;
        }

        // --- USUARIO ---
        int usuarioId = usuarioLogeado.getId();
        EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;

        if (usuarioLogeado.getRol()) { // es admin
            String usuarioParam = request.getParameter("usuarioId");
            if (usuarioParam != null && !usuarioParam.isEmpty()) {
                usuarioId = Integer.parseInt(usuarioParam);
            }

            String estadoParam = request.getParameter("estado");
            if (estadoParam != null && !estadoParam.isEmpty()) {
                try {
                    estadoReserva = EstadoReserva.valueOf(estadoParam.toUpperCase());
                } catch (Exception ignored) { }
            }
        }

        Usuario usuarioSeleccionado = usuarioDAO.obtenerPorId(usuarioId);

        // --- INSTALACIÓN ---
        Instalacion instalacion = instalacionDAO.obtenerPorId(idInstalacion);
        if (instalacion == null) {
            reenviarConError("instalacion_invalida", request, response);
            return;
        }

        double montoCalculado = (duracionMinutos / 60.0) * instalacion.getPrecioxhora();

        // --- DISPONIBILIDAD ---
        if (!reservaDAO.estaDisponible(idInstalacion, fecha, horaInicio, horaFin)) {
            reenviarConError("no_disponible", request, response);
            return;
        }

        // --- CREAR Y GUARDAR ---
        Reserva nuevaReserva = new Reserva(
                usuarioSeleccionado,
                instalacion,
                fecha,
                horaInicio,
                horaFin,
                estadoReserva,
                montoCalculado
        );

        reservaDAO.agregarReserva(nuevaReserva);

        response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?exito=1");

    } catch (Exception e) {
        e.printStackTrace();
        reenviarConError("formato_invalido", request, response);
    }
}


    
    private void actualizarReserva(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Reserva reserva = reservaDAO.obtenerPorId(id);

        if (reserva == null) {
            response.sendRedirect("reservas?error=no_encontrada");
            return;
        }

        try {
            int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
            int instalacionId = Integer.parseInt(request.getParameter("instalacion"));
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
            LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
            LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));
            double monto = Double.parseDouble(request.getParameter("monto"));

            
            EstadoReserva estado = EstadoReserva.valueOf(request.getParameter("estado"));

            reserva.setUsuario(usuarioDAO.obtenerPorId(usuarioId));
            reserva.setInstalacion(instalacionDAO.obtenerPorId(instalacionId));
            reserva.setFecha(fecha);
            reserva.setHoraInicio(horaInicio);
            reserva.setHoraFin(horaFin);
            reserva.setEstado(estado);
            reserva.setMonto(monto);

            // Actualizar en DB
            reservaDAO.modificarReserva(reserva);

            System.out.println("Reserva actualizada correctamente: " + reserva);

            response.sendRedirect("reservas?exito=1");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("reservas?error=formato_invalido");
        }
    }

    private void reenviarConError(String codigoError, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("error", codigoError);

        request.setAttribute("instalacionSeleccionada", request.getParameter("instalacion"));
        request.setAttribute("fechaSeleccionada", request.getParameter("fecha"));
        request.setAttribute("horaInicioSeleccionada", request.getParameter("horaInicio"));
        request.setAttribute("horaFinSeleccionada", request.getParameter("horaFin"));
        request.setAttribute("usuarioSeleccionado", request.getParameter("usuarioId"));
        request.setAttribute("estadoSeleccionado", request.getParameter("estado"));

        request.getRequestDispatcher("/registroReserva.jsp").forward(request, response);
    }



}
