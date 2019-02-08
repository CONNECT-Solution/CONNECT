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
package gov.hhs.fha.nhinc.exchangemanagement;

import static gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper.copyExchangeTypeList;
import static java.lang.Boolean.FALSE;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.exchangemanagement.DeleteExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetAssigningAuthoritiesByHCIDRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetEndpointUrlByAdapterRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetEndpointUrlByNhinTargetRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetEndpointUrlDefaultByServiceNameRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetEndpointUrlInternalByServiceNameRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetHomeCommunityIdByAssigningAuthorityIdRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetOrganizationByHCIDRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetOrganizationByHCIDServiceNameRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointUrlInfoByNhinTargetCommunitiesRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointUrlInfoByNhinTargetCommunitiesResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsByHCIDListRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsByHCIDServiceNameRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsByServiceNameRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeConfigRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SimpleExchangeManagementResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.util.ExchangeManagerUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ttang
 */
public class ExchangeManagement implements EntityExchangeManagementPortType {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagement.class);
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ACT_FAIL = "fail";
    private static final String ACT_BUSY = "refresh locked";
    private static final String ACT_HCID_SERVICENAME_MISSING = "HCID and ServiceName are required";

    private static final AssigningAuthorityHomeCommunityMappingDAO mappingDao
    = new AssigningAuthorityHomeCommunityMappingDAO();

    @Override
    public SimpleExchangeManagementResponseMessageType deleteExchange(DeleteExchangeRequestMessageType request) {
        LOG.trace("deleteExchange--call");
        String exchangeName = request.getExchangeName();

        if (StringUtils.isBlank(exchangeName)) {
            return buildSimpleResponse(Boolean.FALSE, "ExchangeName is required");
        }

        if (getExchangeManager().isRefreshLocked()) {
            return buildSimpleResponse(Boolean.FALSE, ACT_BUSY);
        }

        try {
            return buildSimpleResponse(getExchangeManager().deleteExchange(exchangeName), ACT_SUCCESSFUL);
        } catch (ExchangeManagerException e) {
            LOG.error("error during delete exchange: {}", e.getLocalizedMessage(), e);
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
    }

    @Override
    public SimpleExchangeManagementResponseMessageType saveExchange(SaveExchangeRequestMessageType request) {
        LOG.trace("saveExchange--call");

        if (getExchangeManager().isRefreshLocked()) {
            return buildSimpleResponse(Boolean.FALSE, ACT_BUSY);
        }

        try {
            return buildSimpleResponse(getExchangeManager().saveExchange(request.getExchange(), request.getOriginalExchangeName()), ACT_SUCCESSFUL);
        } catch (ExchangeManagerException e) {
            LOG.error("error during save exchange: {}", e.getLocalizedMessage(), e);
            return buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
    }

    @Override
    public RefreshExchangeManagerResponseMessageType refreshExchangeManager(
        RefreshExchangeManagerRequestMessageType request) {
        LOG.trace("refreshExchangeManager--call");
        RefreshExchangeManagerResponseMessageType response = new RefreshExchangeManagerResponseMessageType();

        if (getExchangeManager().isRefreshLocked()) {
            LOG.info("exchange manager is busy: {}", ACT_BUSY);
        } else {
            response.getExchangeDownloadStatusList().addAll(ExchangeManagerUtil.forceExchangesRefresh());
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizations(ListOrganizationsRequestMessageType request) {
        LOG.trace("listOrganizations--call");
        SimpleExchangeManagementResponseMessageType response;
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().getAllOrganizations(
                exchangeName));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("ListOrganizations encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getOrganizationByHCID(
        GetOrganizationByHCIDRequestMessageType request) {
        LOG.trace("getOrganizationByHCID--call");
        SimpleExchangeManagementResponseMessageType response;
        String hcid = request.getHcid();
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();
        if (StringUtils.isBlank(hcid)) {
            return buildSimpleResponse(Boolean.FALSE, "HCID is required.");
        }

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().getOrganization(exchangeName,
                hcid));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("getOrganizationByHCID encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }

        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationByHCIDList(
        ListOrganizationsByHCIDListRequestMessageType request) {
        LOG.trace("listOrganizationByHCIDList--call");
        SimpleExchangeManagementResponseMessageType response;
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();
        if (CollectionUtils.isEmpty(request.getHcidList())) {
            return buildSimpleResponse(Boolean.FALSE, "HCID is required.");
        }

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().getOrganizationSet(
                request.getHcidList(), exchangeName));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationByHCIDList encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getOrganizationByHCIDServiceName(
        GetOrganizationByHCIDServiceNameRequestMessageType request) {
        LOG.trace("getOrganizationByHCIDServiceName--call");
        SimpleExchangeManagementResponseMessageType response;
        String hcid = request.getHcid();
        String serviceName = request.getServiceName();
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();
        if (StringUtils.isBlank(hcid) || StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, ACT_HCID_SERVICENAME_MISSING);
        }

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().getOrganizationByServiceName(
                hcid, serviceName, exchangeName));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("getOrganizationByHCIDServiceName encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationsByHCIDServiceName(
        ListOrganizationsByHCIDServiceNameRequestMessageType request) {
        LOG.trace("listOrganizationsByHCIDServiceName--call");
        SimpleExchangeManagementResponseMessageType response;
        List<String> hcidList = CoreHelpUtils.getUniqueList(request.getHcidList());
        String serviceName = request.getServiceName();
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();
        if (CollectionUtils.isEmpty(hcidList) || StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, ACT_HCID_SERVICENAME_MISSING);
        }

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().
                getOrganizationSetByServiceNameForHCID(hcidList, serviceName, exchangeName));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationsByHCIDServiceName encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationsByServiceName(
        ListOrganizationsByServiceNameRequestMessageType request) {
        LOG.trace("listOrganizationsByServiceName--call");
        SimpleExchangeManagementResponseMessageType response;
        String serviceName = request.getServiceName();
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();
        if (StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, "ServiceName is required");
        }

        try {
            OrganizationListType orglist = buildOrganizationListType(getExchangeManager().
                getAllOrganizationSetByServiceName(serviceName, exchangeName));
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setOrganizationList(orglist);
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationsByServiceName encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlDefaultByServiceName(
        GetEndpointUrlDefaultByServiceNameRequestMessageType request) {
        LOG.trace("getEndpointUrlByServiceName--call");
        SimpleExchangeManagementResponseMessageType response;
        String serviceName = request.getServiceName();
        String hcid = request.getHcid();
        String exchangeName = StringUtils.isBlank(request.getExchangeName()) ? null : request.getExchangeName().trim();

        if (StringUtils.isBlank(hcid) || StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, ACT_HCID_SERVICENAME_MISSING);
        }

        try {
            String url = getExchangeManager().getDefaultEndpointURL(hcid, serviceName, exchangeName);
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setUrl(url);
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlDefaultByServiceName encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlInternalByServiceName(
        GetEndpointUrlInternalByServiceNameRequestMessageType request) {
        LOG.trace("getEndpointUrlInternalByServiceName--call");
        SimpleExchangeManagementResponseMessageType response;
        String serviceName = request.getServiceName();

        if (StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, "ServiceName is required");
        }

        try {
            String url = getInternalExchangeManager().getEndpointURL(serviceName);
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setUrl(url);
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlInternalByServiceName encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlByNhinTarget(
        GetEndpointUrlByNhinTargetRequestMessageType request) {
        LOG.trace("getEndpointUrlByNhinTarget--call");
        SimpleExchangeManagementResponseMessageType response;
        NhinTargetSystemType targetSystem = request.getNhinTargetSystem();
        String serviceName = request.getServiceName();

        if (null == targetSystem || StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, "NhinTargetSystem element and ServiceName are required");
        }

        try {
            String url = getExchangeManager().getEndpointURLFromNhinTarget(targetSystem, serviceName);
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setUrl(url);
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlByNhinTarget encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public ListEndpointUrlInfoByNhinTargetCommunitiesResponseMessageType listEndpointUrlInfoByNhinTargetCommunities(
        ListEndpointUrlInfoByNhinTargetCommunitiesRequestMessageType request) {
        LOG.trace("listEndpointUrlInfoByNhinTargetCommunities--call");
        ListEndpointUrlInfoByNhinTargetCommunitiesResponseMessageType response
        = new ListEndpointUrlInfoByNhinTargetCommunitiesResponseMessageType();
        NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
        String serviceName = request.getServiceName();

        if (null == targets || StringUtils.isBlank(serviceName)) {
            response.setMessage("NhinTargetCommunities and ServiceName are required.");
            return response;
        }

        try {
            response.getUrlInfoList().addAll(convertUrlInfoTypeList(
                getExchangeManager().getEndpointURLFromNhinTargetCommunities(targets, serviceName)));
        } catch (ExchangeManagerException ex) {
            LOG.error("listEndpointUrlInfoByNhinTargetCommunities encountered error: {}", ex.getLocalizedMessage(), ex);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlByAdapter(
        GetEndpointUrlByAdapterRequestMessageType request) {
        LOG.trace("getEndpointUrlByAdapter--call");
        SimpleExchangeManagementResponseMessageType response;
        ADAPTER_API_LEVEL adapterLevel = ADAPTER_API_LEVEL.valueOf(request.getAdapterLevel());
        String serviceName = request.getServiceName();

        if (null == adapterLevel || StringUtils.isBlank(serviceName)) {
            return buildSimpleResponse(Boolean.FALSE, "AdapterLevel and ServiceName are required");
        }

        try {
            String url = getInternalExchangeManager().getEndpointURL(serviceName, adapterLevel);
            response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
            response.setUrl(url);
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlByAdapter encountered error: {}", ex.getLocalizedMessage(), ex);
            response = buildSimpleResponse(Boolean.FALSE, ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getHomeCommunityIdByAssigningAuthorityId(
        GetHomeCommunityIdByAssigningAuthorityIdRequestMessageType request) {
        LOG.trace("getHomeCommunityIdByAssigningAuthorityId--call");
        SimpleExchangeManagementResponseMessageType response;
        String aaid = request.getAaId();

        if (StringUtils.isBlank(aaid)) {
            return buildSimpleResponse(Boolean.FALSE, "AssigningAuthorityId is required");
        }
        response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        response.setHcid(mappingDao.getHomeCommunityId(aaid));
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getAssigningAuthoritiesByHCID(
        GetAssigningAuthoritiesByHCIDRequestMessageType request) {
        LOG.trace("getAssigningAuthoritiesByHCID--call");
        SimpleExchangeManagementResponseMessageType response;
        String hcid = request.getHcid();

        if (StringUtils.isBlank(hcid)) {
            return buildSimpleResponse(Boolean.FALSE, "HomeCommunityId is required");
        }
        response = buildSimpleResponse(Boolean.TRUE, ACT_SUCCESSFUL);
        response.getAaidList()
            .addAll(CoreHelpUtils.getUniqueList(mappingDao.getAssigningAuthoritiesByHomeCommunity(hcid)));
        return response;
    }

    @Override
    public ListEndpointsResponseMessageType listEndpoints(ListEndpointsRequestMessageType request) {
        LOG.trace("listEndpoints--call");
        String hcid = request.getHcid();
        String exchangeName = request.getExchangeName();
        ListEndpointsResponseMessageType response = new ListEndpointsResponseMessageType();

        if (StringUtils.isBlank(hcid) || StringUtils.isBlank(exchangeName)) {
            response.setMessage("HCID and Exchange namme are required.");
            return response;
        }

        response.getEndpointsList().addAll(getExchangeManager().getAllEndpointTypesBy(exchangeName, hcid));
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType saveExchangeConfig(
        SaveExchangeConfigRequestMessageType request) {
        LOG.trace("saveExchangeConfig--call");
        try{
            Boolean updateStatus = Boolean.valueOf(getExchangeManager().updateExchangeInfo(request.getRefreshInterval(),
                request.getMaxNumberOfBackups(), request.getDefaultExchange()));
            return buildSimpleResponse(updateStatus, ACT_SUCCESSFUL);
        } catch (ExchangeManagerException ex) {
            LOG.error("error while updating exchange config: {}", ex.getMessage(), ex);
            return buildSimpleResponse(FALSE, ACT_FAIL);
        }
    }

    @Override
    public GetExchangeInfoViewResponseMessageType getExchangeInfoView(GetExchangeInfoViewRequestMessageType request) {
        LOG.trace("getExchangeInfoView--call");
        GetExchangeInfoViewResponseMessageType response = new GetExchangeInfoViewResponseMessageType();
        response.setExchangeInfo(getExchangeManager().getExchangeInfoView());
        return response;
    }

    @Override
    public ListExchangesResponseMessageType listExchanges(ListExchangesRequestMessageType request) {
        LOG.trace("listExchanges--call");
        ListExchangesResponseMessageType response = new ListExchangesResponseMessageType();
        response.getExchangesList().addAll(copyExchangeTypeList(getExchangeManager().getAllExchanges()));
        return response;
    }

    private static SimpleExchangeManagementResponseMessageType buildSimpleResponse(Boolean status, String message) {
        SimpleExchangeManagementResponseMessageType retMsg = new SimpleExchangeManagementResponseMessageType();
        retMsg.setStatus(status);
        retMsg.setMessage(message);
        return retMsg;
    }

    private static InternalExchangeManager getInternalExchangeManager() {
        return InternalExchangeManager.getInstance();
    }

    private static ExchangeManager getExchangeManager() {
        return ExchangeManager.getInstance();
    }

    private static UrlInfoType convertUrlInfoType(UrlInfo urlInfo) {
        if (null == urlInfo) {
            return null;
        }
        UrlInfoType urlInfoType = new UrlInfoType();
        urlInfoType.setId(urlInfo.getHcid());
        urlInfoType.setUrl(urlInfo.getUrl());
        return urlInfoType;
    }

    private static List<UrlInfoType> convertUrlInfoTypeList(List<UrlInfo> fromList) {
        List<UrlInfoType> retList = new ArrayList<>();
        for (UrlInfo urlInfo : fromList) {
            retList.add(convertUrlInfoType(urlInfo));
        }
        return retList;
    }

    private static OrganizationListType buildOrganizationListType(Collection<OrganizationType> orgs) {
        OrganizationListType orgListType = null;
        if (CollectionUtils.isNotEmpty(orgs)) {
            orgListType = new OrganizationListType();
            orgListType.getOrganization().addAll(orgs);
        }
        return orgListType;
    }

    private static OrganizationListType buildOrganizationListType(OrganizationType org) {
        OrganizationListType orgListType = null;
        if (null != org) {
            orgListType = new OrganizationListType();
            orgListType.getOrganization().add(org);
        }
        return orgListType;
    }
}
