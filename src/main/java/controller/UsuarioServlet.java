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

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogeado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
        boolean esAdmin = (usuarioLogeado != null && usuarioLogeado.getRol());

        request.setAttribute("esAdmin", esAdmin);

        
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
                    if (usuarioLogeado == null) {
                        response.sendRedirect(request.getContextPath() + "/login.jsp");
                        return;
                    }
                    cargarUsuarioParaEditar(request, response);
                    break;

                case "eliminar":
                    if (!esAdmin) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                            "Acceso denegado. Solo administradores pueden eliminar usuarios.");
                        return;
                    }
                    eliminarUsuario(request, response);
                    break;

                case "listar":
                    if (!esAdmin) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                            "Acceso denegado. Solo administradores pueden listar usuarios.");
                        return;
                    }
                    listarUsuarios(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                        "Acción no válida: " + action);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException("Error al procesar la acción: " + action, ex);
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

        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de usuario inválido");
            return;
        }

        int id = Integer.parseInt(idParam);
        Usuario usuarioExistente = usuarioDAO.obtenerPorId(id);

        if (usuarioExistente == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado");
            return;
        }

        request.setAttribute("usuarioAEditar", usuarioExistente);
        RequestDispatcher dispatcher = request.getRequestDispatcher("registroUsuarios.jsp");
        dispatcher.forward(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            return;
        }

        int id = Integer.parseInt(idParam);
        usuarioDAO.eliminarUsuario(id);
        response.sendRedirect(request.getContextPath() + "/usuarios?action=listar&eliminacion=exitosa");
    }

    private void mostrarFormularioVacio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/registroUsuarios.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }

        try {
            switch (action) {
                case "registrar":
                    registrarUsuario(request, response);
                    break;

                case "actualizar":
                    actualizarUsuario(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción POST no válida: " + action);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Error en la base de datos durante la acción: " + action, e);
        }
    }

    private void registrarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean esAdminDelUsuarioNuevo = "admin".equalsIgnoreCase(request.getParameter("rol"));

        boolean creadoPorAdmin = "true".equals(request.getParameter("creadoPorAdmin"));

        // Validaciones básicas
        if (nombre == null || email == null || password == null ||
            nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan campos obligatorios");
            return;
        }

        // Crear el usuario nuevo
        Usuario nuevoUsuario = new Usuario(nombre, email, password, esAdminDelUsuarioNuevo);
        usuarioDAO.agregarUsuario(nuevoUsuario);

        // Redirecciones según quién creó el usuario
        if (creadoPorAdmin) {
            // El administrador creó este usuario, se queda en su panel
            response.sendRedirect(request.getContextPath() + "/usuarios?action=listar&registro=exitoso");
        } else {
            // Registro normal desde la pantalla pública
            response.sendRedirect(request.getContextPath() + "/login.jsp?registro=exitoso");
        }
    }


    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            return;
        }

        int id = Integer.parseInt(idParam);
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        boolean esAdmin = "admin".equalsIgnoreCase(request.getParameter("rol"));

        if (nombre == null || email == null || password == null ||
            nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan campos obligatorios");
            return;
        }

        Usuario usuario = new Usuario(id, nombre, email, password, esAdmin);
        usuarioDAO.modificarUsuario(usuario);
        response.sendRedirect(request.getContextPath() + "/usuarios?action=listar&actualizacion=exitosa");
    }
}
