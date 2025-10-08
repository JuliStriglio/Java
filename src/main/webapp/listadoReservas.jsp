<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion de Reservas</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Gestión de 	Reservas</h2>
                <p>Aquí puedes ver, editar y eliminar las Reservas del sistema.</p>
            </div>
           
            <a href="${pageContext.request.contextPath}/reservas?action=mostrarFormulario" class="btn-primary">Añadir Nueva Reserva</a>
        </header>

        <main class="dashboard-content">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Instalacion</th>
                        <th>Fecha</th>
                        <th>HoraInicio</th>
                        <th>HoraFin</th>
                        <th>Estado</th>
                        <th>Monto</th>
                        <th class="actions-column">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${reservas}" var="reserva">
                        <tr>
                            <td>${reserva.id}</td>
                            <td>${reserva.usuario.nombre}</td>
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
        </main>
        
        <footer class="dashboard-footer">
             <a href="${pageContext.request.contextPath}/admin_dashboard.jsp" class="btn-secondary">Volver al Panel</a>
        </footer>
    </div>

</body>
</html>