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
package gov.hhs.fha.nhinc.direct.messagemonitoring.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @author nsubrama
 */
public class MonitoredMessage implements java.io.Serializable {

    private Long id;
    private String senderemailid;
    private String subject;
    private String messageid;
    private String recipients;
    private Boolean deliveryrequested;
    private String status;
    private Date createtime;
    private Date updatetime;
    private Set monitoredmessagenotifications = new HashSet(0);

    public MonitoredMessage() {
    }

    public MonitoredMessage(String senderemailid, String subject, String messageid, String recipients, Boolean deliveryrequested, String status, Date createtime, Date updatetime, Set monitoredmessagenotifications) {
        this.senderemailid = senderemailid;
        this.subject = subject;
        this.messageid = messageid;
        this.recipients = recipients;
        this.deliveryrequested = deliveryrequested;
        this.status = status;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.monitoredmessagenotifications = monitoredmessagenotifications;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderemailid() {
        return this.senderemailid;
    }

    public void setSenderemailid(String senderemailid) {
        this.senderemailid = senderemailid;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageid() {
        return this.messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Boolean getDeliveryrequested() {
        return this.deliveryrequested;
    }

    public void setDeliveryrequested(Boolean deliveryrequested) {
        this.deliveryrequested = deliveryrequested;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Set getMonitoredmessagenotifications() {
        return this.monitoredmessagenotifications;
    }

    public void setMonitoredmessagenotifications(Set monitoredmessagenotifications) {
        this.monitoredmessagenotifications = monitoredmessagenotifications;
    }

}
