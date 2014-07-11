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

import gov.hhs.fha.nhinc.admingui.model.direct.DirectAgent;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy;
import gov.hhs.fha.nhinc.admingui.services.DirectService;

import java.util.List;

import org.apache.log4j.Logger;

import org.nhind.config.common.AddAnchor;
import org.nhind.config.common.AddCertificates;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.Anchor;
import org.nhind.config.common.Certificate;
import org.nhind.config.common.Domain;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.ListCertificates;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.RemoveCertificates;
import org.nhind.config.common.UpdateDomain;

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
    public List<DirectAgent> getAgents() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void updateAgent(DirectAgent agent) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void addAgent(DirectAgent agent) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void deleteAgent(DirectAgent agent) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
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

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.admingui.services.DirectService#listCertificate(org.nhind.config.common.ListCertificates)
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

    @Override
    public List<DirectTrustBundle> getTrustBundles() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void updateTrustBundle(DirectTrustBundle tb) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void addTrustBundle(DirectTrustBundle tb) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

    @Override
    public void deleteTrustBundle(DirectTrustBundle tb) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
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
