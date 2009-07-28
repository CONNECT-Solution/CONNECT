package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;

/**
 * 
 * 
 * @author Neil Webb
 */
public class SubscriptionHandlerFactory {
    // Child adapter subscription mode
    private static Log log = LogFactory.getLog(SubscriptionHandlerFactory.class);

    public SubscriptionHandler getSubscriptionHandler() throws SubscribeCreationFailedFault, ConfigurationException {
        SubscriptionHandler subscriptionHandler = null;

        ConfigurationManager config = new ConfigurationManager();
        String childAdapterSubscriptionMode = config.getAdapterSubscriptionMode();
        log.debug("child adapter subscription mode = " + childAdapterSubscriptionMode ) ;
        if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_SUBSCRIPTIONS.equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new ChildSubscriptionModeSubscriptionHandler();
        } else if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_FORWARD.equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new ForwardChildModeSubscriptionHandler();
        } else if (NhincConstants.HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED.equals(childAdapterSubscriptionMode)) {
            subscriptionHandler = new DisabledChildModeSubscriptionHandler();
        } else {
            throw new SoapFaultFactory().getUnknownSubscriptionServiceAdapterModeFault(childAdapterSubscriptionMode);
        }
        log.debug("subscriptionHandler = " + subscriptionHandler.toString() ) ;
        return subscriptionHandler;
    }
}
