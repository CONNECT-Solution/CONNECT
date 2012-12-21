package gov.hhs.fha.nhinc.callback.openSAML;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Test;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Subject;

import com.sun.identity.saml2.assertion.Evidence;

/**
 * @author achidamb
 *
 */
public class OpenSAML2ComponentBuilderTest {
    
    @Test
    public void testCreateAssertionPassed() {        
       String uuid = "2345";
       Assertion assertion=  (Assertion) OpenSAML2ComponentBuilder.getInstance().createAssertion(uuid);
       assertEquals(uuid , assertion.getID());
    }
    
    @Test
    public void testCreateAssertionUUIDGenerated() {       
        String uuid = "_".concat(String.valueOf(UUID.randomUUID()));;
        Assertion assertion=  (Assertion) OpenSAML2ComponentBuilder.getInstance().createAssertion(uuid);
        assertEquals(uuid, assertion.getID());
    }
    
   
}
