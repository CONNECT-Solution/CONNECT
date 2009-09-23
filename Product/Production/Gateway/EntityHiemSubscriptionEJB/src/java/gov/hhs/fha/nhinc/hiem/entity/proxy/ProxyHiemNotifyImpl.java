package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxy;
import gov.hhs.fha.nhinc.nhinhiem.proxy.notify.NhinHiemNotifyProxyObjectFactory;
import org.w3c.dom.Element;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 *
 * @author jhoppesc
 */
public class ProxyHiemNotifyImpl
{

    private static Log log = LogFactory.getLog(ProxyHiemNotifyImpl.class);

    public void notify(NotifyRequestType notifyRequest, WebServiceContext context)
    {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, notifyRequest.getAssertion(), notifyRequest.getNhinTargetSystem());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    public void notify(NotifyRequestSecuredType notifyRequest, WebServiceContext context)
    {
        log.debug("Entering ProxyHiemNotifyImpl.notify...");

        Element notifyElement = new SoapUtil().extractFirstElement(context, "notifySoapMessage", "Notify");

        log.debug("NOTIFY MESSAGE RECEIVED FROM SECURED INTERFACE: " + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        log.debug("extracting soap header elements");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
//        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "unsubscribeSoapMessage");
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, "notifySoapMessage");
        log.debug("extracted soap header elements");

        // Audit the HIEM Notify Request Message sent on the Nhin Interface
        AcknowledgementType ack = audit(notifyRequest);

        NhinHiemNotifyProxyObjectFactory hiemNotifyFactory = new NhinHiemNotifyProxyObjectFactory();
        NhinHiemNotifyProxy proxy = hiemNotifyFactory.getNhinHiemNotifyProxy();

        proxy.notify(notifyElement, referenceParametersElements, SamlTokenExtractor.GetAssertion(context), notifyRequest.getNhinTargetSystem());

        log.debug("Exiting ProxyHiemNotifyImpl.notify...");
    }

    private AcknowledgementType audit(NotifyRequestType notifyRequest)
    {
        AcknowledgementType ack = null;

        return ack;
    }

    private AcknowledgementType audit(NotifyRequestSecuredType notifyRequest)
    {
        AcknowledgementType ack = null;

        return ack;
    }
}
