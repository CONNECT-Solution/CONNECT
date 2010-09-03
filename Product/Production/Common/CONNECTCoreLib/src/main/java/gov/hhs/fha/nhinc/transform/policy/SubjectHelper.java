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

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

/**
 *
 * @author rayj
 */
public class SubjectHelper {

    public static final String SubjectCategory = "urn:oasis:names:tc:xacml:1.0:subject-category:access-subject";
//    private static final String UserAttributeId = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
    public static final String UserRoleAttributeId = "urn:gov:hhs:fha:nhinc:user-role-code";
    public static final String PurposeAttributeId = "urn:gov:hhs:fha:nhinc:purpose-for-use";
    public static final String UserHomeCommunityAttributeId = "urn:gov:hhs:fha:nhinc:home-community-id";

    public SubjectType subjectFactory(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectCategory);
        //subject.getAttribute().add(AttributeHelper.attributeFactory(UserAttributeId, Constants.DataTypeString, AssertionHelper.extractUserName(assertion)));
        AttributeHelper attrHelper = new AttributeHelper();
        subject.getAttribute().add(attrHelper.attributeFactory(UserHomeCommunityAttributeId, Constants.DataTypeString, determineSendingHomeCommunityId(sendingHomeCommunity, assertion)));
        return subject;
    }

    public SubjectType subjectFactoryReident(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        SubjectType subject = new SubjectType();
        subject.setSubjectCategory(SubjectCategory);
        // removed as this causes the user-role-code to show up twice
        //subject.getAttribute().add(AttributeHelper.attributeFactory(UserRoleAttributeId, Constants.DataTypeString, AssertionHelper.extractUserRole(assertion)));
        //subject.getAttribute().add(AttributeHelper.attributeFactory(PurposeAttributeId, Constants.DataTypeString, AssertionHelper.extractPurpose(assertion)));
        AttributeHelper attrHelper = new AttributeHelper();
        subject.getAttribute().add(attrHelper.attributeFactory(UserHomeCommunityAttributeId, Constants.DataTypeString, determineSendingHomeCommunityId(sendingHomeCommunity, assertion)));
        return subject;
    }

    public String determineSendingHomeCommunityId(HomeCommunityType sendingHomeCommunity, AssertionType assertion) {
        String homeCommunityId = null;

        if (sendingHomeCommunity != null) {
            homeCommunityId = sendingHomeCommunity.getHomeCommunityId();
        }

        if (NullChecker.isNullish(homeCommunityId)) {
            AssertionHelper assertHelp = new AssertionHelper();
            homeCommunityId = assertHelp.extractUserHomeCommunity(assertion);
        }

        return homeCommunityId;
    }
}
