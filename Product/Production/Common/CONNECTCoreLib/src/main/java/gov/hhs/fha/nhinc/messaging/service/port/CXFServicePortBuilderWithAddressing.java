/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.service.port;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;

/**
 * @author mweaver
 *
 */
public class CXFServicePortBuilderWithAddressing<T> extends CachingCXFServicePortBuilder<T> {

    /**
     * @param portDescriptor
     */
    public CXFServicePortBuilderWithAddressing(ServicePortDescriptor<T> portDescriptor) {
        super(portDescriptor);
    }
    
    @Override
    protected void configureJaxWsProxyFactory(JaxWsProxyFactoryBean factory) {
        super.configureJaxWsProxyFactory(factory);
        factory.getFeatures().add(new WSAddressingFeature());
    }
}
