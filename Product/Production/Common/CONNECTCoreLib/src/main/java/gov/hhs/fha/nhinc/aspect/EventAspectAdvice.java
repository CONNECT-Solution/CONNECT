/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.event.AssertionExtractor;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventDirector;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zmelnick
 *
 */
@Aspect
public class EventAspectAdvice {

    private EventAdviceDelegate inboundMessageAdviceDelegate;

    private EventAdviceDelegate inboundProcessingAdviceDelegate;

    private EventAdviceDelegate adapterDelegationAdviceDelegate;

    private EventAdviceDelegate outboundMessageAdviceDelegate;

    private EventAdviceDelegate outboundProcessingAdviceDelegate;

    private EventAdviceDelegate nwhinInvocationAdviceDelegate;

    private EventRecorder eventRecorder;



    @Before("@annotation(annotation)")
    public void beginInboundMessageEvent(JoinPoint joinPoint, InboundMessageEvent annotation) {
//        inboundMessageAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endInboundMessageEvent(JoinPoint joinPoint, InboundMessageEvent annotation, Object returnValue) {
//        inboundMessageAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    @Before("@annotation(annotation)")
    public void beginInboundProcessingEvent(JoinPoint joinPoint, InboundProcessingEvent annotation) {
//        inboundProcessingAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endInboundProcessingEvent(JoinPoint joinPoint, InboundProcessingEvent annotation, Object returnValue) {
//        inboundProcessingAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    @Before("@annotation(annotation)")
    public void beginAdapterDelegationEvent(JoinPoint joinPoint, AdapterDelegationEvent annotation) {
//        adapterDelegationAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endAdapterDelegationEvent(JoinPoint joinPoint, AdapterDelegationEvent annotation, Object returnValue) {
//        adapterDelegationAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    @Before("@annotation(annotation)")
    public void beginOutboundMessageEvent(JoinPoint joinPoint, OutboundMessageEvent annotation) {
//        outboundMessageAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endOutboundMessageEvent(JoinPoint joinPoint, OutboundMessageEvent annotation, Object returnValue) {
//        outboundMessageAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    @Before("@annotation(annotation)")
    public void beginOutboundProcessingEvent(JoinPoint joinPoint, OutboundProcessingEvent annotation) {
//        outboundProcessingAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endOutboundProcessingEvent(JoinPoint joinPoint, OutboundProcessingEvent annotation,
            Object returnValue) {
//        outboundProcessingAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    @Before("@annotation(annotation)")
    public void beginNwhinInvocationEvent(JoinPoint joinPoint, NwhinInvocationEvent annotation) {
//        nwhinInvocationAdviceDelegate.begin(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.beforeBuilder());
    }

    @AfterReturning(pointcut = "@annotation(annotation)", returning = "returnValue")
    public void endNwhinInvocationEvent(JoinPoint joinPoint, NwhinInvocationEvent annotation, Object returnValue) {
//        nwhinInvocationAdviceDelegate.end(joinPoint.getArgs(), annotation.serviceType(), annotation.version(),
//                annotation.afterReturningBuilder(), returnValue);
    }

    /*
     * Passthrough modes do not have any Event annotations on them. We want to support the ability to log exceptions
     * for both Standard and Passthrough. If the method is annotated with both for some reason, we only process the
     * LogFailures annotation to prevent recording duplicate entries for the failure.
     */
    /*
    @AfterThrowing(pointcut = "@annotation(gov.hhs.fha.nhinc.aspect.InboundMessageEvent) ||"
        + "@annotation(gov.hhs.fha.nhinc.aspect.InboundProcessingEvent) ||"
        + "@annotation(gov.hhs.fha.nhinc.aspect.AdapterDelegationEvent) || "
        + "@annotation(gov.hhs.fha.nhinc.aspect.OutboundMessageEvent) || "
        + "@annotation(gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent) || "
        + "@annotation(gov.hhs.fha.nhinc.aspect.NwhinInvocationEvent)", throwing = "e")
    */
    public void failEvent(JoinPoint joinPoint, Throwable e) {
        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            MethodSignature sig = (MethodSignature) joinPoint.getSignature();
            Method method = sig.getMethod();

            // Check if the method is annotated with @LogFailures. We dont want to log the event twice.
            if (method.getAnnotation(LogFailures.class) != null) {
                Logger log = LoggerFactory.getLogger(sig.getDeclaringType()); //NOSONAR
                log.warn("Method {} has multiple logging aspects. Will only process LogFailures annotation... ", method.getName());
            } else {
                logFailure(joinPoint, e);
            }
        }
    }


    @AfterThrowing(pointcut = "@annotation(gov.hhs.fha.nhinc.aspect.LogFailures)", throwing = "e")
    public void logFailure(JoinPoint joinPoint, Throwable e) {
        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            MethodSignature sig = (MethodSignature) joinPoint.getSignature();

            Logger log = LoggerFactory.getLogger(sig.getDeclaringType()); //NOSONAR
            log.info("{} threw an exception: {}", sig.getName(), e.getMessage());

            ErrorEventBuilder builder = new ErrorEventBuilder();

            builder.setAssertion(AssertionExtractor.getAssertion(joinPoint.getArgs()));
            builder.setThrowable(e);
            builder.setInvoker(sig.getDeclaringType().toString());
            builder.setMethod(sig.getName());


            EventDirector director = new EventDirector();
            director.setEventBuilder(builder);
            director.constructEvent();

            eventRecorder.recordEvent(director.getEvent());

        }

    }

    @Around("@annotation(anno)")
    public Object logInboundMessageEvent(ProceedingJoinPoint joinPoint, InboundMessageEvent anno) throws Throwable {
        return logEvents(joinPoint, inboundMessageAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    @Around("@annotation(anno)")
    public Object logInboundProcessingEvent(ProceedingJoinPoint joinPoint, InboundProcessingEvent anno) throws Throwable {
        return logEvents(joinPoint, inboundProcessingAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    @Around("@annotation(anno)")
    public Object logAdapterDelegationEvent(ProceedingJoinPoint joinPoint, AdapterDelegationEvent anno) throws Throwable {
        return logEvents(joinPoint, adapterDelegationAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    @Around("@annotation(anno)")
    public Object logOutboundMessageEvent(ProceedingJoinPoint joinPoint, OutboundMessageEvent anno) throws Throwable {
        return logEvents(joinPoint, outboundMessageAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    @Around("@annotation(anno)")
    public Object logOutboundProcessingEvent(ProceedingJoinPoint joinPoint, OutboundProcessingEvent anno) throws Throwable {
        return logEvents(joinPoint, outboundProcessingAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    @Around("@annotation(anno)")
    public Object logNwhinInvocationEvent(ProceedingJoinPoint joinPoint, NwhinInvocationEvent anno) throws Throwable {
        return logEvents(joinPoint, nwhinInvocationAdviceDelegate, anno.beforeBuilder(),
            anno.afterReturningBuilder(), anno.serviceType(), anno.version());
    }

    public Object logEvents(ProceedingJoinPoint joinPoint, EventAdviceDelegate delegate, Class<? extends EventDescriptionBuilder> beforeBuilder,
        Class<? extends EventDescriptionBuilder> afterBuilder, String serviceType, String version) throws Throwable {
        delegate.begin(joinPoint.getArgs(), serviceType, version, beforeBuilder);

        Object value = null;
        boolean hasFailure = false;
        try {
            value = joinPoint.proceed();
        } catch (ErrorEventException e) {
            logFailure(joinPoint, e);
            value =  e.getReturnOverride();
            hasFailure = true;
        } catch (Throwable e) {
            // We don't want to swallow the exception if its not an ErrorEventException.
            // We also don't want the delegate to log the END_* event.
            logFailure(joinPoint, e);
            throw e;
        }
        // Delegate should only log either an END_* if we did not log a MessageProcessingFailed event.
        if (!hasFailure) {
            delegate.end(joinPoint.getArgs(), serviceType, version, afterBuilder, value);
        }
        return value;
    }


    @Autowired
    public void setInboundMessageAdviceDelegate(EventAdviceDelegate inboundMessageAdviceDelegate) {
        this.inboundMessageAdviceDelegate = inboundMessageAdviceDelegate;
    }

    @Autowired
    public void setInboundProcessingAdviceDelegate(EventAdviceDelegate inboundProcessingAdviceDelegate) {
        this.inboundProcessingAdviceDelegate = inboundProcessingAdviceDelegate;
    }

    @Autowired
    public void setAdapterDelegationAdviceDelegate(EventAdviceDelegate adapterDelegationAdviceDelegate) {
        this.adapterDelegationAdviceDelegate = adapterDelegationAdviceDelegate;
    }

    @Autowired
    public void setOutboundMessageAdviceDelegate(EventAdviceDelegate outboundMessageAdviceDelegate) {
        this.outboundMessageAdviceDelegate = outboundMessageAdviceDelegate;
    }

    @Autowired
    public void setOutboundProcessingAdviceDelegate(EventAdviceDelegate outboundProcessingAdviceDelegate) {
        this.outboundProcessingAdviceDelegate = outboundProcessingAdviceDelegate;
    }

    @Autowired
    public void setNwhinInvocationAdviceDelegate(EventAdviceDelegate nwhinInvocationAdviceDelegate) {
        this.nwhinInvocationAdviceDelegate = nwhinInvocationAdviceDelegate;
    }

    @Autowired
    public void setEventRecorder(EventRecorder eventRecorder) {
        this.eventRecorder = eventRecorder;
    }

}
