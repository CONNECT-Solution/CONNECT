/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.subscribe.nhin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.hiem.processor.common.SubscriptionStorage;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 * Base subscription handler
 *
 * @author Neil Webb
 */
abstract class BaseSubscriptionHandler implements SubscriptionHandler {

    protected Log log = null;
    protected SubscriptionStorage subscriptionStorage;

    public BaseSubscriptionHandler() {
        subscriptionStorage = new SubscriptionStorage();
        log = LogFactory.getLog(getClass());
        log.debug("Constructor called");
    }

    @Override
    abstract public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault;

    protected EndpointReferenceType storeSubscriptionItem(HiemSubscriptionItem subscriptionItem) {
        log.debug("In storeSubscriptionItem");
        return subscriptionStorage.storeSubscriptionItem(subscriptionItem);
    }

    protected HiemSubscriptionItem createSubscriptionItem(Element subscribe, String producer, String consumer)
            throws SubscribeCreationFailedFault {

        String rawSubscribe;
        try {
            rawSubscribe = XmlUtility.serializeElement(subscribe);
        } catch (Exception ex) {
            throw new SoapFaultFactory().getMalformedSubscribe("Unable to serialize subscribe element", ex);
        }

        HiemSubscriptionItem subscription = null;
        if (subscribe != null) {
            log.debug("Creating subscription item");
            subscription = new HiemSubscriptionItem();

            subscription.setSubscribeXML(rawSubscribe);
            // TODO: finish
            subscription.setProducer(producer);
            subscription.setConsumer(consumer);
            log.debug("Finished creating subscription item");
        } else {
            log.debug("Subscribe message was null in createSubscriptionItem");
        }
        return subscription;
    }
}
