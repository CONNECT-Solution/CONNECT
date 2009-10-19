/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.gateway.aggregator;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.util.StringUtil;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifiersType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocQueryAggregator;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocRetrieveAggregator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author westbergl
 */
@Ignore //todo: move this to integration test suit
public class NhincComponentAggregatorTest
{
    private static final String ADHOC_QUERY_RESPONSE_XML_FILE = "AdhocQueryResponseExample.xml";
    private static final String RETRIEVE_DOCUMENT_SET_RESPONSE_XML_FILE = "RetrieveDocumentSetResponseExample.xml";

    public NhincComponentAggregatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }
    
    /**
     * This creates a filled out AdhocQueryResponse message.
     * 
     * @return A filled out AdhocQueryResponse message.
     */
    private AdhocQueryResponse createAdhocQueryResponse()
    {
        AdhocQueryResponse oAdhocQueryResponse = new AdhocQueryResponse();
        
        try
        {
            String sAdhocQueryResponseXML = StringUtil.readTextFile(ADHOC_QUERY_RESPONSE_XML_FILE);
            if ((sAdhocQueryResponseXML != null) &&
                (sAdhocQueryResponseXML.length() > 0))
            {
                DocQueryAggregator oDocQueryAggregator = new DocQueryAggregator();
                oAdhocQueryResponse = oDocQueryAggregator.unmarshalAdhocQueryResponse(sAdhocQueryResponseXML);
            }
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            fail("Failed to read example XML.");
        }
        
        return oAdhocQueryResponse;
    }

    /**
     * This creates a filled out RetrieveDocumentSetResponse message.
     * 
     * @return A filled out RetrieveDocumentSetResponse message.
     */
    private RetrieveDocumentSetResponseType createRetrieveDocumentSetResponse()
    {
        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = new RetrieveDocumentSetResponseType();
        
        try
        {
            String sRetrieveDocumentSetResponseXML = StringUtil.readTextFile(RETRIEVE_DOCUMENT_SET_RESPONSE_XML_FILE);
            if ((sRetrieveDocumentSetResponseXML != null) &&
                (sRetrieveDocumentSetResponseXML.length() > 0))
            {
                DocRetrieveAggregator oDocRetrieveAggregator = new DocRetrieveAggregator();
                oRetrieveDocumentSetResponse = oDocRetrieveAggregator.unmarshalAdhocQueryResponse(sRetrieveDocumentSetResponseXML);
            }
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            fail("Failed to read example XML.");
        }
        
        return oRetrieveDocumentSetResponse;
    }

    /**
     * Test of startTransactionDocQuery method, of class NhincComponentAggregator.
     */
    @Test
    public void testDocQueryFullTransactionProcess()
    {
        System.out.println("Start testDocQueryFullTransactionProcess");

        System.out.println("Starting transaction...");
        
        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String sHomeCommunityId = mappingDao.getHomeCommunityId("1.1");

        StartTransactionDocQueryRequestType oRequest = new StartTransactionDocQueryRequestType();
        oRequest.setQualifiedPatientIdentifiers(new QualifiedSubjectIdentifiersType());

        QualifiedSubjectIdentifierType oId = new QualifiedSubjectIdentifierType();
        oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().add(oId);
        oId.setAssigningAuthorityIdentifier("1.1");
        oId.setSubjectIdentifier("1");

        oId = new QualifiedSubjectIdentifierType();
        oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().add(oId);
        oId.setAssigningAuthorityIdentifier("1.1");
        oId.setSubjectIdentifier("2");

        oId = new QualifiedSubjectIdentifierType();
        oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().add(oId);
        oId.setAssigningAuthorityIdentifier("1.1");
        oId.setSubjectIdentifier("3");

        NhincComponentAggregator oAggregator = new NhincComponentAggregator();

        StartTransactionDocQueryResponseType oStartTransactResponse = null;
        oStartTransactResponse = oAggregator.startTransactionDocQuery(oRequest);
        assertNotNull(oStartTransactResponse);
        assertNotSame("TransactionId:", oStartTransactResponse.getTransactionId(), "");
        String sTransactionId = oStartTransactResponse.getTransactionId();

        System.out.println("Done starting transaction...");
        
        System.out.println("Adding response messages starting transaction...");

        AdhocQueryResponse oAdhocQueryResponse = createAdhocQueryResponse();
        SetResponseMsgDocQueryResponseType oSetResponseMsgResponse = null;
        // Message 1
        //----------
        SetResponseMsgDocQueryRequestType oSetResponseMsgRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId(sHomeCommunityId);
        oSetResponseMsgRequest.setQualifiedPatientIdentifier(new QualifiedSubjectIdentifierType());
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setAssigningAuthorityIdentifier("1.1");
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setSubjectIdentifier("1");
        oSetResponseMsgRequest.setAdhocQueryResponse(oAdhocQueryResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocQuery(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");
        
        // Message 2
        //----------
        oSetResponseMsgRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId(sHomeCommunityId);
        oSetResponseMsgRequest.setQualifiedPatientIdentifier(new QualifiedSubjectIdentifierType());
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setAssigningAuthorityIdentifier("1.1");
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setSubjectIdentifier("2");
        oSetResponseMsgRequest.setAdhocQueryResponse(oAdhocQueryResponse);
        oAggregator.setResponseMsgDocQuery(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");

        // Message 3
        //----------
        oSetResponseMsgRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId(sHomeCommunityId);
        oSetResponseMsgRequest.setQualifiedPatientIdentifier(new QualifiedSubjectIdentifierType());
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setAssigningAuthorityIdentifier("1.1");
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setSubjectIdentifier("3");
        oSetResponseMsgRequest.setAdhocQueryResponse(oAdhocQueryResponse);
        oAggregator.setResponseMsgDocQuery(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");

        System.out.println("Done adding response messages starting transaction...");
        
        GetAggResultsDocQueryRequestType oGetAggResultsDocQueryRequest = new GetAggResultsDocQueryRequestType();
        oGetAggResultsDocQueryRequest.setTimedOut(false);
        oGetAggResultsDocQueryRequest.setTransactionId(sTransactionId);
        GetAggResultsDocQueryResponseType oGetAggResultsDocQueryResponse = null;
        oGetAggResultsDocQueryResponse = oAggregator.getAggResultsDocQuery(oGetAggResultsDocQueryRequest);
        assertNotNull("GetAggResultsDocQueryResponse:", oGetAggResultsDocQueryResponse);
        assertEquals("oGetAggResultsDocQueryResponse.getStatus():", oGetAggResultsDocQueryResponse.getStatus(), "COMPLETE");
        assertNotNull("oGetAggResultsDocQueryResponse.getAdhocQueryResponse():", oGetAggResultsDocQueryResponse.getAdhocQueryResponse());
        assertNotNull("RegistryObjectList:", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList());
        assertNotNull("Identifiable:", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable());
        assertEquals("Identifiable.size():", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size(), 9);
        
        System.out.println("Completed testDocQueryFullTransactionProcess");
    }

    /**
     * Now lets make sure the time out function works...  To do this we will create a transaction with two results.
     * we will then set response only to one, and see what happens if we call for the results first with
     * the timedOut = false and then second with the timedOut = true.
     */
    @Test
    public void testDocQueryFullTransactionUsingTimeout()
    {
        System.out.println("Start testDocQueryFullTransactionUsingTimeout");

        System.out.println("Starting transaction...");

        AssigningAuthorityHomeCommunityMappingDAO mappingDao = new AssigningAuthorityHomeCommunityMappingDAO();
        String sHomeCommunityId = mappingDao.getHomeCommunityId("1.1");
        
        StartTransactionDocQueryRequestType oRequest = new StartTransactionDocQueryRequestType();
        oRequest.setQualifiedPatientIdentifiers(new QualifiedSubjectIdentifiersType());

        QualifiedSubjectIdentifierType oId = new QualifiedSubjectIdentifierType();
        oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().add(oId);
        oId.setAssigningAuthorityIdentifier("1.1");
        oId.setSubjectIdentifier("1");

        oId = new QualifiedSubjectIdentifierType();
        oRequest.getQualifiedPatientIdentifiers().getQualifiedSubjectIdentifier().add(oId);
        oId.setAssigningAuthorityIdentifier("1.1");
        oId.setSubjectIdentifier("2");

        NhincComponentAggregator oAggregator = new NhincComponentAggregator();

        StartTransactionDocQueryResponseType oStartTransactResponse = null;
        oStartTransactResponse = oAggregator.startTransactionDocQuery(oRequest);
        assertNotNull(oStartTransactResponse);
        assertNotSame("TransactionId:", oStartTransactResponse.getTransactionId(), "");
        String sTransactionId = oStartTransactResponse.getTransactionId();

        System.out.println("Done starting transaction...");
        
        System.out.println("Adding response for first message...");

        AdhocQueryResponse oAdhocQueryResponse = createAdhocQueryResponse();
        SetResponseMsgDocQueryResponseType oSetResponseMsgResponse = null;
        // Message 1
        //----------
        SetResponseMsgDocQueryRequestType oSetResponseMsgRequest = new SetResponseMsgDocQueryRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId(sHomeCommunityId);
        oSetResponseMsgRequest.setQualifiedPatientIdentifier(new QualifiedSubjectIdentifierType());
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setAssigningAuthorityIdentifier("1.1");
        oSetResponseMsgRequest.getQualifiedPatientIdentifier().setSubjectIdentifier("1");
        oSetResponseMsgRequest.setAdhocQueryResponse(oAdhocQueryResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocQuery(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");
        
        System.out.println("Done adding response for first message...");
        
        System.out.println("Calling for results with timedOut = false...");
        
        GetAggResultsDocQueryRequestType oGetAggResultsDocQueryRequest = new GetAggResultsDocQueryRequestType();
        oGetAggResultsDocQueryRequest.setTimedOut(false);
        oGetAggResultsDocQueryRequest.setTransactionId(sTransactionId);
        GetAggResultsDocQueryResponseType oGetAggResultsDocQueryResponse = null;
        oGetAggResultsDocQueryResponse = oAggregator.getAggResultsDocQuery(oGetAggResultsDocQueryRequest);
        assertNotNull("GetAggResultsDocQueryResponse:", oGetAggResultsDocQueryResponse);
        assertEquals("oGetAggResultsDocQueryResponse.getStatus():", oGetAggResultsDocQueryResponse.getStatus(), "PENDING");
        assertNull("oGetAggResultsDocQueryResponse.getAdhocQueryResponse():", oGetAggResultsDocQueryResponse.getAdhocQueryResponse());
        
        System.out.println("Done calling for results with timedOut = false...");
        
        System.out.println("Calling for results with timedOut = true...");
        
        oGetAggResultsDocQueryRequest = new GetAggResultsDocQueryRequestType();
        oGetAggResultsDocQueryRequest.setTimedOut(true);
        oGetAggResultsDocQueryRequest.setTransactionId(sTransactionId);
        oGetAggResultsDocQueryResponse = null;
        oGetAggResultsDocQueryResponse = oAggregator.getAggResultsDocQuery(oGetAggResultsDocQueryRequest);
        assertNotNull("GetAggResultsDocQueryResponse:", oGetAggResultsDocQueryResponse);
        assertEquals("oGetAggResultsDocQueryResponse.getStatus():", oGetAggResultsDocQueryResponse.getStatus(), "COMPLETE");
        assertNotNull("oGetAggResultsDocQueryResponse.getAdhocQueryResponse():", oGetAggResultsDocQueryResponse.getAdhocQueryResponse());
        assertNotNull("RegistryObjectList:", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList());
        assertNotNull("Identifiable:", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable());
        assertEquals("Identifiable.size():", oGetAggResultsDocQueryResponse.getAdhocQueryResponse().getRegistryObjectList().getIdentifiable().size(), 3);
        
        System.out.println("Done calling for results with timedOut = true...");

        System.out.println("Completed testDocQueryFullTransactionUsingTimeout");

    }
    
    /**
     * Test of DocRetrieve -full cycle through the aggregator.
     */
    @Test
    public void testDocRetrieveFullTransactionProcess()
    {
        System.out.println("Start testDocRetrieveFullTransactionProcess");

        System.out.println("Starting transaction...");

        StartTransactionDocRetrieveRequestType oRequest = new StartTransactionDocRetrieveRequestType();
        oRequest.setRetrieveDocumentSetRequest(new RetrieveDocumentSetRequestType());
        List<DocumentRequest> olDocRequest = oRequest.getRetrieveDocumentSetRequest().getDocumentRequest();

        DocumentRequest oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("1.1");
        oDocRequest.setRepositoryUniqueId("1.2");
        oDocRequest.setDocumentUniqueId("1.3");

        oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("2.1");
        oDocRequest.setRepositoryUniqueId("2.2");
        oDocRequest.setDocumentUniqueId("2.3");
        
        oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("3.1");
        oDocRequest.setRepositoryUniqueId("3.2");
        oDocRequest.setDocumentUniqueId("3.3");

        NhincComponentAggregator oAggregator = new NhincComponentAggregator();

        StartTransactionDocRetrieveResponseType oStartTransactResponse = null;
        oStartTransactResponse = oAggregator.startTransactionDocRetrieve(oRequest);
        assertNotNull(oStartTransactResponse);
        assertNotSame("TransactionId:", oStartTransactResponse.getTransactionId(), "");
        String sTransactionId = oStartTransactResponse.getTransactionId();

        System.out.println("Done starting transaction...");
        
        System.out.println("Adding response messages starting transaction...");

        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = createRetrieveDocumentSetResponse();
        SetResponseMsgDocRetrieveResponseType oSetResponseMsgResponse = null;
        
        // Message 1
        //----------
        SetResponseMsgDocRetrieveRequestType oSetResponseMsgRequest = new SetResponseMsgDocRetrieveRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId("1.1");
        oSetResponseMsgRequest.setRepositoryUniqueId("1.2");
        oSetResponseMsgRequest.setDocumentUniqueId("1.3");
        oSetResponseMsgRequest.setRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocRetrieve(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");
        
        // Message 2
        //----------
        oSetResponseMsgRequest = new SetResponseMsgDocRetrieveRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId("2.1");
        oSetResponseMsgRequest.setRepositoryUniqueId("2.2");
        oSetResponseMsgRequest.setDocumentUniqueId("2.3");
        oSetResponseMsgRequest.setRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocRetrieve(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");

        // Message 3
        //----------
        oSetResponseMsgRequest = new SetResponseMsgDocRetrieveRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId("3.1");
        oSetResponseMsgRequest.setRepositoryUniqueId("3.2");
        oSetResponseMsgRequest.setDocumentUniqueId("3.3");
        oSetResponseMsgRequest.setRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocRetrieve(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");

        System.out.println("Done adding response messages starting transaction...");
        
        GetAggResultsDocRetrieveRequestType oGetAggResultsDocRetrieveRequest = new GetAggResultsDocRetrieveRequestType();
        oGetAggResultsDocRetrieveRequest.setTimedOut(false);
        oGetAggResultsDocRetrieveRequest.setTransactionId(sTransactionId);
        GetAggResultsDocRetrieveResponseType oGetAggResultsDocRetrieveResponse = null;
        oGetAggResultsDocRetrieveResponse = oAggregator.getAggResultsDocRetrieve(oGetAggResultsDocRetrieveRequest);
        assertNotNull("GetAggResultsDocRetrieveResponse:", oGetAggResultsDocRetrieveResponse);
        assertEquals("oGetAggResultsDocRetrieveResponse.getStatus():", oGetAggResultsDocRetrieveResponse.getStatus(), "COMPLETE");
        assertNotNull("oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse():", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse());
        assertNotNull("DocumentResponse:", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse().getDocumentResponse());
        assertEquals("DocumentResponse.size():", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse().getDocumentResponse().size(), 3);
        
        System.out.println("Completed testFullTransactionProcess");
    }
    
    /**
     * Now lets make sure the time out function works...  To do this we will create a transaction with two results.
     * we will then set response only to one, and see what happens if we call for the results first with
     * the timedOut = false and then second with the timedOut = true.
     */
    @Test
    public void testDocRetrieveFullTransactionUsingTimeout()
    {
        System.out.println("Start testDocRetrieveFullTransactionUsingTimeout");

        System.out.println("Starting transaction...");

        StartTransactionDocRetrieveRequestType oRequest = new StartTransactionDocRetrieveRequestType();
        oRequest.setRetrieveDocumentSetRequest(new RetrieveDocumentSetRequestType());
        List<DocumentRequest> olDocRequest = oRequest.getRetrieveDocumentSetRequest().getDocumentRequest();

        DocumentRequest oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("1.1");
        oDocRequest.setRepositoryUniqueId("1.2");
        oDocRequest.setDocumentUniqueId("1.3");

        oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("2.1");
        oDocRequest.setRepositoryUniqueId("2.2");
        oDocRequest.setDocumentUniqueId("2.3");
        
        oDocRequest = new DocumentRequest();
        olDocRequest.add(oDocRequest);
        oDocRequest.setHomeCommunityId("3.1");
        oDocRequest.setRepositoryUniqueId("3.2");
        oDocRequest.setDocumentUniqueId("3.3");

        NhincComponentAggregator oAggregator = new NhincComponentAggregator();

        StartTransactionDocRetrieveResponseType oStartTransactResponse = null;
        oStartTransactResponse = oAggregator.startTransactionDocRetrieve(oRequest);
        assertNotNull(oStartTransactResponse);
        assertNotSame("TransactionId:", oStartTransactResponse.getTransactionId(), "");
        String sTransactionId = oStartTransactResponse.getTransactionId();

        System.out.println("Done starting transaction...");
        
        System.out.println("Adding response messages starting transaction...");

        RetrieveDocumentSetResponseType oRetrieveDocumentSetResponse = createRetrieveDocumentSetResponse();
        SetResponseMsgDocRetrieveResponseType oSetResponseMsgResponse = null;
        
        // Message 1
        //----------
        SetResponseMsgDocRetrieveRequestType oSetResponseMsgRequest = new SetResponseMsgDocRetrieveRequestType();
        oSetResponseMsgRequest.setTransactionId(sTransactionId);
        oSetResponseMsgRequest.setHomeCommunityId("1.1");
        oSetResponseMsgRequest.setRepositoryUniqueId("1.2");
        oSetResponseMsgRequest.setDocumentUniqueId("1.3");
        oSetResponseMsgRequest.setRetrieveDocumentSetResponse(oRetrieveDocumentSetResponse);
        oSetResponseMsgResponse = oAggregator.setResponseMsgDocRetrieve(oSetResponseMsgRequest);
        assertNotNull(oSetResponseMsgResponse);
        assertNotNull(oSetResponseMsgResponse.getStatus());
        assertEquals("SetResponseMsg.Status:", oSetResponseMsgResponse.getStatus(), "SUCCESS");
        
        System.out.println("Done adding response messages starting transaction...");
        
        System.out.println("Calling for results with timedOut = false...");
        
        GetAggResultsDocRetrieveRequestType oGetAggResultsDocRetrieveRequest = new GetAggResultsDocRetrieveRequestType();
        oGetAggResultsDocRetrieveRequest.setTimedOut(false);
        oGetAggResultsDocRetrieveRequest.setTransactionId(sTransactionId);
        GetAggResultsDocRetrieveResponseType oGetAggResultsDocRetrieveResponse = null;
        oGetAggResultsDocRetrieveResponse = oAggregator.getAggResultsDocRetrieve(oGetAggResultsDocRetrieveRequest);
        assertNotNull("GetAggResultsDocRetrieveResponse:", oGetAggResultsDocRetrieveResponse);
        assertEquals("oGetAggResultsDocRetrieveResponse.getStatus():", oGetAggResultsDocRetrieveResponse.getStatus(), "PENDING");
        assertNull("oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse():", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse());
        
        System.out.println("Done calling for results with timedOut = false...");
        
        System.out.println("Calling for results with timedOut = true...");
        
        oGetAggResultsDocRetrieveRequest = new GetAggResultsDocRetrieveRequestType();
        oGetAggResultsDocRetrieveRequest.setTimedOut(true);
        oGetAggResultsDocRetrieveRequest.setTransactionId(sTransactionId);
        oGetAggResultsDocRetrieveResponse = null;
        oGetAggResultsDocRetrieveResponse = oAggregator.getAggResultsDocRetrieve(oGetAggResultsDocRetrieveRequest);
         assertNotNull("GetAggResultsDocRetrieveResponse:", oGetAggResultsDocRetrieveResponse);
        assertEquals("oGetAggResultsDocRetrieveResponse.getStatus():", oGetAggResultsDocRetrieveResponse.getStatus(), "COMPLETE");
        assertNotNull("oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse():", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse());
        assertNotNull("DocumentResponse:", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse().getDocumentResponse());
        assertEquals("DocumentResponse.size():", oGetAggResultsDocRetrieveResponse.getRetrieveDocumentSetResponse().getDocumentResponse().size(), 1);
       
        System.out.println("Done calling for results with timedOut = true...");

        System.out.println("Completed testDocRetrieveFullTransactionUsingTimeout");
    }

}