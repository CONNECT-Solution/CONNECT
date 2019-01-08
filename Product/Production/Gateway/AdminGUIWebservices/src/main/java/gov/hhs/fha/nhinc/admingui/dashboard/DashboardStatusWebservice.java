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
package gov.hhs.fha.nhinc.admingui.dashboard;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getDate;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getXMLGregorianCalendarFrom;

import gov.hhs.fha.nhinc.admingui.services.StatusEvent;
import gov.hhs.fha.nhinc.admingui.services.StatusService;
import gov.hhs.fha.nhinc.admingui.services.impl.StatusEventImpl;
import gov.hhs.fha.nhinc.adminguimanagement.AdminGUIManagementPortType;
import gov.hhs.fha.nhinc.common.adminguimanagement.AdminGUIRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.DashboardStatusMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.EventLogMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.GetSearchFilterRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.ListErrorLogRequestMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.LogEventSimpleResponseMessageType;
import gov.hhs.fha.nhinc.common.adminguimanagement.LogEventType;
import gov.hhs.fha.nhinc.common.adminguimanagement.ViewErrorLogRequestMessageType;
import gov.hhs.fha.nhinc.event.dao.DatabaseEventLoggerDao;
import gov.hhs.fha.nhinc.event.model.EventCount;
import gov.hhs.fha.nhinc.event.model.EventDTO;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EVENT_LOGGING_SERVICE_NAME;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;


public class DashboardStatusWebservice implements AdminGUIManagementPortType{

    @Autowired
    StatusService service;

    private final StatusEvent eventService = new StatusEventImpl();

    @Override
    public DashboardStatusMessageType dashboardStatus(AdminGUIRequestMessageType request) {
        request.isIncludeEventMessages();
        DashboardStatusMessageType resp = new DashboardStatusMessageType();
        resp.setMemory(service.getMemory());
        resp.setOS(service.getOperatingSystem());
        resp.setServer(service.getApplicationServer());
        resp.setVersion(service.getJavaVersion());

        eventService.setCounts();

        for (Entry<String, EventCount> event : eventService.getEventCounts().entrySet())
        {
            EventLogMessageType eventType = new EventLogMessageType();
            eventType.setEvent(event.getKey());
            eventType.setInbound(event.getValue().getInbound());
            eventType.setOutbound(event.getValue().getOutbound());

            resp.getEvent().add(eventType);
        }

        return resp;
    }

    @Override
    public LogEventSimpleResponseMessageType getSearchFilter(GetSearchFilterRequestMessageType request) {
        LogEventSimpleResponseMessageType response = new LogEventSimpleResponseMessageType();
        response.getServiceList().addAll(getDropdownServices());
        response.getExceptionList().addAll(getEventDAO().getExceptions());
        return response;
    }

    @Override
    public LogEventSimpleResponseMessageType listErrorLog(ListErrorLogRequestMessageType request) {
        LogEventSimpleResponseMessageType response = new LogEventSimpleResponseMessageType();
        response.getEventLogList().addAll(getLogEventList(getEventDAO().getAllFailureMessages(request.getService(),
            request.getException(), getDate(request.getFromDate()), getDate(request.getToDate()))));
        return response;
    }

    @Override
    public LogEventSimpleResponseMessageType viewErrorLog(ViewErrorLogRequestMessageType request) {
        if (!isId(request.getId())) {
            return buildSimpleResponse(false, "Id is required.");
        }

        LogEventSimpleResponseMessageType response = new LogEventSimpleResponseMessageType();
        response.getEventLogList().add(getLogEventType(getEventDAO().getEventById(request.getId())) );

        return response;
    }

    private static LogEventSimpleResponseMessageType buildSimpleResponse(Boolean status, String message) {
        LogEventSimpleResponseMessageType resp = new LogEventSimpleResponseMessageType();
        resp.setStatus(status);
        resp.setMessage(message);
        return resp;
    }

    private static DatabaseEventLoggerDao getEventDAO() {
        return DatabaseEventLoggerDao.getInstance();
    }

    private static List<LogEventType> getLogEventList(List<EventDTO> dtoList) {
        List<LogEventType> list = new LinkedList<>();
        for(EventDTO event : dtoList) {
            list.add(getLogEventType(event));
        }
        return list;
    }

    private static LogEventType getLogEventType(EventDTO event) {
        LogEventType log = new LogEventType();
        log.setId(event.getId());
        log.setName(event.getEventName());
        log.setDescription(event.getDescription());
        log.setTransactionId(event.getTransactionID());
        log.setMessageId(event.getMessageID());
        log.setServiceType(event.getServiceType());
        log.setInitiatingHcid(event.getInitiatorHcid());
        log.setRespondingHcids(event.getRespondingHcid());
        log.setEventTime(getXMLGregorianCalendarFrom(event.getEventTime()));
        log.setExceptionType(event.getExceptionType());
        log.setVersion(event.getVersion());

        return log;
    }

    private static boolean isId(long id) {
        return id > 0;
    }

    private static Set<String> getDropdownServices() {
        Set<String> retObj = new HashSet<>();
        retObj.addAll(EVENT_LOGGING_SERVICE_NAME.getDropdownEventLoggingService().values());
        return retObj;
    }

}
