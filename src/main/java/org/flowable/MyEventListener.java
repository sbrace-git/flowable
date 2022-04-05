package org.flowable;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.flowable.common.engine.api.delegate.event.FlowableEngineEventType.JOB_EXECUTION_FAILURE;
import static org.flowable.common.engine.api.delegate.event.FlowableEngineEventType.JOB_EXECUTION_SUCCESS;

public class MyEventListener implements FlowableEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyEventListener.class);

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        FlowableEventType type = flowableEvent.getType();
//        LOGGER.info("type = 【{}】", type);
//        if (JOB_EXECUTION_SUCCESS.equals(type)) {
//            LOGGER.info("A job well done!");
//        } else if (JOB_EXECUTION_FAILURE.equals(type)) {
//            LOGGER.info("A job has failed...");
//        } else {
//            LOGGER.info("Event received: " + flowableEvent.getType());
//        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
