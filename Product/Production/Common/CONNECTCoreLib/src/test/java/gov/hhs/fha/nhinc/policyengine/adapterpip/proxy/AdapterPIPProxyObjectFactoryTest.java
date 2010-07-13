package gov.hhs.fha.nhinc.policyengine.adapterpip.proxy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This is a JUNIT test that is used to test the AdapterPIPProxyObjectFactory
 * class.
 *
 * @author Les Westberg
 */
@Ignore //ToDo: Move this test to Integration Suite
public class AdapterPIPProxyObjectFactoryTest
{

    /**
     * Default constructor.
     */
    public AdapterPIPProxyObjectFactoryTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getAdapterPIPProxy method, of class AdapterPIPProxyObjectFactory.
     */
    @Test
    public void testAdapterPIPProxyObjectFactory()
    {
        System.out.println("Begin testAdapterPIPProxyObjectFactory");
        try
        {
            AdapterPIPProxyObjectFactory oFactory = new AdapterPIPProxyObjectFactory();
            AdapterPIPProxy oAdapterPIPProxy = oFactory.getAdapterPIPProxy();
            assertNotNull(oAdapterPIPProxy);
            assertTrue("Adapter PIP was not the default type.", (oAdapterPIPProxy instanceof AdapterPIPProxyNoOpImpl));
        }
        catch (Throwable t)
        {
            System.out.println("Exception in testAdapterPIPProxyObjectFactory: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }

        System.out.println("End testAdapterPIPProxyObjectFactory");

    }
}