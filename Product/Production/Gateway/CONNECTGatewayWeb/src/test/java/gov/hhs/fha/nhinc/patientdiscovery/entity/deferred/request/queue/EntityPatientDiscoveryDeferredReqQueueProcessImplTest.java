/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

import org.hl7.v3.MCCIIN000002UV01;
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
    public void testProcessPatientDiscoveryAsyncReqQueueHappy() {
        try {
            EntityPatientDiscoveryDeferredReqQueueProcessImpl deferredProcessImpl = new EntityPatientDiscoveryDeferredReqQueueProcessImpl() {
                @Override
                protected EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl getEntityPatientDiscoveryDeferredReqQueueProcessOrchImpl() {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).processPatientDiscoveryAsyncReqQueue(with(aNonNull(String.class)));
                }
            });

            MCCIIN000002UV01 response = deferredProcessImpl.processPatientDiscoveryAsyncReqQueue("");
            assertNotNull("processPatientDiscoveryAsyncReqQueue response was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testProcessPatientDiscoveryAsyncReqQueueHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessPatientDiscoveryAsyncReqQueueHappy: " + t.getMessage());
        }
    }

}
