package gov.hhs.fha.nhinc.gateway.executorservice;


/**
 * Interface that are implemented by the particular web service client
 * that is used by the task executor
 * 
 * @author paul.eftis
 */
public interface WebServiceClient<Target, Request, Response>{

    public Response callWebService(Target t, Request r) throws Exception;
    
}
