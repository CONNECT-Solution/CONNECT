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

import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionCriteria;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;

/**
 * Interface for access to a subscription repository
 * 
 * @author Neil Webb
 */
public interface SubscriptionRepositoryService {
    /**
     * Store a subscription item in the repository.
     * 
     * @param subscriptionRecord Object to store
     * @return The subscription reference to the newly created subscription record
     */
    public SubscriptionReference storeSubscription(SubscriptionRecord subscriptionRecord);

    /**
     * Remove a subscription from the repository.
     * 
     * @param subscriptionRecord Subscription record to delete.
     */
    public void deleteSubscription(SubscriptionRecord subscriptionRecord);

    /**
     * Retrieve matching subscriptions matching the provided criteria. Both the subscribee and subscriber patients must
     * match (may be null) and the criterion values provided must all be found in the stored criterion.
     * 
     * @param criteria Subscription criteria to use in matching.
     * @param subscriptionType Subscription type
     * @return A list of subscription records matching the provided criteria.
     */
    public SubscriptionRecordList retrieveByCriteria(SubscriptionCriteria criteria, SubscriptionType subscriptionType);

    /**
     * Retrieve a subscription by subscription reference.
     * 
     * @param subscriptionReference Subscription reference to the stored subscription record.
     * @param subscriptionType Subscription type
     * @return Matching subscription record
     */
    public SubscriptionRecord retrieveBySubscriptionReference(SubscriptionReference subscriptionReference,
            SubscriptionType subscriptionType);

    /**
     * Retrieve all subscriptions that match the provided parent subscription reference.
     * 
     * @param parentSubscriptionReference Parent subscription reference.
     * @param subscriptionType Subscription type
     * @return Subscription records matching the provided parent subscription reference.
     */
    public SubscriptionRecordList retrieveByParentSubscriptionReference(
            SubscriptionReference parentSubscriptionReference, SubscriptionType subscriptionType);

}
