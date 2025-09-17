<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion de Instalaciones</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Gestión de 	Instalaciones</h2>
                <p>Aquí puedes ver, editar y eliminar los instalaciones del sistema.</p>
            </div>
           
            <a href="${pageContext.request.contextPath}/instalaciones?action=mostrarFormulario" class="btn-primary">Añadir Nueva Instalacion</a>
        </header>

        <main class="dashboard-content">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Tipo</th>
                        <th>HoraApertura</th>
                        <th>HoraCierre</th>
                        <th>Direccion</th>
                        <th>PrecioxHora</th>
                        <th class="actions-column">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${instalaciones}" var="instalacion">
                        <tr>
                            <td>${instalacion.id}</td>
                            <td>${instalacion.nombre}</td>
                            <td>${instalacion.tipo.nombre}</td>
                            <td>${instalacion.horaApertura}</td>
                            <td>${instalacion.horaCierre}</td>
                            <td>${instalacion.direccion}</td>
                            <td>${instalacion.precioxhora}</td>
                            
                            
                            <td class="actions-cell">
                                
                                <a href="${pageContext.request.contextPath}/instalaciones?action=cargar&id=${instalacion.getId()}" class="btn-icon btn-edit" title="Editar">✏️</a>
                                
                                
                                <a href="${pageContext.request.contextPath}/instalaciones?action=eliminar&id=${instalacion.getId()}" 
                                   class="btn-icon btn-delete" 
                                   title="Eliminar" 
                                   onclick="return confirm('¿Estás seguro de que deseas eliminar a esta instalacion? Esta acción es irreversible.');">🗑️</a>
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