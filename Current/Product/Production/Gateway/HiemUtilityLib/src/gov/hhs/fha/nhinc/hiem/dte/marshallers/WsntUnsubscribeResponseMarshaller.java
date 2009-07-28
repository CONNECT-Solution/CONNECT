/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class WsntUnsubscribeResponseMarshaller {
    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(UnsubscribeResponse object) {
        return new Marshaller().marshal(object, ContextPath);
    }

    public UnsubscribeResponse unmarshal(Element element) {
        return (UnsubscribeResponse) new Marshaller().unmarshal(element, ContextPath);
    }

}
