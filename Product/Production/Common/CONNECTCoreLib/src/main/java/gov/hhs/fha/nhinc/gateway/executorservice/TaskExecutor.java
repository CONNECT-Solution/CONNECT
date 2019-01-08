/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.gateway.executorservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main unit of execution Executes a DQ or PD request currently, but could be used to execute any of the Nhin
 * transaction requests (such as DR)
 *
 * Uses generics for Target (which represents the object that contains url to call) Request (which represents the object
 * to send in the request, such as an AdhocQueryRequest) and Response (which represents object that is returned, such as
 * an AdhocQueryResponse).
 *
 * Constructs with the java.util.concurrent.ExecutorService, ResponseProcessor, WebServiceClient to use and also a list
 * of Target to send the requests
 *
 * Uses an ExecutorCompletionService, and executeTask will return only when all CallableRequest have completed/returned.
 * Once executeTask has returned, call getFinalResponse to get the final cumulative/aggregated/processed response which
 * contains all the responses from the individual CallableRequest
 *
 * @author paul.eftis
 */
public class TaskExecutor<Target, Request, Response> {

    private static final Logger LOG = LoggerFactory.getLogger(TaskExecutor.class);

    private Executor executor = null;
    private ResponseProcessor processor = null;
    private WebServiceClient client = null;
    private List<Target> targetList = null;
    private Request request = null;
    private List<CallableRequest<Target, Request, Response>> requestList = new ArrayList<>();

    /**
     * Determines the taskexecutor service to use based on size of targetList. If targetList size is of the order of the
     * size of the executor service, then the large job executor service is used
     *
     * Constructs with the RequestProcessor to use and WebServiceClient to use. The targetlist, request and response are
     * generics
     */
    public TaskExecutor(Executor e, ResponseProcessor p, WebServiceClient c, List<Target> t, Request req, String id) {

        processor = p;
        client = c;
        targetList = t;
        request = req;
        executor = e;
    }

    /**
     * Called when TaskExecutor is complete to retrieve the final result
     *
     * @return Response which contains all the responses from the individual CallableRequest aggregated into a single
     *         response
     */
    public Response getFinalResponse() {
        return (Response) processor.getCumulativeResponse();
    }

    @SuppressWarnings("static-access")
    public void executeTask() throws InterruptedException, ExecutionException {

        LOG.debug("TaskExecutor::executeTask");

        try {
            for (Target target : targetList) {
                CallableRequest<Target, Request, Response> callable = new CallableRequest<>(target, request, processor,
                        client);
                requestList.add(callable);
            }

            CompletionService<Response> executorCompletionService = new ExecutorCompletionService<>(executor);
            // loop through the request list and submit the callable requests for execution
            for (CallableRequest<Target, Request, Response> c : requestList) {
                executorCompletionService.submit(c);
            }

            // the executor completion service puts the callable responses on a
            // blocking queue where you retrieve <Future> responses off queue using
            // take(), when they become available
            int count = 0;
            for (CallableRequest<Target, Request, Response> c : requestList) {
                Future<Response> future = executorCompletionService.take();
                // for debug
                count++;
                LOG.debug("TaskExecutor::executeTask::take received response count=" + count);

                if (future != null) {
                    try {
                        ResponseWrapper r = (ResponseWrapper) future.get();
                        if (r != null) {
                            // process response
                            processor.processResponse(r.getCallableRequest(), r.getResponse(), r.getCallableTarget());
                        } else {
                            // shouldn't ever get here, but if we do all we can do is log and skip it
                            LOG.error("TaskExecutor::executeTask (count=" + count + ") received null response!!!!!");
                        }
                    } catch (Exception e) {
                        // shouldn't ever get here
                        LOG.error("TaskExecutorexecuteTask processResponse EXCEPTION!!!");
                        ExecutorServiceHelper.getInstance().outputCompleteException(e);
                    }
                } else {
                    // shouldn't ever get here
                    LOG.error("TaskExecutor::executeTask received null future from queue (i.e. take)!!!!!");
                }
            }

        } catch (Exception e) {
            // shouldn't ever get here
            LOG.error("TaskExecutorexecuteTask EXCEPTION!!!");
            ExecutorServiceHelper.getInstance().outputCompleteException(e);
        }
    }

}
