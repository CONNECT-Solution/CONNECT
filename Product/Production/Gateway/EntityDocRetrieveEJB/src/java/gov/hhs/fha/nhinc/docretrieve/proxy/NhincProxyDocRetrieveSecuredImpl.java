package gov.hhs.fha.nhinc.docretrieve.proxy;

import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveSecuredImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocRetrieveSecuredImpl.class);

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context)
    {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();

        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus("From NhincProxyDocRetrieveSecuredImpl");
        response.setRegistryResponse(responseType);
        return response;
    }
    
}
