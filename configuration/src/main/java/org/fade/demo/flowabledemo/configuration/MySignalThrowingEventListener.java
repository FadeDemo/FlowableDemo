package org.fade.demo.flowabledemo.configuration;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.engine.impl.bpmn.helper.SignalThrowingEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fade
 * @date 2021/09/27
 */
public class MySignalThrowingEventListener extends SignalThrowingEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MySignalThrowingEventListener.class);

    @Override
    public void onEvent(FlowableEvent event) {
        logger.info("begin to deal with throw type event");
        super.onEvent(event);
        logger.info("dealing with throw type event is over");
    }

}
