<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<c:choose>
    <c:when test="${apartments != null && not empty apartments}">
        <div id="apartments">
            <script>
                var elmnt = document.getElementById("apartments");
                elmnt.scrollIntoView();
            </script>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.apartment.room"/></th>
                    <th><spring:message code="label.apartment.building"/></th>
                    <th><spring:message code="label.address.street"/></th>
                    <th><spring:message code="label.address.city"/></th>
                    <th><spring:message code="label.address.postalCode"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="apartment" items="${apartments}">
                    <tr class="table-white">
                        <td>${apartment.roomNumber}</td>
                        <td>${apartment.building.name}</td>
                        <td>${apartment.building.address.street}</td>
                        <td>${apartment.building.address.city}</td>
                        <td>${apartment.building.address.postalCode}</td>
                        <td>
                            <a href="/housing/tenant/apartments/${apartment.id}">
                                <button type="button" class="btn btn-outline-info"><spring:message
                                        code="label.info"/></button>
                            </a>
                        </td>
                        <td>
                            <a href="/housing/tenant/leaveApartment/${apartment.id}">
                                <button type="button" class="btn btn-outline-danger"><spring:message
                                        code="label.leave"/></button>
                            </a>
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
