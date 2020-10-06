package com.bpmn.project.adapters;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class VerifyRecaptchaAdapter implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("[CAMUNDA] - Recaptcha verification");
		boolean verified = (boolean) execution.getVariable("verified");
		if(!verified) throw new BpmnError("0", "Błąd walidacji recaptcha!");
	}
}
