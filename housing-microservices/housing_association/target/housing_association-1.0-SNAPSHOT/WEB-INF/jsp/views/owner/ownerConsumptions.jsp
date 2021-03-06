<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>

<c:choose>
    <c:when test="${consumptions != null && not empty consumptions}">
        <div id="consumptions">
            <script>
                var elmnt = document.getElementById("consumptions");
                elmnt.scrollIntoView();
            </script>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.date"/></th>
                    <th><spring:message code="label.consumption.type"/></th>
                    <th><spring:message code="label.consumption.amount"/></th>
                    <th><spring:message code="label.consumption.charge"/></th>
                    <th><spring:message code="label.apartment"/></th>
                    <th><spring:message code="label.bill"/></th>
                    <th><spring:message code="label.consumption.add"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="consumption" items="${consumptions}">
                    <tr class="table-white">
                        <td>${consumption.date}</td>
                        <td>${consumption.type}</td>
                        <td><b>${consumption.amount}</b> ${consumption.type.unit}</td>
                        <td><b>${consumption.cost}</b></td>
                        <td>${consumption.apartment.id}</td>
                        <td>${consumption.bill.id}</td>
                        <c:choose>
                            <c:when test="${consumption.bill == null}">
                                <td>
                                    <a href="/housing/owner/addToBill/${consumption.id}">
                                        <button type="button" class="btn btn-outline-info"><spring:message
                                                code="label.add"/></button>
                                    </a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td></td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-danger custom-input">
            <spring:message code="error.no_results"/>
        </div>
    </c:otherwise>
</c:choose>
