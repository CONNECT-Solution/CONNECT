/**
 *
 */
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Ignore;

import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTTestClient;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.service.port.TestServicePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mpnguyen
 *
 */
public class TLSUddiClientEndpointDecoratorTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testTLSDefaultConfiguration() throws PropertyAccessException {
        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);
        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "UDDI.TLS")).thenReturn("TLSv1.2");
        CONNECTClient<TestServicePortType> uddiClient = createClient(mockPropAccessor);
        Assert.assertNotNull(uddiClient);

        Client clientProxy = ClientProxy.getClient(uddiClient.getPort());
        HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();
        TLSClientParameters tlsCP = conduit.getTlsClientParameters();
        assertTrue(tlsCP.isDisableCNCheck());

        // Assert.assertEquals("TLSv1.2", tlsCP.getSecureSocketProtocol());
    }

    @Test
    @Ignore
    public void testTLSConfiguration() throws PropertyAccessException {
        final PropertyAccessor mockPropAccessor = mock(PropertyAccessor.class);
        when(mockPropAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "UDDI.TLS"))
        .thenReturn("TLSv1,TLSv1.1");
        CONNECTClient<TestServicePortType> uddiClient = createClient(mockPropAccessor);
        Assert.assertNotNull(uddiClient);
        Client clientProxy = ClientProxy.getClient(uddiClient.getPort());
        HTTPConduit conduit = (HTTPConduit) clientProxy.getConduit();
        TLSClientParameters tlsCP = conduit.getTlsClientParameters();
        assertTrue(tlsCP.isDisableCNCheck());
        Assert.assertEquals("TLSv1,TLSv1.1", tlsCP.getSecureSocketProtocol());
    }

    private CONNECTClient<TestServicePortType> createClient(final PropertyAccessor propAccessor)
        throws PropertyAccessException {
        CONNECTTestClient<TestServicePortType> testClient = new CONNECTTestClient<>(new TestServicePortDescriptor());

        ServiceEndpoint<TestServicePortType> serviceEndpoint = testClient.getServiceEndpoint();
        final TLSUddiClientEndpointDecorator uddiDecorator = new TLSUddiClientEndpointDecorator(
            serviceEndpoint) {
            @Override
            protected PropertyAccessor getPropertyAccessor() {
                return propAccessor;
            }

        };

        uddiDecorator.configure();

        return testClient;
    }

}
