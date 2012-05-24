/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import java.io.IOException;

import com.sun.xml.wss.impl.callback.SAMLCallback;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAMLCallbackHandlerTest {

    private Mockery context = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final NameCallback mockNonSAMLCallback = context.mock(NameCallback.class);
    final SAMLCallback mockCallback = context.mock(SAMLCallback.class);
    final SAMLAssertionBuilderFactory mockAssertionBuilderFactory = context.mock(SAMLAssertionBuilderFactory.class);
    final SAMLAssertionBuilder mockAssertionBuilder = context.mock(SAMLAssertionBuilder.class);
    final Element mockElement = context.mock(Element.class);
    final CallbackProperties mockProperties = context.mock(CallbackProperties.class);

    @Test
    public void testHOKAssertionType() throws Exception {
        OpenSAMLCallbackHandler handler = new OpenSAMLCallbackHandler(mockAssertionBuilderFactory);
        Callback[] callbacks = { mockCallback };

        assertEquals(callbacks.length, 1);

        context.checking(new Expectations() {
            {
                allowing(mockAssertionBuilderFactory).getBuilder(with(SAMLCallback.HOK_ASSERTION_TYPE));
                will(returnValue(mockAssertionBuilder));

                oneOf(mockAssertionBuilder).build(with(any(CallbackProperties.class)));
                will(returnValue(mockElement));

                oneOf(mockCallback).getRuntimeProperties();
                will(returnValue(Collections.EMPTY_MAP));

                oneOf(mockCallback).getConfirmationMethod();
                will(returnValue(SAMLCallback.HOK_ASSERTION_TYPE));

                oneOf(mockCallback).getAssertionElement();
                oneOf(mockCallback).setAssertionElement(with(same(mockElement)));

            }
        });

        handler.handle(callbacks);
    }

    @Test(expected=UnsupportedCallbackException.class)
    public void testHandleWithNullBuilder() throws Exception {
        OpenSAMLCallbackHandler handler = new OpenSAMLCallbackHandler(mockAssertionBuilderFactory);
        Callback[] callbacks = { mockCallback };

        context.checking(new Expectations() {
            {
                oneOf(mockAssertionBuilderFactory).getBuilder(with(SAMLCallback.HOK_ASSERTION_TYPE));
                will(returnValue(null));
                
                oneOf(mockCallback).getRuntimeProperties();
                will(returnValue(Collections.EMPTY_MAP));

                oneOf(mockCallback).getConfirmationMethod();
                will(returnValue(SAMLCallback.HOK_ASSERTION_TYPE));
            }
        });
        assertEquals(callbacks.length, 1);
        handler.handle(callbacks);
    }
    
    @Test(expected=UnsupportedCallbackException.class)
    public void testHandleWithNonSAMLCallback() throws Exception {
        OpenSAMLCallbackHandler handler = new OpenSAMLCallbackHandler(mockAssertionBuilderFactory);
        Callback[] callbacks = { mockNonSAMLCallback };

        context.checking(new Expectations() {
            {
                oneOf(mockAssertionBuilderFactory).getBuilder(with(SAMLCallback.HOK_ASSERTION_TYPE));
                will(returnValue(null));
            }
        });
        assertEquals(callbacks.length, 1);
        handler.handle(callbacks);
    }

}
