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

package gov.hhs.fha.nhinc.patientcorrelation.nhinc.config;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dunnek
 */
public class ExpirationConfiguration {
    private int defExpiration;
    private String defUnits;
    private List<Expiration> expirationList;

    public ExpirationConfiguration()
    {
        defUnits = "";
        defExpiration = -1;
        expirationList = new ArrayList<Expiration>();
    }
    public ExpirationConfiguration(int defaultExpiration, String defaultUnits)
    {
       defExpiration = defaultExpiration;
       defUnits = defaultUnits;
       expirationList = new ArrayList<Expiration>();
    }
    public List<Expiration> getExpirations()
    {
        return expirationList;
    }
    public int getDefaultDuration()
    {
        return defExpiration;
    }
    public String getDefaultUnits()
    {
        return defUnits;
    }

}
