package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout") // Este servlet responde a la URL /logout
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtenemos la sesión actual, sin crear una nueva si no existe
        HttpSession session = request.getSession(false); 

        if (session != null) {
            // Si la sesión existe, la invalidamos
            session.invalidate(); 
        }

        // Redirigimos al usuario a la página de login
        response.sendRedirect("login.jsp");
    }

    // Es buena práctica que doPost llame a doGet para que el logout funcione con ambos métodos
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
