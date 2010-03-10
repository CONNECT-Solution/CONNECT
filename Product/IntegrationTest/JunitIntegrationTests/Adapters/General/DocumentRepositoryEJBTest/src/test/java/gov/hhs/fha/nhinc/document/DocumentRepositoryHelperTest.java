package gov.hhs.fha.nhinc.document;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.UUID;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;

/**
 * NOTE: The previous class under test (DocumentRepositoryHelper) is no longer available since it has been moved to a
 * web application. This test might be appropriate on the AdapterInternalComponentProxy project.
 *
 * @author svalluripalli
 */
@Ignore
public class DocumentRepositoryHelperTest {
    private Log log = LogFactory.getLog(DocumentRepositoryHelperTest.class);
    public DocumentRepositoryHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
     @Test
     public void testDocumentRepositoryProvideAndRegisterDocumentSet()
     {
        String hl7PatientId = "D123401^^^&amp;1.1&amp;ISO";
        String sDocUniqueId = "D123401-Consent";
        String sHomeCommunityId = "1.1";
        ProvideAndRegisterDocumentSetRequestType oRequest = new ProvideAndRegisterDocumentSetRequestType();
        SubmitObjectsRequest oSubmitObjectRequest = new SubmitObjectsRequest();
        RegistryObjectListType oRegistryObjectList = new RegistryObjectListType();
        List<JAXBElement<? extends IdentifiableType>> oRegistryObjList = oRegistryObjectList.getIdentifiable();
        ExtrinsicObjectType oExtObj = new ExtrinsicObjectType();
        oExtObj.setId(sDocUniqueId);
        oExtObj.setHome(sHomeCommunityId);
        oExtObj.setMimeType("text/xml");
        oExtObj.setObjectType("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1");
        ObjectFactory oRimObjectFactory = new ObjectFactory();
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlots = oExtObj.getSlot();
        Format oFormatter = new SimpleDateFormat("yyyyMMDDHHmmss");
        Date date = new Date();
        oSlots.add(createSlot(oRimObjectFactory, "creationTime", oFormatter.format(date)));
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
                sDocUniqueId,/*CDAConstants.XDS_REGISTRY_CONTENT_TYPE_UUID*/
                "",
                "Consult",
                CDAConstants.CLASSIFICATION_REGISTRY_OBJECT,
                "codingScheme",
                "Connect-a-thon classCodes",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "Consult");
        oExtObj.getClassification().add(oClassificationClassCode);
        
        ExternalIdentifierType oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
				CDAConstants.EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME,
				CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
				hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "XDSDocumentEntry.patientId");
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
        oExtIdTypePat = createExternalIdentifier(oRimObjectFactory,
                sDocUniqueId,
                CDAConstants.DOCUMENT_ID_IDENT_SCHEME,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sDocUniqueId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                "XDSDocumentEntry.uniqueId");
        oExtObj.getExternalIdentifier().add(oExtIdTypePat);
        JAXBElement<? extends IdentifiableType> oJAXBExtId = oRimObjectFactory.createExtrinsicObject(oExtObj);
        oRegistryObjList.add(oJAXBExtId);
        RegistryPackageType oRegistryPackage = new RegistryPackageType();
        oRegistryPackage.setObjectType(CDAConstants.XDS_REGISTRY_REGISTRY_PACKAGE_TYPE);
        oRegistryPackage.setId(CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT);
        List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> oSlotsReg = oRegistryPackage.getSlot();
        oSlotsReg.add(createSlot(oRimObjectFactory, CDAConstants.XDS_REGISTRY_SLOT_NAME_SUBMISSION_TIME, oFormatter.format(date)));
        //oRegistryPackage.getClassification().add(oClassificationContentType); //accidentally deleted the code to create a populate this object
        oRegistryPackage.getClassification().add(oClassificationClassCode);
        ExternalIdentifierType oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory, CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
				CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_PATIENTID,
				CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
				hl7PatientId,
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.XDS_PROVIDE_REGISTER_SLOT_NAME_PATIENT_ID);
        oRegistryPackage.getExternalIdentifier().add(oExtIdTypePatForReg);
        oExtIdTypePatForReg = createExternalIdentifier(oRimObjectFactory,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_REGISTRYOBJECT,
                CDAConstants.EXTERNAL_IDENTIFICATION_SCHEMA_UNIQUEID,
                CDAConstants.EXTERNAL_OBJECT_IDENTIFIER_TYPE,
                sDocUniqueId+".1",
                CDAConstants.CHARACTER_SET,
                CDAConstants.LANGUAGE_CODE_ENGLISH,
                CDAConstants.SLOT_NAME_DOC_SUBMISSION_SET_DOCUMENT_ID);
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
        log.info("Found Doc Id - Begin Association RPLC Created");
        AssociationType1 oAssociationRPLC = new AssociationType1();
        oAssociationRPLC.setAssociationType(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE_RPLC);
        oAssociationRPLC.setSourceObject(sDocUniqueId);
        oAssociationRPLC.setTargetObject(CDAConstants.XDS_REGISTRY_ASSOCIATION_TYPE_TARGET_OBJECT);
        oAssociationRPLC.setId(UUID.randomUUID().toString());
        oAssociationRPLC.setObjectType(CDAConstants.XDS_REGISTRY_ASSOCIATION_OBJECT_TYPE);
        JAXBElement<? extends IdentifiableType> oJAXBAssRplc = oRimObjectFactory.createAssociation(oAssociationRPLC);
        oRegistryObjList.add(oJAXBAssRplc);
        log.info("End Association RPLC Created");
        oSubmitObjectRequest.setRegistryObjectList(oRegistryObjectList);
        oRequest.setSubmitObjectsRequest(oSubmitObjectRequest);
        ProvideAndRegisterDocumentSetRequestType.Document oDoc = new ProvideAndRegisterDocumentSetRequestType.Document();
        oDoc.setId(sDocUniqueId);
        oDoc.setValue("Test Document".getBytes());
        oRequest.getDocument().add(oDoc);
//        RegistryResponseType oResponse = new DocumentRepositoryHelper().documentRepositoryProvideAndRegisterDocumentSet(oRequest);
//        if(oResponse != null &&
//                oResponse.getStatus() != null &&
//                oResponse.getStatus().equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"))
//        {
//            log.info("Document Saved Successfully to the repository");
//        }
//        else
//        {
//            log.info("Unable to Save the Document");
//
//        }
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
}