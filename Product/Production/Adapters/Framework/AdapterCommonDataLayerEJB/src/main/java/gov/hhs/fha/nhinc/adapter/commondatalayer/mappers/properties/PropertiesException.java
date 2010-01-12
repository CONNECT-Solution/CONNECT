package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers.properties;


/**
 *
 * @author A22387
 */
public class PropertiesException extends Exception
{
    /**
     * Default Constructor
     */
    public PropertiesException()
    {
        super();
    }

    /**
     * Create an excetption with a message
     *
     * @param sMessage A message about the exception
     */
    public PropertiesException(String sMessage)
    {
        super(sMessage);
    }

    /**
     * Create an exception with a message and the root cause
     *
     * @param sMessage A message about the exception
     * @param oRootCause The root cause of the exception
     */
    public PropertiesException(String sMessage, Throwable oRootCause)
    {
        super(sMessage, oRootCause);
    }

    /**
     * Create an exception with the root cause
     *
     * @param oRootCause The root cause of the exception
     */
    public PropertiesException(Throwable oRootCause)
    {
        super(oRootCause);
    }
}
