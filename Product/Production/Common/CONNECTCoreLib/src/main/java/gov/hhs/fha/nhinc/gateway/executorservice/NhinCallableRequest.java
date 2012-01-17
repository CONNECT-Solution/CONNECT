package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.concurrent.Callable;
import gov.hhs.fha.nhinc.orchestration.EntityOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.NhinDelegate;
import gov.hhs.fha.nhinc.orchestration.NhinResponseProcessor;


/**
 * CallableRequest is basically what is executed (i.e. the Runnable)
 * Uses generics for Target (which represents the object that contains url to call)
 * Request (which represents the object to send in the request, such
 * as an AdhocQueryRequest) and Response (which represents object that is returned,
 * such as an AdhocQueryResponse).
 *
 * Constructs with a ResponseProcessor (abstract base class for response processor to be used),
 * and a WebServiceClient (interface for web service client to be used)
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
