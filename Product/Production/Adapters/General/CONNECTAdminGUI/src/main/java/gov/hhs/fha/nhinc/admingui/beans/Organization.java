/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.beans;

import java.util.Collection;
import javax.faces.bean.SessionScoped;

/**
 * @author sadusumilli
 *
 */
@SessionScoped
public class Organization {
	
        private String hcidName;
    
	private Service[] services;
        
        private Service service1;
        private Service service2;
        private Service service3;
        private Service service4;
        private Service service5;
        
        private String service1Name;
        private int service1InOutBoundValue;
        
        private String service2Name;
        private int service2InOutBoundValue;
        
        private String service3Name;
        private int service3InOutBoundValue;
        
        private String service4Name;
        private int service4InOutBoundValue;
        
        private String service5Name;
        private int service5InOutBoundValue;
        
        private int totalInOutBoundValue;
        
        public void setService1(Service service) {
            service1 = service;
        }
        
        public Service getService1() {
            return service1;
        }
        
        public void setService2(Service service) {
            service2 = service;
        }
        
        public Service getService2() {
            return service2;
        }
        
        public void setService3(Service service) {
            service3 = service;
        }
        
        public Service getService3() {
            return service3;
        }
        
        public void setService4(Service service) {
            service4 = service;
        }
        
        public Service getService4() {
            return service4;
        }

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
     * @param service1Name the service1Name to set
     */
    public void setService1Name(String service1Name) {
        this.service1Name = service1Name;
    }

    /**
     * @return the service1InOutBoundValue
     */
    public int getService1InOutBoundValue() {
        return service1InOutBoundValue;
    }

    /**
     * @param service1InOutBoundValue the service1InOutBoundValue to set
     */
    public void setService1InOutBoundValue(int service1InOutBoundValue) {
        this.service1InOutBoundValue = service1InOutBoundValue;
    }

    /**
     * @return the service2Name
     */
    public String getService2Name() {
        return service2Name;
    }

    /**
     * @param service2Name the service2Name to set
     */
    public void setService2Name(String service2Name) {
        this.service2Name = service2Name;
    }

    /**
     * @return the service2InOutBoundValue
     */
    public int getService2InOutBoundValue() {
        return service2InOutBoundValue;
    }

    /**
     * @param service2InOutBoundValue the service2InOutBoundValue to set
     */
    public void setService2InOutBoundValue(int service2InOutBoundValue) {
        this.service2InOutBoundValue = service2InOutBoundValue;
    }

    /**
     * @return the service3Name
     */
    public String getService3Name() {
        return service3Name;
    }

    /**
     * @param service3Name the service3Name to set
     */
    public void setService3Name(String service3Name) {
        this.service3Name = service3Name;
    }

    /**
     * @return the service3InOutBoundValue
     */
    public int getService3InOutBoundValue() {
        return service3InOutBoundValue;
    }

    /**
     * @param service3InOutBoundValue the service3InOutBoundValue to set
     */
    public void setService3InOutBoundValue(int service3InOutBoundValue) {
        this.service3InOutBoundValue = service3InOutBoundValue;
    }

    /**
     * @return the service4Name
     */
    public String getService4Name() {
        return service4Name;
    }

    /**
     * @param service4Name the service4Name to set
     */
    public void setService4Name(String service4Name) {
        this.service4Name = service4Name;
    }

    /**
     * @return the service4InOutBoundValue
     */
    public int getService4InOutBoundValue() {
        return service4InOutBoundValue;
    }

    /**
     * @param service4InOutBoundValue the service4InOutBoundValue to set
     */
    public void setService4InOutBoundValue(int service4InOutBoundValue) {
        this.service4InOutBoundValue = service4InOutBoundValue;
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
     * @return the service5Name
     */
    public String getService5Name() {
        return service5Name;
    }

    /**
     * @param service5Name the service5Name to set
     */
    public void setService5Name(String service5Name) {
        this.service5Name = service5Name;
    }

    /**
     * @return the service5InOutBoundValue
     */
    public int getService5InOutBoundValue() {
        return service5InOutBoundValue;
    }

    /**
     * @param service5InOutBoundValue the service5InOutBoundValue to set
     */
    public void setService5InOutBoundValue(int service5InOutBoundValue) {
        this.service5InOutBoundValue = service5InOutBoundValue;
    }

    /**
     * @return the service5
     */
    public Service getService5() {
        return service5;
    }

    /**
     * @param service5 the service5 to set
     */
    public void setService5(Service service5) {
        this.service5 = service5;
    }

    /**
     * @return the service1Name
     */
    public String getService1Name() {
        return service1Name;
    }
}
