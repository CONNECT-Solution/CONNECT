package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.concurrent.Callable;


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
public class CallableRequest<Target, Request, Response> implements Callable<Response>{

    private Target target = null;
    private Request request = null;
    private ResponseProcessor processor = null;
    private WebServiceClient client = null;


    public CallableRequest(Target t, Request r, ResponseProcessor p, WebServiceClient c){
        this.target = t;
        this.request = r;
        this.processor = p;
        this.client = c;
    }


    public Request getRequest(){
        return request;
    }


    public Target getTarget(){
        return target;
    }


    /**
     * Initiates web service client call to target with request
     * @return Response is web service response from client call
     * @throws Exception
     */
    @Override
    public Response call() throws Exception{
        Response response = null;
        try{
            if(request != null){
                response = (Response)client.callWebService(target, request);
                if(response == null){
                    throw new Exception("Response received is null!!!");
                }
            }else{
                throw new Exception("Request is null!!!");
            }
        }catch(Exception e){
            response = (Response)new ResponseWrapper(target, request,
                    processor.processError(e.getMessage(), request, target));
        }
        return response;
    }

}
