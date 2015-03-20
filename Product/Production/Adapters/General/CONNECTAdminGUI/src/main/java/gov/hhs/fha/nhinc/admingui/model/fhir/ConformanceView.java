/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admingui.model.fhir;

import java.util.ArrayList;
import java.util.List;

/**
 * View data for conformance resource including description.
 * @author jassmit
 */
public class ConformanceView {
    
    private String conformanceDesc;
    private List<ConformanceResource> confResources = new ArrayList<ConformanceResource>();

    public String getConformanceDesc() {
        return conformanceDesc;
    }

    public void setConformanceDesc(String conformanceDesc) {
        this.conformanceDesc = conformanceDesc;
    }

    public List<ConformanceResource> getConfResources() {
        return confResources;
    }

    public void setConfResources(List<ConformanceResource> confResources) {
        this.confResources = confResources;
    }
    
    
}
