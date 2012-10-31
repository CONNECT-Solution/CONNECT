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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author zmelnick
 * 
 */
@Aspect
public class EventAspectAdvice {

    private static final Log log = LogFactory.getLog(EventAspectAdvice.class);

    private EventAdviceDelegate delegate;

    public void setDelegate(EventAdviceDelegate delegate) {
        this.delegate = delegate;
    }

    @Before("@annotation(annotation)")
    public void beginInboundMessageEvent(JoinPoint joinPoint, InboundMessageEvent annotation) {
        delegate.beginInboundMessageEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endInboundMessageEvent(JoinPoint joinPoint, InboundMessageEvent annotation) {
        delegate.endInboundMessageEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @Before("@annotation(annotation)")
    public void beginInboundProcessingEvent(JoinPoint joinPoint, InboundProcessingEvent annotation) {
        delegate.beginInboundProcessingEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endInboundProcessingEvent(JoinPoint joinPoint, InboundProcessingEvent annotation) {
        delegate.endInboundProcessingEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @Before("@annotation(annotation)")
    public void beginAdapterDelegationEvent(JoinPoint joinPoint, AdapterDelegationEvent annotation) {
        delegate.beginAdapterDelegationEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endAdapterDelegationEvent(JoinPoint joinPoint, AdapterDelegationEvent annotation) {
        delegate.endAdapterDelegationEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @Before("@annotation(annotation)")
    public void beginOutboundMessageEvent(JoinPoint joinPoint, OutboundMessageEvent annotation) {
        delegate.beginOutboundMessageEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endOutboundMessageEvent(JoinPoint joinPoint, OutboundMessageEvent annotation) {
        delegate.endOutboundMessageEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @Before("@annotation(annotation)")
    public void beginOutboundProcessingEvent(JoinPoint joinPoint, OutboundProcessingEvent annotation) {
        delegate.beginOutboundProcessingEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endOutboundProcessingEvent(JoinPoint joinPoint, OutboundProcessingEvent annotation) {
        delegate.endOutboundProcessingEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @Before("@annotation(annotation)")
    public void beginNwhinInvocationEvent(JoinPoint joinPoint, NwhinInvocationEvent annotation) {
        delegate.beginNwhinInvocationEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @After("@annotation(annotation)")
    public void endNwhinInvocationEvent(JoinPoint joinPoint, NwhinInvocationEvent annotation) {
        delegate.endNwhinInvocationEvent(joinPoint.getArgs(), annotation.serviceType(), annotation.version());
    }

    @AfterThrowing("@annotation(gov.hhs.fha.nhinc.aspect.InboundMessageEvent) &&"
            + "@annotation(gov.hhs.fha.nhinc.aspect.InboundProcessingEvent) &&"
            + "@annotation(gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent) && "
            + "@annotation(gov.hhs.fha.nhinc.aspect.OutboundMessageEvent) && "
            + "@annotation(gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent) && "
            + "@annotation(gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent)")
    public void failEvent(JoinPoint joinPoint) {
        delegate.failEvent(joinPoint.getArgs());
    }

}
