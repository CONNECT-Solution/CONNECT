package gov.hhs.fha.nhinc.fta;

import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.*;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import javax.xml.bind.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.xml.sax.InputSource;
import java.io.StringReader;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author dunnek
 */
public class Util {

    private static Log log = LogFactory.getLog(Util.class);
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String DATE_FORMAT_NOW = "MM/dd/yyyy HH:mm:ss";

    public static String unmarshalPayload(org.w3c.dom.Element element) {
        Payload payload;
        String result = "";

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(Payload.class);
            Unmarshaller um = jc.createUnmarshaller();

            payload = (Payload) um.unmarshal(element);

            result = Util.convertToString(payload.payload);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static org.w3c.dom.Element marshalPayload(String contents) {
        org.w3c.dom.Document doc = null;

        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(Payload.class);
            Marshaller marshaller = jc.createMarshaller();
            Payload payload = new Payload();
            javax.xml.parsers.DocumentBuilderFactory dbf;

            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().newDocument();

            payload.payload = (Util.convertToByte(contents));

            marshaller.marshal(payload, doc);
            log.info(doc.getNodeValue());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return doc.getDocumentElement();

    }

    public static org.w3c.dom.Element marshalTopic(String topic, String dialect) {
        org.w3c.dom.Element element = null;

        try {
            TopicExpressionType topicType = new TopicExpressionType();

            topicType.setDialect(dialect);
            topicType.getContent().add(topic);

            TopicMarshaller marshaller = new TopicMarshaller();

            element = marshaller.marshal(topicType);

            log.info(element.getTextContent());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return element;

    }

    public static TopicExpressionType unmarshalTopic(String contents) {
        org.w3c.dom.Document doc = null;
        TopicExpressionType topic = null;
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext(TopicExpressionType.class);
            Unmarshaller um = jc.createUnmarshaller();
            javax.xml.parsers.DocumentBuilderFactory dbf;

            dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(contents)));

            log.info(doc.getDocumentElement());
            log.info(doc.getDocumentElement().getAttributeNode("Dialect").getNodeValue());

            topic = (TopicExpressionType) um.unmarshal(doc.getDocumentElement());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return topic;

    }

    public static AssertionType createAssertion() {
        AssertionType result = new AssertionType();

        if(result.getSamlAuthzDecisionStatement() == null)
        {
            SamlAuthzDecisionStatementType samlAuthDecisionStatement = new SamlAuthzDecisionStatementType();
            result.setSamlAuthzDecisionStatement(samlAuthDecisionStatement);
        }
        if(result.getSamlAuthzDecisionStatement().getEvidence() == null)
        {
            SamlAuthzDecisionStatementEvidenceType samlEvedence = new SamlAuthzDecisionStatementEvidenceType();
            result.getSamlAuthzDecisionStatement().setEvidence(samlEvedence);
        }
        if(result.getSamlAuthzDecisionStatement().getEvidence().getAssertion() == null)
        {
            SamlAuthzDecisionStatementEvidenceAssertionType samlAssertionType = new SamlAuthzDecisionStatementEvidenceAssertionType();
            result.getSamlAuthzDecisionStatement().getEvidence().setAssertion(samlAssertionType);
        }
        if(result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() == null)
        {
            SamlAuthzDecisionStatementEvidenceConditionsType samlConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
            result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setConditions(samlConditions);
        }

        result.setAuthorized(true);
        result.setDateOfBirth("19800516");
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(now());
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(expirationDate());
        result.setExplanationNonClaimantSignature("non-null");
        result.setHaveSecondWitnessSignature(true);
        result.setHaveSignature(true);
        result.setHaveWitnessSignature(true);
        result.setSecondWitnessAddress(createAddress());
        result.setSecondWitnessName(createPersonName("John", "Q.", "Witness"));
        result.setHomeCommunity(createHomeCommunity());
        result.setPersonName(createPersonName("John", "Q.", "Nhin"));
        result.setPhoneNumber(createPhoneType());
        result.setWitnessPhone(createPhoneType());
        result.setWitnessAddress(createAddress());
        result.setUserInfo(CreateUser());

        result.setSSN("123456789");
        result.setPurposeOfDisclosureCoded(createPurposeCode());
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy("Ref-Clm-123");
        return result;
    }

    private static gov.hhs.fha.nhinc.common.nhinccommon.CeType createPurposeCode() {
        gov.hhs.fha.nhinc.common.nhinccommon.CeType result;
        result = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();

        result.setCode("P");
        result.setCodeSystem("45");
        result.setCodeSystemName("purpose");
        result.setCodeSystemVersion("1.0");
        result.setDisplayName("Purpose");
        result.setOriginalText("P");

        return result;
    }

    private static gov.hhs.fha.nhinc.common.nhinccommon.PhoneType createPhoneType() {
        gov.hhs.fha.nhinc.common.nhinccommon.PhoneType result;
        gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

        type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();

        result = new gov.hhs.fha.nhinc.common.nhinccommon.PhoneType();

        result.setAreaCode("703");
        result.setLocalNumber("555");
        result.setExtension("1234");

        type.setCode("W");
        type.setCodeSystem("50");
        type.setCodeSystemName("phoneNumberType");
        type.setCodeSystemVersion("1.0");
        type.setDisplayName("work");
        type.setOriginalText("W");
        result.setPhoneNumberType(type);

        return result;
    }

    private static gov.hhs.fha.nhinc.common.nhinccommon.AddressType createAddress() {
        gov.hhs.fha.nhinc.common.nhinccommon.AddressType address;
        gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

        type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
        address = new gov.hhs.fha.nhinc.common.nhinccommon.AddressType();

        address.setCity("Hometown");
        address.setCountry("USA");
        address.setState("VA");
        address.setZipCode("12345");
        address.setStreetAddress("123 Main Street");

        type.setCode("W");
        type.setCodeSystem("30");
        type.setCodeSystemName("address");
        type.setCodeSystemVersion("1.0");
        type.setDisplayName("White");
        type.setOriginalText("W");

        address.setAddressType(type);

        return address;
    }

    private static gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType createHomeCommunity() {
        gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType hcType;

        hcType = new gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType();
        hcType.setDescription("Default Community");
        hcType.setHomeCommunityId("1.1");
        hcType.setName("Default Community");

        return hcType;
    }

    private static CeType createRoleCodeType() {
        CeType result = new CeType();

        result.setCode("307969004");
        result.setCodeSystem("2.16.840.1.113883.6.96");
        result.setCodeSystemName("SNOMED_CT");
        result.setCodeSystemVersion("1.0");
        result.setDisplayName("Public Health");
        result.setOriginalText("Public Health");

        return result;
    }

    private static gov.hhs.fha.nhinc.common.nhinccommon.UserType CreateUser() {
        gov.hhs.fha.nhinc.common.nhinccommon.UserType result;

        result = new gov.hhs.fha.nhinc.common.nhinccommon.UserType();


        result.setPersonName(createPersonName("Mark", "Q.", "FRANKLIN"));
        result.setOrg(createHomeCommunity());

        result.setRoleCoded(createRoleCodeType());

        return result;
    }

    private static PersonNameType createPersonName(String fName, String mName, String lName) {
        PersonNameType result = new PersonNameType();
        gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

        type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();

        type.setCode("G");
        type.setCodeSystem("30");
        type.setCodeSystemName("nameType");
        type.setCodeSystemVersion("1.0");
        type.setDisplayName("G");
        type.setOriginalText("G");

        result.setNameType(type);
        result.setFamilyName(lName);
        result.setGivenName(fName);
        result.setSecondNameOrInitials(mName);
        return result;
    }

    private static String expirationDate() {
        String retDate = null;
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            cal.add(Calendar.YEAR, 1);
            DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(cal);
            retDate = xmlDate.toXMLFormat();
        } catch (DatatypeConfigurationException ex) {
            log.error("****** UTIL THROWABLE: " + ex.getMessage(), ex);
        }
        return retDate;
    }

    private static String now() {
        String retDate = null;
        try {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            DatatypeFactory xmlDateFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlDate = xmlDateFactory.newXMLGregorianCalendar(cal);
            retDate = xmlDate.toXMLFormat();
        } catch (DatatypeConfigurationException ex) {
            log.error("****** UTIL THROWABLE: " + ex.getMessage(), ex);
        }
        return retDate;
    }

    public static byte[] convertToByte(String value) {
        byte[] rc = null;

        try {
            rc = value.getBytes("UTF8");
        } catch (Exception ex) {
            log.error("****** UTIL THROWABLE: " + ex.getMessage(), ex);
        }

        return rc;

    }

    public static String convertToString(byte[] value) {
        String rc = "";

        try {
            rc = new String(value, 0, value.length, "UTF8");//in string
        } catch (Exception ex) {
            log.error("****** UTIL THROWABLE: " + ex.getMessage(), ex);
        }
        return rc;
    }

    public static String getFileContents(File aFile) {
        StringBuilder contents = new StringBuilder();

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null;

                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("****** FTATimerTask THROWABLE: " + ex.getMessage(), ex);
        }

        return contents.toString();
    }

    public static FTAChannel getChannelByTopic(List<FTAChannel> channels, String topic) {
        FTAChannel result = null;
        TopicMarshaller marshaller = new TopicMarshaller();

        for (FTAChannel channel : channels) {
            TopicExpressionType topicExp;
            topicExp = marshaller.unmarshal(channel.getTopic());

            if (topicExp.getContent().get(0).equals(topic)) {
                result = channel;
            }
        }

        return result;
    }
}
