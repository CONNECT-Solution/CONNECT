/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn;

import java.util.ArrayList;
import java.util.List;
import javax.xml.soap.SOAPException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import nhinc.wsn.helpers.SubscriptionReferenceHelper;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import nhinc.wsn.entities.subscriptionItem;
import nhinc.wsn.helpers.XmlHelper;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumerService;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.xml.ws.developer.WSBindingProvider;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import javax.xml.soap.SOAPBody;
import nhinc.wsn.entities.filters.IFilterCheck;
import nhinc.wsn.entities.filters.SimpleTopicFilter;
import org.oasis_open.docs.wsn.b_2.FilterType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.ws.WebServiceContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import nhinc.wsn.jaxb.Marshaler;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author rayj
 */
public class Brain {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Brain.class);    //todo: all kinds of thread safety concerns
    private static List<subscriptionItem> _SubscriptionList = new ArrayList<subscriptionItem>();

    private void marshalAndUnmarshal(Subscribe subscribe, WebServiceContext context)
    {
        log.debug("*** Begin marshal and unmarshal");
        Marshaler marshaler = new Marshaler();
//        String xml = marshaler.marshalSubscribe(subscribe);
        
        
        String xml = getRawSubscribe(context);
        Subscribe back = marshaler.unmarshalSubscribe(xml);
        SubscribeSender sender = new SubscribeSender();
        SubscribeResponse response = sender.sendSubscribe(back);
        log.debug("*** End marshal and unmarshal");
    }
    
    public SubscribeResponse StoreSubscription(Subscribe subscribe, WebServiceContext context) {
        dumpSoapMessage(context);
        marshalAndUnmarshal(subscribe, context);
        
        subscriptionItem item = new subscriptionItem();

        item.subscribe = subscribe;
        item.subscriptionReference = SubscriptionReferenceHelper.CreateSubscriptionReference();
        item.setRawSubscribe(getRawSubscribe(context));
        
        TopicExpressionType topic = GetTopic(subscribe);

        IFilterCheck topicFilter = new SimpleTopicFilter(topic);
        item.Filters.add(topicFilter);
        item.Filters.add(topicFilter);
// ElementNSImpl element = (ElementNSImpl) topic.getContent().get(0) ;
// NamespacePrefixMapper npm = new NamespacePrefixMapper();
// element.getOwnerDocument()
//NamespaceContext nc;
//nc.getNamespaceURI(prefix);

//        JAXBElement<TopicExpressionType> jbElement = (JAXBElement<TopicExpressionType>) subscribe.getFilter().getAny().get(0);
//        TopicExpressionType topicExpression = jbElement.getValue();
//        String topicValue = (String) topicExpression.getContent().get(0);

        _SubscriptionList.add(item);

        SubscribeResponse response = new SubscribeResponse();
        response.setSubscriptionReference(item.subscriptionReference);
        return response;
    }
    
    private String getRawSubscribe(WebServiceContext context)
    {
        String rawSubscribe = null;
        SOAPMessage soapMessage = extractSoapMessage(context);
        if(soapMessage != null)
        {
            try
            {
                // Dump body
                SOAPBody soapBody = soapMessage.getSOAPBody();
                log.debug("Soap body to string: " + soapBody.toString());
                
                // Dump namespaces
                Iterator namespaceIter = soapBody.getVisibleNamespacePrefixes();
                if(namespaceIter != null)
                {
                    while(namespaceIter.hasNext())
                    {
                        String namespacePrefix = (String)namespaceIter.next();
                        String namespace = soapBody.getNamespaceURI(namespacePrefix);
                        log.debug("Namespace - " + namespacePrefix + ":" + namespace);
                    }
                }
                
                // Get first element (should be Subscribe and dump
                Node subscribeNode = soapBody.getFirstChild();
                if((subscribeNode != null) && (subscribeNode instanceof Element))
                {
                    String bNamespaceVal = subscribeNode.lookupNamespaceURI("b");
                    log.debug("The namespace value for b: " + bNamespaceVal);

                    NamedNodeMap subAttrMap = subscribeNode.getAttributes();
                    if(subAttrMap != null)
                    {
                        int length = subAttrMap.getLength();
                        for(int i = 0; i < length; i++)
                        {
                            Node attrNode = subAttrMap.item(i);
                            log.debug("Attribute = " + attrNode.getNodeName() + ":" + attrNode.getNodeValue());
                        }
                        Attr testAttr = subscribeNode.getOwnerDocument().createAttribute("xmlns:b");
                        testAttr.setNodeValue("http://docs.oasis-open.org/wsn/b-2");
                        subAttrMap.setNamedItem(testAttr);
                        testAttr = subscribeNode.getOwnerDocument().createAttribute("xmlns:b");
                        testAttr.setNodeValue("http://docs.oasis-open.org/wsn/b-2");
                        subAttrMap.setNamedItem(testAttr);
                    }
                    
                    
                    OutputFormat outputFormat = new OutputFormat(soapBody.getOwnerDocument());
                    outputFormat.setIndenting(true);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    XMLSerializer serializer = new XMLSerializer(bos, outputFormat);
                    Element subscribeElement = (Element)subscribeNode;
                    
                    try
                    {
                        serializer.serialize(subscribeElement);
                    }
                    catch (IOException ioe)
                    {
                        log.error("IOException serializing subscribe: " + ioe.getMessage(), ioe);
                    }
                    rawSubscribe = bos.toString();
                    log.debug("Serialized subscribe: " + rawSubscribe);
                    
                    // Now go back
                    
                }
                else
                {
                    log.debug("Subscribe node invalid");
                }
            } 
            catch (SOAPException ex)
            {
                log.error("SOAPException reading soap body: " + ex.getMessage(), ex);
            }

        }
        return rawSubscribe;
    }
    
    private SOAPMessage extractSoapMessage(WebServiceContext context)
    {
        SOAPMessage soapMessage = null;
        if(context != null)
        {
            MessageContext msgContext = context.getMessageContext();
            if(msgContext != null)
            {
                javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
                soapMessage = (SOAPMessage)servletRequest.getAttribute("subscribeSoapMessage");
            }
        }
        return soapMessage;
    }
    
    private static void dumpSoapMessage(WebServiceContext context)
    {
        @SuppressWarnings("unchecked")
        MessageContext msgContext = context.getMessageContext();
        if(msgContext != null)
        {
            javax.servlet.http.HttpServletRequest servletRequest = (javax.servlet.http.HttpServletRequest)msgContext.get(MessageContext.SERVLET_REQUEST);
            SOAPMessage soapMessage = (SOAPMessage)servletRequest.getAttribute("subscribeSoapMessage");
                if(soapMessage != null)
                {
                    log.debug("******** Attempting to write out SOAP message *************");
                    try
                    {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        soapMessage.writeTo(bos);
                        String soapMessageText = new String(bos.toByteArray());
                        log.debug("Extracted soap message: " + soapMessageText);
                    }
                    catch (Throwable t)
                    {
                        log.debug("Exception writing out the message");
                        t.printStackTrace();
                    }
                }
                else
                {
                    log.debug("SOAPMessage was null");
                }
        }
    }

    private static org.oasis_open.docs.wsn.b_2.TopicExpressionType GetTopic(Subscribe subscribe) {
        org.oasis_open.docs.wsn.b_2.TopicExpressionType topicExpression = null;
        FilterType filter = subscribe.getFilter();
        if (filter != null) {
            for (Object filterItem : filter.getAny()) {
                org.oasis_open.docs.wsn.b_2.TopicExpressionType value = null;
                value = GetTopicExpressionFromAny(filterItem);
                if (value != null) {
                    topicExpression = value;
                }
            }
        }
        return topicExpression;
    }

    private static org.oasis_open.docs.wsn.b_2.TopicExpressionType GetTopicExpressionFromAny(Object value) {
        org.oasis_open.docs.wsn.b_2.TopicExpressionType topicExpression = null;
        if (value instanceof JAXBElement) {
            JAXBElement jb = (JAXBElement) value;
            topicExpression = GetTopicExpressionFromAny(jb.getValue());
        } else if (value instanceof org.oasis_open.docs.wsn.b_2.TopicExpressionType) {
            topicExpression = (org.oasis_open.docs.wsn.b_2.TopicExpressionType) value;
        }

        return topicExpression;
    }

    public static void Publish(Notify notify) {
        //for each subscription, check to see if filters match, and if so, send notify
        for (subscriptionItem item : _SubscriptionList) {
            //todo: check for null, don't be lazy and just catch exception
            try {
                //todo: check to see if we should send to this address
                //todo: modify notify - are there any fields that need to be updated before sending?
                boolean meetsCriteria = true;
                for (IFilterCheck filter : item.Filters) {
                    meetsCriteria = meetsCriteria && filter.MeetsCriteria(notify);
                }

                if (meetsCriteria) {
                    SendNotification(notify, item.subscribe, item.subscriptionReference);
                }
            } catch (Exception ex) {
                //todo: handle exception.. errr... not sure how
                log.error(ex);
            }
        }
    }

    private static void SendNotification(Notify notify, Subscribe subscribe, W3CEndpointReference subscriptionReference) {
        String address = getConsumerReferenceAddress(subscribe);

        // Call Web Service Operation
        NotificationConsumerService service = new org.oasis_open.docs.wsn.bw_2.NotificationConsumerService();
        log.debug("Got service");
        NotificationConsumer port = service.getNotificationConsumerPort();
        log.debug("Got Port");
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
        log.debug("Set endpoint");

        notify.getNotificationMessage().get(0).setSubscriptionReference(subscriptionReference);

        WSBindingProvider bp = (WSBindingProvider) port;
        //bp.setOutboundHeaders(Headers.create(new QName("http://www.hhs.gov/healthit/nhin", "SubscriptionId", "nhin"), collectSubscriptionId(subscriptionReference)));
        List<Header> headers = new ArrayList<Header>();

        Document document = XmlHelper.transformEndpointReferenceToDocument(subscribe.getConsumerReference());
        NodeList referenceParameterNodes = XmlHelper.getSingleNodeChildren(document, "ReferenceParameters");
        if (referenceParameterNodes != null) {
            for (int i = 0; i < referenceParameterNodes.getLength(); i++) {
                Node referenceParameterNode = referenceParameterNodes.item(i);
                Element referenceParameterElement = (Element) referenceParameterNode;
                Header header = Headers.create(referenceParameterElement);
                headers.add(header);
            }
        }
        bp.setOutboundHeaders(headers);

        port.notify(notify);
    }

    private static String getConsumerReferenceAddress(Subscribe nhinSubscribe) {
        String consumerReferenceAddress = XmlHelper.getNodeValue(getConsumerReferenceEndpointNodeDoc(nhinSubscribe), "Address");
        //log.info("consumerReferenceAddress=" + consumerReferenceAddress);
        return consumerReferenceAddress;
    }

    private static Document getConsumerReferenceEndpointNodeDoc(Subscribe nhinSubscribe) {
        W3CEndpointReference endpointRef = nhinSubscribe.getConsumerReference();
        Document endpointNodeDoc = null;

        if (endpointRef != null) {
            DOMResult domresult = new DOMResult();
            endpointRef.writeTo(domresult);
            Node endpointNode = domresult.getNode();

            endpointNodeDoc = (Document) endpointNode;
        }
        return endpointNodeDoc;
    }

    public static void ClearSubscriptionList() {
        _SubscriptionList = new ArrayList<subscriptionItem>();
    }

    public static int GetSubscriptionListCount() {
        return _SubscriptionList.size();
    }
}
