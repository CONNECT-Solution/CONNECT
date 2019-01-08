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
package gov.hhs.fha.nhinc.aspect;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.ContextEventHelper;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.event.EventBuilder;
import gov.hhs.fha.nhinc.event.error.MessageProcessingFailedEvent;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorEventBuilder implements EventBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorEventBuilder.class);

    private MessageProcessingFailedEvent event = new MessageProcessingFailedEvent();
    private ContextEventHelper helper = new ContextEventHelper();
    private Throwable throwable;
    private AssertionType assertion;
    private String invoker;
    private String method;
    private String service;
    private String version;

    @Override
    public void createNewEvent() {
        event = new MessageProcessingFailedEvent();
    }

    @Override
    public void buildDescription() {
        JSONObject jsonObject = new JSONObject();

            try {
                if (throwable != null) {
                    String message = StringUtils.isBlank(throwable.getMessage()) ? "N/A" : throwable.getMessage();
                    jsonObject.put("exceptionMessage", message);
                    jsonObject.put("exceptionClass", throwable.getClass().getName());
                    jsonObject.put("stackTrace", throwable.getStackTrace());
                }
                if (StringUtils.isNotBlank(invoker)) {
                    jsonObject.put("failedClass", invoker);
                }
                if (StringUtils.isNotBlank(method)) {
                    jsonObject.put("failedMethod", method);
                }
                if (StringUtils.isNotBlank(service)) {
                    jsonObject.put("service_type", service);
                }
                if (StringUtils.isNotBlank(version)) {
                    jsonObject.put("version", version);
                }

            } catch (JSONException e) {
                LOG.error("Could not build description: {}", e.getLocalizedMessage());
                LOG.trace("Could not build description: {}", e.getLocalizedMessage(), e);
            }

        event.setDescription(jsonObject.toString());
    }

    @Override
    public void buildMessageID() {
        event.setMessageID(helper.getMessageId(assertion));
    }

    @Override
    public void buildTransactionID() {
        event.setTransactionID(helper.getTransactionId());
    }

    @Override
    public Event getEvent() {
        return event;
    }

    ContextEventHelper getContextHelper() {
        return helper;
    }

    void setContextHelper(ContextEventHelper helper) {
        this.helper = helper;
    }

    public void setThrowable(Throwable t) {
        throwable = t;
    }

    @Override
    public void setAssertion(AssertionType assertion) {
        this.assertion = assertion;
    }

    public void setInvoker(String invoker) {
       this.invoker = invoker;
    }

    public void setMethod(String method) {
       this.method = method;
    }

    public void setService(String service) {
        event.setServiceType(service);
        this.service = service;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
