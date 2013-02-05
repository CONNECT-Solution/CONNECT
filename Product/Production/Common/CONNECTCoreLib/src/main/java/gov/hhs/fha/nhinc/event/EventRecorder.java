package gov.hhs.fha.nhinc.event;

public interface EventRecorder {

    /**
     * Records an event. May be a no-op (discoverable via {@link #isRecordEventEnabled()}.
     * 
     * @param event
     *            event to record
     */
    void recordEvent(Event event);

    /**
     * @return true if calls to {@link #recordEvent(Event)} will be recorded somewhere. Useful if creating an event is
     *         expensive. Similar to @{link Logger.isDebugEnabled()}
     */
    boolean isRecordEventEnabled();
}
