/**
 * 
 */
package gov.hhs.fha.nhinc.cxf.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import java.util.List;

import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

/**
 * This class is used to extract the AssertionType from Saml2Assertion in the ws security header. This AssertionType is
 * provided to the responding HIO for using the information for authorization.
 * 
 * @author mweaver
 * 
 */
public class SAML2AssertionExtractor {

    private static final Logger LOG = Logger.getLogger(SAML2AssertionExtractor.class);

    private static SAML2AssertionExtractor INSTANCE = null;

    private SAMLExtractorDOM extractor;

    SAML2AssertionExtractor() {
        SAMLExtractorDOMFactory factory = new SAMLExtractorDOMFactory();
        extractor = factory.getExtractor();
    }

    public static SAML2AssertionExtractor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SAML2AssertionExtractor();

        }
        return INSTANCE;
    }

    /**
     * This method is used to extract the saml assertion information.
     * 
     * @param context context
     * @return AssertionType
     */
    public final AssertionType extractSamlAssertion(final WebServiceContext context) {
        LOG.debug("Executing Saml2AssertionExtractor.extractSamlAssertion()...");
        AssertionType target = null;

        if (context == null) {
            return null;
        }

        MessageContext mContext = (MessageContext) context.getMessageContext();
        SoapHeader header = getSecuritySoapHeader(mContext);

        if (header != null) {
            Object obj = header.getObject();
            Element element = (Element) obj;

            target = extractor.extractSAMLAssertion(element);
        }

        return target;
    }

    private final SoapHeader getSecuritySoapHeader(MessageContext mContext) {
        @SuppressWarnings("unchecked")
        List<Header> headers = (List<Header>) mContext.get(org.apache.cxf.headers.Header.HEADER_LIST);

        if (headers != null) {
            for (Header header : headers) {
                if (header.getName().getLocalPart().equalsIgnoreCase("Security")) {
                    return (SoapHeader) header;
                }
            }
        }

        return null;
    }
}
