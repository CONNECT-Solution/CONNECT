/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy.service;

import gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class AdapterDocRetrieveSecuredServicePortDescriptor implements
        ServicePortDescriptor<AdapterDocRetrieveSecuredPortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrievesecured";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":"
            + "RespondingGateway_CrossGatewayRetrieveSecuredRequestMessage";

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<AdapterDocRetrieveSecuredPortType> getPortClass() {
        return AdapterDocRetrieveSecuredPortType.class;
    }

}
