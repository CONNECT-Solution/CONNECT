package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.AddressType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyCustodianInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyDataEntererInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyLegalAuthenticatorType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyOriginalAuthorInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyPatientInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PolicyScannerAuthorInfoType;
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
import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.EnExplicitPrefix;
import org.hl7.v3.EnExplicitSuffix;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.IVXBTSExplicit;
import org.hl7.v3.ONExplicit;
import org.hl7.v3.ObjectFactory;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.POCDMT000040AssignedAuthor;
import org.hl7.v3.POCDMT000040AssignedCustodian;
import org.hl7.v3.POCDMT000040AssignedEntity;
import org.hl7.v3.POCDMT000040Author;
import org.hl7.v3.POCDMT000040AuthoringDevice;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Custodian;
import org.hl7.v3.POCDMT000040CustodianOrganization;
import org.hl7.v3.POCDMT000040DataEnterer;
import org.hl7.v3.POCDMT000040DocumentationOf;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.POCDMT000040LegalAuthenticator;
import org.hl7.v3.POCDMT000040NonXMLBody;
import org.hl7.v3.POCDMT000040Organization;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040Person;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.POCDMT000040ServiceEvent;
import org.hl7.v3.SCExplicit;
import org.hl7.v3.STExplicit;
import org.hl7.v3.TSExplicit;

/**
 * This class creates CDA documents from BinaryDocumentPolicyCriteria objects.
 *
 * @author Les Westberg
 */
public class CdaPdfCreator {

    protected Log log = null;
    private static final String HL7_DATE_ONLY_FORMAT = "yyyyMMdd";
    private static final SimpleDateFormat oHL7DateOnlyFormatter = new SimpleDateFormat(HL7_DATE_ONLY_FORMAT);
    private static final String HL7_DATE_TIME_FORMAT = "yyyyMMddHHmmssZ";
    private static final SimpleDateFormat oHL7DateTimeFormatter = new SimpleDateFormat(HL7_DATE_TIME_FORMAT);

    /**
     * Default constructor.
     */
    public CdaPdfCreator() {
        log = createLogger();
    }

    /**
     * Sets up the logger object.
     */
    protected Log createLogger() {
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
    private II createII(String sRoot, String sExtension) {
        II oII = new II();
        boolean bHaveData = false;

        if (sRoot != null) {
            oII.setRoot(sRoot);
            bHaveData = true;
        }

        if (sExtension != null) {
            oII.setExtension(sExtension);
            bHaveData = true;
        }

        if (bHaveData) {
            return oII;
        } else {
            return null;
        }
    }

    /**
     * Create the template ID tag.
     *
     * @return The Template ID tag.
     */
    private II createTemplateId() {
        return createII(CDAConstants.TEMPLATE_ID_ROOT_XDS_SD_DOCUMENT, null);
    }

    /**
     * This returns the home community ID from the gateway.properties file.
     *
     * @return The home community ID.
     * @throws AdapterPIPException This exception is thrown if there is an error.
     */
    private String getHomeCommunityId()
            throws AdapterPIPException {
        String sHomeCommunityId = null;
        try {
            sHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception e) {
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
            throws AdapterPIPException {
        String sHomeCommunityId = getHomeCommunityId();
        return createII(sHomeCommunityId, sDocumentUniqueId);

    }

    /**
     * This creates the type ID tag and returns it.
     *
     * @return The TypeId tag.
     */
    private POCDMT000040InfrastructureRootTypeId createTypeId() {
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
    private CE createCode(CeType oCe) {
        CE oHl7Ce = new CE();
        boolean bHaveData = false;

        if (oCe != null) {
            if (oCe.getCode() != null) {
                oHl7Ce.setCode(oCe.getCode());
                bHaveData = true;
            }

            if (oCe.getDisplayName() != null) {
                oHl7Ce.setDisplayName(oCe.getDisplayName());
                bHaveData = true;
            }

            if (oCe.getCodeSystem() != null) {
                oHl7Ce.setCodeSystem(oCe.getCodeSystem());
                bHaveData = true;
            }

            if (oCe.getCodeSystemName() != null) {
                oHl7Ce.setCodeSystemName(oCe.getCodeSystemName());
                bHaveData = true;
            }

        }   // if (oCE != null)

        if (bHaveData) {
            return oHl7Ce;
        } else {
            return null;
        }

    }

    /**
     * This creates an STExplicit with the given string value.
     *
     * @param sValue The value to use when creating the node.
     * @return The STExplicit object containing the value.
     */
    private STExplicit createST(String sValue) {
        STExplicit oHl7St = new STExplicit();
        boolean bHaveData = false;

        if (sValue != null) {
            oHl7St.getContent().add(sValue);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHl7St;
        } else {
            return null;
        }
    }

    /**
     * This method transforms the given XMLDate into an HL7 date.
     *
     * @param sHL7Date The date in HL7 format.
     * @return The HL7 date.
     */
    private TSExplicit createTS(String sHL7Date) {
        TSExplicit oHL7Ts = new TSExplicit();
        boolean bHaveData = false;

        if (sHL7Date != null) {
            oHL7Ts.setValue(sHL7Date);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7Ts;
        } else {
            return null;
        }
    }

    /**
     * Transform the Ce into an HL7 CS type.
     *
     * @param oCe The policy representation of the CE.
     * @return The HL7 representation of the CS.
     */
    private CS createCS(CeType oCs) {
        CS oHl7Cs = new CS();
        boolean bHaveData = false;

        if (oCs != null) {
            if (oCs.getCode() != null) {
                oHl7Cs.setCode(oCs.getCode());
                bHaveData = true;
            }

            if (oCs.getDisplayName() != null) {
                oHl7Cs.setDisplayName(oCs.getDisplayName());
                bHaveData = true;
            }

            if (oCs.getCodeSystem() != null) {
                oHl7Cs.setCodeSystem(oCs.getCodeSystem());
                bHaveData = true;
            }

            if (oCs.getCodeSystemName() != null) {
                oHl7Cs.setCodeSystemName(oCs.getCodeSystemName());
                bHaveData = true;
            }

        }   // if (oCs != null)

        if (bHaveData) {
            return oHl7Cs;
        } else {
            return null;
        }

    }

    /**
     * This method creates a CS using the given code.
     *
     * @param sCode The code to put into the CS.
     * @return The CS object to be returned..
     */
    private CS createCS(String sCode) {
        CS oHL7Cs = new CS();
        boolean bHaveData = false;

        if (sCode != null) {
            oHL7Cs.setCode(sCode);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7Cs;
        } else {
            return null;
        }
    }

    /**
     * This creates an HL7 address from an AddressType object.
     *
     * @param oAddress The address to get the information from.
     * @return The HL7 address to be returned.
     */
    private ADExplicit createAD(AddressType oAddress) {
        ADExplicit oHL7Ad = new ADExplicit();
        boolean bHaveData = false;
        org.hl7.v3.ObjectFactory oObjectFactory = new ObjectFactory();

        if (oAddress != null) {
            // Street
            //-------
            if (oAddress.getStreetAddress() != null) {
                AdxpExplicitStreetAddressLine oHL7StreetAddressLine = new AdxpExplicitStreetAddressLine();
                oHL7StreetAddressLine.setContent(oAddress.getStreetAddress());
                JAXBElement<AdxpExplicitStreetAddressLine> oElement = oObjectFactory.createADExplicitStreetAddressLine(oHL7StreetAddressLine);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // City
            //------
            if (oAddress.getCity() != null) {
                AdxpExplicitCity oHL7City = new AdxpExplicitCity();
                oHL7City.setContent(oAddress.getCity());
                JAXBElement<AdxpExplicitCity> oElement = oObjectFactory.createADExplicitCity(oHL7City);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // State
            //------
            if (oAddress.getState() != null) {
                AdxpExplicitState oHL7State = new AdxpExplicitState();
                oHL7State.setContent(oAddress.getState());
                JAXBElement<AdxpExplicitState> oElement = oObjectFactory.createADExplicitState(oHL7State);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // Zip Code
            //----------
            if (oAddress.getZipCode() != null) {
                AdxpExplicitPostalCode oHL7Zipcode = new AdxpExplicitPostalCode();
                oHL7Zipcode.setContent(oAddress.getZipCode());
                JAXBElement<AdxpExplicitPostalCode> oElement = oObjectFactory.createADExplicitPostalCode(oHL7Zipcode);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

            // Country
            //--------
            if (oAddress.getCountry() != null) {
                AdxpExplicitCountry oHL7Country = new AdxpExplicitCountry();
                oHL7Country.setContent(oAddress.getCountry());
                JAXBElement<AdxpExplicitCountry> oElement = oObjectFactory.createADExplicitCountry(oHL7Country);
                oHL7Ad.getContent().add(oElement);
                bHaveData = true;
            }

        }   // if (oAddress != null)

        if (bHaveData) {
            return oHL7Ad;
        } else {
            return null;
        }

    }

    /**
     * This creates an HL7 PN from a PersonNameType object.
     *
     * @param oName The name to get the information from.
     * @return The HL7 PN to be returned.
     */
    private PNExplicit createPN(PersonNameType oName) {
        PNExplicit oHL7Pn = new PNExplicit();
        boolean bHaveData = false;
        org.hl7.v3.ObjectFactory oObjectFactory = new ObjectFactory();

        if (oName != null) {
            // Prefix
            //-------
            if (oName.getPrefix() != null) {
                EnExplicitPrefix oHL7Prefix = new EnExplicitPrefix();
                oHL7Prefix.setContent(oName.getPrefix());
                JAXBElement<EnExplicitPrefix> oElement = oObjectFactory.createENExplicitPrefix(oHL7Prefix);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Given
            //------
            if (oName.getGivenName() != null) {
                EnExplicitGiven oHL7Given = new EnExplicitGiven();
                oHL7Given.setContent(oName.getGivenName());
                JAXBElement<EnExplicitGiven> oElement = oObjectFactory.createENExplicitGiven(oHL7Given);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Family
            //--------
            if (oName.getFamilyName() != null) {
                EnExplicitFamily oHL7Family = new EnExplicitFamily();
                oHL7Family.setContent(oName.getFamilyName());
                JAXBElement<EnExplicitFamily> oElement = oObjectFactory.createENExplicitFamily(oHL7Family);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

            // Suffix
            //----------
            if (oName.getSuffix() != null) {
                EnExplicitSuffix oHL7Suffix = new EnExplicitSuffix();
                oHL7Suffix.setContent(oName.getSuffix());
                JAXBElement<EnExplicitSuffix> oElement = oObjectFactory.createENExplicitSuffix(oHL7Suffix);
                oHL7Pn.getContent().add(oElement);
                bHaveData = true;
            }

        }   // if (oName != null)

        if (bHaveData) {
            return oHL7Pn;
        } else {
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
    private POCDMT000040RecordTarget createRecordTarget(String sAssigningAuthority, String sPatientId, PolicyPatientInfoType oPatientInfo)
            throws AdapterPIPException {
        POCDMT000040RecordTarget oRecordTarget = new POCDMT000040RecordTarget();
        boolean bHaveData = false;

        POCDMT000040PatientRole oPatientRole = new POCDMT000040PatientRole();
        oRecordTarget.setPatientRole(oPatientRole);

        // Patient Assigning Authority and ID
        //------------------------------------
        II oII = createII(sAssigningAuthority, sPatientId);
        if (oII != null) {
            oPatientRole.getId().add(oII);
            bHaveData = true;
        }

        if (oPatientInfo != null) {
            // Patient Address
            //----------------
            if ((oPatientInfo.getAddr() != null) &&
                    (oPatientInfo.getAddr().getAddress() != null) &&
                    (oPatientInfo.getAddr().getAddress().size() > 0)) {
                for (AddressType oAddress : oPatientInfo.getAddr().getAddress()) {
                    ADExplicit oHL7Address = createAD(oAddress);
                    if (oHL7Address != null) {
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
            if (oPatientInfo.getName() != null) {
                PNExplicit oHL7Name = createPN(oPatientInfo.getName());
                if (oHL7Name != null) {
                    oPatientTag.getName().add(oHL7Name);
                    bHavePatientTag = true;
                }
            }

            // Gender
            //--------
            if (oPatientInfo.getGender() != null) {
                CE oHL7Ce = createCode(oPatientInfo.getGender());
                if (oHL7Ce != null) {
                    oPatientTag.setAdministrativeGenderCode(oHL7Ce);
                    bHavePatientTag = true;
                }
            }

            // BirthTime
            //-----------
            if (oPatientInfo.getBirthTime() != null) {
                TSExplicit oHL7BirthTime = createTS(oPatientInfo.getBirthTime());
                if (oHL7BirthTime != null) {
                    oPatientTag.setBirthTime(oHL7BirthTime);
                    bHavePatientTag = true;
                }
            }

            if (bHavePatientTag) {
                oPatientRole.setPatient(oPatientTag);
                bHaveData = true;
            }
        }   // if (oPatientInfo != null)

        if (bHaveData) {
            return oRecordTarget;
        } else {
            return null;
        }


    }

    /**
     * Create an HL7 ON from the given name.  It will be placed into a single
     * string value in the object.
     * 
     * @param sOrgName The name of the organization
     * @return The HL7 ON object that is returned.
     */
    private ONExplicit createON(String sOrgName) {
        ONExplicit oHL7On = new ONExplicit();
        boolean bHaveData = false;

        if (sOrgName != null) {
            oHL7On.getContent().add(sOrgName);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7On;
        } else {
            return null;
        }
    }

    /**
     * This creates an HL7 organization object with the given data.
     *
     * @param sIdRoot The root attribute for the ID tag.
     * @param sIdExtension The extension attribute for the ID tag.
     * @param sOrgName The name of the organization.
     * @param oAddress The address of the orgnization.
     * @return
     */
    private POCDMT000040Organization createOrganization(String sIdRoot, String sIdExtension, String sOrgName, AddressType oAddress) {
        POCDMT000040Organization oHL7Org = new POCDMT000040Organization();
        boolean bHaveData = false;

        II oId = createII(sIdRoot, sIdExtension);
        if (oId != null) {
            oHL7Org.getId().add(oId);
            bHaveData = true;
        }

        ONExplicit oName = createON(sOrgName);
        if (oName != null) {
            oHL7Org.getName().add(oName);
            bHaveData = true;
        }

        ADExplicit oHL7Addr = createAD(oAddress);
        if (oHL7Addr != null) {
            oHL7Org.getAddr().add(oHL7Addr);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7Org;
        } else {
            return null;
        }
    }

    /**
     * Create an HL7 Author from the information in the given author object.
     *
     * @param oAuthor The author information to use when creating the HL7 object.
     * @return The HL7 author object.
     */
    private POCDMT000040Author createAuthorOriginal(PolicyOriginalAuthorInfoType oAuthor) {
        POCDMT000040Author oHL7Author = new POCDMT000040Author();
        boolean bHaveData = false;

        // Template ID
        //-------------
        II oTemplateId = createII(CDAConstants.TEMPLATE_ID_ROOT_AUTHOR_ORIGINAL, null);
        if (oTemplateId != null) {
            oHL7Author.getTemplateId().add(oTemplateId);
            bHaveData = true;
        }
        if (oAuthor != null) {

            // Time
            //------
            TSExplicit oTime = createTS(oAuthor.getAuthorTime());
            if (oTime != null) {
                oHL7Author.setTime(oTime);
                bHaveData = true;
            }

            // Assigned Author
            //----------------
            POCDMT000040AssignedAuthor oAssignedAuthor = new POCDMT000040AssignedAuthor();
            boolean bHaveAssignedAuthorData = false;

            // Author ID
            //----------
            II oAuthorId = createII(oAuthor.getAuthorIdAssigningAuthority(), oAuthor.getAuthorId());
            if (oAuthorId != null) {
                oAssignedAuthor.getId().add(oAuthorId);
                bHaveAssignedAuthorData = true;
            }

            // Name
            //-----
            PNExplicit oName = createPN(oAuthor.getName());
            if (oName != null) {
                POCDMT000040Person oAssignedPerson = new POCDMT000040Person();
                oAssignedAuthor.setAssignedPerson(oAssignedPerson);
                oAssignedPerson.getName().add(oName);
                bHaveAssignedAuthorData = true;
            }

            // Represented Organization...
            //-----------------------------
            POCDMT000040Organization oRepOrg = createOrganization(oAuthor.getRepresentedOrganizationIdAssigningAuthority(),
                    oAuthor.getRepresentedOrganizationId(),
                    oAuthor.getRepresentedOrganizationName(),
                    null);
            if (oRepOrg != null) {
                oAssignedAuthor.setRepresentedOrganization(oRepOrg);
                bHaveAssignedAuthorData = true;
            }

            if (bHaveAssignedAuthorData) {
                oHL7Author.setAssignedAuthor(oAssignedAuthor);
                bHaveData = true;
            }
        }
        if (bHaveData) {
            return oHL7Author;
        } else {
            return null;
        }
    }

    /**
     * Create an SC type with the given value.
     *
     * @param sValue The string value to place in the SC
     * @return The HL7 SC object containing the value.
     */
    private SCExplicit creteSC(String sValue) {
        SCExplicit oHL7Sc = new SCExplicit();
        boolean bHaveData = false;

        if (sValue != null) {
            oHL7Sc.getContent().add(sValue);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7Sc;
        } else {
            return null;
        }

    }

    /**
     * Create an HL7 Authoring device node with the given data.
     *
     * @param oAuthoringDeviceCode The authroing device coded data - identifies the device.
     * @param sDeviceManufactureModelName The device manufacutre model and name.
     * @param sDeviceSoftwareName The device software name and version.
     * @return
     */
    private POCDMT000040AuthoringDevice createAuthoringDevice(CeType oAuthoringDeviceCode, String sDeviceManufactureModelName, String sDeviceSoftwareName) {
        POCDMT000040AuthoringDevice oHL7AuthoringDevice = new POCDMT000040AuthoringDevice();
        boolean bHaveData = false;

        // Code
        //------
        CE oHL7AuthoringDeviceCode = createCode(oAuthoringDeviceCode);
        if (oHL7AuthoringDeviceCode != null) {
            oHL7AuthoringDevice.setCode(oHL7AuthoringDeviceCode);
            bHaveData = true;
        }

        // Manufacture Model Name
        //------------------------
        SCExplicit oManufacturerModelName = creteSC(sDeviceManufactureModelName);
        if (oManufacturerModelName != null) {
            oHL7AuthoringDevice.setManufacturerModelName(oManufacturerModelName);
            bHaveData = true;
        }

        // Software Name
        //---------------
        SCExplicit oSoftwareName = creteSC(sDeviceSoftwareName);
        if (oSoftwareName != null) {
            oHL7AuthoringDevice.setSoftwareName(oSoftwareName);
            bHaveData = true;
        }

        if (bHaveData) {
            return oHL7AuthoringDevice;
        } else {
            return null;
        }

    }

    /**
     * Create an HL7 Author (Scanner) from the information in the given author object.
     *
     * @param oAuthor The author information to use when creating the HL7 object.
     * @return The HL7 author object.
     */
    private POCDMT000040Author createAuthorScanner(PolicyScannerAuthorInfoType oAuthor) {
        POCDMT000040Author oHL7Author = new POCDMT000040Author();
        boolean bHaveData = false;

        // Template ID
        //-------------
        II oTemplateId = createII(CDAConstants.TEMPLATE_ID_ROOT_AUTHOR_SCANNER, null);
        if (oTemplateId != null) {
            oHL7Author.getTemplateId().add(oTemplateId);
            bHaveData = true;
        }

        if (oAuthor != null) {
            // Time
            //------
            TSExplicit oTime = createTS(oAuthor.getAuthorTime());
            if (oTime != null) {
                oHL7Author.setTime(oTime);
                bHaveData = true;
            }

            // Assigned Author
            //----------------
            POCDMT000040AssignedAuthor oAssignedAuthor = new POCDMT000040AssignedAuthor();
            boolean bHaveAssignedAuthorData = false;

            // Author ID
            //----------
            II oAuthorId = createII(oAuthor.getAuthorIdAssigningAuthority(), oAuthor.getAuthorId());
            if (oAuthorId != null) {
                oAssignedAuthor.getId().add(oAuthorId);
                bHaveAssignedAuthorData = true;
            }

            // Assigned Authoring Device
            //---------------------------
            POCDMT000040AuthoringDevice oAuthoringDevice = createAuthoringDevice(oAuthor.getAuthoringDevice(), oAuthor.getDeviceManufactureModelName(), oAuthor.getDeviceSoftwareName());
            if (oAuthoringDevice != null) {
                oAssignedAuthor.setAssignedAuthoringDevice(oAuthoringDevice);
                bHaveAssignedAuthorData = true;
            }

            // Represented Organization...
            //-----------------------------
            POCDMT000040Organization oRepOrg = createOrganization(oAuthor.getRepresentedOrganizationIdAssigningAuthority(),
                    null,
                    oAuthor.getRepresentedOrganizationName(),
                    oAuthor.getRepresentedOrganizationAddress());
            if (oRepOrg != null) {
                oAssignedAuthor.setRepresentedOrganization(oRepOrg);
                bHaveAssignedAuthorData = true;
            }

            if (bHaveAssignedAuthorData) {
                oHL7Author.setAssignedAuthor(oAssignedAuthor);
                bHaveData = true;
            }
        }
        if (bHaveData) {
            return oHL7Author;
        } else {
            return null;
        }
    }

    /**
     * Create the HL7 data enterer information from the given data.
     *
     * @param oDataEnterer The data enterer information.
     * @return The HL7 Data enterer information.
     */
    private POCDMT000040DataEnterer createDataEnterer(PolicyDataEntererInfoType oDataEnterer) {
        POCDMT000040DataEnterer oHL7DataEnterer = new POCDMT000040DataEnterer();
        boolean bHaveData = false;

        // Template ID  - If the template ID is the only thing, we consider this object null...
        //--------------------------------------------------------------------------------------
        II oTemplateId = createII(CDAConstants.TEMPLATE_ID_ROOT_DATA_ENTERER, null);
        if (oTemplateId != null) {
            oHL7DataEnterer.getTemplateId().add(oTemplateId);
        }
        if (oDataEnterer != null) {
            // Time
            //------
            TSExplicit oTime = createTS(oDataEnterer.getDataEntererTime());
            if (oTime != null) {
                oHL7DataEnterer.setTime(oTime);
                bHaveData = true;
            }

            POCDMT000040AssignedEntity oAssignedEntity = new POCDMT000040AssignedEntity();
            boolean bHaveAssignedEntityData = false;

            // Assigned Entity ID
            //--------------------
            II oId = createII(oDataEnterer.getDataEntererIdAssigningAuthority(), oDataEnterer.getDataEntererId());
            if (oId != null) {
                oAssignedEntity.getId().add(oId);
                bHaveAssignedEntityData = true;
            }

            // Assigned Person/Name
            //----------------------
            PNExplicit oName = createPN(oDataEnterer.getName());
            if (oName != null) {
                oAssignedEntity.setAssignedPerson(new POCDMT000040Person());
                oAssignedEntity.getAssignedPerson().getName().add(oName);
                bHaveAssignedEntityData = true;
            }

            if (bHaveAssignedEntityData) {
                oHL7DataEnterer.setAssignedEntity(oAssignedEntity);
                bHaveData = true;
            }
        }
        if (bHaveData) {
            return oHL7DataEnterer;
        } else {
            return null;
        }

    }

    /**
     * Create the HL7 custodian from the given data.
     *
     * @param oCustodian The data to be transformed.
     * @return The HL7 custodian object,
     */
    private POCDMT000040Custodian createCustodian(PolicyCustodianInfoType oCustodian) {
        POCDMT000040Custodian oHL7Custodian = new POCDMT000040Custodian();
        boolean bHaveData = false;

        oHL7Custodian.setAssignedCustodian(new POCDMT000040AssignedCustodian());
        POCDMT000040CustodianOrganization oOrg = new POCDMT000040CustodianOrganization();
        oHL7Custodian.getAssignedCustodian().setRepresentedCustodianOrganization(oOrg);

        if (oCustodian != null) {
            // Id
            //----
            II oId = createII(oCustodian.getOrganizationIdAssigningAuthority(), oCustodian.getOrganizationId());
            if (oId != null) {
                oOrg.getId().add(oId);
                bHaveData = true;
            }

            // Name
            //-----
            ONExplicit oName = createON(oCustodian.getOrganizationName());
            if (oName != null) {
                oOrg.setName(oName);
                bHaveData = true;
            }

            // Address
            //--------
            ADExplicit oAddr = createAD(oCustodian.getOrganizationAddress());
            if (oAddr != null) {
                oOrg.setAddr(oAddr);
                bHaveData = true;
            }
        }
        if (bHaveData) {
            return oHL7Custodian;
        } else {
            return null;
        }
    }

    /**
     * Create an HL7 legal authenticator object from the given data.
     *
     * @param oLegalAuthenticator The legal authenticator data to be placd in the HL7 object.
     * @return The HL7 legal authenticator object to be returned.
     */
    private POCDMT000040LegalAuthenticator createLegalAuthenticator(PolicyLegalAuthenticatorType oLegalAuthenticator) {
        POCDMT000040LegalAuthenticator oHL7Auth = new POCDMT000040LegalAuthenticator();
        boolean bHaveData = false;

        if (oLegalAuthenticator != null) {
            // Time
            //------
            TSExplicit oTime = createTS(oLegalAuthenticator.getAuthenticationTime());
            if (oTime != null) {
                oHL7Auth.setTime(oTime);
                bHaveData = true;
            }

            // Signature code
            //---------------
            CS oSignatureCode = createCS(oLegalAuthenticator.getSignatureCode());
            if (oSignatureCode != null) {
                oHL7Auth.setSignatureCode(oSignatureCode);
                bHaveData = true;
            }

            POCDMT000040AssignedEntity oAssignedEntity = new POCDMT000040AssignedEntity();
            boolean bHaveAssignedEntityData = false;

            // Assigned Entity ID
            //--------------------
            II oId = createII(oLegalAuthenticator.getAuthenticatorIdAssigningAuthority(), oLegalAuthenticator.getAuthenticatorId());
            if (oId != null) {
                oAssignedEntity.getId().add(oId);
                bHaveAssignedEntityData = true;
            }

            // Assigned Person/Name
            //----------------------
            PNExplicit oName = createPN(oLegalAuthenticator.getAuthenticatorPersonName());
            if (oName != null) {
                oAssignedEntity.setAssignedPerson(new POCDMT000040Person());
                oAssignedEntity.getAssignedPerson().getName().add(oName);
                bHaveAssignedEntityData = true;
            }

            if (bHaveAssignedEntityData) {
                oHL7Auth.setAssignedEntity(oAssignedEntity);
                bHaveData = true;
            }
        }
        if (bHaveData) {
            return oHL7Auth;
        } else {
            return null;
        }
    }

    /**
     * Create a time interval based on the given low and high date.
     *
     * @param sLowHL7Date The lower date of the range.
     * @param sHighHL7Date The higher date of the range.
     * @return The time interval that has been created.
     */
    private IVLTSExplicit createIVLTS(String sLowHL7Date, String sHighHL7Date) {
        IVLTSExplicit oTimeInterval = new IVLTSExplicit();
        boolean bHaveData = false;
        org.hl7.v3.ObjectFactory oFactory = new ObjectFactory();

        TSExplicit oTSLowDate = createTS(sLowHL7Date);
        if ((oTSLowDate != null) &&
                (oTSLowDate.getValue() != null) &&
                (oTSLowDate.getValue().length() > 0)) {
            IVXBTSExplicit oLowDateElement = new IVXBTSExplicit();
            oLowDateElement.setValue(oTSLowDate.getValue());
            JAXBElement<IVXBTSExplicit> oLowDateJaxbElement = oFactory.createIVLTSExplicitLow(oLowDateElement);
            oTimeInterval.getContent().add(oLowDateJaxbElement);
            bHaveData = true;
        }

        TSExplicit oTSHighDate = createTS(sHighHL7Date);
        if ((oTSHighDate != null) &&
                (oTSHighDate.getValue() != null) &&
                (oTSHighDate.getValue().length() > 0)) {
            IVXBTSExplicit oHighDateElement = new IVXBTSExplicit();
            oHighDateElement.setValue(oTSHighDate.getValue());
            JAXBElement<IVXBTSExplicit> oHighDateJaxbElement = oFactory.createIVLTSExplicitHigh(oHighDateElement);
            oTimeInterval.getContent().add(oHighDateJaxbElement);
            bHaveData = true;
        }

        if (bHaveData) {
            return oTimeInterval;
        } else {
            return null;
        }

    }

    /**
     * Create the DocumentationOf tag with the given data.
     * 
     * @param oCriterion The object containing the data to be in the documentation of tag.
     * @return The HL7 documentation of object that was created.
     */
    private POCDMT000040DocumentationOf createDocumentationOf(BinaryDocumentPolicyCriterionType oCriterion) {
        POCDMT000040DocumentationOf oDocOf = new POCDMT000040DocumentationOf();

        oDocOf.getTypeCode().add(CDAConstants.DOCUMENTATION_OF_TYPE_CODE);

        POCDMT000040ServiceEvent oServiceEvent = new POCDMT000040ServiceEvent();
        oDocOf.setServiceEvent(oServiceEvent);
        oServiceEvent.getMoodCode().add(CDAConstants.SERVICE_EVENT_MOOD_CODE_EVENT);
        oServiceEvent.getClassCode().add(CDAConstants.SERVICE_EVENT_CLASS_CODE_ACT);

        II oTemplateId = createII(CDAConstants.SERVICE_EVENT_TEMPLATE_ID_ROOT, null);
        oServiceEvent.getTemplateId().add(oTemplateId);

        CE oHL7ConsentCode = new CE();
        oHL7ConsentCode.setCode(CDAConstants.CONSENT_CODE_YES);
        oHL7ConsentCode.setDisplayName(CDAConstants.CONSENT_CODE_YES_DISPLAY_NAME);
        oHL7ConsentCode.setCodeSystem(CDAConstants.SNOMED_CT_CODE_SYSTEM);
        oHL7ConsentCode.setCodeSystemName(CDAConstants.SNOMED_CT_CODE_SYSTEM_DISPLAY_NAME);
        oServiceEvent.setCode(oHL7ConsentCode);

        IVLTSExplicit oTimeInterval = createIVLTS(oCriterion.getStartDate(), oCriterion.getEndDate());
        oServiceEvent.setEffectiveTime(oTimeInterval);

        return oDocOf;      // Always return one of these - even if it is only with our default values.

    }

    /**
     * Create the component tag.
     *
     * @param oCriterion The information to be used in the object.
     * @return The component object that was created.
     */
    private POCDMT000040Component2 createComponent(BinaryDocumentPolicyCriterionType oCriterion) {
        POCDMT000040Component2 oComponent = new POCDMT000040Component2();
        boolean bHaveData = false;

        POCDMT000040NonXMLBody oNonXMLBody = new POCDMT000040NonXMLBody();
        oComponent.setNonXMLBody(oNonXMLBody);

        EDExplicit oED = new EDExplicit();
        oNonXMLBody.setText(oED);
        oED.setRepresentation(BinaryDataEncoding.B_64);

        // MediaType
        //-----------
        if (oCriterion.getMimeType() != null) {
            oED.setMediaType(oCriterion.getMimeType());
            bHaveData = true;
        }

        // Content
        //--------
        if ((oCriterion.getBinaryDocument() != null) &&
                (oCriterion.getBinaryDocument().length > 0)) {
            String sBinaryDocument = new String(oCriterion.getBinaryDocument());
            oED.getContent().add(sBinaryDocument);
            bHaveData = true;
        }

        if (bHaveData) {
            return oComponent;
        } else {
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
            throws AdapterPIPException {
        POCDMT000040ClinicalDocument oCda = new POCDMT000040ClinicalDocument();
        boolean bHaveData = false;

        if (oCriterion != null) {
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

            // Author (Original)
            //------------------
            oCda.getAuthor().add(createAuthorOriginal(oCriterion.getAuthorOriginal()));

            // Author (Scanner)
            //------------------
            oCda.getAuthor().add(createAuthorScanner(oCriterion.getAuthorScanner()));

            // Data Enterer
            //--------------
            oCda.setDataEnterer(createDataEnterer(oCriterion.getDataEnterer()));

            // Custodian
            //----------
            oCda.setCustodian(createCustodian(oCriterion.getCustodian()));

            // Legal Authenticator
            //---------------------
            oCda.setLegalAuthenticator(createLegalAuthenticator(oCriterion.getLegalAuthenticator()));

            // Documentation Of
            //------------------
            oCda.getDocumentationOf().add(createDocumentationOf(oCriterion));

            // Component
            //----------
            oCda.setComponent(createComponent(oCriterion));

        }   // if (oCriterion != null)

        if (bHaveData) {
            return oCda;
        } else {
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
            throws AdapterPIPException {
        ArrayList<POCDMT000040ClinicalDocument> olCda = new ArrayList<POCDMT000040ClinicalDocument>();

        if ((oPtPref != null) &&
                (oPtPref.getBinaryDocumentPolicyCriteria() != null) &&
                (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null) &&
                (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().size() > 0)) {
            for (BinaryDocumentPolicyCriterionType oCriterion : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion()) {
                POCDMT000040ClinicalDocument oCda = createCDA(oPtPref, oCriterion);
                if (oCda != null) {
                    olCda.add(oCda);
                }
            }
        }


        if (olCda.size() > 0) {
            return olCda;
        } else {
            return null;
        }
    }
}
