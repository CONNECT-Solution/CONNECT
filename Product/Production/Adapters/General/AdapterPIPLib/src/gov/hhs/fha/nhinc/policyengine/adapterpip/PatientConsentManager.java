package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040DocumentationOf;
import org.hl7.v3.POCDMT000040ServiceEvent;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Act;
import org.hl7.v3.POCDMT000040NonXMLBody;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.II;
import org.hl7.v3.CE;
import org.hl7.v3.POCDMT000040StructuredBody;
import org.hl7.v3.CS;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.TS;
import org.hl7.v3.XActClassDocumentEntryAct;
import org.hl7.v3.XDocumentActMood;

import java.io.StringReader;
import java.io.StringWriter;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Date;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.repository.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.repository.model.EventCode;
import gov.hhs.fha.nhinc.repository.service.DocumentService;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import oasis.names.tc.xacml._2_0.policy.schema.os.ActionMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionsType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionType;
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

/**
 * This class manages the patient consent form.  It stores or retrieves the patient
 * consent document from the repository.
 *
 * @author Les Westberg
 */
public class PatientConsentManager
{
    private static Log log = LogFactory.getLog(PatientConsentManager.class);
    private static JAXBContext oJaxbContextHL7 = null;
    private static JAXBContext oJaxbContextXACML = null;
    private static Marshaller oHL7Marshaller = null;
    private static Marshaller oXACMLMarshaller = null;
    private static Unmarshaller oHL7Unmarshaller = null;
    private static Unmarshaller oXACMLUnmarshaller = null;
    private static DocumentRegistryService oDocRegService = null;
    private static DocumentRepositoryService oDocRepService = null;

    static
    {
        try
        {
            oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            oHL7Marshaller = oJaxbContextHL7.createMarshaller();
            oHL7Unmarshaller = oJaxbContextHL7.createUnmarshaller();

            oJaxbContextXACML = JAXBContext.newInstance("oasis.names.tc.xacml._2_0.policy.schema.os");
            oXACMLMarshaller = oJaxbContextXACML.createMarshaller();
            oXACMLUnmarshaller = oJaxbContextXACML.createUnmarshaller();

        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to create HL7 JAXB Context and Marshaller.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    /**
     * Return a handle to the document registry port.
     *
     * @return The handle to the document registry port web service.
     */
    private DocumentRegistryPortType getDocumentRegistryPort()
        throws AdapterPIPException
    {
        DocumentRegistryPortType oDocRegistryPort = null;

        try
        {
            if (oDocRegService == null)
            {
                oDocRegService = new DocumentRegistryService();
            }

            oDocRegistryPort = oDocRegService.getDocumentRegistryPortSoap();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(CDAConstants.DOC_REGISTRY_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = CDAConstants.DOC_REGISTRY_SERVICE_DEFAULT_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       CDAConstants.DOC_REGISTRY_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
            ((javax.xml.ws.BindingProvider)oDocRegistryPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Document Registry web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oDocRegistryPort;
    }

    /**
     * Return a handle to the document repository port.
     *
     * @return The handle to the document registry port web service.
     */
    private DocumentRepositoryPortType getDocumentRepositoryPort()
        throws AdapterPIPException
    {
        DocumentRepositoryPortType oDocRepositoryPort = null;

        try
        {
            if (oDocRepService == null)
            {
                oDocRepService = new DocumentRepositoryService();
            }

            oDocRepositoryPort = oDocRepService.getDocumentRepositoryPortSoap();

            // Get the real endpoint URL for this service.
            //--------------------------------------------
            String sEndpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(CDAConstants.DOC_REPOSITORY_SERVICE_NAME);

            if ((sEndpointURL == null) ||
                (sEndpointURL.length() <= 0))
            {
                sEndpointURL = CDAConstants.DOC_REPOSITORY_SERVICE_DEFAULT_URL;
                String sErrorMessage = "Failed to retrieve the Endpoint URL for service: '" +
                                       CDAConstants.DOC_REPOSITORY_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + sEndpointURL + "'";
                log.warn(sErrorMessage);
            }
            ((javax.xml.ws.BindingProvider)oDocRepositoryPort).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, sEndpointURL);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve a handle to the Document Repository web service.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oDocRepositoryPort;
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

        // resources/Resource
        //--------------------
        ResourcesType oResources = new ResourcesType();
        oRuleTarget.setResources(oResources);
        List<ResourceType> olResource = oResources.getResource();
        ResourceType oRuleResource = new ResourceType();
        olResource.add(oRuleResource);

        // Resource match
        //----------------
        ResourceMatchType oResMatch = new ResourceMatchType();
        oRuleResource.getResourceMatch().add(oResMatch);
        oResMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

        // Attribute Value
        //----------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oResMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
        oAttributeValue.getContent().add(sPermitOrDeny);

        // Resource Attribute Designator
        //-------------------------------
        AttributeDesignatorType oResAttrDesig = new AttributeDesignatorType();
        oResMatch.setResourceAttributeDesignator(oResAttrDesig);
        oResAttrDesig.setAttributeId(XACMLConstants.RESOURCE_PATIENT_OPT_IN);
        oResAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);

        return oRule;
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
        // Subject will contain the information for User Role and
        // purpose of use.
        //---------------------------------------------------------------------
        if (((oCriterion.getUserRole() != null) &&
             (oCriterion.getUserRole().getCode() != null) &&
             (oCriterion.getUserRole().getCode().length() > 0)) ||
            ((oCriterion.getPurposeOfUse() != null) &&
             (oCriterion.getPurposeOfUse().getCode() != null) &&
             (oCriterion.getPurposeOfUse().getCode().length() > 0)))
        {

            SubjectsType oSubjects = new SubjectsType();
            oRuleTarget.setSubjects(oSubjects);
            List<SubjectType> olSubject = oSubjects.getSubject();
            SubjectType oSubject = new SubjectType();
            olSubject.add(oSubject);

            // User Role
            //-----------
            if ((oCriterion.getUserRole() != null) &&
                (oCriterion.getUserRole().getCode() != null) &&
                (oCriterion.getUserRole().getCode().length() > 0))
            {
                // Subject Match
                //--------------
                SubjectMatchType oSubjMatch = new SubjectMatchType();
                oSubject.getSubjectMatch().add(oSubjMatch);
                oSubjMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

                // Attribute Value
                //-----------------
                AttributeValueType oAttributeValue = new AttributeValueType();
                oSubjMatch.setAttributeValue(oAttributeValue);
                oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oAttributeValue.getContent().add(oCriterion.getUserRole().getCode());

                // Subject Attribute Designator
                //-----------------------------
                SubjectAttributeDesignatorType oSubjAttrDesig = new SubjectAttributeDesignatorType();
                oSubjMatch.setSubjectAttributeDesignator(oSubjAttrDesig);
                oSubjAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oSubjAttrDesig.setAttributeId(XACMLConstants.SUBJECT_ROLE);
            }   // User Role

            // Purpose of Use
            //----------------
            if ((oCriterion.getPurposeOfUse() != null) &&
                (oCriterion.getPurposeOfUse().getCode() != null) &&
                (oCriterion.getPurposeOfUse().getCode().length() > 0))
            {
                // Subject Match
                //--------------
                SubjectMatchType oSubjMatch = new SubjectMatchType();
                oSubject.getSubjectMatch().add(oSubjMatch);
                oSubjMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

                // Attribute Value
                //-----------------
                AttributeValueType oAttributeValue = new AttributeValueType();
                oSubjMatch.setAttributeValue(oAttributeValue);
                oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oAttributeValue.getContent().add(oCriterion.getPurposeOfUse().getCode());

                // Subject Attribute Designator
                //-----------------------------
                SubjectAttributeDesignatorType oSubjAttrDesig = new SubjectAttributeDesignatorType();
                oSubjMatch.setSubjectAttributeDesignator(oSubjAttrDesig);
                oSubjAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oSubjAttrDesig.setAttributeId(XACMLConstants.SUBJECT_PURPOSE_OF_USE);
            }   // User Role
        }   // if ((oCriterion.getUserRole() != null) &&

        // resources/Resource
        // Rsoources will contain the information for Confidentiality Code and
        // Document Type.
        //---------------------------------------------------------------------
        if (((oCriterion.getConfidentialityCode() != null) &&
             (oCriterion.getConfidentialityCode().getCode() != null) &&
             (oCriterion.getConfidentialityCode().getCode().length() > 0)) ||
            ((oCriterion.getDocumentTypeCode() != null) &&
             (oCriterion.getDocumentTypeCode().getCode() != null) &&
             (oCriterion.getDocumentTypeCode().getCode().length() > 0)))
        {
            ResourcesType oResources = new ResourcesType();
            oRuleTarget.setResources(oResources);
            List<ResourceType> olResource = oResources.getResource();
            ResourceType oRuleResource = new ResourceType();
            olResource.add(oRuleResource);

            // Confidentiality Code
            //---------------------
            if ((oCriterion.getConfidentialityCode() != null) &&
                 (oCriterion.getConfidentialityCode().getCode() != null) &&
                 (oCriterion.getConfidentialityCode().getCode().length() > 0))
            {
                // Resource match
                //----------------
                ResourceMatchType oResMatch = new ResourceMatchType();
                oRuleResource.getResourceMatch().add(oResMatch);
                oResMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

                // Attribute Value
                //----------------
                AttributeValueType oAttributeValue = new AttributeValueType();
                oResMatch.setAttributeValue(oAttributeValue);
                oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oAttributeValue.getContent().add(oCriterion.getConfidentialityCode().getCode());

                // Resource Attribute Designator
                //-------------------------------
                AttributeDesignatorType oResAttrDesig = new AttributeDesignatorType();
                oResMatch.setResourceAttributeDesignator(oResAttrDesig);
                oResAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oResAttrDesig.setAttributeId(XACMLConstants.RESOURCE_CONFIDENTIALITY_CODE);
            }

            // Confidentiality Code
            //---------------------
            if ((oCriterion.getDocumentTypeCode() != null) &&
                 (oCriterion.getDocumentTypeCode().getCode() != null) &&
                 (oCriterion.getDocumentTypeCode().getCode().length() > 0))
            {
                // Resource match
                //----------------
                ResourceMatchType oResMatch = new ResourceMatchType();
                oRuleResource.getResourceMatch().add(oResMatch);
                oResMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

                // Attribute Value
                //----------------
                AttributeValueType oAttributeValue = new AttributeValueType();
                oResMatch.setAttributeValue(oAttributeValue);
                oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oAttributeValue.getContent().add(oCriterion.getDocumentTypeCode().getCode());

                // Resource Attribute Designator
                //-------------------------------
                AttributeDesignatorType oResAttrDesig = new AttributeDesignatorType();
                oResMatch.setResourceAttributeDesignator(oResAttrDesig);
                oResAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_STRING);
                oResAttrDesig.setAttributeId(XACMLConstants.RESOURCE_SNOMEDCT_TYPE);
            }
        }

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
    private PolicyType createConsentXACMLDoc(PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        PolicyType oPolicy = new PolicyType();

        // Not sure what this ID is used for at this point...   Not sure if we have to track it
        // or tie it to the document ID in some fashion....  Now we will just generate it...
        //--------------------------------------------------------------------------------------
        UUID oPolicyId = UUID.randomUUID();
        String sPolicyId = oPolicyId.toString();
        oPolicy.setPolicyId(sPolicyId);

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
        // Set the action to be specific to Retrieve Documents Service
        //-------------------------------------------------------------
        ActionsType oActions = new ActionsType();
        oTarget.setActions(oActions);
        ActionType oAction = new ActionType();
        oActions.getAction().add(oAction);
        ActionMatchType oActionMatch = new ActionMatchType();
        oAction.getActionMatch().add(oActionMatch);
        oActionMatch.setMatchId(XACMLConstants.MATCH_ID_STRING_EQUAL);

        // Action - AttributeValue
        //-------------------------
        AttributeValueType oAttributeValue = new AttributeValueType();
        oActionMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI);
        oAttributeValue.getContent().add(XACMLConstants.ATTRIBUTE_VALUE_RETRIEVE_DOCUMENT);

        // Action - Attribute Designator
        //-------------------------------
        AttributeDesignatorType oActionAttrDesig = new AttributeDesignatorType();
        oActionMatch.setActionAttributeDesignator(oActionAttrDesig);
        oActionAttrDesig.setAttributeId(XACMLConstants.ACTION_ID);
        oActionAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_ANYURI);

        // Set up environment to pertain to the correct home community.
        // It appears that the NHIN spec has put the patient ID in as
        // a subject in the environment rather than as a resouce.  I am
        // matching the spec, but I think the spec is wrong.
        //-------------------------------------------------------------
        EnvironmentsType oEnvironments = new EnvironmentsType();
        oTarget.setEnvironments(oEnvironments);
        List<EnvironmentType> olEnvironment = oEnvironments.getEnvironment();
        EnvironmentType oEnvironment = new EnvironmentType();
        olEnvironment.add(oEnvironment);
        EnvironmentMatchType oEnvMatch = new EnvironmentMatchType();
        oEnvironment.getEnvironmentMatch().add(oEnvMatch);
        oEnvMatch.setMatchId(XACMLConstants.MATCH_ID_IID_EQUAL);

        // Environment - AttributeValue
        //------------------------------
        oAttributeValue = new AttributeValueType();
        oEnvMatch.setAttributeValue(oAttributeValue);
        oAttributeValue.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_IID);
        String sNHINPatientId = createNhinPatientId(oPtPref);
        oAttributeValue.getContent().add(sNHINPatientId);

        // Environment - Environment Designator
        //-------------------------------------
        AttributeDesignatorType oEnvAttrDesig = new AttributeDesignatorType();
        oEnvMatch.setEnvironmentAttributeDesignator(oEnvAttrDesig);
        oEnvAttrDesig.setAttributeId(XACMLConstants.PATIENT_SUBJECT_ID);
        oEnvAttrDesig.setDataType(XACMLConstants.ATTRIBUTE_VALUE_TYPE_IID);

        // Rules...
        //---------
        int iRuleIdx = 1;           // Index for each rule number.

        // Add in a rule for the Opt-in/Opt-Out setting
        //----------------------------------------------
        List<Object> olRules = oPolicy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();
        RuleType oRule = createOptInOutRule(iRuleIdx++, oPtPref);
        if (oRule != null)
        {
            olRules.add(oRule);
        }

        // Add in rule for each of the Fine Grained Policy Settings
        //----------------------------------------------------------
        if ((oPtPref.getFineGrainedPolicyCriteria() != null) &&
            (oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion() != null) &&
            (oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion().size() > 0))
        {
            List<FineGrainedPolicyCriterionType> olCriterion = oPtPref.getFineGrainedPolicyCriteria().getFineGrainedPolicyCriterion();
            for (FineGrainedPolicyCriterionType oCriterion : olCriterion)
            {
                oRule = createFineGrainedRule(iRuleIdx++, oCriterion);
                if (oRule != null)
                {
                    olRules.add(oRule);
                }
            }
        }
        
        return oPolicy;
    }


    /**
     * This method fills in the structured portion of the CDA document.  It was used in CONNECT
     * Release 2.1
     *
     * @param oDOMDocument The DOM Document to create elements to.
     * @param bOptIn TRUE if the user has opted in.
     * @return The structured body node to be placed in the CDA document.
     */
    private POCDMT000040StructuredBody fillInCDAStructuredBody(Document oDOMDocument, boolean bOptIn)
    {
        // Structured Body
        //----------------
        POCDMT000040StructuredBody oStructuredBody = new POCDMT000040StructuredBody();

        // Structured Body Component
        //---------------------------
        POCDMT000040Component3 oSBComponent = new POCDMT000040Component3();
        oStructuredBody.getComponent().add(oSBComponent);

        // Section
        //---------
        POCDMT000040Section oSection = new POCDMT000040Section();
        oSBComponent.setSection(oSection);

        // Section Code
        //-------------
        CE oCode = new CE();
        oSection.setCode(oCode);
        if (bOptIn)
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_YES);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_NO);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
        oCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);

        // Section Title
        //---------------
        Element oSectionTitle = null;
        oSectionTitle = oDOMDocument.createElement(CDAConstants.TITLE_TAG);
        if (bOptIn)
        {
            oSectionTitle.setTextContent(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oSectionTitle.setTextContent(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oSection.setTitle(oSectionTitle);

        // Section Text
        //-------------
        Element oSectionText = oDOMDocument.createElement(CDAConstants.TEXT_TAG);
        if (bOptIn)
        {
            oSectionText.setTextContent(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oSectionText.setTextContent(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oSection.setText(oSectionText);

        // Section Entry
        //--------------
        POCDMT000040Entry oSectionEntry = new POCDMT000040Entry();
        oSection.getEntry().add(oSectionEntry);

        // Entry Act
        //-----------
        POCDMT000040Act oEntryAct = new POCDMT000040Act();
        oSectionEntry.setAct(oEntryAct);

        // Entry Act Class & Mood Code
        //----------------------------
        oEntryAct.setClassCode(XActClassDocumentEntryAct.ACT);
        oEntryAct.setMoodCode(XDocumentActMood.EVN);

        // Entry Act Code
        //----------------
        oCode = new CE();
        oEntryAct.setCode(oCode);
        if (bOptIn)
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_YES);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_NO);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
        oCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);

        return oStructuredBody;
    }


    /**
     * This class takes the patient preference information and creates the
     * Clinical Document that represents that information.
     * 
     * @param oPtPref  The patient prefernces.
     * @param sConsentXACML The XACML string that should be placed in the CDA document
     * @return The clinical document that represents the patient preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This
     *         exception is thrown if any errors occur.
     */
    private POCDMT000040ClinicalDocument createConsentCDADoc(PatientPreferencesType oPtPref, String sConsentXACML)
        throws AdapterPIPException
    {
        DocumentBuilderFactory oDOMFactory = null;
        DocumentBuilder oDOMBuilder = null;
        Document oDOMDocument = null;
        Date dtNow = new Date();
        SimpleDateFormat oFormat = new SimpleDateFormat("yyyyMMddmmssSSZ");
        String sNow = oFormat.format(dtNow);
        boolean bOptIn = false;

        if ((oPtPref != null) &&
            (oPtPref.isOptIn()))
        {
            bOptIn = true;
        }

        try
        {
            oDOMFactory = DocumentBuilderFactory.newInstance();
            oDOMBuilder = oDOMFactory.newDocumentBuilder();
            oDOMDocument = oDOMBuilder.newDocument();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to create DOM factory and builder objects.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        POCDMT000040ClinicalDocument oPrefCDA = new POCDMT000040ClinicalDocument();
        oPrefCDA.setClassCode(ActClassClinicalDocument.DOCCLIN);

        // Type ID
        POCDMT000040InfrastructureRootTypeId oTypeId = new POCDMT000040InfrastructureRootTypeId();
        oPrefCDA.setTypeId(oTypeId);
        oTypeId.setExtension(CDAConstants.TYPE_ID_EXTENSION_POCD_HD000040);
        oTypeId.setRoot(CDAConstants.TYPE_ID_ROOT);

        // Template
        List<II> oaTemplateId = oPrefCDA.getTemplateId();
        II oTemplate = new II();
        oaTemplateId.add(oTemplate);
        oTemplate.setRoot(CDAConstants.TEMPLATE_ID_ROOT_MEDICAL_DOCUMENTS);
        oTemplate = new II();
        oaTemplateId.add(oTemplate);
        oTemplate.setRoot(CDAConstants.TEMPLATE_ID_ROOT_CONSENT_TO_SHARE);

        // Id
        //----
        II oId = new II();
        oPrefCDA.setId(oId);
        oId.setRoot("");
        oId.setExtension("");

        // Code
        //-----
        CE oCode = new CE();
        oPrefCDA.setCode(oCode);
        oCode.setCode("");
        oCode.setDisplayName("");
        oCode.setCodeSystem(CDAConstants.CODE_SYSTEM_LOINC_OID);
        oCode.setCodeSystemName(CDAConstants.CODE_SYSTEM_NAME_LOINC);

        // Title
        //------
        Element oTitle = oDOMDocument.createElement(CDAConstants.TITLE_TAG);
        oPrefCDA.setTitle(oTitle);
        oTitle.setTextContent(CDAConstants.TITLE);

        // EffectiveTime
        //---------------
        TS oEffectiveTime = new TS();
        oPrefCDA.setEffectiveTime(oEffectiveTime);
        oEffectiveTime.setValue(sNow);

        // Confidentiality code
        //----------------------
        oCode = new CE();
        oPrefCDA.setConfidentialityCode(oCode);
        oCode.setCode(CDAConstants.CONFIDENTIALITY_CODE_NORMAL);
        oCode.setDisplayName(CDAConstants.CONFIDENTIALITY_CODE_NORMAL_DISPLAY_NAME);
        oCode.setCodeSystem(CDAConstants.CONFIDENTIALITY_CODE_SYSTEM);
        oCode.setCodeSystemName(CDAConstants.CONFIDENTIALITY_CODE_SYSTEM_DISPLAY_NAME);

        // Language Code
        //---------------
        CS oLanguageCode = new CS();
        oPrefCDA.setLanguageCode(oLanguageCode);
        oLanguageCode.setCode(CDAConstants.LANGUAGE_CODE_ENGLISH);

        // Documentation Of
        //------------------
        List<POCDMT000040DocumentationOf> oaDocumentationOf = oPrefCDA.getDocumentationOf();
        POCDMT000040DocumentationOf oDocumenationOf = new POCDMT000040DocumentationOf();
        oaDocumentationOf.add(oDocumenationOf);
        oDocumenationOf.getTypeCode().add(CDAConstants.DOCUMENTATION_OF_TYPE_CODE);

        // Service Event
        //--------------
        POCDMT000040ServiceEvent oServiceEvent = new POCDMT000040ServiceEvent();
        oDocumenationOf.setServiceEvent(oServiceEvent);
        oServiceEvent.getClassCode().add(CDAConstants.SERVICE_EVENT_CLASS_CODE_ACT);
        oServiceEvent.getMoodCode().add(CDAConstants.SERVICE_EVENT_MOOD_CODE_EVENT);

        // Service Event - Template
        //--------------------------
        II oServiceEventTemplate = new II();
        oServiceEvent.getTemplateId().add(oServiceEventTemplate);
        oServiceEventTemplate.setRoot(CDAConstants.SERVICE_EVENT_TEMPLATE_ID_ROOT);

        // Id
        //----
        oId = new II();
        oServiceEvent.getId().add(oId);
        oId.setRoot("");

        // Code
        //------
        oCode = new CE();
        oServiceEvent.setCode(oCode);
        if (bOptIn)
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_YES);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oCode.setCode(CDAConstants.CONSENT_CODE_NO);
            oCode.setDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
        oCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);

        // Effective Time
        //----------------
        IVLTSExplicit oSEEffectiveTime = new IVLTSExplicit();
        oServiceEvent.setEffectiveTime(oSEEffectiveTime);
        IVXBTSExplicit oLowTime = new IVXBTSExplicit();
        oLowTime.setValue(sNow);
        org.hl7.v3.ObjectFactory oHL7ObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<IVXBTSExplicit> oJAXBLowTime = oHL7ObjectFactory.createIVLTSExplicitLow(oLowTime);
        oSEEffectiveTime.getContent().add(oJAXBLowTime);

        // Component
        //-----------
        POCDMT000040Component2 oMainComponent = new POCDMT000040Component2();
        oPrefCDA.setComponent(oMainComponent);

        // This is the old way (CONNECT 2.1) way of doing this.  We will comment it out for now.  We may
        // end up coming back to this....
        //-----------------------------------------------------------------------------------------------
        //POCDMT000040StructuredBody oStructuredBody = fillInCDAStructuredBody(oDOMDocument, bOptIn);
        //oMainComponent.setStructuredBody(oStructuredBody);

        // New way - OONNECT 2.2 and higher...
        //-------------------------------------
        POCDMT000040NonXMLBody oNonXMLBody = new POCDMT000040NonXMLBody();
        oMainComponent.setNonXMLBody(oNonXMLBody);

        Element oElementXACMLConsent = null;
        oElementXACMLConsent = oDOMDocument.createElement(CDAConstants.TEXT_TAG);
        oElementXACMLConsent.setTextContent(StringUtil.wrapCdata(sConsentXACML));
        oNonXMLBody.setText(oElementXACMLConsent);

        return oPrefCDA;
    }

    /**
     * This method takes in an object representation of the clinical document
     * and serializes it to a text string representation of the document.
     *
     * @param oPrefCDA The object representation of the clinical document.
     * @return The textual string representation of the clinical document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    private String serializeConsentCDADoc(POCDMT000040ClinicalDocument oPrefCDA)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextHL7 == null)
            {
                oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            }

            if (oHL7Marshaller == null)
            {
                oHL7Marshaller = oJaxbContextHL7.createMarshaller();
            }

            StringWriter swXML = new StringWriter();

            org.hl7.v3.ObjectFactory oHL7ObjectFactory = new org.hl7.v3.ObjectFactory();
            JAXBElement oJaxbElement = oHL7ObjectFactory.createClinicalDocument(oPrefCDA);

            oHL7Marshaller.marshal(oJaxbElement, swXML);
            sPrefCDA = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the CDA document to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sPrefCDA;
    }

    /**
     * This method takes in an object representation of the XACML Policy
     * and serializes it to a text string representation of the document.
     *
     * @param oConsentXACML The object representation of the XACML Consent Policy.
     * @return The textual string representation of the XACML Consent document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    private String serializeConsentXACMLDoc(PolicyType oConsentXACML)
        throws AdapterPIPException
    {
        String sConsentXACML = "";

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextXACML == null)
            {
                oJaxbContextXACML = JAXBContext.newInstance("oasis.names.tc.xacml._2_0.policy.schema.os");
            }

            if (oXACMLMarshaller == null)
            {
                oXACMLMarshaller = oJaxbContextXACML.createMarshaller();
            }

            StringWriter swXML = new StringWriter();

            oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory oXACMLObjectFactory = new oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory();
            JAXBElement oJaxbElement = oXACMLObjectFactory.createPolicy(oConsentXACML);

            oXACMLMarshaller.marshal(oJaxbElement, swXML);
            sConsentXACML = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the XACML document to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sConsentXACML;
    }

    /**
     * This method takes a string version of the Patient Pref document and
     * creates the JAXB object version of the same document.
     *
     * @param sPrefCDA The string version of the patient preference CDA document.
     * @return The JAXB object version of the patient preferences CDA document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    private POCDMT000040ClinicalDocument deserializeConsentCDADoc(String sPrefCDA)
        throws AdapterPIPException
    {
        POCDMT000040ClinicalDocument oPrefCDA = null;

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextHL7 == null)
            {
                oJaxbContextHL7 = JAXBContext.newInstance("org.hl7.v3");
            }

            if (oHL7Unmarshaller == null)
            {
                oHL7Unmarshaller = oJaxbContextHL7.createUnmarshaller();
            }

            StringReader srXML = new StringReader(sPrefCDA);

            JAXBElement oJAXBElementPrefCDA = (JAXBElement) oHL7Unmarshaller.unmarshal(srXML);
            if (oJAXBElementPrefCDA.getValue() instanceof POCDMT000040ClinicalDocument)
            {
                oPrefCDA = (POCDMT000040ClinicalDocument) oJAXBElementPrefCDA.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the CDA string: " + sPrefCDA + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oPrefCDA;
    }

    /**
     * This method takes a string version of the Patient Pref document and
     * creates the JAXB object version of the same document.
     *
     * @param sConsentXACML The string version of the patient preference XACML document.
     * @return The JAXB object version of the patient preferences XACML document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    private PolicyType deserializeConsentXACMLDoc(String sConsentXACML)
        throws AdapterPIPException
    {
        PolicyType oConsentXACML = null;

        try
        {
            // If the JAXBContext or Marshaller was not created - try to create it now.
            //-------------------------------------------------------------------------
            if (oJaxbContextXACML == null)
            {
                oJaxbContextXACML = JAXBContext.newInstance("oasis.names.tc.xacml._2_0.policy.schema.os");
            }

            if (oXACMLUnmarshaller == null)
            {
                oXACMLUnmarshaller = oJaxbContextXACML.createUnmarshaller();
            }

            StringReader srXML = new StringReader(sConsentXACML);

            JAXBElement oJAXBElementConsentXACML = (JAXBElement) oXACMLUnmarshaller.unmarshal(srXML);
            if (oJAXBElementConsentXACML.getValue() instanceof PolicyType)
            {
                oConsentXACML = (PolicyType) oJAXBElementConsentXACML.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the XACML consent string: " + sConsentXACML + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oConsentXACML;
    }


    /** This method creates the AdhocQueryRequest that is used to retreive
     *  the meta data about the CPP document in the repository.
     *
     * @param sPatientId The patient ID.
     * @param sAssigningAuthority The assigning authority
     * @return The AdhocQueryRequest used to retrieve the document meta data.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if there is an error.
     */
    private AdhocQueryRequest createAdhocQueryRequest(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        AdhocQueryRequest oRequest = new AdhocQueryRequest();
        String sHL7PatId = "";
        // ResponseOption
        //----------------
        ResponseOptionType oResponseOption = new ResponseOptionType();
        oRequest.setResponseOption(oResponseOption);
        oResponseOption.setReturnComposedObjects(true);
        oResponseOption.setReturnType(CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS);

        AdhocQueryType oAQ = new AdhocQueryType();
        oRequest.setAdhocQuery(oAQ);
        oAQ.setId(CDAConstants.ADHOC_QUERY_REQUEST_BY_PATIENT_ID_UUID);

        List<SlotType1> olSlot = oAQ.getSlot();

        // Patient ID
        //------------
        SlotType1 oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_CPP_PATIENT_ID);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        if(sPatientId != null &&
                !sPatientId.contains("&ISO"))
        {
            sHL7PatId = PatientIdFormatUtil.hl7EncodePatientId(sPatientId, sAssigningAuthority);
        }
        else
        {
            if(sPatientId != null &&
                    !sPatientId.equals("") &&
                    !sPatientId.startsWith("'"))
            {
                sPatientId = "'"+sPatientId+"'";
            }
            sHL7PatId = sPatientId;
        }
        oValueList.getValue().add(sHL7PatId);

        // Document Class Code
        //--------------------
        oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_DOCUMENT_CLASS_CODE);
        oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.ADHOC_QUERY_CLASS_CODE);

        // Status
        //-------
        oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);
        oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.STATUS_APPROVED_QUERY_VALUE);

        return oRequest;
    }

    /**
     * This method will create the DocumentRequest (Document IDs) from the
     * AdhocQueryResponse object.
     *
     * @param oResponse The AdhocQueryResponse that contains the document ids.
     * @return The DocumentRequest containing the document ids
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         Any error in the process of conversion.
     */
    private DocumentRequest createDocumentRequest(AdhocQueryResponse oResponse)
        throws AdapterPIPException
    {
        DocumentRequest oDocRequest = new DocumentRequest();

        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentId = "";
        String sHL7PatientId = "";

        if ((oResponse != null) &&
            (oResponse.getRegistryObjectList() != null) &&
            (oResponse.getRegistryObjectList().getIdentifiable() != null) &&
            (oResponse.getRegistryObjectList().getIdentifiable().size() > 0))
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList().getIdentifiable();

            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();

                    // Home Community ID
                    //-------------------
                    if ((oExtObj != null) &&
                        (oExtObj.getHome() != null) &&
                        (oExtObj.getHome().length() > 0))
                    {
                        sHomeCommunityId = oExtObj.getHome().trim();
                    } else if ((oExtObj.getSlot() != null) &&
                        (oExtObj.getSlot().size() > 0))
                    {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot)
                        {
                            if ((oSlot.getName() != null) &&
                                (oSlot.getName().equals(CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID)) &&
                                (oSlot.getValueList() != null) &&
                                (oSlot.getValueList().getValue() != null) &&
                                (oSlot.getValueList().getValue().size() > 0) &&
                                (oSlot.getValueList().getValue().get(0).length() > 0))
                            {
                                sHL7PatientId = oSlot.getValueList().getValue().get(0).trim();
                                sHomeCommunityId = PatientIdFormatUtil.parseCommunityId(sHL7PatientId);
                            }
                        }   // for (SlotType1 oSlot : olSlot)
                    }   // if ((oExtObj.getSlot() != null) && ...

                    // Repository ID
                    //---------------
                    if ((oExtObj.getSlot() != null) &&
                        (oExtObj.getSlot().size() > 0))
                    {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot)
                        {
                            if ((oSlot.getName() != null) &&
                                (oSlot.getName().equals(CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID)) &&
                                (oSlot.getValueList() != null) &&
                                (oSlot.getValueList().getValue() != null) &&
                                (oSlot.getValueList().getValue().size() > 0) &&
                                (oSlot.getValueList().getValue().get(0).length() > 0))
                            {
                                sRepositoryId = oSlot.getValueList().getValue().get(0).trim();
                            }
                        }   // for (SlotType1 oSlot : olSlot)
                    }   // if ((oExtObj.getSlot() != null) && ...

                    // Document Unique ID
                    //-------------------
                    if ((oExtObj.getExternalIdentifier() != null) &&
                        (oExtObj.getExternalIdentifier().size() > 0))
                    {
                        List<ExternalIdentifierType> olExtId = oExtObj.getExternalIdentifier();
                        for (ExternalIdentifierType oExtId : olExtId)
                        {
                            if ((oExtId.getIdentificationScheme() != null) &&
                                (oExtId.getIdentificationScheme().equals(CDAConstants.DOCUMENT_ID_IDENT_SCHEME)) &&
                                (oExtId.getValue() != null) &&
                                (oExtId.getValue().length() > 0))
                            {
                                sDocumentId = oExtId.getValue().trim();
                            }
                        }   // for (ExternalIdentifierType oExtid : olExtId)
                    }   // if ((oExtObj.getExternalIdentifier() != null) &&
                }   // if ((oJAXBObj != null) &&
            }   // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)

            // Now lets create the response...
            //---------------------------------
            oDocRequest.setHomeCommunityId(sHomeCommunityId);
            oDocRequest.setRepositoryUniqueId(sRepositoryId);
            oDocRequest.setDocumentUniqueId(sDocumentId);
        }

        return oDocRequest;
    }

    /**
     * This operation returns the Patient CPP document in XML form that it
     * extracts from the Response.
     *
     * @param oResponse The response that was recieved from the repository.
     * @return The XML form of the patient CPP.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there are any errors.
     */
    private String extractPrefCDA(RetrieveDocumentSetResponseType oResponse)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        if ((oResponse != null) &&
            (oResponse.getDocumentResponse() != null) &&
            (oResponse.getDocumentResponse().size() > 0))
        {
            // A patient should only have one document - so if there are more than one, we
            // will use the first one...
            //-----------------------------------------------------------------------------
            DocumentResponse oDocResponse = oResponse.getDocumentResponse().get(0);
            if ((oDocResponse.getDocument() != null) &&
                (oDocResponse.getDocument().length > 0))
            {
                sPrefCDA = new String(oDocResponse.getDocument());
            }
        }   // if ((oResponse != null) &&

        return sPrefCDA;
    }

    /**
     * This method retrieves the document identifier information from the
     * repository for the Consumer Preferences document for this patient.
     *
     * @param sPatientId The patient ID of the patient.
     * @param sAssigningAuthority The assigning authority
     * @return The document identifiers for the CPP document
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if any problem occurs getting the data.
     */
    public DocumentRequest retrieveCPPDocIdentifiers(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        DocumentRequest oDocRequest = new DocumentRequest();

        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = createAdhocQueryRequest(sPatientId, sAssigningAuthority);

        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);

        oDocRequest = createDocumentRequest(oResponse);
        
        return oDocRequest;
    }

    /**
     * This method takes the document identifiers for the CPP document and
     * retrieves the document from the repository.
     *
     * @param oDocRequest The document identifiers.
     * @return The XML CPP document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there are any errors.
     */
    private String retrieveCPPDoc(DocumentRequest oDocRequest)
        throws AdapterPIPException
    {
        String sPrefCDA = "";

        DocumentRepositoryPortType oDocRepositoryPort = getDocumentRepositoryPort();

        RetrieveDocumentSetRequestType oRequest = new RetrieveDocumentSetRequestType();
        oRequest.getDocumentRequest().add(oDocRequest);

        RetrieveDocumentSetResponseType oResponse = null;
        oResponse = oDocRepositoryPort.documentRepositoryRetrieveDocumentSet(oRequest);

        sPrefCDA = extractPrefCDA(oResponse);

        return sPrefCDA;

    }

    /**
     * This operation retrieves the currently stored patient consent
     * information from the repository.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The Assigning authority
     * @return The document information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         The error if one occurs.
     */
    private CPPDocumentInfo retrieveCPPFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        CPPDocumentInfo oCPPDocInfo = new CPPDocumentInfo();


        DocumentRequest oDocRequest = retrieveCPPDocIdentifiers(sPatientId, sAssigningAuthority);

        if (oDocRequest != null)
        {
            if (oDocRequest.getHomeCommunityId() != null)
            {
                oCPPDocInfo.sHomeCommunityId = oDocRequest.getHomeCommunityId();
            }

            if (oDocRequest.getRepositoryUniqueId() != null)
            {
                oCPPDocInfo.sRepositoryId = oDocRequest.getRepositoryUniqueId();
            }

            if (oDocRequest.getDocumentUniqueId() != null)
            {
                oCPPDocInfo.sDocumentUniqueId = oDocRequest.getDocumentUniqueId();
            }

            String sPrefCDA = retrieveCPPDoc(oDocRequest);

            if ((sPrefCDA != null) &&
                (sPrefCDA.length() > 0))
            {
                oCPPDocInfo.sPrefCDA = sPrefCDA;

                POCDMT000040ClinicalDocument oPrefCDA = deserializeConsentCDADoc(sPrefCDA);
                oCPPDocInfo.oPrefCDA = oPrefCDA;
            }
        }

        return oCPPDocInfo;
    }

    
    /**
     * This operation retrieves the currently stored patient consent
     * information from the repository.
     *
     * @param sPatientId The ID of the patient.
     * @return The document information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         The error if one occurs.
     */
    private CPPDocumentInfo retrieveCPPFromRepositoryUsingDocumentService(String sPatientId)
        throws AdapterPIPException
    {
        CPPDocumentInfo oCPPDocInfo = new CPPDocumentInfo();

        gov.hhs.fha.nhinc.repository.model.Document oDoc = new gov.hhs.fha.nhinc.repository.model.Document();
        DocumentService oDocService = new DocumentService();
        DocumentQueryParams oParams = new DocumentQueryParams();

        
        LinkedList<String> oaClassCode = new LinkedList<String>();
        List<gov.hhs.fha.nhinc.repository.model.Document> oDocs;

        oaClassCode.add(CDAConstants.METADATA_CLASS_CODE);

        oParams.setClassCodes(oaClassCode);
        oParams.setPatientId(sPatientId);

        oDocs = oDocService.documentQuery(oParams);

        // We should only have one document.  If we found more than one, throw
        // an exception.
        //---------------------------------------------------------------------
        if (oDocs.size() == 1)
        {
            oDoc = oDocs.get(0);

            log.debug("Retrieved patient preferences for patient: " + sPatientId +
                      "Document: " + new String(oDoc.getRawData()));
        }
        else if (oDocs.size() > 1)
        {
            String sErrorMessage = "Found more than one Consent document in the repository.  " +
                                   "There should have been only 1.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        if (oDoc != null)
        {
            if (oDoc.getDocumentid() != null)
            {
                oCPPDocInfo.lDocumentId = oDoc.getDocumentid();
            }

            if (oDoc.getDocumentUniqueId() != null)
            {
                oCPPDocInfo.sDocumentUniqueId = oDoc.getDocumentUniqueId();
            }

            if ((oDoc.getRawData() != null) &&
                (oDoc.getRawData().length > 0))
            {
                oCPPDocInfo.sPrefCDA = new String(oDoc.getRawData());

                oCPPDocInfo.oPrefCDA = deserializeConsentCDADoc(oCPPDocInfo.sPrefCDA);
            }
        }

        return oCPPDocInfo;

    }

     /**
     * This method stores the patient preference CDA document to the repository.
     *
     * @param oPtPref The patient preference information.
     * @param sPrefCDA The CDA document form of the patient preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if there is an error.
     */
    private void storeCPPToRepositoryUsingXDSb(PatientPreferencesType oPtPref, String sPrefCDA)
        throws AdapterPIPException, PropertyAccessException
    {
        log.info("------ Begin PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
        if ((oPtPref == null) ||
            (oPtPref.getPatientId() == null) ||
            (oPtPref.getPatientId().trim().length() <= 0))
        {
            String sErrorMessage = "failed to store patient consent.  The patient ID was null or blank.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }
        
        saveCPPDoc(sPrefCDA, oPtPref);
        log.info("------ End PatientConsentManager.storeCPPToRepositoryUsingXDSb() ------");
    }

    /**
     * This method stores the patient preference CDA document to the repository.
     *
     * @param oPtPref The patient preference information.
     * @param sPrefCDA The CDA document form of the patient preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if there is an error.
     */
    private void storeCPPToRepository(PatientPreferencesType oPtPref, String sPrefCDA)
        throws AdapterPIPException
    {
        // Note that right now we are storing using the Document repository API
        // As soon as the XDS.b document storage operations are completed,
        // this code should be changed to call that web service.
        //----------------------------------------------------------------------
        if ((oPtPref == null) ||
            (oPtPref.getPatientId() == null) ||
            (oPtPref.getPatientId().trim().length() <= 0))
        {
            String sErrorMessage = "failed to store patient consent.  The patient ID was null or blank.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        CPPDocumentInfo oCPPDocInfo = retrieveCPPFromRepositoryUsingDocumentService(oPtPref.getPatientId());

        gov.hhs.fha.nhinc.repository.model.Document oDoc = new gov.hhs.fha.nhinc.repository.model.Document();

        // Document ID - If it exists - then the previous one will be overwritten
        //-----------------------------------------------------------------------
        if ((oCPPDocInfo != null) &&
            (oCPPDocInfo.lDocumentId > 0))
        {
            oDoc.setDocumentid(oCPPDocInfo.lDocumentId);
        }

        // Class Code
        //-----------
        oDoc.setClassCode(CDAConstants.METADATA_CLASS_CODE);
        oDoc.setClassCodeDisplayName(CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);

        // Event Code
        //-----------
        HashSet<EventCode> oaEventCode = new HashSet<EventCode>();
        EventCode oEventCode = new EventCode();
        oaEventCode.add(oEventCode);
        oEventCode.setDocument(oDoc);       // This must be set for foreign key
        if (oPtPref.isOptIn())
        {
            oEventCode.setEventCode(CDAConstants.CONSENT_CODE_YES);
            oEventCode.setEventCodeDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        }
        else
        {
            oEventCode.setEventCode(CDAConstants.CONSENT_CODE_NO);
            oEventCode.setEventCodeDisplayName(CDAConstants.CONSENT_CODE_NO_DISPLAY_NAME);
        }
        oEventCode.setEventCodeScheme(CDAConstants.SNOMED_CT_CODE_SYSTEM);
        oDoc.setEventCodes(oaEventCode);

        // Format Code
        //-------------
        oDoc.setFormatCode(CDAConstants.METADATA_FORMAT_CODE);
        oDoc.setFormatCodeScheme(CDAConstants.METADATA_FORMAT_CODE_SYSTEM);

        // CDA Consent XML
        //-----------------
        oDoc.setRawData(sPrefCDA.getBytes());

        // Document Title
        //---------------
        oDoc.setDocumentTitle(CDAConstants.TITLE);

        // Patient ID
        //------------
        oDoc.setPatientId(oPtPref.getPatientId());

        // Unique ID - Note that this Unique ID would normally be set by
        // the repository (I assume).  If that is the case then we need to
        // allow it to create this..  Right now we will make one up...  (This
        // may be an issue with the XDS.b interface.
        //--------------------------------------------------------------------
        oDoc.setDocumentUniqueId(oPtPref.getPatientId()+'-'+CDAConstants.METADATA_CLASS_CODE);

        // Status Code
        //-------------
        oDoc.setStatus(CDAConstants.STATUS_APPROVED_STORE_VALUE);

        DocumentService oDocService = new DocumentService();
        oDocService.saveDocument(oDoc);

        log.debug("Stored CPP to repository for patient ID: " + oPtPref.getPatientId() +
                  "Document: " + sPrefCDA);

    }

    /**
     * This returns true if the given rule contains the specified attribute
     * designator.  False if it does not.
     *
     * @param oRule The rule being checked.
     * @param sAttrDesignator The attribute designator being checked for.
     * @return TRUE if the rule contains the attribute designator and false if it does not.
     */
    private boolean containsAttributeDesignatorXACML(RuleType oRule, String sAttrDesignator)
    {
        // Note that as soon as we find it, we will return out of this routine.  We will not
        // keep looking through the rest...
        //-----------------------------------------------------------------------------------

        // Check through the Resources first for this attribute...
        //----------------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getResources() != null) &&
            (oRule.getTarget().getResources().getResource() != null) &&
            (oRule.getTarget().getResources().getResource().size() > 0))
        {
            for (ResourceType oResource : oRule.getTarget().getResources().getResource())
            {
                if ((oResource != null) &&
                    (oResource.getResourceMatch() != null) &&
                    (oResource.getResourceMatch().size() > 0))
                {
                    for (ResourceMatchType oResourceMatch : oResource.getResourceMatch())
                    {
                        if ((oResourceMatch != null) &&
                            (oResourceMatch.getResourceAttributeDesignator() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId().equals(sAttrDesignator)))
                        {
                            return true;
                        }
                    }   // for (ResourceMatchType oResourceMatch : oResource.getResourceMatch())
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : oRule.getTarget().getResources().getResource())
        }   // if ((oRule != null) &&

        // It was not in the resource area - lets look for it in the subjects area.
        //-------------------------------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getSubjects() != null) &&
            (oRule.getTarget().getSubjects().getSubject() != null) &&
            (oRule.getTarget().getSubjects().getSubject().size() > 0))
        {
            for (SubjectType oSubject : oRule.getTarget().getSubjects().getSubject())
            {
                if ((oSubject != null) &&
                    (oSubject.getSubjectMatch() != null) &&
                    (oSubject.getSubjectMatch().size() > 0))
                {
                    for (SubjectMatchType oSubjectMatch : oSubject.getSubjectMatch())
                    {
                        if ((oSubjectMatch != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator() != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId() != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId().equals(sAttrDesignator)))
                        {
                            return true;
                        }
                    }   // for (ResourceMatchType oResourceMatch : oResource.getResourceMatch())
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : oRule.getTarget().getResources().getResource())
        }   // if ((oRule != null) &&

        return false;       // If we got here we never found it...
    }

    /**
     * This extracts the information for one instnce of a FineGrainedPolicyCriterion from the
     * Rule.
     *
     * @param oRule The rule being checked.
     * @return An instance of the FineGrainedPolicyCriterion that was creatd from the data in the rule.
     */
    private FineGrainedPolicyCriterionType extractSingleFineGrainedPolicyXACML(RuleType oRule)
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
            oCriterion.setPolicyOID(oRule.getRuleId());
        }

        // Extract any values from the Resource section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getResources() != null) &&
            (oRule.getTarget().getResources().getResource() != null) &&
            (oRule.getTarget().getResources().getResource().size() > 0))
        {
            for (ResourceType oResource : oRule.getTarget().getResources().getResource())
            {
                if ((oResource != null) &&
                    (oResource.getResourceMatch() != null) &&
                    (oResource.getResourceMatch().size() > 0))
                {
                    for (ResourceMatchType oResourceMatch : oResource.getResourceMatch())
                    {
                        // Is this the DocumentType field
                        //--------------------------------
                        if ((oResourceMatch != null) &&
                            (oResourceMatch.getResourceAttributeDesignator() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId() != null) &&
                            (oResourceMatch.getResourceAttributeDesignator().getAttributeId().equals(XACMLConstants.RESOURCE_SNOMEDCT_TYPE)) &&
                            (oResourceMatch.getAttributeValue() != null) &&
                            (oResourceMatch.getAttributeValue().getContent() != null) &&
                            (oResourceMatch.getAttributeValue().getContent().size() > 0) &&
                            (oResourceMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oResourceMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                bHasData = true;
                                CeType oCode = new CeType();
                                oCriterion.setDocumentTypeCode(oCode);
                                oCode.setCode(sValue);
                            }
                        }   // if ((oResourceMatch != null) &&
                        // Is this the Confidentiality Code field
                        //----------------------------------------
                        else if ((oResourceMatch != null) &&
                                 (oResourceMatch.getResourceAttributeDesignator() != null) &&
                                 (oResourceMatch.getResourceAttributeDesignator().getAttributeId() != null) &&
                                 (oResourceMatch.getResourceAttributeDesignator().getAttributeId().equals(XACMLConstants.RESOURCE_CONFIDENTIALITY_CODE)) &&
                                 (oResourceMatch.getAttributeValue() != null) &&
                                 (oResourceMatch.getAttributeValue().getContent() != null) &&
                                 (oResourceMatch.getAttributeValue().getContent().size() > 0) &&
                                 (oResourceMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oResourceMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                bHasData = true;
                                CeType oCode = new CeType();
                                oCriterion.setConfidentialityCode(oCode);
                                oCode.setCode(sValue);
                            }
                        }   // if ((oResourceMatch != null) &&

                    }   // for (ResourceMatchType oResourceMatch : oResource.getResourceMatch())
                }   // if ((oResource != null) &&
            }   // for (ResourceType oResource : oRule.getTarget().getResources().getResource())
        }   // if ((oRule != null) &&

        // Extract any values from the Subjects section...
        //------------------------------------------------
        if ((oRule != null) &&
            (oRule.getTarget() != null) &&
            (oRule.getTarget().getSubjects() != null) &&
            (oRule.getTarget().getSubjects().getSubject() != null) &&
            (oRule.getTarget().getSubjects().getSubject().size() > 0))
        {
            for (SubjectType oSubject : oRule.getTarget().getSubjects().getSubject())
            {
                if ((oSubject != null) &&
                    (oSubject.getSubjectMatch() != null) &&
                    (oSubject.getSubjectMatch().size() > 0))
                {
                    for (SubjectMatchType oSubjectMatch : oSubject.getSubjectMatch())
                    {
                        // Is this the Subject Role field
                        //--------------------------------
                        if ((oSubjectMatch != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator() != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId() != null) &&
                            (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId().equals(XACMLConstants.SUBJECT_ROLE)) &&
                            (oSubjectMatch.getAttributeValue() != null) &&
                            (oSubjectMatch.getAttributeValue().getContent() != null) &&
                            (oSubjectMatch.getAttributeValue().getContent().size() > 0) &&
                            (oSubjectMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oSubjectMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                bHasData = true;
                                CeType oCode = new CeType();
                                oCriterion.setUserRole(oCode);
                                oCode.setCode(sValue);
                            }
                        }   // if ((oSubjectMatch != null) &&
                        // Is this the Purpose of Use field
                        //----------------------------------------
                        else if ((oSubjectMatch != null) &&
                                 (oSubjectMatch.getSubjectAttributeDesignator() != null) &&
                                 (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId() != null) &&
                                 (oSubjectMatch.getSubjectAttributeDesignator().getAttributeId().equals(XACMLConstants.SUBJECT_PURPOSE_OF_USE)) &&
                                 (oSubjectMatch.getAttributeValue() != null) &&
                                 (oSubjectMatch.getAttributeValue().getContent() != null) &&
                                 (oSubjectMatch.getAttributeValue().getContent().size() > 0) &&
                                 (oSubjectMatch.getAttributeValue().getContent().get(0) instanceof String))
                        {
                            String sValue = (String) oSubjectMatch.getAttributeValue().getContent().get(0);
                            if ((sValue != null) &&
                                (sValue.length() > 0))
                            {
                                bHasData = true;
                                CeType oCode = new CeType();
                                oCriterion.setPurposeOfUse(oCode);
                                oCode.setCode(sValue);
                            }
                        }   // if ((oSubjectMatch != null) &&

                    }   // for (SubjectMatchType oSubjectMatch : oSubject.getSubjectMatch())
                }   // if ((oSubject != null) &&
            }   // for (SubjectType oSubject : oRule.getTarget().getSubjects().getSubject())
        }   // if ((oRule != null) &&

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
    {
        FineGrainedPolicyCriteriaType oFineGrainCriteria = new FineGrainedPolicyCriteriaType();

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

                    // Currently we are looking for only rules that contain the fine-grained policy information.
                    // These rules will contain one or more of the attributes in the following if statement.
                    //-------------------------------------------------------------------------------------------
                    if ((containsAttributeDesignatorXACML(oRule, XACMLConstants.SUBJECT_ROLE)) ||
                        (containsAttributeDesignatorXACML(oRule, XACMLConstants.SUBJECT_PURPOSE_OF_USE)) ||
                        (containsAttributeDesignatorXACML(oRule, XACMLConstants.RESOURCE_SNOMEDCT_TYPE)) ||
                        (containsAttributeDesignatorXACML(oRule, XACMLConstants.RESOURCE_CONFIDENTIALITY_CODE)))
                    {
                        FineGrainedPolicyCriterionType oCriterion = extractSingleFineGrainedPolicyXACML(oRule);
                        if (oCriterion != null)
                        {
                            oFineGrainCriteria.getFineGrainedPolicyCriterion().add(oCriterion);
                        }
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
     * This method takes the XML Patient preference CDA document and it populates
     * the PtPref with the information it finds in it.
     *
     * @param oDocInfo  The CPP document information.
     * @param oPtPref The Patient Prefernces object.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    private void populateConsentInfo(CPPDocumentInfo oDocInfo, PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        // if we do not have an XML string, then set it to opt out and get out of here.
        //-----------------------------------------------------------------------------
        if ((oDocInfo == null) ||
            (oDocInfo.oPrefCDA == null))
        {
            oPtPref.setOptIn(false);
            return;         // Get out of here...  Nothing more to do
        }

        POCDMT000040ClinicalDocument oPrefCDA = oDocInfo.oPrefCDA;

        if ((oPrefCDA != null) &&
            (oPrefCDA.getDocumentationOf() != null) &&
            (oPrefCDA.getDocumentationOf().size() > 0) &&
            (oPrefCDA.getDocumentationOf().get(0) != null) &&
            (oPrefCDA.getDocumentationOf().get(0).getServiceEvent() != null) &&
            (oPrefCDA.getDocumentationOf().get(0).getServiceEvent().getCode() != null) &&
            (oPrefCDA.getDocumentationOf().get(0).getServiceEvent().getCode().getCode() != null))
        {
            // If it is not yes - it is no for all other cases.
            //--------------------------------------------------
            if (oPrefCDA.getDocumentationOf().get(0).getServiceEvent().getCode().getCode().equals(CDAConstants.CONSENT_CODE_YES))
            {
                oPtPref.setOptIn(true);
            }
            else
            {
                oPtPref.setOptIn(false);
            }
        }

        // Check to see if we have any fiine grained policy information...
        //-----------------------------------------------------------------
        if ((oPrefCDA != null) &&
            (oPrefCDA.getComponent() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent() != null) &&
            (oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent().length() > 0))
        {
            String sConsentXACML = StringUtil.unwrapCdata(oPrefCDA.getComponent().getNonXMLBody().getText().getTextContent());

            if ((sConsentXACML != null) &&
                (sConsentXACML.trim().length() > 0))
            {
                PolicyType oConsentXACML = deserializeConsentXACMLDoc(sConsentXACML);
                if (oConsentXACML != null)
                {
                    FineGrainedPolicyCriteriaType oFineGrainCriteria = extractFineGrainedCriteriaFromXACML(oConsentXACML);
                    if (oFineGrainCriteria != null)
                    {
                        oPtPref.setFineGrainedPolicyCriteria(oFineGrainCriteria);
                    }
                }   // if (oConsentXACML != null)
            }   // if ((sConsentXACML != null) &&
        }   // if ((oPrefCDA != null) &&
    }

    /**
     * This method takes the parameters and creates an AdhocQueryRequest message
     * that can be used to retrieve the meta data about this document.
     *
     * @param sDocumentUniqueId The unique ID of the document.
     * @param sRepositoryId The repository ID where the document is stored.
     * @return The AdhocQueryRequest containing the search information.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *          This is thrown if there is an error.
     */
    private AdhocQueryRequest createPatientIdQuery(String sDocumentUniqueId, String sRepositoryId)
        throws AdapterPIPException
    {
        AdhocQueryRequest oRequest = new AdhocQueryRequest();

        // ResponseOption
        //----------------
        ResponseOptionType oResponseOption = new ResponseOptionType();
        oRequest.setResponseOption(oResponseOption);
        oResponseOption.setReturnComposedObjects(true);
        oResponseOption.setReturnType(CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS);

        AdhocQueryType oAQ = new AdhocQueryType();
        oRequest.setAdhocQuery(oAQ);
        oAQ.setId(CDAConstants.ADHOC_QUERY_REQUEST_BY_DOCUMENT_ID_UUID);

        List<SlotType1> olSlot = oAQ.getSlot();

        // Document ID
        //------------
        if (sDocumentUniqueId.length() > 0)
        {
            SlotType1 oSlot = new SlotType1();
            olSlot.add(oSlot);
            oSlot.setName(CDAConstants.SLOT_NAME_DOC_RETRIEVE_DOCUMENT_ID);
            ValueListType oValueList = new ValueListType();
            oSlot.setValueList(oValueList);
            String sDocId = "('"+sDocumentUniqueId+"')"; //Formatted for NIST XDS.b
            oValueList.getValue().add(sDocId);
        }

        // Repository ID
        //--------------
        if (sRepositoryId.length() > 0)
        {
            SlotType1 oSlot = new SlotType1();
            olSlot.add(oSlot);
            oSlot.setName(CDAConstants.SLOT_NAME_DOC_RETRIEVE_REPOSITORY_UNIQUE_ID);
            ValueListType oValueList = new ValueListType();
            oSlot.setValueList(oValueList);
            oValueList.getValue().add(sRepositoryId);
        }

        // Status
        //-------
        SlotType1 oSlot = new SlotType1();
        olSlot.add(oSlot);
        oSlot.setName(CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);
        ValueListType oValueList = new ValueListType();
        oSlot.setValueList(oValueList);
        oValueList.getValue().add(CDAConstants.STATUS_APPROVED_QUERY_VALUE);

        return oRequest;
    }

    /**
     * This method extracts the patient ID from the AdhocQueryResponse
     * message and returns it.
     *
     * @param oResponse The AdhocQueryResponse message from the repository.
     * @return The patient ID in the response message.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if any issues occur.
     */
    private String extractPatientId(AdhocQueryResponse oResponse)
        throws AdapterPIPException
    {
        String sPatientId = "";

        // Find the Patient ID in the response...
        //---------------------------------------
        if ((oResponse != null) &&
            (oResponse.getRegistryObjectList() != null) &&
            (oResponse.getRegistryObjectList().getIdentifiable() != null) &&
            (oResponse.getRegistryObjectList().getIdentifiable().size() > 0))
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList().getIdentifiable();

            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();

                    // Patient ID
                    //-----------
                    if ((oExtObj.getSlot() != null) &&
                        (oExtObj.getSlot().size() > 0))
                    {
                        List<SlotType1> olSlot = oExtObj.getSlot();
                        for (SlotType1 oSlot : olSlot)
                        {
                            if ((oSlot.getName() != null) &&
                                (oSlot.getName().equals(CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID)) &&
                                (oSlot.getValueList() != null) &&
                                (oSlot.getValueList().getValue() != null) &&
                                (oSlot.getValueList().getValue().size() > 0) &&
                                (oSlot.getValueList().getValue().get(0).length() > 0))
                            {
                                sPatientId = oSlot.getValueList().getValue().get(0).trim();
                                break;          // Get out of the loop - we found what we want.
                            }
                        }   // for (SlotType1 oSlot : olSlot)
                    }   // if ((oExtObj.getSlot() != null) && ...
                }   // if ((oJAXBObj != null) &&
            }   // for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
        }   // if ((oResponse != null) && ...

        return sPatientId;
    }

    /**
     * This method takes the given Document ID and does a query against the repository
     * to find out the patient ID for this patient.  It returns that patient ID.
     *
     * @param sDocumentUniqueId The document ID of an existing document in the repository.
     * @param sRepositoryId The repository ID of the repository where the document is stored.
     * @return The patient ID of the patient.
     * @throws AdapterPIPException - This is thrown if there is an error.
     */
    private String retrievePtIdFromDocumentId(String sDocumentUniqueId, String sRepositoryId)
        throws AdapterPIPException
    {
        String sPatientId = "";

        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = createPatientIdQuery(sDocumentUniqueId, sRepositoryId);

        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);

        sPatientId = extractPatientId(oResponse);

        return sPatientId;
    }

    /**
     * This method saves the patient consent information to the document repository.
     * It will overwrite anything that is currently there.
     *
     * @param oPtPref The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This
     *         exception is thrown if there is an error storing.
     */
    public void storePatientConsent(PatientPreferencesType oPtPref)
        throws AdapterPIPException, PropertyAccessException
    {
        POCDMT000040ClinicalDocument oPrefCDA = null;
        PolicyType oConsentXACML = null;

        oConsentXACML = createConsentXACMLDoc(oPtPref);
        String sConsentXACML = serializeConsentXACMLDoc(oConsentXACML);
        
        oPrefCDA = createConsentCDADoc(oPtPref, sConsentXACML);
        String sPrefCDA = serializeConsentCDADoc(oPrefCDA);

        //Uses Repository Services
        storeCPPToRepositoryUsingXDSb(oPtPref, sPrefCDA);
        //storeCPPToRepository(oPtPref, sPrefCDA);

    }

    /**
     * This method retrieves the patient consent information from the repository
     * based on patient Id.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The assigning authority associated with the patient ID.
     *                            Currently it is not really used, but here if it is needed.
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if there are any issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByPatientId(String sPatientId, String sAssigningAuthority)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sPatientId == null) ||
            (sPatientId.trim().length() <= 0))
        {
            String sErrorMessage = "Failed to retrieve patient consent.  The patient ID was either null or an empty string.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        oPtPref.setPatientId(sPatientId);
        oPtPref.setAssigningAuthority(sAssigningAuthority);
        oPtPref.setOptIn(false);        // Default is opt out
        
        CPPDocumentInfo oDocInfo = retrieveCPPFromRepositoryUsingXDSb(sPatientId, sAssigningAuthority);

        if (oDocInfo != null)
        {
            populateConsentInfo(oDocInfo, oPtPref);
        }

        return oPtPref;
    }

    /**
     * This method retrieves the patient consent information from the repository
     * based on a document Id.  It does this by first using the document ID to
     * retrieve the patient ID.  Then it uses the patient ID to retrieve the consent
     * information.
     *
     * @param sPatientId The ID of the patient.
     * @param sAssigningAuthority The assigning authority associated with the patient ID.
     *                            Currently it is not really used, but here if it is needed.
     * @return The patient consent preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This error is thrown if there are any issues retrieving the document.
     */
    public PatientPreferencesType retrievePatientConsentByDocId(String sHomeCommunityId,
                                                                String sRepositoryId,
                                                                String sDocumentUniqueId)
        throws AdapterPIPException
    {
        PatientPreferencesType oPtPref = new PatientPreferencesType();

        if ((sDocumentUniqueId == null) ||
            (sDocumentUniqueId.trim().length() <= 0))
        {
            String sErrorMessage = "Failed to retrieve patient consent.  The document unique ID was either null or an empty string.";
            log.error(sErrorMessage);
            throw new AdapterPIPException(sErrorMessage);
        }

        String sPatientId = retrievePtIdFromDocumentId(sDocumentUniqueId, sRepositoryId);

        if ((sPatientId != null) &&
            (sPatientId.length() >= 0))
        {
            oPtPref = retrievePatientConsentByPatientId(sPatientId, "");
        }

        return oPtPref;
    }


    /**
     * This class is an inner class used to hold the document along with its
     * set of identifiers.  For internal purposes.
     */
    private class CPPDocumentInfo
    {
        long lDocumentId = 0;           // note this is only used by our internal document repository service
        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentUniqueId = "";
        String sPrefCDA = "";
        POCDMT000040ClinicalDocument oPrefCDA = null;
    }

    /**
     * 
     * @param sPrefCDA
     * @param oPtPref
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    private void saveCPPDoc(String sPrefCDA, PatientPreferencesType oPtPref)
            throws AdapterPIPException, PropertyAccessException
    {
       log.info("------ Begin PatientConsentManager.saveCPPDoc ------");
       PatientConsentDocumentBuilderHelper oPatConsentDocBuilderHelper = new PatientConsentDocumentBuilderHelper();
       DocumentRepositoryPortType oDocRepositoryPort = getDocumentRepositoryPort();
       String sHomeCommunityId = PropertyAccessor.getProperty("gateway", "localHomeCommunityId");
       String sDocumentUniqueId = oPatConsentDocBuilderHelper.getOidFromProperty("documentuniqueid");
       ProvideAndRegisterDocumentSetRequestType oRequest = new ProvideAndRegisterDocumentSetRequestType();
       ProvideAndRegisterDocumentSetRequestType.Document oDoc =createDocumentRawData(sPrefCDA, sDocumentUniqueId);
       oRequest.getDocument().add(oDoc);
       String sTargetObject = checkCPPMetaFromRepositoryUsingXDSb(oPtPref.getPatientId(), oPtPref.getAssigningAuthority());
       SubmitObjectsRequest oSubmitObjectRequest = oPatConsentDocBuilderHelper.createSubmitObjectRequest(sTargetObject, sHomeCommunityId, sDocumentUniqueId, oPtPref);
       oRequest.setSubmitObjectsRequest(oSubmitObjectRequest);
       RegistryResponseType oRegistryResponse = oDocRepositoryPort.documentRepositoryProvideAndRegisterDocumentSetB(oRequest);
       if(oRegistryResponse != null &&
               oRegistryResponse.getStatus() != null &&
               !oRegistryResponse.getStatus().equals(""))
       {
           log.info("Patient Consent Document saved to repository Successfully");
       }
       else
       {
           throw new AdapterPIPException("Unable to Save patient Consent Document"+oRegistryResponse.getRegistryErrorList().getRegistryError().get(0).getValue());
       }
       log.info("------ End PatientConsentManager.saveCPPDoc ------");
    }

    /**
     * This method is used internal by saveCPPDoc creates Document with rawData and document unique id
     * @param sPrefCDA
     * @param sDocUniqueId
     * @return ProvideAndRegisterDocumentSetRequestType.Document
     */
    private ProvideAndRegisterDocumentSetRequestType.Document createDocumentRawData(String sPrefCDA, String sDocUniqueId)
    {
        log.info("------ Begin PatientConsentManager.createDocumentRawData ------");
        ProvideAndRegisterDocumentSetRequestType.Document oDoc = new ProvideAndRegisterDocumentSetRequestType.Document();
        oDoc.setId(sDocUniqueId);
        oDoc.setValue(sPrefCDA.getBytes());
        log.info("------ End PatientConsentManager.createDocumentRawData ------");
        return oDoc;
    }
    
   /**
     * To verify a XAML Document already persists in database
     * @param sPatientId The Patient identifier
    *  @param sAssigningAuthority The assigning authority
     * @return boolean
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     */
    public String checkCPPMetaFromRepositoryUsingXDSb(String sPatientId, String sAssigningAuthority)
            throws AdapterPIPException
    {
        log.info("--------------- Begin checkCPPMetaFromRepositoryUsingXDSb ---------------");
        boolean bResults = false;
        String sTargetObject = "";
        DocumentRegistryPortType oDocRegistryPort = getDocumentRegistryPort();

        AdhocQueryResponse oResponse = null;
        AdhocQueryRequest oRequest = createAdhocQueryRequest(sPatientId, sAssigningAuthority);
        oResponse = oDocRegistryPort.documentRegistryRegistryStoredQuery(oRequest);
        
        if(oResponse != null &&
                oResponse.getRegistryObjectList() != null &&
                oResponse.getRegistryObjectList().getIdentifiable() != null &&
                oResponse.getRegistryObjectList().getIdentifiable().size() > 0)
        {
            List<JAXBElement<? extends IdentifiableType>> olRegObjs = oResponse.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> oJAXBObj : olRegObjs)
            {
                if ((oJAXBObj != null) &&
                    (oJAXBObj.getDeclaredType() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName() != null) &&
                    (oJAXBObj.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                    (oJAXBObj.getValue() != null))
                {
                    ExtrinsicObjectType oExtObj = (ExtrinsicObjectType) oJAXBObj.getValue();
                    sTargetObject = oExtObj.getId();
                }
            }
        }
        return sTargetObject;
    }   
}
