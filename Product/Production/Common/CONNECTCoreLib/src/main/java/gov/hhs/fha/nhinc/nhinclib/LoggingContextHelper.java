/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.nhinclib;


import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.xml.ws.WebServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This helper class sets up a logging context such that message flow can be traced through process log messages.
 */
public class LoggingContextHelper {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingContextHelper.class);

    /**
     * Builds the Nested Diapnostic Context for the current thread and initializes the generated logging context id in
     * it.
     *
     * @param webServiceContext Provides the message context of the request being served
     */
    public void setContext(WebServiceContext webServiceContext) {
        String loggingContextId = generateLoggingContextId(webServiceContext);
        NDC.push(loggingContextId);
    }

    /**
     * This method will create a logging context id that retains the message id for the current message as well as the
     * message id of what it may be responding to. An additional unique identifier will also be appended to make this
     * logging message unique.
     *
     * @param webServiceContext Provides the message context of the request being served
     * @return Unique representation of this logging context
     */
    protected String generateLoggingContextId(WebServiceContext webServiceContext) {

        StringBuffer buffer = new StringBuffer();



        String messageId = ""; //AsyncMessageIdExtractor.GetAsyncMessageId(webServiceContext);
        List<String> allRelatesToIds = Collections.EMPTY_LIST; // AsyncMessageIdExtractor.GetAsyncRelatesTo(webServiceContext);
        if (messageId != null) {
            buffer.append(messageId);
        }
        buffer.append(".");
        if (allRelatesToIds != null && !allRelatesToIds.isEmpty()) {
            for (String relatesToId : allRelatesToIds) {
                if (relatesToId != null) {
                    buffer.append(relatesToId);
                    buffer.append(" ");
                }
            }
        }
        buffer.append(".");
        buffer.append(UUID.randomUUID().toString());
        if(LOG.isInfoEnabled()){
        	LOG.info("Setting contextId: " + buffer.toString());
        }
        return buffer.toString();
    }

    /**
     * This method should be used when exiting the processing thread to remove the context.
     */
    public void clearContext() {
        NDC.remove();
    }
}
