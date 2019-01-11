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
package gov.hhs.fha.nhinc.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.PRPAIN201305UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class is used to test the AsyncMessageProcessorHelperTest class
 *
 * @author Arthur Kong
 */
public class AsyncMessageProcessHelperTest {

    private final Session session = mock(Session.class);
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AsyncMsgRecordDao mockDao = context.mock(AsyncMsgRecordDao.class);

    /**
     * Default constructor
     */
    public AsyncMessageProcessHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private AsyncMessageProcessHelper createAsyncMessageProcessHelper() {
        return new AsyncMessageProcessHelper() {
            @Override
            protected Session getSession() {
                return session;
            }

            @Override
            protected AsyncMsgRecordDao createAsyncMsgRecordDao() {
                return mockDao;
            }
        };
    }

    private List<AsyncMsgRecord> returnMockRecords() {
        ArrayList<AsyncMsgRecord> mockRecords = new ArrayList<>();
        AsyncMsgRecord record = new AsyncMsgRecord();
        record.setResponseTime(new Date());
        record.setCreationTime(new Date());
        mockRecords.add(record);

        return mockRecords;
    }

    @Test
    public void testCreateAsyncMsgRecordDao() {
        try {
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            AsyncMsgRecordDao dao = asyncHelper.createAsyncMsgRecordDao();
            assertNotNull(dao);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error running testCreateLogger test: " + e.getMessage());
        }
    }

    @Test
    public void testAddPatientDiscoveryRequest() {

        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();

        try {
            context.checking(new Expectations() {
                {
                    exactly(2).of(mockDao).insertRecords(with(any(List.class)));
                    will(returnValue(true));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();

            boolean result = asyncHelper.addPatientDiscoveryRequest(request, assertion, "INBOUND");
            assertTrue(result);
            result = asyncHelper.addPatientDiscoveryRequest(request, assertion, "OUTBOUND");
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error running testAddPatientDiscoveryRequest: " + e.getMessage());
        }
    }

    @Test
    public void testProcessAck_MCCIIN000002UV01() {
        String messageId = "111111111";
        String newStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        String errorStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        MCCIMT000200UV01Acknowledgement mccAck = new MCCIMT000200UV01Acknowledgement();
        CS typeCode = new CS();
        typeCode.setCode(HL7AckTransforms.ACK_TYPE_CODE_ERROR);
        mccAck.setTypeCode(typeCode);
        ack.getAcknowledgement().add(mccAck);

        try {
            context.checking(new Expectations() {
                {
                    exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                    exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)),
                            with(any(String.class)));
                    will(returnValue(returnMockRecords()));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            boolean result = asyncHelper.processAck(messageId, newStatus, errorStatus, ack);
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Error running testProcessAck_MCCIIN000002UV01: " + e.getMessage());
        }
    }

    @Test
    public void testProcessMessageStatus() {
        context.checking(new Expectations() {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(aNull(String.class)));
                will(returnValue(returnMockRecords()));
            }
        });

        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processMessageStatus("1.1", AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
        assertTrue(result);
    }

    @Test
    public void testProcessPatientDiscoveryResponse() {

        context.checking(new Expectations() {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                will(returnValue(returnMockRecords()));
            }
        });
        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processPatientDiscoveryResponse("1.1", AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS,
                AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, null);
        assertTrue(result);
    }

    @Test
    public void testCopyAssertionTypeObject() {
        AssertionType origAssertion = new AssertionType();
        origAssertion.setMessageId("1.1");

        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        AssertionType copiedAssertion = asyncHelper.copyAssertionTypeObject(origAssertion);
        assertEquals("1.1", copiedAssertion.getMessageId());
    }

    @Test
    public void testMarshalAssertionTypeObject() {
        AssertionType assertion = new AssertionType();
        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        String assertionString = asyncHelper.marshalAssertionTypeObject(assertion);

        assertNotNull(assertionString);
    }
}
