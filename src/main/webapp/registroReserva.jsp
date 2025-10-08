<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${not empty reservaAEditar ? 'Editar' : 'Registrar'} Reserva</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>
    <div class="form-container">
         <h2>${not empty reservaAEditar ? 'reservaAEditar' : 'Registro de Nueva'} Reserva</h2>
        
        <form action="${pageContext.request.contextPath}/reservas" method="post">
        
            <!-- LÓGICA CLAVE: La acción y el ID cambian según si estamos editando o no -->
            <c:if test="${not empty reservaAEditar}">
                <!-- MODO EDICIÓN: La acción es 'actualizar' e incluimos el ID del usuario -->
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${reservaAEditar.id}">
            </c:if>
            <c:if test="${empty reservaAEditar}">
                <!-- MODO REGISTRO: La acción es 'registrar' -->
                <input type="hidden" name="action" value="registrar">
            </c:if>
            
            
<div class="form-group">
    <!-- Solo admin ve el select -->
    <c:if test="${usuario.rol}">
        <label for="usuario">Usuario:</label>
        <select name="usuarioId" id="usuario">
            <c:forEach var="u" items="${usuarios}">
                <option value="${u.id}">${u.nombre}</option>
            </c:forEach>
            
        </select>
    </c:if>

    <!-- Usuario común solo recibe un hidden con su ID -->
    <c:if test="${not usuario.rol}">
        <input type="hidden" name="usuarioId" value="${usuarioLogueado.id}" />
    </c:if>
</div>
            

            
            <div class="form-group">
                <label for="ins">Instalacion:</label>
                <select id="ins" name="insId" required>
               	 <c:forEach items="${instalaciones}" var="ins">
                    <option value="${ins.id}"
                        <c:if test="${reservaAEditar != null && reservaAEditar.ins.id == ins.id}">selected</c:if>>
                        ${ins.nombre}
                    </option>
                </c:forEach>
            </select>

            </div>
            
           <div class="form-group">
                <label for="fecha">Fecha:</label>
                <input type="date" id="fecha" name="fecha"value="${reservaAEditar.fecha}" required >
            </div>
            
            
            <div class="form-group">
                <label for="horaInicio">Hora Inicio:</label>
                <input type="time" id="horaInicio" name="horaInicio"value="${reservaAEditar.horaInicio}" required >
            </div>
            
            <div class="form-group">
                <label for="horaFin">Hora Fin:</label>
                <input type="time" id="horaFin" name="horaFin" value="${reservaAEditar.horaFin}" required >
            </div>
            
            <div class="form-group">
                <label for="monto">Monto:</label>
                <input type="number" id="monto" name="monto" value="${reservaAEditar.monto}" required >
            </div>
            
            <div class="form-group button-group">
                 
                <button type="submit" class="btn-primary">${not empty reservaAEditar ? 'Actualizar' : 'Registrar'}</button>
                <a href="${pageContext.request.contextPath}/reservas" class="btn-secondary">Cancelar</a>
            </div>
        </form>


	    
</body>
</html>