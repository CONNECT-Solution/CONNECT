/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV;

/**
 * This class is used to transform 201305 request message to a CheckPolicyRequestType
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelper {

    private static Log log = null;
    private static final String ActionValue = "PatientDiscoveryIn";
    
    /**
     * Default Constructor
     */
    public PatientDiscoveryPolicyTransformHelper() {
        log = createLogger();
    }

    /**
     * Transform method to create a CheckPolicyRequest object from a 201305 request
     * @param request
     * @return CheckPolicyRequestType
     */
    public CheckPolicyRequestType transformPatientDiscoveryEntityToCheckPolicy(PRPAIN201305UV request) {
        addDebugLog("Begin -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;
        if (request == null) {
            addErrorLog("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }
        
        RequestType req = new RequestType();
        req.setAction(ActionHelper.actionFactory(ActionValue));
        //SubjectType subject = SubjectHelper.subjectFactory(event.getSendingHomeCommunity(), event.getMessage().getAssertion());
        SubjectType subject = new SubjectType();
        req.getSubject().add(subject);
//        II ii = extractPatientIdentifier(subjectAdded);
//        if (ii != null) {
//            ResourceType resource = new ResourceType();
//            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientAssigningAuthorityAttributeId, Constants.DataTypeString, ii.getRoot()));
//            String sStrippedPatientId = PatientIdFormatUtil.parsePatientId(ii.getExtension());
//            log.debug("transformSubjectAddedInToCheckPolicy: sStrippedPatientId = " + sStrippedPatientId);
//            resource.getAttribute().add(AttributeHelper.attributeFactory(PatientIdAttributeId, Constants.DataTypeString, sStrippedPatientId));
//            req.getResource().add(resource);
//        }
        ResourceType resource = new ResourceType();
        req.getResource().add(resource);
        log.debug("transformSubjectAddedInToCheckPolicy - adding assertion data");
        //AssertionHelper.appendAssertionDataToRequest(req, event.getMessage().getAssertion());
        checkPolicyRequest.setAssertion(new AssertionType());
        checkPolicyRequest.setRequest(req);
        addDebugLog("End -- PatientDiscoveryPolicyTransformHelper.transformPatientDiscoveryEntityToCheckPolicy()");
        return checkPolicyRequest;
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
}
