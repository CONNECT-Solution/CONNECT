/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
public class AdhocQueryTest {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Marshaller.class);
    private String message = "" +
            "<?xml version='1.0' encoding='UTF-8'?>" +
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:wsa='http://www.w3.org/2005/08/addressing'>" +
            "    <soapenv:Header>" +
            "        <wsa:Action soapenv:mustUnderstand='0'>urn:ihe:iti:2007:CrossGatewayQueryResponse</wsa:Action>" +
            "        <wsa:RelatesTo soapenv:mustUnderstand='0'>uuid:fdfd1907-8cc9-47a8-a94e-f08d3ceef322</wsa:RelatesTo>" +
            "    </soapenv:Header>" +
            "    <soapenv:Body>" +
            "        <AdhocQueryResponse status='urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success' totalResultCount='1' xmlns='urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0'>" +
            "            <ns1:RegistryObjectList xmlns:ns1='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0'>" +
            "            </ns1:RegistryObjectList>" +
            "        </AdhocQueryResponse>" +
            "    </soapenv:Body>" +
            "</soapenv:Envelope>";

    @Test
    public void UnmarshallResponse() throws Exception {
        AdhocQueryResponse response = null;
        Object unmarshalledObject = null;
        Marshaller marshaller = new Marshaller();

        Element messageElement = XmlUtility.convertXmlToElement(message);
        Element adhocQueryElement = XmlUtility.getSingleChildElement(messageElement, "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0", "AdhocQueryResponse");

        unmarshalledObject = marshaller.unmarshal(adhocQueryElement, "oasis.names.tc.ebxml_regrep.xsd.query._3");
        response = (AdhocQueryResponse) unmarshalledObject;
    }

    @Test
    public void MarksVersion() throws Exception {
        AdhocQueryResponse result = null;
//            log.info("FX: FxDocumentRegistryHelper: buildAdhocQueryImpl");
//            String infoAdhocQueryRequestXml = toXml(body, null);
//            log.info("FX: adhocQueryRequestXml = <" + infoAdhocQueryRequestXml + ">");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
//            String adhocQueryRequestXml = FxDocQueryImpl.wrapAdHocQueryRequest(body);
//            log.info("FX: adhocQueryRequestXml = <" + adhocQueryRequestXml + ">");
        String adhocQueryResponseXml = message;
        log.info("FX: adhocQueryResponseXml = <" + adhocQueryResponseXml + ">");
        final byte[] adhocQueryResponseBytes = adhocQueryResponseXml.getBytes();
        ByteArrayInputStream b = new ByteArrayInputStream(adhocQueryResponseBytes);
        org.w3c.dom.Document document = loader.parse(b);
        NodeList responseNodes = document.getElementsByTagName("AdhocQueryResponse");
        log.info("responseNodes.getLength() = " + responseNodes.getLength());
        AdhocQueryResponse response;
        if (responseNodes.getLength() > 0) {
//log.info("FX: colin " + FxDocQueryImpl.toXml(new AdhocQueryResponse(), null));
            Node responseNode = responseNodes.item(0);
            Element responseElement = (Element) responseNode;

            log.info("FX: responseNode = " + XmlUtility.serializeElement(responseElement));
            log.info("response element namespace " + responseElement.getNamespaceURI());
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jaxbContext = oHandler.getJAXBContext("oasis.names.tc.ebxml_regrep.xsd.rim._3");
            //JAXBContext.newInstance(AdhocQueryResponse.class);
            javax.xml.bind.Unmarshaller u = jaxbContext.createUnmarshaller();
            //result = (AdhocQueryResponse) u.unmarshal(responseNode);

//            StringReader stringReader = new StringReader(XmlUtility.serializeElement(responseElement));
//            log.debug("Calling unmarshal");
//            Object o = u.unmarshal(stringReader);
//            log.info("FX: Result class = " + o.getClass().getName());
//            log.info("FX: Result = " + o);
            Marshaller marshaller = new Marshaller();
            Object unmarshalledObject = marshaller.unmarshal(responseElement, "oasis.names.tc.ebxml_regrep.xsd.query._3");
        response = (AdhocQueryResponse) unmarshalledObject;

        }
    }
}
