/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    final AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl mockServiceImpl = context
            .mock(AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl.class);

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
        AdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl processImpl = deferredProcessImpl
                .getAdapterPatientDiscoveryDeferredReqQueueProcessOrchImpl();
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
                    exactly(NUM_CALLS).of(mockServiceImpl).processPatientDiscoveryDeferredReqQueue(
                            with(any(String.class)));
                    will(onConsecutiveCalls(returnValue(createMCCIIN000002UV01()),
                            returnValue(createMCCIIN000002UV01_2()), returnValue(createMCCIIN000002UV01_3()),
                            returnValue(createMCCIIN000002UV01_4()), returnValue(createMCCIIN000002UV01_5()),
                            returnValue(new MCCIIN000002UV01()), returnValue(null)));
                }
            });

            PatientDiscoveryDeferredReqQueueProcessResponseType response;
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
