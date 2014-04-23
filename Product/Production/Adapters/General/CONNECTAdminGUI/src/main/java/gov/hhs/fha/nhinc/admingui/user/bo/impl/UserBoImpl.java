package gov.hhs.fha.nhinc.admingui.user.bo.impl;

import gov.hhs.fha.nhinc.admingui.user.bo.UserBo;

import org.springframework.stereotype.Service;


@Service
public class UserBoImpl implements UserBo{
 
	public String getMessage() {
		
		return "JSF 2 + Spring Integration - auto scan!";
	
	}
 
}