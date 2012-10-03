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

import javax.xml.ws.WebServiceContext;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.event.EventFactory;
import gov.hhs.fha.nhinc.event.responder.BeginInboundMessageEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundMessageEvent;

/**
 * @author zmelnick
 *
 */
@Aspect
public class EventAspect {

    public EventAspect(){

    }

    @Pointcut("execution(* gov.hhs.fha.nhinc..nhin..*.*rovideAndRegisterDocumentSetB*(*,*)) &&" + "args(*,WebServiceContext)")
    private void DSInboundMessage(WebServiceContext context){
    }

    @Before("DSInboundMessage(WebServiceContext)")
    public void doSomethingBefore(WebServiceContext context){
        String messageID = AsyncMessageIdExtractor.GetAsyncMessageId(context);
        EventFactory.createEvent(BeginInboundMessageEvent.class, messageID, null, null);
        System.out.println("doSomethingBefore(narf)");
    }

    @After("DSInboundMessage(WebServiceContext)")
    public void doSomethingAfter(WebServiceContext context){
        String messageID = AsyncMessageIdExtractor.GetAsyncMessageId(context);
        EventFactory.createEvent(EndInboundMessageEvent.class, messageID, null, null);
        System.out.println("doSomethingAfter(narf)");
    }

}
