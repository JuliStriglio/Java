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