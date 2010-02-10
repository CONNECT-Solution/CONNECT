package gov.hhs.fha.nhinc.gateway.aggregator;

/**
 * This exception is thrown when an error occurs accessing the aggregator.
 * 
 * @author Les Westberg
 */
public class AggregatorException extends Exception
{
    
    /**
     * Default constructor.
     */
    public AggregatorException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public AggregatorException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public AggregatorException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public AggregatorException(String sMessage)
    {
        super(sMessage);
    }
    
}
