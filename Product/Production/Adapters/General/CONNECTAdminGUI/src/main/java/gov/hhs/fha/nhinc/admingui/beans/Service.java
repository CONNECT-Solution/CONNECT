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

public class Service {
	    
        private int inboundvalue;
	private int outboundvalue;
        private int inoutboundvalue;
	private String servicename; 
	
	public int getInboundvalue() {
		return inboundvalue;
	}
	public void setInboundvalue(int inboundvalue) {
		this.inboundvalue = inboundvalue;
	}
        
        public int getInoutboundvalue() {
		return inoutboundvalue;
	}
	public void setInoutboundvalue(int inoutboundvalue) {
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
}
       