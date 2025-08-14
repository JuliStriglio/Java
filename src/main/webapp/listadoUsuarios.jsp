<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>

    <div class="dashboard-container">
        <header class="dashboard-header">
            <div class="welcome-message">
                <h2>Gestión de Usuarios</h2>
                <p>Aquí puedes ver, editar y eliminar los usuarios del sistema.</p>
            </div>
           
            <a href="${pageContext.request.contextPath}/usuarios?action=mostrarFormulario" class="btn-primary">Añadir Nuevo Usuario</a>
        </header>

        <main class="dashboard-content">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th class="actions-column">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${usuarios}" var="usuario">
                        <tr>
                            <td>${usuario.getId()}</td>
                            <td>${usuario.getNombre()}</td>
                            <td>${usuario.getEmail()}</td>
                            <td>${usuario.getRol() ? 'Admin' : 'Usuario'}</td>
                            <td class="actions-cell">
                                
                                <a href="${pageContext.request.contextPath}/usuarios?action=cargar&id=${usuario.getId()}" class="btn-icon btn-edit" title="Editar">✏️</a>
                                
                                
                                <a href="${pageContext.request.contextPath}/usuarios?action=eliminar&id=${usuario.getId()}" 
                                   class="btn-icon btn-delete" 
                                   title="Eliminar" 
                                   onclick="return confirm('¿Estás seguro de que deseas eliminar a este usuario? Esta acción es irreversible.');">🗑️</a>
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