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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.event.model.Certificate;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import java.util.List;

/**
 *
 * @author tjafri
 */
public interface CertificateManagerService {

    public List<Certificate> fetchKeyStores();

    public List<Certificate> fetchTrustStores();

    public String getKeyStoreLocation();

    public String getTrustStoreLocation();

    public List<Certificate> refreshKeyStores();

    public List<Certificate> refreshTrustStores();

    public Certificate createCertificate(byte[] data);

    public boolean isAliasInUse(String alias, List<Certificate> certs);

    public boolean isLeafOnlyCertificate(Certificate cert);

    public void importCertificate(Certificate cert) throws CertificateManagerException;

    public boolean deleteCertificateFromTrustStore(String alias) throws CertificateManagerException;

    public boolean validateTrustStorePassKey(String passkey);

    public boolean updateCertificateTS(String oldAlias, Certificate cert) throws CertificateManagerException;

    public boolean updateCertificateKS(String oldAlias, Certificate cert) throws CertificateManagerException;
    
    public Certificate restoreCertificate();
}
