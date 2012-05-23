
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QUQI_MT021001UV01.Overseer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QUQI_MT021001UV01.Overseer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="noteText" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="time" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="modeCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="signatureCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="signatureText" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="assignedPerson" type="{urn:hl7-org:v3}COCT_MT090100UV01.AssignedPerson"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="typeCode" use="required" type="{urn:hl7-org:v3}x_ParticipationVrfRespSprfWit" />
 *       &lt;attribute name="contextControlCode" type="{urn:hl7-org:v3}ContextControl" default="AP" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QUQI_MT021001UV01.Overseer", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "noteText",
    "time",
    "modeCode",
    "signatureCode",
    "signatureText",
    "assignedPerson"
})
public class QUQIMT021001UV01Overseer {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected EDExplicit noteText;
    protected IVLTSExplicit time;
    protected CE modeCode;
    protected CE signatureCode;
    protected EDExplicit signatureText;
    @XmlElement(required = true, nillable = true)
    protected COCTMT090100UV01AssignedPerson assignedPerson;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "typeCode", required = true)
    protected XParticipationVrfRespSprfWit typeCode;
    @XmlAttribute(name = "contextControlCode")
    protected String contextControlCode;

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
     * Gets the value of the noteText property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getNoteText() {
        return noteText;
    }

    /**
     * Sets the value of the noteText property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setNoteText(EDExplicit value) {
        this.noteText = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link IVLTSExplicit }
     *     
     */
    public IVLTSExplicit getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLTSExplicit }
     *     
     */
    public void setTime(IVLTSExplicit value) {
        this.time = value;
    }

    /**
     * Gets the value of the modeCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getModeCode() {
        return modeCode;
    }

    /**
     * Sets the value of the modeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setModeCode(CE value) {
        this.modeCode = value;
    }

    /**
     * Gets the value of the signatureCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getSignatureCode() {
        return signatureCode;
    }

    /**
     * Sets the value of the signatureCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setSignatureCode(CE value) {
        this.signatureCode = value;
    }

    /**
     * Gets the value of the signatureText property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getSignatureText() {
        return signatureText;
    }

    /**
     * Sets the value of the signatureText property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setSignatureText(EDExplicit value) {
        this.signatureText = value;
    }

    /**
     * Gets the value of the assignedPerson property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT090100UV01AssignedPerson }
     *     
     */
    public COCTMT090100UV01AssignedPerson getAssignedPerson() {
        return assignedPerson;
    }

    /**
     * Sets the value of the assignedPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT090100UV01AssignedPerson }
     *     
     */
    public void setAssignedPerson(COCTMT090100UV01AssignedPerson value) {
        this.assignedPerson = value;
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

    /**
     * Gets the value of the typeCode property.
     * 
     * @return
     *     possible object is
     *     {@link XParticipationVrfRespSprfWit }
     *     
     */
    public XParticipationVrfRespSprfWit getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XParticipationVrfRespSprfWit }
     *     
     */
    public void setTypeCode(XParticipationVrfRespSprfWit value) {
        this.typeCode = value;
    }

    /**
     * Gets the value of the contextControlCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextControlCode() {
        if (contextControlCode == null) {
            return "AP";
        } else {
            return contextControlCode;
        }
    }

    /**
     * Sets the value of the contextControlCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextControlCode(String value) {
        this.contextControlCode = value;
    }

}
