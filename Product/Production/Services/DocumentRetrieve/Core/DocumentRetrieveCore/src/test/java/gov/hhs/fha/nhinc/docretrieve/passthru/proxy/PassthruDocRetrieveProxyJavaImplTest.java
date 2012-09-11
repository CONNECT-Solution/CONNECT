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
package gov.hhs.fha.nhinc.docretrieve.passthru.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.passthru.NhincProxyDocRetrieveOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
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
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class PassthruDocRetrieveProxyJavaImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final NhincProxyDocRetrieveOrchImpl mockOrchImpl = context.mock(NhincProxyDocRetrieveOrchImpl.class);

    @Test
    public void testCreateLogger() {
        try {
            PassthruDocRetrieveProxyJavaImpl sut = new PassthruDocRetrieveProxyJavaImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }
            };
            Log log = sut.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetNhincProxyDocRetrieveOrchImpl() {
        try {
            PassthruDocRetrieveProxyJavaImpl sut = new PassthruDocRetrieveProxyJavaImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected NhincProxyDocRetrieveOrchImpl getNhincProxyDocRetrieveOrchImpl() {
                    return mockOrchImpl;
                }
            };
            NhincProxyDocRetrieveOrchImpl orchImpl = sut.getNhincProxyDocRetrieveOrchImpl();
            assertNotNull("NhincProxyDocRetrieveOrchImpl was null", orchImpl);
        } catch (Throwable t) {
            System.out.println("Error running testGetNhincProxyDocRetrieveOrchImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetNhincProxyDocRetrieveOrchImpl: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveHappy() {
        try {
            PassthruDocRetrieveProxyJavaImpl sut = new PassthruDocRetrieveProxyJavaImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected NhincProxyDocRetrieveOrchImpl getNhincProxyDocRetrieveOrchImpl() {
                    return mockOrchImpl;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    allowing(mockOrchImpl).respondingGatewayCrossGatewayRetrieve(
                            with(aNonNull(RetrieveDocumentSetRequestType.class)), with(aNonNull(AssertionType.class)),
                            with(aNonNull(NhinTargetSystemType.class)));
                }
            });
            RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
            AssertionType assertion = new AssertionType();
            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            RetrieveDocumentSetResponseType response = sut.respondingGatewayCrossGatewayRetrieve(request, assertion,
                    targetSystem);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRespondingGatewayCrossGatewayRetrieveHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveHappy: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayCrossGatewayRetrieveNullOrchImpl() {
        try {
            PassthruDocRetrieveProxyJavaImpl sut = new PassthruDocRetrieveProxyJavaImpl() {
                @Override
                protected Log createLogger() {
                    return mockLog;
                }

                @Override
                protected NhincProxyDocRetrieveOrchImpl getNhincProxyDocRetrieveOrchImpl() {
                    return null;
                }
            };
            context.checking(new Expectations() {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });
            RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
            AssertionType assertion = new AssertionType();
            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            RetrieveDocumentSetResponseType response = sut.respondingGatewayCrossGatewayRetrieve(request, assertion,
                    targetSystem);
            assertNull("testRespondingGatewayCrossGatewayRetrieveNullOrchImpl was not null", response);
        } catch (Throwable t) {
            System.out
                    .println("Error running testRespondingGatewayCrossGatewayRetrieveNullOrchImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayCrossGatewayRetrieveNullOrchImpl: " + t.getMessage());
        }
    }

}