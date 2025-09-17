<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${not empty instalacionAEditar ? 'Editar' : 'Registrar'} Instalacion</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>
    <div class="form-container">
         <h2>${not empty instalacionAEditar ? 'instalacionAEditar' : 'Registro de Nueva'} Instalacion</h2>
        
        <form action="${pageContext.request.contextPath}/instalaciones" method="post">
        
            <!-- LÓGICA CLAVE: La acción y el ID cambian según si estamos editando o no -->
            <c:if test="${not empty instalacionAEditar}">
                <!-- MODO EDICIÓN: La acción es 'actualizar' e incluimos el ID del usuario -->
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${instalacionAEditar.id}">
            </c:if>
            <c:if test="${empty instalacionAEditar}">
                <!-- MODO REGISTRO: La acción es 'registrar' -->
                <input type="hidden" name="action" value="registrar">
            </c:if>
            
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <!-- Usamos el atributo 'value' para pre-rellenar el campo si el usuario existe -->
                <input type="text" id="nombre" name="nombre" value="${instalacionAEditar.nombre}" required>
            </div>
            
            <div class="form-group">
                <label for="tipo">Tipo:</label>
                <select id="tipo" name="tipoId" required>
               	 <c:forEach items="${tiposInstalaciones}" var="tipo">
                    <option value="${tipo.id}"
                        <c:if test="${instalacionAEditar != null && instalacionAEditar.tipo.id == tipo.id}">selected</c:if>>
                        ${tipo.nombre}
                    </option>
                </c:forEach>
            </select>

            </div>
            
            <div class="form-group">
                <label for="horaApertura">Hora de apertura:</label>
                <input type="time" id="horaApertura" name="horaApertura"value="${instalacionAEditar.horaApertura}" required >
            </div>
            
            <div class="form-group">
                <label for="horaCierre">Hora de cierre:</label>
                <input type="time" id="horaCierre" name="horaCierre" value="${instalacionAEditar.horaCierre}" required >
            </div>
            
            <div class="form-group">
                <label for="direccion">Direccion:</label>
                <input type="text" id="direccion" name="direccion" value="${instalacionAEditar.direccion}" required>
            </div>
            <div class="form-group">
                <label for="precioxhora">Precio por hora:</label>
                <input type="number" id="precioxhora" name="precioxhora" value="${instalacionAEditar.precioxhora}" required >
            </div>
            
            <div class="form-group button-group">
                 
                <button type="submit" class="btn-primary">${not empty instalacionAEditar ? 'Actualizar' : 'Registrar'}</button>
                <a href="${pageContext.request.contextPath}/instalaciones" class="btn-secondary">Cancelar</a>
            </div>
        </form>


	    
</body>
</html>