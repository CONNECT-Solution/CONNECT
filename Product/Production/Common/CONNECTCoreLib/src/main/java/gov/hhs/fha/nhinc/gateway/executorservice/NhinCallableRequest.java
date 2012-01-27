package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.concurrent.Callable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;


/**
 * CallableRequest is basically what is executed (i.e. the Runnable)
 * Uses generics for Response (which represents object that is returned)
 *
 * Constructs with a OutboundOrchestratableMessage to be used,
 * which contains the OutboundDelegate to execute request and
 * NhinProcessor to processErrorResponse (in the case of error/exception)
 * 
 * @author paul.eftis
 */
public class NhinCallableRequest<Response extends OutboundOrchestratableMessage>
        implements Callable<Response>{


    private OutboundDelegate client = null;
    private OutboundResponseProcessor processor = null;
    private OutboundOrchestratableMessage entityRequest = null;


    public NhinCallableRequest(OutboundOrchestratableMessage orch){
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
