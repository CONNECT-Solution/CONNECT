package gov.hhs.fha.nhinc.patientdiscovery.entity.test;

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
public class EntityPatientDiscoveryOrchImplTest{

    private Log log = LogFactory.getLog(getClass());
    private ExecutorService regularExecutor = null;
    private ExecutorService largejobExecutor = null;

    // quick rig for test/demo
    private boolean isTest = false;
    private int requestCount = 0;
    private String testServiceUrl = null;
    public void setTest(int requestcount, String url){
        requestCount = requestcount;
        testServiceUrl = url;
        isTest = true;
    }


    /**
     * We construct the orch impl class with references to both executor services
     * that could be used for this particular orchestration instance.
     * Determination of which executor service to use (largejob or regular) is based on
     * the size of the pdlist and configs
     */
    public EntityPatientDiscoveryOrchImplTest(ExecutorService e, ExecutorService le){
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
                 * We replace the current code here with the concurrent fanout impl
                 * Note that the checkPolicy is done in the PDClient
                 * and all response processing is done in the PDProcessor
                 *
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = createNewRequest(request, assertion, urlInfo);

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest, assertion);

                    if (bIsPolicyOk) {
                        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

                        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
                        oTargetSystemType.setUrl(urlInfo.getUrl());

                        //format request for nhincProxyPatientDiscoveryImpl call
                        ProxyPRPAIN201305UVProxySecuredRequestType oProxyPRPAIN201305UVProxySecuredRequestType =
                                new ProxyPRPAIN201305UVProxySecuredRequestType();
                        oProxyPRPAIN201305UVProxySecuredRequestType.setPRPAIN201305UV02(newRequest.getPRPAIN201305UV02());
                        oProxyPRPAIN201305UVProxySecuredRequestType.setNhinTargetSystem(oTargetSystemType);
                        ResponseParams params = new ResponseParams();

                        try {
                            resultFromNhin = proxy.PRPAIN201305UV(oProxyPRPAIN201305UVProxySecuredRequestType.getPRPAIN201305UV02(), assertion, oProxyPRPAIN201305UVProxySecuredRequestType.getNhinTargetSystem());

                            params.assertion = assertion;
                            params.origRequest = oProxyPRPAIN201305UVProxySecuredRequestType;
                            params.response = resultFromNhin;
                            resultFromNhin = new ResponseFactory().getResponseMode().processResponse(params);

                        } catch (Exception ex) {
                            log.error(ex.getMessage(), ex);
                            resultFromNhin = new HL7PRPA201306Transforms().createPRPA201306ForErrors(oProxyPRPAIN201305UVProxySecuredRequestType.getPRPAIN201305UV02(), NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
                        }

                        // Store AA to HCID Mapping from response
                        getPatientDiscovery201306Processor().storeMapping(resultFromNhin);

                        communityResponse.setPRPAIN201306UV02(resultFromNhin);

                        log.debug("Adding Community Response to response object");
                        response.getCommunityResponse().add(communityResponse);
                    }else{
                        //else policy engine did not return a permit response
                        // TODO handle this case by sending an error response (currently will return null)
                        log.error("The policy engine evaluated the request and denied the request.");
                    }
                }
                ***********************************************************************/

                String transactionId = (UUID.randomUUID()).toString();
                PDProcessor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02, RespondingGatewayPRPAIN201306UV02ResponseType> pdprocessor =
                        new PDProcessor<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02, RespondingGatewayPRPAIN201306UV02ResponseType>(assertion);

                PDClient<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> pdclient =
                        new PDClient<CMUrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(assertion);

                
                List<CMUrlInfo> pdlist = new ArrayList<CMUrlInfo>();
                // for test we generate the pdlist rather than using urlInfoList which
                // is list from internalConnectionInfo.xml config
                if(isTest){
                    log.debug("EntityPatientDiscoveryOrchImpl running test");
                    for(int i = 0; i < requestCount; i++){
                        CMUrlInfo urlInfo = new CMUrlInfo();
                        urlInfo.setUrl(testServiceUrl);
                        urlInfo.setHcid(PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                            NhincConstants.HOME_COMMUNITY_ID_PROPERTY));
                        pdlist.add(urlInfo);
                    }
                }else{
                    pdlist = urlInfoList.getUrlInfo();
                }

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
