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

import gov.hhs.fha.nhinc.admingui.services.exception.DomainException;
import gov.hhs.fha.nhinc.direct.config.AddAnchor;
import gov.hhs.fha.nhinc.direct.config.AddCertificates;
import gov.hhs.fha.nhinc.direct.config.AddDomain;
import gov.hhs.fha.nhinc.direct.config.Anchor;
import gov.hhs.fha.nhinc.direct.config.Certificate;
import gov.hhs.fha.nhinc.direct.config.Domain;
import gov.hhs.fha.nhinc.direct.config.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.direct.config.ListCertificates;
import gov.hhs.fha.nhinc.direct.config.RemoveAnchors;
import gov.hhs.fha.nhinc.direct.config.RemoveCertificates;
import gov.hhs.fha.nhinc.direct.config.Setting;
import gov.hhs.fha.nhinc.direct.config.TrustBundle;
import gov.hhs.fha.nhinc.direct.config.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.direct.config.UpdateDomain;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
public interface DirectService {

    /**
     * Get a list of all direct domains.
     *
     * @return
     */
    public List<Domain> getDomains();

    /**
     * Update a direct domain.
     *
     * @param domain
     * @throws DomainException
     */
    public void updateDomain(UpdateDomain domain) throws DomainException;

    /**
     *
     * @param domain
     * @throws DomainException
     */
    public void addDomain(AddDomain domain) throws DomainException;

    /**
     * Delete the provided direct domain.
     *
     * @param domain
     */
    public void deleteDomain(Domain domain);

    /**
     *
     * @param addAnchor
     */
    public void addAnchor(AddAnchor addAnchor);

    /**
     * Get all direct anchors for the given owner.
     *
     * @param getAnchorsForOwner
     * @return
     */
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner);

    /**
     * Delete the given list of direct anchors.
     *
     * @param removeAnchors
     */
    public void deleteAnchor(RemoveAnchors removeAnchors);

    /**
     * Get all direct settings.
     *
     * @return
     */
    public List<Setting> getSetting();

    /**
     * Add the given key value pair as a direct setting.
     *
     * @param name
     * @param value
     * @throws Exception
     */
    public void addSetting(String name, String value) throws Exception;

    /**
     * Delete the given direct settings with the given names.
     *
     * @param deleteNames
     */
    public void deleteSetting(List<String> deleteNames);

    /**
     * Add the given direct certificates.
     *
     * @param addcert
     */
    public void addCertificate(AddCertificates addcert);

    /**
     * Remove the given direct certificates.
     *
     * @param removeCert
     */
    public void deleteCertificate(RemoveCertificates removeCert);

    /**
     * Get all direct certificates for the given certificate criteria.
     *
     * @param listCert
     * @return
     */
    public List<Certificate> listCertificate(ListCertificates listCert);

    /**
     * Get all direct trust bundles with the option of including anchors.
     *
     * @param fetchAnchors
     * @return
     */
    public List<TrustBundle> getTrustBundles(boolean fetchAnchors);

    /**
     * Get direct trust bundle for the given name.
     *
     * @param bundleName
     * @return
     */
    public TrustBundle getTrustBundleByName(String bundleName);

    /**
     * Get all direct trust bundles for the given domain with the option of including anchors.
     *
     * @param domainId
     * @param fetchAnchors
     * @return
     */
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors);

    /**
     * Add the given direct trust bundle.
     *
     * @param b
     */
    public void addTrustBundle(TrustBundle b);

    /**
     *
     * @param ids
     */
    public void deleteTrustBundles(List<Long> ids);

    /**
     * Update a direct trust bundle with the given fields.
     *
     * @param trustBundleId
     * @param trustBundleName
     * @param trustBundleURL
     * @param signingCert
     * @param trustBundleRefreshInterval
     */
    public void updateTrustBundle(long trustBundleId, String trustBundleName, String trustBundleURL,
            Certificate signingCert, int trustBundleRefreshInterval);

    /**
     * Refresh the given direct trust bundle by ID.
     *
     * @param id
     */
    public void refreshTrustBundle(long id);

    /**
     * Associate the given direct trust bundle to the given direct domain.
     *
     * @param domainId
     * @param trustBundleId
     * @param incoming
     * @param outgoing
     */
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing);

    /**
     * Disassociate the given direct trust bundle to the given direct domain.
     *
     * @param domainId
     * @param trustBundleId
     */
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId);

    /**
     * Disassociate the given direct trust bundle to all domains.
     *
     * @param trustBundleId
     */
    public void disassociateTrustBundleFromDomains(long trustBundleId);

    /**
     * Disassociate all direct trust bundles to the given domain.
     *
     * @param domainId
     */
    public void disassociateTrustBundlesFromDomain(long domainId);

    /**
     * Delete given domain address.
     *
     * @param addressEmail
     * @return
     */
    public boolean removeAddress(String addressEmail);
}
