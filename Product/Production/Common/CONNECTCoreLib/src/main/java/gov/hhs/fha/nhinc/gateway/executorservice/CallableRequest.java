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

import java.util.concurrent.Callable;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CallableRequest is basically what is executed (i.e. the Runnable) Uses generics for Target (which represents the
 * object that contains url to call) Request (which represents the object to send in the request, such as an
 * AdhocQueryRequest) and Response (which represents object that is returned, such as an AdhocQueryResponse).
 *
 * Constructs with a ResponseProcessor (abstract base class for response processor to be used), and a WebServiceClient
 * (interface for web service client to be used)
 *
 * @author paul.eftis
 * @param <Target>
 * @param <Request>
 * @param <Response>
 */
public class CallableRequest<Target, Request, Response> implements Callable<Response> {

    private Target target = null;
    private Request request = null;
    private ResponseProcessor processor = null;
    private WebServiceClient client = null;
    private MessageContext context = null;

    private static final Logger LOG = LoggerFactory.getLogger(CallableRequest.class);

    public CallableRequest(Target t, Request r, ResponseProcessor p, WebServiceClient c) {
        this.target = t;
        this.request = r;
        this.processor = p;
        this.client = c;
        WebServiceContext wsContext = new WebServiceContextImpl();
        context = wsContext.getMessageContext();
    }

    public Request getRequest() {
        return request;
    }

    public Target getTarget() {
        return target;
    }

    /**
     * Initiates web service client call to target with request
     *
     * @return Response is web service response from client call
     * @throws Exception
     */
    @Override
    public Response call() throws Exception {
        WebServiceContextImpl.setMessageContext(context);

        Response response;
        try {
            if (request != null) {
                response = (Response) client.callWebService(target, request);
                if (response == null) {
                    throw new Exception("Response received is null!!!");
                }
            } else {
                throw new Exception("Request is null!!!");
            }
        } catch (Exception e) {
            LOG.trace("Exception making service call: {}", e.getLocalizedMessage(), e);
            response = (Response) new ResponseWrapper(target, request,
                    processor.processError(e.getLocalizedMessage(), request, target));
        }
        return response;
    }

}
