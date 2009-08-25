package gov.hhs.fha.nhinc.docretrieve.proxy;

import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhindocretrieve.proxy.NhinDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhindocretrieve.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

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
        log.debug("Begin NhincProxyDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;
        // Collect assertion
        log.debug("Collecting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        log.debug("Creating NHIN doc retrieve proxy");
        NhinDocRetrieveProxyObjectFactory objFactory = new NhinDocRetrieveProxyObjectFactory();
        NhinDocRetrieveProxy docRetrieveProxy = objFactory.getNhinDocRetrieveProxy();

        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        request.setAssertion(assertion);
        request.setNhinTargetSystem(body.getNhinTargetSystem());
        request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());

        log.debug("Calling doc retrieve proxy");
        response = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request);

        log.debug("End NhincProxyDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }
    
}
