/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class SubscribeMarshaller {

    private static final String SubscribeContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshalSubscribe(Subscribe object) {
        return new Marshaller().marshal(object, SubscribeContextPath);
    }

    public Subscribe unmarshalSubscribe(Element element) {
        return (Subscribe) new Marshaller().unmarshal(element, SubscribeContextPath);
    }
}
