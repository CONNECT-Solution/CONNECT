package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
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
public class DocQueryResponseProcessorTest
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
        AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
        AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();

        AdhocQueryResponse response = testFilterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        // TODO: Change assertion when implemented and add additional assertion
        assertNull("AdhocQueryResponse was not null", response);
    }

    @Test
    public void filterAdhocQueryResultsNullInputs()
    {
        AdhocQueryRequest adhocQueryRequest = null;
        AdhocQueryResponse adhocQueryResponse = null;

        AdhocQueryResponse response = testFilterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        // TODO: Change assertion when implemented and add additional assertion
        assertNull("AdhocQueryResponse was not null", response);
    }

    private AdhocQueryResponse testFilterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        AdhocQueryResponse response = null;
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {

            };
            response = processor.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterAdhocQueryResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterAdhocQueryResults test: " + t.getMessage());
        }
        return response;
    }

    @Test
    public void testGetPatientId()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {

            };
            assertNull("Patient identifier", processor.getPatientId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientId test: " + t.getMessage());
        }

    }

    @Test
    public void testGetAssigningAuthorityId()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {

            };
            assertNull("Assigning authority id", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAssigningAuthorityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityId test: " + t.getMessage());
        }

    }

    @Test
    public void testExtractIdentifiers()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {

            };
            processor.extractIdentifiers(adhocQueryRequest);
            // TODO: Update assertions when implimented
            assertNull("Patient identifier", processor.getPatientId());
            assertNull("Assigning authority id", processor.getAssigningAuthorityId());
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
        AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
        PatientPreferencesType patientPreferences = new PatientPreferencesType();

        AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences);

        // TODO: Update after implimented
        assertNull("AdhocQueryResponse was not null", response);
    }

    @Test
    public void testFilterResultsNullInputs()
    {
        AdhocQueryResponse adhocQueryResponse = null;
        PatientPreferencesType patientPreferences = null;

        AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences);

        // TODO: Update after implimented
        assertNull("AdhocQueryResponse was not null", response);
    }

    private AdhocQueryResponse testFilterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
    {
        AdhocQueryResponse response = null;
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {

            };
            response = processor.filterResults(adhocQueryResponse, patientPreferences);
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