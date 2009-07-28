/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.fta;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import static org.junit.Assert.*;

/**
 *
 * @author dunnek
 */
public class UtilTest {

    public UtilTest() {
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

    @Test
    public void testMarshallNotify()
    {
        gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
        marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();
        org.w3c.dom.Element element;

        TopicExpressionType topic = new TopicExpressionType();
        topic.setDialect("http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        topic.getContent().add("nhinc:testTopic");

        element = marshaller.marshal(topic);

        System.out.println(element.toString());
        System.out.println(element.getParentNode().getTextContent());
        System.out.println(element.getTextContent());
        System.out.println(element.getOwnerDocument().getTextContent());
    }
       
    @Test
    public void testUnMarshallNotify()
    {
        String xml = "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:testTopic</wsnt:TopicExpression>";
        org.oasis_open.docs.wsn.b_2.TopicExpressionType topic;
        gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
        marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();
        //topic = Util.unmarshalTopic(xml);

        
        topic = marshaller.unmarshal(xml);

        assertEquals("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple", topic.getDialect());
        assertEquals("nhinc:testTopic", topic.getContent().get(0));

    }
    @Test
    public void testByteArrayConversion()
    {
        String expValue = "blah, blah, blah";
        String result;

        byte[] array;
        
        array = Util.convertToByte(expValue);

        result = Util.convertToString(array);

        assertEquals(expValue, result);
    }
    @Test
    public void testMarshaller()
    {
        String expValue = "blah, blah, blah";
        String result;

        org.w3c.dom.Element marshalledElement;

        marshalledElement = Util.marshalPayload(expValue);

        result = Util.unmarshalPayload(marshalledElement);

        assertEquals(expValue, result);
    }
}