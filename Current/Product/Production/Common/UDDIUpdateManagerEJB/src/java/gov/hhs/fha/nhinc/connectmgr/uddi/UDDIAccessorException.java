package gov.hhs.fha.nhinc.connectmgr.uddi;

/**
 * This exception is thrown when an error occurs accessing properties.
 * 
 * @author Les Westberg
 */
public class UDDIAccessorException extends Exception
{
    private static final long serialVersionUID = -4399592211810514874L;

    /**
     * Default constructor.
     */
    public UDDIAccessorException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public UDDIAccessorException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public UDDIAccessorException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public UDDIAccessorException(String sMessage)
    {
        super(sMessage);
    }
    
}
