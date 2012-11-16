package gov.hhs.fha.nhinc.event;

public interface EventBuilder {

    public void createNewEvent();

    public void buildDescription();

    public void buildMessageID();

    public void buildTransactionID();

    public Event getEvent();
    

}
