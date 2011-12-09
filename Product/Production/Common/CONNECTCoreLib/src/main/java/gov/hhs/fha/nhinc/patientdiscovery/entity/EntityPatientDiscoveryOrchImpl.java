package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.gateway.executorservice.PDProcessor;
import gov.hhs.fha.nhinc.gateway.executorservice.PDClient;
import gov.hhs.fha.nhinc.gateway.executorservice.ResponseWrapper;
import gov.hhs.fha.nhinc.gateway.executorservice.TaskExecutor;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;


import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Orchestrates the Entity (i.e. from Adapter) PatientDiscovery transaction
 * @author Neil Webb
 * @author paul.eftis (updated 10/15/2011 to implement new concurrent request handling/fanout)
 */
public class EntityPatientDiscoveryOrchImpl{

    private Log log = LogFactory.getLog(getClass());
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;


    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the pdlist and configs
     */
    public EntityPatientDiscoveryOrchImpl(ExecutorService e, ExecutorService le){
        regularExecutor = e;
        largejobExecutor = le;
    }


    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion){

        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try{
            if(request == null){
                log.warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                throw new Exception("PatientDiscovery RespondingGatewayPRPAIN201305UV02RequestType request was null.");
            }else if(assertion == null){
                log.warn("AssertionType was null.");
                throw new Exception("Assertion was null.");
            }else if (request.getPRPAIN201305UV02() == null){
                log.warn("PRPAIN201305UV02 was null.");
                throw new Exception("PatientDiscovery PRPAIN201305UV02 request was null.");
            }else{
                logEntityPatientDiscoveryRequest(request, assertion);
                response = getResponseFromCommunities(request, assertion);
                logAggregatedResponseFromNhin(response, assertion);
            }
        }catch(Exception e){
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getPRPAIN201305UV02(),
                        e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }
        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }


    @SuppressWarnings("static-access")
    protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        log.debug("Entering getResponseFromCommunities");
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        try{
            CMUrlInfos urlInfoList = getEndpoints(request.getNhinTargetCommunities());
            // loop through the communities and send request if results were not null
            if((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())){
                log.warn("No targets were found for the Patient Discovery Request");
                throw new Exception("No Endpoints For Communities Found!!!");
            }else{
                /************************************************************************
                 * We replaced the 3.2.1 connect code here with the new 3.3 concurrent fanout impl
                 * Note that the checkPolicy is done in the PDClient
                 * and all response processing is done in the PDProcessor
                ***********************************************************************/

                String transactionId = (UUID.randomUUID()).toString();
                PDProcessor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02, RespondingGatewayPRPAIN201306UV02ResponseType> pdprocessor =
                        new PDProcessor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02, RespondingGatewayPRPAIN201306UV02ResponseType>(assertion);

                PDClient<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> pdclient =
                        new PDClient<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(assertion);
                
                List<CMUrlInfo> pdlist = urlInfoList.getUrlInfo();

                TaskExecutor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, RespondingGatewayPRPAIN201306UV02ResponseType> pdexecutor =
                        new TaskExecutor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, RespondingGatewayPRPAIN201306UV02ResponseType>(
                            ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(pdlist.size()) ? largejobExecutor : regularExecutor ,
                            pdprocessor, pdclient, pdlist, request, transactionId);
                pdexecutor.executeTask();
                response = pdexecutor.getFinalResponse();
                log.debug("EntityPatientDiscoveryOrchImpl taskexecutor done and received response");
            }
        }catch(Exception e){
            // generate error message and add to response
            CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();
            communityResponse.setPRPAIN201306UV02((new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request.getPRPAIN201305UV02(),
                        e.getMessage()));
            response.getCommunityResponse().add(communityResponse);
        }

        log.debug("EntityPatientDiscoveryOrchImpl Exiting getResponseFromCommunities");
        return response;
    }


    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities){
        CMUrlInfos urlInfoList = null;
        try{
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        }catch (ConnectionManagerException ex){
            log.error("Failed to obtain target URLs", ex);
        }
        return urlInfoList;
    }


    protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
        new PatientDiscoveryAuditLogger().auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

}
