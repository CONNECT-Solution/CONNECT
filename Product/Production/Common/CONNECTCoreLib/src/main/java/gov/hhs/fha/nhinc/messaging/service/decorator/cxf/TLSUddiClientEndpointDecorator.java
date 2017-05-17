/**
 *
 */
package gov.hhs.fha.nhinc.messaging.service.decorator.cxf;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.transport.http.HTTPConduit;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author mpnguyen
 *
 */
public class TLSUddiClientEndpointDecorator<T> extends TLSClientServiceEndpointDecorator<T> {
    private static final Logger LOG = LoggerFactory.getLogger(TLSUddiClientEndpointDecorator.class);
    /**
     * @param decoratoredEndpoint
     */
    public TLSUddiClientEndpointDecorator(final ServiceEndpoint<T> decoratoredEndpoint) {
        super(decoratoredEndpoint);
    }
    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.messaging.service.decorator.cxf.TLSClientServiceEndpointDecorator#configure()
     */
    @Override
    public void configure() {
        super.configure();
        final String protocol = getSecureProtocol();
        LOG.info("TLS support versions {}", protocol);
        HTTPConduit conduit = getHttpConduit();
        TLSClientParameters tlsCP = getTlsClientFactory().getTLSClientParameters(protocol);
        conduit.setTlsClientParameters(tlsCP);
    }

    /**
     * @return
     */
    private String getSecureProtocol() {
        String secureProtocol = null;
        try {
            secureProtocol = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.UDDI_TLS);
            LOG.debug("Retrieve UDDI {} from {} property",secureProtocol, NhincConstants.GATEWAY_PROPERTY_FILE);
            // convert test1;test2 into test1,test2
            secureProtocol = StringUtils.replace(secureProtocol, ";", ",");
        } catch (final PropertyAccessException e) {
            LOG.warn("Unable to retrieve {} {}",NhincConstants.UDDI_TLS, e.getLocalizedMessage(), e);
        }
        return secureProtocol;

    }
    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

}
