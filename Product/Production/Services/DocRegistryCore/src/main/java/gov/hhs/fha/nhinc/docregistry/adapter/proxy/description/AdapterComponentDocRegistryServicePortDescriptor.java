/**
 * 
 */
package gov.hhs.fha.nhinc.docregistry.adapter.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;

/**
 * @author mweaver
 * 
 */
public class AdapterComponentDocRegistryServicePortDescriptor implements
        ServicePortDescriptor<DocumentRegistryPortType> {
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:RegistryStoredQuery";

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
    public Class<DocumentRegistryPortType> getPortClass() {
        return DocumentRegistryPortType.class;
    }
}
