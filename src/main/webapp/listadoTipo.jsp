<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion de Tipo de Instalaciones</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Gestión de 	Tipo Instalaciones</h2>
                <p>Aquí puedes ver, editar y eliminar los tipos de instalaciones del sistema.</p>
            </div>
           
            <a href="${pageContext.request.contextPath}/tipoInstalaciones?action=mostrarFormulario" class="btn-primary">Añadir Nuevo Tipo de Instalacion</a>
        </header>

        <main class="dashboard-content">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Descripcion</th>
                        <th class="actions-column">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tiposInstalaciones}" var="tipoInstalacion">
                        <tr>
                            <td>${tipoInstalacion.id}</td>
                            <td>${tipoInstalacion.nombre}</td>
                            <td>${tipoInstalacion.descripcion}</td>
                            
                            
                            <td class="actions-cell">
                                
                                <a href="${pageContext.request.contextPath}/tipoInstalaciones?action=cargar&id=${tipoInstalacion.getId()}" class="btn-icon btn-edit" title="Editar">✏️</a>
                                
                                
                                <a href="${pageContext.request.contextPath}/tipoInstalaciones?action=eliminar&id=${tipoInstalacion.getId()}" 
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