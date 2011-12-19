/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

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
    public void testTransform() {
        System.out.println("transform");
        Orchestratable message = null;
        AuditTransformer instance = new AuditTransformerImpl();
        instance.transform(message);
        // we are just testing that the interface is good, so this test should suffice
    }

    public class AuditTransformerImpl implements AuditTransformer {
        public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transform(Orchestratable message) {
            return null;
        }
    }

}