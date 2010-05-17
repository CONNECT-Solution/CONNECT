package gov.hhs.fha.nhinc.lift;

import gov.hhs.fha.nhinc.lift.proxy.properties.interfaces.ProducerProxyPropertiesFacade;
import gov.hhs.fha.nhinc.lift.proxy.server.SSLServer;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;

/**
 *
 * @author webbn
 */
@RunWith(JMock.class)
public class ServerAppTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    public ServerAppTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
/*
    @Test
    public void testCreateSocketAddr() {
        // Create mock objects
        final Log mockLog = context.mock(Log.class);

        ServerApp app = new ServerApp() {

            @Override
            protected Log createLogger() {
                return mockLog;
            }
        };

        try {
            // Set expectations
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            InetSocketAddress addr = app.createSocketAddr();
            assertNotNull("Created SocketAddr was null", addr);
            String proxyAddr = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_ADDRESS);
            String proxyPort = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_PROXY_PORT);
            assertEquals("Proxy IP ", proxyAddr, addr.getAddress().getHostAddress());
            assertEquals("Proxy Port ", proxyPort, Integer.toString(addr.getPort()));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception calling createSocketAddr: " + ex.getMessage());
        }
    }
 * */

    @Test
    public void testCreateProxyProps() {
        ServerApp app = new ServerApp();
        try {
            ProducerProxyPropertiesFacade props = app.setProxyProps();
            String loc = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_KEYSTORE);
            String pass = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.LIFT_KEYSTOREPASS);
            assertEquals("Keystore", loc, System.getProperty("javax.net.ssl.keyStore"));
            assertEquals("Keystore Password", pass, System.getProperty("javax.net.ssl.keyStorePassword"));

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception calling setProxyProps: " + ex.getMessage());
        }
    }

/*    @Test
    public void testCreateServer() {
        final Log mockLog = context.mock(Log.class);

        ServerApp app = new ServerApp(){

            @Override
            protected Log createLogger() {
                return mockLog;
            }
        };
        try {
            // Set expectations
            context.checking(new Expectations() {

                {
                    allowing(mockLog).isDebugEnabled();
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });

            SSLServer serv = (SSLServer)app.createServer();
            serv.getServer().close();

        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception calling createServer: " + ex.getMessage());
        }
    } */
}
