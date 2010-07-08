/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import org.w3c.dom.Element;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import javax.xml.bind.JAXBElement;

/**
 *
 * @author rayj
 */
public class RetrieveDocumentSetRequestMarshaller {

    private static final String ContextPath = "ihe.iti.xds_b._2007";

    public Element marshal (RetrieveDocumentSetRequestType object) {
        return new Marshaller().marshal(object, ContextPath);

//         ihe.iti.xds_b._2007.ObjectFactory objectFactory = new ihe.iti.xds_b._2007.ObjectFactory();
//        Object something = objectFactory.createRetrieveDocumentSetRequestType();
//        Marshaller marshaller = new Marshaller();
//        Element element = marshaller.marshal(jaxb, ContextPath);
//        return element;
}

    public RetrieveDocumentSetRequestType unmarshal (Element element) {
        RetrieveDocumentSetRequestType unmarshalledObject=null;
        Marshaller marshaller = new Marshaller();
        Object object = marshaller.unmarshal(element, ContextPath);
        if (object instanceof RetrieveDocumentSetRequestType) {
            unmarshalledObject = (RetrieveDocumentSetRequestType) object;
        } else if (object instanceof JAXBElement) {
            JAXBElement jaxb = (JAXBElement) object;
            Object jaxbValue = jaxb.getValue();
            if (jaxbValue instanceof RetrieveDocumentSetRequestType) {
                unmarshalledObject = (RetrieveDocumentSetRequestType) jaxbValue;
            }
        }
        return unmarshalledObject;
    }
}
