/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.w3c.dom.Element;

/**
 *
 * @author dunnek
 */
public class TopicMarshallerTest {

    public TopicMarshallerTest() {
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
    public void testMarshall()
    {
        TopicExpressionType topic = new TopicExpressionType();
        Element element = null;
        TopicMarshaller marshaller = new TopicMarshaller();

        topic.setDialect("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        topic.getContent().add("nhinc:testTopic");
        element = marshaller.marshal(topic);

        assertEquals("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple", element.getAttribute("Dialect"));
        assertEquals("nhinc:testTopic", element.getTextContent());

    }
    @Test
    public void testUnMarshall()
    {
        String xml = "" +
                " <wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:document</wsnt:TopicExpression>";

        TopicExpressionType topic;
        TopicMarshaller marshaller = new TopicMarshaller();

        topic = marshaller.unmarshal(xml);

        assertEquals("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple", topic.getDialect());
    }

}