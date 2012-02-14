package gov.hhs.fha.nhinc.patientdiscovery.entity.test;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImpl;
//import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryOrchImplRuntimeTest;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Orchestrates the Entity (i.e. from Adapter) PatientDiscovery transaction
 * 
 * @author Neil Webb
 * @author paul.eftis (updated 10/15/2011 to implement new concurrent request handling/fanout)
 */
public class EntityPatientDiscoveryOrchImplTest {

    private Log log = LogFactory.getLog(getClass());
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;

    // quick rig for test/demo
    private boolean isTest = false;
    private int requestCount = 0;
    private String testServiceUrl = null;

    public void setTest(int requestcount, String url) {
        requestCount = requestcount;
        testServiceUrl = url;
        isTest = true;
    }

    /**
     * We construct the orch impl class with references to both executor services that could be used for this particular
     * orchestration instance. Determination of which executor service to use (largejob or regular) is based on the size
     * of the pdlist and configs
     */
    public EntityPatientDiscoveryOrchImplTest(ExecutorService e, ExecutorService le) {
        regularExecutor = e;
        largejobExecutor = le;
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {

        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try {
            if (request == null) {
                log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                throw new Exception("PatientDiscovery RespondingGatewayPRPAIN201305UV02RequestType request was null.");
            } else if (assertion == null) {
                log.warn("AssertionType was null.");
                throw new Exception("Assertion was null.");
            } else if (request.getPRPAIN201305UV02() == null) {
                log.warn("PRPAIN201305UV02 was null.");
                throw new Exception("PatientDiscovery PRPAIN201305UV02 request was null.");
            } else {
                logEntityPatientDiscoveryRequest(request, assertion);
                response = getResponseFromCommunities(request, assertion);
                logAggregatedResponseFromNhin(response, assertion);
            }
        } catch (Exception e) {
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                    request.getPRPAIN201305UV02(), e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }
        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

    @SuppressWarnings("static-access")
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("Entering EntityPatientDiscoveryOrchImplTest getResponseFromCommunities");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try {
            String transactionId = (UUID.randomUUID()).toString();

            List<UrlInfo> pdlist = new ArrayList<UrlInfo>();
            // for test we generate the pdlist rather than using urlInfoList which
            // is list from internalConnectionInfo.xml config
            if (isTest) {
                log.debug("EntityPatientDiscoveryOrchImplTest running test");
                for (int i = 0; i < requestCount; i++) {
                    UrlInfo urlInfo = new UrlInfo();
                    urlInfo.setUrl(testServiceUrl);
                    urlInfo.setHcid(PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                            NhincConstants.HOME_COMMUNITY_ID_PROPERTY));
                    pdlist.add(urlInfo);
                }
                // EntityPatientDiscoveryOrchImplRuntimeTest was removed from core lib
                // EntityPatientDiscoveryOrchImplRuntimeTest orchestrator =
                // new EntityPatientDiscoveryOrchImplRuntimeTest(
                // regularExecutor, largejobExecutor);
                // response = orchestrator.entityPatientDiscoveryOrchImplFanoutTest(
                // request, assertion, pdlist);
            } else {
                // just do normal test
                EntityPatientDiscoveryOrchImpl orchestrator = new EntityPatientDiscoveryOrchImpl(regularExecutor,
                        largejobExecutor);
                response = orchestrator.respondingGatewayPRPAIN201305UV02(request, assertion);
            }
            log.debug("EntityPatientDiscoveryOrchImplTest received response");
        } catch (Exception e) {
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(
                    request.getPRPAIN201305UV02(), e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }

        log.debug("EntityPatientDiscoveryOrchImplTest Exiting getResponseFromCommunities");
        return response;
    }

    protected List<UrlInfo> getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        List<UrlInfo> urlInfoList = null;
        try {
            urlInfoList = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTargetCommunities(
                    targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }

    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request,
            AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201305(request, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response,
            AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201306(response, assertion,
                NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

}
