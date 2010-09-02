/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.common.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LiftConnectionRequest")
public class LiftConnectionRequestToken {
	@XmlElement(name = "RequestGUID", required = true)
	private String requestGUID;

	public LiftConnectionRequestToken() {
	}

	public LiftConnectionRequestToken(String request) {
		this.requestGUID = request;
	}

	public String getRequestGUID() {
		return requestGUID;
	}

	public void setRequestGUID(String request) {
		this.requestGUID = request;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestGUID == null) ? 0 : requestGUID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiftConnectionRequestToken other = (LiftConnectionRequestToken) obj;
		if (requestGUID == null) {
			if (other.requestGUID != null)
				return false;
		} else if (!requestGUID.equals(other.requestGUID))
			return false;
		return true;
	}
}