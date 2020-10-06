<c:if test="${apartmentLeaveSuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.apartment.leave.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${apartmentLeaveError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.apartment.leave.error"/></strong>
        </p>
    </div>
</c:if>