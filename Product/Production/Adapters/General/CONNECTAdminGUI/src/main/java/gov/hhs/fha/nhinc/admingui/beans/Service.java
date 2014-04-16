/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.beans;

import javax.faces.bean.SessionScoped;

/**
 *
 * @author sadusumilli
 */
@SessionScoped
public class Service {
	    
        private Organization organization;
	private int inboudvalue;
	private int outboundvalue;
        private int inoutboundvalue;
	private String servicename; // like pd, dq, dr, ds etc..
	
	public int getInboudvalue() {
		return inboudvalue;
	}
	public void setInboudvalue(int inboudvalue) {
		this.inboudvalue = inboudvalue;
	}
        
        public int getInoutboudvalue() {
		return inoutboundvalue;
	}
	public void setInoutboudvalue(int inoutboundvalue) {
		this.inoutboundvalue = inoutboundvalue;
	}
        
	public int getOutboundvalue() {
		return outboundvalue;
	}
	public void setOutboundvalue(int outboundvalue) {
		this.outboundvalue = outboundvalue;
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
        
        public void setOrganization(Organization organization) {
            this.organization = organization;
        }
        
        public Organization getOrganization() {
            return organization;
        }
}
