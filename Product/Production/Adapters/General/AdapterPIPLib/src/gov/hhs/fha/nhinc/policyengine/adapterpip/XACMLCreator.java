package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UserIdFormatType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.namespace.QName;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionsType;
import oasis.names.tc.xacml._2_0.policy.schema.os.AttributeDesignatorType;
import oasis.names.tc.xacml._2_0.policy.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EffectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentsType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourcesType;
import oasis.names.tc.xacml._2_0.policy.schema.os.RuleType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectAttributeDesignatorType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectsType;
import oasis.names.tc.xacml._2_0.policy.schema.os.TargetType;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Element;
import org.w3c.dom.Document;


/**
 * This class is used to create the XACML policy object.
 *
 * @author Les Westberg
 */
public class XACMLCreator
{
    private static final String HL7_DATE_ONLY_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat oHL7DateOnlyFormatter = new SimpleDateFormat(HL7_DATE_ONLY_FORMAT);
    private static final String HL7_DATE_TIME_FORMAT = "yyyyMMddHHmmssZ";
    private static final SimpleDateFormat oHL7DateTimeFormatter = new SimpleDateFormat(HL7_DATE_TIME_FORMAT);
    private static final String XML_DATE_ONLY_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat oXMLDateOnlyFormatter = new SimpleDateFormat(XML_DATE_ONLY_FORMAT);
    private static final String XML_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat oXMLDateTimeFormatter = new SimpleDateFormat(XML_DATE_TIME_FORMAT);


    /**
     * This method constructs a Action that will be placed in the XACML target.
     *
     * @param sMatchId The value to use for the match ID.
     * @param sDataType  The value to use for the data type.
     * @param sAttributeId The value to be used for the attribute Id.
     * @param sAttributeValue The actual value.
     * @return
     */
    private ActionType createAction(String sMatchId, String sDataType, String sAttributeId, String sAttributeValue)
    {
        ActionType oAction = new ActionType();

        // Action Match
        //--------------
        ActionMatchType oActionMatch = new ActionMatchType();
        oAction.getActionMatch().add(oActionMatch);
        oActionMatch.setMatchId(sMatchId);

        // Attribute Value
        //-----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oActionMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(sDataType);
        oAttributeValue.getContent().add(sAttributeValue);

        // Action Attribute Designator
        //-----------------------------
        AttributeDesignatorType oActionAttrDesig = new AttributeDesignatorType();
        oActionMatch.setActionAttributeDesignator(oActionAttrDesig);
        oActionAttrDesig.setDataType(sDataType);
        oActionAttrDesig.setAttributeId(sAttributeId);

        return oAction;

    }

    /**
     * This method constructs a resource that will be placed in a rule.
     *
     * @param sPatientId The patient ID of the patient
     * @param sAssigningAuthority The assigning authority of associated with the patient iD.
     * @return
     */
    private ResourceType createResourcePatientId(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        ResourceType oResource = new ResourceType();

        // Resource Match
        //--------------
        ResourceMatchType oResMatch = new ResourceMatchType();
        oResource.getResourceMatch().add(oResMatch);
        oResMatch.setMatchId(XACMLConstants.MATCH_ID_IID_EQUAL);

        // Attribute Value
        //-----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oResMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_IID);

        Map<QName, String> mapAttrValueAttrs = oAttributeValue.getOtherAttributes();
        QName oHL7Attr = new QName("xmlns:hl7");
        mapAttrValueAttrs.put(oHL7Attr, XACMLConstants.NAMESPACE_HL7_V3);

        DocumentBuilderFactory oDOMFactory = null;
        DocumentBuilder oDOMBuilder = null;
        Document oDOMDocument = null;
        Element oElement = null;

        try
        {
            oDOMFactory = DocumentBuilderFactory.newInstance();
            oDOMBuilder = oDOMFactory.newDocumentBuilder();
            oDOMDocument = oDOMBuilder.newDocument();
            oElement = oDOMDocument.createElementNS(XACMLConstants.NAMESPACE_HL7_V3, XACMLConstants.NHIN_PATIENT_ID_TAG);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to create DOM factory and builder objects.  Error: " +
                                   e.getMessage();
            throw new AdapterPIPException(sErrorMessage, e);
        }
        oElement.setAttribute(XACMLConstants.NHIN_PATIENT_ID_TAG_ROOT, sAssigningAuthority);
        oElement.setAttribute(XACMLConstants.NHIN_PATIENT_ID_TAG_EXTENSION, sPatientId);
        oAttributeValue.getContent().add(oElement);

        // Resource Attribute Designator
        //-----------------------------
        AttributeDesignatorType oResAttrDesig = new AttributeDesignatorType();
        oResMatch.setResourceAttributeDesignator(oResAttrDesig);
        oResAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_IID);
        oResAttrDesig.setAttributeId(XACMLConstants.PATIENT_SUBJECT_ID);

        return oResource;

    }


    /**
     * This method constructs a resource that will be placed in a rule.
     *
     * @param sMatchId The value to use for the match ID.
     * @param sDataType  The value to use for the data type.
     * @param sAttributeId The value to be used for the attribute Id.
     * @param sAttributeValue The actual value.
     * @return
     */
    private ResourceType createResource(String sMatchId, String sDataType, String sAttributeId, String sAttributeValue)
    {
        ResourceType oResource = new ResourceType();

        // Resource Match
        //--------------
        ResourceMatchType oResMatch = new ResourceMatchType();
        oResource.getResourceMatch().add(oResMatch);
        oResMatch.setMatchId(sMatchId);

        // Attribute Value
        //-----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oResMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(sDataType);

        // If this is an HL7 type - need to add in the name space for it
        //---------------------------------------------------------------
        if (sDataType.equals(XACMLConstants.ATTRIBUTE_VALUE_TYPE_IID))
        {
            Map<QName, String> mapAttrValueAttrs = oAttributeValue.getOtherAttributes();
            //QName oHL7Attr = new QName(XACMLConstants.NAMESPACE_HL7_V3, XACMLConstants.QNAME_NAMESPACE_PREFIX, XACMLConstants.QNAME_NAMESPACE_LOCALPART_HL7);
            QName oHL7Attr = new QName("xmlns:hl7");
            mapAttrValueAttrs.put(oHL7Attr, XACMLConstants.NAMESPACE_HL7_V3);
            oAttributeValue.getContent().add(sAttributeValue);
        }
        else
        {
            oAttributeValue.getContent().add(sAttributeValue);
        }

        // Resource Attribute Designator
        //-----------------------------
        AttributeDesignatorType oResAttrDesig = new AttributeDesignatorType();
        oResMatch.setResourceAttributeDesignator(oResAttrDesig);
        oResAttrDesig.setDataType(sDataType);
        oResAttrDesig.setAttributeId(sAttributeId);

        return oResource;

    }

    /**
     * This method constructs a subject that will be placed in a rule.
     *
     * @param sMatchId The value to use for the match ID.
     * @param sDataType  The value to use for the data type.
     * @param sAttributeId The value to be used for the attribute Id.
     * @param sAttributeValue The actual value.
     * @return
     */
    private SubjectType createSubject(String sMatchId, String sDataType, String sAttributeId, String sAttributeValue)
    {
        SubjectType oSubject = new SubjectType();

        // Subject Match
        //--------------
        SubjectMatchType oSubjMatch = new SubjectMatchType();
        oSubject.getSubjectMatch().add(oSubjMatch);
        oSubjMatch.setMatchId(sMatchId);

        // Attribute Value
        //-----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oSubjMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(sDataType);
        oAttributeValue.getContent().add(sAttributeValue);

        // Subject Attribute Designator
        //-----------------------------
        SubjectAttributeDesignatorType oSubjAttrDesig = new SubjectAttributeDesignatorType();
        oSubjMatch.setSubjectAttributeDesignator(oSubjAttrDesig);
        oSubjAttrDesig.setDataType(sDataType);
        oSubjAttrDesig.setAttributeId(sAttributeId);

        return oSubject;

    }


    /**
     * This method constructs an Environment criteria that will be placed in the XACML target.
     *
     * @param sMatchId The value to use for the match ID.
     * @param sDataType  The value to use for the data type.
     * @param sAttributeId The value to be used for the attribute Id.
     * @param sAttributeValue The actual value.
     * @return
     */
    private EnvironmentType createEnvironment(String sMatchId, String sDataType, String sAttributeId, String sAttributeValue)
    {
        EnvironmentType oEnvironment = new EnvironmentType();

        // Environment Match
        //-------------------
        EnvironmentMatchType oEnvMatch = new EnvironmentMatchType();
        oEnvironment.getEnvironmentMatch().add(oEnvMatch);
        oEnvMatch.setMatchId(sMatchId);

        // Attribute Value
        //-----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oEnvMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(sDataType);
        oAttributeValue.getContent().add(sAttributeValue);

        // Action Attribute Designator
        //-----------------------------
        AttributeDesignatorType oEnvAttrDesig = new AttributeDesignatorType();
        oEnvMatch.setEnvironmentAttributeDesignator(oEnvAttrDesig);
        oEnvAttrDesig.setDataType(sDataType);
        oEnvAttrDesig.setAttributeId(sAttributeId);

        return oEnvironment;

    }

    /**
     * This formats a string that represents the patient ID in the format of:
     *
     * <nhin:PatientId root="1.1" extension="11111" />
     *
     * @param oPtPref The patient preference information.
     * @return
     */
    private String createNhinPatientId(PatientPreferencesType oPtPref)
    {
        String sPatientId = "";
        StringBuffer sbPatientId = new StringBuffer();

        // Make sure we have an assigning authority or a patient ID.
        //-----------------------------------------------------------
        if (((oPtPref.getAssigningAuthority() != null) &&
            (oPtPref.getAssigningAuthority().trim().length() > 0)) ||
            ((oPtPref.getPatientId() != null) &&
            (oPtPref.getPatientId().trim().length() > 0)))
        {
            sbPatientId.append("<" + XACMLConstants.NHIN_PATIENT_ID_TAG + " " + XACMLConstants.NHIN_PATIENT_ID_TAG_ROOT + "=\"");
            if (oPtPref.getAssigningAuthority() != null)
            {
                sbPatientId.append(oPtPref.getAssigningAuthority().trim());
            }
            sbPatientId.append("\" " + XACMLConstants.NHIN_PATIENT_ID_TAG_EXTENSION + "=\"");
            if (oPtPref.getPatientId() != null)
            {
                sbPatientId.append(oPtPref.getPatientId().trim());
            }
            sbPatientId.append("\" />");
            sPatientId = sbPatientId.toString();
        }

        return sPatientId;
    }

    /**
     * This method creates an date formatted according to XML default from a
     * HL7 date/time format.
     *
     * @param sHL7DateTime The date or date-time in HL7 format.
     * @return The date or date-time in XML default format.
     */
    private String createXMLDate(String sHL7DateTime)
        throws AdapterPIPException
    {
        String sXMLDate = null;

        // Do we have a "date only"?
        //--------------------------
        if ((sHL7DateTime != null) &&
            (sHL7DateTime.length() == 8))
        {
            try
            {
                Date oHL7Date = oHL7DateOnlyFormatter.parse(sHL7DateTime);
                sXMLDate = oXMLDateOnlyFormatter.format(oHL7Date);
            }
            catch (Exception e)
            {
                String sErrorMessage = "Date had invalid HL7 format: " + sHL7DateTime + ".  Error = " + e.getMessage();
                throw new AdapterPIPException(sErrorMessage, e);
            }
        }
        // Date + Time
        //------------
        else if ((sHL7DateTime != null) &&
              (sHL7DateTime.length() > 8))
        {
            try
            {
                Date oHL7Date = oHL7DateTimeFormatter.parse(sHL7DateTime);
                sXMLDate = oXMLDateTimeFormatter.format(oHL7Date);
            }
            catch (Exception e)
            {
                String sErrorMessage = "Date-time had invalid HL7 format: " + sHL7DateTime + ".  Error = " + e.getMessage();
                throw new AdapterPIPException(sErrorMessage, e);
            }
        }
        // Do not know format - put it in as is.
        else
        {
            sXMLDate = sHL7DateTime;
        }


        return sXMLDate;

    }

    /**
     * This method creates the Rule associated with a single instance of a
     * fine grained criterion.
     *
     * @param iRuleId The ID number for this rule.
     * @param oCriterion The patient Preferneces
     * @return The rule representing a single fine grained policy settings.
     */
    private RuleType createFineGrainedRule(int iRuleId, FineGrainedPolicyCriterionType oCriterion)
        throws AdapterPIPException
    {
        RuleType oRule = new RuleType();

        oRule.setRuleId(Integer.toString(iRuleId));

        // Permission setting
        //--------------------
        String sPermitOrDeny = "";
        if (oCriterion.isPermit())
        {
            oRule.setEffect(EffectType.PERMIT);
            sPermitOrDeny = "Permit";
        }
        else
        {
            oRule.setEffect(EffectType.DENY);
            sPermitOrDeny = "Deny";
        }

        oRule.setDescription(XACMLConstants.DESCRIPTION_FINE_GRAINED_RULE);

        // Rule Target
        //------------
        TargetType oRuleTarget = new TargetType();
        oRule.setTarget(oRuleTarget);

        // Subjects/Subject
        // Subject will contain the information for:
        //    User Role
        //    Organization ID
        //    Home Community ID
        //    User ID
        //    Purpose of use.
        //---------------------------------------------------------------------
        SubjectsType oSubjects = new SubjectsType();
        List<SubjectType> olSubject = oSubjects.getSubject();
        boolean bHaveSubjectInfo = false;

        // User Role
        //-----------
        if ((oCriterion.getUserRole() != null) &&
            (oCriterion.getUserRole().getCode() != null) &&
            (oCriterion.getUserRole().getCode().length() > 0))
        {
            SubjectType oSubject = createSubject(XACMLConstants.MATCH_ID_STRING_EQUAL,
                                                 XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING,
                                                 XACMLConstants.SUBJECT_ROLE,
                                                 oCriterion.getUserRole().getCode());
            olSubject.add(oSubject);
            bHaveSubjectInfo = true;

        }   // User Role

        // Organization ID
        //-----------------
        if ((oCriterion.getOrganizationId() != null) &&
            (oCriterion.getOrganizationId().length() > 0))
        {
            SubjectType oSubject = createSubject(XACMLConstants.MATCH_ID_URI_EQUAL,
                                                 XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI,
                                                 XACMLConstants.SUBJECT_ORGANIZATION_ID,
                                                 oCriterion.getOrganizationId());
            olSubject.add(oSubject);
            bHaveSubjectInfo = true;
        }   // Organization ID


        // Home Community ID
        //------------------
        if ((oCriterion.getHomeCommunityId() != null) &&
            (oCriterion.getHomeCommunityId().length() > 0))
        {
            SubjectType oSubject = createSubject(XACMLConstants.MATCH_ID_URI_EQUAL,
                                                 XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI,
                                                 XACMLConstants.SUBJECT_HOME_COMMUNITY_ID,
                                                 oCriterion.getHomeCommunityId());
            olSubject.add(oSubject);
            bHaveSubjectInfo = true;
        }   // Organization ID

        // User ID
        //--------
        if ((oCriterion.getUserId() != null) &&
            (oCriterion.getUserId().length() > 0) &&
            (oCriterion.getUserIdFormat() != null) &&
            (!oCriterion.getUserIdFormat().equals(UserIdFormatType.NONE)))
        {
            SubjectType oSubject = null;
            if (oCriterion.getUserIdFormat().equals(UserIdFormatType.EMAIL))
            {
                oSubject = createSubject(XACMLConstants.MATCH_ID_EMAIL_EQUAL,
                                         XACMLConstants.ATTRIBUTE_VALUE_TYPE_EMAIL,
                                         XACMLConstants.SUBJECT_USER_ID,
                                         oCriterion.getUserId());
            }
            else
            {
                oSubject = createSubject(XACMLConstants.MATCH_ID_X500_EQUAL,
                                         XACMLConstants.ATTRIBUTE_VALUE_TYPE_X500,
                                         XACMLConstants.SUBJECT_USER_ID,
                                         oCriterion.getUserId());
            }
            olSubject.add(oSubject);
            bHaveSubjectInfo = true;
        }   // Organization ID

        // Purpose of Use
        //----------------
        if ((oCriterion.getPurposeOfUse() != null) &&
            (oCriterion.getPurposeOfUse().getCode() != null) &&
            (oCriterion.getPurposeOfUse().getCode().length() > 0))
        {
            SubjectType oSubject = createSubject(XACMLConstants.MATCH_ID_STRING_EQUAL,
                                                 XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING,
                                                 XACMLConstants.SUBJECT_PURPOSE_OF_USE,
                                                 oCriterion.getPurposeOfUse().getCode());
            olSubject.add(oSubject);
            bHaveSubjectInfo = true;
        }   // User Role

        // if we had subject data, put it in now.
        //---------------------------------------
        if (bHaveSubjectInfo)
        {
            oRuleTarget.setSubjects(oSubjects);
        }

        // resources/Resource
        // Rsoources will contain the information for:
        //     Confidentiality Code
        //     Document Type.
        //     Unique Document ID
        //---------------------------------------------------------------------
        ResourcesType oResources = new ResourcesType();
        List<ResourceType> olResource = oResources.getResource();
        boolean bHaveResourceInfo = false;

        // Confidentiality Code
        //---------------------
        if ((oCriterion.getConfidentialityCode() != null) &&
             (oCriterion.getConfidentialityCode().getCode() != null) &&
             (oCriterion.getConfidentialityCode().getCode().length() > 0))
        {
            ResourceType oRuleResource = createResource(XACMLConstants.MATCH_ID_STRING_EQUAL,
                                                        XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING,
                                                        XACMLConstants.RESOURCE_CONFIDENTIALITY_CODE,
                                                        oCriterion.getConfidentialityCode().getCode());

            olResource.add(oRuleResource);
            bHaveResourceInfo = true;
        }

        // Document Type Code
        //---------------------
        if ((oCriterion.getDocumentTypeCode() != null) &&
            (oCriterion.getDocumentTypeCode().getCode() != null) &&
            (oCriterion.getDocumentTypeCode().getCode().length() > 0))
        {
            ResourceType oRuleResource = createResource(XACMLConstants.MATCH_ID_STRING_EQUAL,
                                                        XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING,
                                                        XACMLConstants.RESOURCE_DOCUMENT_TYPE,
                                                        oCriterion.getDocumentTypeCode().getCode());

            olResource.add(oRuleResource);
            bHaveResourceInfo = true;
        }

        // Document ID
        //------------
        if ((oCriterion.getUniqueDocumentId() != null) &&
            (oCriterion.getUniqueDocumentId().length() > 0))
        {
            ResourceType oRuleResource = createResource(XACMLConstants.MATCH_ID_STRING_EQUAL,
                                                        XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING,
                                                        XACMLConstants.RESOURCE_DOCUMENT_ID,
                                                        oCriterion.getUniqueDocumentId());

            olResource.add(oRuleResource);
            bHaveResourceInfo = true;
        }

        // if we had resource data, put it in now.
        //---------------------------------------
        if (bHaveResourceInfo)
        {
            oRuleTarget.setResources(oResources);
        }

        // Environments/Environment
        // Environment will contain the information for:
        //     Rule Start Date
        //     Rule End Date.
        //---------------------------------------------------------------------
        EnvironmentsType oEnvironments = new EnvironmentsType();
        List<EnvironmentType> olEnv = oEnvironments.getEnvironment();
        boolean bHaveEnvInfo = false;

        // Rule Start Date Code
        //---------------------
        if (oCriterion.getRuleStartDate() != null)
        {
            EnvironmentType oRuleEnv = createEnvironment(XACMLConstants.MATCH_ID_DATE_GREATER_OR_EQUAL,
                                                         XACMLConstants.ATTRIBUTE_VALUE_TYPE_DATE,
                                                         XACMLConstants.ENVIRONMENT_RULE_START_DATE,
                                                         createXMLDate(oCriterion.getRuleStartDate()));

            olEnv.add(oRuleEnv);
            bHaveEnvInfo = true;
        }

        // Rule End Date Code
        //---------------------
        if (oCriterion.getRuleEndDate() != null)
        {
            EnvironmentType oRuleEnv = createEnvironment(XACMLConstants.MATCH_ID_DATE_LESS_OR_EQUAL,
                                                         XACMLConstants.ATTRIBUTE_VALUE_TYPE_DATE,
                                                         XACMLConstants.ENVIRONMENT_RULE_END_DATE,
                                                         createXMLDate(oCriterion.getRuleEndDate()));

            olEnv.add(oRuleEnv);
            bHaveEnvInfo = true;
        }

        // if we had environment data, put it in now.
        //---------------------------------------
        if (bHaveEnvInfo)
        {
            oRuleTarget.setEnvironments(oEnvironments);
        }


        return oRule;
    }

    /**
     * This method creates the Rule associated with the patient Opt In/Opt Out settings.
     *
     * @param iRuleId The ID number for this rule.
     * @param oPtPref The patient Preferneces
     * @return The rule showing the patient Opt in Opt Out settings.
     */
    private RuleType createOptInOutRule(int iRuleId, PatientPreferencesType oPtPref)
    {
        RuleType oRule = new RuleType();

        oRule.setRuleId(Integer.toString(iRuleId));

        // Permission setting
        //--------------------
        String sPermitOrDeny = "";
        if (oPtPref.isOptIn())
        {
            oRule.setEffect(EffectType.PERMIT);
            sPermitOrDeny = "Permit";
        }
        else
        {
            oRule.setEffect(EffectType.DENY);
            sPermitOrDeny = "Deny";
        }

        oRule.setDescription(XACMLConstants.DESCRIPTION_OPT_IN_OUT_RULE);

        // Rule Target
        //------------
        TargetType oRuleTarget = new TargetType();
        oRule.setTarget(oRuleTarget);

        return oRule;
    }



    /**
     * This method creates the XACML policy document that represents the
     * patient's consent preference information.
     *
     * @param oPtPref The patient's consent preference information.
     * @return The XACML Policy that represents this consent information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *          This exception is thrown if any error occurs.
     */
    public PolicyType createConsentXACMLDoc(PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        PolicyType oPolicy = new PolicyType();

        // If this is the first time this is being stored, generate the OID, otherwise
        // use the one we already know.
        //--------------------------------------------------------------------------------------
        if ((oPtPref.getFineGrainedPolicyMetadata() != null) &&
            (oPtPref.getFineGrainedPolicyMetadata().getPolicyOID() != null) &&
            (oPtPref.getFineGrainedPolicyMetadata().getPolicyOID().length() > 0))
        {
            oPolicy.setPolicyId(oPtPref.getFineGrainedPolicyMetadata().getPolicyOID());
        }
        else
        {
            UUID oPolicyId = UUID.randomUUID();
            String sPolicyId = oPolicyId.toString();
            oPolicy.setPolicyId(sPolicyId);
        }

        // Rule combining algorithm
        //--------------------------
        oPolicy.setRuleCombiningAlgId(XACMLConstants.RULE_COMBINING_ALG_FIRST_APPLICABLE);

        // Description
        //------------
        oPolicy.setDescription(XACMLConstants.POLICY_DESCRIPTION);

        // Target
        //--------
        TargetType oTarget = new TargetType();
        oPolicy.setTarget(oTarget);

        // Actions -> Action ->
        // Set the action to be specific to Query/Retrieve Documents Service
        //-------------------------------------------------------------
        ActionsType oActions = new ActionsType();
        oTarget.setActions(oActions);

        // Action for Doc Retrieve
        //------------------------
        ActionType oAction = createAction(XACMLConstants.MATCH_ID_URI_EQUAL,
                                          XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI,
                                          XACMLConstants.ACTION_ID,
                                          XACMLConstants.ATTRIBUTE_VALUE_RETRIEVE_DOCUMENT);
        oActions.getAction().add(oAction);

        // Action for Doc Query
        //------------------------
        oAction = createAction(XACMLConstants.MATCH_ID_URI_EQUAL,
                               XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI,
                               XACMLConstants.ACTION_ID,
                               XACMLConstants.ATTRIBUTE_VALUE_QUERY_DOCUMENT);
        oActions.getAction().add(oAction);


        // Set up resource to pertain to the correct patient ID and assigning authority.
        // It appears that the NHIN spec has put the patient ID in as
        // a subject in the resource section rather than as a resouce.  I am
        // matching the spec, but I think the spec is wrong.
        //-------------------------------------------------------------------------------
        ResourcesType oResourcess = new ResourcesType();
        oTarget.setResources(oResourcess);
        List<ResourceType> olResource = oResourcess.getResource();
        // String sNHINPatientId = createNhinPatientId(oPtPref);
        String sPatientId = "";
        if (oPtPref.getPatientId() != null)
        {
            sPatientId = oPtPref.getPatientId();
        }
        String sAssigningAuthority = "";
        if (oPtPref.getAssigningAuthority() != null)
        {
            sAssigningAuthority = oPtPref.getAssigningAuthority();
        }

        ResourceType oResource = createResourcePatientId(sPatientId, sAssigningAuthority);
        if (oResource != null)
        {
            olResource.add(oResource);
        }

        // Rules...
        //---------
        int iRuleIdx = 1;           // Index for each rule number.
        List<Object> olRules = oPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

        // Put blanket opt-in or opt-out rule if there is no fine-grained criteria.
        //--------------------------------------------------------------------------
        if ((oPtPref.getFineGrainedPolicyCriteria() == null) ||
            (oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion() == null) ||
            (oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion().size() <= 0))
        {
            // Add in a rule for the Opt-in/Opt-Out setting
            //----------------------------------------------
            RuleType oRule = createOptInOutRule(iRuleIdx++, oPtPref);
            if (oRule != null)
            {
                olRules.add(oRule);
            }
        }
        else
        {
            // Add in rule for each of the Fine Grained Policy Settings
            //----------------------------------------------------------
            List<FineGrainedPolicyCriterionType> olCriterion = oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion();
            for (FineGrainedPolicyCriterionType oCriterion : olCriterion)
            {
                RuleType oRule = createFineGrainedRule(iRuleIdx++, oCriterion);
                if (oRule != null)
                {
                    olRules.add(oRule);
                }
            }
        }

        return oPolicy;
    }

}
