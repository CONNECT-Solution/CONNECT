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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.slf4j.MDC;

/**
 *
 * @author jasonasmith
 */
public class TransactionHandlerTest {

    private final String MESSAGE_ID = "urn:uuid:AAA-AAA-AAA-AAA";
    private final String TRANSACTION_ID = "urn:uuid:BBB-BBB-BBB-BBB";
    private final String RELATESTO_ID = "urn:uuid:CCC-CCC-CCC-CCC";
    private final String WSA_NS = "http://www.w3.org/2005/08/addressing";
    private final String TRANS_NS = "http://connectopensource.org/transaction/";

    private TransactionLogger transactionLogger = null;
    private TransactionStore transactionStore = null;

    /**
     * Test of TransactionHandler.handleMessage() with transaction and message elements in the soap message.
     */
    @Test
    public void testHandleMessage_transaction_element_in_message() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        when(transactionStore.getTransactionId(MESSAGE_ID)).thenReturn(null);

        runHandleMessage(true, true, false, transHandler);

        verify(transactionLogger).createTransactionRecord(MESSAGE_ID, TRANSACTION_ID);
        verify(transactionLogger).enableMdcLogging(eq(TRANSACTION_ID), eq(MESSAGE_ID));
    }

    /**
     * Test of TransactionHandler.handleMessage() with message elements in the soap message and a corresponding
     * transaction id in the database.
     */
    @Test
    public void testHandleMessage_transaction_in_database() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        when(transactionStore.getTransactionId(MESSAGE_ID)).thenReturn(TRANSACTION_ID);

        runHandleMessage(true, false, false, transHandler);

        verify(transactionLogger, never()).createTransactionRecord(MESSAGE_ID, TRANSACTION_ID);
        verify(transactionLogger).enableMdcLogging(eq(TRANSACTION_ID), eq(MESSAGE_ID));
    }

    /**
     * Test of TransactionHandler.handleMessage() with message element in the soap message but with no transaction
     * element and no corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_no_transaction_in_message_or_database() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        when(transactionStore.getTransactionId(MESSAGE_ID)).thenReturn(null);

        runHandleMessage(true, false, false, transHandler);

        verify(transactionLogger, never()).createTransactionRecord(MESSAGE_ID, MESSAGE_ID);
        verify(transactionLogger).enableMdcLogging(eq((String) null), eq(MESSAGE_ID));
    }

    /**
     * Test of TransactionHandler.handleMessage() with relatesTo and message elements in the soap message and a
     * corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_relatesTo_with_transaction() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        when(transactionStore.getTransactionId(RELATESTO_ID)).thenReturn(TRANSACTION_ID);

        runHandleMessage(true, false, true, transHandler);

        verify(transactionLogger).createTransactionRecord(MESSAGE_ID, TRANSACTION_ID);
        verify(transactionLogger).enableMdcLogging(eq(TRANSACTION_ID), eq(MESSAGE_ID));
    }

    /**
     * Test of TransactionHandler.handleMessage() with relatesTo and message elements in the soap message but no
     * corresponding transaction id in the database.
     */
    @Test
    public void testHandleMessage_relatesTo_with_no_transaction() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        when(transactionStore.getTransactionId(MESSAGE_ID)).thenReturn(null);
        when(transactionStore.getTransactionId(RELATESTO_ID)).thenReturn(null);

        runHandleMessage(true, false, true, transHandler);

        verify(transactionLogger, never()).createTransactionRecord(any(String.class), any(String.class));
        verify(transactionLogger).enableMdcLogging(eq((String) null), eq(MESSAGE_ID));
    }

    /**
     * Test of TransactionHandler.handleMessage() with no messageId.
     */
    @Test
    public void testHandleMessage_with_no_messageId() {

        transactionLogger = mock(TransactionLogger.class);
        transactionStore = mock(TransactionStore.class);

        final TransactionHandler transHandler = new TransactionHandler() {

            @Override
            protected TransactionLogger getTransactionLogger() {
                return transactionLogger;
            }

            @Override
            protected TransactionStore getTransactionStore() {
                return transactionStore;
            }
        };

        runHandleMessage(false, true, false, transHandler);
        verify(transactionStore, never()).getTransactionId(any(String.class));
        verify(transactionLogger, never()).createTransactionRecord(any(String.class), any(String.class));
        verify(transactionLogger, never()).enableMdcLogging(any(String.class), any(String.class));
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

        final SOAPMessageContext mockSoapContext = mock(SOAPMessageContext.class);

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

            when(mockSoapContext.getMessage()).thenReturn(soapMessage);

            transactionHandler.handleMessage(mockSoapContext);

        } catch (SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
