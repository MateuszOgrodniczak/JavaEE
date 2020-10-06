<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title><spring:message code="label.title"/></title>
    <link href="/resources/css/bootstrap-flatly.css" rel="stylesheet"
          type="text/css"/>
    <link href="/resources/css/multi-select.css" rel="stylesheet" type="text/css" />
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/resources/js/jquery-multiselect.js" type="text/javascript"></script>
    <script src="/resources/js/jquery-quicksearch.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/resources/css/phoca-flags.css">
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>

<%@include file="navbar.jsp" %>
<div class="jumbotron">
    <div class="custom-container-big">
        <%@include file="msg/sendRequestResponse.jsp" %>
        <h1 class="display-3"><spring:message code="label.request.send"/></h1>
        <hr class="my-4"/>

        <form:form action="/userRequests/${userRole}/saveRequest" method="POST" modelAttribute="request">
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="subject"><spring:message
                        code="label.request.subject"/></form:label></div>
                <div class="Column-input"><form:input class="form-control" required="required" path="subject"/></div>
                <div class="Column"><form:errors class="text-danger" path="subject"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="text"><spring:message
                        code="label.request.text"/></form:label></div>
                <div class="Column-input-long"><form:textarea class="form-control" path="text"/></div>
            </div>
            <div class="Row">
                <div class="Column-label"><form:label class="col-form-label" path="recipients"><spring:message
                        code="label.request.recipients"/></form:label></div>
                <div class="Column-input">
                    <select id="custom-headers" class="searchable form-control" name="recipientNames[]" multiple="multiple">
                        <c:forEach items="${usernames}" var="username">
                            <option value="${username}">${username}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="Column"><form:errors class="text-danger" path="recipients"/></div>
            </div>
            <hr class="my-4"/>

            <%--Buttons--%>
            <div class="Row">
                <div class="Column"><a href="/${userRole}"><input type="button" class="btn btn-danger" style="width:115%"
                                                            value='<spring:message code="label.cancel"/>'></a></div>
                <div class="Column"><input type="submit" class="btn btn-success" style="width:115%"
                                           value='<spring:message code="label.send"/>'></div>
            </div>
        </form:form>
    </div>
</div>

<script>
    $('.searchable').multiSelect({
        selectableHeader: "<input type='text' class='search-input form-control' autocomplete='on' placeholder='search'>",
        selectionHeader: "<input type='text' class='search-input form-control' autocomplete='on' placeholder='recipients'>",
        afterInit: function (ms) {
            var that = this,
                $selectableSearch = that.$selectableUl.prev(),
                $selectionSearch = that.$selectionUl.prev(),
                selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
                selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

            that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
                .on('keydown', function (e) {
                    if (e.which === 40) {
                        that.$selectableUl.focus();
                        return false;
                    }
                });

            that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
                .on('keydown', function (e) {
                    if (e.which == 40) {
                        that.$selectionUl.focus();
                        return false;
                    }
                });
        },
        afterSelect: function () {
            this.qs1.cache();
            this.qs2.cache();
        },
        afterDeselect: function () {
            this.qs1.cache();
            this.qs2.cache();
        }
    });
</script>
</body>
</html>
