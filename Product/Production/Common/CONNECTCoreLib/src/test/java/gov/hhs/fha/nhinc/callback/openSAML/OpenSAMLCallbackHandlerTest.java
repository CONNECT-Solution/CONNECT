/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import java.util.Collections;
import java.util.HashMap;

import javax.security.auth.callback.Callback;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.w3c.dom.Element;

import com.sun.xml.wss.impl.callback.SAMLCallback;

/**
 * @author bhumphrey
 * 
 */
public class OpenSAMLCallbackHandlerTest {
	
	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
	
	final SAMLCallback mockCallback = context.mock(SAMLCallback.class);
	final SAMLAssertionBuilderFactory mockAssertionBuilderFactory = context.mock(SAMLAssertionBuilderFactory.class);
	final SAMLAssertionBuilder mockAssertionBuilder = context.mock(SAMLAssertionBuilder.class);
	final Element mockElement = context.mock(Element.class);
	final CallbackProperties mockProperties = context.mock(CallbackProperties.class);
	
	@Test
	public void testHOKAssertionType() throws Exception {
		OpenSAMLCallbackHandler handler = new OpenSAMLCallbackHandler(mockAssertionBuilderFactory);
		Callback[] callbacks = {mockCallback};
		
		context.checking(new Expectations() {{
		   oneOf(mockAssertionBuilder).build(mockProperties);
		   will(returnValue(mockElement));
			
			
		   oneOf(mockAssertionBuilderFactory).getBuilder(with(SAMLCallback.HOK_ASSERTION_TYPE));
		   will(returnValue(mockAssertionBuilder));
			
		   allowing(mockCallback).getRuntimeProperties();
		   will(returnValue(Collections.EMPTY_MAP));
	       allowing(mockCallback).getConfirmationMethod(); 
	       will(returnValue(SAMLCallback.HOK_ASSERTION_TYPE));
	       oneOf(mockCallback).getAssertionElement();
	       oneOf(mockCallback).setAssertionElement(with(same(mockElement)));
	       
		}});

		
		handler.handle(callbacks);
	}
	
	
}
