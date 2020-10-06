<c:if test="${sendRequestSuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.request.send.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${sendRequestError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.request.send.error"/></strong>
        </p>
    </div>
</c:if>