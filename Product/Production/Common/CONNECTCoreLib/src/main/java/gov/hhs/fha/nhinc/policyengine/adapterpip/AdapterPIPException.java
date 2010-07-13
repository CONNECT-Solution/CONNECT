package gov.hhs.fha.nhinc.policyengine.adapterpip;

/**
 * This exception is thrown when an error occurs within the AdapterPIP.
 * 
 * @author Les Westberg
 */
public class AdapterPIPException extends Exception
{
    private static final long serialVersionUID = -2763323716005332016L;
    
    /**
     * Default constructor.
     */
    public AdapterPIPException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public AdapterPIPException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public AdapterPIPException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public AdapterPIPException(String sMessage)
    {
        super(sMessage);
    }
    
}
