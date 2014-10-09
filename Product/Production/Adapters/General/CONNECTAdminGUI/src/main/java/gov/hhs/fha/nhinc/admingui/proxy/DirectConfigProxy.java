/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package gov.hhs.fha.nhinc.admingui.proxy;

import java.util.List;
import org.nhind.config.AddAnchor;
import org.nhind.config.AddCertificates;
import org.nhind.config.AddDomain;
import org.nhind.config.Anchor;
import org.nhind.config.Certificate;
import org.nhind.config.Domain;
import org.nhind.config.GetAnchorsForOwner;
import org.nhind.config.ListCertificates;
import org.nhind.config.RemoveAnchors;
import org.nhind.config.RemoveCertificates;
import org.nhind.config.Setting;
import org.nhind.config.TrustBundle;
import org.nhind.config.TrustBundleDomainReltn;
import org.nhind.config.UpdateDomain;
import org.nhind.config.UpdateDomainResponse;

/**
 *
 * @author jasonasmith
 */
public interface DirectConfigProxy {

    /**
     * Direct Config proxy call to get direct domain with given ID.
     * @param id
     * @return
     * @throws Exception
     */
    public Domain getDomain(Long id) throws Exception;

    /**
     * Direct Config proxy call to add given direct domain.
     * @param domain
     * @throws Exception
     */
    public void addDomain(AddDomain domain) throws Exception;

    /**
     * Direct Config proxy call to get all direct domains.
     * @return
     * @throws Exception
     */
    public List<Domain> listDomains() throws Exception;

    /**
     * Direct Config proxy call to update given direct domain.
     * @param updateDomain
     * @return
     * @throws Exception
     */
    public UpdateDomainResponse updateDomain(UpdateDomain updateDomain) throws Exception;

    /**
     * Direct Config proxy call to delete direct domain with given name.
     * @param name
     * @throws Exception
     */
    public void deleteDomain(String name) throws Exception;

    /**
     * Direct Config proxy call to add given direct anchor.
     * @param addAnchor
     * @throws Exception
     */
    public void addAnchor(AddAnchor addAnchor) throws Exception;

    /**
     * Direct Config proxy call to delete given anchors.
     * @param removeAnchors
     * @throws Exception
     */
    public void removeAnchors(RemoveAnchors removeAnchors) throws Exception;

    /**
     * Direct Config proxy call to get direct anchors for given owner.
     * @param getAnchorsForOwner
     * @return
     * @throws Exception
     */
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner) throws Exception;

    /**
     * Direct Config proxy call to add a direct setting with given key value pair.
     * @param name
     * @param Value
     * @throws Exception
     */
    public void addSetting(String name, String Value) throws Exception;

    /**
     * Direct Config proxy call to get all direct settings.
     * @return
     * @throws Exception
     */
    public List<Setting> getSetting() throws Exception;

    /**
     * Direct Config proxy call to delete all direct settings with given names.
     * @param deleteNames
     * @throws Exception
     */
    public void deleteSetting(List<String> deleteNames) throws Exception;

    /**
     * Direct Config proxy call to add given direct certificates.
     * @param certificate
     * @throws Exception
     */
    public void addCertificates(AddCertificates certificate) throws Exception;

    /**
     * Direct Config proxy call to remove given direct certificate.
     * @param cert
     * @throws Exception
     */
    public void removeCertificate(RemoveCertificates cert) throws Exception;

    /**
     * Direct Config proxy call to get all direct certificates from given criteria.
     * @param listCert
     * @return
     * @throws Exception
     */
    public List<Certificate> listCertificates(ListCertificates listCert) throws Exception;

    /**
     * Direct Config proxy call to get all direct trust bundles with option of including anchors.
     * @param fetchAnchors
     * @return
     * @throws Exception
     */
    public List<TrustBundle> getTrustBundles(boolean fetchAnchors) throws Exception;

    /**
     * Direct Config proxy call to get all direct trust bundles for given domain with the option of including anchors.
     * @param domainId
     * @param fetchAnchors
     * @return
     * @throws Exception
     */
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors) throws Exception;

    /**
     * Direct Config proxy call to get a direct trust bundle with the given name.
     * @param bundleName
     * @return
     * @throws Exception
     */
    public TrustBundle getTrustBundleByName(String bundleName) throws Exception;

    /**
     * Direct Config proxy call to add the given direct trust bundle.
     * @param b
     * @throws Exception
     */
    public void addTrustBundle(TrustBundle b) throws Exception;

    /**
     * Direct Config proxy call to associate a direct trust bundle with a direct domain.
     * @param domainId
     * @param trustBundleId
     * @param incoming
     * @param outgoing
     * @throws Exception
     */
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing) throws Exception;

    /**
     * Direct Config proxy call to delete direct trust bundles with the given IDs.
     * @param ids
     * @throws Exception
     */
    public void deleteTrustBundles(List<Long> ids) throws Exception;

    /**
     * Direct Config proxy call to disassociate the direct trust bundle with the given ID from
     * the direct domain with the other given ID.
     * @param domainId
     * @param trustBundleId
     * @throws Exception
     */
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) throws Exception;

    /**
     * Direct Config proxy call to disassociate the direct trust bundle with the given ID from all domains.
     * @param trustBundleId
     * @throws Exception
     */
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws Exception;

    /**
     * Direct Config proxy call to disassociate all direct trust bundles from the domain with the given ID.
     * @param domainId
     * @throws Exception
     */
    public void disassociateTrustBundlesFromDomain(long domainId) throws Exception;

    /**
     * Direct Config proxy call to refresh the given direct trust bundle.
     * @param id
     * @throws Exception
     */
    public void refreshTrustBundle(int id) throws Exception;

    /**
     * Direct Config proxy call to update a direct trust bundle with the given parameters.
     * @param trustBundleId
     * @param trustBundleName
     * @param trustBundleURL
     * @param signingCert
     * @param trustBundleRefreshInterval
     * @throws Exception
     */
    public void updateTrustBundleAttributes(long trustBundleId, String trustBundleName, String trustBundleURL,
        Certificate signingCert, int trustBundleRefreshInterval) throws Exception;

    /**
     * Direct Config proxy call to delete a direct domain address.
     * @param addressEmail
     * @throws Exception
     */
    public void removeAddress(String addressEmail) throws Exception;
}
