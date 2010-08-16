package gov.hhs.fha.nhinc.docquery;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
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
public class DocQueryPolicyCheckerTest {

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
   * Test of checkPolicy method, of class DocQueryPolicyChecker.
   */
  @Test
  public void testValidatePolicyResponse_null_PolicyResponse_returns_false() {
    final CheckPolicyResponseType mockResponse = mockery.mock(CheckPolicyResponseType.class);
    DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

    mockery.checking(new Expectations() {

      {
        one(mockResponse).getResponse();
        will(returnValue(null));
      }
    });

    assertFalse(testSubject.validatePolicyResponse(mockResponse));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of checkPolicy method, of class DocQueryPolicyChecker.
   */
  @Test
  public void testValidatePolicyResponse_null_Result_returns_false() {
    final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
    final ResponseType mockResponse = mockery.mock(ResponseType.class);
    DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

    mockery.checking(new Expectations() {

      {
        atLeast(1).of(mockPolicyResponse).getResponse();
        will(returnValue(mockResponse));
        atLeast(1).of(mockResponse).getResult();
        will(returnValue(null));
      }
    });

    assertFalse(testSubject.validatePolicyResponse(mockPolicyResponse));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of checkPolicy method, of class DocQueryPolicyChecker.
   */
  @Test
  public void testValidatePolicyResponse_DecisionType_not_PERMIT_returns_false() {
    final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
    final ResponseType mockResponse = mockery.mock(ResponseType.class);
    final ResultType mockResult = mockery.mock(ResultType.class);
    final List<ResultType> results = new ArrayList<ResultType>();
    results.add(mockResult);
    
    DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

    mockery.checking(new Expectations() {

      {
        allowing(mockPolicyResponse).getResponse();
        will(returnValue(mockResponse));
        allowing(mockResponse).getResult();
        will(returnValue(results));
        one(mockResult).getDecision();
      }
    });

    assertFalse(testSubject.validatePolicyResponse(mockPolicyResponse));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of checkPolicy method, of class DocQueryPolicyChecker.
   */
  @Test
  public void testValidatePolicyResponse_DecisionType_PERMIT_returns_true() {
    final CheckPolicyResponseType mockPolicyResponse = mockery.mock(CheckPolicyResponseType.class);
    final ResponseType mockResponse = mockery.mock(ResponseType.class);
    final ResultType mockResult = mockery.mock(ResultType.class);
    final List<ResultType> results = new ArrayList<ResultType>();
    results.add(mockResult);
    
    DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();

    mockery.checking(new Expectations() {

      {
        allowing(mockPolicyResponse).getResponse();
        will(returnValue(mockResponse));
        allowing(mockResponse).getResult();
        will(returnValue(results));
        one(mockResult).getDecision();
        will(returnValue(DecisionType.PERMIT));
      }
    });

    assertTrue(testSubject.validatePolicyResponse(mockPolicyResponse));

    mockery.assertIsSatisfied();
  }

  /**
   * Test of getPolicyChecker method, of class DocQueryPolicyChecker.
   */
  @Test
  public void testGetPolicyChecker() {
    DocQueryPolicyChecker testSubject = new DocQueryPolicyChecker();
    PolicyEngineChecker result = testSubject.getPolicyChecker();
    assertNotNull(result);
  }
}
