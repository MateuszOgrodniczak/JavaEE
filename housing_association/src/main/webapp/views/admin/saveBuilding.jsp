<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

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
<%@include file="../navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <%@include file="../msg/saveItemResponse.jsp" %>
        <h1 class="display-3"><spring:message code="label.admin.save.building"/></h1>
        <hr class="my-4"/>

        <form:form action="/admin/saveBuilding" method="POST" modelAttribute="building">
            <form:hidden path="id"/>
            <form:hidden path="address.id"/>
            <form:hidden path="address.street"/>
            <form:hidden path="address.city"/>
            <form:hidden path="address.postalCode"/>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="name"><spring:message
                        code="label.name"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="name"/></div>
                <div class="Column"><form:errors class="text-danger" path="name"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="owner.username"><spring:message
                        code="label.building.owner"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required"
                                                      path="owner.username"/></div>
                <div class="Column"><form:errors class="text-danger" path="owner.username"/></div>
            </div>
            <hr class="my-4"/>

            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/admin"><input type="button" class="btn btn-danger" style="width:115%"
                                                            value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>
