<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>


<fmt:setBundle basename="messages"/>

<%@ include file="/partials/header.jsp" %>

<h1><fmt:message key="${empty user.id ? 'user.form.new' : 'user.form.edit'}" /></h1>
<c:if test="${not empty errors}"><p role="alert"><fmt:message key="validation.summary" /></p></c:if>
<form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="action" value="save">
    <c:if test="${not empty user.id}"><input type="hidden" name="id" value="${user.id}"></c:if>
    <fieldset>
        <legend><fmt:message key="user.section.identity" /></legend>
        <div class="field"><label for="username"><fmt:message key="user.username" /></label>
            <input id="username" name="username" type="text" maxlength="40" required value="<c:out value='${user.username}' />" autocomplete="username">
            <c:if test="${not empty errors.username}"><p class="error"><fmt:message key="${errors.username}" /></p></c:if>
        </div>
        <div class="field"><label for="passwordHash"><fmt:message key="user.passwordHash" /></label>
            <input id="passwordHash" name="passwordHash" type="text" maxlength="500" required value="<c:out value='${user.passwordHash}' />" autocomplete="off" aria-describedby="hash-help">
            <small id="hash-help"><fmt:message key="user.passwordHash.help" /></small>
            <c:if test="${not empty errors.passwordHash}"><p class="error"><fmt:message key="${errors.passwordHash}" /></p></c:if>
        </div>
        <div class="field"><label for="lastPasswordChange"><fmt:message key="user.lastPasswordChange" /></label>
            <input id="lastPasswordChange" name="lastPasswordChange" type="datetime-local" required value="<c:out value='${user.lastPasswordChange}' />">
            <c:if test="${not empty errors.lastPasswordChange}"><p class="error"><fmt:message key="${errors.lastPasswordChange}" /></p></c:if>
        </div>
    </fieldset>
    <fieldset>
        <legend><fmt:message key="user.section.status" /></legend>
        <div class="field"><label for="active"><fmt:message key="user.active" /></label>
            <input type="checkbox" name="active" id="active" value="true" <c:if test="${user.active}">checked="checked"</c:if>>
            <c:if test="${not empty errors.active}"><p class="error"><fmt:message key="${errors.active}" /></p></c:if></div>
        <div class="field"><label for="accountNonLocked"><fmt:message key="user.accountNonLocked" /></label>
            <input type="checkbox" name="accountNonLocked" id="accountNonLocked" value="true" <c:if test="${user.accountNonLocked}">checked="checked"</c:if>>
            <c:if test="${not empty errors.accountNonLocked}"><p class="error"><fmt:message key="${errors.accountNonLocked}" /></p></c:if></div>
        <div class="field"><label for="failedLoginAttempts"><fmt:message key="user.failedLoginAttempts" /></label>
            <input type="number" name="failedLoginAttempts" id="failedLoginAttempts" min="0" step="1" required value="<c:out value='${user.failedLoginAttempts}' />">
            <c:if test="${not empty errors.failedLoginAttempts}"><p class="error"><fmt:message key="${errors.failedLoginAttempts}" /></p></c:if></div>
        <div class="field"><label for="emailVerified"><fmt:message key="user.emailVerified" /></label>
            <input type="checkbox" name="emailVerified" id="emailVerified" value="true" <c:if test="${user.emailVerified}">checked="checked"</c:if>>
            <c:if test="${not empty errors.emailVerified}"><p class="error"><fmt:message key="${errors.emailVerified}" /></p></c:if></div>
        <div class="field"><label for="mustChangePassword"><fmt:message key="user.mustChangePassword" /></label>
            <input type="checkbox" name="mustChangePassword" id="mustChangePassword" value="true" <c:if test="${user.mustChangePassword}">checked="checked"</c:if>>
            <c:if test="${not empty errors.mustChangePassword}"><p class="error"><fmt:message key="${errors.mustChangePassword}" /></p></c:if></div>
    </fieldset>
    <p><strong><fmt:message key="user.passwordExpiresAt" />:</strong>
        <fmt:message key="user.passwordExpiresAt.help" /></p>
    <div class="buttons"><button type="submit"><fmt:message key="user.save" /></button>
        <a href="${pageContext.request.contextPath}/users"><fmt:message key="user.cancel" /></a></div>
</form>

<%@ include file="/partials/footer.jsp" %>