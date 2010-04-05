package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
public class DocRetrieveResponseProcessorTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void filterAdhocQueryResultsHappy()
    {
        RetrieveDocumentSetRequestType retrieveRequest = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetResponseType retrieveResponse = new RetrieveDocumentSetResponseType();

        RetrieveDocumentSetResponseType response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
        // TODO: Change assertion when implemented and add additional assertion
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    @Test
    public void filterAdhocQueryResultsNullInputs()
    {
        RetrieveDocumentSetRequestType retrieveRequest = null;
        RetrieveDocumentSetResponseType retrieveResponse = null;

        RetrieveDocumentSetResponseType response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
        // TODO: Change assertion when implemented and add additional assertion
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    private RetrieveDocumentSetResponseType testFilterRetrieveDocumentSetReults(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse)
    {
        RetrieveDocumentSetResponseType response = null;
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            response = processor.filterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterRetrieveDocumentSetReults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetReults test: " + t.getMessage());
        }
        return response;
    }

    @Test
    public void testGetDocumentId()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            assertNull("Document identifier", processor.getDocumentId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetDocumentId test: " + t.getMessage());
        }

    }

    @Test
    public void testGetHomeCommunityId()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            assertNull("Home community id", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetHomeCommunityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHomeCommunityId test: " + t.getMessage());
        }

    }

    @Test
    public void testRepositoryId()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            assertNull("Repository id", processor.getRepositoryId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRepositoryId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRepositoryId test: " + t.getMessage());
        }

    }

    @Test
    public void testExtractIdentifiers()
    {
        try
        {
            RetrieveDocumentSetRequestType retrieveRequest = new RetrieveDocumentSetRequestType();
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            processor.extractIdentifiers(retrieveRequest);
            // TODO: Update assertions when implimented
            assertNull("Document identifier", processor.getDocumentId());
            assertNull("Home community id", processor.getHomeCommunityId());
            assertNull("Repository id", processor.getRepositoryId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiers test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiers test: " + t.getMessage());
        }

    }

    @Test
    public void testFilterResultsHappy()
    {
        RetrieveDocumentSetResponseType retrieveDocSetResponse = new RetrieveDocumentSetResponseType();
        PatientPreferencesType patientPreferences = new PatientPreferencesType();

        RetrieveDocumentSetResponseType response = testFilterResults(retrieveDocSetResponse, patientPreferences);

        // TODO: Update after implimented
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    @Test
    public void testFilterResultsNullInputs()
    {
        RetrieveDocumentSetResponseType adhocQueryResponse = null;
        PatientPreferencesType patientPreferences = null;

        RetrieveDocumentSetResponseType response = testFilterResults(adhocQueryResponse, patientPreferences);

        // TODO: Update after implimented
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    private RetrieveDocumentSetResponseType testFilterResults(RetrieveDocumentSetResponseType retrieveResponse, PatientPreferencesType patientPreferences)
    {
        RetrieveDocumentSetResponseType response = null;
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {

            };
            response = processor.filterResults(retrieveResponse, patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResults test: " + t.getMessage());
        }
        return response;
    }
}