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
package gov.hhs.fha.nhinc.admingui.services;

import java.util.List;
import org.nhind.config.AddAnchor;
import org.nhind.config.AddCertificates;
import org.nhind.config.AddDomain;
import org.nhind.config.Anchor;
import org.nhind.config.AssociateTrustBundleToDomain;
import org.nhind.config.Certificate;
import org.nhind.config.DeleteTrustBundles;
import org.nhind.config.DisassociateTrustBundleFromDomains;
import org.nhind.config.Domain;
import org.nhind.config.GetAnchorsForOwner;
import org.nhind.config.GetTrustBundleByName;
import org.nhind.config.GetTrustBundles;
import org.nhind.config.GetTrustBundlesByDomain;
import org.nhind.config.ListCertificates;
import org.nhind.config.RemoveAnchors;
import org.nhind.config.RemoveCertificates;
import org.nhind.config.Setting;
import org.nhind.config.TrustBundle;
import org.nhind.config.TrustBundleDomainReltn;
import org.nhind.config.UpdateDomain;
import org.nhind.config.UpdateTrustBundleAttributes;

/**
 *
 * @author jasonasmith
 */
public interface DirectService {

    public List<Domain> getDomains();
    public void updateDomain(UpdateDomain domain);
    public void addDomain(AddDomain domain);
    public void deleteDomain(Domain domain);

    public void addAnchor(AddAnchor addAnchor);
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner);
    public void deleteAnchor(RemoveAnchors removeAnchors);

    public List<Setting> getSetting();
    public void addSetting(String name, String value);
    public void deleteSetting(List<String> deleteNames);

    public void addCertificate(AddCertificates addcert);
    public void deleteCertificate(RemoveCertificates removeCert);
    public List<Certificate> listCertificate(ListCertificates listCert);

    public List<TrustBundle> getTrustBundles(boolean fetchAnchors);
    public TrustBundle getTrustBundleByName(String bundleName);
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors);
    public void addTrustBundle(TrustBundle b);
    public void deleteTrustBundles(List<Long> ids);
    public void updateTrustBundle(long trustBundleId, String trustBundleName, String trustBundleURL,
        Certificate signingCert, int trustBundleRefreshInterval);
    public void refreshTrustBundle(int id);
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing);
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId);
    public void disassociateTrustBundleFromDomains(long trustBundleId);
    public void disassociateTrustBundlesFromDomain(long domainId);
}
