<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administrador</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <% 
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // Scriptlet de seguridad: Redirigir si no hay sesión o el rol no es de administrador
        if (usuario == null || !usuario.getRol()) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Panel de Administrador</h2>
                <p>Hola, <strong><%= usuario.getNombre() %></strong>. Tienes acceso total al sistema.</p>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="btn-secondary">Cerrar Sesión</a>
        </header>

        <main class="dashboard-content">
            <h3>Acciones Rápidas</h3>
            <div class="action-grid">
                <a href="${pageContext.request.contextPath}/usuarios" class="action-card">
                    <h4>Gestionar Usuarios</h4>
                    <p>Ver, editar y eliminar usuarios del sistema.</p>
                </a>
                <a href="#" class="action-card">
                    <h4>Gestionar Reservas</h4>
                    <p>Visualizar y administrar todas las reservas.</p>
                </a>
                 <a href="#" class="action-card">
                    <h4>Ver Reportes</h4>
                    <p>Generar estadísticas y reportes de uso.</p>
                </a>
            </div>
            <!-- Aquí irá el resto del contenido de administración, como tablas, etc. -->
        </main>
    </div>

</body>
</html>