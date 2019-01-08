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
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.logging.transaction.TransactionStore;
import gov.hhs.fha.nhinc.logging.transaction.factory.TransactionStoreFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;

public class ContextEventHelper {

    private AsyncMessageIdExtractor extractor = new AsyncMessageIdExtractor();
    private WebServiceContextImpl context = new WebServiceContextImpl();
    private TransactionStoreFactory factory = new TransactionStoreFactory();

    public String getMessageId() {
        return extractor.getMessageId(context);
    }

    public String getTransactionId() {
        String transactionId = null;
        String messageId = getMessageId();
        TransactionStore store = factory.getTransactionStore();

        List<String> transactionIdList = extractor.getAsyncRelatesTo(context);
        if (NullChecker.isNotNullish(transactionIdList)) {
            transactionId = store.getTransactionId(transactionIdList.get(0));
        }

        if (transactionId == null && messageId != null) {
            transactionId = store.getTransactionId(messageId);
        }

        return transactionId;
    }

    void setAsyncMessageIdExtractor(AsyncMessageIdExtractor extractor) {
        this.extractor = extractor;
    }

    void setTransactionStoreFacotyr(TransactionStoreFactory factory) {
        this.factory = factory;
    }

    WebServiceContext getContext() {
        return context;
    }

    AsyncMessageIdExtractor getExtractor() {
        return extractor;
    }

    public String getMessageId(AssertionType assertion) {
        if (assertion != null && StringUtils.isNotEmpty(assertion.getMessageId())) {
            return assertion.getMessageId();
        } else {
            return getMessageId();
        }
    }
}
