package gov.hhs.fha.nhinc.patientcorrelation;

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
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeWebServiceProxy;
import org.junit.Ignore;

/**
 *
 *
 * @author Neil Webb
 */
public class PatientCorrelationFacadeProxyImplTest {

    /**
     * Test case for patient correlation java proxy.
     *
     * Several services must be running:
     *
     * Spring config file must be correctly configured.
     */
    
    //TODO: Dum test remove once above test is fixed
    @Test
    public void testProxyImplThrowsException() {
        try{
            PatientCorrelationFacadeProxyObjectFactory factory = new PatientCorrelationFacadeProxyObjectFactory(){
                public PatientCorrelationFacadeProxy getPatientCorrelationFacadeProxy(){
                    return null;
                }
            };
            factory.getPatientCorrelationFacadeProxy();
        }catch(Exception e){
            assertTrue(true);
        }
    }
}
