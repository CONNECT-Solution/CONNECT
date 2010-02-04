package gov.hhs.fha.nhinc.util;

/**
 * This exception is thrown when an error occurs accessing properties.
 * 
 * @author Les Westberg
 */
public class UtilException extends Exception
{
    private static final long serialVersionUID = 5796300225177957766L;
    /**
     * Default constructor.
     */
    public UtilException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public UtilException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public UtilException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public UtilException(String sMessage)
    {
        super(sMessage);
    }
    
}
