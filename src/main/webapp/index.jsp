<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio - Reservas Club</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
   
</head>
<body>
    <h1>Bienvenido al Sistema de Reservas</h1>

    <button class="btn-primary" href="${pageContext.request.contextPath}/usuarios">Ver Usuarios</button>
    <button class="btn-primary" href="${pageContext.request.contextPath}/login">inicio de sesion</button>
	<button class="btn-primary" href="${pageContext.request.contextPath}/usuarios?action=mostrarFormulario">Registrar</button>

</body>
</html>