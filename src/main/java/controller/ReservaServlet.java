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
            // No está logueado → no puede ver reservas
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Reserva> listaReservas;

        if (usuario.getRol()) {
            // 🌟 ADMINISTRADOR → todas las reservas
            listaReservas = reservaDAO.listarReservas();
        } else {
            // 👤 USUARIO NORMAL → solo sus reservas
            listaReservas = reservaDAO.listarReservasPorUsuario(usuario.getId());
        }

        request.setAttribute("reservas", listaReservas);

        RequestDispatcher dispatcher = request.getRequestDispatcher("listadoReservas.jsp");
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
            throws SQLException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogeado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogeado == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 1️ Obtener datos del formulario
            int idInstalacion = Integer.parseInt(request.getParameter("instalacion"));
            LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
            LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
            LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));

            // 2️ Validar que la fecha no haya pasado
            if (fecha.isBefore(LocalDate.now())) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=fecha_pasada");
                return;
            }

            // 3️ Validar que horaFin sea después de horaInicio y duración mínima 1 hora
            long duracionMinutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
            if (duracionMinutos < 60) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=duracion_minima");
                return;
            }

            // 4️ Obtener usuario (admin puede elegir, usuario normal solo él mismo)
            int usuarioId = usuarioLogeado.getId();
            if (usuarioLogeado.getRol()) { // admin
                String usuarioParam = request.getParameter("usuarioId");
                if (usuarioParam != null && !usuarioParam.isEmpty()) {
                    usuarioId = Integer.parseInt(usuarioParam);
                }
            }
            Usuario usuarioSeleccionado = usuarioDAO.obtenerPorId(usuarioId);

            // 5️ Obtener instalación y calcular monto
            Instalacion instalacion = instalacionDAO.obtenerPorId(idInstalacion);
            if (instalacion == null) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=instalacion_invalida");
                return;
            }
            double montoCalculado = (duracionMinutos / 60.0) * instalacion.getPrecioxhora();

            // 6️ Verificar que la instalación esté disponible
            if (!reservaDAO.estaDisponible(idInstalacion, fecha, horaInicio, horaFin)) {
                response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=no_disponible");
                return;
            }

            // 7️ Crear reserva y guardarla
            Reserva nuevaReserva = new Reserva(usuarioSeleccionado, instalacion, fecha, horaInicio, horaFin, montoCalculado);
            reservaDAO.agregarReserva(nuevaReserva);

            // Redirigir con éxito
            response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?exito=1");

        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/registroReserva.jsp?error=formato_invalido");
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

            // 👇 IMPORTANTE: leer el nuevo estado
            EstadoReserva estado = EstadoReserva.valueOf(request.getParameter("estado"));

            // Asignar cambios
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


}
