/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessRequestType;
import gov.hhs.fha.nhinc.gateway.entitypatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
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
public class EntityPatientDiscoveryDeferredReqQueueProcessImplTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl mockServiceImpl = context.mock(EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.class);

    @Test
    public void testProcessPatientDiscoveryDeferredReqQueueHappy() {
        try {
            EntityPatientDiscoveryDeferredReqQueueProcessImpl deferredProcessImpl = new EntityPatientDiscoveryDeferredReqQueueProcessImpl() {
                @Override
                protected EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).processPatientDiscoveryDeferredReqQueue(with(aNonNull(String.class)));
                }
            });

            PatientDiscoveryDeferredReqQueueProcessRequestType request = new PatientDiscoveryDeferredReqQueueProcessRequestType();
            request.setMessageId("");
            PatientDiscoveryDeferredReqQueueProcessResponseType response = deferredProcessImpl.processPatientDiscoveryDeferredReqQueue(request, null);
            assertNotNull("processPatientDiscoveryDeferredReqQueue response was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testProcessPatientDiscoveryDeferredReqQueueHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessPatientDiscoveryDeferredReqQueueHappy: " + t.getMessage());
        }
    }

}
