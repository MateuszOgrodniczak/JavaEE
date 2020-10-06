<c:if test="${addedToBillSuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.bill.add.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${addedToBillError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.bill.add.failure"/></strong>
        </p>
    </div>
</c:if>