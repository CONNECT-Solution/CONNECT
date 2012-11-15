package gov.hhs.fha.nhinc.direct.xdr;

import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectMailSenderTest {

    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
        DirectUnitTestUtil.writeSmtpAgentConfig();
    }
    
    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        DirectUnitTestUtil.removeSmtpAgentConfig();
    }
    
	@Test
	public void test() {
		DirectMailSender sender = new DirectMailSender() {
			@Override
			protected String getContextXml() {
		    	return "test.direct.appcontext.xml";
		    }
		};
		
		assertNotNull(sender.getDirectMailClient());
	}

}
