package gov.hhs.fha.nhinc.policyengine.adapterpep;

/**
 * This exception is thrown when an error occurs within the AdapterPEP.
 */
public class AdapterPEPException extends Exception
{   
    /**
     * Default constructor.
     */
    public AdapterPEPException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param ex  The exception that caused this one.
     */
    public AdapterPEPException(Exception ex)
    {
        super(ex);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param message The message to place in the exception.
     * @param ex The exception that triggered this one.
     */
    public AdapterPEPException(String message, Exception ex)
    {
        super(message, ex);
    }

    /**
     * Constructor with a given message.
     * 
     * @param message The message for the exception.
     */
    public AdapterPEPException(String message)
    {
        super(message);
    }
    
}
