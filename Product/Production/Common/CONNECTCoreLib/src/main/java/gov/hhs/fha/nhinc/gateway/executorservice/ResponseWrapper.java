package gov.hhs.fha.nhinc.gateway.executorservice;


/**
 * Wrapper class returned by the CallableRequest that contains the response and
 * also contains the request and target used by the CallableRequest.
 * 
 * @author paul.eftis
 */
public class ResponseWrapper<Target, Request, Response>{

    Target target = null;
    Request request = null;
    Response response = null;


    public ResponseWrapper(){

    }

    public ResponseWrapper(Target t, Request r, Response resp){
        target = t;
        request = r;
        response = resp;
    }


    public Request getCallableRequest(){
        return request;
    }


    public void setCallableRequest(Request r){
        request = r;
    }


    public Target getCallableTarget(){
        return target;
    }


    public void setCallableTarget(Target t){
        target = t;
    }


    public Response getResponse(){
        return response;
    }


    public void setResponse(Response r){
        response = r;
    }

}
