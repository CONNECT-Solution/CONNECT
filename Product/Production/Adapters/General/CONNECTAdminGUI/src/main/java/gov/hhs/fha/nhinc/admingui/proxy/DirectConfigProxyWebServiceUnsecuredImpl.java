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

import gov.hhs.fha.nhinc.admingui.proxy.service.DirectConfigUnsecuredServicePortDescriptor;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;

import java.util.List;

import org.nhind.config.common.AddAnchor;
import org.nhind.config.common.AddCertificates;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.AddTrustBundle;
import org.nhind.config.common.Anchor;
import org.nhind.config.common.AssociateTrustBundleToDomain;
import org.nhind.config.common.Certificate;
import org.nhind.config.common.ConfigurationService;
import org.nhind.config.common.DeleteTrustBundles;
import org.nhind.config.common.DisassociateTrustBundleFromDomains;
import org.nhind.config.common.Domain;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.GetTrustBundleByName;
import org.nhind.config.common.GetTrustBundles;
import org.nhind.config.common.GetTrustBundlesByDomain;
import org.nhind.config.common.ListCertificates;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.RemoveCertificates;
import org.nhind.config.common.Setting;
import org.nhind.config.common.TrustBundle;
import org.nhind.config.common.UpdateDomain;
import org.nhind.config.common.UpdateDomainResponse;
import org.nhind.config.common.UpdateTrustBundleAttributes;

import org.springframework.stereotype.Service;

/**
 * 
 * @author jasonasmith
 */
@SuppressWarnings("unchecked")
@Service
public class DirectConfigProxyWebServiceUnsecuredImpl implements DirectConfigProxy {

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private final Class<ConfigurationService> directConfigClazz = ConfigurationService.class;

    @Override
    public Domain getDomain(Long id) throws Exception {
        return (Domain) getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_GET_DOMAIN, id);
    }

    @Override
    public void addDomain(AddDomain domain) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_DOMAIN, domain);
    }

    @Override
    public List<Domain> listDomains() throws Exception {
        return (List<Domain>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_LIST_DOMAINS, null, 0);
    }

    @Override
    public UpdateDomainResponse updateDomain(UpdateDomain updateDomain) throws Exception {
        return (UpdateDomainResponse) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_UPDATE_DOMAIN, updateDomain);
    }

    @Override
    public void deleteDomain(String name) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_DOMAIN, name);
    }

    @Override
    public void addAnchor(AddAnchor addAnchor) throws Exception {
        getClient()
            .invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_ANCHOR, addAnchor.getAnchor());
    }

    @Override
    public void removeAnchors(RemoveAnchors removeAnchors) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_ANCHOR,
            removeAnchors.getAnchorId());
    }

    @Override
    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner getAnchorsForOwner) throws Exception {
        return (List<Anchor>) getClient().invokePort(directConfigClazz,
            DirectConfigConstants.DIRECT_CONFIG_GET_ANCHORS_FOR_OWNER, getAnchorsForOwner.getOwner(),
            getAnchorsForOwner.getOptions());
    }

    @Override
    public void addCertificates(AddCertificates certificate) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_CERT, certificate.getCerts());

    }

    @Override
    public void removeCertificate(RemoveCertificates certificate) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_CERT,
                certificate.getCertificateIds());

    }

    @Override
    public List<Certificate> listCertificates(ListCertificates listCert) throws Exception {
        return (List<Certificate>) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_LIST_CERTS, listCert.getLastCertificateId(),
                listCert.getMaxResutls(), listCert.getOptions());
    }

    @Override
    public void addSetting(String name, String value) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_SETTING, name, value);
    }

    @Override
    public List<Setting> getSetting() throws Exception {
        return (List<Setting>) getClient().invokePort(directConfigClazz,
            DirectConfigConstants.DIRECT_CONFIG_LIST_SETTINGS);
    }

    @Override
    public void deleteSetting(List<String> deleteNames) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_SETTING, deleteNames);
    }

    @Override
    public void addTrustBundle(AddTrustBundle tb) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ADD_TRUST_BUNDLE, tb.getBundle());
    }

    @Override
    public List<TrustBundle> getTrustBundles(GetTrustBundles gtb) throws Exception {
        return (List<TrustBundle>) getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_GET_TRUST_BUNDLE, gtb.isFetchAnchors());
    }

    @Override
    public TrustBundle getTrustBundleByName(GetTrustBundleByName getTrustBundleByName) throws Exception {
        return (TrustBundle) getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_GET_TRUST_BUNDLE_BY_NAME, getTrustBundleByName.getBundleName());
    }

    @Override
    public TrustBundle getTrustBundlesByDomain(GetTrustBundlesByDomain getTrustBundlesByDomain) throws Exception {
        return (TrustBundle) getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ASSOCIATE_TRUST_BUNDLE_TO_DOMAIN, getTrustBundlesByDomain.getDomainId(), getTrustBundlesByDomain.isFetchAnchors());
    }
    
    @Override
    public void associateTrustBundleToDomain(AssociateTrustBundleToDomain associateTrustBundleToDomain) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_ASSOCIATE_TRUST_BUNDLE_TO_DOMAIN, associateTrustBundleToDomain.getDomainId(), associateTrustBundleToDomain.getTrustBundleId(), associateTrustBundleToDomain.isIncoming(), associateTrustBundleToDomain.isOutgoing());
    }

    @Override
    public void updateTrustBundleAttributes(UpdateTrustBundleAttributes tb) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_UPDATE_TRUST_BUNDLE_ATTRIBUTES, tb.getTrustBundleId(), tb.getTrustBundleName(), tb.getTrustBundleURL(), tb.getSigningCert(), tb.getTrustBundleRefreshInterval());
    }

    @Override
    public void refreshTrustBundle(int id) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_REFRESH_TRUST_BUNDLE, id);
    }

    @Override
    public void disassociateTrustBundleFromDomains(DisassociateTrustBundleFromDomains disassociateTrustBundleFromDomains) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DISASSOCIATE_TRUST_BUNDLE_FROM_DOMAIN, disassociateTrustBundleFromDomains.getTrustBundleId());
    }

    @Override
    public void deleteTrustBundle(DeleteTrustBundles tb) throws Exception {
        getClient().invokePort(directConfigClazz, DirectConfigConstants.DIRECT_CONFIG_DELETE_TRUST_BUNDLE, tb.getTrustBundleIds());
    }

    private CONNECTClient<ConfigurationService> getClient() throws Exception {

        String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(DirectConfigConstants.DIRECT_CONFIG_SERVICE_NAME);

        ServicePortDescriptor<ConfigurationService> portDescriptor = new DirectConfigUnsecuredServicePortDescriptor();

        CONNECTClient<ConfigurationService> client = CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(
                portDescriptor, url, null);

        return client;
    }
}
