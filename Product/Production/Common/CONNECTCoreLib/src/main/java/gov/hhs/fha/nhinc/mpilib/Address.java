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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class Address 
{
    private static Log log = LogFactory.getLog(Address.class);
    private String street1 = "";
    private String street2 = "";
    private String city = "";
    private String state = "";
    private String zip = "";

    public String getStreet1()
    {
        return street1;
    }
    public void setStreet1(String value)
    {
        street1 = value;
    }
    public void setStreet2(String value)
    {
        street2 = value;
    }
    
    public String getStreet2()
    {
        return street2;
    }
    public void setCity(String value)
    {
        city = value;
    }
    public String getCity()
    {
        return city;
    }
    public void setState(String value)
    {
        state = value;
    }
    public String getState()
    {
        return state;
    }
    public void setZip(String value)
    {
        zip = value;
    }
    public String getZip()
    {
        return zip;
    }
}
