/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer.Direction;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class NhinDocRetrievePolicyTransformer_g0Test {

    public NhinDocRetrievePolicyTransformer_g0Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of tranform method, of class NhinDocRetrievePolicyTransformer_g0.
     */
    @Test
    public void testTranform() {
        /*NhinDocRetrievePolicyTransformer_g0 instance = new NhinDocRetrievePolicyTransformer_g0();

        RetrieveDocumentSetRequestType req = new RetrieveDocumentSetRequestType();
        DocumentRequest dreq = new DocumentRequest();
        dreq.setDocumentUniqueId("1");
        dreq.setHomeCommunityId("1");
        dreq.setRepositoryUniqueId("1");
        req.getDocumentRequest().add(dreq);
        Orchestratable message = new NhinDocRetrieveOrchestratableImpl_g0(req, null, instance, null, null);
        Direction direction = PolicyTransformer.Direction.INBOUND;
        CheckPolicyRequestType expResult = null;
        CheckPolicyRequestType result = instance.tranform(message, direction);
        assertEquals(expResult, result);*/
        // TODO: this test requires mocking of the CheckPolicyRequestType and NhinDocRetrieveOrchestratableImpl_g0 classes.
    }

}