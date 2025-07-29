package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet("/login") // Este servlet responde a la URL /login
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    // El login solo debe procesar datos, por lo que solo necesita doPost
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        Usuario usuario = usuarioDAO.verificarCredenciales(email, pass);

        if (usuario != null) {
            // Si el usuario es válido, se crea una sesión
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario); // Guardamos el objeto usuario en la sesión

            // Redirigir según el rol del usuario
            if (usuario.getRol()) { // true (o 1) es para el Administrador
                response.sendRedirect("admin_dashboard.jsp"); // Página para administradores
            } else { // false (o 0) es para el Usuario normal
                response.sendRedirect("user_dashboard.jsp"); // Página para usuarios normales
            }
        } else {
            // Si las credenciales son incorrectas, se reenvía al login con un mensaje de error
            request.setAttribute("mensajeError", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    // Es buena práctica tener un doGet, aunque no se use, para evitar errores.
    // Puede redirigir al login o a la página principal.
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

}
