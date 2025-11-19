<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Usuario" %>
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
        // Scriptlet de seguridad: Redirige si no hay sesión
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
			    
			    <p style="color: red; font-weight: bold;">
				    Debug: ¿Hay reservas? ${reservas != null} | Cantidad: ${reservas.size()}
				</p>
				    <c:choose>
				        <c:when test="${empty reservas}">
				            <p>No tenés reservas registradas.</p>
				        </c:when>
				        <c:otherwise>
				            <table class="data-table">
				                <thead>
				                    <tr>
				                        <th>ID</th>
				                        <th>Instalación</th>
				                        <th>Fecha</th>
				                        <th>Hora Inicio</th>
				                        <th>Hora Fin</th>
				                        <th>Estado</th>
				                        <th>Monto</th>
				                        <th class="actions-column">Acciones</th>
				                    </tr>
				                </thead>
				                <tbody>
				                    <c:forEach var="reserva" items="${reservas}">
				                        <tr>
				                            <td>${reserva.id}</td>
				                            <td>${reserva.instalacion.nombre}</td>
				                            <td>${reserva.fecha}</td>
				                            <td>${reserva.horaInicio}</td>
				                            <td>${reserva.horaFin}</td>
				                            <td>${reserva.estado}</td>
				                            <td>${reserva.monto}</td>
				                            <td class="actions-cell">
                                
				                                <a href="${pageContext.request.contextPath}/reservas?action=cargar&id=${reserva.getId()}" class="btn-icon btn-edit" title="Editar">✏️</a>
				                                <a href="${pageContext.request.contextPath}/reservas?action=eliminar&id=${reserva.getId()}" 
				                                   class="btn-icon btn-delete" 
				                                   title="Eliminar" 
				                                   onclick="return confirm('¿Estás seguro de que deseas eliminar a esta reserva? Esta acción es irreversible.');">🗑️</a>
				                            </td>
				                        </tr>
				                    </c:forEach>
				                </tbody>
				            </table>
				        </c:otherwise>
				    </c:choose>
			    <a href="${pageContext.request.contextPath}/reservas?action=mostrarFormulario" class="btn-primary">Crear Nueva Reserva </a>
			</div>

        </main>
    </div>

</body>
</html>