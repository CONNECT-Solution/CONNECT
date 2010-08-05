/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquerydeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mark Goldman
 */
public class EntityDocQueryDeferredRequestHelperTest {

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
   * Test of getAdhocQueryRequest method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAdhocQueryRequest_RespondingGatewayCrossGatewayQueryRequestType() {
    final RespondingGatewayCrossGatewayQueryRequestType mockRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    mockery.checking(new Expectations() {

      {
        one(mockRequest).getAdhocQueryRequest();
        will(returnValue(new AdhocQueryRequest()));
      }
    });

    assertNotNull(testSubject.getAdhocQueryRequest(mockRequest));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getAdhocQueryRequest method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAdhocQueryRequest_RespondingGatewayCrossGatewayQueryRequestType_null() {
    RespondingGatewayCrossGatewayQueryRequestType request = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getAdhocQueryRequest(request));
  }

  /**
   * Test of getAdhocQueryRequest method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAdhocQueryRequest_RespondingGatewayCrossGatewayQuerySecuredRequestType() {
    final RespondingGatewayCrossGatewayQuerySecuredRequestType mockRequest = mockery.mock(RespondingGatewayCrossGatewayQuerySecuredRequestType.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    mockery.checking(new Expectations() {

      {
        one(mockRequest).getAdhocQueryRequest();
        will(returnValue(new AdhocQueryRequest()));
      }
    });

    assertNotNull(testSubject.getAdhocQueryRequest(mockRequest));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getAdhocQueryRequest method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAdhocQueryRequest_RespondingGatewayCrossGatewayQuerySecuredRequestType_null() {
    RespondingGatewayCrossGatewayQuerySecuredRequestType request = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getAdhocQueryRequest(request));
  }

  /**
   * Test of getAssertion method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAssertion_RespondingGatewayCrossGatewayQueryRequestType() {
    final RespondingGatewayCrossGatewayQueryRequestType mockRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    mockery.checking(new Expectations() {

      {
        one(mockRequest).getAssertion();
        will(returnValue(new AssertionType()));
      }
    });

    assertNotNull(testSubject.getAssertion(mockRequest));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getAssertion method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAssertion_RespondingGatewayCrossGatewayQueryRequestType_null() {
    RespondingGatewayCrossGatewayQueryRequestType request = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getAssertion(request));
  }

  /**
   * Test of getAssertion method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAssertion_WebServiceContext() {
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper() {

      @Override
      protected AssertionType extractAssertion(WebServiceContext context) {
        return new AssertionType();
      }
    };

    assertNotNull(testSubject.getAssertion(mockContext));
  }

  /**
   * Test of getAssertion method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetAssertion_WebServiceContext_null() {
    WebServiceContext context = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getAssertion(context));
  }

  /**
   * Test of getNhinTargetCommunities method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetNhinTargetCommunities_RespondingGatewayCrossGatewayQueryRequestType() {
    final RespondingGatewayCrossGatewayQueryRequestType mockRequest = mockery.mock(RespondingGatewayCrossGatewayQueryRequestType.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    mockery.checking(new Expectations() {

      {
        one(mockRequest).getNhinTargetCommunities();
        will(returnValue(new NhinTargetCommunitiesType()));
      }
    });

    assertNotNull(testSubject.getNhinTargetCommunities(mockRequest));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getNhinTargetCommunities method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetNhinTargetCommunities_RespondingGatewayCrossGatewayQueryRequestType_null() {
    RespondingGatewayCrossGatewayQueryRequestType request = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getNhinTargetCommunities(request));
  }

  /**
   * Test of getNhinTargetCommunities method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetNhinTargetCommunities_RespondingGatewayCrossGatewayQuerySecuredRequestType() {
    final RespondingGatewayCrossGatewayQuerySecuredRequestType mockRequest = mockery.mock(RespondingGatewayCrossGatewayQuerySecuredRequestType.class);
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    mockery.checking(new Expectations() {

      {
        one(mockRequest).getNhinTargetCommunities();
        will(returnValue(new NhinTargetCommunitiesType()));
      }
    });

    assertNotNull(testSubject.getNhinTargetCommunities(mockRequest));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getNhinTargetCommunities method, of class EntityDocQueryDeferredRequestHelper.
   */
  @Test
  public void testGetNhinTargetCommunities_RespondingGatewayCrossGatewayQuerySecuredRequestType_null() {
    RespondingGatewayCrossGatewayQuerySecuredRequestType request = null;
    EntityDocQueryDeferredRequestHelper testSubject = new EntityDocQueryDeferredRequestHelper();

    assertNull(testSubject.getNhinTargetCommunities(request));
  }

}
