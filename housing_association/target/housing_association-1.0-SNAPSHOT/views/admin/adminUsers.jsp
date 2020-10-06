<c:choose>
    <c:when test="${users != null && not empty users}">
        <div id="users">
            <script>
                var elmnt = document.getElementById("users");
                elmnt.scrollIntoView();
            </script>
            <table class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.account.user_name"/></th>
                    <th><spring:message code="label.account.enabled"/></th>
                    <th><spring:message code="label.account.removed"/></th>
                    <th><spring:message code="label.user.first_name"/></th>
                    <th><spring:message code="label.user.surname"/></th>
                    <th><spring:message code="label.account.role"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr class="table-white">
                        <td>${user.username}</td>
                        <td>${user.enabled}</td>
                        <td>${user.removed}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.role}</td>
                        <td>
                            <a href="/admin/saveUser/${user.id}">
                                <button type="button" class="btn btn-outline-info"><spring:message
                                        code="label.edit"/></button>
                            </a>
                        </td>
                        <td>
                            <c:if test="${user.removed}">
                                <a href="/admin/activateUser/${user.id}">
                                    <button type="button" class="btn btn-outline-success"><spring:message
                                            code="label.activate"/></button>
                                </a>
                            </c:if>
                            <c:if test="${!user.removed}">
                                <a href="/admin/removeUser/${user.id}">
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
