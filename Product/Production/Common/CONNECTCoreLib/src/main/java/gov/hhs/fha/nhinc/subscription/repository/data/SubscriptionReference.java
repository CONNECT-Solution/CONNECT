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
 * Data class for a subscription reference
 * 
 * @author Neil Webb
 */
public class SubscriptionReference implements Serializable {
    private static final long serialVersionUID = -5693688804432186068L;
    private String subscriptionManagerEndpointAddress;
    private List<ReferenceParameter> referenceParameters;

    public String getSubscriptionManagerEndpointAddress() {
        return subscriptionManagerEndpointAddress;
    }

    public void setSubscriptionManagerEndpointAddress(String subscriptionManagerEndpointAddress) {
        this.subscriptionManagerEndpointAddress = subscriptionManagerEndpointAddress;
    }

    public List<ReferenceParameter> getReferenceParameters() {
        if (referenceParameters == null) {
            referenceParameters = new ArrayList<ReferenceParameter>();
        }
        return referenceParameters;
    }

    public void setReferenceParameters(List<ReferenceParameter> referenceParameters) {
        this.referenceParameters = referenceParameters;
    }

    public void addReferenceParameter(ReferenceParameter refParam) throws SubscriptionRepositoryException {
        if (refParam == null) {
            throw new SubscriptionRepositoryException("Attempted to add null reference parameter");
        }
        getReferenceParameters().add(refParam);
    }

    public void removeReferenceParameter(ReferenceParameter refParam) {
        if (refParam != null) {
            Iterator<ReferenceParameter> iter = getReferenceParameters().iterator();
            while (iter.hasNext()) {
                ReferenceParameter rp = iter.next();
                if (refParam.equals(rp)) {
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
        final SubscriptionReference other = (SubscriptionReference) obj;
        if (this.subscriptionManagerEndpointAddress != other.subscriptionManagerEndpointAddress
                && (this.subscriptionManagerEndpointAddress == null || !this.subscriptionManagerEndpointAddress
                        .equals(other.subscriptionManagerEndpointAddress))) {
            System.out.println("Subscription manager endpoint address did not equal");
            System.out
                    .println("This subscription manager endpoint address: " + this.subscriptionManagerEndpointAddress);
            System.out.println("Other subscription manager endpoint address: "
                    + other.subscriptionManagerEndpointAddress);
            return false;
        }
        if (this.referenceParameters != other.referenceParameters
                && (this.referenceParameters == null || !this.referenceParameters.equals(other.referenceParameters))) {
            System.out.println("Reference parameters did not equal");
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97
                * hash
                + (this.subscriptionManagerEndpointAddress != null ? this.subscriptionManagerEndpointAddress.hashCode()
                        : 0);
        hash = 97 * hash + (this.referenceParameters != null ? this.referenceParameters.hashCode() : 0);
        return hash;
    }
}
