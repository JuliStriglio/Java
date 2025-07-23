package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	String action = request.getParameter("action");
        
        if ("mostrarFormulario".equals(action)) {
            request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
        } else {
        	UsuarioDAO usuarioDAO = new UsuarioDAO();
            try {
                List<Usuario> usuarios = usuarioDAO.listarUsuarios();
                
                request.setAttribute("usuarios", usuarios); // Envía datos al JSP
                // System.out.println("Usuarios enviados al JSP: " + request.getAttribute("usuarios"));
                request.getRequestDispatcher("/listadoUsuarios.jsp").forward(request, response);
                
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error al cargar usuarios: " + e.getMessage());
            }
        }
        
    }

 // Método POST para registrar nuevos usuarios
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("registrar".equals(action)) {
            // 1. Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String rol = request.getParameter("rol"); // Si tienes roles
            
            // 2. Crear objeto Usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(password);
            String rolParam = request.getParameter("rol");
            Boolean esAdmin = "admin".equals(rolParam); // Convierte a boolean
            nuevoUsuario.setRol(esAdmin);
            
            // 3. Guardar en base de datos
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            try {
                usuarioDAO.agregarUsuario(nuevoUsuario);
                
                // 4. Redirigir con mensaje de éxito
                request.setAttribute("mensaje", "Usuario registrado exitosamente");
                request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
                
            } catch (Exception e) {
                // 5. Manejar errores
                request.setAttribute("error", "Error al registrar: " + e.getMessage());
                request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
            }
        } else {
            // Si no es acción de registro, mostrar formulario
            request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
        }
    }
  }