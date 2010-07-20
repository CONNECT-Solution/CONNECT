package gov.hhs.fha.nhinc.nhinhiem.proxy.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotificationProducerService;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.RespectBindingFeature;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * ######## NOTICE #########
 * This class is not currently used due to problems placing SAML assertion
 * data in the header. It has been retained to allow future research on this 
 * issue.
 *
 * @author Jon Hoppesch
 */
public class NhinHiemSubscribeXmlWebServiceProxy implements NhinHiemSubscribeProxy {

    private static Log log = LogFactory.getLog(NhinHiemSubscribeWebServiceProxy.class);
    static NotificationProducerService nhinService = new NotificationProducerService();

    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        Element resp = null;

        Dispatch<Source> sourceDispatch = getNotificationProducerDispath(target, assertion);

        try
        {
            if(sourceDispatch != null)
            {
                Document subscribeRequestDocument = buildSubscribeRequestMessage(subscribeElement);

                Source input = new DOMSource(subscribeRequestDocument);
                Source result = sourceDispatch.invoke(input);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                javax.xml.transform.Result streamResult = new javax.xml.transform.stream.StreamResult(bos);

                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(result, streamResult);

                String resultXml = new String(bos.toByteArray());
                resp = parseSubscribeResponse(resultXml);
            }
            else
            {
                log.error("The source dispatch object was null");
            }
        }
        catch (ParserConfigurationException ex)
        {
            log.error("ParserConfigurationException sending subscribe message: " + ex.getMessage(), ex);
            // TODO: Create and throw generic exception
        }
        catch (TransformerConfigurationException ex)
        {
            log.error("TransformerConfigurationException sending subscribe message: " + ex.getMessage(), ex);
            // TODO: Create and throw generic exception
        }
        catch (TransformerException ex)
        {
            log.error("TransformerException sending subscribe message: " + ex.getMessage(), ex);
            // TODO: Create and throw generic exception
        }
        return resp;
    }

    private Dispatch<Source> getNotificationProducerDispath(NhinTargetSystemType target, AssertionType assertion)
    {
        Dispatch<Source> sourceDispatch = null;

        org.oasis_open.docs.wsn.bw_2.NotificationProducerService service = new org.oasis_open.docs.wsn.bw_2.NotificationProducerService();

        QName portQName = new QName("http://docs.oasis-open.org/wsn/bw-2" , "NotificationProducerPort");

        List<WebServiceFeature> wsFeatures = new ArrayList<WebServiceFeature>();
//        wsFeatures.add(new AddressingFeature());
        wsFeatures.add(new RespectBindingFeature());
        WebServiceFeature[] wsFeatureArray = wsFeatures.toArray(new WebServiceFeature[0]);
        sourceDispatch = service.createDispatch(portQName, Source.class, Service.Mode.PAYLOAD, wsFeatureArray);

        // Set endpoint URL
        String url = getUrl(target);
        sourceDispatch.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

//        sourceDispatch.getRequestContext().put(javax.xml.ws.BindingProvider.SOAPACTION_URI_PROPERTY, "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest");

        // Add assertion data
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.AUDIT_QUERY_ACTION);
        sourceDispatch.getRequestContext().putAll(requestContext);

        return sourceDispatch;
    }

    private String getUrl(NhinTargetSystemType target)
    {
        String url = null;
        try
        {
            url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);
        }
        catch (ConnectionManagerException ex)
        {
            log.error("Error obtaining endpoint url: " + ex.getMessage(), ex);
        }

        return url;
    }

    private Document buildSubscribeRequestMessage(Element subscribe) throws ParserConfigurationException, DOMException {
        Document document = null;
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        document = docBuilder.newDocument();
        Element subscribeRequestElement = null;
        subscribeRequestElement = document.createElementNS("http://docs.oasis-open.org/wsn/b-2", "SubscribeRequest");
        Node subscribeNode = document.importNode(subscribe, true);
        subscribeRequestElement.appendChild(subscribeNode);
        document.appendChild(subscribeRequestElement);
        return document;
    }
//    private NotificationProducer getPort(String url) {
//        NotificationProducer port = nhinService.getNotificationProducerPort();
//
//        log.info("Setting endpoint address to Nhin Hiem Subscribe Service to " + url);
//        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
//
//        return port;
//    }

//    private Subscribe unmarshalSubscribe(String subscribeXml)
//    {
//        log.debug("Begin unmarshal subscribe");
//        Subscribe subscribe = null;
//
//        if (subscribeXml != null)
//        {
//            try
//            {
//                JAXBContext jc = JAXBContext.newInstance("org.oasis_open.docs.wsn.b_2");
//                Unmarshaller unmarshaller = jc.createUnmarshaller();
//                StringReader srSubscribeXML = new StringReader(subscribeXml);
//                log.debug("Calling unmarshal");
//                subscribe = (Subscribe)unmarshaller.unmarshal(srSubscribeXML);
//            }
//            catch (Exception e)
//            {
//                log.error("Failed to marshall Subscribe to XML: " + e.getMessage(), e);
//            }
//        }
//        log.debug("End unmarshal subscribe");
//        return subscribe;
//    }
    // TODO: Move to a common library - needs access to b-2.wsdl(?) messages
    private Element parseSubscribeResponse(String responseXml) throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault, SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault, UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault
    {
        Element responseElement = null;
        try
        {
            responseElement = XmlUtility.convertXmlToElement(responseXml);
        }
        catch (Exception ex)
        {
            log.error("Error parsing subscribe response: " + ex.getMessage(), ex);
        }
        return responseElement;
    }
}
