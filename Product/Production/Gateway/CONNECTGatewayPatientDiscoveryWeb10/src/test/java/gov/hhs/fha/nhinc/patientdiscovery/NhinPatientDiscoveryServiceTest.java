package gov.hhs.fha.nhinc.patientdiscovery;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.NhinPatientDiscoveryOrchImpl;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;

import javax.xml.ws.WebServiceContext;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

public class NhinPatientDiscoveryServiceTest {

	Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    @Test
    public void testHappyPath() {
    	
    	final PRPAIN201305UV02 request = context.mock(PRPAIN201305UV02.class);
    	final WebServiceContext webServiceContext = context.mock(WebServiceContext.class);
    	final PRPAIN201306UV02 expectedResponse = context.mock(PRPAIN201306UV02.class);
    	final NhinPatientDiscoveryOrchImpl mockOrchestration = context.mock(NhinPatientDiscoveryOrchImpl.class);
    	final AssertionType mockAssertion = context.mock(AssertionType.class);
    	final PatientDiscoveryAuditLogger mockAuditLogger = context.mock(PatientDiscoveryAuditLogger.class);
    	final PatientDiscoveryTransforms mockPatientDiscoveryTransforms = context.mock(PatientDiscoveryTransforms.class);
    	final PerformanceManager mockPerformanceManager = context.mock(PerformanceManager.class);

    	

    	NhinPatientDiscoveryImpl service = new NhinPatientDiscoveryImpl() {

			
			@Override
			protected NhinPatientDiscoveryOrchImpl getOrchestrator() {
				return mockOrchestration;
			}

			@Override
			protected AssertionType getSamlAssertion(WebServiceContext context) {
				return mockAssertion;
			}

			@Override
			protected PatientDiscoveryAuditLogger getAuditLogger() {
				return mockAuditLogger;
			}
			
			@Override
			protected PatientDiscoveryTransforms getTransforms() {
				return mockPatientDiscoveryTransforms;
			}

			@Override
			protected PerformanceManager getPerformanceManager() {
				return mockPerformanceManager;
			}
			
			
			
			
			
   		 
   	 };
    	
    	context.checking(new Expectations() {{
    		allowing(mockOrchestration).respondingGatewayPRPAIN201305UV02(with(same(request)), with(same(mockAssertion)));
    		will(returnValue(expectedResponse));
    		
    		oneOf(mockPatientDiscoveryTransforms).getPatientDiscoveryMessageCommunityId(request,
					NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
					NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
					NhincConstants.AUDIT_LOG_SYNC_TYPE,
					NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
    		will(returnValue("1.1"));
    		
    		
    		
    		oneOf(mockPerformanceManager).logPerformanceStart(with(aNonNull(Timestamp.class)),
						with(same(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME)),
						with(same(NhincConstants.AUDIT_LOG_NHIN_INTERFACE)),
						with(same(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION)),
						with(same("1.1")));
    		
    		oneOf(mockPerformanceManager).logPerformanceStop(with(aNonNull(Long.class)), with(aNonNull(Timestamp.class)), with(aNonNull(Timestamp.class)));
    		
    		oneOf(mockAuditLogger).auditNhin201305(with(same(request)), with(same(mockAssertion)), with(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));
    		oneOf(mockAuditLogger).auditNhin201306(with(same(expectedResponse)), with(same(mockAssertion)),
    				with(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION));

    	}});
    	
    	
    	PRPAIN201306UV02 actualResponse = service.respondingGatewayPRPAIN201305UV02(request, webServiceContext);
    	
    	assertSame(expectedResponse, actualResponse);
    	
    }
	
}
