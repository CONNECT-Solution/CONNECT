/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.entity.proxy.service;

import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class EntityDocRetrieveUnsecuredServicePortDescriptor implements
        ServicePortDescriptor<EntityDocRetrievePortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrieve";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":RespondingGateway_CrossGatewayRetrieve";

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
    public Class<EntityDocRetrievePortType> getPortClass() {
        return EntityDocRetrievePortType.class;
    }

}
