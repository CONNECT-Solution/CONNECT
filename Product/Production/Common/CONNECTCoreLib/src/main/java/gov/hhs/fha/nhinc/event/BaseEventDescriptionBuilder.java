package gov.hhs.fha.nhinc.event;


public class BaseEventDescriptionBuilder implements EventDescriptionBuilder {
    
    protected BaseEventDescription description;
    
    public EventDescription getEventDescription() {
        return description;
    }
    
    public EventDescription createEventDescription() {
        return new BaseEventDescription();
    }
    
    public void buildMessageId() {}
    public void buildTransactionId() {}
    public void buildTimeStamp() {}
    public void buildStatus() {}
    public void buildServiceType() {}
    public void buildResponseMsgIdList() {}
    public void buildRespondingHCID() {}
    public void buildPayloadType() {}
    public void buildPayloadSize() {}
    public void buildNPI() {}
    public void buildInitiatingHCID() {}
    public void buildErrorCode() {}
    public void buildAction() {}

}
