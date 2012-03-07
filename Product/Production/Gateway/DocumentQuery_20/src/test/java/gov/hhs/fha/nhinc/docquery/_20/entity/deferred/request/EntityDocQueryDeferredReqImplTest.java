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
package gov.hhs.fha.nhinc.docquery._20.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.docquery._20.entity.deferred.request.EntityDocQueryDeferredReqImpl;
import gov.hhs.fha.nhinc.docquery.entity.deferred.request.EntityDocQueryDeferredReqOrchImpl;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author goldmanm
 */
public class EntityDocQueryDeferredReqImplTest {

  Mockery mockery;

  @Before
  public void setup() {
    mockery = new Mockery() {

      {
        setImposteriser(ClassImposteriser.INSTANCE);
      }
    };
  }

  /**
   * Test of setMessageID method, of class EntityDocQueryDeferredReqImpl.
   */
  @Test
  public void testSetMessageID() {
    final AssertionType mockAssertion = mockery.mock(AssertionType.class);
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);

    EntityDocQueryDeferredReqImpl testSubject = new EntityDocQueryDeferredReqImplImpl();

    mockery.checking(new Expectations() {

      {
        one(mockAssertion).setMessageId(with(any(String.class)));
        ignoring(mockContext);
      }
    });

    testSubject.setMessageID(mockAssertion, mockContext);
    mockery.assertIsSatisfied();

  }

  /**
   * Test of setMessageID method, of class EntityDocQueryDeferredReqImpl
   * with null AssertionType.
   */
  @Test
  public void testSetMessageID_AssertionType_null() {
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);

    EntityDocQueryDeferredReqImpl testSubject = new EntityDocQueryDeferredReqImplImpl();

    mockery.checking(new Expectations() {

      {
        never(mockContext);
      }
    });

    testSubject.setMessageID(null, mockContext);
    mockery.assertIsSatisfied();

  }

  /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReqImpl.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery_RespondingGatewayCrossGatewayQueryRequestType_WebServiceContext() {
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
    final AssertionType mockAssertion = mockery.mock(AssertionType.class);
    final RespondingGatewayCrossGatewayQueryRequestType mockBody = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
    final EntityDocQueryDeferredReqOrchImpl mockOrchImpl = mockery.mock(EntityDocQueryDeferredReqOrchImpl.class);
    final AdhocQueryRequest mockRequest = mockery.mock(AdhocQueryRequest.class);
    final NhinTargetCommunitiesType mockTargetComm = mockery.mock(NhinTargetCommunitiesType.class);

    EntityDocQueryDeferredReqImpl testSubject = new EntityDocQueryDeferredReqImplImpl() {

      @Override
      protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
      }

      @Override
      protected EntityDocQueryDeferredReqOrchImpl getOrchImpl() {
        return mockOrchImpl;
      }
    };

    mockery.checking(new Expectations() {

      {
        one(mockBody).getAssertion();
        will(returnValue(mockAssertion));
        one(mockBody).getAdhocQueryRequest();
        will(returnValue(mockRequest));
        one(mockBody).getNhinTargetCommunities();
        will(returnValue(mockTargetComm));
        never(mockContext);
        one(mockOrchImpl).respondingGatewayCrossGatewayQuery(with(same(mockRequest)), with(same(mockAssertion)), with(same(mockTargetComm)));
      }
    });

    testSubject.respondingGatewayCrossGatewayQuery(mockBody, mockContext);
    mockery.assertIsSatisfied();
  }

  /**
   * Test of respondingGatewayCrossGatewayQuery method, of class EntityDocQueryDeferredReqImpl.
   */
  @Test
  public void testRespondingGatewayCrossGatewayQuery_RespondingGatewayCrossGatewayQuerySecuredRequestType_WebServiceContext() {
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
    final AssertionType mockAssertion = mockery.mock(AssertionType.class);
    final RespondingGatewayCrossGatewayQuerySecuredRequestType mockBody = mockery.mock(RespondingGatewayCrossGatewayQuerySecuredRequestType.class);
    final EntityDocQueryDeferredReqOrchImpl mockOrchImpl = mockery.mock(EntityDocQueryDeferredReqOrchImpl.class);
    final AdhocQueryRequest mockRequest = mockery.mock(AdhocQueryRequest.class);
    final NhinTargetCommunitiesType mockTargetComm = mockery.mock(NhinTargetCommunitiesType.class);

    EntityDocQueryDeferredReqImpl testSubject = new EntityDocQueryDeferredReqImplImpl() {

      @Override
      protected AssertionType extractAssertion(WebServiceContext context) {
        return mockAssertion;
      }

      @Override
      protected void setMessageID(AssertionType assertion, final WebServiceContext context) {
      }

      @Override
      protected EntityDocQueryDeferredReqOrchImpl getOrchImpl() {
        return mockOrchImpl;
      }
    };

    mockery.checking(new Expectations() {

      {
        one(mockBody).getAdhocQueryRequest();
        will(returnValue(mockRequest));
        one(mockBody).getNhinTargetCommunities();
        will(returnValue(mockTargetComm));
        never(mockContext);
        one(mockOrchImpl).respondingGatewayCrossGatewayQuery(with(same(mockRequest)), with(same(mockAssertion)), with(same(mockTargetComm)));
      }
    });

    testSubject.respondingGatewayCrossGatewayQuery(mockBody, mockContext);
    mockery.assertIsSatisfied();
  }

  /**
   * Test of getOrchImpl method, of class EntityDocQueryDeferredReqImpl.
   */
  @Test
  public void testGetEntityDocQueryDeferredReqOrchImpl() {
    EntityDocQueryDeferredReqImplImpl testSubject = new EntityDocQueryDeferredReqImplImpl();

    EntityDocQueryDeferredReqOrchImpl result1 = testSubject.getOrchImpl();
    assertNotNull(result1);

    EntityDocQueryDeferredReqOrchImpl result2 = testSubject.getOrchImpl();
    assertSame(result2, result1);
  }

  public class EntityDocQueryDeferredReqImplImpl extends EntityDocQueryDeferredReqImpl {
  }
}
