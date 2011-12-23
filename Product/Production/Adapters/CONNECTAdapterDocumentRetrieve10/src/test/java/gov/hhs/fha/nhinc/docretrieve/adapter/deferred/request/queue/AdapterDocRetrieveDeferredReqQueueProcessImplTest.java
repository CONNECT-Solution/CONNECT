/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
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
public class AdapterDocRetrieveDeferredReqQueueProcessImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterDocRetrieveDeferredReqQueueProcessOrchImpl mockServiceImpl = context.mock(AdapterDocRetrieveDeferredReqQueueProcessOrchImpl.class);

    private DocRetrieveAcknowledgementType createDocRetrieveAcknowledgementType() {
        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus(NhincConstants.DOC_RETRIEVE_DEFERRED_REQ_ACK_STATUS_MSG);
        docRetrieveAck.setMessage(responseType);
        return docRetrieveAck;
    }

    private DocRetrieveAcknowledgementType createDocRetrieveAcknowledgementType2() {
        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        docRetrieveAck.setMessage(null);
        return docRetrieveAck;
    }

    private DocRetrieveAcknowledgementType createDocRetrieveAcknowledgementType3() {
        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus(null);
        docRetrieveAck.setMessage(responseType);
        return docRetrieveAck;
    }

    private DocRetrieveAcknowledgementType createDocRetrieveAcknowledgementType4() {
        DocRetrieveAcknowledgementType docRetrieveAck = new DocRetrieveAcknowledgementType();
        RegistryResponseType responseType = new RegistryResponseType();
        responseType.setStatus("123");
        docRetrieveAck.setMessage(responseType);
        return docRetrieveAck;
    }

    @Test
    public void testGetAdapterDocRetrieveDeferredReqQueueProcessOrchImpl() {
        AdapterDocRetrieveDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterDocRetrieveDeferredReqQueueProcessImpl();
        AdapterDocRetrieveDeferredReqQueueProcessOrchImpl processImpl = deferredProcessImpl.getAdapterDocRetrieveDeferredReqQueueProcessOrchImpl();
        assertNotNull(processImpl);
    }

    @Test
    public void testProcessDocRetrieveDeferredReqQueue() {
        try {
            final int NUM_CALLS = 6;
            DocRetrieveDeferredReqQueueProcessRequestType request = new DocRetrieveDeferredReqQueueProcessRequestType();
            request.setMessageId("");

            AdapterDocRetrieveDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterDocRetrieveDeferredReqQueueProcessImpl() {
                @Override
                protected AdapterDocRetrieveDeferredReqQueueProcessOrchImpl getAdapterDocRetrieveDeferredReqQueueProcessOrchImpl() {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    exactly(NUM_CALLS).of(mockServiceImpl).processDocRetrieveDeferredReqQueue(with(any(String.class)));
                    will(onConsecutiveCalls(
                            returnValue(createDocRetrieveAcknowledgementType()),
                            returnValue(createDocRetrieveAcknowledgementType2()),
                            returnValue(createDocRetrieveAcknowledgementType3()),
                            returnValue(createDocRetrieveAcknowledgementType4()),
                            returnValue(new DocRetrieveAcknowledgementType()),
                            returnValue(null)
                            ));

                }
            });
            
            DocRetrieveDeferredReqQueueProcessResponseType response = null;
            for (int i = 0; i < NUM_CALLS; i++) {
                response = deferredProcessImpl.processDocRetrieveDeferredReqQueue(request, null);
                assertNotNull(response);
            }
        } catch (Throwable t) {
            System.out.println("Error running testProcessDocRetrieveDeferredReqQueue: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessDocRetrieveDeferredReqQueue: " + t.getMessage());
        }
    }
}
