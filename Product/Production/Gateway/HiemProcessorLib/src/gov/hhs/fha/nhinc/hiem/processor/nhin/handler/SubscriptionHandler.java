package gov.hhs.fha.nhinc.hiem.processor.nhin.handler;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3c.dom.Element;

/**
 * 
 * 
 * @author Neil Webb
 */
public interface SubscriptionHandler
{
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault;
}
