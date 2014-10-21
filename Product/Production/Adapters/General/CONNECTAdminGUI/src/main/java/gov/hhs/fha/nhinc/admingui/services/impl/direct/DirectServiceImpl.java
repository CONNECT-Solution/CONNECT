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
package gov.hhs.fha.nhinc.admingui.services.impl.direct;

import gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.admingui.services.exception.CreateDomainException;
import java.util.List;
import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */
@Service
public class DirectServiceImpl implements DirectService {

    @Autowired
    private DirectConfigProxy directProxy;

    private static final Logger LOG = Logger.getLogger(DirectServiceImpl.class);

    /**
     *
     * @return
     */
    @Override
    public List<Domain> getDomains() {
        List<Domain> domains = null;
        try {
            domains = directProxy.listDomains();
        } catch (Exception ex) {
            LOG.error("Error retrieving list of domains: " + ex.getMessage(), ex);
        }
        return domains;
    }

    /**
     *
     * @param domain
     * @throws CreateDomainException
     */
    @Override
    public void updateDomain(UpdateDomain domain) throws CreateDomainException {
        try {
            directProxy.updateDomain(domain);
        } catch (Exception ex) {
            throw new CreateDomainException("Duplicate Domain: " + domain.getDomain().getDomainName(), ex);
        }
    }

    /**
     *
     * @param domain
     * @throws CreateDomainException
     */
    @Override
    public void addDomain(AddDomain domain) throws CreateDomainException {
        try {
            directProxy.addDomain(domain);
        } catch (Exception ex) {
            throw new CreateDomainException("Duplicate Domain: " + domain.getDomain().getDomainName(), ex);
        }
    }

    /**
     *
     * @param domain
     */
    @Override
    public void deleteDomain(Domain domain) {
        try {
            directProxy.deleteDomain(domain.getDomainName());
        } catch (Exception ex) {
            LOG.error("Unable to delete domain: " + domain.getDomainName(), ex);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public List<Setting> getSetting() {
        List<Setting> listSetting = null;
        try {
            listSetting = directProxy.getSetting();
        } catch (Exception ex) {
            LOG.error("Error retrieving list of Setting: " + ex.getMessage(), ex);
        }
        return listSetting;
    }

    /**
     *
     * @param name
     * @param value
     * @throws Exception
     */
    @Override
    public void addSetting(String name, String value) throws Exception {
        try {
            directProxy.addSetting(name, value);
        } catch (Exception ex) {
            LOG.error("Unable to add new setting: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     *
     * @param deleteNames
     */
    @Override
    public void deleteSetting(List<String> deleteNames) {
        try {
            directProxy.deleteSetting(deleteNames);
        } catch (Exception ex) {
            LOG.error("Unable to delete setting: ", ex);
        }
    }

    /**
     *
     * @param certificate
     */
    @Override
    public void addCertificate(AddCertificates certificate) {
        try {
            directProxy.addCertificates(certificate);
        } catch (Exception ex) {
            LOG.error("Error While adding Certificate " + ex.getMessage(), ex);
        }

    }

    /**
     *
     * @param removeCert
     */
    @Override
    public void deleteCertificate(RemoveCertificates removeCert) {
        try {
            directProxy.removeCertificate(removeCert);
        } catch (Exception ex) {
            LOG.error("Error While removing Certificate " + ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param listCert
     * @return
     */
    @Override
    public List<Certificate> listCertificate(ListCertificates listCert) {
        List<Certificate> certs = null;
        try {
            certs = directProxy.listCertificates(listCert);
        } catch (Exception ex) {
            LOG.error("Error While retrieving Certificate " + ex.getMessage(), ex);
        }
        return certs;
    }

    /**
     *
     * @param fetchAnchors
     * @return
     */
    @Override
    public List<TrustBundle> getTrustBundles(boolean fetchAnchors) {
        List<TrustBundle> listTB = null;
        try {
            listTB = directProxy.getTrustBundles(fetchAnchors);
        } catch (Exception ex) {
            LOG.error("Unable to get List of Trust Bundles: ", ex);
        }
        return listTB;
    }

    /**
     *
     * @param trustBundleId
     * @param trustBundleName
     * @param trustBundleURL
     * @param signingCert
     * @param trustBundleRefreshInterval
     */
    @Override
    public void updateTrustBundle(long trustBundleId, String trustBundleName, String trustBundleURL,
        Certificate signingCert, int trustBundleRefreshInterval) {
        try {
            directProxy.updateTrustBundleAttributes(trustBundleId, trustBundleName, trustBundleURL, signingCert,
                trustBundleRefreshInterval);
        } catch (Exception ex) {
            LOG.error("Unable to update trust bundle with Name: " + trustBundleName, ex);
        }
    }

    /**
     *
     * @param tb
     */
    @Override
    public void addTrustBundle(TrustBundle tb) {
        try {
            directProxy.addTrustBundle(tb);
        } catch (Exception ex) {
            LOG.error("Unable to add trust bundle with Name: " + tb.getBundleName(), ex);
        }
    }

    /**
     *
     * @param ids
     */
    @Override
    public void deleteTrustBundles(List<Long> ids) {
        try {
            directProxy.deleteTrustBundles(ids);
        } catch (Exception ex) {
            LOG.error("Unable to delete trust bundle ", ex);
        }
    }

    /**
     *
     * @param bundleName
     * @return
     */
    @Override
    public TrustBundle getTrustBundleByName(String bundleName) {
        TrustBundle response = null;
        try {
            response = directProxy.getTrustBundleByName(bundleName);
        } catch (Exception ex) {
            LOG.error("Unable to get Trust Bundle By Name: ", ex);
        }
        return response;
    }

    /**
     *
     * @param domainId
     * @param fetchAnchors
     * @return
     */
    @Override
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors) {
        List<TrustBundleDomainReltn> bundles = null;
        try {
            bundles = directProxy.getTrustBundlesByDomain(domainId, fetchAnchors);
        } catch (Exception ex) {
            LOG.error("Unable to get Trust Bundle By Doman: ", ex);
        }
        return bundles;
    }

    /**
     *
     * @param id
     */
    @Override
    public void refreshTrustBundle(long id) {
        try {
            directProxy.refreshTrustBundle(id);
        } catch (Exception ex) {
            LOG.error("Unable to refresh Trust Bundle: " + ex);
        }
    }

    /**
     *
     * @param domainId
     * @param trustBundleId
     * @param incoming
     * @param outgoing
     */
    @Override
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing) {
        try {
            directProxy.associateTrustBundleToDomain(domainId, trustBundleId, incoming, outgoing);
        } catch (Exception ex) {
            LOG.error("Unable to associate Trust Bundle with ID " + trustBundleId + " to Domain with ID " + domainId, ex);
        }
    }

    /**
     *
     * @param domainId
     * @param trustBundleId
     */
    @Override
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) {
        try {
            directProxy.disassociateTrustBundleFromDomain(domainId, trustBundleId);
        } catch (Exception ex) {
            LOG.error("Unable to disassociate Trust Bundle with ID " + trustBundleId + " to Domain with ID " + domainId, ex);
        }
    }

    /**
     *
     * @param domainId
     */
    @Override
    public void disassociateTrustBundlesFromDomain(long domainId) {
        try {
            directProxy.disassociateTrustBundlesFromDomain(domainId);
        } catch (Exception ex) {
            LOG.error("Unable to disassociate Trust Bundles from Domain with ID " + domainId, ex);
        }
    }

    /**
     *
     * @param trustBundleId
     */
    @Override
    public void disassociateTrustBundleFromDomains(long trustBundleId) {
        try {
            directProxy.disassociateTrustBundleFromDomains(trustBundleId);
        } catch (Exception ex) {
            LOG.error("Unable to disassociate Trust Bundle with ID " + trustBundleId + " from Domains", ex);
        }
    }

    /**
     *
     * @param getAnchorsForOwner
     * @return
     */
    @Override
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner) {
        List<Anchor> anchors = null;

        try {
            anchors = directProxy.getAnchorsForOwner(getAnchorsForOwner);
        } catch (Exception ex) {
            LOG.error(
                "Error retrieving list of anchors for owner " + getAnchorsForOwner.getOwner() + ": "
                + ex.getMessage(), ex);
        }

        return anchors;
    }

    /**
     *
     * @param addAnchor
     */
    @Override
    public void addAnchor(AddAnchor addAnchor) {
        try {
            directProxy.addAnchor(addAnchor);
        } catch (Exception ex) {
            LOG.error("Unable to add anchor", ex);
        }
    }

    /**
     *
     * @param removeAnchors
     */
    @Override
    public void deleteAnchor(RemoveAnchors removeAnchors) {
        try {
            directProxy.removeAnchors(removeAnchors);
        } catch (Exception ex) {
            LOG.error("Unable to remove anchor", ex);
        }
    }

    /**
     *
     * @param addressEmail
     * @return
     */
    @Override
    public boolean removeAddress(String addressEmail) {
        boolean removed = true;
        try {
            directProxy.removeAddress(addressEmail);
        } catch (Exception ex) {
            removed = false;
            LOG.error("Unable to remove address with email: " + addressEmail, ex);
        }
        return removed;
    }
}
