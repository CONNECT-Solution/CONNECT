
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
 * <p>Java class for REPC_MT000301UV.ConditionEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000301UV.ConditionEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}SXCM_TS_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CV" minOccurs="0"/>
 *         &lt;element name="uncertaintyCode" type="{urn:hl7-org:v3}CV" minOccurs="0"/>
 *         &lt;element name="value" type="{urn:hl7-org:v3}ANY" minOccurs="0"/>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}REPC_MT000301UV.Subject2" minOccurs="0"/>
 *         &lt;element name="recordTarget" type="{urn:hl7-org:v3}REPC_MT000301UV.RecordTarget" minOccurs="0"/>
 *         &lt;element name="responsibleParty" type="{urn:hl7-org:v3}REPC_MT000301UV.ResponsibleParty2" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT000301UV.Performer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT000301UV.Author" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT000301UV.DataEnterer" minOccurs="0"/>
 *         &lt;element name="informant" type="{urn:hl7-org:v3}REPC_MT000301UV.Informant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT000301UV.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}REPC_MT000301UV.Location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="replacementOf" type="{urn:hl7-org:v3}REPC_MT000301UV.ReplacementOf" minOccurs="0"/>
 *         &lt;element name="sequelTo" type="{urn:hl7-org:v3}REPC_MT000301UV.SequelTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component" type="{urn:hl7-org:v3}REPC_MT000301UV.Component" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}REPC_MT000301UV.Subject5" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}REPC_MT000301UV.Subject1" minOccurs="0"/>
 *         &lt;element name="subjectOf3" type="{urn:hl7-org:v3}REPC_MT000301UV.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="assignedConditionName" type="{urn:hl7-org:v3}REPC_MT000301UV.ConditionNamed" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" type="{urn:hl7-org:v3}ActClassCondition" fixed="COND" />
 *       &lt;attribute name="moodCode" type="{urn:hl7-org:v3}ActMood" fixed="EVN" />
 *       &lt;attribute name="negationInd" type="{urn:hl7-org:v3}bl" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000301UV.ConditionEvent", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "text",
    "statusCode",
    "effectiveTime",
    "confidentialityCode",
    "uncertaintyCode",
    "value",
    "subject",
    "recordTarget",
    "responsibleParty",
    "performer",
    "author",
    "dataEnterer",
    "informant",
    "verifier",
    "location",
    "replacementOf",
    "sequelTo",
    "component",
    "subjectOf1",
    "subjectOf2",
    "subjectOf3",
    "assignedConditionName"
})
public class REPCMT000301UVConditionEvent {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected List<II> id;
    @XmlElement(required = true)
    protected CD code;
    protected EDExplicit text;
    protected CS statusCode;
    protected List<SXCMTSExplicit> effectiveTime;
    protected CV confidentialityCode;
    protected CV uncertaintyCode;
    protected ANY value;
    @XmlElementRef(name = "subject", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVSubject2> subject;
    @XmlElementRef(name = "recordTarget", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVRecordTarget> recordTarget;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVResponsibleParty2> responsibleParty;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVPerformer> performer;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVAuthor> author;
    @XmlElementRef(name = "dataEnterer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVDataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVInformant> informant;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVVerifier> verifier;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVLocation> location;
    @XmlElementRef(name = "replacementOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVReplacementOf> replacementOf;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVSequelTo> sequelTo;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVComponent> component;
    @XmlElementRef(name = "subjectOf1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVSubject5> subjectOf1;
    @XmlElementRef(name = "subjectOf2", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVSubject1> subjectOf2;
    @XmlElement(nillable = true)
    protected List<REPCMT000301UVSubject3> subjectOf3;
    @XmlElementRef(name = "assignedConditionName", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000301UVConditionNamed> assignedConditionName;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode")
    protected String classCode;
    @XmlAttribute(name = "moodCode")
    protected List<String> moodCode;
    @XmlAttribute(name = "negationInd")
    protected Boolean negationInd;

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
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setStatusCode(CS value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the effectiveTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the effectiveTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEffectiveTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SXCMTSExplicit }
     * 
     * 
     */
    public List<SXCMTSExplicit> getEffectiveTime() {
        if (effectiveTime == null) {
            effectiveTime = new ArrayList<SXCMTSExplicit>();
        }
        return this.effectiveTime;
    }

    /**
     * Gets the value of the confidentialityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CV }
     *     
     */
    public CV getConfidentialityCode() {
        return confidentialityCode;
    }

    /**
     * Sets the value of the confidentialityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CV }
     *     
     */
    public void setConfidentialityCode(CV value) {
        this.confidentialityCode = value;
    }

    /**
     * Gets the value of the uncertaintyCode property.
     * 
     * @return
     *     possible object is
     *     {@link CV }
     *     
     */
    public CV getUncertaintyCode() {
        return uncertaintyCode;
    }

    /**
     * Sets the value of the uncertaintyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CV }
     *     
     */
    public void setUncertaintyCode(CV value) {
        this.uncertaintyCode = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link ANY }
     *     
     */
    public ANY getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link ANY }
     *     
     */
    public void setValue(ANY value) {
        this.value = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVSubject2> getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject2 }{@code >}
     *     
     */
    public void setSubject(JAXBElement<REPCMT000301UVSubject2> value) {
        this.subject = value;
    }

    /**
     * Gets the value of the recordTarget property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVRecordTarget }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVRecordTarget> getRecordTarget() {
        return recordTarget;
    }

    /**
     * Sets the value of the recordTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVRecordTarget }{@code >}
     *     
     */
    public void setRecordTarget(JAXBElement<REPCMT000301UVRecordTarget> value) {
        this.recordTarget = value;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVResponsibleParty2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVResponsibleParty2> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVResponsibleParty2 }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<REPCMT000301UVResponsibleParty2> value) {
        this.responsibleParty = value;
    }

    /**
     * Gets the value of the performer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVPerformer }
     * 
     * 
     */
    public List<REPCMT000301UVPerformer> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT000301UVPerformer>();
        }
        return this.performer;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVAuthor }
     * 
     * 
     */
    public List<REPCMT000301UVAuthor> getAuthor() {
        if (author == null) {
            author = new ArrayList<REPCMT000301UVAuthor>();
        }
        return this.author;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVDataEnterer }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVDataEnterer> getDataEnterer() {
        return dataEnterer;
    }

    /**
     * Sets the value of the dataEnterer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVDataEnterer }{@code >}
     *     
     */
    public void setDataEnterer(JAXBElement<REPCMT000301UVDataEnterer> value) {
        this.dataEnterer = value;
    }

    /**
     * Gets the value of the informant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVInformant }
     * 
     * 
     */
    public List<REPCMT000301UVInformant> getInformant() {
        if (informant == null) {
            informant = new ArrayList<REPCMT000301UVInformant>();
        }
        return this.informant;
    }

    /**
     * Gets the value of the verifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVerifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVVerifier }
     * 
     * 
     */
    public List<REPCMT000301UVVerifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT000301UVVerifier>();
        }
        return this.verifier;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVLocation }
     * 
     * 
     */
    public List<REPCMT000301UVLocation> getLocation() {
        if (location == null) {
            location = new ArrayList<REPCMT000301UVLocation>();
        }
        return this.location;
    }

    /**
     * Gets the value of the replacementOf property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVReplacementOf }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVReplacementOf> getReplacementOf() {
        return replacementOf;
    }

    /**
     * Sets the value of the replacementOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVReplacementOf }{@code >}
     *     
     */
    public void setReplacementOf(JAXBElement<REPCMT000301UVReplacementOf> value) {
        this.replacementOf = value;
    }

    /**
     * Gets the value of the sequelTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sequelTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSequelTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVSequelTo }
     * 
     * 
     */
    public List<REPCMT000301UVSequelTo> getSequelTo() {
        if (sequelTo == null) {
            sequelTo = new ArrayList<REPCMT000301UVSequelTo>();
        }
        return this.sequelTo;
    }

    /**
     * Gets the value of the component property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the component property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVComponent }
     * 
     * 
     */
    public List<REPCMT000301UVComponent> getComponent() {
        if (component == null) {
            component = new ArrayList<REPCMT000301UVComponent>();
        }
        return this.component;
    }

    /**
     * Gets the value of the subjectOf1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject5 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVSubject5> getSubjectOf1() {
        return subjectOf1;
    }

    /**
     * Sets the value of the subjectOf1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject5 }{@code >}
     *     
     */
    public void setSubjectOf1(JAXBElement<REPCMT000301UVSubject5> value) {
        this.subjectOf1 = value;
    }

    /**
     * Gets the value of the subjectOf2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject1 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVSubject1> getSubjectOf2() {
        return subjectOf2;
    }

    /**
     * Sets the value of the subjectOf2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVSubject1 }{@code >}
     *     
     */
    public void setSubjectOf2(JAXBElement<REPCMT000301UVSubject1> value) {
        this.subjectOf2 = value;
    }

    /**
     * Gets the value of the subjectOf3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000301UVSubject3 }
     * 
     * 
     */
    public List<REPCMT000301UVSubject3> getSubjectOf3() {
        if (subjectOf3 == null) {
            subjectOf3 = new ArrayList<REPCMT000301UVSubject3>();
        }
        return this.subjectOf3;
    }

    /**
     * Gets the value of the assignedConditionName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVConditionNamed }{@code >}
     *     
     */
    public JAXBElement<REPCMT000301UVConditionNamed> getAssignedConditionName() {
        return assignedConditionName;
    }

    /**
     * Sets the value of the assignedConditionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000301UVConditionNamed }{@code >}
     *     
     */
    public void setAssignedConditionName(JAXBElement<REPCMT000301UVConditionNamed> value) {
        this.assignedConditionName = value;
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
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassCode() {
        if (classCode == null) {
            return "COND";
        } else {
            return classCode;
        }
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassCode(String value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the moodCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMoodCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMoodCode() {
        if (moodCode == null) {
            moodCode = new ArrayList<String>();
        }
        return this.moodCode;
    }

    /**
     * Gets the value of the negationInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNegationInd() {
        return negationInd;
    }

    /**
     * Sets the value of the negationInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNegationInd(Boolean value) {
        this.negationInd = value;
    }

}
