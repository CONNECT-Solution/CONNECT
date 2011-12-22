/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
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
public class AuditTransformerTest {

    public AuditTransformerTest() {
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
     * Test of transform method, of class AuditTransformer.
     */
    @Test
    public void testTransformRequestInterface() {
        Orchestratable message = null;
        AuditTransformer instance = new AuditTransformerImpl();
        instance.transformRequest(message);
        // we are just testing that the interface is good, so this test should suffice
    }

    @Test
    public void testTransformResponseInterface() {
        Orchestratable message = null;
        AuditTransformer instance = new AuditTransformerImpl();
        instance.transformResponse(message);
        // we are just testing that the interface is good, so this test should suffice
    }

    public class AuditTransformerImpl implements AuditTransformer {

        public LogEventRequestType transformRequest(Orchestratable message) {
            return null;
        }

        public LogEventRequestType transformResponse(Orchestratable message) {
            return null;
        }
    }
}
