/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class NhincCommonAcknowledgementMarshallerTest {

    public NhincCommonAcknowledgementMarshallerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testMarshal() {
        AcknowledgementType object = new AcknowledgementType () ;
        object.setMessage("hello world");
        NhincCommonAcknowledgementMarshaller marshaller = new NhincCommonAcknowledgementMarshaller();
        Element element = marshaller.marshal(object);
        assertNotNull(element);
    }

    @Test
    public void testUnmarshal() throws Exception {
        String xml = "" +
                "<urn:Acknowledgement xmlns:urn='urn:gov:hhs:fha:nhinc:common:nhinccommon'>" +
                "   <urn:message>hello world</urn:message>" +
                "</urn:Acknowledgement>";

        Element element = XmlUtility.convertXmlToElement(xml);

        NhincCommonAcknowledgementMarshaller marshaller = new NhincCommonAcknowledgementMarshaller();
        AcknowledgementType object = marshaller.unmarshal(element);
        assertNotNull(object);
        assertEquals("hello world", object.getMessage());
    }
}