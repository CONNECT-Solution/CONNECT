/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
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
public class AdapterPIPServiceTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterPIPServiceImpl mockServiceImpl = context.mock(AdapterPIPServiceImpl.class);
    final WebServiceContext mockWebServiceContext = context.mock(WebServiceContext.class);

    @Test
    public void testGetAdapterPIPServiceImpl() {
        try {
            AdapterPIPService sut = new AdapterPIPService() {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl() {
                    return mockServiceImpl;
                }
            };

            AdapterPIPServiceImpl serviceImpl = sut.getAdapterPIPServiceImpl();
            assertNotNull("AdapterPIPServiceImpl was null", serviceImpl);
        } catch (Throwable t) {
            System.out.println("Error running testGetAdapterPIPServiceImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPIPServiceImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetWebServiceContext() {
        try {
            AdapterPIPService sut = new AdapterPIPService() {
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
    public void testRetrievePtConsentByPtId() {
        try {
            AdapterPIPService sut = new AdapterPIPService() {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl() {
                    return mockServiceImpl;
                }

                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).retrievePtConsentByPtId(
                            with(aNonNull(RetrievePtConsentByPtIdRequestType.class)),
                            with(aNonNull(WebServiceContext.class)));
                }
            });

            RetrievePtConsentByPtIdRequestType request = new RetrievePtConsentByPtIdRequestType();

            RetrievePtConsentByPtIdResponseType response = sut.retrievePtConsentByPtId(request);
            assertNotNull("RetrievePtConsentByPtIdResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtId: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePtConsentByPtDocId() {
        try {
            AdapterPIPService sut = new AdapterPIPService() {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl() {
                    return mockServiceImpl;
                }

                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).retrievePtConsentByPtDocId(
                            with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)),
                            with(aNonNull(WebServiceContext.class)));
                }
            });

            RetrievePtConsentByPtDocIdRequestType request = new RetrievePtConsentByPtDocIdRequestType();

            RetrievePtConsentByPtDocIdResponseType response = sut.retrievePtConsentByPtDocId(request);
            assertNotNull("RetrievePtConsentByPtDocIdResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePtConsentByPtDocId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePtConsentByPtDocId: " + t.getMessage());
        }
    }

    @Test
    public void testStorePtConsent() {
        try {
            AdapterPIPService sut = new AdapterPIPService() {
                @Override
                protected AdapterPIPServiceImpl getAdapterPIPServiceImpl() {
                    return mockServiceImpl;
                }

                @Override
                protected WebServiceContext getWebServiceContext() {
                    return mockWebServiceContext;
                }
            };
            context.checking(new Expectations() {
                {
                    oneOf(mockServiceImpl).storePtConsent(with(aNonNull(StorePtConsentRequestType.class)),
                            with(aNonNull(WebServiceContext.class)));
                }
            });

            StorePtConsentRequestType request = new StorePtConsentRequestType();

            StorePtConsentResponseType response = sut.storePtConsent(request);
            assertNotNull("StorePtConsentResponseType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testStorePtConsent: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStorePtConsent: " + t.getMessage());
        }
    }

}