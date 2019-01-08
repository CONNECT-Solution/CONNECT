/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.admingui.managed.fhir;

import gov.hhs.fha.nhinc.admingui.model.fhir.ConformanceResource;
import gov.hhs.fha.nhinc.admingui.model.fhir.ConformanceView;
import gov.hhs.fha.nhinc.admingui.model.fhir.ResourceInfo;
import gov.hhs.fha.nhinc.admingui.services.FhirResourceService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Backing bean for FHIR Resource service updating including conformance ping and editing url for service.
 *
 * @author jassmit
 */
@ManagedBean(name = "fhirResourceBean")
@ViewScoped
@Component
public class FhirResourceBean {

    private List<ResourceInfo> fhirResources;
    private ConformanceView confView;

    private static final Logger LOG = LoggerFactory.getLogger(FhirResourceBean.class);

    @Autowired
    private FhirResourceService fhirService;

    public List<ResourceInfo> getFhirResources() {
        if (fhirResources == null) {
            fhirResources = fhirService.loadResources();
        }
        return fhirResources;
    }

    /**
     * Listener for url changes to edit a FHIR resource url via the FhirResourceService.
     *
     * @param event
     */
    public void onUrlChange(ValueChangeEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceInfo resource = context.getApplication().evaluateExpressionGet(context, "#{fResource}",
                ResourceInfo.class);

        if (!event.getNewValue().equals(event.getOldValue())) {
            try {
                fhirService.updateUrl(resource.getServiceName(), (String) event.getNewValue());
                resource.setUrl((String) event.getNewValue());
            } catch (Exception ex) {
                LOG.warn("Unable to set new url value for resource: " + resource.getServiceName() + " with value: "
                        + event.getNewValue(), ex);
            }
        }
    }

    /**
     * Passes url from ring item to FhirResourceService and sets view for conformance data.
     *
     * @param url
     */
    public void pingForConformance(String url) {
        confView = fhirService.getConformance(url);
    }

    public void clearDialog() {
        confView = null;
    }

    public String getConformanceDesc() {
        String desc = null;
        if (confView != null) {
            desc = confView.getConformanceDesc();
        }
        return desc;
    }

    public String getConformanceUrl() {
        String url = null;
        if (confView != null) {
            url = confView.getConformanceUrl();
        }
        return url;
    }

    public void setConformanceDesc(String conformanceDesc) {
        if (confView != null) {
            confView.setConformanceDesc(conformanceDesc);
        }
    }

    public List<ConformanceResource> getConfResources() {
        if (confView != null) {
            return confView.getConfResources();
        }
        return new ArrayList<>();
    }

    public void setConfResources(List<ConformanceResource> confResources) {
        if (confView != null) {
            confView.setConfResources(confResources);
        }
    }

    public boolean hasResources() {
        return confView != null && NullChecker.isNotNullish(confView.getConfResources());
    }

}
