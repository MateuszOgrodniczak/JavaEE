<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<hr>
<h4>&#9993 <spring:message code="label.request.received"/></h4>
<c:choose>
    <c:when test="${applicationsReceived != null && not empty applicationsReceived}">
        <div id="applicationsReceived">
            <script>
                var elmnt = document.getElementById("applicationsReceived");
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
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="application" items="${applicationsReceived}">
                    <tr class="table-white">
                        <td>${application.title}</td>
                        <td>${application.body}</td>
                        <td>${application.creationDate}</td>
                        <td>${application.removed}</td>
                        <td>${application.issuerNameAndSurname} (${application.issuerLogin})</td>
                        <td><a href="/housing/userApplications/export/${application.id}">
                            <i class="fa fa-file-pdf-o" style="font-size:32px;color:red"></i>
                        </a>
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

<hr>
<h4>&#9993 <spring:message code="label.request.sent"/></h4>
<c:choose>
    <c:when test="${applicationsSent != null && not empty applicationsSent}">
        <div id="applicationsSent">
            <script>
                var elmnt = document.getElementById("applicationsSent");
                elmnt.scrollIntoView();
            </script>
            <br>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.request.subject"/></th>
                    <th><spring:message code="label.request.date"/></th>
                    <th><spring:message code="label.removed"/></th>
                    <th><spring:message code="label.request.recipients"/></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="application" items="${applicationsSent}">
                    <tr class="table-info">
                        <td>${application.title}</td>
                        <td>${application.creationDate}</td>
                        <td>${application.removed}</td>
                        <td>
                            <c:forEach items="${application.recipients}" var="recipient" varStatus="status">
                                <c:choose>
                                    <c:when test="status == ${fn:length(application.recipients) - 1}">${recipient}, </c:when>
                                    <c:otherwise>${recipient}</c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </td>
                        <td><a href="/housing/userApplications/export/${application.id}">
                                <i class="fa fa-file-pdf-o" style="font-size:32px;color:red"></i>
                            </a>
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
