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
import java.util.List;
import org.apache.log4j.Logger;
import org.nhind.config.common.AddAnchor;
import org.nhind.config.common.AddCertificates;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.AddTrustBundle;
import org.nhind.config.common.Anchor;
import org.nhind.config.common.AssociateTrustBundleToDomain;
import org.nhind.config.common.Certificate;
import org.nhind.config.common.DeleteTrustBundles;
import org.nhind.config.common.DisassociateTrustBundleFromDomain;
import org.nhind.config.common.DisassociateTrustBundleFromDomains;
import org.nhind.config.common.Domain;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.GetTrustBundles;
import org.nhind.config.common.GetTrustBundlesByDomain;
import org.nhind.config.common.ListCertificates;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.RemoveCertificates;
import org.nhind.config.common.Setting;
import org.nhind.config.common.TrustBundle;
import org.nhind.config.common.UpdateDomain;
import org.nhind.config.common.UpdateTrustBundleAttributes;
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

    @Override
    public void updateDomain(UpdateDomain domain) {
        try {
            directProxy.updateDomain(domain);
        } catch (Exception ex) {
            LOG.error("Unable to update domain: " + domain.getDomain().getDomainName(), ex);
        }
    }

    @Override
    public void addDomain(AddDomain domain) {
        try {
            directProxy.addDomain(domain);
        } catch (Exception ex) {
            LOG.error("Unable to add new domain: " + domain.getDomain().getDomainName(), ex);
        }
    }

    @Override
    public void deleteDomain(Domain domain) {
        try {
            directProxy.deleteDomain(domain.getDomainName());
        } catch (Exception ex) {
            LOG.error("Unable to delete domain: " + domain.getDomainName(), ex);
        }
    }

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

    @Override
    public void addSetting(String name, String value) {
        try {
            directProxy.addSetting(name, value);
        } catch (Exception ex) {
            LOG.error("Unable to add new setting: " + ex.getMessage());
        }
    }

    @Override
    public void deleteSetting(List<String> deleteNames) {
        try {
            directProxy.deleteSetting(deleteNames);
        } catch (Exception ex) {
            LOG.error("Unable to delete setting: ", ex);
        }
    }

    @Override
    public void addCertificate(AddCertificates certificate) {
        try {
            directProxy.addCertificates(certificate);
        } catch (Exception ex) {
            LOG.error("Error While adding Certificate " + ex.getMessage(), ex);
        }

    }

    @Override
    public void deleteCertificate(RemoveCertificates removeCert) {
        try {
            directProxy.removeCertificate(removeCert);
        } catch (Exception ex) {
            LOG.error("Error While removing Certificate " + ex.getMessage(), ex);
        }
    }

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

    @Override
    public List<TrustBundle> getTrustBundles(GetTrustBundles gtb) {
        List<TrustBundle> listTB = null;
        try {
            listTB = directProxy.getTrustBundles(gtb);
        } catch (Exception ex) {
            LOG.error("Unable to get List of Trust Bundles: ", ex);
        }
        return listTB;
    }

    @Override
    public void updateTrustBundle(UpdateTrustBundleAttributes utba) {
        try {
            directProxy.updateTrustBundleAttributes(utba);
        } catch (Exception ex) {
            LOG.error("Unable to update trust bundle with Name: " + utba.getTrustBundleName(), ex);
        }
    }

    @Override
    public void addTrustBundle(TrustBundle tb) {
        try {
            AddTrustBundle atb = new AddTrustBundle();
            atb.setBundle(tb);
            directProxy.addTrustBundle(atb);
        } catch (Exception ex) {
            LOG.error("Unable to add trust bundle with Name: " + tb.getBundleName(), ex);
        }
    }

    @Override
    public void deleteTrustBundle(DeleteTrustBundles tb) {
        try {
            directProxy.deleteTrustBundle(tb);
        } catch (Exception ex) {
            LOG.error("Unable to delete trust bundle ", ex);
        }
    }

    @Override
    public List<TrustBundle> getTrustBundlesByDomain(GetTrustBundlesByDomain getTrustBundlesByDomain) {
        List<TrustBundle> bundles = null;
        try {
            bundles = directProxy.getTrustBundlesByDomain(getTrustBundlesByDomain);
        } catch (Exception ex) {
            LOG.error("Unable to get Trust Bundle By Doman: ", ex);
        }
        return bundles;
    }

    @Override
    public void refreshTrustBundle(int id) {
        try {
            directProxy.refreshTrustBundle(id);
        } catch (Exception ex) {
            LOG.error("Unable to refresh Trust Bundle: " + ex);
        }
    }

    @Override
    public void associateTrustBundleToDomain(AssociateTrustBundleToDomain associateTrustBundleToDomain) {
        try {
            directProxy.associateTrustBundleToDomain(associateTrustBundleToDomain);
        } catch (Exception ex) {
            LOG.error("Unable to Associate Trust Bundle to Domain domain ID =" + associateTrustBundleToDomain.getDomainId() + " Trust Bundle ID = " + associateTrustBundleToDomain.getTrustBundleId(), ex);
        }
    }

    @Override
    public void disassociateTrustBundleFromDomain(DisassociateTrustBundleFromDomain disassociateTrustBundleFromDomain) {
        try {
            directProxy.disassociateTrustBundleFromDomain(disassociateTrustBundleFromDomain);
        } catch (Exception ex) {
            LOG.error("Unable to disassociate Trust Bundle from Domain trust bundle ID =" + disassociateTrustBundleFromDomain.getTrustBundleId(), ex);
        }
    }

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

    @Override
    public void addAnchor(AddAnchor addAnchor) {
        try {
            directProxy.addAnchor(addAnchor);
        } catch (Exception ex) {
            LOG.error("Unable to add anchor", ex);
        }
    }

    @Override
    public void deleteAnchor(RemoveAnchors removeAnchors) {
        try {
            directProxy.removeAnchors(removeAnchors);
        } catch (Exception ex) {
            LOG.error("Unable to remove anchor", ex);
        }
    }
}
