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

        <h1><spring:message code="label.apartment"/></h1>
        <hr class="my-4"/>

        <h3><spring:message code="label.apartment.edit"/></h3>
        <form:form action="/owner/saveApartment" method="post" modelAttribute="apartment">
            <form:hidden path="id"/>
            <form:hidden path="building.id"/>
            <form:hidden path="tenant.id"/>
            <form:hidden path="building.name"/>
            <form:hidden path="building.address.street"/>
            <form:hidden path="building.address.city"/>
            <form:hidden path="building.address.postalCode"/>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="removed"><spring:message
                        code="label.removed"/></form:label></div>
                <div class="Column-checkbox"><form:checkbox class="form-check-input" path="removed"/></div>
                <div class="Column"><form:errors class="text-danger" path="removed"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="roomNumber"><spring:message
                        code="label.apartment.room"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="roomNumber"/></div>
                <div class="Column"><form:errors class="text-danger" path="roomNumber"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="tenant.name"><spring:message
                        code="label.tenant.username"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="tenant.username"/></div>
                <div class="Column"><form:errors class="text-danger" path="tenant.username"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="building.name"><spring:message
                        code="label.apartment.building"/></form:label></div>
                <div class="Column-input">
                    <div class="form-control" readonly="readonly">${apartment.building.name}</div>
                </div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label"
                                                      path="building.address.street"><spring:message
                        code="label.address.street"/></form:label></div>
                <div class="Column-input">
                    <div class="form-control" readonly="readonly">${apartment.building.address.street}</div>
                </div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label"
                                                      path="building.address.city"><spring:message
                        code="label.address.city"/></form:label></div>
                <div class="Column-input">
                    <div class="form-control" readonly="readonly">${apartment.building.address.city}</div>
                </div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label"
                                                      path="building.address.postalCode"><spring:message
                        code="label.address.postalCode"/></form:label></div>
                <div class="Column-input">
                    <div class="form-control" readonly="readonly">${apartment.building.address.postalCode}</div>
                </div>
            </div>
            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/owner/saveBuilding/${apartment.building.id}"><input type="button"
                                                                                                  class="btn btn-danger"
                                                                                                  style="width:115%"
                                                                                                  value='<spring:message code="label.cancel"/>'></a>
                </div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>
        <hr class="my-4"/>
    </div>
</div>
</body>
</html>
