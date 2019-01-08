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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.client.fhir.ConformanceClient;
import gov.hhs.fha.nhinc.admingui.model.fhir.ConformanceResource;
import gov.hhs.fha.nhinc.admingui.model.fhir.ConformanceView;
import gov.hhs.fha.nhinc.admingui.model.fhir.ResourceInfo;
import gov.hhs.fha.nhinc.admingui.services.FhirResourceService;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.instance.model.Conformance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for updating FHIR resource url and pulling Conformance resource.
 *
 * @author jassmit
 */
@Service
public class FhirResourceServiceImpl implements FhirResourceService {

    private static final String PATIENT_ICON_FILE = "patient.png";
    private static final String DOCREF_ICON_FILE = "document.png";
    private static final String BINARY_ICON_FILE = "binary.png";

    private static final String PATIENT_DISPLAY = "Patient Resource";
    private static final String DOCREF_DISPLAY = "Document Reference Resource";
    private static final String BINARY_DISPLAY = "Binary Resource";

    private static final Logger LOG = LoggerFactory.getLogger(FhirResourceServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResourceInfo> loadResources() {
        List<ResourceInfo> resources = new ArrayList<>();

        String patientUrl = getUrl(PATIENT_RESOURCE_NAME);
        if (NullChecker.isNotNullish(patientUrl)) {
            resources.add(new ResourceInfo(PATIENT_RESOURCE_NAME, PATIENT_DISPLAY, patientUrl, PATIENT_ICON_FILE));
        }

        String docRefUrl = getUrl(DOCREF_RESOURCE_NAME);
        if (NullChecker.isNotNullish(docRefUrl)) {
            resources.add(new ResourceInfo(DOCREF_RESOURCE_NAME, DOCREF_DISPLAY, docRefUrl, DOCREF_ICON_FILE));
        }

        String binaryUrl = getUrl(BINARY_RESOURCE_NAME);
        if (NullChecker.isNotNullish(binaryUrl)) {
            resources.add(new ResourceInfo(BINARY_RESOURCE_NAME, BINARY_DISPLAY, binaryUrl, BINARY_ICON_FILE));
        }

        return resources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUrl(String serviceName, String url) throws Exception {
        WebServiceProxyHelper.updateAdapterServiceUrlBy(serviceName, url);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConformanceView getConformance(String url) {
        ConformanceClient fhirClient = new ConformanceClient();
        ConformanceView view = null;
        try {
            Conformance conformance = fhirClient.getConformanceStatement(url);
            if (conformance != null) {
                view = new ConformanceView();
                view.setConformanceDesc(conformance.getDescriptionSimple());
                view.setConfResources(populateFromRest(conformance.getRest()));
                view.setConformanceUrl(url);
            }
        } catch (Exception ex) {
            LOG.error("Could not get conformance statement due to: {}", ex.getMessage(), ex);
        }
        return view;
    }

    protected String getUrl(String serviceName) {
        try {
            return WebServiceProxyHelper.getAdapterEndpointURLBy(serviceName,
                NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);
        } catch (ExchangeManagerException ex) {
            LOG.warn("Unable to access resource url for service: {}", serviceName, ex);
        }
        return null;
    }

    private List<ConformanceResource> populateFromRest(List<Conformance.ConformanceRestComponent> rest) {
        List<ConformanceResource> confResources = new ArrayList<>();
        for (Conformance.ConformanceRestComponent component : rest) {
            if (NullChecker.isNotNullish(component.getResource())) {
                confResources.addAll(populateConfResources(component.getResource()));
            }
        }
        return confResources;
    }

    private List<ConformanceResource> populateConfResources(
        List<Conformance.ConformanceRestResourceComponent> resources) {
        List<ConformanceResource> confResources = new ArrayList<>();
        for (Conformance.ConformanceRestResourceComponent resource : resources) {
            ConformanceResource builtResource = new ConformanceResource();
            builtResource.setName(resource.getTypeSimple());
            if (NullChecker.isNotNullish(resource.getOperation())) {
                populateOperations(builtResource, resource.getOperation());
            }
            confResources.add(builtResource);
        }
        return confResources;
    }

    private void populateOperations(ConformanceResource builtResource,
        List<Conformance.ConformanceRestResourceOperationComponent> operations) {
        for (Conformance.ConformanceRestResourceOperationComponent operation : operations) {
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
