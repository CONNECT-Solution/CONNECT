package gov.hhs.fha.nhinc.redaction.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class RedactionEngineNoOpImplTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testFilterAdhocQueryResults()
    {
        try
        {
            String expectedRequestId = "TestRequest";
            final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
            AdhocQueryResponse inputAdhocQueryResponse = new AdhocQueryResponse();
            inputAdhocQueryResponse.setRequestId(expectedRequestId);

            RedactionEngineNoOpImpl noOpProxy = new RedactionEngineNoOpImpl();

            AdhocQueryResponse response = noOpProxy.filterAdhocQueryResults(mockAdhocQueryRequest, inputAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse should not be null", response);
            assertEquals("Request id", expectedRequestId, response.getRequestId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResults test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterRetrieveDocumentSetResults()
    {
        try
        {
            String expectedStatus = "TestStatus";
            final RetrieveDocumentSetRequestType mockRequest = context.mock(RetrieveDocumentSetRequestType.class);
            RetrieveDocumentSetResponseType inputResponse = new RetrieveDocumentSetResponseType();
            RegistryResponseType registryResponse = new RegistryResponseType();
            inputResponse.setRegistryResponse(registryResponse);
            registryResponse.setStatus(expectedStatus);

            RedactionEngineNoOpImpl noOpProxy = new RedactionEngineNoOpImpl();

            RetrieveDocumentSetResponseType response = noOpProxy.filterRetrieveDocumentSetResults(mockRequest, inputResponse);
            assertNotNull("RetrieveDocumentSetResponseType should not be null", response);
            assertNotNull("Registry response was null", response.getRegistryResponse());
            assertEquals("Status", expectedStatus, response.getRegistryResponse().getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetResults test: " + t.getMessage());
        }
    }

}