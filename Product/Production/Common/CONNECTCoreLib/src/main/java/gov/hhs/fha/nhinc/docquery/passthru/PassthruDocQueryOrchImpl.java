/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.passthru;

import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxy;
import gov.hhs.fha.nhinc.docquery.nhin.proxy.NhinDocQueryProxyObjectFactory;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;

import java.sql.Timestamp;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 * @paul.eftis updated exception handling to return DQ error response
 * with error/exception detail within DQ response.
 */
public class PassthruDocQueryOrchImpl {

    private static Log log = LogFactory.getLog(PassthruDocQueryOrchImpl.class);

    private static final String XDS_RESPONSE_STATUS_FAILURE =
            "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

    /**
     *
     * @param body
     * @param assertion
     * @param target
     * @return <code>AdhocQueryResponse</code>
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, 
            AssertionType assertion, NhinTargetSystemType target){
        log.debug("Entering NhincProxyDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        AdhocQueryResponse response = null;

        // The responding home community id is required in the audit log
        String responseCommunityID = null;
        if (target != null &&
                target.getHomeCommunity() != null) {
            responseCommunityID = target.getHomeCommunity().getHomeCommunityId();
        }
        log.debug("=====>>>>> responseCommunityID is " + responseCommunityID);
        // Audit the Document Query Request Message sent on the Nhin Interface
        DocQueryAuditLog auditLog = new DocQueryAuditLog();
        AcknowledgementType ack = auditLog.auditDQRequest(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        try {
            log.debug("Creating NhinDocQueryProxy");
            NhinDocQueryProxyObjectFactory docQueryFactory = new NhinDocQueryProxyObjectFactory();
            NhinDocQueryProxy proxy = docQueryFactory.getNhinDocQueryProxy();

            log.debug("Calling NhinDocQueryProxy.respondingGatewayCrossGatewayQuery(request)");

            // Log the start of the nhin performance record
            Timestamp starttime = new Timestamp(System.currentTimeMillis());
            Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, responseCommunityID);

            response = proxy.respondingGatewayCrossGatewayQuery(body, assertion, target);

            // Log the end of the nhin performance record
            Timestamp stoptime = new Timestamp(System.currentTimeMillis());
            PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
        }catch(Exception ex){
            log.error("PassthruDocQueryOrchImpl Exception", ex);
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, target,
                    NhincConstants.DOC_QUERY_SERVICE_NAME);
            response = generateErrorResponse(target, err);
        }

        // Audit the Document Query Response Message received on the Nhin Interface
        AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
        auditMsg.setAdhocQueryResponse(response);
        auditMsg.setAssertion(assertion);
        ack = auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, responseCommunityID);

        log.debug("Leaving NhincProxyDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }


    private AdhocQueryResponse generateErrorResponse(NhinTargetSystemType target, String error){
        AdhocQueryResponse adhocresponse = new AdhocQueryResponse();
        RegistryErrorList regErrList = new RegistryErrorList();
        adhocresponse.setStatus(XDS_RESPONSE_STATUS_FAILURE);
        RegistryError regErr = new RegistryError();
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setCodeContext("Error from target homeId=" + target.getHomeCommunity().getHomeCommunityId());
        regErr.setValue(error);
        regErr.setSeverity("Error");
        regErrList.getRegistryError().add(regErr);
        adhocresponse.setRegistryErrorList(regErrList);
        return adhocresponse;
    }
}
