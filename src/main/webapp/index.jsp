<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio - Reservas Club</title>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
        h1 { color: #2c3e50; }
        a, button {
            display: inline-block;
            margin: 10px;
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            border: none;
            cursor: pointer;
        }
        a:hover, button:hover { background-color: #2980b9; }
    </style>
</head>
<body>
    <h1>Bienvenido al Sistema de Reservas</h1>

    <a href="${pageContext.request.contextPath}/usuarios">Ver Usuarios</a>
	<a href="${pageContext.request.contextPath}/usuarios?action=mostrarFormulario">Registrar</a>

</body>
</html>