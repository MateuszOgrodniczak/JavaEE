<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${bills != null && not empty bills}">
        <table class="table table-hover">
            <thead>
            <tr class="table-primary">
                <th><spring:message code="label.apartment.room"/></th>
                <th><spring:message code="label.building"/></th>
                <th><spring:message code="label.bill.date.creation"/></th>
                <th><spring:message code="label.bill.date.payment"/></th>
                <th><spring:message code="label.bill.cost"/></th>
                <th><spring:message code="label.bill.accepted"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="bill" items="${bills}">
                <c:choose>
                    <c:when test="${bill.accepted}">
                        <tr class="table-success">
                    </c:when>
                    <c:otherwise>
                        <tr class="table-white">
                    </c:otherwise>
                </c:choose>
                <td>${bill.apartment.building.name}</td>
                <td>${bill.apartment.roomNumber}</td>
                <td>${bill.dateOfCreation}</td>
                <td>${bill.dateOfPayment}</td>
                <td>${bill.cost}</td>
                <td>${bill.accepted}</td>
                <td>
                    <a href="/owner/billInfo/${bill.id}">
                        <button type="button" class="btn btn-outline-info"><spring:message code="label.info"/></button>
                    </a>
                </td>
                <c:choose>
                    <c:when test="${bill.accepted}">
                        <td>
                            <a href="/owner/removeBill/${bill.id}">
                                <button type="button" class="btn btn-outline-danger"><spring:message
                                        code="label.remove"/></button>
                            </a>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <a href="/owner/acceptBill/${bill.id}">
                                <button type="button" class="btn btn-outline-success"><spring:message
                                        code="label.accept"/></button>
                            </a>
                        </td>
                    </c:otherwise>
                </c:choose>
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