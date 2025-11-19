<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bienvenido al Sistema de Reservas</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
   
</head>
<body>
    <div class="welcome-container">
        <header class="welcome-header">
            <h1>Sistema de Reservas</h1>
            <p class="subtitle">Gestiona tus espacios y citas de forma rápida y eficiente.</p>
        </header>
        
        <main class="welcome-main">
            <p>Para crear, consultar o administrar tus reservas, por favor, ingresa con tu cuenta. Si aún no tienes una, el registro es rápido y sencillo.</p>
        </main>

        <footer class="cta-container">
            
            <a class="btn-primary" href="${pageContext.request.contextPath}/login.jsp">Iniciar Sesión</a>
            <a class="btn-secondary" href="${pageContext.request.contextPath}/usuarios?action=mostrarFormulario">Registrarse</a>
        </footer>
    </div>
</body>
</html>