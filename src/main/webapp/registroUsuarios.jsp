<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${not empty usuarioAEditar ? 'Editar' : 'Registrar'} Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>
    <div class="form-container">
         <h2>${not empty usuarioAEditar ? 'usuarioAEditar' : 'Registro de Nuevo'} Usuario</h2>
        <p class="form-subtitle">Crea tu cuenta para acceder al sistema de reservas.</p>
        
        <form action="${pageContext.request.contextPath}/usuarios" method="post">
        
            <!-- LÓGICA CLAVE: La acción y el ID cambian según si estamos editando o no -->
            <c:if test="${not empty usuarioAEditar}">
                <!-- MODO EDICIÓN: La acción es 'actualizar' e incluimos el ID del usuario -->
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${usuario.id}">
            </c:if>
            <c:if test="${empty usuarioAEditar}">
                <!-- MODO REGISTRO: La acción es 'registrar' -->
                <input type="hidden" name="action" value="registrar">
            </c:if>
            
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <!-- Usamos el atributo 'value' para pre-rellenar el campo si el usuario existe -->
                <input type="text" id="nombre" name="nombre" value="${usuarioAEditar.nombre}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${usuarioAEditar.email}" required>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <!-- NUNCA pre-rellenes una contraseña. Es una mala práctica de seguridad. -->
                <input type="password" id="password" name="password" placeholder="Dejar en blanco para no cambiar">
            </div>
            
            <div class="form-group">
                <label for="rol">Rol:</label>
                <select id="rol" name="rol">
                    <!-- Marcamos como 'selected' la opción que coincida con el rol del usuario -->
                    <option value="usuario" ${!usuarioAEditar.rol ? 'selected' : ''}>Usuario Normal</option>
                    <option value="admin" ${usuarioAEditar.rol ? 'selected' : ''}>Administrador</option>
                </select>
            </div>
            
            <div class="form-group button-group">
                 
                <button type="submit" class="btn-primary">${not empty usuarioAEditar ? 'Actualizar' : 'Registrar'}</button>
                <a href="${pageContext.request.contextPath}/usuarios" class="btn-secondary">Cancelar</a>
            </div>
        </form>


	    
</body>
</html>