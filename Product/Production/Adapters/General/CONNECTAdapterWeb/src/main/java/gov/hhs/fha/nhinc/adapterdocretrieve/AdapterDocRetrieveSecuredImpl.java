/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterdocretrieve;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.xml.ws.WebServiceContext;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxy;
import gov.hhs.fha.nhinc.docrepositoryadapter.proxy.AdapterDocumentRepositoryProxyObjectFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxyObjectFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocRetrieveSecuredImpl {

    private Log log = null;

    public AdapterDocRetrieveSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, WebServiceContext context) {
        log.debug("Enter AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        RetrieveDocumentSetResponseType response = null;
        AssertionType assertion = getAssertion(context);

        try {
            AdapterDocumentRepositoryProxy proxy = new AdapterDocumentRepositoryProxyObjectFactory().getAdapterDocumentRepositoryProxy();
            response = proxy.retrieveDocumentSet(body);
            response = callRedactionEngine(body, response, assertion);
        } catch (Throwable t) {
            log.error("Error processing an adapter document retrieve message: " + t.getMessage(), t);
            response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        }
        log.debug("Leaving AdapterDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve()");
        return response;
    }

    protected RetrieveDocumentSetResponseType callRedactionEngine(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse, AssertionType assertion) {
        RetrieveDocumentSetResponseType response = null;
        if (retrieveResponse == null) {
            log.warn("Did not call redaction engine because the retrieve response was null.");
        } else {
            response = getRedactionEngineProxy().filterRetrieveDocumentSetResults(retrieveRequest, retrieveResponse, assertion);
        }
        return response;
    }

    protected AdapterRedactionEngineProxy getRedactionEngineProxy() {
        return new AdapterRedactionEngineProxyObjectFactory().getRedactionEngineProxy();
    }

    private AssertionType getAssertion(WebServiceContext context) {
        AssertionType assertion = new AssertionType();
        AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
        assertion.setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        return assertion;
    }
}
