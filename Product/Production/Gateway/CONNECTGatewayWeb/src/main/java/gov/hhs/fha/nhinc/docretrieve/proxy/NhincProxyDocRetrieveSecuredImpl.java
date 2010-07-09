package gov.hhs.fha.nhinc.docretrieve.proxy;

import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveAuditLog;
import gov.hhs.fha.nhinc.nhindocretrieve.proxy.NhinDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.nhindocretrieve.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
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
        log.debug("Collecting assertion");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        return respondingGatewayCrossGatewayRetrieve(body, assertion);
    }

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion)
    {
        log.debug("Begin NhincProxyDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;

        // Audit request message
        DocRetrieveAuditLog auditLog = new DocRetrieveAuditLog();
        auditLog.auditDocRetrieveRequest(body.getRetrieveDocumentSetRequest(), assertion);

        try
        {
            log.debug("Creating NHIN doc retrieve proxy");
            NhinDocRetrieveProxyObjectFactory objFactory = new NhinDocRetrieveProxyObjectFactory();
            NhinDocRetrieveProxy docRetrieveProxy = objFactory.getNhinDocRetrieveProxy();

            RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
            request.setAssertion(assertion);
            request.setNhinTargetSystem(body.getNhinTargetSystem());
            request.setRetrieveDocumentSetRequest(body.getRetrieveDocumentSetRequest());

            log.debug("Calling doc retrieve proxy");
            response = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request);
        }
        catch(Throwable t)
        {
            log.error("Error occured sending doc query to NHIN target: " + t.getMessage(), t);
            response = new RetrieveDocumentSetResponseType();
            RegistryResponseType responseType = new RegistryResponseType();
            response.setRegistryResponse(responseType);
            responseType.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            RegistryErrorList regErrList = new RegistryErrorList();
            responseType.setRegistryErrorList(regErrList);
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Processing NHIN Proxy document retrieve");
            regErr.setErrorCode("XDSRepositoryError");
            regErr.setSeverity("Error");
        }

        // Audit response message
        auditLog.auditResponse(response, assertion);

        log.debug("End NhincProxyDocRetrieveSecuredImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }
    
}
