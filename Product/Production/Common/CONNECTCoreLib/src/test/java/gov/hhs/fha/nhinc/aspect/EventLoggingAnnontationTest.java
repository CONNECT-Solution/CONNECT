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

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class EventLoggingAnnontationTest {

    private Class<?> annotationClass;
    private String className;

    public EventLoggingAnnontationTest(Class<?> annotationClass) {
        this.annotationClass = annotationClass;
        className = annotationClass.getCanonicalName();
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { NwhinInvocationEvent.class }, { OutboundMessageEvent.class },
                { OutboundProcessingEvent.class }, { InboundMessageEvent.class }, { InboundProcessingEvent.class },
                { AdapterDelegationEvent.class } };
        return Arrays.asList(data);
    }

    @Test
    public void verifyServiceTypeMethod() throws Throwable {
        Method serviceTypeMethod = annotationClass.getMethod("serviceType");
        assertNotNull(className + " has method serviceType", serviceTypeMethod);
        assertTrue(String.class.isAssignableFrom(serviceTypeMethod.getReturnType()));

    }

    @Test
    public void verifyVersionMethod() throws Throwable {
        Method versionMethod = annotationClass.getMethod("version");
        assertNotNull(className + " has method version", versionMethod);
        assertTrue(String.class.isAssignableFrom(versionMethod.getReturnType()));

    }

    @Test
    public void verifyBeforeBuilderMethod() throws Throwable {
        Method descriptionBuilderMethod = annotationClass.getMethod("beforeBuilder");
        assertNotNull(className + " has method beforeBuilder", descriptionBuilderMethod);
        assertTrue(BaseEventDescriptionBuilder.class.getClass()
                .isAssignableFrom(descriptionBuilderMethod.getReturnType()));

    }

    @Test
    public void verifyAfterReturningBuilderMethod() throws Throwable {
        Method descriptionBuilderMethod = annotationClass.getMethod("afterReturningBuilder");
        assertNotNull(className + " has method afterReturningBuilder", descriptionBuilderMethod);
        assertTrue(BaseEventDescriptionBuilder.class.getClass()
                .isAssignableFrom(descriptionBuilderMethod.getReturnType()));

    }

    @Test
    public void verifyRetentionAnnotation() {
        Retention r = annotationClass.getAnnotation(Retention.class);

        assertNotNull(className + " has retention annotation", r);

        assertEquals(RetentionPolicy.RUNTIME, r.value());
    }

    @Test
    public void verifyTargetAnnotation() {
        Target t = annotationClass.getAnnotation(Target.class);
        assertNotNull(className + " has target annotation", t);
        assertEquals(ElementType.METHOD, t.value()[0]);
    }

    @Test
    public void verifyInhertitedAnnotation() {
        Inherited i = annotationClass.getAnnotation(Inherited.class);
        assertNotNull(className + " has inhertied annotation", i);

    }

}
