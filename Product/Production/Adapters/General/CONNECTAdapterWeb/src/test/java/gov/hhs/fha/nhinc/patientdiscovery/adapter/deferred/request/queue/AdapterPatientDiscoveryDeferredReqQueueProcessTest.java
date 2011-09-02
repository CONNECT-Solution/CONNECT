/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author akong
 */
@RunWith(JMock.class)
public class AdapterPatientDiscoveryDeferredReqQueueProcessTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterPatientDiscoveryDeferredReqQueueProcessImpl mockImpl = context.mock(AdapterPatientDiscoveryDeferredReqQueueProcessImpl.class);

    @Test
    public void testGetAdapterPatientDiscoveryDeferredReqQueueProcessImpl() {
        AdapterPatientDiscoveryDeferredReqQueueProcess process = new AdapterPatientDiscoveryDeferredReqQueueProcess();
        AdapterPatientDiscoveryDeferredReqQueueProcessImpl processImpl = process.getAdapterPatientDiscoveryDeferredReqQueueProcessImpl();
        assertNotNull(processImpl);
    }


    @Test
    public void testProcessPatientDiscoveryDeferredReqQueue() {
        AdapterPatientDiscoveryDeferredReqQueueProcess process = new AdapterPatientDiscoveryDeferredReqQueueProcess() {
            @Override
            protected AdapterPatientDiscoveryDeferredReqQueueProcessImpl getAdapterPatientDiscoveryDeferredReqQueueProcessImpl() {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {
            {
                oneOf(mockImpl).processPatientDiscoveryDeferredReqQueue(with(any(PatientDiscoveryDeferredReqQueueProcessRequestType.class)), with(any(javax.xml.ws.WebServiceContext.class)));
                will(returnValue(new PatientDiscoveryDeferredReqQueueProcessResponseType()));
            }
        });

        PatientDiscoveryDeferredReqQueueProcessResponseType response = process.processPatientDiscoveryDeferredReqQueue(null);
        assertNotNull(response);
    }
}
