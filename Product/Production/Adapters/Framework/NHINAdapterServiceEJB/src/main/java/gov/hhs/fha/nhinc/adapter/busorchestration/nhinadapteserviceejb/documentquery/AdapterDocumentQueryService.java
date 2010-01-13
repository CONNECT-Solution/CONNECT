
package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentquery;

import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * Generated WebService Service which delegates to to the AdapterDocumentQueryServiceImpl.
 *
 * @author Jerry Goodnough
 */
@WebService(serviceName = "AdapterDocQuery", portName = "AdapterDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquery", wsdlLocation = "META-INF/wsdl/AdpaterDocumentQueryService/AdapterDocQuery.wsdl")
@Stateless
public class AdapterDocumentQueryService implements AdapterDocQueryPortType {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        return AdapterDocumentQueryServiceImpl.getInstance().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
