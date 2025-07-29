<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Administrador</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
</head>
<body>
    <% 
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Redirigir si no hay sesión o si el usuario no es admin
        if (usuario == null || !usuario.getRol()) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    <h1>Bienvenido, Administrador <%= usuario.getNombre() %>!</h1>
    <p>Este es el panel exclusivo para administradores.</p>
    <!-- funciones de administrador -->
</body>
</html>