/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
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
public class AdapterDocRetrieveDeferredReqQueueProcessTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterDocRetrieveDeferredReqQueueProcessImpl mockImpl = context.mock(AdapterDocRetrieveDeferredReqQueueProcessImpl.class);

    @Test
    public void testGetAdapterDocRetrieveDeferredReqQueueProcessImpl() {
        AdapterDocRetrieveDeferredReqQueueProcess process = new AdapterDocRetrieveDeferredReqQueueProcess();
        AdapterDocRetrieveDeferredReqQueueProcessImpl processImpl = process.getAdapterDocRetrieveDeferredReqQueueProcessImpl();
        assertNotNull(processImpl);
    }


    @Test
    public void testProcessDocRetrieveDeferredReqQueue() {
        AdapterDocRetrieveDeferredReqQueueProcess process = new AdapterDocRetrieveDeferredReqQueueProcess() {
            @Override
            protected AdapterDocRetrieveDeferredReqQueueProcessImpl getAdapterDocRetrieveDeferredReqQueueProcessImpl() {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {
            {
                oneOf(mockImpl).processDocRetrieveDeferredReqQueue(with(any(DocRetrieveDeferredReqQueueProcessRequestType.class)), with(any(javax.xml.ws.WebServiceContext.class)));
                will(returnValue(new DocRetrieveDeferredReqQueueProcessResponseType()));
            }
        });

        DocRetrieveDeferredReqQueueProcessResponseType response = process.processDocRetrieveDeferredReqQueue(null);
        assertNotNull(response);
    }
}
