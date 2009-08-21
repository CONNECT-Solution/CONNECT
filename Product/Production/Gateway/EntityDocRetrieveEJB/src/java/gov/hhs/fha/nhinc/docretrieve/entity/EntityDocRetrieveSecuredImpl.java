package gov.hhs.fha.nhinc.docretrieve.entity;

import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class EntityDocRetrieveSecuredImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityDocRetrieveSecuredImpl.class);

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body, WebServiceContext context)
    {
        // Collect assertion
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();

        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus("From EntityDocRetrieveSecuredImpl");
        response.setRegistryResponse(responseType);
        return response;
    }
    
}
