/**
 * 
 */
package gov.hhs.fha.nhinc.callback.openSAML;

import org.junit.Test;

/**
 * @author bhumphrey
 *
 */
public class OpenSAMLCallbackHandlerTest {

    @Test
    public void createHOKSAMLAssertion20Test()
    {
        OpenSAMLCallbackHandler handler = new OpenSAMLCallbackHandler();
        try {
            handler.createHOKSAMLAssertion20();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
