/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mpilib;

/**
 *
 * @author dunnek
 */
public class PhoneNumber 
 implements java.io.Serializable
{
    static final long serialVersionUID = 5000000000000000000L;
    private String phoneNumber = "";

    public PhoneNumber()
    {
        phoneNumber = "";
    }
    public PhoneNumber(String val)
    {
        phoneNumber = val;
    }

    public void setPhoneNumber(String val)
    {
        this.phoneNumber = val;
    }
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
}
