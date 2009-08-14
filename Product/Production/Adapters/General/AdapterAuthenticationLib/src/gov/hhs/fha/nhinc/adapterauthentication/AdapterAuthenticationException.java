package gov.hhs.fha.nhinc.adapterauthentication;

/**
 * This exception is thrown when an error occurs within the AdapterAuthentication.
 */
public class AdapterAuthenticationException extends Exception
{   
    /**
     * Default constructor.
     */
    public AdapterAuthenticationException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param ex  The exception that caused this one.
     */
    public AdapterAuthenticationException(Exception ex)
    {
        super(ex);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param message The message to place in the exception.
     * @param ex The exception that triggered this one.
     */
    public AdapterAuthenticationException(String message, Exception ex)
    {
        super(message, ex);
    }

    /**
     * Constructor with a given message.
     * 
     * @param message The message for the exception.
     */
    public AdapterAuthenticationException(String message)
    {
        super(message);
    }
    
}
