/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.model.direct;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
public class DirectDomain {

    private int position;
    private String domainStatus;
    private String domainName;
    private String domainPostMaster;
    private String domainCreated;
    private String domainUpdated;

    private List<DirectAddress> addresses = new ArrayList<>();
    private List<DirectAnchor> anchors = new ArrayList<>();
    private List<DirectTrustBundle> trustBundles = new ArrayList<>();

    /**
     *
     */
    public DirectDomain() {

    }

    /**
     *
     * @param position
     * @param domainStatus
     * @param domainName
     * @param domainPostMaster
     * @param domainCreated
     * @param domainUpdated
     */
    public DirectDomain(int position, String domainStatus, String domainName, String domainPostMaster,
            String domainCreated, String domainUpdated) {
        this.position = position;
        this.domainStatus = domainStatus;
        this.domainName = domainName;
        this.domainPostMaster = domainPostMaster;
        this.domainCreated = domainCreated;
        this.domainUpdated = domainUpdated;
    }

    /**
     *
     * @return
     */
    public int getPosition() {
        return position;
    }

    /**
     *
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     *
     * @return
     */
    public String getDomainStatus() {
        return domainStatus;
    }

    /**
     *
     * @param domainStatus
     */
    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }

    /**
     *
     * @return
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     *
     * @param domainName
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     *
     * @return
     */
    public String getDomainPostMaster() {
        return domainPostMaster;
    }

    /**
     *
     * @param domainPostMaster
     */
    public void setDomainPostMaster(String domainPostMaster) {
        this.domainPostMaster = domainPostMaster;
    }

    /**
     *
     * @return
     */
    public String getDomainCreated() {
        return domainCreated;
    }

    /**
     *
     * @param domainCreated
     */
    public void setDomainCreated(String domainCreated) {
        this.domainCreated = domainCreated;
    }

    /**
     *
     * @return
     */
    public String getDomainUpdated() {
        return domainUpdated;
    }

    /**
     *
     * @param domainUpdated
     */
    public void setDomainUpdated(String domainUpdated) {
        this.domainUpdated = domainUpdated;
    }

    /**
     *
     * @return
     */
    public List<DirectAddress> getAddresses() {
        return addresses;
    }

    /**
     *
     * @param addresses
     */
    public void setAddresses(List<DirectAddress> addresses) {
        this.addresses = addresses;
    }

    /**
     *
     * @param address
     */
    public void addAddress(DirectAddress address) {
        addresses.add(address);
    }

    /**
     *
     * @return
     */
    public List<DirectAnchor> getAnchors() {
        return anchors;
    }

    /**
     *
     * @param anchors
     */
    public void setAnchors(List<DirectAnchor> anchors) {
        this.anchors = anchors;
    }

    /**
     *
     * @param anchor
     */
    public void addAnchor(DirectAnchor anchor) {
        anchors.add(anchor);
    }

    /**
     *
     * @return
     */
    public List<DirectTrustBundle> getTrustBundles() {
        return trustBundles;
    }

    /**
     *
     * @param trustBundles
     */
    public void setTrustBundles(List<DirectTrustBundle> trustBundles) {
        this.trustBundles = trustBundles;
    }

    /**
     *
     * @param tb
     */
    public void addTrustBundle(DirectTrustBundle tb) {
        trustBundles.add(tb);
    }

    /**
     *
     * @param tbs
     */
    public void addTrustBundles(List<DirectTrustBundle> tbs) {
        trustBundles.addAll(tbs);
    }
}
