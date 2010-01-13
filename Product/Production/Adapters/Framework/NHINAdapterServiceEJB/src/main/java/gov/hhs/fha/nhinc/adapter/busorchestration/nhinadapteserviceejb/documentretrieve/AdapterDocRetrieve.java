/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentretrieve;

import gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Jerry Goodnough
 */
@WebService(serviceName = "AdapterDocRetrieve", portName = "AdapterDocRetrievePortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrieve.AdapterDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrieve", wsdlLocation = "META-INF/wsdl/AdapterDocRetrieve/AdapterDocRetrieve.wsdl")
@Stateless
public class AdapterDocRetrieve implements AdapterDocRetrievePortType {

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return AdapterDocRetrieveImpl.getInstance().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }

}
