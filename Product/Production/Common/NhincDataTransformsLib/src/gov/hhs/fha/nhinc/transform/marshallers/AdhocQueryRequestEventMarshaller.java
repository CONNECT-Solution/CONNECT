/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.marshallers;

import gov.hhs.fha.nhinc.common.eventcommon.AdhocQueryRequestEventType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class AdhocQueryRequestEventMarshaller {
    private static final String AdhocQueryRequestEventContextPath = "gov.hhs.fha.nhinc.common.eventcommon";

    public Element marshalAdhocQueryRequestEvent(AdhocQueryRequestEventType object) {
        return new Marshaller().marshal(object, AdhocQueryRequestEventContextPath);
    }

    public AdhocQueryRequestEventType unmarshalAdhocQueryRequestEvent(Element element) {
        return (AdhocQueryRequestEventType) new Marshaller().unmarshal(element, AdhocQueryRequestEventContextPath);
    }
}
