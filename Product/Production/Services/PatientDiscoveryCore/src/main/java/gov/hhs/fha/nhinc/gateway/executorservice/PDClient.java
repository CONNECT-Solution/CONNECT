/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.gateway.executorservice;

import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import ihe.iti.xcpd._2009.RespondingGatewayPortType;
import ihe.iti.xcpd._2009.RespondingGatewayService;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01AttentionLine;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01RespondTo;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the Nhin PatientDiscovery web service client that calls this web service Defines the specific generics to
 * be used as follows Target is a gov.hhs.fha.nhinc.connectmgr.data.UrlInfo object Request is an
 * org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType object Response is an org.hl7.v3.PRPAIN201306UV02 object
 * ResponseWrapper contains the Response, Request and Target.
 *
 * @author paul.eftis
 *
 * @param <Target>
 * @param <Request>
 * @param <Response>
 */
public class PDClient<Target extends UrlInfo, Request extends RespondingGatewayPRPAIN201305UV02RequestType, Response extends ResponseWrapper>
implements WebServiceClient<Target, Request, Response> {

    private static final Logger LOG = LoggerFactory.getLogger(PDClient.class);
    private static RespondingGatewayService serviceInstance = null;
    private static final Object PDSYNC = new Object();
    private AssertionType assertion = null;

    /**
     * Public constructor for PDClient.
     *
     * @param assertionType is of type AssertionType
     */
    public PDClient(AssertionType assertionType) {
        assertion = assertionType;
    }

    // implement singleton pattern using double null check pattern
    private static RespondingGatewayService getWebServiceInstance() throws Exception {
        if (serviceInstance != null) {
            return serviceInstance;
        } else {
            synchronized (PDSYNC) {
                if (serviceInstance == null) {
                    LOG.debug("PDClient retrieving web service client from wsdl");
                    try {
                        serviceInstance = new RespondingGatewayService();
                    } catch (Exception e) {
                        ExecutorServiceHelper.outputCompleteException(e);
                        throw e;
                    }
                }
            }
            return serviceInstance;
        }
    }

    /**
     * Implements all connect logic to generate web service call.
     *
     * Updates RespondingGatewayPRPAIN201305UV02RequestType for the target Checks policy through
     * PatientDiscoveryPolicyChecker (if policy false returns PRPAIN201306UV02 with error set
     *
     * Note that web service client timeouts set to InitServlet.getTimeoutValues().get("DQConnectTimeout") and
     * InitServlet.getTimeoutValues().get("DQRequestTimeout"))
     *
     * @param target is UrlInfo target with url to call
     * @param request is RespondingGatewayPRPAIN201305UV02RequestType request to send in web service call
     * @return Response is PRPAIN201306UV02 returned
     * @throws Exception
     */
    @Override
    public Response callWebService(Target target, Request request) throws Exception {
        ResponseWrapper resp;
        PRPAIN201306UV02 discoveryResponse = null;
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = null;
        try {
            // create a new request to send out to each target community
            newRequest = createNewRequest(request, assertion, target);

            // check the policy for the outgoing request to the target community
            boolean bIsPolicyOk = checkPolicy(newRequest, assertion);
            if (bIsPolicyOk) {
                //Removed the old PatientDiscoveryAuditLogger.
                String serviceAddress = target.getUrl();

                RespondingGatewayPortType servicePort = getWebServiceInstance().getRespondingGatewayPortSoap();
                Map requestContext = ((BindingProvider) servicePort).getRequestContext();
                requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);
                Map samlMap
                = new SamlTokenCreator().createRequestContext(assertion, serviceAddress,
                    SamlConstants.PATIENT_DISCOVERY_ACTION);
                requestContext.putAll(samlMap);

                // ensure target hcid is set on request
                if (newRequest.getPRPAIN201305UV02() != null && newRequest.getPRPAIN201305UV02().getReceiver() != null
                    && newRequest.getPRPAIN201305UV02().getReceiver().get(0) != null
                    && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice() != null
                    && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId() != null
                    && newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0) != null) {

                    String hcid = HomeCommunityMap.formatHomeCommunityId(target.getHcid());
                    newRequest.getPRPAIN201305UV02().getReceiver().get(0).getDevice().getId().get(0).setRoot(hcid);
                    LOG.debug("{} set Receiver.Device.Id.Root of PRPAIN201305UV02 request to hcid={}"
                        ,Thread.currentThread().getName(), hcid);

                }

                LOG.debug("{} calling serviceAddress={} for target hcid={}"
                    ,Thread.currentThread().getName(),serviceAddress, target.getHcid());
                discoveryResponse = servicePort.respondingGatewayPRPAIN201305UV02(newRequest.getPRPAIN201305UV02());
            } else {
                LOG.debug("{} has validPolicy=false", Thread.currentThread().getName());
                discoveryResponse
                = new HL7PRPA201306Transforms().createPRPA201306ForErrors(newRequest.getPRPAIN201305UV02(),
                    NhincConstants.PATIENT_DISCOVERY_POLICY_FAILED_ACK_MSG);
            }
        } catch (Exception e) {
            ExecutorServiceHelper.outputCompleteException(e);
            throw e;
        } finally {
            resp = new ResponseWrapper(target, newRequest, discoveryResponse);
        }
        return (Response) resp;
    }

    /**
     * Policy Check verification done here....from connect code.
     *
     * @param request request
     * @param assertion assertion
     * @return true or false
     */
    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {
        if (request != null) {
            request.setAssertion(assertion);
        }
        return PatientDiscoveryPolicyChecker.getInstance().checkOutgoingPolicy(request);
    }

    /**
     * Create a new RespondingGatewayPRPAIN201305UV02RequestType which has a new PRPAIN201305UV02 cloned from the
     * original.
     *
     * @param request request
     * @param assertion assertionType
     * @param urlInfo urlInfo
     * @return new RespondingGatewayPRPAIN201305UV02RequestType
     */
    protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(
        RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, UrlInfo urlInfo) {
        RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();

        PRPAIN201305UV02 new201305
        = new PatientDiscovery201305Processor().createNewRequest(cloneRequest(request.getPRPAIN201305UV02()),
            urlInfo.getHcid());

        newRequest.setAssertion(assertion);
        newRequest.setPRPAIN201305UV02(new201305);
        newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        return newRequest;
    }

    /**
     * paul added this to generate a new PRPAIN201305UV02 for every PDClient thread rather than a single
     * PRPAIN201305UV02 for all requests.
     *
     * The reason is that otherwise you can get a java.util.ConcurrentModificationException when the PRPAIN201305UV02 is
     * marshalled for audit/policy etc calls in one thread and updated in another thread
     *
     * @param request is original PRPAIN201305UV02
     * @return new PRPAIN201305UV02 object with values set to original
     */
    private PRPAIN201305UV02 cloneRequest(PRPAIN201305UV02 request) {
        PRPAIN201305UV02 newRequest = new PRPAIN201305UV02();

        newRequest.setAcceptAckCode(request.getAcceptAckCode());

        for (EDExplicit edex : request.getAttachmentText()) {
            newRequest.getAttachmentText().add(edex);
        }
        for (MCCIMT000100UV01AttentionLine mcc : request.getAttentionLine()) {
            newRequest.getAttentionLine().add(mcc);
        }
        newRequest.setControlActProcess(request.getControlActProcess());
        newRequest.setCreationTime(request.getCreationTime());
        newRequest.setITSVersion(request.getITSVersion());
        newRequest.setId(request.getId());
        newRequest.setInteractionId(request.getInteractionId());
        for (String n : request.getNullFlavor()) {
            newRequest.getNullFlavor().add(n);
        }
        newRequest.setProcessingCode(request.getProcessingCode());
        newRequest.setProcessingModeCode(request.getProcessingModeCode());
        for (II ii : request.getProfileId()) {
            newRequest.getProfileId().add(ii);
        }
        for (CS cs : request.getRealmCode()) {
            newRequest.getRealmCode().add(cs);
        }
        for (MCCIMT000100UV01Receiver mcc : request.getReceiver()) {
            newRequest.getReceiver().add(mcc);
        }
        for (MCCIMT000100UV01RespondTo mcc : request.getRespondTo()) {
            newRequest.getRespondTo().add(mcc);
        }
        newRequest.setSecurityText(request.getSecurityText());
        newRequest.setSender(request.getSender());
        newRequest.setSequenceNumber(request.getSequenceNumber());
        for (II ii : request.getTemplateId()) {
            newRequest.getTemplateId().add(ii);
        }
        newRequest.setTypeId(request.getTypeId());
        newRequest.setVersionCode(request.getVersionCode());

        return newRequest;
    }
}
