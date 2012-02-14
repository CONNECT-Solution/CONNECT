package gov.hhs.fha.nhinc.gateway.util;

import java.util.Properties;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthnStatementType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Constructs a SAML AssertionType for testing This class was taken from the connect universal client
 * 
 * @author paul
 */
public class AssertionCreator {

    private static final String PROPERTY_FILE_NAME = "/gov/hhs/fha/nhinc/gateway/util/assertion.properties";
    private static final String PROPERTY_KEY_PURPOSE_CODE = "AssertionPurposeCode";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM = "AssertionPurposeSystem";
    private static final String PROPERTY_KEY_PURPOSE_SYSTEM_NAME = "AssertionPurposeSystemName";
    private static final String PROPERTY_KEY_PURPOSE_DISPLAY = "AssertionPurposeDisplay";
    private static final String PROPERTY_KEY_USER_FIRST = "AssertionUserFirstName";
    private static final String PROPERTY_KEY_USER_MIDDLE = "AssertionUserMiddleName";
    private static final String PROPERTY_KEY_USER_LAST = "AssertionUserLastName";
    private static final String PROPERTY_KEY_USER_NAME = "AssertionUserName";
    private static final String PROPERTY_KEY_USER_ORG = "AssertionUserOrganization";
    private static final String PROPERTY_KEY_USER_CODE = "AssertionUserCode";
    private static final String PROPERTY_KEY_USER_SYSTEM = "AssertionUserSystem";
    private static final String PROPERTY_KEY_USER_SYSTEM_NAME = "AssertionUserSystemName";
    private static final String PROPERTY_KEY_USER_DISPLAY = "AssertionUserDisplay";
    private static final String PROPERTY_KEY_EXPIRE = "AssertionExpiration";
    private static final String PROPERTY_KEY_SIGN = "AssertionSignDate";
    private static final String PROPERTY_KEY_CONTENT_REF = "AssertionContentRef";
    private static final String PROPERTY_KEY_CONTENT_FILENAME = "AssertionContentFile";
    private static Log log = LogFactory.getLog(AssertionCreator.class);

    public AssertionType createAssertion() {

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
        assertOut.setHomeCommunity(userHc);

        SamlAuthnStatementType auth = new SamlAuthnStatementType();
        assertOut.setSamlAuthnStatement(auth);

        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream(PROPERTY_FILE_NAME));
            userPerson.setGivenName(props.getProperty(PROPERTY_KEY_USER_FIRST));
            userPerson.setFamilyName(props.getProperty(PROPERTY_KEY_USER_LAST));
            userPerson.setSecondNameOrInitials(props.getProperty(PROPERTY_KEY_USER_MIDDLE));
            userHc.setName(props.getProperty(PROPERTY_KEY_USER_ORG));
            userHc.setHomeCommunityId("1.1");
            user.setUserName(props.getProperty(PROPERTY_KEY_USER_NAME));
            userRole.setCode(props.getProperty(PROPERTY_KEY_USER_CODE));
            userRole.setCodeSystem(props.getProperty(PROPERTY_KEY_USER_SYSTEM));
            userRole.setCodeSystemName(props.getProperty(PROPERTY_KEY_USER_SYSTEM_NAME));
            userRole.setDisplayName(props.getProperty(PROPERTY_KEY_USER_DISPLAY));

            purposeCoded.setCode(props.getProperty(PROPERTY_KEY_PURPOSE_CODE));
            purposeCoded.setCodeSystem(props.getProperty(PROPERTY_KEY_PURPOSE_SYSTEM));
            purposeCoded.setCodeSystemName(props.getProperty(PROPERTY_KEY_PURPOSE_SYSTEM_NAME));
            purposeCoded.setDisplayName(props.getProperty(PROPERTY_KEY_PURPOSE_DISPLAY));

            /*
             * assertOut.setDateOfSignature(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_SIGN));
             * assertOut.setExpirationDate(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_EXPIRE));
             * assertOut.setClaimFormRef(PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_CONTENT_REF));
             * 
             * byte[] binData = getBinaryClaimForm(); if (binData != null && binData.length > 0) {
             * assertOut.setClaimFormRaw(binData); }
             */
        } catch (IOException iex) {
            ExceptionDump.outputCompleteException(iex);
        } catch (Exception ex) {
            ExceptionDump.outputCompleteException(ex);
        }
        return assertOut;
    }

    private byte[] getBinaryClaimForm() {
        log.debug("AssertionCreator.getBinaryClaimForm() -- Begin");

        String binFileName = null;
        FileInputStream in = null;
        byte[] binOut = null;
        try {
            String propDir = PropertyAccessor.getPropertyFileLocation();
            binFileName = propDir + PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_KEY_CONTENT_FILENAME);
            System.out.println("Requesting file: " + binFileName);
            File inFile = new File(binFileName);
            if (inFile.exists()) {
                long fileLen = inFile.length();
                if (fileLen <= Integer.MAX_VALUE && fileLen > 0) {
                    binOut = new byte[(int) fileLen];

                    in = new FileInputStream(binFileName);
                    // Read in the bytes
                    int offset = 0;
                    int numRead = 0;
                    while (offset < binOut.length && (numRead = in.read(binOut, offset, binOut.length - offset)) >= 0) {
                        offset += numRead;
                    }
                    // Ensure all the bytes have been read in
                    if (offset < binOut.length) {
                        log.error("AssertionCreator: Could only read " + offset + " of " + binOut.length
                                + " bytes of file " + binFileName);
                    } else {
                        log.debug("Binary A27 form read: " + binOut.length + " bytes");
                    }
                } else {
                    log.error(binFileName + " file is too long to read");
                }
            } else {
                log.error("File " + binFileName + " containing Binary A27 form does not exist");
            }
        } catch (IOException ex) {
            log.error("AssertionCreator " + ex.getMessage());
        } catch (PropertyAccessException ex) {
            log.error("AssertionCreator " + ex.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException iOException) {
                log.error("AssertionCreator " + iOException.getMessage());
            }
        }

        log.debug("AssertionCreator.getBinaryClaimForm() -- End");
        return binOut;
    }

}
