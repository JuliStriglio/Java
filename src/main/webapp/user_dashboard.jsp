<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
</head>
<body>
    <% 
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Redirigir si no hay sesión
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    <h1>Bienvenido, <%= usuario.getNombre() %>!</h1>
    <p>Este es tu panel de usuario.</p>
    <!-- Contenido para el usuario  -->
</body>
</html>