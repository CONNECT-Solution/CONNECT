package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.AddressType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyPatientInfoType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.AdxpExplicitCity;
import org.hl7.v3.AdxpExplicitCountry;
import org.hl7.v3.AdxpExplicitPostalCode;
import org.hl7.v3.AdxpExplicitState;
import org.hl7.v3.AdxpExplicitStreetAddressLine;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TSExplicit;

/**
 * This class creates CDA documents from BinaryDocumentPolicyCriteria objects.
 *
 * @author Les Westberg
 */
public class CdaPdfCreator
{
    protected Log log = null;

    private static final String HL7_DATE_ONLY_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat oHL7DateOnlyFormatter = new SimpleDateFormat(HL7_DATE_ONLY_FORMAT);
    private static final String HL7_DATE_TIME_FORMAT = "yyyyMMddHHmmssZ";
    private static final SimpleDateFormat oHL7DateTimeFormatter = new SimpleDateFormat(HL7_DATE_TIME_FORMAT);

    /**
     * Default constructor.
     */
    public CdaPdfCreator()
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
     * This class creates an instance of an II with the given
     * root and extension.
     *
     * @param sRoot The root value for the II object.
     * @param sExtension The extension for the II object.
     *
     * @return The II object that was constructed.
     */
    private II createII(String sRoot, String sExtension)
    {
        II oII = new II();
        boolean bHaveData = false;

        if (sRoot != null)
        {
            oII.setRoot(sRoot);
            bHaveData = true;
        }

        if (sExtension != null)
        {
            oII.setExtension(sExtension);
            bHaveData = true;
        }

        if (bHaveData)
        {
            return oII;
        }
        else
        {
            return null;
        }
    }

    /**
     * Create the template ID tag.
     *
     * @return The Template ID tag.
     */
    private II createTemplateId()
    {
        return createII(CDAConstants.TEMPLATE_ID_ROOT_XDS_SD_DOCUMENT, null);
    }

    /**
     * This returns the home community ID from the gateway.properties file.
     *
     * @return The home community ID.
     * @throws AdapterPIPException This exception is thrown if there is an error.
     */
    private String getHomeCommunityId()
        throws AdapterPIPException
    {
        String sHomeCommunityId = null;
        try
        {
            sHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve home community ID from gateway properties file.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sHomeCommunityId;
    }

    /**
     * Creates the ID tag for the CDA document.
     *
     * @return The ID tag for the CDA document.
     * @throws AdapterPIPException This exception is thrown if any error occurs.
     */
    private II createId(String sDocumentUniqueId)
        throws AdapterPIPException
    {
        String sHomeCommunityId = getHomeCommunityId();
        return createII(sHomeCommunityId, sDocumentUniqueId);

    }

    /**
     * This creates the type ID tag and returns it.
     *
     * @return The TypeId tag.
     */
    private POCDMT000040InfrastructureRootTypeId createTypeId()
    {
        POCDMT000040InfrastructureRootTypeId oTypeId = new POCDMT000040InfrastructureRootTypeId();

        oTypeId.setExtension(CDAConstants.TYPE_ID_EXTENSION_POCD_HD000040);
        oTypeId.setRoot(CDAConstants.TYPE_ID_ROOT);

        return oTypeId;
    }

    /**
     * Transform the CE into an HL7 CE type.
     *
     * @param oCe The policy representation of the CE.
     * @return The HL7 representation of the CE.
     */
    private CE createCode(CeType oCe)
    {
        CE oHl7Ce = new CE();
        boolean bHaveData = false;

        if (oCe != null)
        {
            if (oCe.getCode() != null)
            {
                oHl7Ce.setCode(oCe.getCode());
                bHaveData = true;
            }

            if (oCe.getDisplayName() != null)
            {
                oHl7Ce.setDisplayName(oCe.getDisplayName());
                bHaveData = true;
            }

            if (oCe.getCodeSystem() != null)
            {
                oHl7Ce.setCodeSystem(oCe.getCodeSystem());
                bHaveData = true;
            }

            if (oCe.getCodeSystemName() != null)
            {
                oHl7Ce.setCodeSystemName(oCe.getCodeSystemName());
                bHaveData = true;
            }

        }   // if (oCE != null)

        if (bHaveData)
        {
            return oHl7Ce;
        }
        else
        {
            return null;
        }

    }

    /**
     * This creates an STExplicit with the given string value.
     *
     * @param sValue The value to use when creating the node.
     * @return The STExplicit object containing the value.
     */
    private STExplicit createST(String sValue)
    {
        STExplicit oHl7St = new STExplicit();
        boolean bHaveData = false;

        if (sValue != null)
        {
            oHl7St.getContent().add(sValue);
            bHaveData = true;
        }

        if (bHaveData)
        {
            return oHl7St;
        }
        else
        {
            return null;
        }
    }

    /**
     * This method transforms the given XMLDate into an HL7 date.
     *
     * @param oXMLDate The XML date.
     * @return The HL7 date.
     */
    private TSExplicit createTS(XMLGregorianCalendar oXMLDate)
    {
        TSExplicit oHL7Ts = new TSExplicit();
        boolean bHaveData = false;

        if ((oXMLDate != null) &&
            (oXMLDate.toGregorianCalendar() != null) &&
            (oXMLDate.toGregorianCalendar().getTime() != null))
        {
            String sDate = oHL7DateTimeFormatter.format(oXMLDate.toGregorianCalendar().getTime());
            if ((sDate != null) &&
                (sDate.length() > 0))
            {
                oHL7Ts.setValue(sDate);
                bHaveData = true;
            }
        }

        if (bHaveData)
        {
            return oHL7Ts;
        }
        else
        {
            return null;
        }
    }


    /**
     * This method creates a CS using the given code.
     *
     * @param sCode The code to put into the CS.
     * @return The CS object to be returned..
     */
    private CS createCS(String sCode)
    {
        CS oHL7Cs = new CS();
        boolean bHaveData = false;

        if (sCode != null)
        {
            oHL7Cs.setCode(sCode);
            bHaveData = true;
        }

        if (bHaveData)
        {
            return oHL7Cs;
        }
        else
        {
            return null;
        }
    }

    /**
     * This creates an HL7 address from an AddressType object.
     *
     * @param oAddress The address to get the information from.
     * @return The HL7 address to be returned.
     */
    private ADExplicit createAD(AddressType oAddress)
    {
        ADExplicit oHL7Ad = new ADExplicit();
        boolean bHaveData = false;
        org.hl7.v3.ObjectFactory oObjectFactory = new ObjectFactory();

        if (oAddress != null)
        {
            // Street
            //-------
            if (oAddress.getStreetAddress() != null)
            {
                AdxpExplicitStreetAddressLine oHL7StreetAddressLine = new AdxpExplicitStreetAddressLine();
                oHL7StreetAddressLine.setContent(oAddress.getStreetAddress());
                JAXBElement<AdxpExplicitStreetAddressLine> oElement = oObjectFactory.createADExplicitStreetAddressLine(oHL7StreetAddressLine);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // City
            //------
            if (oAddress.getCity() != null)
            {
                AdxpExplicitCity oHL7City = new AdxpExplicitCity();
                oHL7City.setContent(oAddress.getCity());
                JAXBElement<AdxpExplicitCity> oElement = oObjectFactory.createADExplicitCity(oHL7City);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // State
            //------
            if (oAddress.getState() != null)
            {
                AdxpExplicitState oHL7State = new AdxpExplicitState();
                oHL7State.setContent(oAddress.getState());
                JAXBElement<AdxpExplicitState> oElement = oObjectFactory.createADExplicitState(oHL7State);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // Zip Code
            //----------
            if (oAddress.getZipCode() != null)
            {
                AdxpExplicitPostalCode oHL7Zipcode = new AdxpExplicitPostalCode();
                oHL7Zipcode.setContent(oAddress.getZipCode());
                JAXBElement<AdxpExplicitPostalCode> oElement = oObjectFactory.createADExplicitPostalCode(oHL7Zipcode);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // Country
            //--------
            if (oAddress.getCountry() != null)
            {
                AdxpExplicitCountry oHL7Country = new AdxpExplicitCountry();
                oHL7Country.setContent(oAddress.getCountry());
                JAXBElement<AdxpExplicitCountry> oElement = oObjectFactory.createADExplicitCountry(oHL7Country);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

        }   // if (oAddress != null)

        if (bHaveData)
        {
            return oHL7Ad;
        }
        else
        {
            return null;
        }

    }

    /**
     * This creates an HL7 PN from a PersonNameType object.
     *
     * @param oName The name to get the information from.
     * @return The HL7 PN to be returned.
     */
    private PNExplicit createPN(PersonNameType oName)
    {
        PNExplicit oHL7Pn = new PNExplicit();
        boolean bHaveData = false;
        org.hl7.v3.ObjectFactory oObjectFactory = new ObjectFactory();

        if (oName != null)
        {
            // Prefix
            //-------
            if (oName.getPrefix() != null)
            {
                EnExplicitPrefix oHL7Prefix = new EnExplicitPrefix();
                oHL7Prefix.setContent(oName.getPrefix());
                JAXBElement<EnExplicitPrefix> oElement = oObjectFactory.createENExplicitPrefix(oHL7Prefix);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Given
            //------
            if (oName.getGivenName() != null)
            {
                EnExplicitGiven oHL7Given = new EnExplicitGiven();
                oHL7Given.setContent(oName.getGivenName());
                JAXBElement<EnExplicitGiven> oElement = oObjectFactory.createENExplicitGiven(oHL7Given);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Family
            //--------
            if (oName.getFamilyName() != null)
            {
                EnExplicitFamily oHL7Family = new EnExplicitFamily();
                oHL7Family.setContent(oName.getFamilyName());
                JAXBElement<EnExplicitFamily> oElement = oObjectFactory.createENExplicitFamily(oHL7Family);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Suffix
            //----------
            if (oName.getSuffix() != null)
            {
                EnExplicitSuffix oHL7Suffix = new EnExplicitSuffix();
                oHL7Suffix.setContent(oName.getSuffix());
                JAXBElement<EnExplicitSuffix> oElement = oObjectFactory.createENExplicitSuffix(oHL7Suffix);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

        }   // if (oName != null)

        if (bHaveData)
        {
            return oHL7Pn;
        }
        else
        {
            return null;
        }

    }


    /**
     * This method creates a Record Target from the given data fields.
     *
     * @param sAssigningAuthority The assigning authority for the patient ID.
     * @param sPatientId The patient ID.
     * @param oPatientInfo The patient information from the criterion object.
     * @return The RecordTarget object.
     */
    private POCDMT000040RecordTarget createRecordTarget(String sAssigningAuthority, String sPatientId,  PolicyPatientInfoType oPatientInfo)
        throws AdapterPIPException
    {
        POCDMT000040RecordTarget oRecordTarget = new POCDMT000040RecordTarget();
        boolean bHaveData = false;

        POCDMT000040PatientRole oPatientRole = new POCDMT000040PatientRole();
        oRecordTarget.setPatientRole(oPatientRole);

        // Patient Assigning Authority and ID
        //------------------------------------
        II oII = createII(sAssigningAuthority, sPatientId);
        if (oII != null)
        {
            oPatientRole.getId().add(oII);
            bHaveData = true;
        }

        if (oPatientInfo != null)
        {
            // Patient Address
            //----------------
            if ((oPatientInfo.getAddr() != null) &&
                (oPatientInfo.getAddr().getAddress() != null) &&
                (oPatientInfo.getAddr().getAddress().size() > 0))
            {
                for (AddressType oAddress : oPatientInfo.getAddr().getAddress())
                {
                    ADExplicit oHL7Address = createAD(oAddress);
                    if (oHL7Address != null)
                    {
                        oPatientRole.getAddr().add(oHL7Address);
                        bHaveData = true;
                    }
                }   // for (AddressType oAddress : oPatientInfo.getAddr().getAddress())
            }   // if ((oPatientInfo.getAddr() != null) &&

            // Fill in the patient tag.
            //-------------------------
            POCDMT000040Patient oPatientTag = new POCDMT000040Patient();
            boolean bHavePatientTag = false;

            // Patient Name
            //-------------
            if (oPatientInfo.getName() != null)
            {
                PNExplicit oHL7Name = createPN(oPatientInfo.getName());
                if (oHL7Name != null)
                {
                    oPatientTag.getName().add(oHL7Name);
                    bHavePatientTag = true;
                }
            }

            // Gender
            //--------
            if (oPatientInfo.getGender() != null)
            {
                CE oHL7Ce = createCode(oPatientInfo.getGender());
                if (oHL7Ce != null)
                {
                    oPatientTag.setAdministrativeGenderCode(oHL7Ce);
                    bHavePatientTag = true;
                }
            }

            // BirthTime
            //-----------
            if (oPatientInfo.getBirthTime() != null)
            {
                TSExplicit oHL7BirthTime = createTS(oPatientInfo.getBirthTime());
                if (oHL7BirthTime != null)
                {
                    oPatientTag.setBirthTime(oHL7BirthTime);
                    bHavePatientTag = true;
                }
            }

            if (bHavePatientTag)
            {
                oPatientRole.setPatient(oPatientTag);
                bHaveData = true;
            }
        }   // if (oPatientInfo != null)

        if (bHaveData)
        {
            return oRecordTarget;
        }
        else
        {
            return null;
        }


    }


    /**
     * This method creates a single CDA document from the given BinaryDocumentPolicyCriterionType.
     *
     * @param oPtPref This contains the patient preference information. There is some infomration in this
     *                that is common to each of the criterion that need to be available when we create the CDA
     *                document.
     * @param oCriterion The binary document criterion containing the data.
     * @return The CDA document returned.
     * @throws AdapterPIPException This exception is thrown if there are any errors.
     */
    public POCDMT000040ClinicalDocument createCDA(PatientPreferencesType oPtPref, BinaryDocumentPolicyCriterionType oCriterion)
        throws AdapterPIPException
    {
        POCDMT000040ClinicalDocument oCda = new POCDMT000040ClinicalDocument();
        boolean bHaveData = false;

        if (oCriterion != null)
        {
            bHaveData = true;
            
            // Class code and mood code
            //-------------------------
            oCda.setClassCode(ActClassClinicalDocument.DOCCLIN);
            oCda.getMoodCode().add(CDAConstants.CDA_MOOD_CODE);

            // Type ID
            //---------
            oCda.setTypeId(createTypeId());

            // Template ID
            //-------------
            oCda.getTemplateId().add(createTemplateId());

            // ID
            //---
            oCda.setId(createId(oCriterion.getDocumentUniqueId()));

            // Code
            //-----
            oCda.setCode(createCode(oCriterion.getDocumentTypeCode()));

            // Title
            //-------
            oCda.setTitle(createST(oCriterion.getDocumentTitle()));

            // EffectiveTime
            //---------------
            oCda.setEffectiveTime(createTS(oCriterion.getEffectiveTime()));

            // ConfidentialityCode
            //---------------------
            oCda.setConfidentialityCode(createCode(oCriterion.getConfidentialityCode()));

            // Language Code
            //--------------
            oCda.setLanguageCode(createCS(CDAConstants.LANGUAGE_CODE_ENGLISH));

            // Record Target
            //---------------
            oCda.getRecordTarget().add(createRecordTarget(oPtPref.getAssigningAuthority(), oPtPref.getPatientId(), oCriterion.getPatientInfo()));

            
            
            

            



        }   // if (oCriterion != null)

        if (bHaveData)
        {
            return oCda;
        }
        else
        {
            return null;
        }

    }

    /**
     * This method creates a set of CDA documents from the given BinaryDocumentPolicyCriterion objects.
     *
     * @param oPtPref The patient preferences information for the CDA.
     * @return The list of CDA documents returned.
     * @throws AdapterPIPException This is thrown if any exception occurs in the process.
     */
    public List<POCDMT000040ClinicalDocument> createCDA(PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        ArrayList<POCDMT000040ClinicalDocument> olCda = new ArrayList<POCDMT000040ClinicalDocument>();

        if ((oPtPref != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria() != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().size() > 0))
        {
            for (BinaryDocumentPolicyCriterionType oCriterion : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion())
            {
                POCDMT000040ClinicalDocument oCda = createCDA(oPtPref, oCriterion);
                if (oCda != null)
                {
                    olCda.add(oCda);
                }
            }
        }


        if (olCda.size() > 0)
        {
            return olCda;
        }
        else
        {
            return null;
        }
    }



}
