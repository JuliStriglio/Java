<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<%-- Incluimos JSTL para usar tags como c:if, si es necesario en el futuro --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <% 
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Scriptlet de seguridad: Redirigir si no hay sesión
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Panel de Usuario</h2>
                <p>Bienvenido de nuevo, <strong><%= usuario.getNombre() %></strong></p>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="btn-secondary">Cerrar Sesión</a>
        </header>

        <main class="dashboard-content">
            <h3>Tus Reservas</h3>
            <div class="content-placeholder">
                <p>Próximamente, aquí podrás ver y gestionar todas tus reservas activas.</p>
                <a href="#" class="btn-primary">Crear Nueva Reserva</a>
            </div>
            <!-- Aquí irá el resto del contenido específico para el usuario, como tablas de reservas, etc. -->
        </main>
    </div>

</body>
</html>