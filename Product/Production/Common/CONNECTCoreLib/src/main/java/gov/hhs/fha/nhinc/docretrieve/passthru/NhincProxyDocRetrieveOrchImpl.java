/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveAuditLog;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.nhin.proxy.NhinDocRetrieveProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveOrchImpl {

    private Log log = null;

    public NhincProxyDocRetrieveOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType targetSystem) {
        log.debug("Begin NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        RetrieveDocumentSetResponseType response = null;
        String responseCommunityId = HomeCommunityMap.getCommunitIdForRDRequest(request);
        // Audit request message
        DocRetrieveAuditLog auditLog = new DocRetrieveAuditLog();
        auditLog.auditDocRetrieveRequest(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        try {
            log.debug("Creating NHIN doc retrieve proxy");
            NhinDocRetrieveProxyObjectFactory objFactory = new NhinDocRetrieveProxyObjectFactory();
            NhinDocRetrieveProxy docRetrieveProxy = objFactory.getNhinDocRetrieveProxy();

            log.debug("Calling doc retrieve proxy");
            response = docRetrieveProxy.respondingGatewayCrossGatewayRetrieve(request, assertion, targetSystem);
        } catch (Throwable t) {
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
        auditLog.auditResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityId);

        log.debug("End NhincProxyDocRetrieveOrchImpl.respondingGatewayCrossGatewayRetrieve(...)");
        return response;
    }
}
