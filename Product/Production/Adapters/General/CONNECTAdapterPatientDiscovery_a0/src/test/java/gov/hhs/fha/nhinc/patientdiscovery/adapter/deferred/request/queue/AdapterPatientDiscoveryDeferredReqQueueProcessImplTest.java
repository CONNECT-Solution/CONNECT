/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.CS;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
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
 * @author richard.ettema
 */
@RunWith(JMock.class)
public class AdapterPatientDiscoveryDeferredReqQueueProcessImplTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl mockServiceImpl = context.mock(AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.class);

    private MCCIIN000002UV01 createMCCIIN000002UV01() {
        MCCIIN000002UV01 mCCIIN000002UV01 = new MCCIIN000002UV01();
        MCCIMT000200UV01Acknowledgement mccAck = new MCCIMT000200UV01Acknowledgement();
        CS typeCode = new CS();
        typeCode.setCode(HL7AckTransforms.ACK_TYPE_CODE_ACCEPT);
        mccAck.setTypeCode(typeCode);
        mCCIIN000002UV01.getAcknowledgement().add(mccAck);
        return mCCIIN000002UV01;
    }

    private MCCIIN000002UV01 createMCCIIN000002UV01_2() {
        MCCIIN000002UV01 mCCIIN000002UV01 = createMCCIIN000002UV01();
        mCCIIN000002UV01.getAcknowledgement().clear();
        return mCCIIN000002UV01;
    }

    private MCCIIN000002UV01 createMCCIIN000002UV01_3() {
        MCCIIN000002UV01 mCCIIN000002UV01 = createMCCIIN000002UV01();
        mCCIIN000002UV01.getAcknowledgement().get(0).setTypeCode(null);
        return mCCIIN000002UV01;
    }

    private MCCIIN000002UV01 createMCCIIN000002UV01_4() {
        MCCIIN000002UV01 mCCIIN000002UV01 = createMCCIIN000002UV01();
        mCCIIN000002UV01.getAcknowledgement().get(0).getTypeCode().setCode(null);
        return mCCIIN000002UV01;
    }

    private MCCIIN000002UV01 createMCCIIN000002UV01_5() {
        MCCIIN000002UV01 mCCIIN000002UV01 = createMCCIIN000002UV01();
        mCCIIN000002UV01.getAcknowledgement().get(0).getTypeCode().setCode("123");
        return mCCIIN000002UV01;
    }

    @Test
    public void testGetAdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
        AdapterPatientDiscoveryDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterPatientDiscoveryDeferredReqQueueProcessImpl();
        AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl processImpl = deferredProcessImpl.getAdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        assertNotNull(processImpl);
    }

    @Test
    public void testProcessPatientDiscoveryDeferredReqQueueHappy() {
        try {
            final int NUM_CALLS = 7;
            PatientDiscoveryDeferredReqQueueProcessRequestType request = new PatientDiscoveryDeferredReqQueueProcessRequestType();
            request.setMessageId("");

            AdapterPatientDiscoveryDeferredReqQueueProcessImpl deferredProcessImpl = new AdapterPatientDiscoveryDeferredReqQueueProcessImpl() {
                @Override
                protected AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl getAdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    exactly(NUM_CALLS).of(mockServiceImpl).processPatientDiscoveryDeferredReqQueue(with(any(String.class)));
                    will(onConsecutiveCalls(
                            returnValue(createMCCIIN000002UV01()),
                            returnValue(createMCCIIN000002UV01_2()),
                            returnValue(createMCCIIN000002UV01_3()),
                            returnValue(createMCCIIN000002UV01_4()),
                            returnValue(createMCCIIN000002UV01_5()),
                            returnValue(new MCCIIN000002UV01()),
                            returnValue(null)
                            ));
                }
            });

            PatientDiscoveryDeferredReqQueueProcessResponseType response = null;
            for (int i = 0; i < NUM_CALLS; i++) {
                response = deferredProcessImpl.processPatientDiscoveryDeferredReqQueue(request, null);
                assertNotNull(response);
            }
        } catch (Throwable t) {
            System.out.println("Error running testProcessPatientDiscoveryDeferredReqQueueHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessPatientDiscoveryDeferredReqQueueHappy: " + t.getMessage());
        }
    }

}
