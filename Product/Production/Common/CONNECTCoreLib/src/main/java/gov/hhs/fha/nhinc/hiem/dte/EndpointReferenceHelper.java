/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3._2005._08.addressing.AttributedURIType;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3._2005._08.addressing.ReferenceParametersType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class EndpointReferenceHelper {

    /**
     * @deprecated
     * @param endpointReferenceXml
     * @return
     */
    public EndpointReferenceType createEndpointReference(Element endpointReferenceXml) {
        org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        EndpointReferenceType endpointReference = wsaObjFact.createEndpointReferenceType();

        String address = getAddressFromXml(endpointReferenceXml);
        setAddress(endpointReference, address);

        //todo: handle "ReferenceParameters"
        //todo: handle "Metadata"
        //todo: handle "other"

        return endpointReference;
    }

    private String getAddressFromXml(Element endpointReferenceXml) {
        String address = null;
        Element addressXml = XmlUtility.getSingleChildElement(endpointReferenceXml, Namespaces.WSA, "Address");
        if (addressXml != null) {
            address = XmlUtility.getNodeValue(addressXml);
        }
        return address;
    }

    public EndpointReferenceType createEndpointReferenceAddressOnly(String address) {
        org.w3._2005._08.addressing.ObjectFactory wsaObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        EndpointReferenceType endpointReference = wsaObjFact.createEndpointReferenceType();
        setAddress(endpointReference, address);
        return endpointReference;
    }

    private void setAddress(EndpointReferenceType endpointReference, String address) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        AttributedURIType addressElement = addrObjFact.createAttributedURIType();
        addressElement.setValue(address);
        endpointReference.setAddress(addressElement);
    }

    public void attachSimpleReferenceParameter(EndpointReferenceType endpointReference, String referenceParameterUri, String referenceParameterElementName, String referenceParameterValue) {
        org.w3._2005._08.addressing.ObjectFactory addrObjFact = new org.w3._2005._08.addressing.ObjectFactory();
        ReferenceParametersType refParams = addrObjFact.createReferenceParametersType();

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = docBuilderFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        doc.setXmlStandalone(true);
        Element referenceParameter = doc.createElementNS(referenceParameterUri, referenceParameterElementName);
        referenceParameter.setTextContent(referenceParameterValue);
        refParams.getAny().add(referenceParameter);
        endpointReference.setReferenceParameters(refParams);
    }
}
