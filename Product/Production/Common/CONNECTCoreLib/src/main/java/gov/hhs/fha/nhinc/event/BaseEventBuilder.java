package gov.hhs.fha.nhinc.event;

public abstract class BaseEventBuilder implements EventBuilder {

    EventDescriptionDirector eventDescriptionDirector;
    protected Event event;

    @Override
    public void buildDescription() {
        eventDescriptionDirector.constructEventDescription();
        EventDescriptionJSONDecorator jsonDescorator = new EventDescriptionJSONDecorator(
                eventDescriptionDirector.getEventDescription());
        event.setDescription(jsonDescorator.toJSONString());
        
        buildServiceType();
        buildInitiatorHcid();
        buildRespondingHcids();
    }

    @Override
    public void buildMessageID() {
    }

    @Override
    public void buildTransactionID() {
    }

    public void setEventDesciptionDirector(EventDescriptionDirector eventDescriptionDirector) {
        this.eventDescriptionDirector = eventDescriptionDirector;
    }

    public EventDescriptionDirector getEventDescriptionDirector() {
        return eventDescriptionDirector;
    }
    
    private void buildServiceType(){
        event.setServiceType(eventDescriptionDirector.getServiceType());
    }
    
    private void buildInitiatorHcid(){
        event.setInitiatorHcid(eventDescriptionDirector.getInitiatorHcid());
    }
    
    private void buildRespondingHcids(){
        event.setRespondingHcid(eventDescriptionDirector.getResponderHcid());
    }
}
