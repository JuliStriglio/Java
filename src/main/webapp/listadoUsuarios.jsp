<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Listado de Usuarios</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" >

</head>
<body>
    <h1>Listado de Usuarios</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody>
        
            <c:forEach items="${usuarios}" var="usuario">
			    <tr>
			        <td>${usuario.getId()}</td>
			        <td>${usuario.getNombre()}</td>
			        <td>${usuario.getEmail()}</td>
			    </tr>
			</c:forEach>
        </tbody>
    </table>
    
    <button onclick="history.back()" class="btn btn-secondary">Volver Atrás</button>
</body>
</html>