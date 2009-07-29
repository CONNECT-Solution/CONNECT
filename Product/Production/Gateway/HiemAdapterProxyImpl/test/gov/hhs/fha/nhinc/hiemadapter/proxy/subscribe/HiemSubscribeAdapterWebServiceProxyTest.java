/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class HiemSubscribeAdapterWebServiceProxyTest {

    public HiemSubscribeAdapterWebServiceProxyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testSubscribe() throws Exception {
        String subscribeXml="" +
          "<wsnt:Subscribe xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' xmlns:wsa='http://www.w3.org/2005/08/addressing'> " +
          "  <wsnt:ConsumerReference> " +
          "     <wsa:Address>https://localhost:8181/NotificationConsumerService/HiemNotifyTestHelper</wsa:Address> " +
          "  </wsnt:ConsumerReference> " +
          "  <wsnt:Filter> " +
          "     <wsnt:TopicExpression Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhin='http://www.hhs.gov/healthit/nhin'>SomeTopic</wsnt:TopicExpression> " +
          "  </wsnt:Filter> " +
          "</wsnt:Subscribe> " ;

        Element subscribe = XmlUtility.convertXmlToElement(subscribeXml);


        String url = "http://localhost:18081/mockAdapterSubscribe";
        NhinTargetSystemType target= new NhinTargetSystemType();
        target.setUrl(url);

        AssertionType assertion = new AssertionType();

        HiemSubscribeAdapterProxy proxy = new HiemSubscribeAdapterWebServiceProxy();
        proxy.subscribe(subscribe, assertion, target);
    }

}