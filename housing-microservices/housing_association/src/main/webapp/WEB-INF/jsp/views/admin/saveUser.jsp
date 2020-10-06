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
<%@include file="../navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <%@include file="../msg/saveItemResponse.jsp" %>

        <h1 class="display-3"><spring:message code="label.admin.save.user"/></h1>
        <hr class="my-4"/>

        <form:form action="/housing/admin/saveUser" method="POST" modelAttribute="user">
            <form:hidden path="id"/>
            <div class="Row">
                <div class="Column-label"><spring:message
                        code="label.account.user_name"/></div>
                <div class="Column-input">
                    <div class="form-control" readonly="readonly">${user.username}</div>
                </div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="enabled"><spring:message
                        code="label.account.enabled"/></form:label></div>
                <div class="Column-checkbox"><form:checkbox class="form-check-input" path="enabled"/></div>
                <div class="Column"><form:errors class="text-danger" path="enabled"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="removed"><spring:message
                        code="label.account.removed"/></form:label></div>
                <div class="Column-checkbox"><form:checkbox class="form-check-input" path="removed"/></div>
                <div class="Column"><form:errors class="text-danger" path="removed"/></div>
            </div>
            <hr class="my-4"/>

            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/housing/admin"><input type="button" class="btn btn-danger" style="width:115%"
                                                            value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>
