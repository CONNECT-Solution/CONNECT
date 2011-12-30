/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
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
public class EntityDocRetrieveAuditTransformer_a0Test {

    public EntityDocRetrieveAuditTransformer_a0Test() {
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
     * Test of transformRequest method, of class EntityDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformRequest() {
        System.out.println("transformRequest");
        Orchestratable message = null;
        EntityDocRetrieveAuditTransformer_a0 instance = new EntityDocRetrieveAuditTransformer_a0();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequest(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of transformResponse method, of class EntityDocRetrieveAuditTransformer_a0.
     */
    @Test
    public void testTransformResponse() {
        System.out.println("transformResponse");
        Orchestratable message = null;
        EntityDocRetrieveAuditTransformer_a0 instance = new EntityDocRetrieveAuditTransformer_a0();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformResponse(message);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}