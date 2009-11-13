package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyMetadataType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040DocumentationOf;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040NonXMLBody;
import org.hl7.v3.POCDMT000040ServiceEvent;
import org.hl7.v3.TSExplicit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.EDExplicit;

/**
 *
 * @author Neil Webb
 */
public class ConsentDocumentCreator
{
    private Log log = null;

    public ConsentDocumentCreator()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    
    /**
     * This method takes the patient preference information and creates the
     * Clinical Document that represents that information.
     *
     * @param oPtPref  The patient prefernces.
     * @param sConsentXACML The XACML string that should be placed in the CDA document
     * @return The clinical document that represents the patient preferences.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException This
     *         exception is thrown if any errors occur.
     */
    public POCDMT000040ClinicalDocument createConsentCDADoc(PatientPreferencesType oPtPref, String sConsentXACML)
        throws AdapterPIPException
    {
        DocumentBuilderFactory oDOMFactory = null;
        DocumentBuilder oDOMBuilder = null;
        Document oDOMDocument = null;
        Date dtNow = new Date();
        SimpleDateFormat oFormat = new SimpleDateFormat("yyyyMMddmmssSSZ");
        String sNow = oFormat.format(dtNow);
        boolean bOptIn = false;
        String sDocumentTitle = null;

        if(oPtPref != null)
        {
            bOptIn = oPtPref.isOptIn();
            FineGrainedPolicyMetadataType metadata = oPtPref.getFineGrainedPolicyMetadata();
            if(metadata != null)
            {
                if(NullChecker.isNotNullish(metadata.getDocumentTitle()))
                {
                    sDocumentTitle = metadata.getDocumentTitle();
                }
            }
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

        // Class code
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
        if(sDocumentTitle != null)
        {
            oTitle.setTextContent(sDocumentTitle);
        }
        else
        {
            oTitle.setTextContent(CDAConstants.TITLE);
        }

        // EffectiveTime
        //---------------
        TSExplicit oEffectiveTime = new TSExplicit();
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

        // This set of code is used if we are using DOM to set this.
        // The XML Schema would have to be set to use DOM for this node.
        //---------------------------------------------------------------
//        Element oElementXACMLConsent = null;
//        oElementXACMLConsent = oDOMDocument.createElement(CDAConstants.TEXT_TAG);
//        oElementXACMLConsent.setTextContent(StringUtil.wrapCdata(sConsentXACML));
//        oNonXMLBody.setText(oElementXACMLConsent);

        EDExplicit oEd = new EDExplicit();
        oNonXMLBody.setText(oEd);
        oEd.getContent().add(StringUtil.wrapCdata(sConsentXACML));


        return oPrefCDA;
    }


}
