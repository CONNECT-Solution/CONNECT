/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

/**
 *
 * @author mflynn02
 */
public class AuditData {

    private String receiverPatientId;
    private String senderPatientId;
    private String messageType;
    private String recieverHomeCommunityId;

    public String getRecieverHomeCommunityId() {
        return recieverHomeCommunityId;
    }

    public void setRecieverHomeCommunityId(String recieverHomeCommunityId) {
        this.recieverHomeCommunityId = recieverHomeCommunityId;
    }

    public String getSenderHomeCommunityId() {
        return senderHomeCommunityId;
    }

    public void setSenderHomeCommunityId(String senderHomeCommunityId) {
        this.senderHomeCommunityId = senderHomeCommunityId;
    }
    private String senderHomeCommunityId;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getReceiverPatientId() {
        return receiverPatientId;
    }

    public void setReceiverPatientId(String receiverPatientId) {
        this.receiverPatientId = receiverPatientId;
    }

    public String getSenderPatientId() {
        return senderPatientId;
    }

    public void setSenderPatientId(String senderPatientId) {
        this.senderPatientId = senderPatientId;
    }
    
}
