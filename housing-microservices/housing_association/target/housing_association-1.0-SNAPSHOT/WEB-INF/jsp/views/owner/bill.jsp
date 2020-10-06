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

        <h1><spring:message code="label.bill.details"/></h1>
        <hr class="my-4"/>

        <div class="Row">
            <div class="Column-label"><label class=""><spring:message
                    code="label.building.name"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.apartment.building.name}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class=""><spring:message
                    code="label.apartment.room"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.apartment.roomNumber}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><spring:message
                    code="label.bill.accepted"/></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.accepted}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.bill.date.creation"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.dateOfCreation}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.bill.date.payment"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.dateOfPayment}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.bill.cost"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${bill.cost}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column"><a href="${pdfServiceUrl}/${bill.id}"><input type="button" class="btn btn-success fa fa-file-pdf-o" style="width:115%; font-size:24px"
                                                              value='<spring:message code="label.bill.export"/>'></a></div>
        </div>

        <br>
        <h3><spring:message code="label.consumptions"/></h3>
        <c:choose>
            <c:when test="${bill.consumptions != null && not empty bill.consumptions}">
                <table class="table table-hover">
                    <thead>
                    <tr class="table-primary">
                        <th><spring:message code="label.date"/></th>
                        <th><spring:message code="label.consumption.type"/></th>
                        <th><spring:message code="label.consumption.amount"/></th>
                        <th><spring:message code="label.consumption.charge"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="consumption" items="${bill.consumptions}">
                        <tr class="table-white">
                            <td>${consumption.date}</td>
                            <td>${consumption.type}</td>
                            <td><b>${consumption.amount}</b> ${consumption.type.unit}</td>
                            <td><b>${consumption.cost}</b> zl</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger custom-input">
                    <spring:message code="error.no_results"/>
                </div>
            </c:otherwise>
        </c:choose>
        <%--Buttons--%>
        <div class="Row">
            <div class="Column"><a href="/housing/${userType}"><input type="button" class="btn btn-danger" style="width:115%"
                                                         value='<spring:message code="label.cancel"/>'></a></div>
        </div>
        <hr class="my-4"/>
    </div>
</div>
</body>
</html>
