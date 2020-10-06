package com.bpmn.project.adapters;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class EndProcessAdapter implements JavaDelegate {
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("[CAMUNDA] - Registration successful");
	}
}
