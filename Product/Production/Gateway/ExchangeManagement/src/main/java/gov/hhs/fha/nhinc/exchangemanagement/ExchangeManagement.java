/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.InternalExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.util.ExchangeManagerUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private static final AssigningAuthorityHomeCommunityMappingDAO mappingDao
        = new AssigningAuthorityHomeCommunityMappingDAO();

    @Override
    public SimpleExchangeManagementResponseMessageType deleteExchange(DeleteExchangeRequestMessageType request) {
        LOG.trace("deleteExchange--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String exchangeName = request.getExchangeName();

        if (StringUtils.isBlank(exchangeName)) {
            response.setMessage("ExchangeName is required");
            return response;
        }

        if (getExchangeManager().isRefreshLocked()) {
            response.setMessage(ACT_BUSY);
            return response;
        }

        try {
            response.setStatus(getExchangeManager().deleteExchange(exchangeName));
        } catch (ExchangeManagerException e) {
            response.setMessage(ACT_FAIL);
            LOG.error("error during delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType saveExchange(SaveExchangeRequestMessageType request) {
        LOG.trace("saveExchange--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();

        if (getExchangeManager().isRefreshLocked()) {
            response.setMessage(ACT_BUSY);
            return response;
        }

        try {
            response.setStatus(getExchangeManager().saveExchange(request.getExchange()));
        } catch (ExchangeManagerException e) {
            response.setMessage(ACT_FAIL);
            LOG.error("error during save-exchange: {}", e.getLocalizedMessage(), e);
        }

        return response;
    }

    @Override
    public RefreshExchangeManagerResponseMessageType refreshExchangeManager(
        RefreshExchangeManagerRequestMessageType request) {
        LOG.trace("refreshExchangeManager--call");
        RefreshExchangeManagerResponseMessageType response = new RefreshExchangeManagerResponseMessageType();

        if (getExchangeManager().isRefreshLocked()) {
            LOG.info("exchange-manager is busy: {}", ACT_BUSY);
        } else {
            response.getExchangeDownloadStatusList().addAll(ExchangeManagerUtil.forceExchangesRefresh());
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizations(ListOrganizationsRequestMessageType request) {
        LOG.trace("listOrganizations--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String exchangeName = request.getExchangeName();

        if (StringUtils.isNotBlank(exchangeName)) {
            response.getOrganizationList().addAll(getExchangeManager().getAllOrganizationsBy(exchangeName));
        } else {
            try {
                response.getOrganizationList().addAll(getExchangeManager().getAllOrganizations());
            } catch (ExchangeManagerException ex) {
                LOG.error("ListOrganizations--encounter error: {}", ex.getLocalizedMessage(), ex);
                response.setMessage(ACT_FAIL);
            }
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getOrganizationByHCID(
        GetOrganizationByHCIDRequestMessageType request) {
        LOG.trace("getOrganizationByHCID--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String hcid = request.getHcid();

        if (StringUtils.isBlank(hcid)) {
            response.setMessage("HCID is required.");
            return response;
        }

        try {
            response.getOrganizationList().add(getExchangeManager().getOrganization(hcid));
        } catch (ExchangeManagerException ex) {
            LOG.error("getOrganizationByHCID encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }

        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationByHCIDList(
        ListOrganizationsByHCIDListRequestMessageType request) {
        LOG.trace("listOrganizationByHCIDList--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        Set<String> hcidSet = new HashSet<>();
        hcidSet.addAll(request.getHcidList());

        if (CollectionUtils.isEmpty(hcidSet)) {
            response.setMessage("HCID is required.");
            return response;
        }

        try {
            for (String hcid : hcidSet) {
                if (StringUtils.isNotBlank(hcid)) {
                    response.getOrganizationList().add(getExchangeManager().getOrganization(hcid));
                }
            }
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationByHCIDList encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }

        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getOrganizationByHCIDServiceName(
        GetOrganizationByHCIDServiceNameRequestMessageType request) {
        LOG.trace("getOrganizationByHCIDServiceName--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String hcid = request.getHcid();
        String serviceName = request.getServiceName();

        if (StringUtils.isBlank(hcid) || StringUtils.isBlank(serviceName)) {
            response.setMessage("HCID and ServiceName are required");
            return response;
        }

        try {
            response.getOrganizationList().add(getExchangeManager().getOrganizationByServiceName(hcid, serviceName));
        } catch (ExchangeManagerException ex) {
            LOG.error("getOrganizationByHCIDServiceName encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationsByHCIDServiceName(
        ListOrganizationsByHCIDServiceNameRequestMessageType request) {
        LOG.trace("listOrganizationsByHCIDServiceName--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        List<String> hcidList = getUniqueList(request.getHcidList());
        String serviceName = request.getServiceName();

        if (CollectionUtils.isEmpty(hcidList) || StringUtils.isBlank(serviceName)) {
            response.setMessage("HCID and ServiceName are required");
            return response;
        }

        try {
            response.getOrganizationList()
                .addAll(getExchangeManager().getOrganizationSetByServiceNameForHCID(hcidList, serviceName));
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationsByHCIDServiceName encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType listOrganizationsByServiceName(
        ListOrganizationsByServiceNameRequestMessageType request) {
        LOG.trace("listOrganizationsByServiceName--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String serviceName = request.getServiceName();

        if (StringUtils.isBlank(serviceName)) {
            response.setMessage("ServiceName is required");
            return response;
        }

        try {
            response.getOrganizationList().addAll(getExchangeManager().getAllOrganizationSetByServiceName(serviceName,
                null));
        } catch (ExchangeManagerException ex) {
            LOG.error("listOrganizationsByServiceName encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlDefaultByServiceName(
        GetEndpointUrlDefaultByServiceNameRequestMessageType request) {
        LOG.trace("getEndpointUrlByServiceName--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String serviceName = request.getServiceName();
        String hcid = request.getHcid();
        String exchangeName = request.getExchangeName();

        if (StringUtils.isBlank(hcid) || StringUtils.isBlank(serviceName)) {
            response.setMessage("HCID and ServiceName is required");
            return response;
        }

        if (StringUtils.isBlank(exchangeName)) {
            exchangeName = null;
        }

        try {
            response.setUrl(getExchangeManager().getDefaultEndpointURL(hcid, serviceName, exchangeName));
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlDefaultByServiceName encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlInternalByServiceName(
        GetEndpointUrlInternalByServiceNameRequestMessageType request) {
        LOG.trace("getEndpointUrlInternalByServiceName--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String serviceName = request.getServiceName();

        if (StringUtils.isBlank(serviceName)) {
            response.setMessage("ServiceName is required");
            return response;
        }

        try {
            response.setUrl(getInternalExchangeManager().getEndpointURL(serviceName));
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlInternalByServiceName encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlByNhinTarget(
        GetEndpointUrlByNhinTargetRequestMessageType request) {
        LOG.trace("getEndpointUrlByNhinTarget--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        NhinTargetSystemType targetSystem = request.getNhinTargetSystem();
        String serviceName = request.getServiceName();

        if (null == targetSystem || StringUtils.isBlank(serviceName)) {
            response.setMessage("NhinTargetSystem-element and ServiceName are required");
            return response;
        }

        try {
            response.setUrl(getExchangeManager().getEndpointURLFromNhinTarget(targetSystem, serviceName));
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlByNhinTarget encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
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
            return response;
        }

        try {
            response.getUrlInfoList().addAll(convertUrlInfoTypeList(
                getExchangeManager().getEndpointURLFromNhinTargetCommunities(targets, serviceName)));
        } catch (ExchangeManagerException ex) {
            LOG.error("listEndpointUrlInfoByNhinTargetCommunities encounter error: {}", ex.getLocalizedMessage(), ex);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getEndpointUrlByAdapter(
        GetEndpointUrlByAdapterRequestMessageType request) {
        LOG.trace("getEndpointUrlByAdapter--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        ADAPTER_API_LEVEL adapterLevel = ADAPTER_API_LEVEL.valueOf(request.getAdapterLevel());
        String serviceName = request.getServiceName();

        if (null == adapterLevel || StringUtils.isBlank(serviceName)) {
            response.setMessage("adapterLevel and ServiceName are required");
            return response;
        }

        try {
            response.setUrl(getInternalExchangeManager().getEndpointURL(serviceName, adapterLevel));
        } catch (ExchangeManagerException ex) {
            LOG.error("getEndpointUrlByAdapter encounter error: {}", ex.getLocalizedMessage(), ex);
            response.setMessage(ACT_FAIL);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getHomeCommunityIdByAssigningAuthorityId(
        GetHomeCommunityIdByAssigningAuthorityIdRequestMessageType request) {
        LOG.trace("getHomeCommunityIdByAssigningAuthorityId--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String aaid = request.getAaId();

        if (StringUtils.isBlank(aaid)) {
            response.setMessage("AssigningAuthorityId is required");
            return response;
        }

        response.setHcid(mappingDao.getHomeCommunityId(aaid));
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType getAssigningAuthoritiesByHCID(
        GetAssigningAuthoritiesByHCIDRequestMessageType request) {
        LOG.trace("getAssigningAuthoritiesByHCID--call");
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String hcid = request.getHcid();

        if (StringUtils.isBlank(hcid)) {
            response.setMessage("HomeCommunityId is required");
            return response;
        }

        response.getAaidList().addAll(getUniqueList(mappingDao.getAssigningAuthoritiesByHomeCommunity(hcid)));
        return response;
    }

    @Override
    public ListEndpointsResponseMessageType listEndpoints(ListEndpointsRequestMessageType request) {
        LOG.trace("listEndpoints--call");
        return null;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType saveExchangeConfig(
        SaveExchangeConfigRequestMessageType request) {
        LOG.trace("saveExchangeConfig--call");
        return null;
    }

    @Override
    public GetExchangeInfoViewResponseMessageType getExchangeInfoView(GetExchangeInfoViewRequestMessageType request) {
        LOG.info("getExchangeInfoView--call");
        return null;
    }

    @Override
    public ListExchangesResponseMessageType listExchanges(ListExchangesRequestMessageType request) {
        LOG.trace("listExchanges--call");
        return null;
    }

    private static SimpleExchangeManagementResponseMessageType newSimpleResponse() {
        SimpleExchangeManagementResponseMessageType retMsg = new SimpleExchangeManagementResponseMessageType();
        retMsg.setStatus(false);
        retMsg.setMessage(ACT_SUCCESSFUL);
        return retMsg;
    }

    private static <T> List<T> getUniqueList(List<T> fromList) {
        Set<T> uniqueList = new HashSet<>();
        uniqueList.addAll(fromList);
        List<T> retList = new ArrayList<>();
        retList.addAll(uniqueList);
        return retList;
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

}
