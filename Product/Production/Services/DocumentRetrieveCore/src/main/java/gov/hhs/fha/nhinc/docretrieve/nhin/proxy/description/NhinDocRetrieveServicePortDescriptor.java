/**
 * 
 */
package gov.hhs.fha.nhinc.docretrieve.nhin.proxy.description;

import gov.hhs.fha.nhinc.messaging.service.port.SOAP12ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RespondingGatewayRetrievePortType;

/**
 * @author achidamb
 * 
 */
public class NhinDocRetrieveServicePortDescriptor extends SOAP12ServicePortDescriptor<RespondingGatewayRetrievePortType> {

    @Override
    public Class<RespondingGatewayRetrievePortType> getPortClass() {
        return RespondingGatewayRetrievePortType.class;
    }

    @Override
    public String getWSAddressingAction() {
        return NhincConstants.DOC_RETRIEVE_WS_ADDRESS_ACTION;
    }

}
