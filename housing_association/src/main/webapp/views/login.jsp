<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" pageEncoding="UTF-8"%>

<html>
<head>
    <title><spring:message code="label.title"/></title>
    <link href="/resources/css/bootstrap-flatly.css" rel="stylesheet"
          type="text/css"/>
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/resources/css/phoca-flags.css">
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container">
        <h1 class="display-3"><spring:message code="label.login.page"/></h1>
        <hr class="my-4"/>


        <c:if test="${loginError != null}">
            <div class="alert alert-dismissible alert-danger">
                <h4>${loginError}
                    <c:if test="${resendLinkURL != null}">
                        <a href="${resendLinkURL}"><b>Link</b></a>
                    </c:if>
                </h4>
            </div>
        </c:if>


        <form:form method="POST" action="login" modelAttribute="loginForm">

            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="username"><spring:message
                        code="label.account.user_name"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="username" /></div>
                <div class="Column"><form:errors class="text-danger" path="username"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="password"><spring:message
                        code="label.account.password"/></form:label></div>
                <div class="Column-input"><form:password class="form-control" required="required"
                                                         path="password" /></div>
                <div class="Column"><form:errors class="text-danger" path="password"/></div>
            </div>
            <hr class="my-4"/>
            <div class="Row">
                <div class="Column"><a href="/home"><input type="button" class="btn btn-danger" style="width:115%"
                                                           value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input id="register-submit" type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>
