package gov.hhs.fha.nhinc.docquery.proxy;

import gov.hhs.fha.nhinc.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyDocQuerySecured", portName = "NhincProxyDocQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerysecured.NhincProxyDocQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerysecured", wsdlLocation = "META-INF/wsdl/NhincProxyDocQuerySecured/NhincProxyDocQuerySecured.wsdl")
@Stateless
public class NhincProxyDocQuerySecured implements NhincProxyDocQuerySecuredPortType
{
    @Resource
    private WebServiceContext context;

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType body)
    {
        return new NhincProxyDocQuerySecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }
    
}
