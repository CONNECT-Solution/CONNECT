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
package gov.hhs.fha.nhinc.hiem.processor.common;

import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.Subscribe;

/**
 * Utility for subscription item objects.
 * 
 * @author Neil Webb
 */
public class SubscriptionItemUtil {
	private static final Logger LOG = Logger.getLogger(SubscriptionItemUtil.class);
	
    /**
     * Create a subscription item from subscription info
     * 
     * @param subscribe Subscription message
     * @param rawSubscribeXml Optional subscribe XML. If not provided, the subscribe will be serialized to an XML
     *            string.
     * @param parentSubscriptionReferenceXML Parent subscription reference
     * @param consumer Notification consumer
     * @param producer Notification producer
     * @param targets Optional NHIN targets if this is a targeted entity subscribe
     * @return Subscription item
     */
    public HiemSubscriptionItem createSubscriptionItem(Subscribe subscribe,
            String parentSubscriptionReferenceXML, String consumer, String producer, String targets) {
        HiemSubscriptionItem subscriptionItem = null;
        if (subscribe != null) {
            subscriptionItem = new HiemSubscriptionItem();
            
            subscriptionItem.setSubscribeXML(marshalSubscribe(subscribe));
            
            subscriptionItem.setSubscriptionReferenceXML(extractSubscriptionReference(subscribe));
            subscriptionItem.setRootTopic(extractRootTopic(subscribe));
            subscriptionItem.setParentSubscriptionReferenceXML(parentSubscriptionReferenceXML);
            subscriptionItem.setConsumer(consumer);
            subscriptionItem.setProducer(producer);
            subscriptionItem.setTargets(targets);
        }
        return subscriptionItem;
    }

    private String marshalSubscribe(Subscribe subscribe) {
        String subscribeXml = null;
        if (subscribe != null) {
        	// write it out as XML
            JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Subscribe.class);
	            StringWriter writer = new StringWriter();
	            jaxbContext.createMarshaller().marshal(subscribe, writer);
	        	subscribeXml = writer.toString();
			} catch (JAXBException e) {
				LOG.error("Failed to marshal Subscribe Object:" + e.getErrorCode());
			}
        }
        return subscribeXml;
    }

    private String extractRootTopic(Subscribe subscribe) {
        String rootTopic = null;
        if (subscribe != null) {
            // TODO: Finish
        }
        return rootTopic;
    }

    private String extractSubscriptionReference(Subscribe subscribe) {
        String subscriptionReference = null;
        if (subscribe != null) {
            // TODO: Finish
        }
        return subscriptionReference;
    }
}
