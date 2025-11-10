package controller;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import dao.TipoInstalacionDAO;
import model.TipoInstalacion;



@WebServlet("/tipoInstalaciones") 
public class TipoInstalacionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TipoInstalacionDAO tipoInstalacionDAO;
    
    public void init() {
        tipoInstalacionDAO = new TipoInstalacionDAO();
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
                    cargarInstalacionParaEditar(request, response);
                    break;
                case "eliminar":
                    eliminarInstalacion(request, response);
                    break;
                case "listar":
                default:
                    listarTipoInstalacion(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listarTipoInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
    	
        List<TipoInstalacion> listaTipoInstalaciones = tipoInstalacionDAO.listarTipo();
        
        request.setAttribute("tiposInstalaciones", listaTipoInstalaciones);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("listadoTipo.jsp");
        
        dispatcher.forward(request, response);

    }

    private void cargarInstalacionParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        
        TipoInstalacion tipoInstalacionExistente = TipoInstalacionDAO.obtenerPorId(id); 
        
        
        request.setAttribute("tipoInstalacionAEditar", tipoInstalacionExistente);
        
      
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroTipoInstalacion.jsp");
        dispatcher.forward(request, response);
    }

    private void eliminarInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	
        int id = Integer.parseInt(request.getParameter("id"));
        
        tipoInstalacionDAO.eliminarTipoInstalacion(id); 
        
        response.sendRedirect(request.getContextPath() + "/tipoInstalaciones?action=listar");
    }
    
    private void mostrarFormularioVacio(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        RequestDispatcher dispatcher = request.getRequestDispatcher("/registroTipoInstalacion.jsp");
        
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        try {
            
            switch (action) {
                case "registrar":
                    registrarTipoInstalacion(request, response);
                    break;
                    
                case "actualizar":
                    actualizarTipoInstalacion(request, response);
                    break;
                    
                default:
                   
                    response.sendRedirect(request.getContextPath() + "/tipoInstalaciones");
                    break;
            }
        } catch (SQLException e) {
           
            throw new ServletException("Error en la base de datos", e);
        }
    }

    private void registrarTipoInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
             
 
       
        TipoInstalacion nuevoTipo = new TipoInstalacion(nombre, descripcion);
        
     
        tipoInstalacionDAO.agregarTipoInstalacion(nuevoTipo); 
        
        response.sendRedirect(request.getContextPath() + "/tipoInstalaciones?action=listar&registro=exitoso");
    }

    private void actualizarTipoInstalacion(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        
        
        TipoInstalacion tipoInstalacion = new TipoInstalacion(id, nombre, descripcion);

        
        TipoInstalacionDAO.modificarTipoInstalacion(tipoInstalacion); 

       
        response.sendRedirect(request.getContextPath() + "/tipoInstalaciones?action=listar&actualizacion=exitosa");
    
    }
}
