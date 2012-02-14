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
package gov.hhs.fha.nhinc.subscription.repository.data;

import gov.hhs.fha.nhinc.subscription.repository.SubscriptionRepositoryException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Data class for subscription criteria
 * 
 * @author Neil Webb
 */
public class SubscriptionCriteria implements Serializable {
    private static final long serialVersionUID = 2035932112955401949L;
    private Patient subscriberPatient;
    private Patient subscribeePatient;
    private List<Criterion> criteria;
    private TopicExpression topicExpression;
    private SubscriptionPolicy subscriptionPolicy;

    public TopicExpression getTopicExpression() {
        return topicExpression;
    }

    public void setTopicExpression(TopicExpression topicExpression) {
        this.topicExpression = topicExpression;
    }

    public Patient getSubscriberPatient() {
        return subscriberPatient;
    }

    public void setSubscriberPatient(Patient patient) {
        this.subscriberPatient = patient;
    }

    public Patient getSubscribeePatient() {
        return subscribeePatient;
    }

    public void setSubscribeePatient(Patient patient) {
        this.subscribeePatient = patient;
    }

    public List<Criterion> getCriteria() {
        if (criteria == null) {
            criteria = new ArrayList<Criterion>();
        }
        return criteria;
    }

    public void setCriteria(List<Criterion> criteria) {
        this.criteria = criteria;
    }

    public SubscriptionPolicy getSubscriptionPolicy() {
        return subscriptionPolicy;
    }

    public void setSubscriptionPolicy(SubscriptionPolicy subscriptionPolicy) {
        this.subscriptionPolicy = subscriptionPolicy;
    }

    public void addCriterion(Criterion criterion) throws SubscriptionRepositoryException {
        // Validate criterion object
        if (criterion == null) {
            throw new SubscriptionRepositoryException("Attempted to add null criterion to subscription criteria list");
        }

        // Validate key
        if ((criterion.getKey() == null) || "".equals(criterion.getKey().trim())) {
            throw new SubscriptionRepositoryException(
                    "Attempted to add criterion with an invalid key to subscription criteria list - key: "
                            + criterion.getKey());
        } else {
            // Trim key prior to storage
            criterion.setKey(criterion.getKey().trim());
        }

        // Valiedate value
        if ((criterion.getValue() == null) || "".equals(criterion.getValue().trim())) {
            throw new SubscriptionRepositoryException(
                    "Attempted to add criterion to the subscription criteria list with an invalid value for key: "
                            + criterion.getKey());
        }
        getCriteria().add(criterion);
    }

    public void removeCriterion(Criterion criterion) {
        if (criterion != null) {
            Iterator<Criterion> iter = getCriteria().iterator();
            while (iter.hasNext()) {
                Criterion c = iter.next();
                if (c.equals(criterion)) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubscriptionCriteria other = (SubscriptionCriteria) obj;
        if (this.subscriberPatient != other.subscriberPatient
                && (this.subscriberPatient == null || !this.subscriberPatient.equals(other.subscriberPatient))) {
            return false;
        }
        if (this.subscribeePatient != other.subscribeePatient
                && (this.subscribeePatient == null || !this.subscribeePatient.equals(other.subscribeePatient))) {
            return false;
        }
        if (this.criteria != other.criteria && (this.criteria == null || !this.criteria.equals(other.criteria))) {
            return false;
        }
        if (this.topicExpression != other.topicExpression
                && (this.topicExpression == null || !this.topicExpression.equals(other.topicExpression))) {
            return false;
        }
        if (this.subscriptionPolicy != other.subscriptionPolicy
                && (this.subscriptionPolicy == null || !this.subscriptionPolicy.equals(other.subscriptionPolicy))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.subscriberPatient != null ? this.subscriberPatient.hashCode() : 0);
        hash = 97 * hash + (this.subscribeePatient != null ? this.subscribeePatient.hashCode() : 0);
        hash = 97 * hash + (this.criteria != null ? this.criteria.hashCode() : 0);
        return hash;
    }
}
