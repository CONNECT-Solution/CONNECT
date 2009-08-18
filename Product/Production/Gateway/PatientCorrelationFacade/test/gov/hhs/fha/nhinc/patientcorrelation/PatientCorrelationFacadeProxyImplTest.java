package gov.hhs.fha.nhinc.patientcorrelation;

import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeWebServiceProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.AddPatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeJavaProxy;

/**
 *
 *
 * @author Neil Webb
 */
public class PatientCorrelationFacadeProxyImplTest
{

    /**
     * Test case for patient correlation java proxy.
     *
     * Several services must be running:
     *
     * Spring config file must be correctly configured.
     */
    @Test
    public void testProxyImpl()
    {
        System.out.println("Start testProxyImpl");
        try
        {
            String homeAssigningAuthority = "1.1.1";
            String homePatientId = "999888999777";
            String remoteAssigninAuthority = "2.2.2";
            String remotePatientId = "999888999666";
            
            // Get patient correlation proxy
            PatientCorrelationFacadeProxyObjectFactory factory = new PatientCorrelationFacadeProxyObjectFactory();
            PatientCorrelationFacadeProxy correlationProxy = factory.getPatientCorrelationFacadeProxy();
            assertNotNull("Patient correlation was null", correlationProxy);
            assertTrue("Patient correlation proxy was not a valid impl", ((correlationProxy instanceof PatientCorrelationFacadeJavaProxy) ||(correlationProxy instanceof PatientCorrelationFacadeWebServiceProxy)));

            // Create add request
            AddPatientCorrelationRequestType addRequest = new AddPatientCorrelationRequestType();
            QualifiedSubjectIdentifierType homeQualifiedSubjectId = new QualifiedSubjectIdentifierType();
            homeQualifiedSubjectId.setAssigningAuthorityIdentifier(homeAssigningAuthority);
            homeQualifiedSubjectId.setSubjectIdentifier(homePatientId);
            addRequest.getQualifiedPatientIdentifier().add(homeQualifiedSubjectId);
            QualifiedSubjectIdentifierType remoteQualifiedSubjectId = new QualifiedSubjectIdentifierType();
            remoteQualifiedSubjectId.setAssigningAuthorityIdentifier(remoteAssigninAuthority);
            remoteQualifiedSubjectId.setSubjectIdentifier(remotePatientId);
            addRequest.getQualifiedPatientIdentifier().add(remoteQualifiedSubjectId);

            
            // Save patient correlation
            AddPatientCorrelationResponseType addResponse = correlationProxy.addPatientCorrelation(addRequest);
            assertNotNull("Add response was null", addResponse);
            assertNotNull("Add response ack was null", addResponse.getAck());
            assertEquals("Add response ack message", "success", addResponse.getAck().getMessage());
            
            // Create retrieve request
            RetrievePatientCorrelationsRequestType retrieveRequest = new RetrievePatientCorrelationsRequestType();
            retrieveRequest.setQualifiedPatientIdentifier(homeQualifiedSubjectId);

            // Retrieve patient correlation
            RetrievePatientCorrelationsResponseType retrieveResponse = correlationProxy.retrievePatientCorrelations(retrieveRequest);
            assertNotNull("Retrieve response was null", retrieveResponse);
            assertNotNull("Retrieve patient list was null", retrieveResponse.getQualifiedPatientIdentifier());
            assertFalse("Retrieve patient list was empty", retrieveResponse.getQualifiedPatientIdentifier().isEmpty());
            assertEquals("Retrieve patient list did not contain remote identifier", 1, retrieveResponse.getQualifiedPatientIdentifier().size());
            QualifiedSubjectIdentifierType retrievedRemote = retrieveResponse.getQualifiedPatientIdentifier().get(0);
            assertNotNull("Retrieved remote id was null", retrievedRemote);
            assertEquals("Retrieved remote patient id", remotePatientId, retrievedRemote.getSubjectIdentifier());
            assertEquals("Retrieved remote assigning authority", remoteAssigninAuthority, retrievedRemote.getAssigningAuthorityIdentifier());

            // Create remove request
            RemovePatientCorrelationRequestType removeRequest = new RemovePatientCorrelationRequestType();
            removeRequest.getQualifiedPatientIdentifier().add(homeQualifiedSubjectId);
            removeRequest.getQualifiedPatientIdentifier().add(remoteQualifiedSubjectId);

            // Remove patient correlation
            RemovePatientCorrelationResponseType removeResponse = correlationProxy.removePatientCorrelation(removeRequest);
            assertNotNull("Remove response was null", removeResponse);
            assertNotNull("Remove response ack was null", removeResponse.getAck());
            assertEquals("Remove response ack message", "success", removeResponse.getAck().getMessage());

            // Attempt retrieve again
            retrieveResponse = correlationProxy.retrievePatientCorrelations(retrieveRequest);
            assertNotNull("Retrieve response after delete was null", retrieveResponse);
            assertNotNull("Retrieve patient after delete list was null", retrieveResponse.getQualifiedPatientIdentifier());
            assertTrue("Retrieve patient list after delete was not empty", retrieveResponse.getQualifiedPatientIdentifier().isEmpty());

        }
        catch(Throwable t)
        {
            System.out.println("Failed in test " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testProxyImpl");
    }

}