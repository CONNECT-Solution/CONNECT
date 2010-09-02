/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 * Entity subscribe message handler.
 *
 * @author Neil Webb
 */
public interface EntitySubscribeHandler
{
    /**
     * Set an optional patient identifier from the subscription
     *
     * @param patientIdentifier Optional patient identifier
     */
    public void setPatientIdentifier(QualifiedSubjectIdentifierType patientIdentifier);

    /**
     * Set an optional XPath location to the patient identifier
     *
     * @param xpathToPatientId Optional XPath location to patient identifier
     */
    public void setPatientIdentiferLocation(String xpathToPatientId);

    /**
     * Handle an entity subscribe message.
     * 
     * @param topicConfig Topic configuration for the subscribe message
     * @param subscribe Subscribe message received on the entity interface
     * @param subscribeElement XML element for the subscription as received in the SOAP message
     * @param assertion Assertion received from the adapter
     * @param targetCommunitites Optional target communities
     * @return Subscribe response
     * @throws org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault
     * @throws org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault
     * @throws org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault
     */
    public SubscribeResponse handleSubscribe(TopicConfigurationEntry topicConfig, Subscribe subscribe, Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault;
}
