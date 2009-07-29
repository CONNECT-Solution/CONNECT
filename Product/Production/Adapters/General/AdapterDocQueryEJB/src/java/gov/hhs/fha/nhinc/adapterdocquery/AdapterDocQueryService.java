/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocquery;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterDocQuery", portName = "AdapterDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquery", wsdlLocation = "META-INF/wsdl/AdapterDocQueryService/AdapterDocQuery.wsdl")
@Stateless
public class AdapterDocQueryService implements AdapterDocQueryPortType
{

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return new AdapterDocQueryServiceImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
