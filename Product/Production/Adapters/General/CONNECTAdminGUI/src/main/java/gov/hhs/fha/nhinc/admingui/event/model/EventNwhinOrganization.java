/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
