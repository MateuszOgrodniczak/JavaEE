<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<hr>
<h4>&#9993 <spring:message code="label.request.received"/></h4>
<c:choose>
    <c:when test="${requestsReceived != null && not empty requestsReceived}">
        <div id="requestsReceived">
            <script>
                var elmnt = document.getElementById("requestsReceived");
                elmnt.scrollIntoView();
            </script>

            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.request.subject"/></th>
                    <th><spring:message code="label.request.text"/></th>
                    <th><spring:message code="label.request.date"/></th>
                    <th><spring:message code="label.removed"/></th>
                    <th><spring:message code="label.request.sender"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="request" items="${requestsReceived}">
                    <tr class="table-white">
                        <td>${request.subject}</td>
                        <td>${request.text}</td>
                        <td>${request.sendingDate.toLocalDate()} ${request.sendingDate.toLocalTime().hour}:${request.sendingDate.toLocalTime().minute}</td>
                        <td>${request.removed}</td>
                        <td>${request.sender.username}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <br>
        <div class="alert alert-danger custom-input">
            <spring:message code="error.no_results"/>
        </div>
    </c:otherwise>
</c:choose>

<hr>
<h4>&#9993 <spring:message code="label.request.sent"/></h4>
<c:choose>
    <c:when test="${requestsSent != null && not empty requestsSent}">
        <div id="requestsSent">
            <script>
                var elmnt = document.getElementById("requestsSent");
                elmnt.scrollIntoView();
            </script>
            <br>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.request.subject"/></th>
                    <th><spring:message code="label.request.text"/></th>
                    <th><spring:message code="label.request.date"/></th>
                    <th><spring:message code="label.removed"/></th>
                    <th><spring:message code="label.request.recipients"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="request" items="${requestsSent}">
                    <tr class="table-info">
                        <td>${request.subject}</td>
                        <td>${request.text}</td>
                        <td>${request.sendingDate.toLocalDate()} ${request.sendingDate.toLocalTime().hour}:${request.sendingDate.toLocalTime().minute}</td>
                        <td>${request.removed}</td>
                        <td>
                            <c:forEach items="${request.recipients}" var="recipient" varStatus="status">
                                <c:choose>
                                    <c:when test="status == ${fn:length(request.recipients) - 1}">${recipient.username}, </c:when>
                                    <c:otherwise>${recipient.username}</c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <br>
        <div class="alert alert-danger custom-input">
            <spring:message code="error.no_results"/>
        </div>
    </c:otherwise>
</c:choose>