/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.log4j.MDC;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author jasonasmith
 * 
 */
public class TransactionHandlerTest {

    private Mockery context = new Mockery();
    private final String MESSAGE_ID = "urn:uuid:AAA-AAA-AAA-AAA";
    private final String TRANSACTION_ID = "urn:uuid:BBB-BBB-BBB-BBB";
    private final String RELATESTO_ID = "urn:uuid:CCC-CCC-CCC-CCC";
    private final String WSA_NS = "http://www.w3.org/2005/08/addressing";
    private final String TRANS_NS = "http://connectopensource.org/transaction/";
    private Log log = context.mock(Log.class);

    /**
     * Test of TransactionHandler.handleMessage() with transaction and message elements in the soap message.
     */
    @Test
    public void testHandleMessage_transaction_element_in_message() {

        final TransactionHandler transHandler = new TransactionHandler() {
            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(messageId, MESSAGE_ID);
                assertEquals(transactionId, TRANSACTION_ID);
            }

            @Override
            protected String getTransactionId(String id) {
                return null;
            }
        };

        runHandleMessage(true, true, false, transHandler);
        assertEquals(MDC.get("transaction-id"), TRANSACTION_ID);
    }

    /**
     * Test of TransactionHandler.handleMessage() with message elements in the soap message and a corresponding
     * transaction id in the database.
     */
    @Test
    public void testHandleMessage_transaction_in_database() {

        final TransactionHandler transHandler = new TransactionHandler() {
            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(messageId, MESSAGE_ID);
                assertEquals(transactionId, TRANSACTION_ID);
            }

            @Override
            protected String getTransactionId(String id) {
                return TRANSACTION_ID;
            }
        };

        runHandleMessage(true, false, false, transHandler);
        assertEquals(MDC.get("transaction-id"), TRANSACTION_ID);
    }

    /**
     * Test of TransactionHandler.handleMessage() with message element in the soap message but with no transaction
     * element and no corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_no_transaction_in_message_or_database() {

        final TransactionHandler transHandler = new TransactionHandler() {
            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(1, 2);
            }

            @Override
            protected String getTransactionId(String id) {
                return null;
            }
        };

        runHandleMessage(true, false, false, transHandler);
        assertNull(MDC.get("transaction-id"));
    }

    /**
     * Test of TransactionHandler.handleMessage() with relatesTo and message elements in the soap message and a
     * corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_relatesTo_with_transaction() {

        final TransactionHandler transHandler = new TransactionHandler() {
            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(messageId, MESSAGE_ID);
                assertEquals(transactionId, TRANSACTION_ID);
            }

            @Override
            protected String getTransactionId(String id) {
                assertEquals(id, RELATESTO_ID);
                return TRANSACTION_ID;
            }
        };

        runHandleMessage(true, false, true, transHandler);
        assertEquals(MDC.get("transaction-id"), TRANSACTION_ID);
    }

    /**
     * Test of TransactionHandler.handleMessage() with relatesTo and message elements in the soap message but no
     * corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_relatesTo_with_no_transaction() {

        final TransactionHandler transHandler = new TransactionHandler() {
            private int counter = 0;

            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(1, 2);
            }

            @Override
            protected String getTransactionId(String id) {
                if (counter == 0)
                    assertEquals(id, RELATESTO_ID);
                else if (counter == 1) {
                    assertEquals(id, MESSAGE_ID);
                }
                counter++;
                return null;
            }
        };

        runHandleMessage(true, false, true, transHandler);
        assertNull(MDC.get("transaction-id"));
    }

    /**
     * Test of TransactionHandler.handleMessage() with no messageId.
     */
    @Test
    public void testHandleMessage_with_no_messageId() {

        final TransactionHandler transHandler = new TransactionHandler() {
            @Override
            protected Log getLogger() {
                return log;
            }

            @Override
            protected void createTransactionRecord(String messageId, String transactionId) {
                assertEquals(1, 2);
            }

            @Override
            protected String getTransactionId(String id) {
                assertEquals(1, 2);
                return null;
            }
        };

        runHandleMessage(false, true, false, transHandler);
        assertNull(MDC.get("transaction-id"));
    }

    /**
     * Clears MDC and then creates the soap message based on the parameters and calls the tested method,
     * handleMessage().
     * 
     * @param enableMessageId
     * @param enableTransactionId
     * @param enableRelatesToId
     * @param transactionHandler
     */
    private void runHandleMessage(Boolean enableMessageId, Boolean enableTransactionId, Boolean enableRelatesToId,
            TransactionHandler transactionHandler) {

        MDC.remove("transaction-id");

        final SOAPMessageContext mockSoapContext = context.mock(SOAPMessageContext.class);

        try {
            final SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

            SOAPHeader soapHeader = soapEnvelope.getHeader();

            if (enableMessageId) {
                SOAPHeaderElement headerElementMessageID = soapHeader.addHeaderElement(soapEnvelope.createName(
                        "MessageID", "", WSA_NS));
                headerElementMessageID.setTextContent(MESSAGE_ID);
            }

            if (enableTransactionId) {
                SOAPHeaderElement headerElementTransID = soapHeader.addHeaderElement(soapEnvelope.createName(
                        "TransactionID", "txn", TRANS_NS));
                headerElementTransID.addTextNode(TRANSACTION_ID);
            }

            if (enableRelatesToId) {
                SOAPHeaderElement headerElementTransID = soapHeader.addHeaderElement(soapEnvelope.createName(
                        "RelatesTo", "", WSA_NS));
                headerElementTransID.addTextNode(RELATESTO_ID);
            }

            context.checking(new Expectations() {
                {
                    oneOf(mockSoapContext).getMessage();
                    will(returnValue(soapMessage));
                    ignoring(log);
                }
            });

            transactionHandler.handleMessage(mockSoapContext);
            context.assertIsSatisfied();

        } catch (SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
