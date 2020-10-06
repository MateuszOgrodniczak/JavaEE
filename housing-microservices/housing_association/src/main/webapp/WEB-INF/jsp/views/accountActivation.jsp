<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<html>
<head>
    <title><spring:message code="label.title"/></title>
    <link href="/housing/resources/css/bootstrap-flatly.css" rel="stylesheet"
          type="text/css"/>
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/housing/resources/css/phoca-flags.css">
    <link rel="stylesheet" href="/housing/resources/css/style.css">
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <h1><spring:message code="label.activation"/></h1>
        <c:if test="${linkSent != null}">
            <div class="alert alert-success">
                    ${linkSent}
            </div>
        </c:if>
        <c:if test="${sendingError != null}">
            <div class="alert alert-danger">
                ${sendingError}
            </div>
        </c:if>
        <c:if test="${accountActivated != null}">
            <div class="alert-success">
                <h4>${accountActivated}</h4>
                <a href="/housing/home/login"><spring:message code="label.login"/></a>
            </div>
        </c:if>
        <c:if test="${invalidToken != null}">
            <h4 class="alert-danger">${invalidToken}</h4>
        </c:if>
    </div>
</div>
</body>
</html>
