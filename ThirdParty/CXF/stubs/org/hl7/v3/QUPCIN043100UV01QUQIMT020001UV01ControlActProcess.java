
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QUPC_IN043100UV01.QUQI_MT020001UV01.ControlActProcess complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QUPC_IN043100UV01.QUQI_MT020001UV01.ControlActProcess">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reasonCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="languageCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="overseer" type="{urn:hl7-org:v3}QUQI_MT020001UV01.Overseer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="authorOrPerformer" type="{urn:hl7-org:v3}QUQI_MT020001UV01.AuthorOrPerformer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}QUQI_MT020001UV01.DataEnterer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="informationRecipient" type="{urn:hl7-org:v3}QUQI_MT020001UV01.InformationRecipient" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reasonOf" type="{urn:hl7-org:v3}QUQI_MT020001UV01.Reason" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="queryByParameter" type="{urn:hl7-org:v3}QUPC_IN043100UV01.QUQI_MT020001UV01.QueryByParameter" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassControlAct" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}x_ActMoodIntentEvent" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QUPC_IN043100UV01.QUQI_MT020001UV01.ControlActProcess", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "text",
    "effectiveTime",
    "priorityCode",
    "reasonCode",
    "languageCode",
    "overseer",
    "authorOrPerformer",
    "dataEnterer",
    "informationRecipient",
    "reasonOf",
    "queryByParameter"
})
public class QUPCIN043100UV01QUQIMT020001UV01ControlActProcess {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CD code;
    protected EDExplicit text;
    protected IVLTSExplicit effectiveTime;
    protected List<CE> priorityCode;
    protected List<CE> reasonCode;
    protected CE languageCode;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01Overseer> overseer;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01AuthorOrPerformer> authorOrPerformer;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01DataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01InformationRecipient> informationRecipient;
    @XmlElement(nillable = true)
    protected List<QUQIMT020001UV01Reason> reasonOf;
    @XmlElementRef(name = "queryByParameter", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> queryByParameter;
    @XmlAttribute(name = "classCode", required = true)
    protected ActClassControlAct classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected XActMoodIntentEvent moodCode;

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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getId() {
        if (id == null) {
            id = new ArrayList<II>();
        }
        return this.id;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CD }
     *     
     */
    public CD getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CD }
     *     
     */
    public void setCode(CD value) {
        this.code = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setText(EDExplicit value) {
        this.text = value;
    }

    /**
     * Gets the value of the effectiveTime property.
     * 
     * @return
     *     possible object is
     *     {@link IVLTSExplicit }
     *     
     */
    public IVLTSExplicit getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * Sets the value of the effectiveTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLTSExplicit }
     *     
     */
    public void setEffectiveTime(IVLTSExplicit value) {
        this.effectiveTime = value;
    }

    /**
     * Gets the value of the priorityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priorityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriorityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getPriorityCode() {
        if (priorityCode == null) {
            priorityCode = new ArrayList<CE>();
        }
        return this.priorityCode;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reasonCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReasonCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getReasonCode() {
        if (reasonCode == null) {
            reasonCode = new ArrayList<CE>();
        }
        return this.reasonCode;
    }

    /**
     * Gets the value of the languageCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setLanguageCode(CE value) {
        this.languageCode = value;
    }

    /**
     * Gets the value of the overseer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the overseer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOverseer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01Overseer }
     * 
     * 
     */
    public List<QUQIMT020001UV01Overseer> getOverseer() {
        if (overseer == null) {
            overseer = new ArrayList<QUQIMT020001UV01Overseer>();
        }
        return this.overseer;
    }

    /**
     * Gets the value of the authorOrPerformer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the authorOrPerformer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthorOrPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01AuthorOrPerformer }
     * 
     * 
     */
    public List<QUQIMT020001UV01AuthorOrPerformer> getAuthorOrPerformer() {
        if (authorOrPerformer == null) {
            authorOrPerformer = new ArrayList<QUQIMT020001UV01AuthorOrPerformer>();
        }
        return this.authorOrPerformer;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataEnterer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataEnterer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01DataEnterer }
     * 
     * 
     */
    public List<QUQIMT020001UV01DataEnterer> getDataEnterer() {
        if (dataEnterer == null) {
            dataEnterer = new ArrayList<QUQIMT020001UV01DataEnterer>();
        }
        return this.dataEnterer;
    }

    /**
     * Gets the value of the informationRecipient property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informationRecipient property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformationRecipient().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01InformationRecipient }
     * 
     * 
     */
    public List<QUQIMT020001UV01InformationRecipient> getInformationRecipient() {
        if (informationRecipient == null) {
            informationRecipient = new ArrayList<QUQIMT020001UV01InformationRecipient>();
        }
        return this.informationRecipient;
    }

    /**
     * Gets the value of the reasonOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reasonOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReasonOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUQIMT020001UV01Reason }
     * 
     * 
     */
    public List<QUQIMT020001UV01Reason> getReasonOf() {
        if (reasonOf == null) {
            reasonOf = new ArrayList<QUQIMT020001UV01Reason>();
        }
        return this.reasonOf;
    }

    /**
     * Gets the value of the queryByParameter property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCIN043100UV01QUQIMT020001UV01QueryByParameter }{@code >}
     *     
     */
    public JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> getQueryByParameter() {
        return queryByParameter;
    }

    /**
     * Sets the value of the queryByParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCIN043100UV01QUQIMT020001UV01QueryByParameter }{@code >}
     *     
     */
    public void setQueryByParameter(JAXBElement<QUPCIN043100UV01QUQIMT020001UV01QueryByParameter> value) {
        this.queryByParameter = value;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link ActClassControlAct }
     *     
     */
    public ActClassControlAct getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActClassControlAct }
     *     
     */
    public void setClassCode(ActClassControlAct value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * @return
     *     possible object is
     *     {@link XActMoodIntentEvent }
     *     
     */
    public XActMoodIntentEvent getMoodCode() {
        return moodCode;
    }

    /**
     * Sets the value of the moodCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XActMoodIntentEvent }
     *     
     */
    public void setMoodCode(XActMoodIntentEvent value) {
        this.moodCode = value;
    }

}
