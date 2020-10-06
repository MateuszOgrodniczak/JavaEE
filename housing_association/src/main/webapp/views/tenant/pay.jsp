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
        <%@include file="../msg/paymentResponse.jsp" %>

        <h1 class="display-3"><spring:message code="label.bill"/></h1>
        <hr class="my-4"/>

        <h3 class="display-3"><spring:message code="label.bill.pay"/></h3>
        <form:form action="/tenant/payBill" method="post" modelAttribute="bill">
            <div class="Row">
                <div class="Column-label col-form-label"><spring:message
                        code="label.apartment.room"/></div>
                <div class="Column-input form-control">${bill.apartment.roomNumber}</div>
            </div>
            <div class="Row">
                <div class="Column-label col-form-label"><spring:message
                        code="label.bill.accepted"/></div>
                <div class="Column-input form-control">${bill.accepted}</div>
            </div>
            <div class="Row">
                <div class="Column-label col-form-label"><spring:message
                        code="label.bill.date.creation"/></div>
                <div class="Column-input">${bill.dateOfCreation}</div>
            </div>
            <div class="Row">
                <div class="Column-label col-form-label"><spring:message
                        code="label.bill.date.payment"/></div>
                <div class="Column-input">${bill.dateOfPayment}</div>
            </div>
            <c:forEach items="${bill.consumptions}" var="consumption">

            </c:forEach>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="payed"><spring:message
                        code="label.bill.payed"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="payed"/></div>
                <div class="Column"><form:errors class="text-danger" path="payed"/></div>
            </div>
            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/tenant/"><input type="button" class="btn btn-danger" style="width:115%"
                                                                                                  value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>
        <hr class="my-4"/>
    </div>
</div>
</body>
</html>
