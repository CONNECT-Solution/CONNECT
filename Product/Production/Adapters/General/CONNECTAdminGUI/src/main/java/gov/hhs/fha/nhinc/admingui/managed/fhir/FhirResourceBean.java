/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.admingui.client.fhir.ConformanceClient;
import gov.hhs.fha.nhinc.admingui.model.fhir.ConformanceResource;
import gov.hhs.fha.nhinc.admingui.model.fhir.ResourceInfo;
import gov.hhs.fha.nhinc.admingui.services.FhirResourceService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.log4j.Logger;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestComponent;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceOperationComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "fhirResourceBean")
@ViewScoped
@Component
public class FhirResourceBean {

    private List<ResourceInfo> fhirResources;

    private static final Logger LOG = Logger.getLogger(FhirResourceBean.class);

    private String conformanceDesc;
    private List<ConformanceResource> confResources = new ArrayList<ConformanceResource>();

    @Autowired
    private FhirResourceService fhirService;

    public List<ResourceInfo> getFhirResources() {
        if (fhirResources == null) {
            fhirResources = fhirService.loadResources();
        }
        return fhirResources;
    }
    
    public void onUrlChange(ValueChangeEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceInfo resource = context.getApplication().evaluateExpressionGet(context, "#{fResource}", ResourceInfo.class);

        if (!event.getNewValue().equals(event.getOldValue())) {
            try {
                fhirService.updateUrl(resource.getServiceName(), (String) event.getNewValue());
                resource.setUrl((String) event.getNewValue());
            } catch (Exception ex) {
                LOG.warn("Unable to set new url value for resource: " + resource.getServiceName() + " with value: " + event.getNewValue(), ex);
            }
        }
    }

    public void pingForConformance(String baseUri) {
        ConformanceClient fhirClient = new ConformanceClient();
        Conformance conformance;
        try {
            conformance = fhirClient.getConformanceStatement(baseUri);
            if (conformance != null) {
                if (NullChecker.isNotNullish(conformance.getDescriptionSimple())) {
                    conformanceDesc = conformance.getDescriptionSimple();
                } else {
                    conformanceDesc = "No Description for: " + baseUri;
                }

                if (NullChecker.isNotNullish(conformance.getRest())) {
                    populateFromRest(conformance.getRest());
                }

            } else {
                conformanceDesc = "Unable to retrieve Conformance resource for base: " + baseUri;
            }
        } catch (Exception ex) {
            LOG.error("Could not get conformance statement due to: " + ex.getMessage(), ex);
        }
    }

    public void clearDialog() {
        conformanceDesc = null;
        confResources.clear();
    }

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

    public boolean hasResources() {
        return confResources != null && !confResources.isEmpty();
    }

    private void populateFromRest(List<ConformanceRestComponent> rest) {
        for (ConformanceRestComponent component : rest) {
            if (NullChecker.isNotNullish(component.getResource())) {
                populateConfResources(component.getResource());
            }
        }
    }

    private void populateConfResources(List<ConformanceRestResourceComponent> resources) {
        for (ConformanceRestResourceComponent resource : resources) {
            ConformanceResource builtResource = new ConformanceResource();
            builtResource.setName(resource.getTypeSimple());
            if (NullChecker.isNotNullish(resource.getOperation())) {
                populateOperations(builtResource, resource.getOperation());
            }
            confResources.add(builtResource);
        }
    }

    private void populateOperations(ConformanceResource builtResource, List<ConformanceRestResourceOperationComponent> operations) {
        for (ConformanceRestResourceOperationComponent operation : operations) {
            if (operation.getCode() != null) {
                switch (operation.getCode().getValue()) {
                    case create:
                        builtResource.setSupportingCreate(true);
                        break;
                    case read:
                        builtResource.setSupportingRead(true);
                        break;
                    case vread:
                        builtResource.setSupportingVRead(true);
                        break;
                    case validate:
                        builtResource.setSupportingValidate(true);
                        break;
                    case delete:
                        builtResource.setSupportingDelete(true);
                        break;
                    case update:
                        builtResource.setSupportingUpdate(true);
                        break;
                    case searchtype:
                        builtResource.setSupportingSearchType(true);
                        break;
                }
            }
        }
    }

}
