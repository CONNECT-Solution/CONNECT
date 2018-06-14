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

import gov.hhs.fha.nhinc.common.exchangemanagement.DeleteExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.GetExchangeInfoViewResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListEndpointsResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListExchangesResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ListOrganizationsResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.RefreshExchangeManagerResponseMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeConfigRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SaveExchangeRequestMessageType;
import gov.hhs.fha.nhinc.common.exchangemanagement.SimpleExchangeManagementResponseMessageType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.exchangemgr.util.ExchangeManagerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ttang
 */
public class ExchangeManagement implements EntityExchangeManagementPortType {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeManagement.class);
    private final ExchangeManager exchangeManager = ExchangeManager.getInstance();
    private static final String ACT_SUCCESSFUL = "successful";
    private static final String ACT_FAIL = "fail";
    private static final String ACT_BUSY = "refresh locked";

    @Override
    public SimpleExchangeManagementResponseMessageType deleteExchange(DeleteExchangeRequestMessageType arg0) {
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();
        String exchangeName = arg0.getExchangeName();

        if (StringUtils.isBlank(exchangeName)) {
            response.setMessage("exchange-name cannot be blank");
            return response;
        }

        if (exchangeManager.isRefreshLocked()) {
            response.setMessage(ACT_BUSY);
            return response;
        }

        try {
            response.setStatus(exchangeManager.deleteExchange(exchangeName));
            response.setMessage(ACT_SUCCESSFUL);
        } catch (ExchangeManagerException e) {
            response.setMessage(ACT_FAIL);
            LOG.error("error during delete-exchange: {}", e.getLocalizedMessage(), e);
        }
        return response;
    }

    @Override
    public SimpleExchangeManagementResponseMessageType saveExchange(SaveExchangeRequestMessageType arg0) {
        SimpleExchangeManagementResponseMessageType response = newSimpleResponse();

        if (exchangeManager.isRefreshLocked()) {
            response.setMessage(ACT_BUSY);
            return response;
        }

        try {
            response.setStatus(exchangeManager.saveExchange(arg0.getExchange()));
            response.setMessage(ACT_SUCCESSFUL);
        } catch (ExchangeManagerException e) {
            response.setMessage(ACT_FAIL);
            LOG.error("error during save-exchange: {}", e.getLocalizedMessage(), e);
        }

        return response;
    }

    @Override
    public RefreshExchangeManagerResponseMessageType refreshExchangeManager(
        RefreshExchangeManagerRequestMessageType arg0) {
        RefreshExchangeManagerResponseMessageType response = new RefreshExchangeManagerResponseMessageType();

        if (exchangeManager.isRefreshLocked()) {
            LOG.info("exchange-manager is busy: {}", ACT_BUSY);
        } else {
            response.getExchangeDownloadStatusList().addAll(ExchangeManagerUtil.forceExchangesRefresh());
        }
        return response;
    }

    @Override
    public GetExchangeInfoViewResponseMessageType getExchangeInfoView(GetExchangeInfoViewRequestMessageType arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListEndpointsResponseMessageType listEndpoints(ListEndpointsRequestMessageType arg0) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ListExchangesResponseMessageType listExchanges(ListExchangesRequestMessageType arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListOrganizationsResponseMessageType listOrganizations(ListOrganizationsRequestMessageType arg0) {
        // TODO Auto-generated method stub
        return null;
    }





    @Override
    public SimpleExchangeManagementResponseMessageType saveExchangeConfig(SaveExchangeConfigRequestMessageType arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    private SimpleExchangeManagementResponseMessageType newSimpleResponse(){
        SimpleExchangeManagementResponseMessageType retMsg = new SimpleExchangeManagementResponseMessageType();
        retMsg.setStatus(false);
        return retMsg;
    }

}
