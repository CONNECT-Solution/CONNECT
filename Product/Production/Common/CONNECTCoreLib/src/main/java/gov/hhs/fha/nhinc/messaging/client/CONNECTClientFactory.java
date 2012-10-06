/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author bhumphrey
 * 
 */
public abstract class CONNECTClientFactory {
    abstract public <T> CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion);

    abstract public <T> CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion, String wsAddresingTo);

    abstract public <T> CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion, String wsAddressing, String subscriptionId);

    abstract public <T> CONNECTClient<T> getCONNECTClientUnsecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion);

    public static CONNECTClientFactory getInstance() {
        return new CONNECTCXFClientFactory();
    }
}
