/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.entity.proxy.service;

import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class EntityDocRetrieveSecuredServicePortDescriptor extends
        SOAP12ServicePortDescriptor<EntityDocRetrieveSecuredPortType> {

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrievesecured";
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
    public Class<EntityDocRetrieveSecuredPortType> getPortClass() {
        return EntityDocRetrieveSecuredPortType.class;
    }

}
