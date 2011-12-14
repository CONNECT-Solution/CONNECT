package gov.hhs.fha.nhinc.gateway.executorservice;



/**
 * Abstract base class for processing of responses received from callable requests
 * 
 * @author paul.eftis
 */
public abstract class ResponseProcessor<Target, Request, Response, CumulativeResponse>{

    public abstract CumulativeResponse getCumulativeResponse();

    public abstract void processResponse(Request r, Response individual, Target t);

    public abstract Response processError(String error, Request r, Target t);
    
}
