<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <a class="navbar-brand" href="/housing/home"><spring:message code="label.home"/></a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarColor01"
            aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarColor01">
        <ul class="navbar-nav mr-auto">
            <sec:authorize access="isAuthenticated()">
                <li class="nav-item">
                    <sec:authentication var="authority" property="principal.authorities[0]"/>
                    <c:set var="authorityLowerCase" value="${fn:toLowerCase(authority)}"/>
                    <a class="nav-link" href="/housing/${authorityLowerCase}"><spring:message
                            code="authority.${authorityLowerCase}.page"/></a>
                </li>
            </sec:authorize>
            <li class="nav-item"><span class="nav-link">|</span>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="?lang=en" style="float: left;"><spring:message code="label.lang.en"/></a>
                <div class="phoca-flagbox" style="float: left; height: 40px; width: 35px;">
                    <span class="phoca-flag gb" style="float: left; height: 20px; width: 35px"></span>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="?lang=pl" style="float: left;"><spring:message code="label.lang.pl"/></a>
                <div class="phoca-flagbox" style="float: left; height: 40px; width: 35px;">
                    <span class="phoca-flag pl" style="float: left; height: 20px; width: 35px"></span>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="?lang=es" style="float: left;"><spring:message code="label.lang.es"/></a>
                <div class="phoca-flagbox" style="float: left; height: 40px; width: 35px;">
                    <span class="phoca-flag es" style="float: left; height: 20px; width: 35px"></span>
                </div>
            </li>
        </ul>

        <sec:authorize access="isAnonymous()">
            <a class="navbar-brand" href="/housing/home/login"><spring:message code="label.login"/></a>
            <a class="navbar-brand" href="/housing/home/register"><spring:message code="label.register"/></a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <sec:authentication var="username" property="principal.username"/>
            <a class="navbar-brand" href="/housing/home/account/${username}">${username}</a>
            <a class="navbar-brand" href="/housing/home/logout"><spring:message code="label.logout"/></a>
        </sec:authorize>
    </div>
</nav>
