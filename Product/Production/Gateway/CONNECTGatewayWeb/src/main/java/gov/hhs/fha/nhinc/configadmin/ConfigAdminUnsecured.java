/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.configadmin;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateManager;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerImpl;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateRequestMessageType;
import gov.hhs.fha.nhinc.common.configadmin.ImportCertificateResponseMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class ConfigAdminUnsecured implements gov.hhs.fha.nhinc.configadmin.EntityConfigAdminPortType {
    
    private static final Logger LOG = LoggerFactory.getLogger(ConfigAdminUnsecured.class);

    @Override
    public ImportCertificateResponseMessageType importCertificate(ImportCertificateRequestMessageType importCertificateRequest) {
        ImportCertificateResponseMessageType response = new ImportCertificateResponseMessageType();
        
        CertificateManager certManager = CertificateManagerImpl.getInstance();
        try {
            certManager.importCertificate(importCertificateRequest.getImportCertRequest().getAlias(), importCertificateRequest.getImportCertRequest().getCertData());
            response.setStatus(true);
            LOG.info("Certificate imported with alias {} by user {}.", importCertificateRequest.getImportCertRequest().getAlias(), importCertificateRequest.getConfigAssertion().getUserInfo().getUserName());
        } catch (CertificateManagerException ex) {
            LOG.error("Unable to import certificate due to: {}", ex.getLocalizedMessage(), ex);
            response.setStatus(false);
            response.setMessage(ex.getLocalizedMessage());
        }
        
        return response;
    }
    
}
