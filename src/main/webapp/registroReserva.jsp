<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${empty reservaAEditar ? 'Registrar' : 'Editar'} Reserva</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="form-container">
    <h2>${empty reservaAEditar ? 'Registro de Nueva Reserva' : 'Editar Reserva'}</h2>


    <c:if test="${not empty param.error}">
        <div class="alert alert-error">
            <c:choose>
                <c:when test="${param.error == 'horario_invalido'}">⚠️ La hora de fin debe ser posterior a la de inicio.</c:when>
                <c:when test="${param.error == 'minimo_una_hora'}">⚠️ La reserva debe durar al menos una hora.</c:when>
                <c:when test="${param.error == 'no_disponible'}">❌ Ese horario ya está reservado.</c:when>
                <c:when test="${param.error == 'instalacion_no_encontrada'}">⚠️ Instalación no encontrada.</c:when>
                <c:when test="${param.error == 'formato_invalido'}">⚠️ Formato de fecha u hora inválido.</c:when>
                <c:otherwise>⚠️ Error al procesar la reserva.</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <c:if test="${not empty param.exito}">
        <div class="alert alert-success">
            ✅ Reserva registrada con éxito.
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/reservas" method="post">
        <c:choose>
            <c:when test="${empty reservaAEditar}">
                <input type="hidden" name="action" value="registrar">
            </c:when>
            <c:otherwise>
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${reservaAEditar.id}">
            </c:otherwise>
        </c:choose>

        <!-- USUARIO -->
        <div class="form-group">
            <c:choose>
                <c:when test="${usuarioLogueado.rol}">
                    <label for="usuarioId">Usuario:</label>
                    <select name="usuarioId" id="usuarioId">
                        <c:forEach var="u" items="${usuarios}">
                            <option value="${u.id}"
                                <c:if test="${reservaAEditar != null && reservaAEditar.usuario.id == u.id}">selected</c:if>>
                                ${u.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="usuarioId" value="${usuarioLogueado.id}">
                    <p><strong>Usuario:</strong> ${usuarioLogueado.nombre}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- INSTALACIÓN -->
        <div class="form-group">
            <label for="ins">Instalación:</label>
            <select id="ins" name="instalacion" required>
                <c:forEach var="ins" items="${instalaciones}">
                    <option value="${ins.id}"
                        <c:if test="${reservaAEditar != null && reservaAEditar.instalacion.id == ins.id}">selected</c:if>>
                        ${ins.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- FECHA -->
        <div class="form-group">
            <label for="fecha">Fecha:</label>
            <input type="date" id="fecha" name="fecha"
                   value="${reservaAEditar != null ? reservaAEditar.fecha : ''}" required>
        </div>

        <!-- HORA INICIO -->
        <div class="form-group">
            <label for="horaInicio">Hora Inicio:</label>
            <input type="time" id="horaInicio" name="horaInicio"
                   value="${reservaAEditar != null ? reservaAEditar.horaInicio : ''}" required>
        </div>

        <!-- HORA FIN -->
        <div class="form-group">
            <label for="horaFin">Hora Fin:</label>
            <input type="time" id="horaFin" name="horaFin"
                   value="${reservaAEditar != null ? reservaAEditar.horaFin : ''}" required>
        </div>

        <!-- ESTADO (solo admin) -->
        <c:if test="${usuarioLogueado.rol}">
            <div class="form-group">
                <label for="estado">Estado:</label>
                <select name="estado" id="estado">
                    <option value="PENDIENTE" ${reservaAEditar.estado == 'PENDIENTE' ? 'selected' : ''}>Pendiente</option>
                    <option value="ACTIVA" ${reservaAEditar.estado == 'ACTIVA' ? 'selected' : ''}>Activa</option>
                    <option value="CANCELADA" ${reservaAEditar.estado == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                </select>
            </div>
        </c:if>

        <!-- MONTO (readonly) -->
        <div class="form-group">
            <label for="monto">Monto:</label>
            <input type="number" id="monto" name="monto"
                   value="${reservaAEditar != null ? reservaAEditar.monto : ''}" required readonly>
            <small>💡 Se calculará automáticamente según las horas y la instalación.</small>
        </div>

        <!-- BOTONES -->
        <div class="form-group button-group">
            <button type="submit" class="btn-primary">
                ${empty reservaAEditar ? 'Registrar' : 'Actualizar'}
            </button>
            <c:choose>
                <c:when test="${usuarioLogueado.rol}">
                    <a href="${pageContext.request.contextPath}/reservas?action=listar" class="btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/user_dashboard.jsp" class="btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</div>

<!-- SCRIPT para calcular monto automáticamente -->
<script>
    const horaInicioInput = document.getElementById('horaInicio');
    const horaFinInput = document.getElementById('horaFin');
    const instalacionSelect = document.getElementById('ins');
    const montoInput = document.getElementById('monto');

    // Objeto con precios por instalación
    const precios = {
        <c:forEach var="ins" items="${instalaciones}" varStatus="status">
            ${ins.id}: ${ins.precioxhora}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    };

    function actualizarMonto() {
        const inicio = horaInicioInput.value;
        const fin = horaFinInput.value;
        const instalacionId = instalacionSelect.value;

        if (inicio && fin && instalacionId) {
            const [h1, m1] = inicio.split(':').map(Number);
            const [h2, m2] = fin.split(':').map(Number);
            const duracion = (h2 + m2/60) - (h1 + m1/60);

            if (duracion >= 1) {
                montoInput.value = (duracion * precios[instalacionId]).toFixed(2);
            } else {
                montoInput.value = '';
            }
        }
    }

    horaInicioInput.addEventListener('change', actualizarMonto);
    horaFinInput.addEventListener('change', actualizarMonto);
    instalacionSelect.addEventListener('change', actualizarMonto);
</script>

</body>
</html>

