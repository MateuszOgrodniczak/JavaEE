<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8"%>

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

        <h1><spring:message code="label.building"/>: ${building.name}</h1>
        <hr class="my-4"/>

        <h3><spring:message code="label.building.info"/></h3>
        <form:form action="/housing/owner/saveBuilding" method="post" modelAttribute="building">
            <form:hidden path="id"/>
            <form:hidden path="owner.id"/>
            <form:hidden path="address.id"/>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="name"><spring:message
                        code="label.name"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="name"/></div>
                <div class="Column"><form:errors class="text-danger" path="name"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="address.street"><spring:message
                        code="label.address.street"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required"
                                                      path="address.street"/></div>
                <div class="Column"><form:errors class="text-danger" path="address.street"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="address.city"><spring:message
                        code="label.address.city"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required"
                                                      path="address.city"/></div>
                <div class="Column"><form:errors class="text-danger" path="address.city"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="address.postalCode"><spring:message
                        code="label.address.postalCode"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required"
                                                      path="address.postalCode"/></div>
                <div class="Column"><form:errors class="text-danger" path="address.postalCode"/></div>
            </div>
            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/housing/owner"><input type="button" class="btn btn-danger" style="width:115%"
                                                            value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>
        <hr class="my-4"/>

        <h3><spring:message code="label.building.apartments"/></h3>
        <br>
        <c:choose>
            <c:when test="${building.apartments != null && not empty building.apartments}">
                <table class="table table-hover">
                    <thead>
                    <tr class="table-primary">
                        <th><spring:message code="label.removed"/></th>
                        <th><spring:message code="label.apartment.room"/></th>
                        <th><spring:message code="label.name"/></th>
                        <th><spring:message code="label.tenant.username"/></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="apartment" items="${building.apartments}">
                        <tr class="table-white">
                        <td>${apartment.removed}</td>
                        <td>${apartment.roomNumber}</td>
                        <td>${apartment.building.name}</td>
                        <td>${apartment.tenant.username}</td>
                        <td>
                            <a href="/housing/owner/saveApartment/${building.id}/${apartment.id}">
                                <button type="button" class="btn btn-outline-info"><spring:message
                                        code="label.edit"/></button>
                            </a>
                        </td>
                        <td>
                        <c:if test="${apartment.removed == false}">
                                <a href="/housing/owner/removeApartment/${apartment.id}">
                                    <button type="button" class="btn btn-outline-danger"><spring:message
                                            code="label.remove"/></button>
                                </a>
                        </c:if>
                        </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <br>
                <div class="alert alert-danger custom-input">
                    <spring:message code="error.no_results"/>
                </div>
            </c:otherwise>
        </c:choose>
        <div class="Row">
            <div class="Column">
                <a href="/housing/owner/saveApartment/${building.id}" class="btn btn-success" style="width: 115%">+
                    <spring:message code="label.new"/></a>
            </div>
        </div>
        <hr class="my-4"/>

        <h3><spring:message code="label.tenants"/></h3>
        <c:choose>
            <c:when test="${tenants != null && not empty tenants}">
                <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                <th><spring:message code="label.account.user_name"/></th>
                <th><spring:message code="label.user.first_name"/></th>
                <th><spring:message code="label.user.surname"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tenant" items="${tenants}">
                    <tr class="table-white">
                        <td>${tenant.username}</td>
                        <td>${tenant.name}</td>
                        <td>${tenant.surname}</td>
                    </tr>
                </c:forEach>
                </tbody>
                </table>
            </c:when>
        </c:choose>
        <hr class="my-4"/>
        <br>
    </div>
</div>
</body>
</html>
