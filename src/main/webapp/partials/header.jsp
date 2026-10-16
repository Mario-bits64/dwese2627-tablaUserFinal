<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<fmt:setLocale value="${sessionScope.locale}" />

<head>
   <title><fmt:message key="msg.title" /></title>
</head>
<html>

<body>
    <nav>
        <ul>
            <!-- Enlace directo a un JSP -->
            <li><a href="/index.jsp">Inicio</a></li>

            <!-- Enlace a la ruta del Servlet -->
            <li><a href="/user">Usuario</a></li>

            <!-- Enlace a la ruta del Servlet Users-->
            <li><a href="/users">Usuarios</a></li>
            <li>
                <fmt:setBundle basename="messages"/>
                <form action = "changeLanguage" method="get">
                    <select name="lang" onchange="this.form.submit()">
                        <option value="en" ${sessionScope.locale.language == 'en' ? 'selected' : ''}>English</option>
                        <option value="es" ${sessionScope.locale.language == 'es' ? 'selected' : ''}>Español</option>
                    </select>
                </form>
            </li>
        </ul>
    </nav>
