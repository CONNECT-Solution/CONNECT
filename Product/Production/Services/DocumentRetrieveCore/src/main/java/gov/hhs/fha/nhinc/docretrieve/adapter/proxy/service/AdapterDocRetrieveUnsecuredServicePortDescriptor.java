/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy.service;

import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class AdapterDocRetrieveUnsecuredServicePortDescriptor extends
        SOAP12ServicePortDescriptor<AdapterDocRetrievePortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocretrieve";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":"
            + "RespondingGateway_CrossGatewayRetrieveRequestMessage";

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
    public Class<AdapterDocRetrievePortType> getPortClass() {
        return AdapterDocRetrievePortType.class;
    }

}
