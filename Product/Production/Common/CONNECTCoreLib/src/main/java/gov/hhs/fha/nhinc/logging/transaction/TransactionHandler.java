/**
 * 
 */
package gov.hhs.fha.nhinc.logging.transaction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;

/**
 * @author bhumphrey
 * 
 */
public class TransactionHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * <txn:TransactionID xmlns:txn="http://connectopensource.org/transaction/">SOME-UUID</txn:TransactionID>
     */
    private static final QName TRANSACTION_QNAME = new QName("http://connectopensource.org/transaction/", "TransactionID");
    private static Log log = LogFactory.getLog(TransactionHandler.class);
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";
    private static final String MESSAGE_ID = "MessageID";

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        String messageId;
        String transactionId;

        SOAPMessage soapMessage = context.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope;
        try {
            soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();

            SOAPElement messageIdElement = getFirstChild(soapHeader, WSA_NS, MESSAGE_ID);
            if (messageIdElement != null) {
                messageId = messageIdElement.getTextContent();

                MDC.put("message-id", messageId);

                SOAPElement transactionIdElement = getFirstChild(soapHeader, TRANSACTION_QNAME);
                if (transactionIdElement != null) {
                    transactionId = transactionIdElement.getTextContent();
                } else {
                    transactionId = TransactionIdMap.getInstance().getTransactionId(messageId);
                }
                    

                log.info("found transaction-id " + transactionId + "for message id" + messageId);
                
                MDC.put("transaction-id", transactionId);
                TransactionIdMap.getInstance().storeTransactionId(messageId, transactionId);

            }

        } catch (SOAPException e) {
            log.error(e);
        }

        return true;
    }

    private SOAPElement getFirstChild(SOAPHeader header, String ns, String name) {
        QName qname = new QName(ns, name);
        return getFirstChild(header, qname);
    }

    private SOAPElement getFirstChild(SOAPHeader header, QName qname) {
        SOAPElement result = null;
        Iterator iter = header.getChildElements(qname);
        if (iter.hasNext()) {
            result = (SOAPElement) iter.next();
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleFault(SOAPMessageContext context) {
        log.warn("TransactionHandler.handleFault");
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public void close(MessageContext context) {
        log.debug("TransactionHandler.close");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
     */
    @Override
    public Set<QName> getHeaders() {
        Set<QName> headers = new HashSet<QName>();
        headers.add(TRANSACTION_QNAME);
        return headers;
    }

}
