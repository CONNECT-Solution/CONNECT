/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.SessionScoped;

/**
 * @author sadusumilli
 *
 */
@SessionScoped
public class Organization {

    private String hcidName;

    public enum servicesEnum {PD, DQ, DR, DS, AD, Direct};
    private Map<servicesEnum, Service> serviceMap = null;

    public Organization() {
        
        serviceMap = new HashMap();
        Service PDService = new Service();
        Service DQService = new Service();
        Service DRService = new Service();
        Service DSService = new Service();
        Service ADService = new Service();
        Service DirectService = new Service();

        serviceMap.put(servicesEnum.PD, PDService);
        serviceMap.put(servicesEnum.DQ, DQService);
        serviceMap.put(servicesEnum.DR, DRService);
        serviceMap.put(servicesEnum.DS, DSService);
        serviceMap.put(servicesEnum.AD, ADService);
        serviceMap.put(servicesEnum.Direct, DirectService);

    } 
   
    private Service[] services;
    private Service PD;
    private Service DQ; 
    private Service DR; 
    private Service DS; 
    private Service AD; 
    private Service Direct; 

    private String PDName; 
    private int pdInOutBoundValue; 

    private String DQName; 
    private int dqInOutBoundValue;

    private String DRName; 
    private int drInOutBoundValue;

    private String DSName; 
    private int dsInOutBoundValue;

    private String ADName; 
    private int adInOutBoundValue;
    
    private String DirectName;
    private int directInOutBoundValue;

    private int totalInOutBoundValue;

    public Service[] getServices() {
        return services;
    }

    public void setServices(Service[] services) {
        this.setServices(services);
    }

    public void setHcidName(String hcidName) {
        this.hcidName = hcidName;

    }

    public String getHcidName() {
        return this.hcidName;
    }

    /**
     * @return the totalInOutBoundValue
     */
    public int getTotalInOutBoundValue() {
        return totalInOutBoundValue;
    }

    /**
     * @param totalInOutBoundValue the totalInOutBoundValue to set
     */
    public void setTotalInOutBoundValue(int totalInOutBoundValue) {
        this.totalInOutBoundValue = totalInOutBoundValue;
    }
    /**
     * @return the Direct
     */
    public Service getDirect() {
        return Direct;
    }

    /**
     * @param Direct the Direct to set
     */
    public void setDirect(Service Direct) {
        this.Direct = Direct;
    }

    /**
     * @return the PD
     */
    public Service getPD() {
        return PD;
    }

    /**
     * @param PD the PD to set
     */
    public void setPD(Service PD) {
        this.PD = PD;
    }

    /**
     * @return the DQ
     */
    public Service getDQ() {
        return DQ;
    }

    /**
     * @param DQ the DQ to set
     */
    public void setDQ(Service DQ) {
        this.DQ = DQ;
    }

    /**
     * @return the DR
     */
    public Service getDR() {
        return DR;
    }

    /**
     * @param DR the DR to set
     */
    public void setDR(Service DR) {
        this.DR = DR;
    }

    /**
     * @return the DS
     */
    public Service getDS() {
        return DS;
    }

    /**
     * @param DS the DS to set
     */
    public void setDS(Service DS) {
        this.DS = DS;
    }

    /**
     * @return the AD
     */
    public Service getAD() {
        return AD;
    }

    /**
     * @param AD the AD to set
     */
    public void setAD(Service AD) {
        this.AD = AD;
    }

    /**
     * @return the PDName
     */
    public String getPDName() {
        return PDName;
    }

    /**
     * @param PDName the PDName to set
     */
    public void setPDName(String PDName) {
        this.PDName = PDName;
    }

    /**
     * @return the pdInOutBoundValue
     */
    public int getPdInOutBoundValue() {
        return pdInOutBoundValue;
    }

    /**
     * @param pdInOutBoundValue the pdInOutBoundValue to set
     */
    public void setPdInOutBoundValue(int pdInOutBoundValue) {
        this.pdInOutBoundValue = pdInOutBoundValue;
    }

    /**
     * @return the DQName
     */
    public String getDQName() {
        return DQName;
    }

    /**
     * @param DQName the DQName to set
     */
    public void setDQName(String DQName) {
        this.DQName = DQName;
    }

    /**
     * @return the dqInOutBoundValue
     */
    public int getDqInOutBoundValue() {
        return dqInOutBoundValue;
    }

    /**
     * @param dqInOutBoundValue the dqInOutBoundValue to set
     */
    public void setDqInOutBoundValue(int dqInOutBoundValue) {
        this.dqInOutBoundValue = dqInOutBoundValue;
    }

    /**
     * @return the DRName
     */
    public String getDRName() {
        return DRName;
    }

    /**
     * @param DRName the DRName to set
     */
    public void setDRName(String DRName) {
        this.DRName = DRName;
    }

    /**
     * @return the drInOutBoundValue
     */
    public int getDrInOutBoundValue() {
        return drInOutBoundValue;
    }

    /**
     * @param drInOutBoundValue the drInOutBoundValue to set
     */
    public void setDrInOutBoundValue(int drInOutBoundValue) {
        this.drInOutBoundValue = drInOutBoundValue;
    }

    /**
     * @return the DSName
     */
    public String getDSName() {
        return DSName;
    }

    /**
     * @param DSName the DSName to set
     */
    public void setDSName(String DSName) {
        this.DSName = DSName;
    }

    /**
     * @return the dsInOutBoundValue
     */
    public int getDsInOutBoundValue() {
        return dsInOutBoundValue;
    }

    /**
     * @param dsInOutBoundValue the dsInOutBoundValue to set
     */
    public void setDsInOutBoundValue(int dsInOutBoundValue) {
        this.dsInOutBoundValue = dsInOutBoundValue;
    }

    /**
     * @return the ADName
     */
    public String getADName() {
        return ADName;
    }

    /**
     * @param ADName the ADName to set
     */
    public void setADName(String ADName) {
        this.ADName = ADName;
    }

    /**
     * @return the DirectName
     */
    public String getDirectName() {
        return DirectName;
    }

    /**
     * @param DirectName the DirectName to set
     */
    public void setDirectName(String DirectName) {
        this.DirectName = DirectName;
    }

    /**
     * @return the directInOutBoundValue
     */
    public int getDirectInOutBoundValue() {
        return directInOutBoundValue;
    }

    /**
     * @param directInOutBoundValue the directInOutBoundValue to set
     */
    public void setDirectInOutBoundValue(int directInOutBoundValue) {
        this.directInOutBoundValue = directInOutBoundValue;
    }

    /**
     * @return the adInOutBoundValue
     */
    public int getAdInOutBoundValue() {
        return adInOutBoundValue;
    }

    /**
     * @param adInOutBoundValue the adInOutBoundValue to set
     */
    public void setAdInOutBoundValue(int adInOutBoundValue) {
        this.adInOutBoundValue = adInOutBoundValue;
    }

    /**
     * @return the serviceMap
     */
    public Map<servicesEnum, Service> getServiceMap() {
        return serviceMap;
    }

    /**
     * @param serviceMap the serviceMap to set
     */
    public void setServiceMap(Map<servicesEnum, Service> serviceMap) {
        this.serviceMap = serviceMap;
    }
}


