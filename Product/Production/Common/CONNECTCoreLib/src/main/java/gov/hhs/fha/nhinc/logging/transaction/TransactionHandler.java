/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.logging.transaction;

import gov.hhs.fha.nhinc.logging.transaction.factory.TransactionStoreFactory;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey/jasonasmith
 *
 */
public class TransactionHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * \<txn:TransactionID xmlns:txn="http://connectopensource.org/transaction/"\>SOME-UUID\</txn:TransactionID\>.
     */
    private static final QName TRANSACTION_QNAME = new QName("http://connectopensource.org/transaction/",
            "TransactionID");
    private static final Logger LOG = LoggerFactory.getLogger(TransactionHandler.class);
    private static final String WSA_NS_2005 = "http://www.w3.org/2005/08/addressing";
    private static final String WSA_NS_2004 = "http://www.w3.org/2004/08/addressing";
    private static final String MESSAGE_ID = "MessageID";
    private static final String RELATESTO_ID = "RelatesTo";

    private TransactionLogger transactionLogger = null;
    private TransactionStoreFactory transactionStoreFactory = null;

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        LOG.debug("TransactionHandler handleMessage() START ");

        String messageId;
        String transactionId;
        String currentWSA;

        SOAPMessage soapMessage = context.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope;

        try {
            soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            SOAPElement messageIdElement;
            SOAPElement messageId05Element = getFirstChild(soapHeader, WSA_NS_2005, MESSAGE_ID);

            if (messageId05Element != null) {
                messageIdElement = messageId05Element;
                currentWSA = WSA_NS_2005;
            } else {
                messageIdElement = getFirstChild(soapHeader, WSA_NS_2004, MESSAGE_ID);
                currentWSA = WSA_NS_2004;
            }

            LOG.debug("TransactionHandler handleMessage() WSA namespace = " + currentWSA);

            if (messageIdElement != null) {
                messageId = messageIdElement.getTextContent();
                LOG.debug("TransactionHandler.handleMessage() messageId= " + messageId);

                SOAPElement transactionIdElement = getFirstChild(soapHeader, TRANSACTION_QNAME);

                // Checks if TransactionID is included in message as TransactionID element
                transactionId = checkTransactionIdFromMessage(transactionIdElement, messageId);

                // Checks if the repeatable RelatesTo value in the message has a transactionID
                if (NullChecker.isNullish(transactionId)) {

                    LOG.debug("TransactionHandler.handleMessage() Looking up on RelatesTo");
                    Iterator<SOAPElement> iter = getAllChildren(soapHeader, currentWSA, RELATESTO_ID);
                    transactionId = iterateThroughRelatesTo(iter, messageId);
                }

                // Finally, checks if transactionID is in the database
                if (NullChecker.isNullish(transactionId)) {
                    transactionId = getTransactionId(messageId);
                }

                enableMdcLogging(transactionId, messageId);
            }

        } catch (SOAPException e) {
            LOG.error("Unable to handle message: " + e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * Creates a new transaction record and inserts it into the table.
     *
     * @param messageId The messageId from the SOAPHeader
     * @param transactionId The transactionId fromthe SOAPHeader
     */
    protected void createTransactionRecord(String messageId, String transactionId) {
        getTransactionLogger().createTransactionRecord(messageId, transactionId);
    }

    /**
     * Gets the transaction logger.
     *
     * @return the transaction logger
     */
    protected TransactionLogger getTransactionLogger() {
        if (transactionLogger == null) {
            transactionLogger = new TransactionLogger();
        }
        return transactionLogger;
    }

    /**
     * Looks up transaction ID using the TransactionDAO.
     *
     * @param id The message Id from the SOAPHeader
     * @return transactionId The transactionId from the DAO lookup
     */
    protected String getTransactionId(String id) {
        return getTransactionStore().getTransactionId(id);
    }

    /**
     * Gets the transaction store.
     *
     * @return the transaction store
     */
    protected TransactionStore getTransactionStore() {
        if (transactionStoreFactory == null) {
            transactionStoreFactory = new TransactionStoreFactory();
        }
        return transactionStoreFactory.getTransactionStore();
    }

    /**
     * Enables MDC logging if the transaction ID is found.
     *
     * @param transactionId The transactionId for the message
     * @param messageId The messageId for the message
     */
    protected void enableMdcLogging(String transactionId, String messageId) {
        getTransactionLogger().enableMdcLogging(transactionId, messageId);
    }

    private String checkTransactionIdFromMessage(SOAPElement transactionIdElement, String messageId) {
        String transactionId = null;
        if (transactionIdElement != null) {
            transactionId = transactionIdElement.getTextContent();
            LOG.debug("TransactionHandler TransactionId found: " + transactionId);
            if (getTransactionId(messageId) == null) {
                createTransactionRecord(messageId, transactionId);
            } // else transactionId is already persisted
        }

        return transactionId;
    }

    private String iterateThroughRelatesTo(Iterator<SOAPElement> iter, String messageId) {
        String relatesToId;
        String transactionId = null;

        if (iter != null) {
            int count = 0;
            StringBuffer relatesBuffer = new StringBuffer();
            while (iter.hasNext()) {
                SOAPElement relatesToIdElement = iter.next();
                if (relatesToIdElement != null) {
                    relatesToId = relatesToIdElement.getTextContent();
                    LOG.debug("TransactionHandler.handleMessage() RelatesTo: " + relatesToId);
                    transactionId = getTransactionId(relatesToId);

                    if (NullChecker.isNotNullish(transactionId)) {
                        createTransactionRecord(messageId, transactionId);
                        if (count > 0) {
                            relatesBuffer.append(", ");
                        }
                        relatesBuffer.append(transactionId);
                        count++;
                    } // else transactionId is already persisted
                }
            }
            if (relatesBuffer.length() > 0) {
                transactionId = relatesBuffer.toString();
            }
        }

        return transactionId;
    }

    private SOAPElement getFirstChild(SOAPHeader header, String ns, String name) {
        QName qname = new QName(ns, name);
        return getFirstChild(header, qname);
    }

    private SOAPElement getFirstChild(SOAPHeader header, QName qname) {
        SOAPElement result = null;
        if (header != null) {
            Iterator iter = header.getChildElements(qname);
            if (iter.hasNext()) {
                result = (SOAPElement) iter.next();
            }
        }
        return result;
    }

    private Iterator<SOAPElement> getAllChildren(SOAPHeader header, String ns, String name) {
        QName qname = new QName(ns, name);
        Iterator<SOAPElement> iter = null;
        if (header != null) {
            iter = header.getChildElements(qname);
        }
        return iter;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public boolean handleFault(SOAPMessageContext context) {
        LOG.warn("TransactionHandler.handleFault");
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
     */
    @Override
    public void close(MessageContext context) {
        LOG.debug("TransactionHandler.close");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
     */
    @Override
    public Set<QName> getHeaders() {
        Set<QName> headers = new HashSet<>();
        headers.add(TRANSACTION_QNAME);
        return headers;
    }
}
