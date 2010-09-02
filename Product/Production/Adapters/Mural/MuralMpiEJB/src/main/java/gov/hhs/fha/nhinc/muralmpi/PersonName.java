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

package gov.hhs.fha.nhinc.muralmpi;

/**
 *
 * @author dunnek
 */
public class PersonName
{
    String fName = "";
    String lName = "";
    String mName = "";

    public String getFirstName()
    {
        return fName;
    }
    public String getLastName()
    {
        return lName;
    }
    public String getMiddleName()
    {
        return mName;
    }
    public void setFirstName(String name)
    {
        fName = name;
    }
    public void setLastName(String name)
    {
        lName = name;
    }
    public void setMiddleName(String name)
    {
        mName = name;
    }
}
