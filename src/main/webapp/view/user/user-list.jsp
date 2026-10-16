<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>


<fmt:setBundle basename="messages"/>

<%@ include file="/partials/header.jsp" %>

    <h1><fmt:message key="user.list.title" /></h1>

<c:if test="${not empty messageKey}"><p class="notice"><fmt:message key="${messageKey}" /></p></c:if>
<div class="toolbar">
    <p><fmt:message key="user.list.count" />: <c:out value="${userCount}" /></p>
    <a href="${pageContext.request.contextPath}/users?action=new"><fmt:message key="user.list.add" /></a>
</div>
<table>
    <thead><tr>
        <th><fmt:message key="user.id" /></th><th><fmt:message key="user.username" /></th>
        <th><fmt:message key="user.active" /></th><th><fmt:message key="user.accountNonLocked" /></th>
        <th><fmt:message key="user.lastPasswordChange" /></th><th><fmt:message key="user.passwordExpiresAt" /></th>
        <th><fmt:message key="user.failedLoginAttempts" /></th><th><fmt:message key="user.emailVerified" /></th>
        <th><fmt:message key="user.mustChangePassword" /></th><th><fmt:message key="user.actions" /></th>
    </tr></thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td><c:out value="${user.id}" /></td><td><c:out value="${user.username}" /></td>
            <td><fmt:message key="bool.${user.active}" /></td><td><fmt:message key="bool.${user.accountNonLocked}" /></td>
            <td><c:out value="${user.lastPasswordChange}" /></td><td><c:out value="${user.passwordExpiresAt}" /></td>
            <td><c:out value="${user.failedLoginAttempts}" /></td><td><fmt:message key="bool.${user.emailVerified}" /></td>
            <td><fmt:message key="bool.${user.mustChangePassword}" /></td>
            <td><div class="actions">
                <a href="${pageContext.request.contextPath}/users?action=edit&amp;id=${user.id}"><fmt:message key="user.edit" /></a>
                <form action="${pageContext.request.contextPath}/users" method="post" onsubmit="return confirm('<fmt:message key="user.delete.confirm" />');">
                    <input type="hidden" name="action" value="delete"><input type="hidden" name="id" value="${user.id}">
                    <button type="submit"><fmt:message key="user.delete" /></button>
                </form>
            </div></td>
        </tr>
    </c:forEach>
    <c:if test="${empty users}"><tr><td colspan="10"><fmt:message key="user.list.empty" /></td></tr></c:if>
    </tbody>
</table>

<%@ include file="/partials/footer.jsp" %>
