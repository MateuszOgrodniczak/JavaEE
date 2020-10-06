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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<%@include file="../navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <%@include file="../msg/removeItemResponse.jsp" %>
        <%@include file="../msg/addToBillResponse.jsp" %>
        <%@include file="../msg/acceptBillResponse.jsp" %>


        <h1 class="display-3"><spring:message code="label.owner.page"/></h1>
        <hr class="my-4"/>

        <div id="buildings">
            <h3><spring:message code="label.owner.buildings"/></h3>
            <p><spring:message code="label.owner.buildings.description"/></p>
            <form action="/housing/owner/buildings" method="get">
                <span><input name="name" type="text" class="form-control-inline" style="display: inline"
                             placeholder='<spring:message code="label.name"/>'/>
                </span>
                <div class="Row">
                    <div class="Column"><input type="submit" class="btn btn-info"
                                               value='<spring:message code="label.search"/>'/>
                    </div>
                </div>
            </form>
            <c:if test="${buildingsQueried == true}">
                <%@include file="ownerBuildings.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>

        <div id="bills">
            <h3><spring:message code="label.owner.bills"/></h3>
            <p><spring:message code="label.owner.bills.description"/></p>
            <form action="/housing/owner/bills" method="get">
                <div class="Row">
                    <div class="Column"><input type="submit" class="btn btn-info"
                                               value='<spring:message code="label.search"/>'/>
                    </div>
                </div>
            </form>
            <c:if test="${billsQueried == true}">
                <%@include file="ownerBills.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>

        <div id="consumptions">
            <h3><spring:message code="label.owner.consumptions"/></h3>
            <p><spring:message code="label.owner.consumptions.description"/></p>
            <form action="/housing/owner/consumptions" method="get">
                <div class="Row">
                    <div class="Column">
                        <input type="submit" class="btn btn-info" style="width: 115%"
                               value='<spring:message code="label.search"/>'/>
                    </div>
                </div>
            </form>
            <c:if test="${consumptionsQueried == true}">
                <%@include file="ownerConsumptions.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>

        <h3><spring:message code="label.requests"/></h3>
        <p><spring:message code="label.requests.description"/></p>
        <form action="/housing/userApplications/owner/applications" method="get">
                <span class="custom-input">
                    <input type="text" name="title" class="form-control-inline"
                           placeholder='<spring:message code="label.request.subject"/>'/>
                </span>

            <div class="Row">
                <div class="Column">
                    <input type="submit" class="btn btn-info" style="width: 115%"
                           value='<spring:message code="label.search"/>'/>
                </div>
                <div class="Column">
                    <a href="/housing/userApplications/owner/saveApplication" class="btn btn-success" style="width: 115%">
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
