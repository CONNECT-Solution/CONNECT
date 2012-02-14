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
package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.*;

import static org.junit.Assert.assertNotNull;

/**
 * Created by User: ralph Date: Jul 29, 2010 Time: 3:39:22 PM
 */
public class AdapterDocRetrieveDeferredReqNoOpImplTest {
    private Mockery context;

    public AdapterDocRetrieveDeferredReqNoOpImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSendToAdapter() {
        //
        // Define mock objects
        //
        final AdapterDocRetrieveDeferredReqProxyNoOpImpl mockAdapterDocRetrieveDeferredReqNoOpImpl;
        final RetrieveDocumentSetRequestType mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType;
        final AssertionType mockAssertionType;
        final DocRetrieveAcknowledgementType mockAck;

        //
        // Define the class to be tested and it's inputs and outputs.
        //
        AdapterDocRetrieveDeferredReqProxyNoOpImpl retrieveRequest;
        RetrieveDocumentSetRequestType req;
        AssertionType assertionType;
        RetrieveDocumentSetRequestType retrieveDocumentSetRequestType;
        DocRetrieveAcknowledgementType ack;

        //
        // Instantiate the mock objects.
        //
        mockAdapterDocRetrieveDeferredReqNoOpImpl = context.mock(AdapterDocRetrieveDeferredReqProxyNoOpImpl.class);
        mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType = context
                .mock(RetrieveDocumentSetRequestType.class);
        mockAssertionType = context.mock(AssertionType.class);
        mockAck = context.mock(DocRetrieveAcknowledgementType.class);

        //
        // Set up the expectations using the instantiated mock objects.
        //
        context.checking(new Expectations() {
            {
                allowing(mockAdapterDocRetrieveDeferredReqNoOpImpl).sendToAdapter(
                        mockRespondingGatewayCrossGatewayRetrieveSecuredRequestType, mockAssertionType);
                will(returnValue(mockAck));
            }
        });

        //
        // Instantiate the object to be tested and it's inputs.
        //
        retrieveRequest = new AdapterDocRetrieveDeferredReqProxyNoOpImpl();
        assertionType = new AssertionType();
        retrieveDocumentSetRequestType = new RetrieveDocumentSetRequestType();
        req = new RetrieveDocumentSetRequestType();

        //
        // Run the test.
        //
        ack = retrieveRequest.sendToAdapter(req, assertionType);

        //
        // Check the results.
        //
        assertNotNull("Ack was null", ack);
        context.assertIsSatisfied();
    }
}
