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
import org.nhind.config.common.AddAnchorResponse;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.ConfigurationService;
import org.nhind.config.common.Domain;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.GetAnchorsForOwnerResponse;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.RemoveAnchorsResponse;
import org.nhind.config.common.UpdateDomain;
import org.nhind.config.common.UpdateDomainResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */
@Service
public class DirectConfigProxyWebServiceUnsecuredImpl implements DirectConfigProxy {

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private final Class directConfigClazz = ConfigurationService.class;

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
    public AddAnchorResponse addAnchor(AddAnchor anchor) throws Exception {
        return (AddAnchorResponse) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_ADD_ANCHOR, anchor);
    }

    @Override
    public RemoveAnchorsResponse removeAnchors(RemoveAnchors anchors) throws Exception {
        return (RemoveAnchorsResponse) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_DELETE_ANCHOR, anchors);
    }

    @Override
    public GetAnchorsForOwnerResponse getAnchorsForOwner(GetAnchorsForOwner anchors) throws Exception {
        return (GetAnchorsForOwnerResponse) getClient().invokePort(directConfigClazz,
                DirectConfigConstants.DIRECT_CONFIG_GET_ANCHORS_FOR_OWNER, anchors);
    }

    private CONNECTClient getClient() throws Exception {

        String url = oProxyHelper
                .getAdapterEndPointFromConnectionManager(DirectConfigConstants.DIRECT_CONFIG_SERVICE_NAME);

        ServicePortDescriptor<ConfigurationService> portDescriptor = new DirectConfigUnsecuredServicePortDescriptor();

        CONNECTClient<ConfigurationService> client = CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(
                portDescriptor, url, null);

        return client;
    }
}
