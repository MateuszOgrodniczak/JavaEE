<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

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
        <%@include file="../msg/paymentResponse.jsp" %>
        <%@include file="../msg/leaveApartmentResponse.jsp" %>

        <h1 class="display-3"><spring:message code="label.tenant.page"/></h1>
        <hr class="my-4"/>

        <h3><spring:message code="label.tenant.apartments"/></h3>
        <p><spring:message code="label.tenant.apartments.description"/></p>
        <form action="/housing/tenant/apartments" method="get">
            <div class="Row">
                <div class="Column" style="padding-top: 0px"><input type="submit" class="btn btn-info"
                                                                    value='<spring:message code="label.search"/>'/>
                </div>
            </div>
        </form>
        <c:if test="${apartmentsQueried == true}">
            <%@include file="tenantApartments.jsp" %>
        </c:if>
        <hr class="my-4"/>

        <h3><spring:message code="label.tenant.bills"/></h3>
        <p><spring:message code="label.tenant.bills.description"/></p>
        <form action="/housing/tenant/bills" method="get">
            <div class="Row">
                <div class="Column" style="padding-top: 0px"><input type="submit" class="btn btn-info"
                                           value='<spring:message code="label.search"/>'/>
                </div>
            </div>
        </form>
        <c:if test="${billsQueried == true}">
            <%@include file="tenantBills.jsp" %>
        </c:if>
        <hr class="my-4"/>

        <h3><spring:message code="label.requests"/></h3>
        <p><spring:message code="label.requests.description"/></p>
        <form action="/housing/userApplications/tenant/requests" method="get">
                <span class="custom-input">
                    <input type="text" class="form-control-inline"
                           placeholder='<spring:message code="label.request.subject"/>'/>
                </span>

            <div class="Row">
                <div class="Column">
                    <input type="submit" class="btn btn-info" style="width: 115%"
                           value='<spring:message code="label.search"/>'/>
                </div>
                <div class="Column">
                    <a href="/housing/userApplications/tenant/saveApplication" class="btn btn-success" style="width: 115%">
                        + <spring:message code="label.new"/></a>
                </div>
            </div>
        </form>
        <c:if test="${applicationsQueried == true}">
            <%@include file="../applications.jsp" %>
        </c:if>
        <hr class="my-4"/>
    </div>
</div>
</body>
</html>
