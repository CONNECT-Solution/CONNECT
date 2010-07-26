/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docretrievedeferred.entity.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
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
 * @author svalluripalli
 */
public class EntityDocRetrieveDeferredReqSecuredTest {
    private Mockery context;

    public EntityDocRetrieveDeferredReqSecuredTest() {
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
    public void testCrossGatewayRetrieveRequestSecured() {
        final DocRetrieveAcknowledgementType mockDocRetrieveAcknowledgementType = context.mock(DocRetrieveAcknowledgementType.class);
        RespondingGatewayCrossGatewayRetrieveSecuredRequestType req = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
        req.setRetrieveDocumentSetRequest(new RetrieveDocumentSetRequestType());
        req.setNhinTargetCommunities(new NhinTargetCommunitiesType());
        EntityDocRetrieveDeferredReqSecured testSubject = new EntityDocRetrieveDeferredReqSecured()
        {
            @Override
            protected DocRetrieveAcknowledgementType getResponse(ExtractEntityDocRetrieveDeferredRequestValues oExtract, RetrieveDocumentSetRequestType retrieveDocumentSetRequest, AssertionType assertion, NhinTargetCommunitiesType nhinTargetCommunities)
            {
                return mockDocRetrieveAcknowledgementType;
            }
        };
        DocRetrieveAcknowledgementType ack = testSubject.crossGatewayRetrieveRequest(req);
        assertNotNull(ack);
    }
}
