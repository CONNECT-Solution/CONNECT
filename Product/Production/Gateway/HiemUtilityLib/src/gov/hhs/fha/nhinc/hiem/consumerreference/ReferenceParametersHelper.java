/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.consumerreference;

import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import gov.hhs.fha.nhinc.xmlCommon.XpathHelper;
import java.util.ArrayList;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.WebServiceContext;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author rayj
 */
public class ReferenceParametersHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(ReferenceParametersHelper.class);

    public ReferenceParametersElements createReferenceParameterElements(WebServiceContext context, String messageContextAttributeName) {
        ReferenceParametersElements referenceParameters = null;
        SOAPHeader header = null;
        try {
            log.debug("extract soapheader");
            SoapUtil soaputil = new SoapUtil();
            header = soaputil.extractSoapHeader(context, messageContextAttributeName);
        } catch (SOAPException ex) {
            log.error("failed to extract soapheader", ex);
        //todo: throw new UnableToDestroySubscriptionFault();
        }

        log.debug(XmlUtility.formatElementForLogging("soapheader", header));

        if (header != null) {
            log.debug("converting soap header to reference parameters elements");
            referenceParameters = createReferenceParameterElements(header);
            log.debug("extracted " + referenceParameters.getElements().size() + " reference parameter(s)");
        } else {
            log.warn("unable to extract soap header - assume no reference parameters");
        }
        return referenceParameters;
    }

    public ReferenceParametersElements createReferenceParameterElements(Element soapHeader) {
        log.debug("initialize reference parameters from soap header");
        ReferenceParametersElements elements = new ReferenceParametersElements();
        if (soapHeader != null) {
            log.debug("soap header: [" + XmlUtility.serializeElementIgnoreFaults(soapHeader) + "]");
            for (Integer i = 0; i < soapHeader.getChildNodes().getLength(); i++) {
                Element singleSoapHeader = (Element) soapHeader.getChildNodes().item(i);
                log.debug("single soap header: [" + XmlUtility.serializeElementIgnoreFaults(singleSoapHeader) + "]");
                elements.getElements().add(singleSoapHeader);
            }
        }
        log.debug("reference parameters elements found = " + elements.getElements().size());
        return elements;
    }

    public ReferenceParametersElements createReferenceParameterElementsFromSoapMessage(Element soapMessage) {
        ReferenceParametersElements elements = null;
        if (soapMessage != null) {
            Element soapHeader = XmlUtility.getSingleChildElement(soapMessage, "http://schemas.xmlsoap.org/soap/envelope/", "Header");
            elements = createReferenceParameterElements(soapHeader);
        }
        return elements;
    }

    public ReferenceParametersElements createReferenceParameterElementsFromSubscriptionReference(String subscriptionReferenceXml) throws XPathExpressionException {
        log.debug("extracting reference parameters from subscription reference [" + subscriptionReferenceXml + "]");
        String xpathQuery = "//*[local-name()='ReferenceParameters']";
        return createReferenceParameterElementsFromEndpointReference(subscriptionReferenceXml, xpathQuery);
    }

    public ReferenceParametersElements createReferenceParameterElementsFromConsumerReference(String subscribeXml) throws XPathExpressionException {
        log.debug("extracting reference parameters from subscribe [" + subscribeXml + "]");
        String xpathQuery = "//*[local-name()='ReferenceParameters']";
        return createReferenceParameterElementsFromEndpointReference(subscribeXml, xpathQuery);
    }

    private ReferenceParametersElements createReferenceParameterElementsFromEndpointReference(String xml, String xpathQuery) throws XPathExpressionException {
        log.debug("extracting reference parameters from xml [" + xml + "]");
        log.debug("get endpoint reference using xpath:" + xpathQuery);
        Element endpointReference = (Element) XpathHelper.performXpathQuery(xml, xpathQuery);
        return createReferenceParameterElementsFromEndpointReference(endpointReference);
    }

    private ReferenceParametersElements createReferenceParameterElementsFromEndpointReference(Element endpointReference) throws XPathExpressionException {
        ReferenceParametersElements referenceParametersElements = new ReferenceParametersElements();
        if (endpointReference != null) {
            for (int i = 0; i < endpointReference.getChildNodes().getLength(); i++) {
                Node childNode = endpointReference.getChildNodes().item(i);
                log.debug("processing child node [" + childNode.getNodeName() + "]");
                if (childNode instanceof Element) {
                    log.debug("adding to reference parameters");
                    Element childElement = (Element) childNode;
                    referenceParametersElements.getElements().add(childElement);
                } else {
                    log.debug("not an element - skipping");
                }
            }
        }
        log.debug("referenceParametersElements.getElements().size() = " + referenceParametersElements.getElements().size());
        return referenceParametersElements;
    }
}
