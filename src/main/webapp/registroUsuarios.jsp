<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${not empty usuarioAEditar ? 'Editar' : 'Registrar'} Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>
    <div class="form-container">
         <h2>${not empty usuarioAEditar ? 'Editar' : 'Registrar Nuevo'} Usuario</h2>
        <p class="form-subtitle">Crea tu cuenta para acceder al sistema de reservas.</p>
        
        <form action="${pageContext.request.contextPath}/usuarios" method="post">           
            <c:if test="${not empty usuarioAEditar}">
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${usuarioAEditar.id}">
            </c:if>
            
            <c:if test="${empty usuarioAEditar}">
                <input type="hidden" name="action" value="registrar">
            </c:if>
            
            <div class="form-group">
                <label for="nombre">Nombre:</label> 
                <input type="text" id="nombre" name="nombre" value="${usuarioAEditar.nombre}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${usuarioAEditar.email}" required>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña:</label>      
                <input type="password" id="password" name="password" placeholder="Dejar en blanco para no cambiar"
                       <c:if test="${empty usuarioAEditar}">required</c:if>>
            </div>
            
            <div class="form-group">
                <label for="rol">Rol:</label>
                <select id="rol" name="rol">
                    <c:choose>
                        <c:when test="${not empty usuarioAEditar and esAdmin}">                            
                            <option value="usuario" ${usuarioAEditar.rol == 'usuario' ? 'selected' : ''}>Usuario Normal</option>
                            <option value="admin" ${usuarioAEditar.rol == 'admin' ? 'selected' : ''}>Administrador</option>
                        </c:when>
                        
                        <c:when test="${not empty usuarioAEditar and not esAdmin}">                            
                            <option value="usuario" selected disabled>Usuario Normal</option>
                            <input type="hidden" name="rol" value="usuario"> 
                        </c:when>
                        
                        <c:otherwise>                          
                            <option value="usuario" selected>Usuario Normal</option>
                            <input type="hidden" name="rol" value="usuario">
                        </c:otherwise>
                    </c:choose>
                </select>

                <c:if test="${not empty usuarioAEditar and not esAdmin}">                   
                    <p>Su rol actual es: <strong>${usuarioAEditar.rol == 'usuario' ? 'Usuario Normal' : 'Administrador'}</strong></p>
                </c:if>
                <c:if test="${empty usuarioAEditar}">                   
                    <p>Su rol será: <strong>Usuario Normal</strong></p>
                </c:if>
            </div>
            
            <%@ page import="model.Usuario" %>
				<%
				    HttpSession sesion = request.getSession(false);
				    Usuario usuario = (sesion != null) ? (Usuario) sesion.getAttribute("usuario") : null;
				    boolean esAdmin = (usuario != null && usuario.getRol());
				    String destinoCancelar;
				
				    if (esAdmin) {
				        destinoCancelar = request.getContextPath() + "/usuarios?action=listar";
				    } else {
				        destinoCancelar = request.getContextPath() + "/index.jsp"; 
				    }
				%>
        
            <div class="form-group button-group">                 
                <button type="submit" class="btn-primary">${not empty usuarioAEditar ? 'Actualizar' : 'Registrar'}</button>
                <a href="<%= destinoCancelar %>" class="btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>