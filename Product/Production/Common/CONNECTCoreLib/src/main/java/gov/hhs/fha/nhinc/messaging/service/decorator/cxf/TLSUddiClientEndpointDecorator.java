/**
 *
 */
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;

/**
 * @author mpnguyen
 *
 */
public class TLSUddiClientEndpointDecorator<T> extends TLSClientServiceEndpointDecorator<T> {
    private static final Logger LOG = LoggerFactory.getLogger(TLSUddiClientEndpointDecorator.class);
    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecorator#configure()
     */
    @Override
    public void configure() {
        super.configure();
        Client client = ClientProxy.getClient(getPort());
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        String protocol = getSecureProtocol();

        TLSClientParameters tlsCP = TLSClientParametersFactory.getInstance().getTLSClientParameters();
        LOG.debug("Secure Protocol uddi vvv5555 {}", protocol);
        tlsCP.setSecureSocketProtocol(protocol);
        conduit.setTlsClientParameters(tlsCP);

    }

    /**
     * @return
     */
    private String getSecureProtocol() {
        String secureProtocol = null;
        try {
            secureProtocol = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "UDDI.TLS");
        } catch (PropertyAccessException e) {
            LOG.warn("Unable to retrive uddi.tls {}", e.getLocalizedMessage(), e);
        }
        return secureProtocol != null ? secureProtocol : "TLSv1";

    }

    /**
     * @param decoratoredEndpoint
     */
    public TLSUddiClientEndpointDecorator(ServiceEndpoint<T> decoratoredEndpoint) {
        super(decoratoredEndpoint);
        LOG.debug("UDDI--Minh version 2");

    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

}
