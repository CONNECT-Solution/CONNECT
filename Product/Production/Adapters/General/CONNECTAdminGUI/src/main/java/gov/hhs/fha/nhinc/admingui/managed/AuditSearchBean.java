/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.event.model.Audit;
import gov.hhs.fha.nhinc.admingui.services.AuditService;
import gov.hhs.fha.nhinc.admingui.services.impl.AuditServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.RemoteOrganizationIdentifier;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author achidamb
 */
@ManagedBean(name = "auditSearchBean")
@SessionScoped
public class AuditSearchBean {

    private String userId;
    private Map<String, String> remoteHcidList;
    private Date eventStartDate;
    private Date eventEndDate;
    private ArrayList<String> eventTypeList;
    private int activeIndex = 0;
    private String outcomeIndicator;
    private ArrayList<String> selectedRemoteHcidList;
    private ArrayList<String> selectedEventTypeList;
    private Map<String, String> eventOutcomeIndicatorList;
    private ArrayList<Audit> auditRecordList;
    private String auditMessage;
    private boolean auditFound;
    private String messageId;
    private String RelatesTo;

    public AuditSearchBean() {
        setEventTypeList(populateEventTypeList());
        setRemoteHcidList(populateRemoteOrgHcid());
        setEventOutcomeIndicatorList(populateEventOutcomeIndicatorValues());
    }

    public void searchAudit() {
        AuditService impl = new AuditServiceImpl();
        this.auditRecordList = impl.createMockAuditRecord();
        if (this.auditRecordList.isEmpty() && this.auditRecordList == null) {
            this.auditMessage = "Audit Records not found";
            auditFound = false;
        } else {
            this.auditMessage = "Audit Records Found";
            auditFound = true;
        }
    }

    public void searchAuditMessageId() {
        AuditServiceImpl impl = new AuditServiceImpl();
        this.auditRecordList = impl.createMockAuditRecord();
        if (this.auditRecordList.isEmpty() && this.auditRecordList == null) {
            this.auditMessage = "Audit Records not found";
            auditFound = false;
        } else {
            this.auditMessage = "Audit Records Found";
            auditFound = true;
        }
    }

    public String clearAuditTab() {
        this.eventEndDate = null;
        this.eventStartDate = null;
        if (this.selectedEventTypeList != null && !this.selectedEventTypeList.isEmpty()) {
            this.selectedEventTypeList.clear();
        }
        if (this.selectedRemoteHcidList != null && !this.selectedRemoteHcidList.isEmpty()) {
            this.selectedRemoteHcidList.clear();
        }
        this.outcomeIndicator = null;
        this.userId = null;
        if (this.auditRecordList != null && !auditRecordList.isEmpty()) {
            this.auditRecordList.clear();
        }
        this.auditMessage = "";
        this.auditFound = false;
        return NavigationConstant.AUDIT_SEARCH_PAGE;
    }

    public String clearAuditTabMessageId() {
        this.RelatesTo = null;
        this.messageId = null;
        if (this.auditRecordList != null && !auditRecordList.isEmpty()) {
            this.auditRecordList.clear();
        }
        this.auditMessage = "";
        this.auditFound = false;
        return NavigationConstant.AUDIT_SEARCH_PAGE;
    }

    private Map<String, String> populateRemoteOrgHcid() {

        return new RemoteOrganizationIdentifier().getRemoteHcidFromUUID();

    }

    private ArrayList<String> populateEventTypeList() {
        //TODO convert into Map. Define enum and and hold service Names.
        ArrayList<String> serviceNames = new ArrayList<>();
        serviceNames.add(NhincConstants.NHINC_XDR_SERVICE_NAME);
        serviceNames.add(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        serviceNames.add(NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        serviceNames.add(NhincConstants.DOC_QUERY_SERVICE_NAME);
        serviceNames.add(NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        serviceNames.add(NhincConstants.CORE_X12DS_REALTIME_SERVICE_NAME);
        serviceNames.add(NhincConstants.CORE_X12DS_GENERICBATCH_REQUEST_SERVICE_NAME);
        serviceNames.add(NhincConstants.CORE_X12DS_GENERICBATCH_RESPONSE_SERVICE_NAME);
        serviceNames.add(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME);
        serviceNames.add(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        serviceNames.add(NhincConstants.NHINC_XDR_REQUEST_SERVICE_NAME);
        serviceNames.add(NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
        return serviceNames;
    }

    private Map<String, String> populateEventOutcomeIndicatorValues() {
        Map<String, String> outcomeList = new HashMap<>();
        outcomeList.put("Success", "0");
        outcomeList.put("Failure", "12");
        return outcomeList;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRelatesTo() {
        return RelatesTo;
    }

    public void setRelatesTo(String RelatesTo) {
        this.RelatesTo = RelatesTo;
    }

    public boolean isAuditFound() {
        return auditFound;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public ArrayList<Audit> getAuditRecordList() {
        return this.auditRecordList;
    }

    public void getAuditRecordList(ArrayList<Audit> auditRecordList) {
        this.auditRecordList = auditRecordList;
    }

    public Map<String, String> getEventOutcomeIndicatorList() {
        return eventOutcomeIndicatorList;
    }

    private void setEventOutcomeIndicatorList(Map<String, String> eventOutcomeIndicatorList) {
        this.eventOutcomeIndicatorList = eventOutcomeIndicatorList;
    }

    public ArrayList<String> getSelectedEventTypeList() {
        return this.selectedEventTypeList;
    }

    public void setSelectedEventTypeList(ArrayList<String> selectedEventTypeList) {
        this.selectedEventTypeList = selectedEventTypeList;
    }

    public ArrayList<String> getSelectedRemoteHcidList() {
        return this.selectedRemoteHcidList;
    }

    public void setSelectedRemoteHcidList(ArrayList<String> remoteHcid) {
        this.selectedRemoteHcidList = remoteHcid;
    }

    public String getOutcomeIndicator() {
        return outcomeIndicator;
    }

    public void setOutcomeIndicator(String outcomeIndicator) {
        this.outcomeIndicator = outcomeIndicator;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public Map<String, String> getRemoteHcidList() {
        return this.remoteHcidList;
    }

    private void setRemoteHcidList(Map<String, String> remoteHcidList) {
        this.remoteHcidList = remoteHcidList;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getEventTypeList() {
        return this.eventTypeList;
    }

    private void setEventTypeList(ArrayList<String> eventTypeList) {
        this.eventTypeList = eventTypeList;
    }

}
