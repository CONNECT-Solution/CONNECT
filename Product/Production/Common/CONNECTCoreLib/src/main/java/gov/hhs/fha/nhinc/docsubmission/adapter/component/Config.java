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

package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dunnek
 */
public class Config {

    private List<RoutingConfig> routingInfo = null;

    public Config()
    {
        routingInfo = new ArrayList<RoutingConfig>();
    }
    public List<RoutingConfig> getRoutingInfo()
    {
        return routingInfo;
    }
    public void setRoutingInfo(List<RoutingConfig> value)
    {
        routingInfo = value;
    }
}
