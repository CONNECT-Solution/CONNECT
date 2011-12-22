/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
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
public class AdapterDocQueryDeferredReqQueueProcessImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterDocQueryDeferredReqQueueProcessOrchImpl mockServiceImpl = context.mock(AdapterDocQueryDeferredReqQueueProcessOrchImpl.class);

    private DocQueryAcknowledgementType createDocQueryAcknowledgementType() {
        DocQueryAcknowledgementType docQueryAck = new DocQueryAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG);
        docQueryAck.setMessage(responseType);       
        return docQueryAck;
    }

    private DocQueryAcknowledgementType createDocQueryAcknowledgementType2() {
        DocQueryAcknowledgementType docQueryAck = createDocQueryAcknowledgementType();
        docQueryAck.setMessage(null);
        return docQueryAck;
    }

    private DocQueryAcknowledgementType createDocQueryAcknowledgementType3() {
        DocQueryAcknowledgementType docQueryAck = createDocQueryAcknowledgementType();
        docQueryAck.getMessage().setStatus(null);
        return docQueryAck;
    }

    private DocQueryAcknowledgementType createDocQueryAcknowledgementType4() {
        DocQueryAcknowledgementType docQueryAck = createDocQueryAcknowledgementType();
        docQueryAck.getMessage().setStatus("123");
        return docQueryAck;
    }

    @Test
    public void testGetAdapterDocQueryDeferredReqQueueProcessOrchImpl() {
        AdapterDocQueryDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterDocQueryDeferredReqQueueProcessImpl();
        AdapterDocQueryDeferredReqQueueProcessOrchImpl processImpl = deferredProcessImpl.getAdapterDocQueryDeferredReqQueueProcessOrchImpl();
        assertNotNull(processImpl);
    }

    @Test
    public void testProcessDocQueryDeferredReqQueue() {
        try {
            final int NUM_CALLS = 6;
            DocQueryDeferredReqQueueProcessRequestType request = new DocQueryDeferredReqQueueProcessRequestType();
            request.setMessageId("");

            AdapterDocQueryDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterDocQueryDeferredReqQueueProcessImpl() {
                @Override
                protected AdapterDocQueryDeferredReqQueueProcessOrchImpl getAdapterDocQueryDeferredReqQueueProcessOrchImpl() {
                    return mockServiceImpl;
                }
            };
            
            context.checking(new Expectations() {
                {
                    exactly(NUM_CALLS).of(mockServiceImpl).processDocQueryAsyncReqQueue(with(any(String.class)));
                    will(onConsecutiveCalls(
                            returnValue(createDocQueryAcknowledgementType()),
                            returnValue(createDocQueryAcknowledgementType2()),
                            returnValue(createDocQueryAcknowledgementType3()),
                            returnValue(createDocQueryAcknowledgementType4()),
                            returnValue(new DocQueryAcknowledgementType()),
                            returnValue(null)
                            ));
                }
            });

            DocQueryDeferredReqQueueProcessResponseType response = null;
            for (int i = 0; i < NUM_CALLS; i++) {
                response = deferredProcessImpl.processDocQueryDeferredReqQueue(request, null);
                assertNotNull(response);
            }       
        } catch (Throwable t) {
            System.out.println("Error running testProcessDocQueryDeferredReqQueue: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessDocQueryDeferredReqQueue: " + t.getMessage());
        }
    }
}
