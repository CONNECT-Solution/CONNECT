package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

/**
 * Exception during service lookup
 * @author Jerry Goodnough
 */
public class ServiceLookupException extends Exception {

    /**
     * Simnple Exception constructor
     * @param msg ExceptionMessage
     */
    public ServiceLookupException(String msg)
    {
        super(msg);
    }

    /**
     * Simple Exception constructor with causing exception
     * @param msg ExceptionMessage
     * @param exp Causing exception
     */
    public ServiceLookupException(String msg, Throwable exp)
    {
        super(msg,exp);
    }
}
