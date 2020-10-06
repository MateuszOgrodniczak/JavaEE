<c:if test="${paySuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.pay.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${payError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.pay.error"/></strong>
        </p>
    </div>
</c:if>