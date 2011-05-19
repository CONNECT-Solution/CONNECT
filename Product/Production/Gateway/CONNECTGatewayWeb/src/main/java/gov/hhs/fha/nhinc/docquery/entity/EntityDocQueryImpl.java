/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class EntityDocQueryImpl {

    private Log log = null;

    public EntityDocQueryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected WebServiceHelper createWebServiceHelper() {
        return new WebServiceHelper();
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;

        try {
            if (request != null) {
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();

                // Log the start of the performance record
                String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
                Timestamp starttime = new Timestamp(System.currentTimeMillis());
                Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

                response = (AdhocQueryResponse) oHelper.invokeSecureWebService(implOrch, implOrch.getClass(), "respondingGatewayCrossGatewayQuery", adhocQueryRequest, targets, context);

                // Log the end of the performance record
                Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    AdhocQueryResponse respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQueryRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        WebServiceHelper oHelper = createWebServiceHelper();
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;

        try {
            if (request != null) {
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                response = (AdhocQueryResponse) oHelper.invokeUnsecureWebService(implOrch, implOrch.getClass(), "respondingGatewayCrossGatewayQuery", adhocQueryRequest, assertIn, targets, context);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + implOrch.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    private EntityDocQueryOrchImpl createEntityDocQueryOrchImpl() {
        return new EntityDocQueryOrchImpl();
    }
}
