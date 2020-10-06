<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" pageEncoding="UTF-8"%>

<html>
<head>
    <title><spring:message code="label.title"/></title>
    <link href="/resources/css/bootstrap-flatly.css" rel="stylesheet"
          type="text/css"/>
    <link rel="stylesheet" href="/resources/css/phoca-flags.css">
    <link rel="stylesheet" href="/resources/css/style.css">
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit"
            async defer>
    </script>
    <script type="text/javascript">
        var verifyCallback = function (response) {
            $.ajax({
                type: "GET",
                url: 'verifyRecaptcha?token=' + response,
                success: function (data) {
                    var verified = JSON.parse(data).success;
                    if (verified === true) {
                        $('#register-submit').removeAttr("disabled");
                    } else {
                        grecaptcha.reset();
                        $('#recaptcha-error').css('display', 'inline');
                        $('#recaptcha-error').html('Error: ' + JSON.parse(data)["error-codes"]);
                    }
                },
                error: function (e) {
                    console.log("[ERROR]: " + e)
                    grecaptcha.reset();
                    $('#recaptcha-error').css('display', 'inline');
                }
            });
        };
        var onloadCallback = function () {
            grecaptcha.render('recaptcha', {
                'sitekey': '6LeHkqAUAAAAANXWXGP_uErrIJwNdkLfkEV7R2Dg',
                'callback': verifyCallback
            });
        };
    </script>
</head>
<body>
<%@include file="navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container">
        <h1 class="display-3"><spring:message code="label.register.page"/></h1>
        <hr class="my-4"/>
        <form:form method="POST" action="/home/register" modelAttribute="registerForm" class="form-group">

            <%--User data--%>
            <h4><spring:message code="label.user.details"/></h4>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="name"><spring:message code="label.user.first_name"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="name"/></div>
                <div class="Column"><form:errors class="text-danger" path="name"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="surname"><spring:message code="label.user.surname"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="surname"/></div>
                <div class="Column"><form:errors class="text-danger" path="surname"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="email"><spring:message code="label.user.email"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="email"/></div>
                <div class="Column"><form:errors class="text-danger" path="email"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="telephone"><spring:message code="label.user.telephone"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="telephone"/></div>
                <div class="Column"><form:errors class="text-danger" path="telephone"/></div>
            </div>

            <hr class="my-4"/>
            <%--Credentials--%>
            <h4><spring:message code="label.account.details"/></h4>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="username"><spring:message code="label.account.user_name"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="username"/></div>
                <div class="Column"><form:errors class="text-danger" path="username"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="password"><spring:message code="label.account.password"/></form:label></div>
                <div class="Column-input"><form:password class="form-control" path="password"/></div>
                <div class="Column"><form:errors class="text-danger" path="password"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label"
                                                path="confirmedPassword"><spring:message code="label.account.password.confirm"/></form:label></div>
                <div class="Column-input"><form:password class="form-control" path="confirmedPassword"/></div>
                <div class="Column"><form:errors class="text-danger" path="confirmedPassword"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="role"><spring:message code="label.account.role"/></form:label></div>
                <div class="Column-input"><form:select class="custom-select" path="role" items="${roles}"/></div>
                <div class="Column"><form:errors class="text-danger" path="role"/></div>
            </div>

            <%--Address--%>
            <hr class="my-4"/>
            <h4><spring:message code="label.address.details"/></h4>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="street"><spring:message code="label.address.street"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="street"/></div>
                <div class="Column"><form:errors class="text-danger" path="street"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="city"><spring:message code="label.address.city"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="city"/></div>
                <div class="Column"><form:errors class="text-danger" path="city"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="postalCode"><spring:message code="label.address.postalCode"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" path="postalCode"/></div>
                <div class="Column"><form:errors class="text-danger" path="postalCode"/></div>
            </div>

            <%--Recaptcha--%>
            <hr class="my-4"/>
            <div class="Row">
                <div class="Column">
                    <div id="recaptcha"></div>
                </div>
            </div>
            <div id="recaptcha-error" class="Row" style="display: none;">
                <div class="Column">
                    <div class="text-danger">
                        <spring:message code="error.recaptcha"/></div>
                </div>
            </div>

            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/home"><input type="button" class="btn btn-danger" style="width:115%" value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input id="register-submit" type="submit" class="btn btn-success" style="width:115%" disabled value='<spring:message code="label.submit"/>'></div>
            </div>
        </form:form>
    </div>
</div>

</body>
</html>
