<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${not empty tipoInstalacionAEditar ? 'Editar' : 'Registrar'} TipoInstalacion</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>
    <div class="form-container">
         <h2>${not empty tipoInstalacionAEditar ? 'tipoInstalacionAEditar' : 'Registro de Nuevo'} Tipo de Instalacion</h2>
        
        <form action="${pageContext.request.contextPath}/tipoInstalaciones" method="post">
        
            
            <c:if test="${not empty tipoInstalacionAEditar}">
                
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${tipoInstalacionAEditar.id}">
            </c:if>
            <c:if test="${empty tipoInstalacionAEditar}">
                
                <input type="hidden" name="action" value="registrar">
            </c:if>
            
            <div class="form-group">
                <label for="nombre">Nombre:</label>
               
                <input type="text" id="nombre" name="nombre" value="${tipoInstalacionAEditar.nombre}" required>
            </div>
            
            <div class="form-group">
                <label for="descripcion">Descripcion:</label>
                <input type="text" id="descripcion" name="descripcion" value="${tipoInstalacionAEditar.descripcion}" required>
            </div>
            
            
            <div class="form-group button-group">
                 
                <button type="submit" class="btn-primary">${not empty tipoInstalacionAEditar ? 'Actualizar' : 'Registrar'}</button>
                <a href="${pageContext.request.contextPath}/tipoInstalaciones" class="btn-secondary">Cancelar</a>
            </div>
        </form>


	    
</body>
</html>