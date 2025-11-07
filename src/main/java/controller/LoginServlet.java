package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet("/login") 
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;

    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        Usuario usuario = usuarioDAO.verificarCredenciales(email, pass);

        if (usuario != null) {
            
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario); 

            
            if (usuario.getRol()) { 
                response.sendRedirect("admin_dashboard.jsp"); 
            } else { 
            	response.sendRedirect(request.getContextPath() + "/reservas?action=listar"); 
            }
        } else {
            request.setAttribute("mensajeError", "Email o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

}


