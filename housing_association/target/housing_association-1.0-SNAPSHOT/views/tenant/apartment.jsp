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

        <h1><strong><spring:message code="label.apartment"/></strong></h1>
        <hr class="my-4"/>

        <h3><spring:message code="label.apartment.info"/></h3>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.apartment.building"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${apartment.building.name}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.apartment.room"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${apartment.roomNumber}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.address.street"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${apartment.building.address.street}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.address.city"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${apartment.building.address.city}</div>
            </div>
        </div>
        <div class="Row">
            <div class="Column-label"><label class="col-form-label"><spring:message
                    code="label.address.postalCode"/></label></div>
            <div class="Column-input">
                <div class="form-control" readonly="readonly">${apartment.building.address.postalCode}</div>
            </div>
        </div>
        <%--Buttons--%>
        <div class="Row">
            <div class="Column"><a href="/tenant"><input type="button" class="btn btn-danger" style="width:115%"
                                                         value='<spring:message code="label.cancel"/>'></a></div>
            <div class="Column"><a href="/tenant/leaveApartment/${apartment.id}"><input type="button"
                                                                                        class="btn btn-warning"
                                                                                        style="width:115%"
                                                                                        value='<spring:message code="label.leave"/>'></a>
            </div>
        </div>
        <hr class="my-4"/>

        <h3><spring:message code="label.apartment.consumptions"/></h3>
        <%@include file="tenantConsumptions.jsp" %>
            <form action="/tenant/getConsumptions/${apartment.id}" method="get">
                    <input type="submit" class="btn btn-success" value='<spring:message
                        code="label.consumptions.get"/>'/>
                <span>
                    <select name="type" class="form-control-inline" style="display: inline">
                        <option value="ELECTRICITY"><spring:message code="label.consumption.electricity"/></option>
                        <option value="GAS"><spring:message code="label.consumption.gas"/></option>
                        <option value="WATER"><spring:message code="label.consumption.water"/></option>
                        <option value="WASTE"><spring:message code="label.consumption.waste"/></option>
                        <option value="HEATING"><spring:message code="label.consumption.heating"/></option>
                        <option value="RENOVATION"><spring:message code="label.consumption.renovation"/></option>
                        <option value=""><spring:message code="label.all"/></option>
                    </select>
                </span>
                <span>
                    <input name="month" type="month" class="form-control-inline" placeholder="MONTH" />
                </span>
            </form>
        <hr class="my-4"/>

        <h3><spring:message code="label.apartment.bills"/></h3>
        <%@include file="tenantBills.jsp" %>
    </div>
</div>
</body>
</html>
