/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.asyncmsgs.model;

import java.sql.Blob;
import java.util.Date;

/**
 *
 * @author JHOPPESC
 */
public class AsyncMsgRecord {
    private String messageId = null;
    private Date creationTime = null;
    private String serviceName = null;
    private Blob msgData = null;

    public AsyncMsgRecord() {}
    
    public String getMessageId () {
        return messageId;
    }

    public void setMessageId (String messageId) {
        this.messageId = messageId;
    }

    public Date getCreationTime () {
        return creationTime;
    }

    public void setCreationTime (Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getServiceName () {
        return serviceName;
    }

    public void setServiceName (String serviceName) {
        this.serviceName = serviceName;
    }

    public Blob getMsgData () {
        return msgData;
    }

    public void setMsgData (Blob msgData) {
        this.msgData = msgData;
    }

}
