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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.admingui.util.HelperUtil.convertList;

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.services.ErrorLogService;
import gov.hhs.fha.nhinc.admingui.services.impl.ErrorLogServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.adminguimanagement.LogEventType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ttang
 *
 */

@ManagedBean(name = "errorLogBean")
@SessionScoped
public class ErrorLogBean {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorLogBean.class);
    public static final String KEY_SERVICES = "key:services";
    public static final String KEY_EXCEPTIONS = "key:exceptions";

    private LogEventType selectedEvent;
    private JSONObject selectedEventJson;
    private String selectedService;
    private String selectedException;
    private Date fromDate;
    private Date toDate;

    private ErrorLogService service = new ErrorLogServiceImpl();

    private Map<String, List<String>> dropdowns;
    private List<LogEventType> eventsList;

    public ErrorLogBean() {
        dropdowns = service.getDropdowns();
    }

    public List<String> getServices() {
        return getDropdown(KEY_SERVICES);
    }

    public List<String> getExceptions() {
        return getDropdown(KEY_EXCEPTIONS);
    }

    private List<String> getDropdown(String nameId) {
        if (null != dropdowns && CollectionUtils.isNotEmpty(dropdowns.get(nameId))) {
            return dropdowns.get(nameId);
        }
        return new ArrayList<>();
    }

    public String getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

    public String getSelectedException() {
        return selectedException;
    }

    public void setSelectedException(String selectedException) {
        this.selectedException = selectedException;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public void search() {
        eventsList = service.search(selectedService, selectedException, fromDate, toDate);
    }

    public List<LogEventType> getEvents() {
        if (null == eventsList) {
            return new ArrayList();
        }
        return eventsList;
    }

    public boolean getEventsNotEmpty() {
        return CollectionUtils.isNotEmpty(eventsList);
    }

    public void viewEvent() {
        LOG.debug("view-event: begin");
        if (null == selectedEvent) {
            LOG.info("no event selected for view.");
            HelperUtil.addMessageError(null, "You need to selected an event.");
            return;
        }
        selectedEvent = service.getLogEvent(selectedEvent.getId());

        if (StringUtils.isNotBlank(selectedEvent.getDescription())) {
            selectedEventJson = new JSONObject(selectedEvent.getDescription());
        }

        HelperUtil.execPFShowDialog("wgvDlgViewEventErrorlog");
    }

    private Object getJsonProperty(String key) {
        if (null == selectedEventJson) {
            return null;
        }
        return selectedEventJson.get(key);
    }

    public JSONArray getJsonStackTrace() {
        return (JSONArray) getJsonProperty("stackTrace");
    }

    public String getStringStackTrace() {
        return StringUtils.join(convertList(getJsonStackTrace()), "<br/>");
    }


    public String getCodeStackTrace() {
        return MessageFormat.format("<div class='code'><div>{0}</div></div>",
            StringUtils.join(convertList(getJsonStackTrace()), "</div><div>"));
    }

    public String getJsonFailedMethod() {
        return (String) getJsonProperty("failedMethod");
    }

    public String getJsonServiceType() {
        return (String) getJsonProperty("service_type");
    }

    public String getJsonExceptionClass() {
        return (String) getJsonProperty("exceptionClass");
    }

    public String getJsonFailedClass() {
        return (String) getJsonProperty("failedClass");
    }

    public String getJsonExceptionMessage() {
        return (String) getJsonProperty("exceptionMessage");
    }

    public LogEventType getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(LogEventType selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public String clearTab() {
        selectedService = null;
        selectedException = null;
        fromDate = null;
        toDate = null;
        return NavigationConstant.LOGGING_PAGE;
    }
}
