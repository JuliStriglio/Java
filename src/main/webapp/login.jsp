<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
	<head>
	    <meta charset="UTF-8">
	    <title>Inicio de Sesión</title>
	    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
	    
	</head>
	<body>
	    <div class="form-container">
	        <h2>Iniciar Sesión</h2>
	        <form action="login" method="post">
	            <div class="form-group">
	                <label for="email">Email:</label>
	                <input type="email" id="email" name="email" required>
	            </div>
	            <div class="form-group">
	                <label for="password">Contraseña:</label>
	                <input type="password" id="password" name="password" required>
	            </div>
	            <div class="form-group button-group">
	            	<button class="btn-primary" type="submit">Entrar</button>
	            	<a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">Inicio</a>
	            </div>
	        </form>
	        <% String registro = request.getParameter("registro"); %>
				<% if ("exitoso".equals(registro)) { %>
				    <div style="color: green; font-weight: bold; margin-bottom: 10px;">
				        ¡Registro exitoso! Ahora podés iniciar sesión.
				    </div>
				<% } %>
	        
	        
	        <%
	            String mensajeError = (String) request.getAttribute("mensajeError");
	            if (mensajeError != null) {
	        %>
	            <p class="error"><%= mensajeError %></p>
	        <%
	            }
	        %>
	    </div>
	</body>
</html>