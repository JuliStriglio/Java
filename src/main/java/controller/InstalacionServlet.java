package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import dao.InstalacionDAO;
import model.Instalacion;
import model.TipoInstalacion;
import java.time.LocalTime;


@WebServlet("/instalaciones") 
public class InstalacionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InstalacionDAO instalacionDAO;
    
    public void init() {
        instalacionDAO = new InstalacionDAO();
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
                    cargarInstalacionParaEditar(request, response);
                    break;
                case "eliminar":
                    eliminarInstalacion(request, response);
                    break;
                case "listar":
                default:
                    listarInstalacion(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listarInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
    	
        List<Instalacion> listaInstalaciones = instalacionDAO.listarInstalacion();
        
        request.setAttribute("instalaciones", listaInstalaciones);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("listadoInstalaciones.jsp");
        
        dispatcher.forward(request, response);

    }

    private void cargarInstalacionParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Obtener el ID del parámetro de la URL
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Usar el DAO para obtener el objeto Instalacion completo
        Instalacion instalacionExistente = instalacionDAO.obtenerPorId(id); 
        
        // Poner el usuario en el request para que el formulario lo pueda leer
        request.setAttribute("instalacionAEditar", instalacionExistente);
        
        // Reenviar al mismo formulario que usas para registrar, pero ahora estará lleno
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroInstalacion.jsp");
        dispatcher.forward(request, response);
    }

    private void eliminarInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	
        int id = Integer.parseInt(request.getParameter("id"));
        
        instalacionDAO.eliminarInstalacion(id); 
        
        response.sendRedirect(request.getContextPath() + "/instalaciones?action=listar");
    }
    
    private void mostrarFormularioVacio(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        RequestDispatcher dispatcher = request.getRequestDispatcher("/registroInstalacion.jsp");
        
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        try {
            
            switch (action) {
                case "registrar":
                    registrarInstalacion(request, response);
                    break;
                    
                case "actualizar":
                    actualizarInstalacion(request, response);
                    break;
                    
                default:
                   
                    response.sendRedirect(request.getContextPath() + "/instalaciones");
                    break;
            }
        } catch (SQLException e) {
           
            throw new ServletException("Error en la base de datos", e);
        }
    }

    private void registrarInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        String nombre = request.getParameter("nombre");
        
        int tipoId = Integer.parseInt(request.getParameter("nombre"));
        TipoInstalacion tipo = new TipoInstalacion(tipoId);
        
        String horaAperturaStr = request.getParameter("horaApertura"); // String
        LocalTime horaApertura = null;

        if (horaAperturaStr != null && !horaAperturaStr.isEmpty()) {
        	horaApertura = LocalTime.parse(horaAperturaStr); // convierte "14:30" a LocalTime
        }
        String horaCierreStr = request.getParameter("horaCierre"); // String
        LocalTime horaCierre = null;

        if (horaCierreStr != null && !horaCierreStr.isEmpty()) {
            horaCierre = LocalTime.parse(horaCierreStr); // convierte "14:30" a LocalTime
        }
        String direccion = request.getParameter("direccion");
        String precioStr = request.getParameter("precioxhora"); // lo traés como String
        Double precioxhora = null;

        if (precioStr != null && !precioStr.isEmpty()) {
            precioxhora = Double.parseDouble(precioStr); // convertís a Double
        } 
        
 
       
        Instalacion nuevaInstalacion = new Instalacion(nombre, tipo, horaApertura, horaCierre, direccion, precioxhora);
        
     
        instalacionDAO.agregarInstalacion(nuevaInstalacion); 
        
        response.sendRedirect(request.getContextPath() + "/instalaciones?action=listar&registro=exitoso");
    }

    private void actualizarInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        
        int tipoId = Integer.parseInt(request.getParameter("nombre"));
        TipoInstalacion tipo = new TipoInstalacion(tipoId);
        
        String horaAperturaStr = request.getParameter("horaApertura"); // String
        LocalTime horaApertura = null;

        if (horaAperturaStr != null && !horaAperturaStr.isEmpty()) {
        	horaApertura = LocalTime.parse(horaAperturaStr); // convierte "14:30" a LocalTime
        }
        String horaCierreStr = request.getParameter("horaCierre"); // String
        LocalTime horaCierre = null;

        if (horaCierreStr != null && !horaCierreStr.isEmpty()) {
            horaCierre = LocalTime.parse(horaCierreStr); // convierte "14:30" a LocalTime
        }
        String direccion = request.getParameter("direccion");
        String precioStr = request.getParameter("precioxhora"); // lo traés como String
        Double precioxhora = null;

        if (precioStr != null && !precioStr.isEmpty()) {
            precioxhora = Double.parseDouble(precioStr); // convertís a Double
        } 


        
        Instalacion instalacion = new Instalacion(id, nombre, tipo, horaApertura, horaCierre, direccion, precioxhora);

        
        instalacionDAO.modificarInstalacion(instalacion); 

       
        response.sendRedirect(request.getContextPath() + "/instalaciones?action=listar&actualizacion=exitosa");
    
    }
}
