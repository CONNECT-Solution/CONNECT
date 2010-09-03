/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.policy;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistributionTransformHelper {
    private static final String ActionInValue = "AdminDistIn";
    private static final String ActionOutValue = "AdminDistOut";
    private static Log log = null;
    
    public AdminDistributionTransformHelper() {
        log = createLogger();
    }
    /**
     * Instantiating log4j logger
     * @return
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    public CheckPolicyRequestType transformNhinAlertToCheckPolicy(EDXLDistribution message, AssertionType assertion)
    {
        CheckPolicyRequestType result = new CheckPolicyRequestType();

         RequestType request = new RequestType();

        if(assertion == null)
        {
            log.error("Missing Assertion");
            return result;
        }
        if(message == null)
        {
            log.error("Missing message");
            return result;
        }
        log.debug("transformAdminDistributionNhincToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, assertion);
        
        request.setAction(ActionHelper.actionFactory(ActionInValue));

        result.setAssertion(assertion);
        result.setRequest(request);
        
        return result;
    }
    public CheckPolicyRequestType transformEntityAlertToCheckPolicy(RespondingGatewaySendAlertMessageType message, String target) {

        CheckPolicyRequestType result = new CheckPolicyRequestType();
        if (message == null) {
            log.error("Request is null.");
            return result;
        }
        if (target == null || target.isEmpty())
        {
            log.error("target is missing");
            return result;
        }

        if (message.getEDXLDistribution() == null)
        {
            log.error("missing body");
            return result;
        }
        if(message.getAssertion() == null)
        {
            log.error("missing assertion");
            return result;
        }
        if(message.getAssertion().getHomeCommunity() == null)
        {
            log.error("missing home community");
            return result;
        }
        
        EDXLDistribution body = message.getEDXLDistribution();
        //RequestType request = getRequestType(patDiscReq, event.getAssertion());
        RequestType request = new RequestType();
        AttributeHelper attrHelper = new AttributeHelper();
        
        log.debug("transformEntityAlertToCheckPolicy - adding subject");
                SubjectHelper subjHelp = new SubjectHelper();
        //SubjectType subject = subjHelp.subjectFactory(event.getAssertion().getHomeCommunity(), event.getAssertion());
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectHelper.SubjectCategory);
        log.debug("transformEntityAlertToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        ResourceType resource = new ResourceType();
        resource.getAttribute().add(attrHelper.attributeFactory(Constants.HomeCommunityAttributeId, Constants.DataTypeString,message.getAssertion().getHomeCommunity().getHomeCommunityId()));
        
        request.getResource().add(resource);

        log.debug("transformEntityAlertToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, message.getAssertion());
        
        


        
        request.setAction(ActionHelper.actionFactory(ActionOutValue));
        result.setAssertion(message.getAssertion());
        result.setRequest(request);
        return result;

    }
    protected SubjectType createSubject(HomeCommunityType hc, AssertionType assertion)
    {
        SubjectHelper subjHelp = new SubjectHelper();
        //SubjectType subject = subjHelp.subjectFactory(event.getAssertion().getHomeCommunity(), event.getAssertion());
        SubjectType subject = subjHelp.subjectFactory(hc, assertion);

        subject.setSubjectCategory(SubjectHelper.SubjectCategory);

        return subject;
    }
}
