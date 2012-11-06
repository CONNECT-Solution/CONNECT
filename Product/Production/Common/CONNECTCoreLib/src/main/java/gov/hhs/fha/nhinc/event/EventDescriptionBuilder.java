package gov.hhs.fha.nhinc.event;

public interface EventDescriptionBuilder {

    public void buildMessageId();

    public void buildTransactionId();

    public void buildTimeStamp();

    public void buildStatuses();

    public void buildServiceType();

    public void buildResponseMsgIdList();

    public void buildRespondingHCIDs();

    public void buildPayloadTypes();

    public void buildPayloadSizes();

    public void buildNPI();

    public void buildInitiatingHCID();

    public void buildErrorCodes();

    public void buildAction();

    public EventDescription getEventDescription();

    public void createEventDescription();

}
