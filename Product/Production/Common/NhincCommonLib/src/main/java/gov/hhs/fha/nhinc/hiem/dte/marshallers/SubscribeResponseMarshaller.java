package gov.hhs.fha.nhinc.hiem.dte.marshallers;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 *
 * @author Neil Webb
 */
public class SubscribeResponseMarshaller {
    private static final String ContextPath = "org.oasis_open.docs.wsn.b_2";

    public Element marshal(SubscribeResponse object) {
        return new Marshaller().marshal(object, ContextPath);
    }

    public SubscribeResponse unmarshal(Element element) {
        return (SubscribeResponse) new Marshaller().unmarshal(element, ContextPath);
    }
}
