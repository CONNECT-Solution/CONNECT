package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * Main unit of execution
 * Executes a DQ or PD request currently, but could be used to execute
 * any of the Nhin transaction requests (such as DR)
 *
 * Uses generics for Target (which represents the object that contains url to call)
 * Request (which represents the object to send in the request, such
 * as an AdhocQueryRequest) and Response (which represents object that is returned,
 * such as an AdhocQueryResponse).
 *
 * Constructs with the java.util.concurrent.ExecutorService, ResponseProcessor, 
 * WebServiceClient to use and also a list of Target to send the requests
 *
 * Uses an ExecutorCompletionService, and executeTask will return only when
 * all CallableRequest have completed/returned.  Once executeTask has returned,
 * call getFinalResponse to get the final cumulative/aggregated/processed response
 * which contains all the responses from the individual CallableRequest
 *
 * @author paul.eftis
 */
public class TaskExecutor<Target, Request, Response>{

    private Log log = LogFactory.getLog(getClass());

    private Executor executor = null;
    private ResponseProcessor processor = null;
    private WebServiceClient client = null;
    private List<Target> targetList = null;
    private Request request = null;
    private String transactionId = null;


    private List<CallableRequest<Target, Request, Response>> requestList
               = new ArrayList<CallableRequest<Target, Request, Response>>();

    /**
     * Determines the taskexecutor service to use based on size of targetList.
     * If targetList size is of the order of the size of the executor service,
     * then the large job executor service is used
     *
     * Constructs with the RequestProcessor to use and WebServiceClient to use.
     * The targetlist, request and response are generics
     */
    public TaskExecutor(Executor e, ResponseProcessor p, WebServiceClient c,
            List<Target> t, Request req, String id){

        processor = p;
        client = c;
        targetList = t;
        request = req;
        transactionId = id;
        executor = e;
    }


    /**
     * Called when TaskExecutor is complete to retrieve the final result
     * @return Response which contains all the responses from the individual CallableRequest
     * aggregated into a single response
     */
    public Response getFinalResponse(){
        return (Response)processor.getCumulativeResponse();
    }


    @SuppressWarnings("static-access")
    public void executeTask()
       throws InterruptedException, ExecutionException{

        log.debug("TaskExecutor::executeTask");

        try{
            for(Target target : targetList){
                CallableRequest<Target, Request, Response> callable =
                    new CallableRequest<Target, Request, Response>(target, request, processor, client);
                requestList.add(callable);
            }

            CompletionService<Response> executorCompletionService = new ExecutorCompletionService<Response>(executor);
            // loop through the request list and submit the callable requests for execution
            for (CallableRequest<Target, Request, Response> c : requestList){
                executorCompletionService.submit(c);
            }

            // the executor completion service puts the callable responses on a 
            // blocking queue where you retrieve <Future> responses off queue using
            // take(), when they become available
            int count = 0;
            for(CallableRequest<Target, Request, Response> c : requestList){
                Future<Response> future = executorCompletionService.take();
                // for debug
                count++;
                log.debug("TaskExecutor::executeTask::take received response count=" + count);

                if(future != null){
                    try{
                        ResponseWrapper r = (ResponseWrapper)future.get();
                        if(r != null){
                            // process response
                            processor.processResponse(r.getCallableRequest(),
                                    r.getResponse(), r.getCallableTarget());
                        }else{
                            // shouldn't ever get here, but if we do all we can do is log and skip it
                            log.error("TaskExecutor::executeTask (count=" + count
                                    + ") received null response!!!!!");
                        }
                    }catch(Exception e){
                        // shouldn't ever get here
                        log.error("TaskExecutorexecuteTask processResponse EXCEPTION!!!");
                        ExecutorServiceHelper.getInstance().outputCompleteException(e);
                    }
                }else{
                    // shouldn't ever get here
                    log.error("TaskExecutor::executeTask received null future from queue (i.e. take)!!!!!");
                }
            }

        }catch(Exception e){
            // shouldn't ever get here
            log.error("TaskExecutorexecuteTask EXCEPTION!!!");
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
        }
    }

}
