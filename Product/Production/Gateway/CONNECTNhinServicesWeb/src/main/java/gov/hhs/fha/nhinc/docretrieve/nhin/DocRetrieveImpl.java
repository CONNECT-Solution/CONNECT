package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the Web Service implementation of Nhin Doc Retrieve.
 *
 * @author vvickers, Les Westberg
 */
class DocRetrieveImpl
{

    private static Log log = LogFactory.getLog(DocRetrieveImpl.class);

    RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, WebServiceContext context)
    {
        log.debug("Entering DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        NhinDocRetrieveOrchImpl oOrchestrator = new NhinDocRetrieveOrchImpl();
        RetrieveDocumentSetResponseType response = oOrchestrator.respondingGatewayCrossGatewayRetrieve(body, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting DocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return response;
    }

}
