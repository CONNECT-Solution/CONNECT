/**
 *
 */
package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssertionExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(AssertionExtractor.class);

    private AssertionExtractor() {}

    public static AssertionType getAssertion(Object[] args) {
        AssertionType assertion = null;
        for (Object obj : args) {
            assertion = getAssertion(obj);
            if (null != assertion) {
                break;
            }
        }
        return assertion;
    }

    public static AssertionType getAssertion(Object obj) {

        if (obj != null) {
            if (obj instanceof AssertionType) {
                return (AssertionType) obj;
            } else {
                return getAssertionType(obj.getClass().getDeclaredMethods(), obj);
            }
        }

        return null;
    }

    public static AssertionType getAssertionType(Method[] methods, Object obj) {
        for (Method m : methods) {
            if ("getAssertion".equals(m.getName())) {
                try {
                    return (AssertionType) m.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    LOG.error("Unable to extract AssertionType from Object: {}", ex.getLocalizedMessage(), ex);
                }
            }
        }
        return null;
    }
}
