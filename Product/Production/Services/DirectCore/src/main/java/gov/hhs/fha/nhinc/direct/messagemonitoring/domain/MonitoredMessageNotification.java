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
package gov.hhs.fha.nhinc.direct.messagemonitoring.domain;

import java.util.Date;

/**
 *
 * @author nsubrama
 */
public class MonitoredMessageNotification implements java.io.Serializable {

    private Long id;
    private MonitoredMessage monitoredmessage;
    private String emailid;
    private String messageid;
    private String status;
    private Date createtime;
    private Date updatetime;

    public MonitoredMessageNotification() {
    }

    public MonitoredMessageNotification(MonitoredMessage monitoredmessage, String emailid, String status) {
        this.monitoredmessage = monitoredmessage;
        this.emailid = emailid;
        this.status = status;
    }

    public MonitoredMessageNotification(MonitoredMessage monitoredmessage, String emailid, String messageid, String status, Date createtime, Date updatetime) {
        this.monitoredmessage = monitoredmessage;
        this.emailid = emailid;
        this.messageid = messageid;
        this.status = status;
        this.createtime = createtime;
        this.updatetime = updatetime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MonitoredMessage getMonitoredmessage() {
        return this.monitoredmessage;
    }

    public void setMonitoredmessage(MonitoredMessage monitoredmessage) {
        this.monitoredmessage = monitoredmessage;
    }

    public String getEmailid() {
        return this.emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getMessageid() {
        return this.messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
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

}
