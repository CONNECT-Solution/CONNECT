/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class SampleUnitTest {

    public SampleUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void failTest() {
        System.out.println("forcing unit test to fail");
        fail("force build to fail");
    }

    @Test
    public void passTest() {
        System.out.println("forcing unit test to pass");
    }
}
