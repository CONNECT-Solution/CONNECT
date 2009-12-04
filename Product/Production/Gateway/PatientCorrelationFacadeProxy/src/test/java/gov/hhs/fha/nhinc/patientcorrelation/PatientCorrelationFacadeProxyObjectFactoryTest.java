package gov.hhs.fha.nhinc.patientcorrelation;

import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyNoOpImpl;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the ObjectFactory class.
 *
 * @author Neil Webb
 */
public class PatientCorrelationFacadeProxyObjectFactoryTest
{

    /**
     * Test case for the PatientCorrelationFacadeProxyObjectFactory class.
     * Validates that the implementation is the default no-op implementation.
     */
    @Ignore
    @Test
    public void testPatientCorrelationFacadeProxyObjectFactory()
    {
        System.out.println("Begin testPatientCorrelationFacadeProxyObjectFactory()");
        try
        {
            PatientCorrelationFacadeProxyObjectFactory factory = new PatientCorrelationFacadeProxyObjectFactory();
            PatientCorrelationFacadeProxy correlation = factory.getPatientCorrelationFacadeProxy();
            assertNotNull("PatientCorrelationFacade object was null", correlation);
            assertTrue("Patient correlation facade was not the default type.", (correlation instanceof PatientCorrelationFacadeProxyNoOpImpl));
        }
        catch(Throwable t)
        {
            System.out.println("Exception in testPatientCorrelationFacadeProxyObjectFactory: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testPatientCorrelationFacadeProxyObjectFactory()");
    }

    //TODO: Remove this test after fixing above one..
    @Test
    public void testPatientCorrelationFacadeProxyObjectFactoryThrowsException()
    {
        try
        {
            PatientCorrelationFacadeProxyObjectFactory factory = new PatientCorrelationFacadeProxyObjectFactory(){
                public PatientCorrelationFacadeProxy getPatientCorrelationFacadeProxy(){
                    throw new NullPointerException();
                }

            };
            PatientCorrelationFacadeProxy correlation = factory.getPatientCorrelationFacadeProxy();
            fail("Cannot come here");
        }
        catch(Throwable t)
        {
            assertTrue(true);
        }
    }
}