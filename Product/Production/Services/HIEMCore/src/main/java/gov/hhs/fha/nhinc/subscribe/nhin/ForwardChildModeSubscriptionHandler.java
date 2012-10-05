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

import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxy;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;

/**
 *
 * @author rayj
 */
class ForwardChildModeSubscriptionHandler extends BaseSubscriptionHandler {

    @Override
    public SubscribeResponse handleSubscribe(Element subscribe) throws SubscribeCreationFailedFault {
        log.debug("In ForwardChildModeSubscriptionHandler.handleSubscribe");
        AssertionType assertion = null;
        NhinTargetSystemType target = null;

        log.info("initialize HiemSubscribeAdapterProxyObjectFactory");
        HiemSubscribeAdapterProxyObjectFactory adapterFactory = new HiemSubscribeAdapterProxyObjectFactory();
        log.info("initialize HIEM subscribe adapter proxy");
        HiemSubscribeAdapterProxy adapterProxy = adapterFactory.getHiemSubscribeAdapterProxy();

        try {
            log.info("begin invoke HIEM subscribe adapter proxy");
            adapterProxy.subscribe(subscribe, assertion, target);
            log.info("end invoke HIEM subscribe adapter proxy");
        } catch (Exception ex) {
            log.error("failed to forward subscribe to adapter", ex);
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        // Build subscription item
        log.debug("Calling createSubscriptionItem");
        HiemSubscriptionItem subscription = createSubscriptionItem(subscribe, "gateway", "nhin");

        // Store subscription
        log.debug("Calling storeSubscriptionItem");
        EndpointReferenceType subRef = storeSubscriptionItem(subscription);

        SubscribeResponse response = null;
        response = new SubscribeResponse();
        response.setSubscriptionReference(subRef);

        return response;
    }
}
