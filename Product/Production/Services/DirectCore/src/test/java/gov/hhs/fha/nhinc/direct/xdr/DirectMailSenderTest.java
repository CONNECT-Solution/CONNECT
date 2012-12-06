package gov.hhs.fha.nhinc.direct.xdr;

import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.direct.DirectClientFactory;
import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
	@Ignore
	public void test() {	    
	    DirectClientFactory testDirectFactory = new DirectClientFactory();
		assertNotNull(testDirectFactory.getDirectClient());
	}

}
