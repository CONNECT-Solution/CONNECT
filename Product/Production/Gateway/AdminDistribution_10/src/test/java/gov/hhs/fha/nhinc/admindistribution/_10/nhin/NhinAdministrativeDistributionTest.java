/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admindistribution._10.nhin;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import gov.hhs.fha.nhinc.admindistribution.nhin.NhinAdminDistributionOrchImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author dunnek
 */
public class NhinAdministrativeDistributionTest {

    private Mockery context;

    public NhinAdministrativeDistributionTest() {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    /**
     * Test of sendAlertMessage method, of class NhinAdministrativeDistribution.
     */
    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final EDXLDistribution body = new EDXLDistribution();
        final NhinAdminDistributionOrchImpl mockImpl = context.mock(NhinAdminDistributionOrchImpl.class);
        final AssertionType assertion = new AssertionType();

        body.setSenderID("test");

        NhinAdministrativeDistribution instance = new NhinAdministrativeDistribution() {

            @Override
            protected AssertionType extractAssertion(WebServiceContext context) {
                return assertion;
            }

            @Override
            protected NhinAdminDistributionOrchImpl getOrchestratorImpl() {
                return mockImpl;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockImpl).sendAlertMessage(body, assertion);
                will(returnValue(null));
            }
        });

        instance.sendAlertMessage(body);
        context.assertIsSatisfied();
    }

}