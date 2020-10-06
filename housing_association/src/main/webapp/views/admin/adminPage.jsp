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
        <%@include file="../msg/removeItemResponse.jsp"%>
        <h1 class="display-3"><spring:message code="label.admin.page"/></h1>
        <hr class="my-4"/>

        <div id="users">
            <h3><spring:message code="label.admin.users"/></h3>
            <p><spring:message code="label.admin.users.description"/></p>
            <form action="/admin/users" method="get">
                <span><input name="username" type="text" class="form-control-inline" style="display: inline"
                             placeholder='<spring:message code="label.username"/>'/>
                    <select name="suffix" class="form-control-inline" style="display: inline"
                            placeholder='<spring:message code="label.username"/>'>
                        <option value=""><spring:message code="label.table"/></option>
                        <option value=".json">.json</option>
                        <option value=".xml">.xml</option>
                    </select>
                </span>
                <span>
                    <select name="removed" class="form-control-inline" style="display: inline">
                        <option value=""><spring:message code="label.all"/></option>
                        <option value="false"><spring:message code="label.removed.not"/></option>
                        <option value="true"><spring:message code="label.removed.only"/></option>
                    </select>
                </span>
                <div class="Row">
                    <div class="Column"><input type="submit" class="btn btn-info"
                                               value='<spring:message code="label.search"/>'/>
                    </div>
                </div>
            </form>
            <c:if test="${usersQueried == true}">
                <%@include file="adminUsers.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>

        <div id="buildings">
            <h3><spring:message code="label.admin.buildings"/></h3>
            <p><spring:message code="label.admin.buildings.description"/></p>
            <form action="/admin/buildings" method="get">
                <span class="custom-input">
                    <input type="text" class="form-control-inline"
                           placeholder='<spring:message code="label.name"/>'/>
                    <select name="suffix" class="form-control-inline"
                            placeholder='<spring:message code="label.name"/>'>
                        <option value=""><spring:message code="label.table"/></option>
                        <option value=".json">.json</option>
                        <option value=".xml">.xml</option>
                    </select>
                </span>
                <span>
                    <select name="removed" class="form-control-inline" style="display: inline">
                        <option value=""><spring:message code="label.all"/></option>
                        <option value="false"><spring:message code="label.removed.not"/></option>
                        <option value="true"><spring:message code="label.removed.only"/></option>
                    </select>
                </span>
                <div class="Row">
                    <div class="Column">
                        <input type="submit" class="btn btn-info" style="width: 115%"
                               value='<spring:message code="label.search"/>'/>
                    </div>
                    <div class="Column">
                        <a href="/admin/saveBuilding" class="btn btn-success" style="width: 115%">
                            + <spring:message code="label.new"/></a>
                    </div>
                </div>
            </form>
            <c:if test="${buildingsQueried == true}">
                <%@include file="adminBuildings.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>

        <div id="requests">
            <h3><spring:message code="label.admin.requests"/></h3>
            <p><spring:message code="label.admin.requests.description"/></p>
            <form action="/admin/requests" method="get">
                <span class="custom-input">
                    <input type="text" class="form-control-inline"
                                                  placeholder='<spring:message code="label.request.subject"/>'/>
                    <select name="suffix" class="form-control-inline"
                        placeholder='<spring:message code="label.request.subject"/>'>
                        <option value=""><spring:message code="label.table"/></option>
                        <option value=".json">.json</option>
                        <option value=".xml">.xml</option>
                    </select>
                </span>
                <span>
                    <select name="removed" class="form-control-inline" style="display: inline">
                        <option value=""><spring:message code="label.all"/></option>
                        <option value="false"><spring:message code="label.removed.not"/></option>
                        <option value="true"><spring:message code="label.removed.only"/></option>
                    </select>
                </span>
                <div class="Row">
                    <div class="Column">
                        <input type="submit" class="btn btn-info" style="width: 115%"
                               value='<spring:message code="label.search"/>'/>
                    </div>
                    <div class="Column">
                        <a href="/userRequests/admin/saveRequest" class="btn btn-success" style="width: 115%">
                            + <spring:message code="label.new"/></a>
                    </div>
                </div>
            </form>
            <c:if test="${requestsQueried == true}">
                <%@include file="../requests.jsp" %>
            </c:if>
            <hr class="my-4"/>
        </div>
    </div>
</div>
</body>
</html>
