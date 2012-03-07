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
package gov.hhs.fha.nhinc.subscription.repository.service;

import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.subscription.repository.data.HiemSubscriptionItem;
import java.util.List;
import org.junit.Ignore;
import org.w3._2005._08.addressing.EndpointReferenceType;

/**
 * 
 * @author Neil Webb
 */
@Ignore
public class SubscriptionItemServiceTest {

    @Test
    public void placeholdertest() {
    }

    // @Test
    public void testRead() {
        System.out.println("Begin testRead");
        try {
            HiemSubscriptionRepositoryService service = new HiemSubscriptionRepositoryService();
            service.emptyRepository();
            assertEquals("Subscription repository was not empty before start.", 0, service.subscriptionCount());

            HiemSubscriptionItem subscriptionItem = createBaseSubscriptionItem();
            // String subRefXml =
            // "<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">"
            // +
            // "  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
            // "  <wsa:ReferenceParameters>" +
            // "	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">402880c120d3ac0d0120d3ac0e230001</nhin:SubscriptionId>"
            // +
            // "  </wsa:ReferenceParameters>" +
            // "</b:SubscriptionReference>";
            String subRefXml = "<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">"
                    + "  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>"
                    + "  <wsa:ReferenceParameters>"
                    + "	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">402880c120d3ac0d0120d3ac00010001</nhin:SubscriptionId>"
                    + "  </wsa:ReferenceParameters>" + "</b:SubscriptionReference>";
            EndpointReferenceType subscriptionReference = service.saveSubscriptionToConnect(subscriptionItem);
            assertNotNull("Subscription reference was null", subscriptionReference);

            // subscriptionItem =
            // service.retreiveBySubscriptionReference(SubscriptionStorageItemServiceTest.SUBSCRIPTION_REFERENCE_XML);
            subscriptionItem = service.retrieveByLocalSubscriptionReference(subRefXml);
            assertNotNull("Subscription item was null", subscriptionItem);
        } catch (Throwable t) {
            System.out.println("Exception encountered in testRead: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }
        System.out.println("End testRead");
    }

    // @Test
    // public void testReadByParentSubRef()
    // {
    // System.out.println("Begin testReadByParentSubRef");
    // try
    // {
    // String subRefXml =
    // "<b:SubscriptionReference xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:b=\"http://docs.oasis-open.org/wsn/b-2\">"
    // +
    // "  <wsa:Address>https://158.147.185.174:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
    // "  <wsa:ReferenceParameters>" +
    // "	 <nhin:SubscriptionId xmlns:nhin=\"http://www.hhs.gov/healthit/nhin\">777666555</nhin:SubscriptionId>" +
    // "  </wsa:ReferenceParameters>" +
    // "</b:SubscriptionReference>";
    // SubscriptionRepositoryService service = new SubscriptionRepositoryService();
    // List<SubscriptionItem> subscriptionItems = service.retrieveByParentSubscriptionReference(subRefXml);
    // assertNotNull("Subscription item list was null", subscriptionItems);
    // assertFalse("Empty response", subscriptionItems.isEmpty());
    // }
    // catch(Throwable t)
    // {
    // System.out.println("Exception encountered in testReadByParentSubRef: " + t.getMessage());
    // t.printStackTrace();
    // fail(t.getMessage());
    // }
    // System.out.println("End testReadByParentSubRef");
    // }
    private HiemSubscriptionItem createBaseSubscriptionItem() {
        HiemSubscriptionItem subscriptionItem = new HiemSubscriptionItem();
        // subscriptionItem.setSubscribeXML(SubscriptionStorageItemServiceTest.SUBSCRIBE_XML);
        // subscriptionItem.setSubscriptionReferenceXML(SubscriptionStorageItemServiceTest.SUBSCRIPTION_REFERENCE_XML);
        // subscriptionItem.setRootTopic(SubscriptionStorageItemServiceTest.ROOT_TOPIC);
        // subscriptionItem.setParentSubscriptionReferenceXML(SubscriptionStorageItemServiceTest.PARENT_SUBSCRIPTION_REFERENCE_XML);
        // subscriptionItem.setConsumer(SubscriptionStorageItemServiceTest.CONSUMER);
        // subscriptionItem.setProducer(SubscriptionStorageItemServiceTest.PRODUCER);
        // subscriptionItem.setTargets(SubscriptionStorageItemServiceTest.TARGETS);
        // subscriptionItem.setCreationDate(SubscriptionStorageItemServiceTest.CREATION_DATE);
        return subscriptionItem;
    }
}