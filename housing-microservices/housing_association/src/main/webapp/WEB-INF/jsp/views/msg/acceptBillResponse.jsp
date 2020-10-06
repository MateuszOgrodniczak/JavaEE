<c:if test="${acceptBillSuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.bill.accept.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${acceptBillError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.bill.accept.failure"/></strong>
        </p>
    </div>
</c:if>