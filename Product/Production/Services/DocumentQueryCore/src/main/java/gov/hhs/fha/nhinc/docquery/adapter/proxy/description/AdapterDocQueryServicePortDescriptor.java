/**
 *
 */
package gov.hhs.fha.nhinc.docquery.adapter.proxy.description;

import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class AdapterDocQueryServicePortDescriptor implements ServicePortDescriptor<AdapterDocQueryPortType> {
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:"
            + "adapterdocquery:AdapterDocQueryRequestMessage";

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
    public Class<AdapterDocQueryPortType> getPortClass() {
        return AdapterDocQueryPortType.class;
    }
}
