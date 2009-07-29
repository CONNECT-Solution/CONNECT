/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.xml;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class HiemSubscribeXmlAdapterWebServiceProxyTest {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(HiemSubscribeXmlAdapterWebServiceProxy.class);

    public HiemSubscribeXmlAdapterWebServiceProxyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSubscribe() throws Exception {
        HiemSubscribeXmlAdapterProxy proxy = new HiemSubscribeXmlAdapterWebServiceProxy();

        String url = "http://localhost:18081/mockAdapterSubscribe";
        NhinTargetSystemType target = new NhinTargetSystemType();
        target.setUrl(url);

        String subscribeXml = "" +
                "<wsnt:Subscribe xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' xmlns:wsa='http://www.w3.org/2005/08/addressing'>" +
                "     <wsnt:ConsumerReference>" +
                "        <wsa:Address>https://localhost:8181/NotificationConsumerService/HiemNotifyTestHelper</wsa:Address>" +
                "     </wsnt:ConsumerReference>" +
                "     <wsnt:Filter>" +
                "        <wsnt:TopicExpression Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhin='http://www.hhs.gov/healthit/nhin'>SomeTopic</wsnt:TopicExpression>" +
                "     </wsnt:Filter>" +
                "</wsnt:Subscribe>";
        Node subscribe = XmlUtility.convertXmlToElement(subscribeXml);

        AssertionType assertion = null;
        Node result = proxy.subscribe(subscribe, assertion, target);

        log.info("result:" + XmlUtility.serializeNode(result));

    }
}