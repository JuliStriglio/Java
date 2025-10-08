package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
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
    	
        List<Reserva> listaReservas = reservaDAO.listarReserva();
        
        request.setAttribute("reservas", listaReservas);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("listadoReservas.jsp");
        
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
        
        reservaDAO.eliminarReserva(id); 
        
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
           
            throw new ServletException("Error en la base de datos", e);
        }
    }

    private void registrarReserva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	
        // 🧍‍♂️ 1. Obtener el usuario logueado desde la sesión
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");

        

        
        
        // 🏟️ 2. Obtener datos del formulario JSP
        int idInstalacion = Integer.parseInt(request.getParameter("idInstalacion"));
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime horaInicio = LocalTime.parse(request.getParameter("horaInicio"));
        LocalTime horaFin = LocalTime.parse(request.getParameter("horaFin"));
        double monto = Double.parseDouble(request.getParameter("monto"));
        
        int usuarioId;
        if(usuarioLogueado.getRol()) {  
            // El admin puede elegir cualquier usuario
            usuarioId = Integer.parseInt(request.getParameter("id"));
        } else {
            // Usuario común solo puede agregarse a sí mismo
            usuarioId = usuarioLogueado.getId();
        }
        Usuario usuarioSeleccionado = usuarioDAO.obtenerPorId(usuarioId);
        
        // 🧩 3. Crear objetos relacionados
        Instalacion instalacion = new Instalacion();
        instalacion.setId(idInstalacion);
        
        // 📌 4. Definir el estado inicial (por ejemplo, "PENDIENTE")
        EstadoReserva estado = EstadoReserva.PENDIENTE;
        
        Reserva nuevaReserva = new Reserva (usuarioSeleccionado, instalacion, fecha, horaInicio, horaFin, estado, monto);
        
        reservaDAO.agregarReserva(nuevaReserva); 
        response.sendRedirect(request.getContextPath() + "/reservas?action=listar&registro=exitoso");
        
    }
     

    private void actualizarReserva(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	

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
