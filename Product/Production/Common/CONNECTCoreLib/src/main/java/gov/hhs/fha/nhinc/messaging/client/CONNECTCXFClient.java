/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.port.CXFServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortBuilder;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author bhumphrey
 * 
 */
public abstract class CONNECTCXFClient<T> extends CONNECTClient<T> {

    protected ServiceEndpoint<T> serviceEndpoint = null;

    CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion) {
        this(portDescriptor, url, assertion, new CXFServicePortBuilder<T>(portDescriptor));
    }

    CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion,
            ServicePortBuilder<T> portBuilder) {
        serviceEndpoint = super.configureBasePort(portBuilder.createPort(), url);
    }

    CONNECTCXFClient(ServicePortDescriptor<T> portDescriptor, String url, AssertionType assertion,
            ServicePortBuilder<T> portBuilder, String subscriptionId) {
        serviceEndpoint = super.configureBasePort(portBuilder.createPort(), subscriptionId);
    }
    
   

    public T getPort() {
        return serviceEndpoint.getPort();
    }

}
