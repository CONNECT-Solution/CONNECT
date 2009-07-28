/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class PatientConsentDocumentBuilderHelper {
    private static Log log = LogFactory.getLog(PatientConsentDocumentBuilderHelper.class);
    private static final String PROP_DIR = "NHINC_PROPERTIES_DIR";
    private static final String FILE_NAME = "XDSUniqueIds.properties";
    private static String sPropertyFile = "";
    static
    {
        String sValue = System.getenv(PROP_DIR);
            if ((sValue != null) && (sValue.length() > 0))
            {
                // Set it up so that we always have a "/" at the end - in case
                //------------------------------------------------------------
                if ((sValue.endsWith("/")) || (sValue.endsWith("\\")))
                {
                    sPropertyFile = sValue;
                }
                else
                {
                    String sFileSeparator = System.getProperty("file.separator");
                    sPropertyFile = sValue + sFileSeparator + FILE_NAME;
                }
            }
            else
            {
                log.error("Path to property files not found");
            }
    }
    
        
    public SubmitObjectsRequest createSubmitObjectRequest(String sTargetObject, String sHid, String sDocUniqueId, PatientPreferencesType oPtPref) throws PropertyAccessException
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        String hl7PatientId = "";
        SubmitObjectsRequest oSubmitObjectRequest = new SubmitObjectsRequest();
        RegistryObjectListType oRegistryObjectList = new RegistryObjectListType();
        if(oPtPref != null &&
                oPtPref.getPatientId() != null &&
                !oPtPref.getPatientId().contains("&ISO"))
        {
            hl7PatientId = PatientIdFormatUtil.hl7EncodePatientId(oPtPref.getPatientId(), sHid);
            hl7PatientId = StringUtil.extractStringFromTokens(hl7PatientId, "'");
        }
        else
        {
            hl7PatientId = oPtPref.getPatientId();
        }
        List<JAXBElement<? extends IdentifiableType>> oRegistryObjList = oRegistryObjectList.getIdentifiable();
        ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
        oExtObj.setId(sDocUniqueId);
        oExtObj.setHome(sHid);
        oExtObj.setMimeType(CDAConstants.PROVIDE_REGISTER_MIME_TYPE);
        oExtObj.setObjectType(CDAConstants.PROVIDE_REGISTER_OBJECT_TYPE);
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new
                oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots = oExtObj.getSlot();
        Format oFormatter = new SimpleDateFormat("yyyyMMDDHHmmss");
        Date date = new Date();
        oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_CREATION_TIME, oFormatter.format(date)));
        oSlots.add(createSlot(oRimObjectFactory,CDAConstants.SLOT_NAME_LANGUAGE_CODE, CDAConstants.LANGUAGE_CODE_ENGLISH));
        oSlots.add(createSlot(oRimObjectFactory, CDAConstants.SLOT_NAME_SOURCE_PATIENT_ID, hl7PatientId));
        oSlots.add(createSlot(oRimObjectFactory, "sourcePatientInfo", "PID-3|pid1^^^domain"));
        oExtObj.setName(createInternationalStringType(oRimObjectFactory,
                CDAConstants.CHARACTER_SET, CDAConstants.LANGUAGE_CODE_ENGLISH, CDAConstants.TITLE));
        ClassificationType oClassificationCDAR2 = createCDARClassification(sDocUniqueId,
                oRimObjectFactory);
        oExtObj.getClassification().add(oClassificationCDAR2);
        ClassificationType oClassificationClassCode = createClassification(oRimObjectFactory,
                CDAConstants.XDS_CLASS_CODE_SCHEMA_UUID,
                sDocUniqueId,
                "",
                CDAConstants.METADATA_CLASS_CODE,
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                "2.16.840.1.113883.6.1",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.METADATA_CLASS_CODE_DISPLAY_NAME);
        oExtObj.getClassification().add(oClassificationClassCode);
        ClassificationType oClassificationConfd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_CONFIDENTIALITY_CODE_UUID,
                sDocUniqueId,
                "",
                "R",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                "2.16.840.1.113883.5.25",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "Restricted");
        oExtObj.getClassification().add(oClassificationConfd);
        ClassificationType oClassificationFacCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_FACILITY_TYPE_UUID,
                sDocUniqueId,
                "",
                "Outpatient",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                "Connect-a-thon healthcareFacilityTypeCodes",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "Outpatient");
        oExtObj.getClassification().add(oClassificationFacCd);
        ClassificationType oClassificationPractCd = createClassification(oRimObjectFactory,
                CDAConstants.PROVIDE_REGISTER_PRACTICE_SETTING_CD_UUID,
                sDocUniqueId,
                "",
                "394802001",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                "2.16.840.1.113883.6.96",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "General Medicine");
        oExtObj.getClassification().add(oClassificationPractCd);
        ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
				CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME,
				CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
				hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
        oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
                CDAConstants.DOCUMENT_ID_IDENT_SCHEME,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sDocUniqueId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOCUMENT_ID);
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
        JAXBElement<? extends IdentifiableType> oJAXBExtId = oRimObjectFactory.createExtrinsicObject(oExtObj);
        oRegistryObjList.add(oJAXBExtId);
        RegistryPackageType oRegistryPackage = new RegistryPackageType();
        oRegistryPackage.setObjectType(CDAConstants.XDS_REGISTRY_REGISTRY_PACKAGE_TYPE);
        oRegistryPackage.setId(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg = oRegistryPackage.getSlot();
        oSlotsReg.add(createSlot(oRimObjectFactory, CDAConstants.XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME, oFormatter.format(date)));
        ClassificationType oClassificationContentType = createClassification(oRimObjectFactory,
                CDAConstants.XDS_REGISTRY_CONTENT_TYPE_UUID,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                "",
                "History and Physical",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                "codingScheme",
                "Connect-a-thon contentTypeCodes",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "History and Physical");
        oRegistryPackage.getClassification().add(oClassificationContentType);
        ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory, CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
				CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID,
				CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
				hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_PATIENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);

        String sSubmissionSetUniqueId = getOidFromProperty("submissionsetuniqueid");
        oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sSubmissionSetUniqueId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
        oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_SOURCE_ID_UUID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sHid,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.PROVIDE_REGISTER_SLOT_NAME_SUBMISSION_SET_SOURCE_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
        JAXBElement<? extends IdentifiableType> oJAXBRegPack = oRimObjectFactory.createRegistryPackage(oRegistryPackage);
        oRegistryObjList.add(oJAXBRegPack);
         AssociationType1 oAssociation = new AssociationType1();
        oAssociation.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE);
        oAssociation.setSourceObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oAssociation.setTargetObject(sDocUniqueId);
        oAssociation.setId(UUID.randomUUID().toString());
        oAssociation.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
        SlotType1 oSlot = createSlot(oRimObjectFactory, "SubmissionSetStatus", "Original");
        oAssociation.getSlot().add(oSlot);
        JAXBElement<? extends IdentifiableType> oJAXBAss = oRimObjectFactory.createAssociation(oAssociation);
        oRegistryObjList.add(oJAXBAss);
        if(sTargetObject != null &&
                !sTargetObject.equals(""))
        {
            log.info("Found Doc Id - Begin Association RPLC Created");
            AssociationType1 oAssociationRPLC = new AssociationType1();
            oAssociationRPLC.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE_RPLC);
            oAssociationRPLC.setSourceObject(sDocUniqueId);
            oAssociationRPLC.setTargetObject(sTargetObject);
            oAssociationRPLC.setId(UUID.randomUUID().toString());
            oAssociationRPLC.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
            JAXBElement<? extends IdentifiableType> oJAXBAssRplc = oRimObjectFactory.createAssociation(oAssociationRPLC);
            oRegistryObjList.add(oJAXBAssRplc);
            log.info("End Association RPLC Created");
        }
       
        ClassificationType oClassificationSumissionSet = new ClassificationType();
        oClassificationSumissionSet.setClassificationNode(CDAConstants.PROVIDE_REGISTER_SUBMISSION_SET_CLASSIFICATION_UUID);
        oClassificationSumissionSet.setClassifiedObject(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        oClassificationSumissionSet.setObjectType(CDAConstants.CLASSIFICATION_REGISTRY_OBJECT);
        oClassificationSumissionSet.setId(UUID.randomUUID().toString());
        JAXBElement<? extends IdentifiableType> oJAXBClass = oRimObjectFactory.createClassification(oClassificationSumissionSet);
        oRegistryObjectList.getIdentifiable().add(oJAXBClass);
        oSubmitObjectRequest.setRegistryObjectList(oRegistryObjectList);
        log.info("------- End PatientConsentDocumentBuilderHelper.createSubmitObjectRequest -------");
        return oSubmitObjectRequest;
    }
    
    /**
     * 
     * @param sDocId
     * @param oFactory
     * @param oPatCD
     * @return ClassificationType
     */
    private ClassificationType createCDARClassification(String sDocId, ObjectFactory oFactory)
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createCDARClassification -------");
        ClassificationType cClass = createClassification(oFactory,
                CDAConstants.CLASSIFICATION_SCHEMA_IDENTIFIER_CDAR2,
                sDocId,
                "",
				CDAConstants.NODE_REPRESENTATION_CDAR2,
				CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                CDAConstants.CLASSIFICATION_SCHEMA_CDNAME,
                CDAConstants.CDAR2_VALUE,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.NODE_REPRESENTATION_CDAR2);
        log.info("------- End PatientConsentDocumentBuilderHelper.createCDARClassification -------");
        return cClass;
    }
    
    /**
     * 
     * @param fact
     * @param name
     * @param value
     * @return SlotType1
     */
    private SlotType1 createSlot(ObjectFactory fact, String name, String value)
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createSlot -------");
        SlotType1 slot = fact.createSlotType1();
        slot.setName(name);
        ValueListType valList = new ValueListType();
        valList.getValue().add(value);
        slot.setValueList(valList);
        log.info("------- End PatientConsentDocumentBuilderHelper.createSlot -------");
        return slot;
    }

    /**
     *
     * @param fact
     * @param id
     * @param regObject
     * @param identScheme
     * @param objType
     * @param value
     * @param nameCharSet
     * @param nameLang
     * @param nameVal
     * @return ExternalIdentifierType
     */
    private ExternalIdentifierType createExternalIdentifier(ObjectFactory fact, String regObject, String identScheme, String objType, String value, String nameCharSet, String nameLang, String nameVal)
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
        String id = UUID.randomUUID().toString();
        ExternalIdentifierType idType = fact.createExternalIdentifierType();
        idType.setId(id);
        idType.setRegistryObject(regObject);
        idType.setIdentificationScheme(identScheme);
        idType.setObjectType(objType);
        idType.setValue(value);
        idType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        log.info("------- End PatientConsentDocumentBuilderHelper.createExternalIdentifier -------");
        return idType;
    }

    /**
     *
     * @param fact
     * @param scheme
     * @param clObject
     * @param id
     * @param nodeRep
     * @param objType
     * @param slotName
     * @param slotVal
     * @param nameCharSet
     * @param nameLang
     * @param nameVal
     * @return ClassificationType
     */
    private ClassificationType createClassification(ObjectFactory fact, String scheme, String clObject, String id, String nodeRep, String objType, String slotName, String slotVal, String nameCharSet, String nameLang, String nameVal)
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createClassification -------");
        ClassificationType cType = fact.createClassificationType();
        cType.setClassificationScheme(scheme);
        cType.setClassifiedObject(clObject);
        cType.setNodeRepresentation(nodeRep);
        if(id!=null && !id.equals(""))
        {
            cType.setId(id);
        }
        else
        {
            cType.setId(UUID.randomUUID().toString());
        }
        cType.setObjectType(objType);
        cType.getSlot().add(createSlot(fact, slotName, slotVal));
        cType.setName(createInternationalStringType(fact, nameCharSet, nameLang, nameVal));
        log.info("------- End PatientConsentDocumentBuilderHelper.createClassification -------");
        return cType;
    }
    
    /**
     *
     * @param fact
     * @param charSet
     * @param language
     * @param value
     * @return InternationalStringType
     */
    private InternationalStringType createInternationalStringType(ObjectFactory fact, String charSet, String language, String value)
    {
        log.info("------- Begin PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        InternationalStringType intStr = fact.createInternationalStringType();
        LocalizedStringType locStr = fact.createLocalizedStringType();
        locStr.setCharset(charSet);
        locStr.setLang(language);
        locStr.setValue(value);
        intStr.getLocalizedString().add(locStr);
        log.info("------- End PatientConsentDocumentBuilderHelper.createInternationalStringType -------");
        return intStr;
    }

    protected synchronized String getOidFromProperty(String sPropertyName)
    {
        String sUniqueId = "";
        try
        {
            Properties tempProp = new Properties();
            InputStream propsInFile = new FileInputStream(sPropertyFile);
            tempProp.load(propsInFile);
            propsInFile.close();
            sUniqueId = tempProp.getProperty(sPropertyName);
            String sFirstPart = sUniqueId.substring(0, sUniqueId.lastIndexOf("."));
            String sDotString = sUniqueId.substring(sUniqueId.lastIndexOf("."));
            sDotString = sDotString.substring(1);
            int iSUniqueId = Integer.parseInt(sDotString);
            iSUniqueId = iSUniqueId + 1;
            String sNewId = sFirstPart+"."+iSUniqueId;
            tempProp.setProperty(sPropertyName, sNewId);
            OutputStream propsOutFile = new FileOutputStream(sPropertyFile);
            tempProp.store(propsOutFile, "Update property file with new sequence");
            propsOutFile.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return sUniqueId;
    }
}
