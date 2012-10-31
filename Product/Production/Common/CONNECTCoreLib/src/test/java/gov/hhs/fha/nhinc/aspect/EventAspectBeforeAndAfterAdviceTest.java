/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author bhumphrey
 * 
 */
@RunWith(value = Parameterized.class)
public class EventAspectBeforeAndAfterAdviceTest {

    private Class<EventAspectAdvice> clazz = EventAspectAdvice.class;
    private Class<?> annotationClass;
    private String methodName;

    public EventAspectBeforeAndAfterAdviceTest(String methodName, Class<?> annotationClass) {
        this.methodName = methodName;
        this.annotationClass = annotationClass;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { "outboundMessageEvent", OutboundMessageEvent.class },
                { "inboundMessageEvent", InboundMessageEvent.class },
                { "outboundProcessingEvent", OutboundProcessingEvent.class },
                { "inboundProcessingEvent", InboundProcessingEvent.class},
                { "nwhinInvocationEvent", NwhinInvocationEvent.class},
                { "adapterDelegationEvent", AdapterDelegationEvent.class} };
        return Arrays.asList(data);
    }

    @Test
    public void verify() throws NoSuchMethodException, SecurityException {
        Method method = clazz.getMethod(methodName, JoinPoint.class, annotationClass);
        assertNotNull("method exsist with correct parameters", method);

        Before beforeAnnotation = method.getAnnotation(Before.class);
        assertNotNull("has before Annotation ", beforeAnnotation);

        assertEquals("@annotation(annotation)", beforeAnnotation.value());

        After afterAnnotation = method.getAnnotation(After.class);
        assertNotNull("has after Annotation", afterAnnotation);

        assertEquals("@annotation(annotation)", afterAnnotation.value());
    }

}
