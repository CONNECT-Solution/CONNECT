/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.notify;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersHelper;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincsubscription.NhincNotificationConsumerService;
import gov.hhs.fha.nhinc.nhincsubscription.NotificationConsumer;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;

/**
 *
 * @author jhoppesc
 */
public class HiemNotifyImpl {

    private static Log log = LogFactory.getLog(HiemNotifyImpl.class);
    private static final String SERVICE_NAME = "mocknotificationconsumer";

    public static void notify(Notify notifyRequest, WebServiceContext context) {
        log.debug("Entering HiemNotifyImpl.notify");

        NotifyRequestType request = new NotifyRequestType();

        request.setNotify(notifyRequest);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        log.debug("extracting reference parameters from soap header");
        ReferenceParametersHelper referenceParametersHelper = new ReferenceParametersHelper();
        ReferenceParametersElements referenceParametersElements = referenceParametersHelper.createReferenceParameterElements(context, NhincConstants.HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE);
        log.debug("extracted reference parameters from soap header");


        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincNotificationConsumerService service = new NhincNotificationConsumerService();
            NotificationConsumer port = service.getNotificationConsumerPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

            try {
                port.notify(request);
            } catch (Exception e) {
                log.error("Received Fault: " + e.getMessage());
            }
        }

        log.debug("Exiting HiemNotifyImpl.notify");
    }
}
