package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import dao.UsuarioDAO;
import model.Usuario;


@WebServlet("/usuarios") 
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;
    
    public void init() {
        usuarioDAO = new UsuarioDAO();
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
                    cargarUsuarioParaEditar(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                case "listar":
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
    	
        List<Usuario> listaUsuarios = usuarioDAO.listarUsuarios();
        
        request.setAttribute("usuarios", listaUsuarios);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("listadoUsuarios.jsp");
        
        dispatcher.forward(request, response);
    }

    private void cargarUsuarioParaEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // Obtener el ID del parámetro de la URL
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Usar el DAO para obtener el objeto Usuario completo
        Usuario usuarioExistente = usuarioDAO.obtenerPorId(id); 
        
        // Poner el usuario en el request para que el formulario lo pueda leer
        request.setAttribute("usuarioAEditar", usuarioExistente);
        
        // Reenviar al mismo formulario que usas para registrar, pero ahora estará lleno
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
    	
        int id = Integer.parseInt(request.getParameter("id"));
        
        usuarioDAO.eliminarUsuario(id); 
        
        response.sendRedirect(request.getContextPath() + "/usuarios?action=listar");
    }
    
    private void mostrarFormularioVacio(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        RequestDispatcher dispatcher = request.getRequestDispatcher("/registroUsuarios.jsp");
        
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        try {
            
            switch (action) {
                case "registrar":
                    registrarUsuario(request, response);
                    break;
                    
                case "actualizar":
                    actualizarUsuario(request, response);
                    break;
                    
                default:
                   
                    response.sendRedirect(request.getContextPath() + "/usuarios");
                    break;
            }
        } catch (SQLException e) {
           
            throw new ServletException("Error en la base de datos", e);
        }
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
      
        boolean esAdmin = "admin".equals(request.getParameter("rol"));

       
        Usuario nuevoUsuario = new Usuario(nombre, email, password, esAdmin);
        
     
        usuarioDAO.agregarUsuario(nuevoUsuario); 
        
        response.sendRedirect(request.getContextPath() + "/usuarios?action=listar&registro=exitoso");
    }

    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
       
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean esAdmin = "admin".equals(request.getParameter("rol"));

       
        Usuario usuario = new Usuario(id, nombre, email, password, esAdmin);

        
        usuarioDAO.modificarUsuario(usuario); 

       
        response.sendRedirect(request.getContextPath() + "/usuarios?action=listar&actualizacion=exitosa");
    
    }
}