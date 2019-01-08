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
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 *
 */
public class AggregationStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(AggregationStrategy.class);

    public void execute(Aggregate aggregate) {
        Executor executor = Executors.newCachedThreadPool();
        CompletionService<OutboundOrchestratable> completionService = new ExecutorCompletionService<>(executor);
        Collection<OutboundOrchestratable> aggregationRequests = aggregate.getAggregateRequests();

        int size = aggregationRequests.size();

        for (OutboundOrchestratable orchestrationContext : aggregationRequests) {
            completionService.submit(new CallableAggregation(orchestrationContext));
        }

        int i = 0;
        for (; i < size; i++) {
            try {
                aggregate.aggregate(completionService.take().get());
                LOG.trace("got response " + (i + 1) + " of " + size);
            } catch (InterruptedException e) {
                LOG.error("Failure during aggregation, aborting: " + e.getLocalizedMessage(), e);
                break;
            } catch (ExecutionException e) {
                LOG.error("Could not aggregrate response #" + (i + 1) + ": " + e.getLocalizedMessage(), e);
                continue;
            }
        }

        if (i < size) {
            LOG.info((size - i) + " responses failed.");
        }

    }

    class CallableAggregation implements Callable<OutboundOrchestratable> {

        private OutboundOrchestratable orchestrable;
        private MessageContext mContext;

        public CallableAggregation(OutboundOrchestratable context) {
            this.orchestrable = context;
            WebServiceContext wsContext = new WebServiceContextImpl();
            mContext = wsContext.getMessageContext();
        }

        @Override
        public OutboundOrchestratable call() throws Exception {
            WebServiceContextImpl.setMessageContext(mContext);
            return orchestrable.getDelegate().process(orchestrable);
        }
    }
}
