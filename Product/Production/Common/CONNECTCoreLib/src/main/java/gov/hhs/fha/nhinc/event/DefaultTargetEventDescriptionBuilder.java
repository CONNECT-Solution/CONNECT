package gov.hhs.fha.nhinc.event;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

public class DefaultTargetEventDescriptionBuilder extends TargetEventDescriptionBuilder {

    ContextEventHelper helper = new ContextEventHelper();

    @Override
    public void buildTimeStamp() {
        //Default implementation does nothing.
    }


    @Override
    public void buildStatuses() {
      //Default implementation does nothing.
    }

    @Override
    public void buildPayloadTypes() {
      //Default implementation does nothing.
    }

    @Override
    public void buildPayloadSizes() {
      //Default implementation does nothing.
    }

    @Override
    public void buildErrorCodes() {
      //Default implementation does nothing.
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
        extractTarget(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
      //Default implementation does nothing.
    }

    @Override
    public void buildMessageId() {
        super.buildMessageId();
        Optional<AssertionType> assertion = getAssertion();
        if (assertion.isPresent()) {
            helper.getMessageId(assertion.get());
        }
    }

}
