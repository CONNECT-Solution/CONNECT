package gov.hhs.fha.nhinc.event;

public class EventDescriptionDirector {
    private EventDescriptionBuilder builder;

    public void setEventDescriptionBuilder(EventDescriptionBuilder builder) {
        this.builder = builder;
    }

    public EventDescription getEventDescription() {
        return builder.getEventDescription();
    }

    public void constructEventDescription() {
        builder.createEventDescription();

        builder.buildMessageId();
        builder.buildTransactionId();
        builder.buildTimeStamp();
        builder.buildStatuses();
        builder.buildServiceType();
        builder.buildResponseMsgIdList();
        builder.buildRespondingHCIDs();
        builder.buildPayloadTypes();
        builder.buildPayloadSizes();
        builder.buildNPI();
        builder.buildInitiatingHCID();
        builder.buildErrorCodes();
        builder.buildAction();
    }

}
