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

import gov.hhs.fha.nhinc.direct.config.TrustBundle;
import gov.hhs.fha.nhinc.direct.config.TrustBundleAnchor;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
public class DirectTrustBundle {

    long id;

    private String bundleName;
    private String bundleURL;
    private String checkSum;

    private String createTime;
    private String lastSuccessfulRefresh;
    private String lastRefreshAttempt;
    private String lastRefreshError;

    private int refreshInterval;

    private boolean incoming;
    private boolean outgoing;

    private List<TrustBundleAnchor> anchors;

    /**
     *
     */
    public DirectTrustBundle() {

    }

    /**
     *
     * @param tb
     * @param incoming
     * @param outgoing
     */
    public DirectTrustBundle(TrustBundle tb, boolean incoming, boolean outgoing) {
        id = tb.getId();

        bundleName = tb.getBundleName();
        bundleURL = tb.getBundleURL();
        checkSum = tb.getCheckSum();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        createTime = sdf.format(tb.getCreateTime().toGregorianCalendar().getTime());

        if (tb.getLastSuccessfulRefresh() != null) {
            lastSuccessfulRefresh = sdf.format(tb.getLastSuccessfulRefresh().toGregorianCalendar().getTime());
        }

        if (tb.getLastRefreshAttempt() != null) {
            lastRefreshAttempt = sdf.format(tb.getLastRefreshAttempt().toGregorianCalendar().getTime());
        }

        lastRefreshError = tb.getLastRefreshError().value();
        refreshInterval = tb.getRefreshInterval();

        this.incoming = incoming;
        this.outgoing = outgoing;

        anchors = tb.getTrustBundleAnchors();
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getBundleName() {
        return bundleName;
    }

    /**
     *
     * @param bundleName
     */
    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    /**
     *
     * @return
     */
    public String getBundleURL() {
        return bundleURL;
    }

    /**
     *
     * @param bundleURL
     */
    public void setBundleURL(String bundleURL) {
        this.bundleURL = bundleURL;
    }

    /**
     *
     * @return
     */
    public String getCheckSum() {
        return checkSum;
    }

    /**
     *
     * @param checkSum
     */
    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    /**
     *
     * @return
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     *
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     *
     * @return
     */
    public String getLastSuccessfulRefresh() {
        return lastSuccessfulRefresh;
    }

    /**
     *
     * @param lastSuccessfulRefresh
     */
    public void setLastSuccessfulRefresh(String lastSuccessfulRefresh) {
        this.lastSuccessfulRefresh = lastSuccessfulRefresh;
    }

    /**
     *
     * @return
     */
    public String getLastRefreshAttempt() {
        return lastRefreshAttempt;
    }

    /**
     *
     * @param lastRefreshAttempt
     */
    public void setLastRefreshAttempt(String lastRefreshAttempt) {
        this.lastRefreshAttempt = lastRefreshAttempt;
    }

    /**
     *
     * @return
     */
    public String getLastRefreshError() {
        return lastRefreshError;
    }

    /**
     *
     * @param lastRefreshError
     */
    public void setLastRefreshError(String lastRefreshError) {
        this.lastRefreshError = lastRefreshError;
    }

    /**
     *
     * @return
     */
    public int getRefreshInterval() {
        return refreshInterval;
    }

    /**
     *
     * @param refreshInterval
     */
    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    /**
     *
     * @return
     */
    public boolean isIncoming() {
        return incoming;
    }

    /**
     *
     * @param incoming
     */
    public void setIncoming(boolean incoming) {
        this.incoming = incoming;
    }

    /**
     *
     * @return
     */
    public boolean isOutgoing() {
        return outgoing;
    }

    /**
     *
     * @param outgoing
     */
    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }

    /**
     *
     * @return
     */
    public List<TrustBundleAnchor> getAnchors() {
        return anchors;
    }

    /**
     *
     * @param anchors
     */
    public void setAnchors(List<TrustBundleAnchor> anchors) {
        this.anchors = anchors;
    }
}
