/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.roottopicextractor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
@Ignore
public class removeNamespaceHelperTest {

    public removeNamespaceHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void removeNamespaceHolderSimple() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("{namespace}value"));
    }
    @Test
    public void removeNamespaceHolderPreData() {
        assertEquals("xxvalue", RootTopicExtractorHelper.removeNamespaceHolder("xx{namespace}value"));
    }
    @Test
    public void removeNamespaceHolderMultipleParts() {
        assertEquals("value/value2", RootTopicExtractorHelper.removeNamespaceHolder("{namespace}value/{namespace2}value2"));
    }
    @Test
    public void removeNamespaceHolderEmptyNamespace() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("{}value"));
    }
    @Test
    public void removeNamespaceHolderNoNamespace() {
        assertEquals("value", RootTopicExtractorHelper.removeNamespaceHolder("value"));
    }

    @Test
    public void testReplaceNamespacePrefixesWithNamespaces() throws Exception {
    }
}