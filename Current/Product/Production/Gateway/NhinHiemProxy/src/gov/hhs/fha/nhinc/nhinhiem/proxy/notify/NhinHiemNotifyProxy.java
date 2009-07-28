package gov.hhs.fha.nhinc.nhinhiem.proxy.notify;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import org.w3c.dom.Element;


/**
 *
 * @author jhoppesc
 */
public interface NhinHiemNotifyProxy {

    public void notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,AssertionType assertion, NhinTargetSystemType target);
}
