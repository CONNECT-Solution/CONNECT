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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionCriteria;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;
import gov.hhs.fha.nhinc.subscription.repository.manager.Manager;
import java.util.List;

/**
 * File based subscription repository implementation
 * 
 * @author Neil Webb
 */
class FileSubscriptionRepository implements SubscriptionRepositoryService {
    public SubscriptionReference storeSubscription(SubscriptionRecord subscriptionRecord) {
        SubscriptionReference reference = null;
        if (subscriptionRecord != null) {
            Manager mgr = new Manager();
            mgr.addSubscription(subscriptionRecord);
            reference = subscriptionRecord.getSubscription().getSubscriptionReference();
        }
        return reference;
    }

    public void deleteSubscription(SubscriptionRecord subscriptionRecord) {
        if ((subscriptionRecord != null) && (subscriptionRecord.getSubscription() != null)
                && (subscriptionRecord.getSubscription().getSubscriptionReference() != null)) {
            Manager mgr = new Manager();
            mgr.removeSubscription(subscriptionRecord.getSubscription().getSubscriptionReference());
        }
    }

    public SubscriptionRecordList retrieveByCriteria(SubscriptionCriteria subscriptionCriteria,
            SubscriptionType subscriptionType) {
        SubscriptionRecordList subscriptionRecords = new SubscriptionRecordList();
        // Only process if one or more criterion values are provided
        if ((subscriptionCriteria != null)
                && ((subscriptionCriteria.getCriteria() != null) && (!subscriptionCriteria.getCriteria().isEmpty()))
                || ((subscriptionCriteria.getTopicExpression() != null) && NullChecker
                        .isNotNullish(subscriptionCriteria.getTopicExpression().getTopicExpressionValue()))) {
            Manager mgr = new Manager();
            SubscriptionRecordList allRecords = mgr.loadSubscriptionList();
            if (allRecords != null) {
                for (SubscriptionRecord record : allRecords) {
                    if ((record.getSubscription() != null)
                            && (record.getSubscription().getSubscriptionCriteria() != null)
                            && (record.getSubscription().getSubscriptionCriteria().getCriteria() != null)
                            && (record.getType().equals(subscriptionType))) {
                        SubscriptionCriteria recordSubscriptionCriteria = record.getSubscription()
                                .getSubscriptionCriteria();

                        boolean match = true;
                        // Subscriber patient matching
                        if (subscriptionCriteria.getSubscriberPatient() != null) {
                            match = subscriptionCriteria.getSubscriberPatient().equals(
                                    recordSubscriptionCriteria.getSubscriberPatient());
                        }

                        // Subscribee patient matching
                        if (match && (subscriptionCriteria.getSubscribeePatient() != null)) {
                            match = subscriptionCriteria.getSubscribeePatient().equals(
                                    recordSubscriptionCriteria.getSubscribeePatient());
                        }

                        if (match && (subscriptionCriteria.getCriteria() != null)) {
                            // Criteria matching
                            List<Criterion> recordCriteria = recordSubscriptionCriteria.getCriteria();

                            if (!recordCriteria.containsAll(subscriptionCriteria.getCriteria())) {
                                match = false;
                            }
                        }

                        if (match
                                && (subscriptionCriteria.getTopicExpression() != null)
                                && NullChecker.isNotNullish(subscriptionCriteria.getTopicExpression()
                                        .getTopicExpressionValue())) {
                            match = ((recordSubscriptionCriteria.getTopicExpression() != null) && (subscriptionCriteria
                                    .getTopicExpression().getTopicExpressionValue().equals(recordSubscriptionCriteria
                                    .getTopicExpression().getTopicExpressionValue())));
                        }

                        if (match) {
                            subscriptionRecords.add(record);
                        }
                    }
                }
            }
        }
        return subscriptionRecords;
    }

    public SubscriptionRecord retrieveBySubscriptionReference(SubscriptionReference subscriptionReference,
            SubscriptionType subscriptionType) {
        SubscriptionRecord record = null;
        if (subscriptionReference != null) {
            Manager mgr = new Manager();
            SubscriptionRecordList allRecords = mgr.loadSubscriptionList();
            if (allRecords != null) {
                for (SubscriptionRecord retRec : allRecords) {
                    if ((retRec.getSubscription() != null)
                            && (subscriptionReference.equals(retRec.getSubscription().getSubscriptionReference()))
                            && (retRec.getType().equals(subscriptionType))) {
                        record = retRec;
                        break;
                    }
                }
            }
        }
        return record;
    }

    public SubscriptionRecordList retrieveByParentSubscriptionReference(
            SubscriptionReference parentSubscriptionReference, SubscriptionType subscriptionType) {
        SubscriptionRecordList records = new SubscriptionRecordList();
        if (parentSubscriptionReference != null) {
            Manager mgr = new Manager();
            SubscriptionRecordList allRecords = mgr.loadSubscriptionList();
            if (allRecords != null) {
                for (SubscriptionRecord retRec : allRecords) {
                    if ((retRec.getSubscription() != null)
                            && (parentSubscriptionReference.equals(retRec.getSubscription()
                                    .getParentSubscriptionReference())) && (retRec.getType().equals(subscriptionType))) {
                        records.add(retRec);
                    }
                }
            }
        }
        return records;
    }
}
