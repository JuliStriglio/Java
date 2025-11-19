<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="model.EstadoReserva" %>

<html>
<head>
    <title>${empty reservaAEditar ? 'Registrar' : 'Editar'} Reserva</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="form-container">
    <h2>${empty reservaAEditar ? 'Registro de Nueva Reserva' : 'Editar Reserva'}</h2>

    <c:choose>
        <c:when test="${not empty requestScope.error}">
            <c:set var="err" value="${requestScope.error}" />
        </c:when>
        <c:when test="${not empty param.error}">
            <c:set var="err" value="${param.error}" />
        </c:when>
        <c:otherwise>
            <c:set var="err" value="" />
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty err}">
        <div class="alert alert-error" style="margin-bottom: 15px;">
            <c:choose>
                <c:when test="${err == 'horario_invalido'}">⚠️ La hora de fin debe ser mayor a la de inicio.</c:when>
                <c:when test="${err == 'duracion_minima' or err == 'minimo_una_hora'}">⚠️ La reserva debe durar al menos una hora.</c:when>
                <c:when test="${err == 'no_disponible'}">❌ Ese horario ya está reservado.</c:when>
                <c:when test="${err == 'fecha_pasada'}">⚠️ La fecha no puede ser anterior a hoy.</c:when>
                <c:when test="${err == 'instalacion_invalida'}">⚠️ Instalación no encontrada.</c:when>
                <c:when test="${err == 'formato_invalido'}">⚠️ Formato inválido en fecha u hora.</c:when>
                <c:otherwise>⚠️ Ocurrió un error. (${fn:escapeXml(err)})</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    
    <c:if test="${not empty param.exito}">
        <div class="alert alert-success">✅ Reserva registrada con éxito.</div>
    </c:if>

    
    <c:set var="fechaVal">
        <c:choose>
            <c:when test="${not empty param.fecha}">${param.fecha}</c:when>
            <c:when test="${not empty requestScope.fecha}">${requestScope.fecha}</c:when>
            <c:when test="${not empty reservaAEditar}">${reservaAEditar.fecha}</c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </c:set>

    <c:set var="horaInicioVal">
        <c:choose>
            <c:when test="${not empty param.horaInicio}">${param.horaInicio}</c:when>
            <c:when test="${not empty requestScope.horaInicio}">${requestScope.horaInicio}</c:when>
            <c:when test="${not empty reservaAEditar}">${reservaAEditar.horaInicio}</c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </c:set>

    <c:set var="horaFinVal">
        <c:choose>
            <c:when test="${not empty param.horaFin}">${param.horaFin}</c:when>
            <c:when test="${not empty requestScope.horaFin}">${requestScope.horaFin}</c:when>
            <c:when test="${not empty reservaAEditar}">${reservaAEditar.horaFin}</c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </c:set>

    <c:set var="instalacionVal">
        <c:choose>
            <c:when test="${not empty param.instalacion}">${param.instalacion}</c:when>
            <c:when test="${not empty requestScope.instalacion}">${requestScope.instalacion}</c:when>
            <c:when test="${not empty reservaAEditar}">${reservaAEditar.instalacion.id}</c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </c:set>

    <c:set var="usuarioIdVal">
        <c:choose>
            <c:when test="${not empty param.usuarioId}">${param.usuarioId}</c:when>
            <c:when test="${not empty requestScope.usuarioId}">${requestScope.usuarioId}</c:when>
            <c:when test="${not empty reservaAEditar}">${reservaAEditar.usuario.id}</c:when>
            <c:otherwise>${usuarioLogueado.id}</c:otherwise>
        </c:choose>
    </c:set>

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
                <c:when test="${usuarioLogueado.rol == true}">
                    <label for="use">Usuario:</label>
                    <select name="usuarioId" id="use" required>
                        <c:if test="${empty usuarios}">
                            <option value="">No hay usuarios cargados</option>
                        </c:if>
                        <c:forEach var="use" items="${usuarios}">
                            <option value="${use.id}"
                                <c:if test="${usuarioIdVal == use.id || usuarioIdVal == (use.id + '')}">selected</c:if>>
                                ${use.nombre}
                            </option>
                        </c:forEach>
                    </select>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="usuarioId" value="${usuarioIdVal}" />
                    <p><strong>Usuario:</strong> ${usuarioLogueado.nombre}</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- INSTALACIÓN -->
        <div class="form-group">
            <label for="ins">Instalación:</label>
            <select id="ins" name="instalacion" required>
                <option value="">Seleccione instalación</option>
                <c:forEach var="ins" items="${instalaciones}">
                    <option value="${ins.id}"
                        <c:if test="${instalacionVal == ins.id || instalacionVal == (ins.id + '')}">selected</c:if>>
                        ${ins.nombre}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- FECHA -->
        <div class="form-group">
            <label for="fecha">Fecha:</label>
            <input type="date" id="fecha" name="fecha"
                   value="${fechaVal}" required>
        </div>

        <!-- HORA INICIO -->
        <div class="form-group">
            <label for="horaInicio">Hora Inicio:</label>
            <input type="time" id="horaInicio" name="horaInicio"
                   value="${horaInicioVal}" required>
        </div>

        <!-- HORA FIN -->
        <div class="form-group">
            <label for="horaFin">Hora Fin:</label>
            <input type="time" id="horaFin" name="horaFin"
                   value="${horaFinVal}" required>
        </div>

        <!-- ESTADO (solo admin) -->
        <c:if test="${usuarioLogueado.rol == true}">
            <div class="form-group">
                <label for="estado">Estado:</label>
                <select name="estado" class="form-select">
                    <c:forEach var="est" items="${EstadoReserva.values()}">
                        <option value="${est}"
                            <c:if test="${not empty reservaAEditar && reservaAEditar.estado == est}">
                                selected
                            </c:if>>
                            ${est}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </c:if>

        <!-- MONTO -->
        <div class="form-group">
            <label for="monto">Monto:</label>
            <input type="number" id="monto" name="monto"
                   value="${not empty reservaAEditar ? reservaAEditar.monto : ''}" readonly>
            <small>💡 Se calcula automáticamente según horas e instalación.</small>
        </div>

        <!-- BOTONES -->
        <div class="form-group button-group">
            <button type="submit" class="btn-primary">
                ${empty reservaAEditar ? 'Registrar' : 'Actualizar'}
            </button>
            <a href="${pageContext.request.contextPath}/reservas" class="btn">Cancelar</a>
        </div>
    </form>
</div>

<!-- SCRIPT cálculo monto -->
<script>
    const horaInicioInput = document.getElementById('horaInicio');
    const horaFinInput = document.getElementById('horaFin');
    const instalacionSelect = document.getElementById('ins');
    const montoInput = document.getElementById('monto');

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

    if (horaInicioInput && horaFinInput && instalacionSelect) {
        horaInicioInput.addEventListener('change', actualizarMonto);
        horaFinInput.addEventListener('change', actualizarMonto);
        instalacionSelect.addEventListener('change', actualizarMonto);
    }
</script>

</body>
</html>

