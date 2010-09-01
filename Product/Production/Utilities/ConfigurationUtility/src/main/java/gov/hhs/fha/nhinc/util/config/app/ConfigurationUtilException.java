package gov.hhs.fha.nhinc.util.config.app;

/**
 *
 * @author Neil Webb
 */
public class ConfigurationUtilException extends RuntimeException
{
    static final long serialVersionUID = -9112304447072344279L;

    public ConfigurationUtilException()
    {
        super();
    }

    public ConfigurationUtilException(String message)
    {
        super(message);
    }

    public ConfigurationUtilException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ConfigurationUtilException(Throwable cause)
    {
        super(cause);
    }
}
