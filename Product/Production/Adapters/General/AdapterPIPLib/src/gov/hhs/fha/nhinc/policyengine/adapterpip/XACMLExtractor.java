package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyMetadataType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UserIdFormatType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.tc.xacml._2_0.policy.schema.os.EffectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.policy.schema.os.RuleType;

import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;


/**
 * This class contains extractor code for extracting data from the XACML.
 *
 * @author Les Westberg
 */
public class XACMLExtractor
{
    protected Log log = null;
    private static final String HL7_DATE_ONLY_FORMAT = "yyyyMMdd";

    private static final SimpleDateFormat oHL7DateOnlyFormatter = new SimpleDateFormat(HL7_DATE_ONLY_FORMAT);
    private static final String HL7_DATE_TIME_FORMAT = "yyyyMMddHHmmssZ";
    private static final SimpleDateFormat oHL7DateTimeFormatter = new SimpleDateFormat(HL7_DATE_TIME_FORMAT);
    private static final String XML_DATE_ONLY_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat oXMLDateOnlyFormatter = new SimpleDateFormat(XML_DATE_ONLY_FORMAT);
    private static final String XML_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat oXMLDateTimeFormatter = new SimpleDateFormat(XML_DATE_TIME_FORMAT);


    /**
     * Default constructor.
     */
    public XACMLExtractor()
    {
        log = createLogger();
    }

    /**
     * Sets up the logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));

    }

    /**
     * This method extracts the policy OID from the consent XACML object.
     *
     * @param oConsentXACML Th XACML consent information
     * @return The Policy OID that was in the XACML.
     */
    private String extractXACMLPolicyOID(PolicyType oConsentXACML)
    {
        String sPolicyOid = "";

        if ((oConsentXACML != null) &&
            (oConsentXACML.getPolicyId() != null) &&
            (oConsentXACML.getPolicyId().length() > 0))
        {
            sPolicyOid = oConsentXACML.getPolicyId();
        }

        return sPolicyOid;
    }

    /**
     * This method looks at the XACML and determines if it represents just a
     * simple opt-in/opt-out value.  This can be determined if it has only one
     * rule and if tha rule contains no fine-grained data - but simply has an
     * effect of Permit or Deny.
     *
     * @param oConsentXACML The XACML form of the patient consent.
     * @return TRUE if this consent represents just a simple opt-in or opt-out value.
     */
    private boolean containsSimpleOptInSetting(PolicyType oConsentXACML)
    {
        boolean bReturnVal = false;

        if ((oConsentXACML != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition() != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size() == 1) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().get(0) instanceof RuleType))
        {
            RuleType oRule = (RuleType) oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().get(0);

            // Make sure that the target is null or if there is a target, that it has no children.
            //-------------------------------------------------------------------------------------
            if ((oRule.getTarget() == null) ||
                ((oRule.getTarget() != null) &&
                 (oRule.getTarget().getActions() == null) &&
                 (oRule.getTarget().getEnvironments() == null) &&
                 (oRule.getTarget().getResources() == null) &&
                 (oRule.getTarget().getSubjects() == null)))
            {
                bReturnVal = true;
            }
        }


        return bReturnVal;
    }

    /**
     * This method extracts a simple opt-in/opt-out setting.  A simple opt-in or
     * opt-out setting occurs if there is exactly one fine-grained rule and if that
     * rule has only a Permit or Deny without any other criteria.  If this is the
     * case and the rule is a "Permit" then true is returned.  Otherwise
     * false is returned.
     *
     * @param oConsentXACML The XACML form of the patient consent.
     * @return True if there is one simple rule that specifies permit for all conditions,
     *         false in all other cases.
     */
    private boolean extractSimpleOptInSetting(PolicyType oConsentXACML)
    {
        boolean bOptIn = false;

        if ((oConsentXACML != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition() != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size() == 1) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().get(0) instanceof RuleType))
        {
            RuleType oRule = (RuleType) oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().get(0);

            // Make sure that the target is null or if there is a target, that it has no children.
            //-------------------------------------------------------------------------------------
            if ((oRule.getTarget() == null) ||
                ((oRule.getTarget() != null) &&
                 (oRule.getTarget().getActions() == null) &&
                 (oRule.getTarget().getEnvironments() == null) &&
                 (oRule.getTarget().getResources() == null) &&
                 (oRule.getTarget().getSubjects() == null)))
            {
                if (oRule.getEffect().equals(EffectType.PERMIT))
                {
                    bOptIn = true;
                }
            }
        }

        return bOptIn;
    }

    /**
     * This method looks for the specified attribute within the Resource section
     * and returns its value in the form of a string.
     *
     * @param oRule The rule that is being looked at.
     * @param sAttributeId The ID of the desired attribute
     * @return The value associated with that attribute
     */
    private String extractValueFromResource(RuleType oRule, String sAttributeId)
    {
        // Extract any values from the Resource section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getResources() != null) &&
            (oRule.getTarget().getResources().getResource() != null) &&
            (oRule.getTarget().getResources().getResource().size() > 0))
        {
            List<ResourceType> olResource = oRule.getTarget().getResources().getResource();
            for (ResourceType oResource : olResource)
            {
                if ((oResource != null) &&
                    (oResource.getResourceMatch() != null) &&
                    (oResource.getResourceMatch().size() > 0))
                {
                    List<ResourceMatchType> olResMatch = oResource.getResourceMatch();
                    for (ResourceMatchType oResourceMatch : olResMatch)
                    {
                        if ((oResourceMatch != null) &&
                            (oResourceMatch.getResourceAttributeDesignator() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId().equals(sAttributeId)) &&
                            (oResourceMatch.getAttributeValue() != null) &&
                            (oResourceMatch.getAttributeValue().getContent() != null) &&
                            (oResourceMatch.getAttributeValue().getContent().size() > 0) &&
                            (oResourceMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oResourceMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                return sValue;
                            }
                        }   // if ((oResourceMatch != null) &&
                    }   // for (ResourceMatchType oResourceMatch : olResMatch)
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : olResource)
        }   // if ((oRule != null) &&

        return null;
    }


    /**
     * This method looks for the User ID subject and returns the user ID format.
     *
     * @param oRule The rule that is being looked at.
     * @return The type of user ID in the record.
     */
    private UserIdFormatType extractUserIdFormatFromSubject(RuleType oRule)
    {
        // Extract any values from the Resource section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getSubjects() != null) &&
            (oRule.getTarget().getSubjects().getSubject() != null) &&
            (oRule.getTarget().getSubjects().getSubject().size() > 0))
        {
            List<SubjectType> olSubject = oRule.getTarget().getSubjects().getSubject();
            for (SubjectType oSubject : olSubject)
            {
                if ((oSubject != null) &&
                    (oSubject.getSubjectMatch() != null) &&
                    (oSubject.getSubjectMatch().size() > 0))
                {
                    List<SubjectMatchType> olSubjMatch = oSubject.getSubjectMatch();
                    for (SubjectMatchType oSubjMatch : olSubjMatch)
                    {
                        if ((oSubjMatch != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator() != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator().getAttributeId() != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator().getAttributeId().equals(XACMLConstants.SUBJECT_USER_ID)) &&
                            (oSubjMatch.getAttributeValue() != null) &&
                            (oSubjMatch.getAttributeValue().getContent() != null) &&
                            (oSubjMatch.getAttributeValue().getContent().size() > 0) &&
                            (oSubjMatch.getAttributeValue().getContent().get(0) instanceof String) &&
                            (oSubjMatch.getAttributeValue().getDataType() != null) &&
                            (oSubjMatch.getAttributeValue().getDataType().length() > 0))
                        {
                            if (oSubjMatch.getAttributeValue().getDataType().equals(XACMLConstants.ATTRIBUTE_VALUE_TYPE_EMAIL))
                            {
                                return UserIdFormatType.EMAIL;
                            }
                            else if (oSubjMatch.getAttributeValue().getDataType().equals(XACMLConstants.ATTRIBUTE_VALUE_TYPE_X500))
                            {
                                return UserIdFormatType.X_500;
                            }
                        }   // if ((oResourceMatch != null) &&
                    }   // for (ResourceMatchType oResourceMatch : olResMatch)
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : olResource)
        }   // if ((oRule != null) &&

        return null;
    }


    /**
     * This method looks for the specified attribute within the Subject section
     * and returns its value in the form of a string.
     *
     * @param oRule The rule that is being looked at.
     * @param sAttributeId The ID of the desired attribute
     * @return The value associated with that attribute
     */
    private String extractValueFromSubject(RuleType oRule, String sAttributeId)
    {
        // Extract any values from the Resource section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getSubjects() != null) &&
            (oRule.getTarget().getSubjects().getSubject() != null) &&
            (oRule.getTarget().getSubjects().getSubject().size() > 0))
        {
            List<SubjectType> olSubject = oRule.getTarget().getSubjects().getSubject();
            for (SubjectType oSubject : olSubject)
            {
                if ((oSubject != null) &&
                    (oSubject.getSubjectMatch() != null) &&
                    (oSubject.getSubjectMatch().size() > 0))
                {
                    List<SubjectMatchType> olSubjMatch = oSubject.getSubjectMatch();
                    for (SubjectMatchType oSubjMatch : olSubjMatch)
                    {
                        if ((oSubjMatch != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator() != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator().getAttributeId() != null) &&
                            (oSubjMatch.getSubjectAttributeDesignator().getAttributeId().equals(sAttributeId)) &&
                            (oSubjMatch.getAttributeValue() != null) &&
                            (oSubjMatch.getAttributeValue().getContent() != null) &&
                            (oSubjMatch.getAttributeValue().getContent().size() > 0) &&
                            (oSubjMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oSubjMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                return sValue;
                            }
                        }   // if ((oResourceMatch != null) &&
                    }   // for (ResourceMatchType oResourceMatch : olResMatch)
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : olResource)
        }   // if ((oRule != null) &&

        return null;
    }


    /**
     * This method looks for the specified attribute within the Environment section
     * and returns its value in the form of a string.
     *
     * @param oRule The rule that is being looked at.
     * @param sAttributeId The ID of the desired attribute
     * @return The value associated with that attribute
     */
    private String extractValueFromEnvironment(RuleType oRule, String sAttributeId)
    {
        // Extract any values from the Resource section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getEnvironments() != null) &&
            (oRule.getTarget().getEnvironments().getEnvironment() != null) &&
            (oRule.getTarget().getEnvironments().getEnvironment().size() > 0))
        {
            List<EnvironmentType> olEnv = oRule.getTarget().getEnvironments().getEnvironment();
            for (EnvironmentType oEnv : olEnv)
            {
                if ((oEnv != null) &&
                    (oEnv.getEnvironmentMatch() != null) &&
                    (oEnv.getEnvironmentMatch().size() > 0))
                {
                    List<EnvironmentMatchType> olEnvMatch = oEnv.getEnvironmentMatch();
                    for (EnvironmentMatchType oEnvMatch : olEnvMatch)
                    {
                        if ((oEnvMatch != null) &&
                            (oEnvMatch.getEnvironmentAttributeDesignator() != null) &&
                            (oEnvMatch.getEnvironmentAttributeDesignator().getAttributeId() != null) &&
                            (oEnvMatch.getEnvironmentAttributeDesignator().getAttributeId().equals(sAttributeId)) &&
                            (oEnvMatch.getAttributeValue() != null) &&
                            (oEnvMatch.getAttributeValue().getContent() != null) &&
                            (oEnvMatch.getAttributeValue().getContent().size() > 0) &&
                            (oEnvMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oEnvMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                return sValue;
                            }
                        }   // if ((oResourceMatch != null) &&
                    }   // for (ResourceMatchType oResourceMatch : olResMatch)
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : olResource)
        }   // if ((oRule != null) &&

        return null;
    }

    /**
     * This method creates a date formatted according to HL7 from a
     * date/time in default XML format.
     *
     * @param sXMLDateTime The date or date-time in XML format.
     * @return The date or date-time in HL7 default format.
     */
    private String createHL7Date(String sXMLDateTime)
        throws AdapterPIPException
    {
        String sHL7Date = null;

        // Do we have a "date only"?
        //--------------------------
        if ((sXMLDateTime != null) &&
            (sXMLDateTime.length() == 10))
        {
            try
            {
                Date oXMLDate = oXMLDateOnlyFormatter.parse(sXMLDateTime);
                sHL7Date = oHL7DateOnlyFormatter.format(oXMLDate);
            }
            catch (Exception e)
            {
                String sErrorMessage = "Date had invalid XML format: " + sXMLDateTime + ".  Error = " + e.getMessage();
                throw new AdapterPIPException(sErrorMessage, e);
            }
        }
        // Date + Time
        //------------
        else if ((sXMLDateTime != null) &&
                 (sXMLDateTime.length() > 10))
        {
            try
            {
                Date oXMLDate = oXMLDateTimeFormatter.parse(sXMLDateTime);
                sHL7Date = oHL7DateTimeFormatter.format(oXMLDate);
            }
            catch (Exception e)
            {
                String sErrorMessage = "Date-time had invalid XML format: " + sXMLDateTime + ".  Error = " + e.getMessage();
                throw new AdapterPIPException(sErrorMessage, e);
            }
        }
        // Do not know format - put it in as is.
        else
        {
            sHL7Date = sXMLDateTime;
        }


        return sHL7Date;

    }



    /**
     * This extracts the information for one instnce of a FineGrainedPolicyCriterion from the
     * Rule.
     *
     * @param oRule The rule being checked.
     * @return An instance of the FineGrainedPolicyCriterion that was creatd from the data in the rule.
     */
    private FineGrainedPolicyCriterionType extractSingleFineGrainedPolicyXACML(RuleType oRule)
        throws AdapterPIPException
    {
        FineGrainedPolicyCriterionType oCriterion = new FineGrainedPolicyCriterionType();
        boolean bHasData = false;       // True if we find something to put in the oCriterion object

        // Permit or deny
        //----------------
        if ((oRule != null) &&
            (oRule.getEffect() != null))
        {
            bHasData = true;
            if (oRule.getEffect() == EffectType.PERMIT)
            {
                oCriterion.setPermit(true);
            }
            else
            {
                oCriterion.setPermit(false);
            }
        }

        // Rule ID
        //--------
        if ((oRule != null) &&
            (oRule.getRuleId() != null) &&
            (oRule.getRuleId().length() > 0))
        {
            bHasData = true;
            oCriterion.setSequentialId(oRule.getRuleId());
        }

        // Document Type
        //--------------
        String sDocumentType = extractValueFromResource(oRule, XACMLConstants.RESOURCE_DOCUMENT_TYPE);
        if (sDocumentType != null)
        {
            bHasData = true;
            CeType oCode = new CeType();
            oCode.setCode(sDocumentType);
            oCriterion.setDocumentTypeCode(oCode);
        }

        // Unique Document ID
        //-------------------
        String sDocumentUniqueId = extractValueFromResource(oRule, XACMLConstants.RESOURCE_DOCUMENT_ID);
        if (sDocumentUniqueId != null)
        {
            bHasData = true;
            oCriterion.setUniqueDocumentId(sDocumentUniqueId);
        }

        // Confidentiality Code
        //----------------------
        String sConfidentialityCode = extractValueFromResource(oRule, XACMLConstants.RESOURCE_CONFIDENTIALITY_CODE);
        if (sConfidentialityCode != null)
        {
            bHasData = true;
            CeType oCode = new CeType();
            oCode.setCode(sConfidentialityCode);
            oCriterion.setConfidentialityCode(oCode);
        }

        // User Role
        //----------------------
        String sUserRole = extractValueFromSubject(oRule, XACMLConstants.SUBJECT_ROLE);
        if (sUserRole != null)
        {
            bHasData = true;
            CeType oCode = new CeType();
            oCode.setCode(sUserRole);
            oCriterion.setUserRole(oCode);
        }

        // User ID
        //--------
        String sUserId = extractValueFromSubject(oRule, XACMLConstants.SUBJECT_USER_ID);
        if (sUserId != null)
        {
            bHasData = true;
            oCriterion.setUserId(sUserId);
        }

        // User ID
        //--------
        UserIdFormatType oUserIdFormat = extractUserIdFormatFromSubject(oRule);
        if (oUserIdFormat != null)
        {
            bHasData = true;
            oCriterion.setUserIdFormat(oUserIdFormat);
        }


        // Organization ID
        //-------------------
        String sOrganizationId = extractValueFromSubject(oRule, XACMLConstants.SUBJECT_ORGANIZATION_ID);
        if (sOrganizationId != null)
        {
            bHasData = true;
            oCriterion.setOrganizationId(sOrganizationId);
        }

        // Home Community ID
        //-------------------
        String sHomeCommunityId = extractValueFromSubject(oRule, XACMLConstants.SUBJECT_HOME_COMMUNITY_ID);
        if (sHomeCommunityId != null)
        {
            bHasData = true;
            oCriterion.setHomeCommunityId(sHomeCommunityId);
        }

        // Purpose of Use
        //----------------------
        String sPurposeOfUse = extractValueFromSubject(oRule, XACMLConstants.SUBJECT_PURPOSE_OF_USE);
        if (sPurposeOfUse != null)
        {
            bHasData = true;
            CeType oCode = new CeType();
            oCode.setCode(sPurposeOfUse);
            oCriterion.setPurposeOfUse(oCode);
        }

        // Rule Start Date
        //-----------------
        String sRuleStartDate = extractValueFromEnvironment(oRule, XACMLConstants.ENVIRONMENT_RULE_START_DATE);
        if ((sRuleStartDate != null) &&
            (sRuleStartDate.length() > 0))
        {
            oCriterion.setRuleStartDate(createHL7Date(sRuleStartDate));
            bHasData = true;
        }

        // Rule End Date
        //-----------------
        String sRuleEndDate = extractValueFromEnvironment(oRule, XACMLConstants.ENVIRONMENT_RULE_END_DATE);
        if ((sRuleEndDate != null) &&
            (sRuleEndDate.length() > 0))
        {
            oCriterion.setRuleEndDate(createHL7Date(sRuleEndDate));
            bHasData = true;
        }

        if (bHasData)
        {
            return oCriterion;
        }
        else
        {
            return null;
        }
    }


    /**
     * This method extracts the fine grained policy information from the XACML consent
     * information and returns it in a form to be placed in the PatientPreferences object.
     *
     * @param oConsentXACML The XACML form of the patient consent.
     * @return The FineGrainedPolicyCriteria object containing the fine grained consent information.
     */
    private FineGrainedPolicyCriteriaType extractFineGrainedCriteriaFromXACML(PolicyType oConsentXACML)
        throws AdapterPIPException
    {
        FineGrainedPolicyCriteriaType oFineGrainCriteria = new FineGrainedPolicyCriteriaType();

        // Just a double check to be sure that this has fine grained criteria...
        //-----------------------------------------------------------------------
        if (containsSimpleOptInSetting(oConsentXACML))
        {
            return null;        // Get out of here...  We do not want to proceed.
        }

        // We know that it is not simple opt-in/opt-out - Now lets see if there
        // are fine grained rules here...
        //------------------------------------------------------------------------
        if ((oConsentXACML != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition() != null) &&
            (oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size() > 0))
        {
            // Loop through each rule and extract what we need...
            //----------------------------------------------------
            for (Object oObject : oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition())
            {
                // Currently we only handle "RuleType" objects - Ignore the rest.
                //----------------------------------------------------------------
                if (oObject instanceof RuleType)
                {
                    RuleType oRule = (RuleType) oObject;

                    FineGrainedPolicyCriterionType oCriterion = extractSingleFineGrainedPolicyXACML(oRule);
                    if (oCriterion != null)
                    {
                        oFineGrainCriteria.getFineGrainedPolicyCriterion().add(oCriterion);
                    }
                }
            }
        }

        if ((oFineGrainCriteria.getFineGrainedPolicyCriterion() != null) &&
            (oFineGrainCriteria.getFineGrainedPolicyCriterion().size() > 0))
        {
            return oFineGrainCriteria;
        }
        else
        {
            return null;        // No fine grained criteria was found - return null.
        }
    }


    /**
     * This method extracts the patient ID and assigning authority from the XACML
     * and places the data into the correct location of the patient preferences
     * object.
     *
     * @param oConsentXACML The XACML consent information
     * @param oPtPref The patient preferences object.
     */
    private void extractAndSetPatientIdInfo(PolicyType oConsentXACML, PatientPreferencesType oPtPref)
    {
        if ((oConsentXACML != null) &&
            (oConsentXACML.getTarget() != null) &&
            (oConsentXACML.getTarget().getResources() != null) &&
            (oConsentXACML.getTarget().getResources().getResource() != null) &&
            (oConsentXACML.getTarget().getResources().getResource().size() > 0))
        {
            List<ResourceType> olResources = oConsentXACML.getTarget().getResources().getResource();
            for (ResourceType oResource : olResources)
            {
                if ((oResource.getResourceMatch() != null) &&
                    (oResource.getResourceMatch().size() > 0))
                {
                    List<ResourceMatchType> olResourceMatch = oResource.getResourceMatch();
                    for (ResourceMatchType oResMatch : olResourceMatch)
                    {
                        if ((oResMatch.getResourceAttributeDesignator() != null) &&
                            (oResMatch.getResourceAttributeDesignator().getAttributeId() != null) &&
                            (oResMatch.getResourceAttributeDesignator().getAttributeId().equals(XACMLConstants.PATIENT_SUBJECT_ID)) &&
                            (oResMatch.getAttributeValue() != null) &&
                            (oResMatch.getAttributeValue().getContent() != null) &&
                            (oResMatch.getAttributeValue().getContent().size() > 0))
                        {
                            // With the way we are formatting this record - what we are looking for will be in the first
                            // node of the content.  The content object will be of type "Element" and the values will be
                            // in the attributes.
                            //-------------------------------------------------------------------------------------------
                            if ((oResMatch.getAttributeValue().getContent().get(0) != null) &&
                                (oResMatch.getAttributeValue().getContent().get(0) instanceof Element))
                            {
                                Element oContent = (Element) oResMatch.getAttributeValue().getContent().get(0);

                                // Make sure that this is the right tag.
                                //--------------------------------------
                                if (oContent.getTagName().equals(XACMLConstants.NHIN_PATIENT_ID_TAG))
                                {
                                    String sPatientId = oContent.getAttribute(XACMLConstants.NHIN_PATIENT_ID_TAG_EXTENSION);
                                    if ((sPatientId != null) &&
                                        (sPatientId.length() > 0))
                                    {
                                        oPtPref.setPatientId(sPatientId);
                                    }

                                    String sAssigningAuthority = oContent.getAttribute(XACMLConstants.NHIN_PATIENT_ID_TAG_ROOT);
                                    if ((sAssigningAuthority != null) &&
                                        (sAssigningAuthority.length() > 0))
                                    {
                                        oPtPref.setAssigningAuthority(sAssigningAuthority);
                                    }
                                }
                            }   // if ((oResMatch.getAttributeValue().getContent().get(0) != null) &&
                        }   // if ((oResMatch.getResourceAttributeDesignator() != null) &&
                    }   // for (ResourceMatchType oResMatch : olResourceMatch)
                }   // if ((oResource.getResourceMatch() != null) &&
            }   // for (ResourceType oResource : olResources)
        }   // if ((oConsentXACML != null) &&
    }

    /**
     * This method extracts the patient preferences data from the XACML.
     *
     * @param oConsentXACML The XACML consent information
     * @return The patient preferences.
     */
    public PatientPreferencesType extractPatientPreferences(PolicyType oConsentXACML)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();
        FineGrainedPolicyMetadataType metadata = new FineGrainedPolicyMetadataType();
        oPtPref.setFineGrainedPolicyMetadata(metadata);

        String sPolicyOid = extractXACMLPolicyOID(oConsentXACML);
        metadata.setPolicyOID(sPolicyOid);

        // Get patient ID and Assigning authority
        //-----------------------------------------
        extractAndSetPatientIdInfo(oConsentXACML, oPtPref);

        // Check first for simple opt-in/opt-out
        //---------------------------------------
        if (containsSimpleOptInSetting(oConsentXACML))
        {
            oPtPref.setOptIn(extractSimpleOptInSetting(oConsentXACML));
        }
        else
        {
            oPtPref.setOptIn(false);        // initialize it - it is not being used if there are fine grained criteria.
            FineGrainedPolicyCriteriaType oFineGrainCriteria = extractFineGrainedCriteriaFromXACML(oConsentXACML);
            if (oFineGrainCriteria != null)
            {
                oPtPref.setFineGrainedPolicyCriteria(oFineGrainCriteria);
            }
        }

        return oPtPref;

    }






}
