/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;

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
