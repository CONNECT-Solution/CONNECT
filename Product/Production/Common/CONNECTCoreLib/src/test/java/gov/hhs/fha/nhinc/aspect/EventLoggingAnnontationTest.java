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
        this.className = annotationClass.getCanonicalName();
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
        assertTrue(BaseEventDescriptionBuilder.class.getClass().isAssignableFrom(
                descriptionBuilderMethod.getReturnType()));

    }

    @Test
    public void verifyAfterReturningBuilderMethod() throws Throwable {
        Method descriptionBuilderMethod = annotationClass.getMethod("afterReturningBuilder");
        assertNotNull(className + " has method afterReturningBuilder", descriptionBuilderMethod);
        assertTrue(BaseEventDescriptionBuilder.class.getClass().isAssignableFrom(
                descriptionBuilderMethod.getReturnType()));

    }

    @Test
    public void verifyRetentionAnnotation() {
        Retention r = (Retention) annotationClass.getAnnotation(Retention.class);

        assertNotNull(className + " has retention annotation", r);

        assertEquals(RetentionPolicy.RUNTIME, r.value());
    }

    @Test
    public void verifyTargetAnnotation() {
        Target t = (Target) annotationClass.getAnnotation(Target.class);
        assertNotNull(className + " has target annotation", t);
        assertEquals(ElementType.METHOD, t.value()[0]);
    }

    @Test
    public void verifyInhertitedAnnotation() {
        Inherited i = (Inherited) annotationClass.getAnnotation(Inherited.class);
        assertNotNull(className + " has inhertied annotation", i);

    }

}
