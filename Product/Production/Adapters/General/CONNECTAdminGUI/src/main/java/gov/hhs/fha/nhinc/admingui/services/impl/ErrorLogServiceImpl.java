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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_DASHBOARD_ERRORLOG_GETFILTERS;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_DASHBOARD_ERRORLOG_LIST;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_DASHBOARD_ERRORLOG_VIEW;
import static gov.hhs.fha.nhinc.admingui.constant.AdminWSConstants.ADMIN_GUI_MANAGEMENT_SERVICE_NAME;
import static gov.hhs.fha.nhinc.admingui.managed.ErrorLogBean.KEY_EXCEPTIONS;
import static gov.hhs.fha.nhinc.admingui.managed.ErrorLogBean.KEY_SERVICES;
import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.buildConfigAssertion;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getXMLGregorianCalendarFrom;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.returnSort;

import gov.hhs.fha.nhinc.admingui.services.ErrorLogService;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.adminguimanagement.AdminGUIManagementPortType;
import gov.hhs.fha.nhinc.common.adminguimanagement.GetSearchFilterRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.ListErrorLogRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.LogEventSimpleResponseMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.LogEventType;
import gov.hhs.fha.nhinc.common.adminguimanagement.ViewErrorLogRequestMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.configuration.AdminGUIManagementPortDescriptor;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */

public class ErrorLogServiceImpl implements ErrorLogService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateManagerServiceImpl.class);
    private static final WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
    private static final String SERVICE_ERROR = "There an error while calling webservice.";
    private static CONNECTClient<AdminGUIManagementPortType> client = null;

    @Override
    public List<LogEventType> search(String service, String exception, Date fromDate, Date toDate) {
        ListErrorLogRequestMessageType request = new ListErrorLogRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setService(service);
        request.setException(exception);
        request.setFromDate(getXMLGregorianCalendarFrom(fromDate));
        request.setToDate(getXMLGregorianCalendarFrom(toDate));

        try {
            LogEventSimpleResponseMessageType response = (LogEventSimpleResponseMessageType) invokeClientPort(
                ADMIN_DASHBOARD_ERRORLOG_LIST, request);
            logDebug(ADMIN_DASHBOARD_ERRORLOG_LIST, response.getEventLogList().size());
            return response.getEventLogList();
        } catch (Exception ex) {
            LOG.error("Error calling service-search: {}", ex.getMessage(), ex);
            HelperUtil.addMessageError(null, SERVICE_ERROR);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, List<String>> getDropdowns() {
        GetSearchFilterRequestMessageType request = new GetSearchFilterRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        Map<String, List<String>> retObj = new HashMap<>();

        try {
            LogEventSimpleResponseMessageType response = (LogEventSimpleResponseMessageType) invokeClientPort(
                ADMIN_DASHBOARD_ERRORLOG_GETFILTERS, request);
            logDebug(ADMIN_DASHBOARD_ERRORLOG_GETFILTERS, response.getExceptionList().size(),
                response.getServiceList().size());
            retObj.put(KEY_EXCEPTIONS, returnSort(response.getExceptionList()));
            retObj.put(KEY_SERVICES, returnSort(response.getServiceList()));
        } catch (Exception ex) {
            LOG.error("Error while getting service-filters: {}", ex.getMessage(), ex);
            HelperUtil.addMessageError(null, SERVICE_ERROR);
        }
        return retObj;
    }

    @Override
    public LogEventType getLogEvent(Long id) {
        ViewErrorLogRequestMessageType request = new ViewErrorLogRequestMessageType();
        request.setConfigAssertion(buildConfigAssertion());
        request.setId(id);

        try {
            LogEventSimpleResponseMessageType response = (LogEventSimpleResponseMessageType) invokeClientPort(
                ADMIN_DASHBOARD_ERRORLOG_VIEW, request);
            logDebug(ADMIN_DASHBOARD_ERRORLOG_VIEW, response.getEventLogList().size());
            addErrorMsgFrom(response);
            return response.getEventLogList().get(0);
        } catch (Exception ex) {
            LOG.error("Error while calling service view-errorlog: {}", ex.getMessage(), ex);
            HelperUtil.addMessageError(null, SERVICE_ERROR);
        }
        return null;
    }

    private static CONNECTClient<AdminGUIManagementPortType> getClient() throws ExchangeManagerException {
        if (null == client) {
            String url = proxyHelper.getAdapterEndPointFromConnectionManager(ADMIN_GUI_MANAGEMENT_SERVICE_NAME);
            ServicePortDescriptor<AdminGUIManagementPortType> portDescriptor = new AdminGUIManagementPortDescriptor();
            client = CONNECTClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url,
                new AssertionType());
        }
        return client;
    }

    private static <T> Object invokeClientPort(String serviceMethod, T request) throws Exception {
        return getClient().invokePort(AdminGUIManagementPortType.class, serviceMethod, request);
    }

    private static void logDebug(String msg, Object... objects) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("{}: {}", msg, StringUtils.join(objects, ", "));
        }
    }

    private static void addErrorMsgFrom(LogEventSimpleResponseMessageType response) {
        if (BooleanUtils.isFalse(response.isStatus()) && StringUtils.isNotBlank(response.getMessage())) {
            HelperUtil.addMessageError(null, response.getMessage());
        }
    }
}
