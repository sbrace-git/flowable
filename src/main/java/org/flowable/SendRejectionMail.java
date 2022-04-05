package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendRejectionMail implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendRejectionMail.class);

    @Override
    public void execute(DelegateExecution execution) {
        String employee = execution.getVariable("employee", String.class);
        LOGGER.info("rejected employee = {}", employee);
    }
}
