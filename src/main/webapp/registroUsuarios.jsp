<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro de Usuarios</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
</head>
<body>
    <h1>Registro de Nuevo Usuario</h1>
    
    <%-- Mostrar mensajes --%>
    <c:if test="${not empty mensaje}">
        <div>${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div>${error}</div>
    </c:if>
    
    <div class="container"> 
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
	        <div class="">
		        <button class="btn-primary" type="submit">Registrar</button>
		        <button onclick="history.back()" class="btn btn-secondary">Volver Atrás</button>
	        </div>
	        
	    </form>
    
    </div>
	    
</body>
</html>