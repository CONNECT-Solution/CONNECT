/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.event.service;

import gov.hhs.fha.nhinc.admingui.event.model.EventNwhinOrganization;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
public interface EventCountService {
    
    public void setCounts();
    
    public List<EventNwhinOrganization> getTotalOrganizations();
    
    public List<EventNwhinOrganization> getInboundOrganizations();
    
    public List<EventNwhinOrganization> getOutboundOrganizations();
}
