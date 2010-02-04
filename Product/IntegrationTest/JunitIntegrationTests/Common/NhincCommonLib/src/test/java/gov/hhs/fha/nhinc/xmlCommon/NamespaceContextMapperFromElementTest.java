/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xmlCommon;

import javax.xml.namespace.NamespaceContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class NamespaceContextMapperFromElementTest {

    public NamespaceContextMapperFromElementTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void PerformMappingTest() throws Exception {
        Element namespacePrefixMappingElement = XmlUtility.convertXmlToElement("<mynode xmlns:prefix='urn:a' xmlns:prefix2='urn:b'></mynode>");
        NamespaceContext namespaceContext = new NamespaceContextMapperFromElement(namespacePrefixMappingElement);

        String xml = "<p:A xmlns:p='urn:a'><p2:B xmlns:p2='urn:b'>data</p2:B></p:A>";
        String xpathQuery = "//prefix:A/prefix2:B";

        Node resultNode = XmlUtility.performXpathQuery(xml, xpathQuery, namespaceContext);
        assertNotNull(resultNode);
        assertEquals("data", XmlUtility.getNodeValue(resultNode));
    }
}