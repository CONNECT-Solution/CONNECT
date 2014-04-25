/*
 *  Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above
 *        copyright notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the United States Government nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.admingui.event.model;

/**
 *
 * @author jasonasmith
 */
public class EventNwhinOrganization {
    
    private String organizationName;
    
    private int dqCount = 0;
    private int drCount = 0;
    private int pdDefReqCount = 0;
    private int pdDefRespCount = 0;
    private int pdSyncCount = 0;
    private int dsDefReqCount = 0;
    private int dsDefRespCount = 0;
    private int dsSyncCount = 0;
    private int adCount = 0;
    private int directCount = 0;
    
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    public int getPdCount() {
        return pdSyncCount + pdDefReqCount + pdDefRespCount;
    }

    public int getDqCount() {
        return dqCount;
    }

    public void setDqCount(int dqCount) {
        this.dqCount = dqCount;
    }

    public int getDrCount() {
        return drCount;
    }

    public void setDrCount(int drCount) {
        this.drCount = drCount;
    }

    public int getDsCount() {
        return dsSyncCount + dsDefReqCount + dsDefRespCount;
    }

    public int getPdDefReqCount() {
        return pdDefReqCount;
    }

    public void setPdDefReqCount(int pdDefReqCount) {
        this.pdDefReqCount = pdDefReqCount;
    }

    public int getPdDefRespCount() {
        return pdDefRespCount;
    }

    public void setPdDefRespCount(int pdDefRespCount) {
        this.pdDefRespCount = pdDefRespCount;
    }

    public int getPdSyncCount() {
        return pdSyncCount;
    }

    public void setPdSyncCount(int pdSyncCount) {
        this.pdSyncCount = pdSyncCount;
    }

    public int getDsDefReqCount() {
        return dsDefReqCount;
    }

    public void setDsDefReqCount(int dsDefReqCount) {
        this.dsDefReqCount = dsDefReqCount;
    }

    public int getDsDefRespCount() {
        return dsDefRespCount;
    }

    public void setDsDefRespCount(int dsDefRespCount) {
        this.dsDefRespCount = dsDefRespCount;
    }

    public int getDsSyncCount() {
        return dsSyncCount;
    }

    public void setDsSyncCount(int dsSyncCount) {
        this.dsSyncCount = dsSyncCount;
    }

    public int getAdCount() {
        return adCount;
    }

    public void setAdCount(int adCount) {
        this.adCount = adCount;
    }

    public int getDirectCount() {
        return directCount;
    }

    public void setDirectCount(int directCount) {
        this.directCount = directCount;
    }

    public int getTotalCount(){
        return getPdCount() + getDqCount() + getDrCount() + getDsCount() + getAdCount() + getDirectCount();
    }
    
}
