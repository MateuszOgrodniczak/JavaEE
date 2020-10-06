<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:choose>
    <c:when test="${buildings != null && not empty buildings}">
        <div id="buildings">
            <script>
                var elmnt = document.getElementById("buildings");
                elmnt.scrollIntoView();
            </script>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.name"/></th>
                    <th><spring:message code="label.removed"/></th>
                    <th><spring:message code="label.address.street"/></th>
                    <th><spring:message code="label.address.city"/></th>
                    <th><spring:message code="label.address.postalCode"/></th>
                    <th><spring:message code="label.building.owner"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="building" items="${buildings}">
                    <tr class="table-white">
                        <td>${building.name}</td>
                        <td>${building.removed}</td>
                        <td>${building.address.street}</td>
                        <td>${building.address.city}</td>
                        <td>${building.address.postalCode}</td>
                        <td>${building.owner.username}</td>
                        <td>
                            <a href="/admin/saveBuilding/${building.id}">
                                <button type="button" class="btn btn-outline-info"><spring:message
                                        code="label.edit"/></button>
                            </a>
                        </td>
                        <td>
                            <c:if test="${building.removed}">
                                <a href="/admin/activateBuilding/${building.id}">
                                <button type="button" class="btn btn-outline-success"><spring:message
                                        code="label.activate"/></button>
                                </a>
                            </c:if>
                            <c:if test="${!building.removed}">
                                <a href="/admin/removeBuilding/${building.id}">
                                    <button type="button" class="btn btn-outline-danger"><spring:message
                                            code="label.remove"/></button>
                                </a>
                            </c:if>
                        </td>
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
