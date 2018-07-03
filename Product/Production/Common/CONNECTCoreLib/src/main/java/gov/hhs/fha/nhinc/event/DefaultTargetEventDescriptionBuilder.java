package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

public class DefaultTargetEventDescriptionBuilder extends TargetEventDescriptionBuilder {

    ContextEventHelper helper = new ContextEventHelper();

    @Override
    public void buildTimeStamp() {

    }


    @Override
    public void buildStatuses() {


    }

    @Override
    public void buildPayloadTypes() {


    }

    @Override
    public void buildPayloadSizes() {


    }

    @Override
    public void buildErrorCodes() {


    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractTarget(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {


    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#buildMessageId()
     */
    @Override
    public void buildMessageId() {
        super.buildMessageId();
        Optional<AssertionType> assertion = getAssertion();
        if (assertion.isPresent()) {
            helper.getMessageId(assertion.get());
        }
    }

}
