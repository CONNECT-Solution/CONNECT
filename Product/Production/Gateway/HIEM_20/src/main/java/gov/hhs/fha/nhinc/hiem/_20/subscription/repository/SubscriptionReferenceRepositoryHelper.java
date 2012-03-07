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
package gov.hhs.fha.nhinc.hiem._20.subscription.repository;

import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;

/**
 * Helper class for subscription reference repository operations
 * 
 * @author Neil Webb
 */
public class SubscriptionReferenceRepositoryHelper extends BaseSubscriptionRepositoryHelper {
    public SubscriptionReferenceRepositoryHelper() throws SubscriptionRepositoryException {
        super();
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByParentSubscriptionReference(
            gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType parentSubscriptionReferenceType) {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItemsType = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType();

        // Transform to subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(parentSubscriptionReferenceType);

        // Retrieve the subscription item
        SubscriptionRecordList subscriptionRecords = subscriptionRepositoryService
                .retrieveByParentSubscriptionReference(subscriptionReference, getSubscriptionType());

        // Transform to subscription item type
        if (subscriptionRecords != null) {
            for (SubscriptionRecord record : subscriptionRecords) {
                if ((record != null) && (record.getSubscription() != null)) {
                    gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType = loadSubscriptionItemType(record
                            .getSubscription());
                    if (subscriptionItemType != null) {
                        subscriptionItemsType.getSubscriptionItem().add(subscriptionItemType);
                    }
                }
            }
        }
        return subscriptionItemsType;
    }

    @Override
    protected SubscriptionType getSubscriptionType() {
        return SubscriptionType.SUBSCRIPTION_REFERENCE;
    }
}
