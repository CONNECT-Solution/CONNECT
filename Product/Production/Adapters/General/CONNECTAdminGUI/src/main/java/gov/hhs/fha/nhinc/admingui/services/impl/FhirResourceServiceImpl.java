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

package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.model.fhir.ResourceInfo;
import gov.hhs.fha.nhinc.admingui.services.FhirResourceService;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author jassmit
 */
@Service
public class FhirResourceServiceImpl implements FhirResourceService {

    private static final String PATIENT_RESOURCE_NAME = "FHIRPatientResource";
    private static final String DOCREF_RESOURCE_NAME = "FHIRDocumentReferenceResource";
    private static final String BINARY_RESOURCE_NAME = "FHIRBinaryResource";
    
    private static final String PATIENT_ICON_FILE = "patient.png";
    private static final String DOCREF_ICON_FILE = "document.png";
    private static final String BINARY_ICON_FILE = "binary.png";
    
    private static final String PATIENT_DISPLAY = "Patient Resource";
    private static final String DOCREF_DISPLAY = "Document Reference Resource";
    private static final String BINARY_DISPLAY = "Binary Resource";
    
    private static final Logger LOG = Logger.getLogger(FhirResourceServiceImpl.class);
    
    @Override
    public List<ResourceInfo> loadResources() {
        List<ResourceInfo> resources = new ArrayList<ResourceInfo>();
        
        String patientUrl = getUrl(PATIENT_RESOURCE_NAME);
        if(NullChecker.isNotNullish(patientUrl)) {
            resources.add(new ResourceInfo(PATIENT_RESOURCE_NAME, PATIENT_DISPLAY, patientUrl, PATIENT_ICON_FILE));
        }
        
        String docRefUrl = getUrl(DOCREF_RESOURCE_NAME);
        if(NullChecker.isNotNullish(docRefUrl)) {
            resources.add(new ResourceInfo(DOCREF_RESOURCE_NAME, DOCREF_DISPLAY, docRefUrl, DOCREF_ICON_FILE));
        }
        
        String binaryUrl = getUrl(BINARY_RESOURCE_NAME);
        if(NullChecker.isNotNullish(binaryUrl)) {
            resources.add(new ResourceInfo(BINARY_RESOURCE_NAME, BINARY_DISPLAY, binaryUrl, BINARY_ICON_FILE));
        }
        
        return resources;
    }

    @Override
    public void updateUrl(String serviceName, String url) throws Exception {
        ConnectionManagerCache.getInstance().updateInternalServiceUrl(serviceName, url);
    }

    @Override
    public String getConformance(String url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected String getUrl(String serviceName) {
        try {
            return ConnectionManagerCache.getInstance().getAdapterEndpointURL(serviceName, NhincConstants.ADAPTER_API_LEVEL.LEVEL_a0);
        } catch (ConnectionManagerException ex) {
            LOG.warn("Unable to access resource url for service: " + serviceName, ex);
        }
        return null;
    }
    
}
