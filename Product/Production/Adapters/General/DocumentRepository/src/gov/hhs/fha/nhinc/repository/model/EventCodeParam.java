package gov.hhs.fha.nhinc.repository.model;

/**
 * Query parameter for a single event code.
 *
 * @author Neil Webb
 */
public class EventCodeParam
{

    private String eventCode;
    private String eventCodeScheme;

    public String getEventCode()
    {
        return eventCode;
    }

    public void setEventCode(String eventCode)
    {
        this.eventCode = eventCode;
    }

    public String getEventCodeScheme()
    {
        return eventCodeScheme;
    }

    public void setEventCodeScheme(String eventCodeScheme)
    {
        this.eventCodeScheme = eventCodeScheme;
    }
}
