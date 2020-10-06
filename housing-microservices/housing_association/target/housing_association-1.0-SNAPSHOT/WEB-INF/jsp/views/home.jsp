<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" pageEncoding="UTF-8"%>

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
<%@include file="navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <h1 class="display-3"><spring:message code="label.home_page"/></h1>
        <hr class="my-4"/>
        <h4><spring:message code="label.home.description"/></h4>
        <hr class="my-4"/>
        <br>
        <h3>1) <spring:message code="label.admin"/></h3>
        <h4><spring:message code="label.home.admin"/></h4>
        <hr class="my-4"/>
        <br>
        <h3>2) <spring:message code="label.owner"/></h3>
        <h4><spring:message code="label.home.owner"/></h4>
        <hr class="my-4"/>
        <br>
        <h3>3) <spring:message code="label.tenant"/></h3>
        <h4><spring:message code="label.home.tenant"/></h4>
        <hr class="my-4"/>
    </div>
</div>
</body>
</html>
