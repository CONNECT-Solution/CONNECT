/**
 * 
 */
package gov.hhs.fha.nhinc.docquery.nhin.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import ihe.iti.xds_b._2007.RespondingGatewayQueryPortType;

/**
 * @author mweaver
 * 
 */
public class NhinDocQueryServicePortDescriptor extends SOAP12ServicePortDescriptor<RespondingGatewayQueryPortType> {
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getPortClass()
     */
    @Override
    public Class<RespondingGatewayQueryPortType> getPortClass() {
        return RespondingGatewayQueryPortType.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor#getWSAddressingAction()
     */
    @Override
    public String getWSAddressingAction() {
        return WS_ADDRESSING_ACTION;
    }
}
