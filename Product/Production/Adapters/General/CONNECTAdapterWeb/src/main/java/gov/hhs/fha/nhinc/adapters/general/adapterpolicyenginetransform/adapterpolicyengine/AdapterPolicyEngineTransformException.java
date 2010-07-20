package gov.hhs.fha.nhinc.adapters.general.adapterpolicyenginetransform.adapterpolicyengine;

/**
 * This exception is thrown when an error occurs accessing properties.
 * 
 * @author Les Westberg
 */
public class AdapterPolicyEngineTransformException extends Exception
{
    /**
     * Default constructor.
     */
    public AdapterPolicyEngineTransformException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public AdapterPolicyEngineTransformException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public AdapterPolicyEngineTransformException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public AdapterPolicyEngineTransformException(String sMessage)
    {
        super(sMessage);
    }
    
}
