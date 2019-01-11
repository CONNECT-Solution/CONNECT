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
/*
 Copyright (c) 2010, NHIN Direct Project
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 in the documentation and/or other materials provided with the distribution.
 3. Neither the name of the The NHIN Direct Project (nhindirect.org) nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.directconfig.service.impl;

import gov.hhs.fha.nhinc.directconfig.dao.TrustBundleDao;
import gov.hhs.fha.nhinc.directconfig.entity.Certificate;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundle;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleAnchor;
import gov.hhs.fha.nhinc.directconfig.entity.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.directconfig.entity.helpers.BundleRefreshError;
import gov.hhs.fha.nhinc.directconfig.exception.CertificateException;
import gov.hhs.fha.nhinc.directconfig.processor.BundleRefreshProcessor;
import gov.hhs.fha.nhinc.directconfig.service.ConfigurationServiceException;
import gov.hhs.fha.nhinc.directconfig.service.TrustBundleService;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import javax.jws.WebParam;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Implementation of the TrustBundleService
 *
 * @author Greg Meyer
 * @since 1.3
 */
@Service("trustBundleSvc")
@WebService(endpointInterface = "gov.hhs.fha.nhinc.directconfig.service.TrustBundleService", portName = "ConfigurationServiceImplPort", targetNamespace = "http://nhind.org/config")
public class TrustBundleServiceImpl extends SpringBeanAutowiringSupport implements TrustBundleService {
    private static final Log log = LogFactory.getLog(TrustBundleServiceImpl.class);

    @Autowired
    private TrustBundleDao dao;

    @Autowired
    private BundleRefreshProcessor bundleRefresh;

    /**
     * Initialization method.
     */
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.info("TrustBundleServiceImpl initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<TrustBundle> getTrustBundles(boolean fetchAnchors) throws ConfigurationServiceException {
        final Collection<TrustBundle> bundles = dao.getTrustBundles();

        if (!fetchAnchors) {
            for (TrustBundle bundle : bundles) {
                bundle.setTrustBundleAnchors(new ArrayList<TrustBundleAnchor>());
            }
        }

        return bundles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrustBundle getTrustBundleByName(String bundleName) throws ConfigurationServiceException {
        return dao.getTrustBundleByName(bundleName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrustBundle getTrustBundleById(long id) throws ConfigurationServiceException {
        return dao.getTrustBundleById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTrustBundle(TrustBundle bundle) throws ConfigurationServiceException {
        dao.addTrustBundle(bundle);

        // the trust bundle does not contain any of the anchors
        // they must be fetched from the URL... use the
        // refresh route to force downloading the anchors
        refreshBundle(bundle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void refreshTrustBundle(@WebParam(name = "id")
    long id) throws ConfigurationServiceException {

        final TrustBundle bundle = dao.getTrustBundleById(id);

        if (bundle != null) {
            refreshBundle(bundle);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLastUpdateError(long trustBundleId, Calendar attemptTime, BundleRefreshError error)
            throws ConfigurationServiceException {

        dao.updateLastUpdateError(trustBundleId, attemptTime, error);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTrustBundles(long[] trustBundleIds) throws ConfigurationServiceException {
        dao.deleteTrustBundles(trustBundleIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrustBundleSigningCertificate(long trustBundleId, Certificate signingCert)
            throws ConfigurationServiceException {

        try {
            dao.updateTrustBundleSigningCertificate(trustBundleId, signingCert.toCredential().getCert());
        } catch (CertificateException e) {
            throw new ConfigurationServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTrustBundleAttributes(long trustBundleId, String bundleName, String bundleUrl,
            Certificate signingCert, int refreshInterval) throws ConfigurationServiceException {

        final TrustBundle oldBundle = dao.getTrustBundleById(trustBundleId);
        String oldBundleURL = "";
        X509Certificate newSigningCert = null;

        // need to know if the URL changed... store off the old URL
        if (oldBundle != null) {
            oldBundleURL = oldBundle.getBundleURL();
        }

        try {
            // make sure the cert isn't null before converting to an X509Certificate
            if (signingCert != null && signingCert.toCredential() != null) {
                newSigningCert = signingCert.toCredential().getCert();
            }

            dao.updateTrustBundleAttributes(trustBundleId, bundleName, bundleUrl, newSigningCert, refreshInterval);

            // if the URL changed, the bundle needs to be refreshed
            if (!oldBundleURL.equals(bundleUrl)) {
                final TrustBundle bundle = dao.getTrustBundleById(trustBundleId);

                if (bundle != null) {
                    refreshBundle(bundle);
                }
            }

        } catch (CertificateException e) {
            throw new ConfigurationServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing)
            throws ConfigurationServiceException {

        dao.associateTrustBundleToDomain(domainId, trustBundleId, incoming, outgoing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId)
            throws ConfigurationServiceException {

        dao.disassociateTrustBundleFromDomain(domainId, trustBundleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundlesFromDomain(long domainId) throws ConfigurationServiceException {
        dao.disassociateTrustBundlesFromDomain(domainId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws ConfigurationServiceException {
        dao.disassociateTrustBundleFromDomains(trustBundleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors)
            throws ConfigurationServiceException {

        final Collection<TrustBundleDomainReltn> bundles = dao.getTrustBundlesByDomain(domainId);

        if (!fetchAnchors) {
            for (TrustBundleDomainReltn bundle : bundles) {
                bundle.getTrustBundle().setTrustBundleAnchors(new ArrayList<TrustBundleAnchor>());
            }
        }

        return bundles;
    }

    /*
     * Sends a bundle for refresh, which may include downloading the associated anchors
     *
     * @param bundle the TrustBundle to be refreshed
     */
    private void refreshBundle(TrustBundle bundle) {
        log.debug("Manually refreshing Trust Bundle");
        bundleRefresh.refreshBundle(bundle);
    }
}
