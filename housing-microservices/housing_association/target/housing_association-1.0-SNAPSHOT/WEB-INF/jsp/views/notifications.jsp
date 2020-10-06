<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<hr>
<h4>&#9993 <spring:message code="label.notifications"/></h4>
<c:choose>
    <c:when test="${notifications != null && not empty notifications}">
        <div id="notifications">
            <script>
                var elmnt = document.getElementById("notifications");
                elmnt.scrollIntoView();
            </script>

            <table id="notificationTable" class="table table-hover">
                <thead>
                <tr class="table-primary">
                    <th><spring:message code="label.notification.body"/></th>
                    <th><spring:message code="label.notification.date"/></th>
                    <th><spring:message code="label.notification.seen"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="notification" items="${notifications}">
                    <c:choose>
                    <c:when test="${notification.seen}">
                        <tr class="table-white">
                    </c:when>
                    <c:otherwise>
                        <tr class="table-secondary">
                    </c:otherwise>
                    </c:choose>
                        <td>${notification.text}</td>
                        <td>${notification.sentAt.toLocalDate()} ${notification.sentAt.toLocalTime().hour}:${notification.sentAt.toLocalTime().minute}</td>
                        <td>${notification.seen}</td>
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

<script type="text/javascript">
    $(document).ready(function () {
        var locale = getCurrentLocale();
        var languageUrl = getLanguageUrl(locale);
        $('#notificationTable').DataTable({
            "language": {
                "url": languageUrl
            }
        });
    });
    function getCurrentLocale() {
        var cookie = document.cookie;
        if(cookie && cookie.length > 0) {
            return cookie.split("=")[1];
        }
        return "en";
    }
    function getLanguageUrl(locale) {
        switch (locale) {
            default: case 'en':
                return "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";
            case 'pl':
                return "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Polish.json";
            case 'es':
                return "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Spanish.json";
        }
    }
    /*function setSeen(id, seen) {
        $.ajax(
    
        )
    }*/
</script>
