package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import dao.UsuarioDAO;
import model.Usuario;


@WebServlet("/usuarios") // Este servlet responde a la URL /usuarios
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Método GET para listar usuarios o mostrar el formulario de registro
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("mostrarFormulario".equals(action)) {
            request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
        } else {
            // Por defecto, lista los usuarios
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            try {
                List<Usuario> usuarios = usuarioDAO.listarUsuarios();
                request.setAttribute("usuarios", usuarios);
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
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            
            // Asumiendo que el rol es un checkbox o un select. 
            // Si el valor es "true" o "on", es admin.
            boolean esAdmin = "true".equalsIgnoreCase(request.getParameter("rol")) || "on".equalsIgnoreCase(request.getParameter("rol"));

            Usuario nuevoUsuario = new Usuario(nombre, email, password, esAdmin);
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            try {
                usuarioDAO.agregarUsuario(nuevoUsuario);
                request.setAttribute("mensaje", "Usuario registrado exitosamente");
                request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error al registrar: " + e.getMessage());
                request.getRequestDispatcher("/registroUsuarios.jsp").forward(request, response);
            }
        } else {
            // Si no hay una acción específica, se podría redirigir o mostrar un error.
            // Por ahora, simplemente llamamos al doGet para que se comporte como se espera.
            doGet(request, response);
        }
    }
}