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
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author mweaver
 */
public class CONNECTOrchestrationBaseTest {

    Mockery context = new JUnit4Mockery();
    OutboundOrchestratable outboundMessage = context.mock(OutboundOrchestratable.class);
    IPropertyAcessor propertyAcessor = context.mock(IPropertyAcessor.class);
    OutboundDelegate outboundDelegate = context.mock(OutboundDelegate.class);

    public CONNECTOrchestrationBaseTest() {
    }

    /**
     * Test of isPolicyOk method, of class CONNECTOrchestrationBase.
     */
    @Test
    public void testIsPolicyOk() {
        Orchestratable message = null;
        Direction direction = null;
        CONNECTOrchestrationBase instance = new DenyCONNECTOrchestrationBaseImpl();
        boolean expResult = false;
        boolean result = instance.isPolicyOk(message, direction);
        assertEquals(expResult, result);
    }

    @Test
    public void testOutboundPolicyFailed() throws PropertyAccessException {

        context.checking(new Expectations() {
            {
                allowing(outboundMessage).getServiceName();

                allowing(outboundMessage).getDelegate();
                will(returnValue(outboundDelegate));

                oneOf(outboundDelegate).createErrorResponse(with(same(outboundMessage)), with(any(String.class)));
            }
        });

        DenyCONNECTOrchestrationBaseImpl instance = new DenyCONNECTOrchestrationBaseImpl();
        instance.processOutboundIfPolicyIsOk(outboundMessage);
    }

    public class DenyCONNECTOrchestrationBaseImpl extends CONNECTOrchestrationBase {

        @Override
        protected Orchestratable processIfPolicyIsOk(Orchestratable message) {
            // TODO Auto-generated method stub
            return null;

        }

        @Override
        protected boolean isPolicyOk(Orchestratable message, Direction direction) {

            return false;
        }

    }
}
