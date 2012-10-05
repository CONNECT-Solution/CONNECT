/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.util.List;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.w3c.dom.Element;

/**
 * @author zmelnick
 * 
 */
@Aspect
public class EventAspect {

    public EventAspect() {

    }

    private String getMessageId() {
        String messageId = null;

        WebServiceContext context = new org.apache.cxf.jaxws.context.WebServiceContextImpl();
        MessageContext mContext = (MessageContext) context.getMessageContext();

        SoapHeader messageIdHeader = getMessageIDSoapHeader(mContext);
        if (messageIdHeader != null) {
            Object obj = messageIdHeader.getObject();
            Element element = (Element) obj;
            messageId = element.getFirstChild().getNodeValue();
        }

        return messageId;
    }

    private final SoapHeader getMessageIDSoapHeader(MessageContext mContext) {
        @SuppressWarnings("unchecked")
        List<Header> headers = (List<Header>) mContext.get(org.apache.cxf.headers.Header.HEADER_LIST);

        if (headers != null) {
            for (Header header : headers) {
                if (header.getName().getLocalPart().equalsIgnoreCase("MessageID")) {
                    return (SoapHeader) header;
                }
            }
        }

        return null;
    }

    @Before("execution(* gov.hhs.fha.nhinc.docsubmission.*.nhin.NhinXDR*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    public void beginInboundMessageEvent() {
        System.out.println("beginInboundMessageEvent" + getMessageId());
    }

    @Before("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.NhinDocSubmissionOrchImpl*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    public void beginInboundProcessingEvent() {
        System.out.println("beginInboundProcessing" + getMessageId());        
    }

    @Before("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy*.provideAndRegisterDocumentSetB(..))")
    public void beginAdapterDelegationEvent() {
        System.out.println("beginAdapterDelegation" + getMessageId());
    }

    @After("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy*.provideAndRegisterDocumentSetB(..))")
    public void endAdapterDelegationEvent() {
        System.out.println("endAdapterDelegationEvent" + getMessageId());
    }

    @After("execution(* gov.hhs.fha.nhinc.docsubmission.nhin.NhinDocSubmissionOrchImpl*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    public void endInboundProcessingEvent() {
        System.out.println("endInboundProcessingEvent" + getMessageId());
    }

    @After("execution(* gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy*.provideAndRegisterDocumentSetB(..))")
    public void endInboundMessageEvent() {
        System.out.println("endInboundMessageEvent" + getMessageId());
    }

    @AfterThrowing("execution(* gov.hhs.fha.nhinc.docsubmission.*.nhin.NhinXDR*.documentRepositoryProvideAndRegisterDocumentSetB(..))")
    public void messageProcessingFailedEvent() {
        System.out.println("endInboundMessageEvent" + getMessageId());
    }

}
