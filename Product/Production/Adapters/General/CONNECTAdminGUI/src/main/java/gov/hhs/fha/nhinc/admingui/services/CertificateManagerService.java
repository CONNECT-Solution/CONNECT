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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.callback.opensaml.CertificateDTO;
import gov.hhs.fha.nhinc.callback.opensaml.CertificateManagerException;
import gov.hhs.fha.nhinc.common.configadmin.SimpleCertificateResponseMessageType;
import java.util.List;
import java.util.Map;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author tjafri
 */
public interface CertificateManagerService {

    public List<CertificateDTO> fetchKeyStores() throws CertificateManagerException;

    public List<CertificateDTO> fetchTrustStores() throws CertificateManagerException;

    public String getKeyStoreLocation();

    public String getTrustStoreLocation();

    public List<CertificateDTO> refreshKeyStores() throws CertificateManagerException;

    public List<CertificateDTO> refreshTrustStores(boolean refreshCache) throws CertificateManagerException;

    public CertificateDTO createCertificate(byte[] data);

    public boolean isAliasInUse(String alias, List<CertificateDTO> certs);

    public boolean isLeafOnlyCertificate(CertificateDTO cert);

    public boolean importCertificate(CertificateDTO cert, boolean refresh, String hashToken) throws Exception;

    public SimpleCertificateResponseMessageType deleteCertificateFromTrustStore(String alias, String hashToken)
        throws CertificateManagerException;

    public boolean updateCertificate(String oldAlias, CertificateDTO cert, String hashToken)
        throws CertificateManagerException;

    public String getHashToken(String trustStorePasskey) throws CertificateManagerException;

    public List<CertificateDTO> listChainOfTrust(String alias) throws CertificateManagerException;

    public SimpleCertificateResponseMessageType deleteTempKeystore();

    public boolean createCertificate(String alias, String commonName, String organizationalUnit,
        String organization, String countryName);

    public SimpleCertificateResponseMessageType createCSR(String alias);

    public SimpleCertificateResponseMessageType importToKeystore(String alias, UploadedFile serverFile,
        Map<String, UploadedFile> intermediateFiles, UploadedFile rootFile);

    public SimpleCertificateResponseMessageType completeImportWizard();

    public SimpleCertificateResponseMessageType listTemporaryAlias();

    public SimpleCertificateResponseMessageType undoImportKeystore(String alias,
        Map<String, UploadedFile> intermediateFiles, UploadedFile rootFile);

}
