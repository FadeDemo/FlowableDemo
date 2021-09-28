package org.fade.demo.flowabledemo.configuration;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;

/**
 * @author fade
 * @date 2021/09/26
 */
public class MyEventListener implements FlowableEventListener {

    @Override
    public void onEvent(FlowableEvent event) {

        if(event.getType() == FlowableEngineEventType.JOB_EXECUTION_SUCCESS) {
            System.out.println("A job well done!");
        } else if (event.getType() == FlowableEngineEventType.JOB_EXECUTION_FAILURE) {
            System.out.println("A job has failed...");
        } else {
            System.out.println("Event received: " + event.getType());
        }
    }

    @Override
    public boolean isFailOnException() {
        // The logic in the onEvent method of this listener is not critical, exceptions
        // can be ignored if logging fails...
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
