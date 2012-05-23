
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COCT_MT080000UV.Subject4 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT080000UV.Subject4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="sequenceNumber" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="time" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="specimenObservationEvent" type="{urn:hl7-org:v3}COCT_MT080000UV.SpecimenObservationEvent"/>
 *           &lt;element name="automationSpecimenObservationEvent" type="{urn:hl7-org:v3}COCT_MT080000UV.AutomationSpecimenObservationEvent"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="typeCode" use="required" type="{urn:hl7-org:v3}ParticipationTargetSubject" />
 *       &lt;attribute name="contextControlCode" type="{urn:hl7-org:v3}ContextControl" default="ON" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT080000UV.Subject4", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "sequenceNumber",
    "time",
    "specimenObservationEvent",
    "automationSpecimenObservationEvent"
})
public class COCTMT080000UVSubject4 {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected INT sequenceNumber;
    protected IVLTSExplicit time;
    @XmlElementRef(name = "specimenObservationEvent", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVSpecimenObservationEvent> specimenObservationEvent;
    @XmlElementRef(name = "automationSpecimenObservationEvent", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVAutomationSpecimenObservationEvent> automationSpecimenObservationEvent;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "typeCode", required = true)
    protected ParticipationTargetSubject typeCode;
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
     * Gets the value of the sequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setSequenceNumber(INT value) {
        this.sequenceNumber = value;
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
     * Gets the value of the specimenObservationEvent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVSpecimenObservationEvent }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVSpecimenObservationEvent> getSpecimenObservationEvent() {
        return specimenObservationEvent;
    }

    /**
     * Sets the value of the specimenObservationEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVSpecimenObservationEvent }{@code >}
     *     
     */
    public void setSpecimenObservationEvent(JAXBElement<COCTMT080000UVSpecimenObservationEvent> value) {
        this.specimenObservationEvent = value;
    }

    /**
     * Gets the value of the automationSpecimenObservationEvent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVAutomationSpecimenObservationEvent }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVAutomationSpecimenObservationEvent> getAutomationSpecimenObservationEvent() {
        return automationSpecimenObservationEvent;
    }

    /**
     * Sets the value of the automationSpecimenObservationEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVAutomationSpecimenObservationEvent }{@code >}
     *     
     */
    public void setAutomationSpecimenObservationEvent(JAXBElement<COCTMT080000UVAutomationSpecimenObservationEvent> value) {
        this.automationSpecimenObservationEvent = value;
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
     *     {@link ParticipationTargetSubject }
     *     
     */
    public ParticipationTargetSubject getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipationTargetSubject }
     *     
     */
    public void setTypeCode(ParticipationTargetSubject value) {
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
            return "ON";
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
