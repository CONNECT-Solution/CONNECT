/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.dte;

import gov.hhs.fha.nhinc.hiem.dte.marshallers.NotificationMessageMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.NotifyMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscriptionReferenceMarshaller;
import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class NotifyBuilder {

    /**
     * this is intended to be used to build an "outbound" notification message
     * based on an "inbound" notify and matching subscribe.  This includes updating
     * the notify with the subscribe's subscription reference and wrapping
     * the notification message in a notify
     * @param notificationMessage
     * @param subscribe
     * @return
     */
    public Element buildNotifyFromSubscribe(Element notificationMessageElement, Element subscriptionReferenceElement) {
        NotificationMessageMarshaller notificationMessageMarshaller = new NotificationMessageMarshaller();
        NotificationMessageHolderType notificationMessage = notificationMessageMarshaller.unmarshal(notificationMessageElement);
        notificationMessage.setSubscriptionReference(null);

        SubscriptionReferenceMarshaller subscriptionReferenceMarshaller = new SubscriptionReferenceMarshaller();
        EndpointReferenceType subscriptionReference = subscriptionReferenceMarshaller.unmarshal(subscriptionReferenceElement);
        notificationMessage.setSubscriptionReference(subscriptionReference);

        Notify notify = new Notify();
        notify.getNotificationMessage().add(notificationMessage);

        NotifyMarshaller notifyMarshaller = new NotifyMarshaller();
        Element notifyElement = notifyMarshaller.marshal(notify);

        return notifyElement;
    }
}
