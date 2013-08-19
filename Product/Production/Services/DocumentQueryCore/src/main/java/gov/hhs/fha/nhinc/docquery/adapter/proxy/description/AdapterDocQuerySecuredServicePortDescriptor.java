/**
 *
 */
package gov.hhs.fha.nhinc.docquery.adapter.proxy.description;

import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;

/**
 * @author mweaver
 * 
 */
public class AdapterDocQuerySecuredServicePortDescriptor extends
        SOAP12ServicePortDescriptor<AdapterDocQuerySecuredPortType> {
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:"
            + "adapterdocquerysecured:AdapterDocQueryRequestSecuredMessage";

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
    public Class<AdapterDocQuerySecuredPortType> getPortClass() {
        return AdapterDocQuerySecuredPortType.class;
    }
}
