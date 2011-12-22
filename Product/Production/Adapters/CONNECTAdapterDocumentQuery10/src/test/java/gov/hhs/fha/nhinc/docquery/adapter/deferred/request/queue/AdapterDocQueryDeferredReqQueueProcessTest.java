/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author akong
 */
public class AdapterDocQueryDeferredReqQueueProcessTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterDocQueryDeferredReqQueueProcessImpl mockImpl = context.mock(AdapterDocQueryDeferredReqQueueProcessImpl.class);

    @Test
    public void testGetAdapterDocQueryDeferredReqQueueProcessImpl() {
        AdapterDocQueryDeferredReqQueueProcess process = new AdapterDocQueryDeferredReqQueueProcess();
        AdapterDocQueryDeferredReqQueueProcessImpl processImpl = process.getAdapterDocQueryDeferredReqQueueProcessImpl();
        assertNotNull(processImpl);
    }


    @Test
    public void testProcessDocQueryDeferredReqQueue() {
        AdapterDocQueryDeferredReqQueueProcess process = new AdapterDocQueryDeferredReqQueueProcess() {
            @Override
            protected AdapterDocQueryDeferredReqQueueProcessImpl getAdapterDocQueryDeferredReqQueueProcessImpl() {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {
            {
                oneOf(mockImpl).processDocQueryDeferredReqQueue(with(any(DocQueryDeferredReqQueueProcessRequestType.class)), with(any(javax.xml.ws.WebServiceContext.class)));
                will(returnValue(new DocQueryDeferredReqQueueProcessResponseType()));
            }
        });

        DocQueryDeferredReqQueueProcessResponseType response = process.processDocQueryDeferredReqQueue(null);
        assertNotNull(response);
    }
}
