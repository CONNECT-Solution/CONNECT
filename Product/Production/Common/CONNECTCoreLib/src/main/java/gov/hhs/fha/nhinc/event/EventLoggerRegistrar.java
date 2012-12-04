package gov.hhs.fha.nhinc.event;

public interface EventLoggerRegistrar {

    public abstract void registerLogger(EventLogger eventLogger);

}
