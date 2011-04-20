/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue;

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
public class EntityPatientDiscoveryDeferredReqQueueProcessTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final EntityPatientDiscoveryDeferredReqQueueProcessImpl mockServiceImpl = context.mock(EntityPatientDiscoveryDeferredReqQueueProcessImpl.class);

    @Test
    public void testProcessPatientDiscoveryAsyncReqQueueHappy() {
        try {
            EntityPatientDiscoveryDeferredReqQueueProcess deferredProcess = new EntityPatientDiscoveryDeferredReqQueueProcess() {
                @Override
                protected EntityPatientDiscoveryDeferredReqQueueProcessImpl getEntityPatientDiscoveryDeferredReqQueueProcessImpl() {
                    return mockServiceImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).processPatientDiscoveryAsyncReqQueue(with(aNonNull(String.class)));
                }
            });

            String response = deferredProcess.processPatientDiscoveryAsyncReqQueue("");
            assertNotNull("processPatientDiscoveryAsyncReqQueue response was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testProcessPatientDiscoveryAsyncReqQueueHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testProcessPatientDiscoveryAsyncReqQueueHappy: " + t.getMessage());
        }
    }

}
