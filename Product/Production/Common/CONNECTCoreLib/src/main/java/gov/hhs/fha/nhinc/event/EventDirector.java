package gov.hhs.fha.nhinc.event;

public class EventDirector {
    private BaseEventBuilder builder;

    public void setEventBuilder(BaseEventBuilder builder) {
        this.builder = builder;
    }

    public Event getEvent() {
        return builder.getEvent();
    }

    public void constructEvent() {
        builder.createNewEvent();
        
        builder.buildMessageID();
        builder.buildTransactionID();
        builder.buildDescription();
        
    }
}