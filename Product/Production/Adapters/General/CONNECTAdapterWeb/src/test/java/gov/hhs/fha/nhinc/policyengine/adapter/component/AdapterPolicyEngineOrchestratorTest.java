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
package gov.hhs.fha.nhinc.policyengine.adapter.component;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import javax.xml.ws.WebServiceContext;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class AdapterPolicyEngineOrchestratorTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterComponentPolicyEngineImpl mockServiceImpl = context.mock(AdapterComponentPolicyEngineImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testGetAdapterComponentPolicyEngineImpl() {
        try {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator() {
                @Override
                protected AdapterComponentPolicyEngineImpl getAdapterComponentPolicyEngineImpl() {
                    return mockServiceImpl;
                }
            };

            AdapterComponentPolicyEngineImpl serviceImpl = sut.getAdapterComponentPolicyEngineImpl();
            assertNotNull("AdapterComponentPolicyEngineImpl was null", serviceImpl);
        } catch (Throwable t) {
            System.out.println("Error running testGetAdapterComponentPolicyEngineImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterComponentPolicyEngineImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext() {
        try {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator() {
                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };

            WebServiceContext wsContext = sut.getWebServiceContext();
            assertNotNull("WebServiceContext was null", wsContext);
        } catch (Throwable t) {
            System.out.println("Error running testGetWebServiceContext: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetWebServiceContext: " + t.getMessage());
        }
    }

    @Test
    public void testCheckPolicy() {
        try {
            AdapterPolicyEngineOrchestrator sut = new AdapterPolicyEngineOrchestrator() {
                @Override
                protected AdapterComponentPolicyEngineImpl getAdapterComponentPolicyEngineImpl() {
                    return mockServiceImpl;
                }

                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).checkPolicy(with(aNonNull(CheckPolicyRequestType.class)),
                            with(aNonNull(WebServiceContext.class)));
                }
            });

            CheckPolicyRequestType request = new CheckPolicyRequestType();

            CheckPolicyResponseType response = sut.checkPolicy(request);
            assertNotNull("CheckPolicyResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testCheckPolicy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCheckPolicy: " + t.getMessage());
        }
    }

}
