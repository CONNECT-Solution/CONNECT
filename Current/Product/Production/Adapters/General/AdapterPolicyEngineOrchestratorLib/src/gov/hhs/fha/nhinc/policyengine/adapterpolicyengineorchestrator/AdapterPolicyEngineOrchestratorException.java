package gov.hhs.fha.nhinc.policyengine.adapterpolicyengineorchestrator;

/**
 * This exception is thrown when an error occurs within the AdapterPolicyEngineOrchestrator.
 * 
 * @author Les Westberg
 */
public class AdapterPolicyEngineOrchestratorException extends Exception
{
    private static final long serialVersionUID = -2763323716005332016L;
    
    /**
     * Default constructor.
     */
    public AdapterPolicyEngineOrchestratorException()
    {
        super();
    }
    
    /**
     * Constructor with an envloping exception.
     * 
     * @param e  The exception that caused this one.
     */
    public AdapterPolicyEngineOrchestratorException(Exception e)
    {
        super(e);
    }

    /**
     * Constructor with the given exception and message.
     * 
     * @param sMessage The message to place in the exception.
     * @param e The exception that triggered this one.
     */
    public AdapterPolicyEngineOrchestratorException(String sMessage, Exception e)
    {
        super(sMessage, e);
    }

    /**
     * Constructor with a given message.
     * 
     * @param sMessage The message for the exception.
     */
    public AdapterPolicyEngineOrchestratorException(String sMessage)
    {
        super(sMessage);
    }
    
}
