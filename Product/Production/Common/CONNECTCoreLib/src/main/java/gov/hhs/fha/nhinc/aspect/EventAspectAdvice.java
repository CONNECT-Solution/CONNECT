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

import gov.hhs.fha.nhinc.event.AssertionExtractor;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.EventRecorder;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author zmelnick
 *
 */
@Aspect
public class EventAspectAdvice {

    @Autowired
    @Qualifier("inboundMessageAdviceDelegate")
    private EventAdviceDelegate inboundMessageAdviceDelegate;
    @Autowired
    @Qualifier("inboundProcessingAdviceDelegate")
    private EventAdviceDelegate inboundProcessingAdviceDelegate;
    @Autowired
    @Qualifier("adapterDelegationAdviceDelegate")
    private EventAdviceDelegate adapterDelegationAdviceDelegate;
    @Autowired
    @Qualifier("outboundMessageAdviceDelegate")
    private EventAdviceDelegate outboundMessageAdviceDelegate;
    @Autowired
    @Qualifier("outboundProcessingAdviceDelegate")
    private EventAdviceDelegate outboundProcessingAdviceDelegate;
    @Autowired
    @Qualifier("nwhinInvocationAdviceDelegate")
    private EventAdviceDelegate nwhinInvocationAdviceDelegate;
    @Autowired
    @Qualifier("eventmanager")
    private EventRecorder eventRecorder;

    @AfterThrowing(pointcut = "@annotation(gov.hhs.fha.nhinc.aspect.LogFailures)", throwing = "e")
    public void logFailureAnnotation(JoinPoint joinPoint, Throwable e) {
        logFailure(joinPoint, e, "Event Service", "0.0");
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

    /**
     * This method ensures that a supported event throws one BEGIN_* event and corresponding END_* or
     * MESSAGE_PROCESSING_FAILED event.
     *
     * In the case where an invocation of a proxy layer throws an exception, it will be handled by the Aspect according
     * to exception thrown. In both cases, there will be a MESSAGE_PROCESSING_FAILED event logged, and no corresponding
     * END_* event.
     *
     * If the exception is an instance of ErrorEventException, then the event will be logged as a failure, but the
     * exception will be suppressed. This will allow for services such as Document Query in which the response must
     * contain a RegistryErrorList body element in the SOAP response and should not produce a SOAP fault. In such
     * situations, the ErrorEventException should return an override object to be used as the result of the method
     * invocation. This will allow the service to both log the exception raised, log the failed processing event, suppress
     * logging the corresponding END_* event, and satisfy the requirements of the given service spec by returning a
     * RegistryErrorList containing the errors that occurred.
     *
     * If the exception is any other type other than ErrorEventException, the event will be logged as a failure, with
     * no corresponding END_* event and the exception will be re-thrown.
     *
     * @param joinPoint
     * @param delegate
     * @param beforeBuilder
     * @param afterBuilder
     * @param serviceType
     * @param version
     * @return
     * @throws Throwable
     */
    public Object logEvents(ProceedingJoinPoint joinPoint, EventAdviceDelegate delegate, Class<? extends EventDescriptionBuilder> beforeBuilder,
        Class<? extends EventDescriptionBuilder> afterBuilder, String serviceType, String version) throws Throwable {

        // Small workaround for getting which version we hit at the individual layers on the END_* events.
        // If the version is blank (Due to the fact that the service might be used for more than one version), the begin
        // delegate will attempt to pull from the NhinTargetSystemType. We return that description to grab the Action
        String resolvedVersion = version;
        EventDescription eventDescription = delegate.begin(joinPoint.getArgs(), serviceType, resolvedVersion, beforeBuilder);
        if (eventDescription != null && StringUtils.isBlank(resolvedVersion)) {
            resolvedVersion = eventDescription.getVersion();
        }

        Object value = null;
        boolean hasFailure = false;
        try {
            value = joinPoint.proceed();
        } catch (ErrorEventException e) {
            // Log the failure, and handle the exception. Service should still return a value in this case
            logFailure(joinPoint, e, serviceType, resolvedVersion);
            value =  e.getReturnOverride();
            hasFailure = true;

            // Make sure there is a return override, otherwise we need to re-throw the error
            if (value == null) {
                throw e;
            }
        } catch (Throwable e) {
            // We don't want to swallow the exception if its not an ErrorEventException.
            // We also don't want the delegate to log the END_* event.
            logFailure(joinPoint, e, serviceType, resolvedVersion);
            throw e;
        }
        // Delegate should only log either an END_* if we did not log a MessageProcessingFailed event.
        if (!hasFailure) {
            delegate.end(joinPoint.getArgs(), serviceType, resolvedVersion, afterBuilder, value);
        }
        return value;
    }

    public void logFailure(JoinPoint joinPoint, Throwable e, String service, String version) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(sig.getDeclaringType()); //NOSONAR
        log.info("{} threw an exception: {}", sig.getName(), e.getMessage());

        if (eventRecorder != null && eventRecorder.isRecordEventEnabled()) {
            ErrorEventBuilder builder = new ErrorEventBuilder();

            builder.setAssertion(AssertionExtractor.getAssertion(joinPoint.getArgs()));

            //We want to get the original cause, we dont want to log ErrorEventException as the source each time.
            Throwable ex = e;
            while (ex instanceof ErrorEventException && ex.getCause() != null) {
                ex = ex.getCause();
            }

            builder.setThrowable(ex);
            builder.setInvoker(sig.getDeclaringType().getName());
            builder.setMethod(sig.getName());
            builder.setService(service);
            builder.setVersion(version);

            builder.buildMessageID();
            builder.buildTransactionID();
            builder.buildDescription();

            eventRecorder.recordEvent(builder.getEvent());
        }
    }

}
