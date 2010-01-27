package gov.hhs.fha.nhinc.adapterdocquery;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterDocQuerySecured", portName = "AdapterDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerysecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocQuerySecured/AdapterDocQuerySecured.wsdl")
public class AdapterDocQuerySecured {
    @Resource
    private WebServiceContext context;
    
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        return new AdapterDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
