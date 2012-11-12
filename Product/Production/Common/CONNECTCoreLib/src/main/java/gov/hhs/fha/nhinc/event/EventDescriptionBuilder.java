package gov.hhs.fha.nhinc.event;

public interface EventDescriptionBuilder {

    void buildMessageId();

    void buildTransactionId();

    void buildTimeStamp();

    void buildStatuses();

    void buildServiceType();

    void buildResponseMsgIdList();

    void buildRespondingHCIDs();

    void buildPayloadTypes();

    void buildPayloadSizes();

    void buildNPI();

    void buildInitiatingHCID();

    void buildErrorCodes();

    void buildAction();

    EventDescription getEventDescription();

    void createEventDescription();

    /**
     * Intended to take the arguments from the method that was executed when this event was triggered and to be used to
     * build the event description. Needs to be implemented in sub classes because only the subs will know what objects
     * they can operate on.
     * 
     * @param arguments
     */
    void setArguments(Object... arguments);

    /**
     * Intended to take the return value from the method that was executed when this event was triggered and to be used
     * to build the event description. Needs to be implemented in sub classes because only the subs will know what
     * objects they can operate on.
     * 
     * The returnValue will only have a valid object if the event was executed after returning from a method. It will be
     * null otherwise.
     * 
     * @param returnValue
     */
    void setReturnValue(Object returnValue);

    void setMsgRouting(MessageRoutingAccessor msgRouting);

    void setMsgContext(EventContextAccessor msgContext);
}
