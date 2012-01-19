package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.concurrent.Callable;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;


/**
 * CallableRequest is basically what is executed (i.e. the Runnable)
 * Uses generics for Response (which represents object that is returned)
 *
 * Constructs with a EntityOrchestratableMessage to be used,
 * which contains the NhinDelegate to execute request and
 * NhinProcessor to processErrorResponse (in the case of error/exception)
 * 
 * @author paul.eftis
 */
public class NhinCallableRequest<Response extends EntityOrchestratableMessage>
        implements Callable<Response>{


    private NhinDelegate client = null;
    private NhinResponseProcessor processor = null;
    private EntityOrchestratableMessage entityRequest = null;


    public NhinCallableRequest(EntityOrchestratableMessage orch){
        this.client = orch.getDelegate();
        this.processor = orch.getResponseProcessor();
        this.entityRequest = orch;
    }



    /**
     * Initiates web service client call to target with request
     * @return Response is web service response from client call
     */
    @Override
    public Response call(){
        Response response = null;
        try{
            if(client != null){
                // make web service call using nhindelegate::process
                response = (Response)client.process(entityRequest);
                if(response == null){
                    throw new Exception("Response received is null!!!");
                }
            }else{
                throw new Exception("NhinDelegate is null!!!");
            }
        }catch(Exception e){
            response = (Response)processor.processErrorResponse(entityRequest, e.getMessage());
        }
        return response;
    }

}
