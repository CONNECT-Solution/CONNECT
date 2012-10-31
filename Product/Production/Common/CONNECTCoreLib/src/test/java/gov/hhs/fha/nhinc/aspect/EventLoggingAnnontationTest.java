package gov.hhs.fha.nhinc.aspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.ElementType;
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

    private Class annotationClass;

    public EventLoggingAnnontationTest(Class annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { NwhinInvocationEvent.class }, 
                { OutboundMessageEvent.class },
                { OutboundProcessingEvent.class },
                { InboundMessageEvent.class },
                { InboundProcessingEvent.class },
                { AdapterDelegationEvent.class } };
        return Arrays.asList(data);
    }

    @Test
    public void verifyServiceTypeMethod() throws Throwable {
        Method serviceTypeMethod = annotationClass.getMethod("serviceType");
        assertNotNull("has method serviceType", serviceTypeMethod);

    }

    @Test
    public void verifyVersionMethod() throws Throwable {
        Method versionMethod = annotationClass.getMethod("version");
        assertNotNull("has method version", versionMethod);

    }

    @Test
    public void verifyRetentionAnnotation() {
        Retention r = (Retention) annotationClass.getAnnotation(Retention.class);

        assertNotNull("has retention annotation", r);

        assertEquals("has retetion of runtime", RetentionPolicy.RUNTIME, r.value());
    }

    @Test
    public void verifyTargetAnnotation() {
        Target t = (Target) annotationClass.getAnnotation(Target.class);
        assertNotNull("has target annotation", t);
        assertEquals("has target of METHOD", ElementType.METHOD, t.value()[0]);
    }

}
