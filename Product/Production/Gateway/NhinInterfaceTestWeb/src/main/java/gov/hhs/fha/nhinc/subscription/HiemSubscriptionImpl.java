/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription;

import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincsubscription.NhincNotificationProducerService;
import gov.hhs.fha.nhinc.nhincsubscription.NotificationProducer;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;

/**
 *
 * @author jhoppesc
 */
public class HiemSubscriptionImpl {

    private static Log log = LogFactory.getLog(HiemSubscriptionImpl.class);
    private static final String SERVICE_NAME = "mocknotificationproducer";

    public static SubscribeResponse subscribe(Subscribe subscribeRequest, WebServiceContext context) {
        log.debug("Entering HiemSubscriptionImpl.subscribe");

        SubscribeResponse resp = new SubscribeResponse();
        SubscribeRequestType request = new SubscribeRequestType();

        request.setSubscribe(subscribeRequest);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));


        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincNotificationProducerService service = new NhincNotificationProducerService();
            NotificationProducer port = service.getNotificationProducerPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            try {
                resp = port.subscribe(request);
            } catch (Exception e) {
                log.error("Received Fault: " + e.getMessage());
                resp = null;
            }
        } else {
            resp = null;
        }

        log.debug("Exiting HiemSubscriptionImpl.subscribe");
        return resp;
    }
}
