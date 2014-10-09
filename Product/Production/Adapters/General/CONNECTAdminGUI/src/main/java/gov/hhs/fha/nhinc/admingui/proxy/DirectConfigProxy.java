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

    public Domain getDomain(Long id) throws Exception;
    public void addDomain(AddDomain domain) throws Exception;
    public List<Domain> listDomains() throws Exception;
    public UpdateDomainResponse updateDomain(UpdateDomain updateDomain) throws Exception;
    public void deleteDomain(String name) throws Exception;

    public void addAnchor(AddAnchor addAnchor) throws Exception;
    public void removeAnchors(RemoveAnchors removeAnchors) throws Exception;
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner) throws Exception;

    public void addSetting(String name, String Value) throws Exception;
    public List<Setting> getSetting() throws Exception;
    public void deleteSetting(List<String> deleteNames) throws Exception;

    public void addCertificates(AddCertificates certificate) throws Exception;
    public void removeCertificate(RemoveCertificates cert) throws Exception;
    public List<Certificate> listCertificates(ListCertificates listCert) throws Exception;

    public List<TrustBundle> getTrustBundles(boolean fetchAnchors) throws Exception;
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors) throws Exception;
    public TrustBundle getTrustBundleByName(String bundleName) throws Exception;

    public void addTrustBundle(TrustBundle b) throws Exception;
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing) throws Exception;
    public void deleteTrustBundles(List<Long> ids) throws Exception;
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) throws Exception;
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws Exception;
    public void disassociateTrustBundlesFromDomain(long domainId) throws Exception;
    public void refreshTrustBundle(int id) throws Exception;
    public void updateTrustBundleAttributes(long trustBundleId, String trustBundleName, String trustBundleURL,
        Certificate signingCert, int trustBundleRefreshInterval) throws Exception;
    public void removeAddress(String addressEmail) throws Exception;
}
