package gov.hhs.fha.nhinc.docretrievedeferred.entity.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docretrievedeferred.entity.request.EntityDocRetrieveDeferredReqImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqJavaImplTest {
    private Mockery mockery = null;

    public EntityDocRetrieveDeferredReqJavaImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockery = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCrossGatewayRetrieveRequestHappy()
    {
        final DocRetrieveAcknowledgementType mockAck = mockery.mock(DocRetrieveAcknowledgementType.class);
        final EntityDocRetrieveDeferredReqImpl mockImpl = mockery.mock(EntityDocRetrieveDeferredReqImpl.class);
        EntityDocRetrieveDeferredReqJavaImpl testSubject = new EntityDocRetrieveDeferredReqJavaImpl()
        {
            @Override
            public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target)
            {
                return mockAck;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RetrieveDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
                will(returnValue(mockAck));
            }
        });
        RetrieveDocumentSetRequestType message = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        assertNotNull(testSubject.crossGatewayRetrieveRequest(message, assertion, target));
        mockery.assertIsSatisfied();
    }

    @Test
    public void testCrossGatewayRetrieveRequestBad()
    {
        final EntityDocRetrieveDeferredReqImpl mockImpl = mockery.mock(EntityDocRetrieveDeferredReqImpl.class);
        EntityDocRetrieveDeferredReqJavaImpl testSubject = new EntityDocRetrieveDeferredReqJavaImpl()
        {
            @Override
            public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message, AssertionType assertion, NhinTargetCommunitiesType target)
            {
                return null;
            }
        };

        mockery.checking(new Expectations(){
            {
                allowing(mockImpl).crossGatewayRetrieveRequest(with(any(RetrieveDocumentSetRequestType.class)), with(any(AssertionType.class)), with(any(NhinTargetCommunitiesType.class)));
                will(returnValue(null));
            }
        });
        RetrieveDocumentSetRequestType message = new RetrieveDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType target = new NhinTargetCommunitiesType();
        assertNull(testSubject.crossGatewayRetrieveRequest(message, assertion, target));
        mockery.assertIsSatisfied();
    }
}