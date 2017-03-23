/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.event.model;

/**
 *
 * @author jasonasmith
 */
public class EventNwhinOrganization {

    private String organizationName;

    private Long dqCount = 0L;
    private Long drCount = 0L;
    private Long pdDefReqCount = 0L;
    private Long pdDefRespCount = 0L;
    private Long pdSyncCount = 0L;
    private Long dsDefReqCount = 0L;
    private Long dsDefRespCount = 0L;
    private Long dsSyncCount = 0L;
    private Long adCount = 0L;
    private Long directCount = 0L;

    /**
     *
     * @return
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     *
     * @param organizationName
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     *
     * @return
     */
    public Long getPdCount() {
        return pdSyncCount + pdDefReqCount + pdDefRespCount;
    }

    /**
     *
     * @return
     */
    public Long getDqCount() {
        return dqCount;
    }

    /**
     *
     * @param dqCount
     */
    public void setDqCount(Long dqCount) {
        this.dqCount = dqCount;
    }

    /**
     *
     * @return
     */
    public Long getDrCount() {
        return drCount;
    }

    /**
     *
     * @param drCount
     */
    public void setDrCount(Long drCount) {
        this.drCount = drCount;
    }

    /**
     *
     * @return
     */
    public Long getDsCount() {
        return dsSyncCount + dsDefReqCount + dsDefRespCount;
    }

    /**
     *
     * @return
     */
    public Long getPdDefReqCount() {
        return pdDefReqCount;
    }

    /**
     *
     * @param pdDefReqCount
     */
    public void setPdDefReqCount(Long pdDefReqCount) {
        this.pdDefReqCount = pdDefReqCount;
    }

    /**
     *
     * @return
     */
    public Long getPdDefRespCount() {
        return pdDefRespCount;
    }

    /**
     *
     * @param pdDefRespCount
     */
    public void setPdDefRespCount(Long pdDefRespCount) {
        this.pdDefRespCount = pdDefRespCount;
    }

    /**
     *
     * @return
     */
    public Long getPdSyncCount() {
        return pdSyncCount;
    }

    /**
     *
     * @param pdSyncCount
     */
    public void setPdSyncCount(Long pdSyncCount) {
        this.pdSyncCount = pdSyncCount;
    }

    /**
     *
     * @return
     */
    public Long getDsDefReqCount() {
        return dsDefReqCount;
    }

    /**
     *
     * @param dsDefReqCount
     */
    public void setDsDefReqCount(Long dsDefReqCount) {
        this.dsDefReqCount = dsDefReqCount;
    }

    /**
     *
     * @return
     */
    public Long getDsDefRespCount() {
        return dsDefRespCount;
    }

    /**
     *
     * @param dsDefRespCount
     */
    public void setDsDefRespCount(Long dsDefRespCount) {
        this.dsDefRespCount = dsDefRespCount;
    }

    /**
     *
     * @return
     */
    public Long getDsSyncCount() {
        return dsSyncCount;
    }

    /**
     *
     * @param dsSyncCount
     */
    public void setDsSyncCount(Long dsSyncCount) {
        this.dsSyncCount = dsSyncCount;
    }

    /**
     *
     * @return
     */
    public Long getAdCount() {
        return adCount;
    }

    /**
     *
     * @param adCount
     */
    public void setAdCount(Long adCount) {
        this.adCount = adCount;
    }

    /**
     *
     * @return
     */
    public Long getDirectCount() {
        return directCount;
    }

    /**
     *
     * @param directCount
     */
    public void setDirectCount(Long directCount) {
        this.directCount = directCount;
    }

    /**
     *
     * @return
     */
    public Long getTotalCount() {
        return getPdCount() + getDqCount() + getDrCount() + getDsCount() + getAdCount() + getDirectCount();
    }

}
