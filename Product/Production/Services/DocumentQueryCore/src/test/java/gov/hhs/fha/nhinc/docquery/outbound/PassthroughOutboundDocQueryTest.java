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
package gov.hhs.fha.nhinc.docquery.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryDelegate;
import gov.hhs.fha.nhinc.docquery.entity.OutboundDocQueryOrchestratable;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author akong
 * 
 */
public class PassthroughOutboundDocQueryTest {

    @Test
    public void passthroughOutboundDocQuery() {
        OutboundDocQueryDelegate mockDelegate = mock(OutboundDocQueryDelegate.class);

        AdhocQueryResponse expectedResponse = new AdhocQueryResponse();
        OutboundDocQueryOrchestratable orchestratableResponse = new OutboundDocQueryOrchestratable();
        orchestratableResponse.setResponse(expectedResponse);

        when(mockDelegate.process(any(OutboundDocQueryOrchestratable.class))).thenReturn(orchestratableResponse);

        AdhocQueryRequest request = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        PassthroughOutboundDocQuery passthroughDocQuery = new PassthroughOutboundDocQuery(mockDelegate);
        AdhocQueryResponse actualResponse = passthroughDocQuery.respondingGatewayCrossGatewayQuery(request, assertion,
                targets);

        assertSame(expectedResponse, actualResponse);

    }
    
    @Test
    public void passthroughOutboundDocQueryTooManyTargets() {
    	final String HCID1 = "1.1";
    	final String HCID2 = "2.2";
    	
    	final Logger mockLogger = mock(Logger.class);
    	ArgumentCaptor<Logger> loggerCaptor = ArgumentCaptor.forClass(Logger.class);
    		
    	final String compareOutput = "Multiple targets in request message in passthrough mode." +
    			"  Only sending to target HCID: " + HCID1 + ".  Not sending request to: " + HCID2 + ".";
    	
    	OutboundDocQueryDelegate mockDelegate = mock(OutboundDocQueryDelegate.class);

        AdhocQueryResponse expectedResponse = new AdhocQueryResponse();
        OutboundDocQueryOrchestratable orchestratableResponse = new OutboundDocQueryOrchestratable();
        orchestratableResponse.setResponse(expectedResponse);

        when(mockDelegate.process(any(OutboundDocQueryOrchestratable.class))).thenReturn(orchestratableResponse);

        AdhocQueryRequest request = new AdhocQueryRequest();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        
        NhinTargetCommunityType target1 = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity1 = new HomeCommunityType();
        homeCommunity1.setHomeCommunityId(HCID1);
        target1.setHomeCommunity(homeCommunity1);
        
        NhinTargetCommunityType target2 = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity2 = new HomeCommunityType();
        homeCommunity2.setHomeCommunityId(HCID2);
        target2.setHomeCommunity(homeCommunity2);
        
        targets.getNhinTargetCommunity().add(target1);
        targets.getNhinTargetCommunity().add(target2);


        PassthroughOutboundDocQuery passthroughDocQuery = new PassthroughOutboundDocQuery(mockDelegate){
        	@Override
        	protected void logWarning(String warning){
        		mockLogger.warn(warning);
        	}
        };
        
        AdhocQueryResponse actualResponse = passthroughDocQuery.respondingGatewayCrossGatewayQuery(request, assertion,
                targets);

        verify(mockLogger).warn(loggerCaptor.capture());    	
        assertSame(expectedResponse, actualResponse);
        assertEquals(compareOutput, loggerCaptor.getValue());
        
    }
}
