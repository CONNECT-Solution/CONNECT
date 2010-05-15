/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.lift.payload.builder;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JHOPPESC
 */
public class LiFTPayloadBuilderTest {

    public LiFTPayloadBuilderTest() {
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
     * Test of buildLiFTPayload method, of class LiFTPayloadBuilder.
     */
    @Test
    public void testBuildLiFTPayload() {
        System.out.println("testBuildLiFTPayload");
        ProvideAndRegisterDocumentSetRequestType msg = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        LiFTPayloadBuilder instance = new LiFTPayloadBuilder();

        boolean result = instance.buildLiFTPayload(msg, assertion);
        assertEquals(true, result);
    }

}