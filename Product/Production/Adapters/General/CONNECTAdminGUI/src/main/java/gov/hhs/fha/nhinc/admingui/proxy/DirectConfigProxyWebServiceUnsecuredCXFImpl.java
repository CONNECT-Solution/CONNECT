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
package gov.hhs.fha.nhinc.admingui.proxy;

import gov.hhs.fha.nhinc.admingui.proxy.service.DirectConfigUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.admingui.services.exception.DomainException;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.direct.config.AddAnchor;
import gov.hhs.fha.nhinc.direct.config.AddCertificates;
import gov.hhs.fha.nhinc.direct.config.AddDomain;
import gov.hhs.fha.nhinc.direct.config.Anchor;
import gov.hhs.fha.nhinc.direct.config.Certificate;
import gov.hhs.fha.nhinc.direct.config.ConfigurationService;
import gov.hhs.fha.nhinc.direct.config.Domain;
import gov.hhs.fha.nhinc.direct.config.GetAnchorsForOwner;
import gov.hhs.fha.nhinc.direct.config.ListCertificates;
import gov.hhs.fha.nhinc.direct.config.RemoveAnchors;
import gov.hhs.fha.nhinc.direct.config.RemoveCertificates;
import gov.hhs.fha.nhinc.direct.config.Setting;
import gov.hhs.fha.nhinc.direct.config.TrustBundle;
import gov.hhs.fha.nhinc.direct.config.TrustBundleDomainReltn;
import gov.hhs.fha.nhinc.direct.config.UpdateDomain;
import gov.hhs.fha.nhinc.direct.config.UpdateDomainResponse;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.List;

/**
 *
 * @author jasonasmith
 */
@SuppressWarnings("unchecked")
public class DirectConfigProxyWebServiceUnsecuredCXFImpl implements DirectConfigProxy {

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private final Class<ConfigurationService> directConfigClazz = ConfigurationService.class;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#getDomain(int)
     */
    @Override
    public Domain getDomain(Long id) throws Exception {
        return (Domain) getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_GET_DOMAIN, id);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#addDomain(gov.hhs.fha.nhinc.direct.config.AddDomain)
     */
    @Override
    public void addDomain(AddDomain domain) throws DomainException {
        try {
            getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_DOMAIN, domain);
        } catch (Exception e) {
            throw new DomainException("Could not create new domain " + domain.getDomain().getDomainName(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#listDomains()
     */
    @Override
    public List<Domain> listDomains() throws Exception {
        return (List<Domain>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_LIST_DOMAINS, null, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#updateDomain(gov.hhs.fha.nhinc.direct.config.UpdateDomain)
     */
    @Override
    public UpdateDomainResponse updateDomain(UpdateDomain updateDomain) throws DomainException {
        try {
            return (UpdateDomainResponse) getClient().invokePort(directConfigClazz,
                    DirectConfigConstants.DIRECT_CONFIG_UPDATE_DOMAIN, updateDomain);
        } catch (Exception e) {
            throw new DomainException("Could not update domain " + updateDomain.getDomain().getDomainName(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#deleteDomain(String)
     */
    @Override
    public void deleteDomain(String name) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_DOMAIN, name);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#addAnchor(gov.hhs.fha.nhinc.direct.config.AddAnchor)
     */
    @Override
    public void addAnchor(AddAnchor addAnchor) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_ANCHOR,
                addAnchor.getAnchor());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#removeAnchors(gov.hhs.fha.nhinc.direct.config.RemoveAnchors)
     */
    @Override
    public void removeAnchors(RemoveAnchors removeAnchors) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_ANCHOR,
                removeAnchors.getAnchorId());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#getAnchorsForOwner(gov.hhs.fha.nhinc.direct.config.
     * GetAnchorsForOwner)
     */
    @Override
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner) throws Exception {
        return (List<Anchor>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_GET_ANCHORS_FOR_OWNER, getAnchorsForOwner.getOwner(),
                getAnchorsForOwner.getOptions());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#addCertificates(gov.hhs.fha.nhinc.direct.config.
     * AddCertificates)
     */
    @Override
    public void addCertificates(AddCertificates certificate) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_CERT, certificate.getCerts());

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#removeCertificate(gov.hhs.fha.nhinc.direct.config.
     * RemoveCertificates)
     */
    @Override
    public void removeCertificate(RemoveCertificates certificate) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_CERT,
                certificate.getCertificateIds());

    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#listCertificates(gov.hhs.fha.nhinc.direct.config.
     * ListCertificates)
     */
    @Override
    public List<Certificate> listCertificates(ListCertificates listCert) throws Exception {
        return (List<Certificate>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_LIST_CERTS, listCert.getLastCertificateId(),
                listCert.getMaxResutls(), listCert.getOptions());
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#addSetting(String, String)
     */
    @Override
    public void addSetting(String name, String value) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_SETTING, name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#getSetting()
     */
    @Override
    public List<Setting> getSetting() throws Exception {
        return (List<Setting>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_LIST_SETTINGS);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#deleteSetting(List<String>)
     */
    @Override
    public void deleteSetting(List<String> deleteNames) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_SETTING, deleteNames);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#addTrustBundle(gov.hhs.fha.nhinc.direct.config.TrustBundle)
     */
    @Override
    public void addTrustBundle(TrustBundle tb) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_TRUST_BUNDLE, tb);
    }

    /**
     *
     * @param fetchAnchors
     * @return
     * @throws Exception
     */
    @Override
    public List<TrustBundle> getTrustBundles(boolean fetchAnchors) throws Exception {
        return (List<TrustBundle>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_GET_TRUST_BUNDLES, fetchAnchors);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#getTrustBundleByName(String)
     */
    @Override
    public TrustBundle getTrustBundleByName(String bundleName) throws Exception {
        return (TrustBundle) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_GET_TRUST_BUNDLE_BY_NAME, bundleName);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#getTrustBundleByDomain(long, boolean)
     */
    @Override
    public List<TrustBundleDomainReltn> getTrustBundlesByDomain(long domainId, boolean fetchAnchors) throws Exception {
        return (List<TrustBundleDomainReltn>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_GET_TRUST_BUNDLES_BY_DOMAIN, domainId, fetchAnchors);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#associateTrustBundleToDomain(long, long, boolean,
     * boolean)
     */
    @Override
    public void associateTrustBundleToDomain(long domainId, long trustBundleId, boolean incoming, boolean outgoing)
            throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ASSOCIATE_TRUST_BUNDLE_TO_DOMAIN,
                domainId, trustBundleId, incoming, outgoing);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#updateTrustBundleAttributes(long, String, String,
     * gov.hhs.fha.nhinc.direct.config.Certificate, int)
     */
    @Override
    public void updateTrustBundleAttributes(long trustBundleId, String trustBundleName, String trustBundleURL,
            Certificate signingCert, int trustBundleRefreshInterval) throws Exception {

        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_UPDATE_TRUST_BUNDLE_ATTRIBUTES,
                trustBundleId, trustBundleName, trustBundleURL, signingCert, trustBundleRefreshInterval);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#refreshTrustBundle(int)
     */
    @Override
    public void refreshTrustBundle(long id) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_REFRESH_TRUST_BUNDLE, id);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#disassociateTrustBundleFromDomain(long, long)
     */
    @Override
    public void disassociateTrustBundleFromDomain(long domainId, long trustBundleId) throws Exception {
        getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLE_FROM_DOMAIN, domainId, trustBundleId);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#disassociateTrustBundleFromDomains(long)
     */
    @Override
    public void disassociateTrustBundleFromDomains(long trustBundleId) throws Exception {
        getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLE_FROM_DOMAINS, trustBundleId);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#disassociateTrustBundlesFromDomain(long)
     */
    @Override
    public void disassociateTrustBundlesFromDomain(long domainId) throws Exception {
        getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLES_FROM_DOMAIN, domainId);
    }

    /**
     *
     * @param ids
     * @throws Exception
     */
    @Override
    public void deleteTrustBundles(List<Long> ids) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_TRUST_BUNDLE, ids);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.admingui.proxy.DirectConfigProxy#removeAddress(String)
     */
    @Override
    public void removeAddress(String addressEmail) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_REMOVE_ADDRESS, addressEmail);
    }

    private CONNECTClient<ConfigurationService> getClient() throws Exception {

        String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(DirectConfigConstants.DIRECT_CONFIG_SERVICE_NAME);

        ServicePortDescriptor<ConfigurationService> portDescriptor = new DirectConfigUnsecuredServicePortDescriptor();

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
                new AssertionType());
    }

    @Override
    public boolean pingDirectConfig(String url) throws Exception {
        List<Domain> domains = (List<Domain>) getClient().invokePort(ConfigurationService.class,
                DirectConfigConstants.DIRECT_CONFIG_LIST_DOMAINS, null, 0);
        return domains != null;
    }
}
