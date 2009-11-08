/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PRPAIN201305UV;
import org.hl7.v3.PRPAMT201306UVQueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAMT201306UVLivingSubjectId;

/**
 * This class is used to transform 201305 request message to a CheckPolicyRequestType
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelper {

    private static Log log = null;
    private static final String ActionInValue = "PatientDiscoveryIn";
    private static final String ActionOutValue = "PatientDiscoveryOut";
    private static final String PatientAssigningAuthorityAttributeId = Constants.AssigningAuthorityAttributeId;
    private static final String PatientIdAttributeId = Constants.ResourceIdAttributeId;

    /**
     * Default Constructor
     */
    public PatientDiscoveryPolicyTransformHelper() {
        log = createLogger();
    }

    public CheckPolicyRequestType transformPatientDiscoveryNhincToCheckPolicy(RespondingGatewayPRPAIN201305UV02RequestType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;
        if (event == null) {
            addErrorLog("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }
        RequestType request = getRequestType(event);
        request.setAction(ActionHelper.actionFactory(ActionOutValue));
        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getAssertion());
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        return checkPolicyRequest;
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201305 request
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryEntityToCheckPolicy(RespondingGatewayPRPAIN201305UV02RequestType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;
        if (event == null) {
            addErrorLog("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }
        RequestType request = getRequestType(event);
        request.setAction(ActionHelper.actionFactory(ActionInValue));
        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getAssertion());
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        return checkPolicyRequest;
    }

    protected HomeCommunityType getHomeCommunityFrom201305(RespondingGatewayPRPAIN201305UV02RequestType event) {
        HomeCommunityType senderHomeCommunity = new HomeCommunityType();
        final PRPAIN201305UV pRPAIN201305UV = event.getPRPAIN201305UV();
        final MCCIMT000100UV01Sender sender = pRPAIN201305UV.getSender();
        final MCCIMT000100UV01Device device = sender.getDevice();
        final List<II> id = device.getId();
        final II get = id.get(0);
        final String root = get.getRoot();
        senderHomeCommunity.setHomeCommunityId(root);
        return senderHomeCommunity;
    }

    protected RequestType getRequestType(RespondingGatewayPRPAIN201305UV02RequestType event) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        RequestType request = new RequestType();
        setSubjectToRequestType(event, request);
        ResourceType resource = null;
        II ii = extractPatientIdentifier(event.getPRPAIN201305UV());
        if (ii != null) {
            resource = new ResourceType();
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
            log.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
            request.getResource().add(resource);
        }
        request.getResource().add(resource);
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.getRequestType()");
        return request;
    }

    protected void setSubjectToRequestType(RespondingGatewayPRPAIN201305UV02RequestType event, RequestType request) {
        HomeCommunityType senderHomeCommunity = getHomeCommunityFrom201305(event);
        SubjectType subject = SubjectHelper.subjectFactory(senderHomeCommunity, event.getAssertion());
        request.getSubject().add(subject);
    }

    /**
     * log4j info messages are logged here
     * @param infoMessage
     */
    private void addInfoLog(String infoMessage) {
        log.info(infoMessage);
    }

    /**
     * log4j error messages are logged here
     * @param errorMessage
     */
    private void addErrorLog(String errorMessage) {
        log.error(errorMessage);
    }

    /**
     * log4j debug messages are logged here
     * @param debugMessage
     */
    private void addDebugLog(String debugMessage) {
        log.debug(debugMessage);
    }

    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    private static II extractPatientIdentifier(PRPAIN201305UV message) {
        II ii = null;

        if ((message != null) && (message.getControlActProcess() != null) && (message.getControlActProcess().getQueryByParameter() != null)) {
            JAXBElement<PRPAMT201306UVQueryByParameter> queryParam = message.getControlActProcess().getQueryByParameter();
            PRPAMT201306UVQueryByParameter queryParam201306 = queryParam.getValue();
            if (queryParam201306.getParameterList() != null && queryParam201306.getParameterList().getLivingSubjectId() != null) {
                List<PRPAMT201306UVLivingSubjectId> livingSubjectIdList = queryParam201306.getParameterList().getLivingSubjectId();
                if (livingSubjectIdList != null) {
                    List<II> patinetIdList = livingSubjectIdList.get(0).getValue();
                    if (patinetIdList != null) {
                        ii = patinetIdList.get(0);
                    }
                }
            }
        }
        return ii;
    }
}
