package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallExternalSystemDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallExternalSystemDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        LOGGER.info("Calling the external system for employee 【{}】", execution.getVariable("employee"));
    }
}
