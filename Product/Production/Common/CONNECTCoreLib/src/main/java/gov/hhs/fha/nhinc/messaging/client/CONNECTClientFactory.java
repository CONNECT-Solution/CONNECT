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
public interface CONNECTClientFactory<T> {
    public CONNECTClient<T> getCONNECTClientSecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion);

    public CONNECTClient<T> getCONNECTClientUnsecured(ServicePortDescriptor<T> portDescriptor, String url,
            AssertionType assertion);
}
