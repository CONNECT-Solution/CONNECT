/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.proxy.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LiftConnectionResponseToken")
public class LiftConnectionResponseToken {

    @XmlElement(name = "Permission", required = true)
    private String permission;

    public static final String ALLOW = "granted";
    public static final String DENY = "denied";

    public LiftConnectionResponseToken() {
    }

    public LiftConnectionResponseToken(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isPermitted() {

        boolean status = false;
        if(ALLOW.equals(permission)){
            status = true;
        }
        return status;
    }
}