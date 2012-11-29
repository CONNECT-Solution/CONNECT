/**
 * 
 */
package gov.hhs.fha.nhinc.direct.transform;

import java.io.IOException;

import gov.hhs.fha.nhinc.direct.DirectUnitTestUtil;
import gov.hhs.fha.nhinc.direct.MimeMessageBuilder;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.nhindirect.xd.transform.util.XmlUtils;

/**
 * @author mweaver
 * 
 */
public class MimeMessageTransformerTest {

    /**
     * Test the transformation of a MimeMessage with a simple S/MIME attachment.
     * 
     * @throws MessagingException
     */
    @Test
    public void usingRFC5322() throws MessagingException {
        MimeMessageTransformer transformer = getMimeMessageTransformer();
        MimeMessage message = buildMimeMessage("Example_A.txt");
        ProvideAndRegisterDocumentSetRequestType result = transformer.transform(message);
        verifyMime(result, 1);
    }

    /**
     * Test the transformation of a MimeMessage with a simple S/MIME attachment.
     * 
     * @throws MessagingException
     */
    @Test
    public void usingRFC5322PlusMime() throws MessagingException {
        MimeMessageTransformer transformer = getMimeMessageTransformer();
        MimeMessage message = buildMimeMessage("Example_B.txt");
        ProvideAndRegisterDocumentSetRequestType result = transformer.transform(message);
        verifyMime(result, 1);
    }

    /**
     * Test the transformation of a MimeMessage with an XDM S/MIME attachment.
     * @throws IOException 
     */
    @Test
    public void usingRFC5322PlusXdm() throws IOException {
        MimeMessageTransformer transformer = getMimeMessageTransformer();
        MimeMessage message = buildXdmMessage();
        ProvideAndRegisterDocumentSetRequestType result = transformer.transform(message);
        verifyXdm(result, 2);
    }

    /**
     * @return a new MimeMessageTransformer
     */
    private MimeMessageTransformer getMimeMessageTransformer() {
        return new MimeMessageTransformer();
    }

    /**
     * @return a MimeMessage with a simple attachment
     * @throws MessagingException
     */
    private MimeMessage buildMimeMessage(String fileName) throws MessagingException {
        MimeMessage message = new MimeMessage(null, IOUtils.toInputStream(DirectUnitTestUtil.getFileAsString(fileName)));

        return message;
    }

    /**
     * @return a MimeMessage with an XDM attachment
     * @throws IOException 
     */
    private MimeMessage buildXdmMessage() throws IOException {
        Session session = Session.getInstance(DirectUnitTestUtil.getMailServerProps("toaddress@localhost", 25, 143));

        MimeMessageBuilder builder = DirectUnitTestUtil
                .getMimeMessageBuilder(session);
        builder.attachment(null).attachmentName(null)
                .documents(DirectUnitTestUtil.mockDirectDocs());
        MimeMessage message = builder.build();
        return message;
    }

    /**
     * Verify that a transformation of a MimeMessage with a simple S/MIME attachemnt can be transformered into a reduced
     * metadata set XDR object.
     * 
     * @param prdsrt result of a transformation to be verified
     */
    private void verifyMime(ProvideAndRegisterDocumentSetRequestType prdsrt, int expectedDocuments) {
        String result = XmlUtils.marshal(
                new QName("urn:ihe:iti:xds-b:2007", "ProvideAndRegisterDocumentSetRequestType"), prdsrt,
                ihe.iti.xds_b._2007.ObjectFactory.class);
        System.out.println(result);

        assertEquals(expectedDocuments, prdsrt.getDocument().size());
        verifyMinimalMetadata(prdsrt);
    }

    /**
     * Verify that a transformation of a MimeMessage with an XDM S/MIME attachment can be transformed into a full
     * metadata set XDR object.
     * 
     * @param prdsrt result of a transformation to be verified
     */
    private void verifyXdm(ProvideAndRegisterDocumentSetRequestType prdsrt, int expectedDocuments) {
        String result = XmlUtils.marshal(
                new QName("urn:ihe:iti:xds-b:2007", "ProvideAndRegisterDocumentSetRequestType"), prdsrt,
                ihe.iti.xds_b._2007.ObjectFactory.class);
        System.out.println(result);

        assertEquals(expectedDocuments, prdsrt.getDocument().size());
        verifyXDSMetadata(prdsrt);
    }

    /**
     * Verify the data elements from the set of minimal required metadata are present. The minimal required metadata is
     * defined as follows:
     * 
     * Metadata Attribute           XDS     Minimal Metadata
     * -----------------------------------------------------
     * author                       R2      R2
     * classCode                    R       R2
     * confidentialityCode          R       R2
     * creationTime                 R       R2
     * entriUUID                    R       R
     * formatCode                   R       R
     * healthcareFacilityTypeCode   R       R2
     * languageCode                 R       R2
     * mimeType                     R       R
     * patientId                    R       R2
     * practiceSettingCode          R       R2
     * sourcePatientId              R       R2
     * typeCode                     R       R2
     * uniqueId                     R       R
     * 
     * @param prdsrt request to verify for minimum required metadata
     */
    private void verifyMinimalMetadata(ProvideAndRegisterDocumentSetRequestType prdsrt) {
        // TODO: verify entriUUID
        // TODO: verify formatCode
        // TODO: verify mimeType
        // TODO: verify uniqueId
    }

    /**
     * Verify the data elements from the set of XDS required metadata are present. The XDS required metadata is defined
     * as follows:
     * 
     * Metadata Attribute           XDS     Minimal Metadata
     * -----------------------------------------------------
     * author                       R2      R2
     * classCode                    R       R2
     * confidentialityCode          R       R2
     * creationTime                 R       R2
     * entriUUID                    R       R
     * formatCode                   R       R
     * healthcareFacilityTypeCode   R       R2
     * languageCode                 R       R2
     * mimeType                     R       R
     * patientId                    R       R2
     * practiceSettingCode          R       R2
     * sourcePatientId              R       R2
     * typeCode                     R       R2
     * uniqueId                     R       R
     * 
     * @param prdsrt request to verify for minimum required metadata
     */
    private void verifyXDSMetadata(ProvideAndRegisterDocumentSetRequestType prdsrt) {
        // TODO: verify author
        // TODO: verify classCode
        // TODO: verify confidentialityCode
        // TODO: verify creationTime
        // TODO: verify entriUUID
        // TODO: verify formatCode
        // TODO: verify healthcareFacilityTypeCode
        // TODO: verify languageCode
        // TODO: verify mimeType
        // TODO: verify patientId
        // TODO: verify practiceSettingCode
        // TODO: verify sourcePatientId
        // TODO: verify typeCode
        // TODO: verify uniqueId
    }
}
