<c:if test="${consumptionsGeneratedSuccess}">
    <div class="alert alert-dismissible alert-success custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.consumptions.add.success"/></strong>
        </p>
    </div>
</c:if>
<c:if test="${consumptionsGeneratedError}">
    <div class="alert alert-dismissible alert-danger custom-alertBox" style="position: fixed">
        <button type="button" class="close" data-dismiss="alert">&times;</button>
        <p>
            <strong><spring:message code="label.consumptions.add.error"/></strong>
        </p>
    </div>
</c:if>
