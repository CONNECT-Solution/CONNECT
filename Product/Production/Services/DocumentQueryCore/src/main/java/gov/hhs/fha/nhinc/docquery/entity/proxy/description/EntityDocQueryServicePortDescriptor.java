/**
 *
 */
package gov.hhs.fha.nhinc.docquery.entity.proxy.description;

import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class EntityDocQueryServicePortDescriptor extends SOAP12ServicePortDescriptor<EntityDocQueryPortType> {
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitydocquery:"
            + "RespondingGateway_CrossGatewayQueryRequest";

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
    public Class<EntityDocQueryPortType> getPortClass() {
        return EntityDocQueryPortType.class;
    }

}
