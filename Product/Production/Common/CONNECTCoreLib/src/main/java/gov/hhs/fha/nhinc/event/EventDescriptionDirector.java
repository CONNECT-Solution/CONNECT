package gov.hhs.fha.nhinc.event;

public class EventDescriptionDirector {
    private BaseEventDescriptionBuilder builder;
    
    public void setEventDescriptionBuilder(BaseEventDescriptionBuilder builder) { this.builder = builder; }
    public EventDescription getPizza() { return builder.getEventDescription(); }
 
    public void constructEvent() {
      builder.createEventDescription();
      
      builder.buildMessageId();
      builder.buildTransactionId();
      builder.buildTimeStamp();
      builder.buildStatus();
      builder.buildServiceType();
      builder.buildResponseMsgIdList();
      builder.buildRespondingHCID();
      builder.buildPayloadType();
      builder.buildPayloadSize();
      builder.buildNPI();
      builder.buildInitiatingHCID();
      builder.buildErrorCode();
      builder.buildAction();
    }

}
