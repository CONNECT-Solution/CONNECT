package gov.hhs.fha.nhinc.event;

public class EventDescriptionDirector {
    private EventDescriptionBuilder builder;
    
    public void setEventDescriptionBuilder(EventDescriptionBuilder builder) { this.builder = builder; }
    public EventDescription getEventDescription() { return builder.getEventDescription(); }
 
    public void constructEventDescription() {
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
