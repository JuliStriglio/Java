<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro de Usuarios</title>
</head>
<body>
    <h1>Registro de Nuevo Usuario</h1>
    
    <%-- Mostrar mensajes --%>
    <c:if test="${not empty mensaje}">
        <div style="color: green;">${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>
    
    <form action="${pageContext.request.contextPath}/usuarios" method="post">
        <input type="hidden" name="action" value="registrar">
        
        <label>Nombre:</label>
        <input type="text" name="nombre" required><br>
        
        <label>Email:</label>
        <input type="email" name="email" required><br>
        
        <label>Contraseña:</label>
        <input type="password" name="password" required><br>
        
        <%-- Si tienes roles --%>
        <label>Rol:</label>
        <select name="rol">
		    <option value="usuario">Usuario Normal</option>
		    <option value="admin">Administrador</option>
		</select>
        
        <button type="submit">Registrar</button>
    </form>
</body>
</html>