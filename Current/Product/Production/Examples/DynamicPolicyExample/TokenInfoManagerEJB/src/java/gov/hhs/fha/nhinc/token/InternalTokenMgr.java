package gov.hhs.fha.nhinc.token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;

/**
 * This class is used to store and retrieve the information for the Saml token 
 * from a file
 * 
 * @author Victoria Vickers
 * @author Neil Webb
 */
public class InternalTokenMgr
{
    private static Log log = LogFactory.getLog(InternalTokenMgr.class);
    public static final String propFileName = "token";
    public static final String commonNameAttrName = "CommonName";
    public static final String orgUnitAttrName = "OrganizationalUnit";
    public static final String otherNameAttrName = "OtherName";
    public static final String locationCityAttrName = "LocationCity";
    public static final String locationStateAttrName = "LocationState";
    public static final String locationCountryAttrName = "LocationCountry";
    public static final String userIdAttrName = "UserId";
    public static final String userNameAttrName = "UserName";
    public static final String userFirstNameAttrName = "UserFirstName";
    public static final String userMiddleNameAttrName = "UserMiddleName";
    public static final String userLastNameAttrName = "UserLastName";
    public static final String userOrgAttrName = "UserOrganization";
    public static final String userRoleCodeAttrName = "UserRoleCode";
    public static final String userRoleCodeSystemAttrName = "UserRoleCodeSystem";
    public static final String userRoleCodeSystemNameAttrName = "UserRoleCodeSystemName";
    public static final String userRoleDisplayAttrName = "UserRoleDisplayName";
    public static final String purposeCodeAttrName = "PurposeForUseRoleCode";
    public static final String purposeCodeSystemAttrName = "PurposeForUseCodeSystem";
    public static final String purposeCodeSystemNameAttrName = "PurposeForUseCodeSystemName";
    public static final String purposeDisplayAttrName = "PurposeForUseDisplayName";
    public static final String actionAttrName = "Action";
    public static final String resourceAttrName = "Resource";
    public static final String signDateAttrName = "SignDate";
    public static final String expireDateAttrName = "ExpirationDate";
    public static final String claimRefAttrName = "ContentReference";
    public static final String claimFormTypeAttrName = "ContentType";
    public static final String claimFormAttrName = "Content";
    public static final String storeFileName = "tokenAttrStoreFile";
    public static final String dumpFileName = "tokenAttrDumpFile";

    /**
     * This method accesses the file specified to hold the information extracted 
     * from the SAML Token and using the key / value properties therein creates 
     * a new Assertion object.
     * @return The new Assertion object containing the extracted token information
     */
    AssertionType retrieveInfoOperation()
    {
        log.debug("Enter retrieveInfoOperation");

        AssertionType assertOut = new AssertionType();
        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertOut.setUserInfo(user);
        assertOut.setPurposeOfDisclosureCoded(purposeCoded);

        BufferedReader reader = null;
        String fileName = null;
        try
        {
            PropertyResourceBundle prop = (PropertyResourceBundle) PropertyResourceBundle.getBundle(propFileName);
            fileName = prop.getString(dumpFileName);

            reader = new BufferedReader(new FileReader(fileName));

            Properties storedProps = new Properties();
            storedProps.load(reader);

            userPerson.setGivenName(storedProps.getProperty(userFirstNameAttrName));
            userPerson.setFamilyName(storedProps.getProperty(userLastNameAttrName));
            userPerson.setSecondNameOrInitials(storedProps.getProperty(userMiddleNameAttrName));
            userHc.setName(storedProps.getProperty(userOrgAttrName));
            user.setUserName(storedProps.getProperty(userNameAttrName));
            userRole.setCode(storedProps.getProperty(userRoleCodeAttrName));
            userRole.setCodeSystem(storedProps.getProperty(userRoleCodeSystemAttrName));
            userRole.setCodeSystemName(storedProps.getProperty(userRoleCodeSystemNameAttrName));
            userRole.setDisplayName(storedProps.getProperty(userRoleDisplayAttrName));

            purposeCoded.setCode(storedProps.getProperty(purposeCodeAttrName));
            purposeCoded.setCodeSystem(storedProps.getProperty(purposeCodeSystemAttrName));
            purposeCoded.setCodeSystemName(storedProps.getProperty(purposeCodeSystemNameAttrName));
            purposeCoded.setDisplayName(storedProps.getProperty(purposeDisplayAttrName));

            assertOut.setDateOfSignature(storedProps.getProperty(signDateAttrName));
            assertOut.setExpirationDate(storedProps.getProperty(expireDateAttrName));
            assertOut.setClaimFormRef(storedProps.getProperty(claimRefAttrName));

            String strForm = storedProps.getProperty(claimFormAttrName);
            if (strForm != null && !strForm.isEmpty())
            {
                byte[] formRaw = strForm.getBytes();
                assertOut.setClaimFormRaw(formRaw);
            }

        }
        catch (IOException ex)
        {
            log.error("retrieveInfoOperation " + ex.getMessage());
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                }
            }
            catch (IOException iOException)
            {
                log.error("retrieveInfoOperation " + iOException.getMessage());
            }
        }
        log.debug("Exit retrieveInfoOperation");
        return assertOut;
    }

    /**
     * This method creates the file specified to hold the Assertion information
     * for use in creating the SAML Token, and stores all available information 
     * as key / value pairs.
     * @param assertIn The Assertion object containing assertion information, 
     * user information, and home community information
     * @param actionName The action associated with the desired operation is set 
     * by the bpel and is defined to be one of: subjectDiscovery, 
     * retrieveDocuments, queryDocuments, queryAuditLog, notify, subscribe, or 
     * unsubscribe
     * @param resourceURI The URI to the service endpoint being invoked as set 
     * by the bpel
     */
    void storeInfoOperation(AssertionType assertIn, String actionName, String resourceURI)
    {
        log.debug("InternalTokenMgr.storeInfoOperation() -- Begin");
        RandomAccessFile raFile = null;
        PrintWriter writeOut = null;

        try
        {
            PropertyResourceBundle prop = (PropertyResourceBundle) PropertyResourceBundle.getBundle(propFileName);
            String fileName = prop.getString(storeFileName);

            raFile = new RandomAccessFile(fileName, "rw");
            raFile.setLength(0);
            log.debug("Create: " + fileName);

            //do writing
            writeOut = new PrintWriter(new FileWriter(fileName));
            if (actionName != null && !actionName.isEmpty())
            {
                writeOut.println(actionAttrName + "=" + actionName);
            }

            if (resourceURI != null && !resourceURI.isEmpty())
            {
                writeOut.println(resourceAttrName + "=" + resourceURI);
            }
            if (assertIn != null)
            {
                String purposeCode = "=TREATMENT";
                String purposeCodeSystem = "=2.16.840.1.113883.3.18.7.1";
                String purposeCodeSystemName = "=nhin-purpose";
                String purposeDisplay = "=" + assertIn.getPurposeOfDisclosure();
                if (assertIn.getPurposeOfDisclosureCoded() != null)
                {
                    purposeCode = "=" + assertIn.getPurposeOfDisclosureCoded().getCode();
                    purposeCodeSystem = "=" + assertIn.getPurposeOfDisclosureCoded().getCodeSystem();
                    purposeCodeSystemName = "=" + assertIn.getPurposeOfDisclosureCoded().getCodeSystemName();
                    purposeDisplay = "=" + assertIn.getPurposeOfDisclosureCoded().getDisplayName();
                }
                else
                {
                    log.warn("InternalTokenMgr.storeInfoOperation assertion.PurposeOfDisclosureCoded is null - PurposeOfDisclosure element has been deprecated");
                }
                writeOut.println(purposeCodeAttrName + purposeCode);
                writeOut.println(purposeCodeSystemAttrName + purposeCodeSystem);
                writeOut.println(purposeCodeSystemNameAttrName + purposeCodeSystemName);
                writeOut.println(purposeDisplayAttrName + purposeDisplay);

                if (assertIn.getUserInfo() != null)
                {
                    if (assertIn.getUserInfo().getPersonName() != null)
                    {
                        writeOut.println(userFirstNameAttrName + "=" + assertIn.getUserInfo().getPersonName().getGivenName());
                        writeOut.println(userMiddleNameAttrName + "=" + assertIn.getUserInfo().getPersonName().getSecondNameOrInitials());
                        writeOut.println(userLastNameAttrName + "=" + assertIn.getUserInfo().getPersonName().getFamilyName());
                    }
                    writeOut.println(userNameAttrName + "=" + assertIn.getUserInfo().getUserName());
                    if (assertIn.getUserInfo().getOrg() != null)
                    {
                        writeOut.println(userOrgAttrName + "=" + assertIn.getUserInfo().getOrg().getName());
                    }

                    String userCode = "=112247003";
                    String userCodeSystem = "=2.16.840.1.113883.6.96";
                    String userCodeSystemName = "=SNOMED_CT";
                    String userDisplay = "=" + assertIn.getUserInfo().getRole();
                    if (assertIn.getUserInfo().getRoleCoded() != null)
                    {
                        userCode = "=" + assertIn.getUserInfo().getRoleCoded().getCode();
                        userCodeSystem = "=" + assertIn.getUserInfo().getRoleCoded().getCodeSystem();
                        userCodeSystemName = "=" + assertIn.getUserInfo().getRoleCoded().getCodeSystemName();
                        userDisplay = "=" + assertIn.getUserInfo().getRoleCoded().getDisplayName();
                    }
                    else
                    {
                        log.warn("InternalTokenMgr.storeInfoOperation assertion.userInfo.RoleCoded is null - User role element has been deprecated");
                    }
                    writeOut.println(userRoleCodeAttrName + userCode);
                    writeOut.println(userRoleCodeSystemAttrName + userCodeSystem);
                    writeOut.println(userRoleCodeSystemNameAttrName + userCodeSystemName);
                    writeOut.println(userRoleDisplayAttrName + userDisplay);
                }
                else
                {
                    log.info("InternalTokenMgr.storeInfoOperation assertion.user is null - No assertion.user data stored");
                }

                // For use in the Evidence
                writeOut.println(expireDateAttrName + "=" + assertIn.getExpirationDate());
                writeOut.println(signDateAttrName + "=" + assertIn.getDateOfSignature());
                writeOut.println(claimRefAttrName + "=" + assertIn.getClaimFormRef());

                String strForm = "";
                byte[] rawForm = assertIn.getClaimFormRaw();
                if (rawForm != null && rawForm.length > 0)
                {
                    strForm = new String(rawForm);
                }
                writeOut.println(claimFormAttrName + "=" + strForm);

            }
            else
            {
                log.info("InternalTokenMgr.storeInfoOperation assertion input parameter is null - No assertion data stored");
            }
        }
        catch (IOException ex)
        {
            log.error("storeInfoOperation " + ex.getMessage());
        } //File closure guaranteed in a finally
        finally
        {
            try
            {
                if (raFile != null)
                {
                    raFile.close();
                }
                if (writeOut != null)
                {
                    writeOut.close();
                }
            }
            catch (IOException iOException)
            {
                log.error("storeInfoOperation " + iOException.getMessage());
            }
        }
        log.debug("InternalTokenMgr.storeInfoOperation() -- End");
    }
}
