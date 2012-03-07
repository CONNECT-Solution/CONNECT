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

import java.io.Serializable;

/**
 * Data class for a subscription item
 * 
 * @author Neil Webb
 */
public class SubscriptionItem implements Serializable {
    private static final long serialVersionUID = 7601908766151278860L;
    private SubscriptionParticipant subscriber;
    private SubscriptionParticipant subscribee;
    private SubscriptionCriteria subscriptionCriteria;
    private SubscriptionReference subscriptionReference;
    private SubscriptionReference parentSubscriptionReference;

    public SubscriptionParticipant getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(SubscriptionParticipant subscriber) {
        this.subscriber = subscriber;
    }

    public SubscriptionParticipant getSubscribee() {
        return subscribee;
    }

    public void setSubscribee(SubscriptionParticipant subscribee) {
        this.subscribee = subscribee;
    }

    public SubscriptionCriteria getSubscriptionCriteria() {
        return subscriptionCriteria;
    }

    public void setSubscriptionCriteria(SubscriptionCriteria subscriptionCriteria) {
        this.subscriptionCriteria = subscriptionCriteria;
    }

    public SubscriptionReference getSubscriptionReference() {
        return subscriptionReference;
    }

    public void setSubscriptionReference(SubscriptionReference subscriptionReference) {
        this.subscriptionReference = subscriptionReference;
    }

    public SubscriptionReference getParentSubscriptionReference() {
        return parentSubscriptionReference;
    }

    public void setParentSubscriptionReference(SubscriptionReference parentSubscriptionReference) {
        this.parentSubscriptionReference = parentSubscriptionReference;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubscriptionItem other = (SubscriptionItem) obj;
        if (this.subscriber != other.subscriber
                && (this.subscriber == null || !this.subscriber.equals(other.subscriber))) {
            return false;
        }
        if (this.subscribee != other.subscribee
                && (this.subscribee == null || !this.subscribee.equals(other.subscribee))) {
            return false;
        }
        if (this.subscriptionCriteria != other.subscriptionCriteria
                && (this.subscriptionCriteria == null || !this.subscriptionCriteria.equals(other.subscriptionCriteria))) {
            return false;
        }
        if (this.subscriptionReference != other.subscriptionReference
                && (this.subscriptionReference == null || !this.subscriptionReference
                        .equals(other.subscriptionReference))) {
            return false;
        }
        if (this.parentSubscriptionReference != other.parentSubscriptionReference
                && (this.parentSubscriptionReference == null || !this.parentSubscriptionReference
                        .equals(other.parentSubscriptionReference))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.subscriber != null ? this.subscriber.hashCode() : 0);
        hash = 83 * hash + (this.subscribee != null ? this.subscribee.hashCode() : 0);
        hash = 83 * hash + (this.subscriptionCriteria != null ? this.subscriptionCriteria.hashCode() : 0);
        hash = 83 * hash + (this.subscriptionReference != null ? this.subscriptionReference.hashCode() : 0);
        hash = 83 * hash + (this.parentSubscriptionReference != null ? this.parentSubscriptionReference.hashCode() : 0);
        return hash;
    }

}
