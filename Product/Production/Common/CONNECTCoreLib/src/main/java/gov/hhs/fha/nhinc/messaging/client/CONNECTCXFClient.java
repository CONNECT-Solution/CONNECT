/**
 * 
 */
package gov.hhs.fha.nhinc.messaging.client;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.service.ServiceEndpoint;
import gov.hhs.fha.nhinc.messaging.service.decorator.cxf.WsAddressingServiceEndpointDecorator;
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
        super();

        String wsAddressingAction = portDescriptor.getWSAddressingAction();

        ServicePortBuilder<T> portBuilder = new CXFServicePortBuilder<T>(portDescriptor);
        
        serviceEndpoint = super.configureBasePort(portBuilder.createPort(), url);
      
        // CXF specific decorator configuration
        serviceEndpoint = new WsAddressingServiceEndpointDecorator<T>(serviceEndpoint, url, wsAddressingAction,
                assertion);
    }

    public T getPort() {
        return serviceEndpoint.getPort();
    }
    
}
