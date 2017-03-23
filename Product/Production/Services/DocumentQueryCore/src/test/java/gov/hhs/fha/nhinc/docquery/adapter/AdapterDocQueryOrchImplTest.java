/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.docquery.AdhocQueryResponseAsserter;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocQueryOrchImplTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final AdapterRedactionEngineProxy mockRedactionEngineProxy = context.mock(AdapterRedactionEngineProxy.class);

    @Test
    public void testGetRedactionEngineProxy() {
        try {
            AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl() {

                @Override
                protected AdapterRedactionEngineProxy getRedactionEngineProxy() {
                    return mockRedactionEngineProxy;
                }
            };

            AdapterRedactionEngineProxy redactionEngineProxy = docQueryImpl.getRedactionEngineProxy();
            assertNotNull("Redaction engine proxy was null", redactionEngineProxy);
        } catch (Throwable t) {
            System.out.println("Error running testGetRedactionEngineProxy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetRedactionEngineProxy test: " + t.getMessage());
        }
    }

    @Test
    public void errorResponseHasRegistryObjectList() throws Exception {
        AdapterDocQueryOrchImpl impl = new AdapterDocQueryOrchImpl();
        AdhocQueryResponse response = impl.respondingGatewayCrossGatewayQuery(null, null);
        AdhocQueryResponseAsserter.assertSchemaCompliant(response);
    }

    // @Test
    // public void testCallRedactionEngineHappy()
    // {
    // try
    // {
    // AdhocQueryRequest mockQueryRequest = context.mock(AdhocQueryRequest.class);
    // AdhocQueryResponse mockQueryResponse = context.mock(AdhocQueryResponse.class);
    //
    // AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
    // {
    // @Override
    // protected Log createLogger()
    // {
    // return mockLog;
    // }
    // @Override
    // protected AdapterRedactionEngineProxy getRedactionEngineProxy()
    // {
    // return mockRedactionEngineProxy;
    // }
    // };
    // context.checking(new Expectations()
    // {
    // {
    // allowing(mockLog).debug(with(any(String.class)));
    // one(mockRedactionEngineProxy).filterAdhocQueryResults(with(aNonNull(AdhocQueryRequest.class)),
    // with(aNonNull(AdhocQueryResponse.class)));
    // }
    // });
    //
    // AdhocQueryResponse response = docQueryImpl.callRedactionEngine(mockQueryRequest, mockQueryResponse);
    // assertNotNull("AdhocQueryResponse returned was null", response);
    // }
    // catch(Throwable t)
    // {
    // System.out.println("Error running testCallRedactionEngineHappy test: " + t.getMessage());
    // t.printStackTrace();
    // fail("Error running testCallRedactionEngineHappy test: " + t.getMessage());
    // }
    // }

    // @Test
    // public void testCallRedactionEngineNullInputResponse()
    // {
    // try
    // {
    // AdhocQueryRequest mockQueryRequest = context.mock(AdhocQueryRequest.class);
    // AdhocQueryResponse mockQueryResponse = null;
    //
    // AdapterDocQueryOrchImpl docQueryImpl = new AdapterDocQueryOrchImpl()
    // {
    // @Override
    // protected Log createLogger()
    // {
    // return mockLog;
    // }
    // @Override
    // protected AdapterRedactionEngineProxy getRedactionEngineProxy()
    // {
    // return mockRedactionEngineProxy;
    // }
    // };
    // context.checking(new Expectations()
    // {
    // {
    // allowing(mockLog).debug(with(any(String.class)));
    // one(mockLog).warn("Did not call redaction engine because the query response was null.");
    // }
    // });
    //
    // AdhocQueryResponse response = docQueryImpl.callRedactionEngine(mockQueryRequest, mockQueryResponse);
    // assertNull("AdhocQueryResponse returned was not null", response);
    // }
    // catch(Throwable t)
    // {
    // System.out.println("Error running testCallRedactionEngineNullInputResponse test: " + t.getMessage());
    // t.printStackTrace();
    // fail("Error running testCallRedactionEngineNullInputResponse test: " + t.getMessage());
    // }
    // }

}