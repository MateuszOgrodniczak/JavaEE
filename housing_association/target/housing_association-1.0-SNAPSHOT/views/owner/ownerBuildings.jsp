<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${buildings != null && not empty buildings}">
        <table class="table table-hover">
            <thead>
            <tr class="table-primary">
                <th><spring:message code="label.name"/></th>
                <th><spring:message code="label.address.street"/></th>
                <th><spring:message code="label.address.city"/></th>
                <th><spring:message code="label.address.postalCode"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="building" items="${buildings}">
                <tr class="table-white">
                    <td>${building.name}</td>
                    <td>${building.address.street}</td>
                    <td>${building.address.city}</td>
                    <td>${building.address.postalCode}</td>
                    <td>
                        <a href="/owner/saveBuilding/${building.id}">
                            <button type="button" class="btn btn-outline-info"><spring:message
                                    code="label.edit"/></button>
                        </a>
                    </td>
                    <td>
                        <a href="/owner/removeBuilding/${building.id}">
                            <button type="button" class="btn btn-outline-danger"><spring:message
                                    code="label.remove"/></button>
                        </a>
                    </td>
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