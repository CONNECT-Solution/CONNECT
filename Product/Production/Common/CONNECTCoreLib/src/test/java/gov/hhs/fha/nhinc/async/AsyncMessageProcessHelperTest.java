/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.async;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
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
import static org.junit.Assert.*;

/**
 * This class is used to test the AsyncMessageProcessorHelperTest class
 *
 * @author Arhtur Kong
 */
public class AsyncMessageProcessHelperTest {

    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
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

    private AsyncMessageProcessHelper createAsyncMessageProcessHelper()
    {
        return new AsyncMessageProcessHelper()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected AsyncMsgRecordDao createAsyncMsgRecordDao()
            {
                return mockDao;
            }
        };
    }

    private List<AsyncMsgRecord> returnMockRecords() {
        ArrayList<AsyncMsgRecord> mockRecords = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord record = new AsyncMsgRecord();
        record.setResponseTime(new Date());
        record.setCreationTime(new Date());
        mockRecords.add(record);

        return mockRecords;
    }

    @Test
    public void testCreateLogger()
    {
        try
        {
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();           
            Log oLog = asyncHelper.createLogger();
            assertNotNull(oLog);
        }
        catch(Exception e)
        {            
            e.printStackTrace();
            fail("Error running testCreateLogger test: " + e.getMessage());
        }
    }

    @Test
    public void testCreateAsyncMsgRecordDao()
    {
        try
        {
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            AsyncMsgRecordDao dao = asyncHelper.createAsyncMsgRecordDao();
            assertNotNull(dao);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("Error running testCreateLogger test: " + e.getMessage());
        }
    }

    @Test
    public void testAddPatientDiscoveryRequest() {

        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockDao).insertRecords(with(any(List.class))); will(returnValue(true));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
         
            boolean result = asyncHelper.addPatientDiscoveryRequest(request, assertion, "INBOUND");
            assertTrue(result);
            result = asyncHelper.addPatientDiscoveryRequest(request, assertion, "OUTBOUND");
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testAddPatientDiscoveryRequest: " + e.getMessage());
        }
    }

    @Test
    public void testAddQueryForDocumentsRequest() {
        AdhocQueryRequest request = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockDao).insertRecords(with(any(List.class))); will(returnValue(true));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();

            boolean result = asyncHelper.addQueryForDocumentsRequest(request, assertion, "INBOUND", "1.1");
            assertTrue(result);
            result = asyncHelper.addQueryForDocumentsRequest(request, assertion, "OUTBOUND", "1.1");
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testAddQueryForDocumentsRequest: " + e.getMessage());
        }
        
    }

    @Test
    public void testAddRetrieveDocumentsRequest() {
        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        AssertionType assertion = new AssertionType();
        
        RetrieveDocumentSetRequestType retrieveDoc = new RetrieveDocumentSetRequestType();
        
        request.setAssertion(assertion);
        request.setRetrieveDocumentSetRequest(retrieveDoc);

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(2).of(mockDao).insertRecords(with(any(List.class))); will(returnValue(true));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();

            boolean result = asyncHelper.addRetrieveDocumentsRequest(request, "INBOUND", "1.1");
            assertTrue(result);
            result = asyncHelper.addRetrieveDocumentsRequest(request, "OUTBOUND", "1.1");
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testAddRetrieveDocumentsRequest: " + e.getMessage());
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

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                    exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                    will(returnValue(returnMockRecords()));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            boolean result = asyncHelper.processAck(messageId, newStatus, errorStatus, ack);
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testProcessAck_MCCIIN000002UV01: " + e.getMessage());
        }
    }

    @Test
    public void testProcessAck_DocQueryAcknowledgementType() {
        String messageId = "111111111";
        String newStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        String errorStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        DocQueryAcknowledgementType ack = new DocQueryAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG);
        ack.setMessage(responseType);

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                    exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                    will(returnValue(returnMockRecords()));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            boolean result = asyncHelper.processAck(messageId, newStatus, errorStatus, ack);
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testProcessAck_DocQueryAcknowledgementType: " + e.getMessage());
        }
    }

    @Test
    public void testProcessAck_DocRetrieveAcknowledgementType() {
        String messageId = "111111111";
        String newStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        String errorStatus = AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR;
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG);
        ack.setMessage(responseType);

        try
        {
            context.checking(new Expectations()
            {
                {
                    exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                    exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                    will(returnValue(returnMockRecords()));
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
            boolean result = asyncHelper.processAck(messageId, newStatus, errorStatus, ack);
            assertTrue(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Error running testProcessAck_DocRetrieveAcknowledgementType: " + e.getMessage());
        }
    }

    @Test
    public void testProcessMessageStatus() {
        context.checking(new Expectations()
        {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                will(returnValue(returnMockRecords()));
                allowing(mockLog).debug(with(any(String.class)));
            }
        });

        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processMessageStatus("1.1", AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS);
        assertTrue(result);
    }

    @Test
    public void testProcessPatientDiscoveryResponse() {

        context.checking(new Expectations()
        {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                will(returnValue(returnMockRecords()));
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processPatientDiscoveryResponse("1.1",
                AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, null);
        assertTrue(result);
    }

    @Test
    public void testProcessQueryForDocumentsResponse() {
        context.checking(new Expectations()
        {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                will(returnValue(returnMockRecords()));
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processQueryForDocumentsResponse("1.1",
                AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, null);
        assertTrue(result);
    }

    @Test
    public void testProcessRetrieveDocumentsResponse() {
        context.checking(new Expectations()
        {
            {
                exactly(1).of(mockDao).save(with(any(AsyncMsgRecord.class)));
                exactly(1).of(mockDao).queryByMessageIdAndDirection(with(any(String.class)), with(any(String.class)));
                will(returnValue(returnMockRecords()));
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        boolean result = asyncHelper.processRetrieveDocumentsResponse("1.1",
                AsyncMsgRecordDao.QUEUE_STATUS_REQPROCESS, AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDERR, null);
        assertTrue(result);
    }

    @Test
    public void testCopyRetrieveDocumentSetRequestTypeObject() {
        RetrieveDocumentSetRequestType origRequest = new RetrieveDocumentSetRequestType();
        DocumentRequest docRequest = new DocumentRequest();
        docRequest.setHomeCommunityId("1.1");
        origRequest.getDocumentRequest().add(docRequest);

        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        RetrieveDocumentSetRequestType copiedRequest =
                asyncHelper.copyRetrieveDocumentSetRequestTypeObject(origRequest);
        assertEquals("1.1", copiedRequest.getDocumentRequest().get(0).getHomeCommunityId());
    }

    @Test
    public void testCopyRetrieveDocumentSetResponseTypeObject() {
        RetrieveDocumentSetResponseType origResponse = new RetrieveDocumentSetResponseType();
        DocumentResponse docResponse = new DocumentResponse();
        docResponse.setHomeCommunityId("1.1");
        origResponse.getDocumentResponse().add(docResponse);

        AsyncMessageProcessHelper asyncHelper = createAsyncMessageProcessHelper();
        RetrieveDocumentSetResponseType copiedResponse =
                asyncHelper.copyRetrieveDocumentSetResponseTypeObject(origResponse);
        assertEquals("1.1", copiedResponse.getDocumentResponse().get(0).getHomeCommunityId());
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
