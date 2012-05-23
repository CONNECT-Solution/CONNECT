
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QUPC_IN043200UV01.MCCI_MT000300UV01.Message complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QUPC_IN043200UV01.MCCI_MT000300UV01.Message">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II"/>
 *         &lt;element name="creationTime" type="{urn:hl7-org:v3}TS_explicit"/>
 *         &lt;element name="securityText" type="{urn:hl7-org:v3}ST" minOccurs="0"/>
 *         &lt;element name="versionCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="interactionId" type="{urn:hl7-org:v3}II"/>
 *         &lt;element name="profileId" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="processingCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="processingModeCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="acceptAckCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="attachmentText" type="{urn:hl7-org:v3}ED_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="receiver" type="{urn:hl7-org:v3}MCCI_MT000300UV01.Receiver" maxOccurs="unbounded"/>
 *         &lt;element name="respondTo" type="{urn:hl7-org:v3}MCCI_MT000300UV01.RespondTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sender" type="{urn:hl7-org:v3}MCCI_MT000300UV01.Sender"/>
 *         &lt;element name="attentionLine" type="{urn:hl7-org:v3}MCCI_MT000300UV01.AttentionLine" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="acknowledgement" type="{urn:hl7-org:v3}MCCI_MT000300UV01.Acknowledgement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="controlActProcess" type="{urn:hl7-org:v3}QUPC_IN043200UV01.MFMI_MT700712UV01.ControlActProcess"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QUPC_IN043200UV01.MCCI_MT000300UV01.Message", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "creationTime",
    "securityText",
    "versionCode",
    "interactionId",
    "profileId",
    "processingCode",
    "processingModeCode",
    "acceptAckCode",
    "attachmentText",
    "receiver",
    "respondTo",
    "sender",
    "attentionLine",
    "acknowledgement",
    "controlActProcess"
})
@XmlSeeAlso({
    QUPCIN043200UV01 .class
})
public class QUPCIN043200UV01MCCIMT000300UV01Message {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected II id;
    @XmlElement(required = true)
    protected TSExplicit creationTime;
    protected ST securityText;
    protected CS versionCode;
    @XmlElement(required = true)
    protected II interactionId;
    protected List<II> profileId;
    @XmlElement(required = true)
    protected CS processingCode;
    @XmlElement(required = true)
    protected CS processingModeCode;
    @XmlElement(required = true)
    protected CS acceptAckCode;
    protected List<EDExplicit> attachmentText;
    @XmlElement(required = true)
    protected List<MCCIMT000300UV01Receiver> receiver;
    @XmlElement(nillable = true)
    protected List<MCCIMT000300UV01RespondTo> respondTo;
    @XmlElement(required = true)
    protected MCCIMT000300UV01Sender sender;
    @XmlElement(nillable = true)
    protected List<MCCIMT000300UV01AttentionLine> attentionLine;
    @XmlElement(nillable = true)
    protected List<MCCIMT000300UV01Acknowledgement> acknowledgement;
    @XmlElement(required = true)
    protected QUPCIN043200UV01MFMIMT700712UV01ControlActProcess controlActProcess;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setId(II value) {
        this.id = value;
    }

    /**
     * Gets the value of the creationTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the value of the creationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setCreationTime(TSExplicit value) {
        this.creationTime = value;
    }

    /**
     * Gets the value of the securityText property.
     * 
     * @return
     *     possible object is
     *     {@link ST }
     *     
     */
    public ST getSecurityText() {
        return securityText;
    }

    /**
     * Sets the value of the securityText property.
     * 
     * @param value
     *     allowed object is
     *     {@link ST }
     *     
     */
    public void setSecurityText(ST value) {
        this.securityText = value;
    }

    /**
     * Gets the value of the versionCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getVersionCode() {
        return versionCode;
    }

    /**
     * Sets the value of the versionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setVersionCode(CS value) {
        this.versionCode = value;
    }

    /**
     * Gets the value of the interactionId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getInteractionId() {
        return interactionId;
    }

    /**
     * Sets the value of the interactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setInteractionId(II value) {
        this.interactionId = value;
    }

    /**
     * Gets the value of the profileId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the profileId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProfileId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getProfileId() {
        if (profileId == null) {
            profileId = new ArrayList<II>();
        }
        return this.profileId;
    }

    /**
     * Gets the value of the processingCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getProcessingCode() {
        return processingCode;
    }

    /**
     * Sets the value of the processingCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setProcessingCode(CS value) {
        this.processingCode = value;
    }

    /**
     * Gets the value of the processingModeCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getProcessingModeCode() {
        return processingModeCode;
    }

    /**
     * Sets the value of the processingModeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setProcessingModeCode(CS value) {
        this.processingModeCode = value;
    }

    /**
     * Gets the value of the acceptAckCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getAcceptAckCode() {
        return acceptAckCode;
    }

    /**
     * Sets the value of the acceptAckCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setAcceptAckCode(CS value) {
        this.acceptAckCode = value;
    }

    /**
     * Gets the value of the attachmentText property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachmentText property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachmentText().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EDExplicit }
     * 
     * 
     */
    public List<EDExplicit> getAttachmentText() {
        if (attachmentText == null) {
            attachmentText = new ArrayList<EDExplicit>();
        }
        return this.attachmentText;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MCCIMT000300UV01Receiver }
     * 
     * 
     */
    public List<MCCIMT000300UV01Receiver> getReceiver() {
        if (receiver == null) {
            receiver = new ArrayList<MCCIMT000300UV01Receiver>();
        }
        return this.receiver;
    }

    /**
     * Gets the value of the respondTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the respondTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRespondTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MCCIMT000300UV01RespondTo }
     * 
     * 
     */
    public List<MCCIMT000300UV01RespondTo> getRespondTo() {
        if (respondTo == null) {
            respondTo = new ArrayList<MCCIMT000300UV01RespondTo>();
        }
        return this.respondTo;
    }

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link MCCIMT000300UV01Sender }
     *     
     */
    public MCCIMT000300UV01Sender getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link MCCIMT000300UV01Sender }
     *     
     */
    public void setSender(MCCIMT000300UV01Sender value) {
        this.sender = value;
    }

    /**
     * Gets the value of the attentionLine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attentionLine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttentionLine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MCCIMT000300UV01AttentionLine }
     * 
     * 
     */
    public List<MCCIMT000300UV01AttentionLine> getAttentionLine() {
        if (attentionLine == null) {
            attentionLine = new ArrayList<MCCIMT000300UV01AttentionLine>();
        }
        return this.attentionLine;
    }

    /**
     * Gets the value of the acknowledgement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acknowledgement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAcknowledgement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MCCIMT000300UV01Acknowledgement }
     * 
     * 
     */
    public List<MCCIMT000300UV01Acknowledgement> getAcknowledgement() {
        if (acknowledgement == null) {
            acknowledgement = new ArrayList<MCCIMT000300UV01Acknowledgement>();
        }
        return this.acknowledgement;
    }

    /**
     * Gets the value of the controlActProcess property.
     * 
     * @return
     *     possible object is
     *     {@link QUPCIN043200UV01MFMIMT700712UV01ControlActProcess }
     *     
     */
    public QUPCIN043200UV01MFMIMT700712UV01ControlActProcess getControlActProcess() {
        return controlActProcess;
    }

    /**
     * Sets the value of the controlActProcess property.
     * 
     * @param value
     *     allowed object is
     *     {@link QUPCIN043200UV01MFMIMT700712UV01ControlActProcess }
     *     
     */
    public void setControlActProcess(QUPCIN043200UV01MFMIMT700712UV01ControlActProcess value) {
        this.controlActProcess = value;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

}
