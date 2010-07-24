package gov.hhs.fha.nhinc.async;

import java.util.List;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class manages the creation of the header section of the soap message that
 * defines WS-Addressing.
 */
public class AsyncHeaderCreator {

    private Log log = null;

    /**
     * Default Constructor defines the logger
     */
    public AsyncHeaderCreator() {
        log = createLogger();
    }

    /**
     * Creates the error logger
     * @return The Logger
     */
    protected Log createLogger() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    /**
     * Creates the WS-Addressing headers.  Specifically the To, Action, ReplyTo,
     * Address, and MessageID elements are created as Header objects and packaged
     * in a List.  This listing can be used to define a WSBindingProvider's
     * outbound headers.
     * @param url       The Endpoint URL used to set the To header element
     * @param action    The WS-Addressing action used to set the Action header
     *                  element and should match the wsaw:Action as defined in
     *                  the port definition section of the corresponding wsdl
     * @param messageId The UUID uniquely identifying the message used to set
     *                  the MessageID header
     * @return A listing of the WS-Addressing headers
     */
    public List<Header> createOutboundHeaders(String url, String action, String messageId) {

        List<Header> headers = new ArrayList<Header>();

        QName qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_TO);
        Header toHdr = Headers.create(qname, url);
        headers.add(toHdr);

        qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_ACTION);
        Header actionHdr = Headers.create(qname, action);
        headers.add(actionHdr);

        //The ReplyTo header contains the Address element
        final Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final Element elemReplyTo = document.createElementNS(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_REPLYTO);
            final Element elemAddr = document.createElementNS(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_ADDRESS);
            elemAddr.setTextContent(NhincConstants.WS_ADDRESSING_URL_ANONYMOUS);
            elemReplyTo.appendChild(elemAddr);
            Header replyToHdr = Headers.create(elemReplyTo);
            headers.add(replyToHdr);
        } catch (ParserConfigurationException ex) {
            log.error("ReplyTo header element can not be created: " + ex.getMessage());
        }

        qname = new QName(NhincConstants.WS_ADDRESSING_URL, NhincConstants.WS_SOAP_HEADER_MESSAGE_ID);
        Header msgIdHdr = Headers.create(qname, messageId);
        headers.add(msgIdHdr);

        return headers;
    }
}
