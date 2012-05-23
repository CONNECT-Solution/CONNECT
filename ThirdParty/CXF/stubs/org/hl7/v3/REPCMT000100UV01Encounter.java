
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
 * <p>Java class for REPC_MT000100UV01.Encounter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000100UV01.Encounter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}SXCM_TS_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="availabilityTime" type="{urn:hl7-org:v3}TS_explicit" minOccurs="0"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="admissionReferralSourceCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="lengthOfStayQuantity" type="{urn:hl7-org:v3}PQ" minOccurs="0"/>
 *         &lt;element name="dischargeDispositionCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="preAdmitTestInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="specialCourtesiesCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="specialArrangementCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}REPC_MT000100UV01.Subject4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="recordTarget" type="{urn:hl7-org:v3}REPC_MT000100UV01.RecordTarget" minOccurs="0"/>
 *         &lt;element name="responsibleParty" type="{urn:hl7-org:v3}REPC_MT000100UV01.ResponsibleParty" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT000100UV01.Performer3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT000100UV01.Author3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT000100UV01.DataEnterer" minOccurs="0"/>
 *         &lt;element name="informant" type="{urn:hl7-org:v3}REPC_MT000100UV01.Informant12" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT000100UV01.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}REPC_MT000100UV01.Location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="definition" type="{urn:hl7-org:v3}REPC_MT000100UV01.Definition2" minOccurs="0"/>
 *         &lt;element name="conditions" type="{urn:hl7-org:v3}REPC_MT000100UV01.Conditions" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sourceOf" type="{urn:hl7-org:v3}REPC_MT000100UV01.SourceOf3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="targetOf" type="{urn:hl7-org:v3}REPC_MT000100UV01.SourceOf" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClass" fixed="ENC" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}x_ClinicalStatementEncounterMood" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000100UV01.Encounter", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "text",
    "statusCode",
    "effectiveTime",
    "availabilityTime",
    "priorityCode",
    "confidentialityCode",
    "admissionReferralSourceCode",
    "lengthOfStayQuantity",
    "dischargeDispositionCode",
    "preAdmitTestInd",
    "specialCourtesiesCode",
    "specialArrangementCode",
    "subject",
    "recordTarget",
    "responsibleParty",
    "performer",
    "author",
    "dataEnterer",
    "informant",
    "verifier",
    "location",
    "definition",
    "conditions",
    "sourceOf",
    "targetOf"
})
public class REPCMT000100UV01Encounter {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected List<II> id;
    protected CD code;
    protected EDExplicit text;
    protected CS statusCode;
    protected List<SXCMTSExplicit> effectiveTime;
    protected TSExplicit availabilityTime;
    protected CE priorityCode;
    protected List<CE> confidentialityCode;
    protected CE admissionReferralSourceCode;
    protected PQ lengthOfStayQuantity;
    protected CE dischargeDispositionCode;
    protected BL preAdmitTestInd;
    protected List<CE> specialCourtesiesCode;
    protected List<CE> specialArrangementCode;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Subject4> subject;
    @XmlElementRef(name = "recordTarget", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01RecordTarget> recordTarget;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01ResponsibleParty> responsibleParty;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Performer3> performer;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Author3> author;
    @XmlElementRef(name = "dataEnterer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01DataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Informant12> informant;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Verifier> verifier;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Location> location;
    @XmlElementRef(name = "definition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Definition2> definition;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Conditions> conditions;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01SourceOf3> sourceOf;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01SourceOf> targetOf;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected XClinicalStatementEncounterMood moodCode;

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
     * Gets the value of the availabilityTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getAvailabilityTime() {
        return availabilityTime;
    }

    /**
     * Sets the value of the availabilityTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setAvailabilityTime(TSExplicit value) {
        this.availabilityTime = value;
    }

    /**
     * Gets the value of the priorityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getPriorityCode() {
        return priorityCode;
    }

    /**
     * Sets the value of the priorityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setPriorityCode(CE value) {
        this.priorityCode = value;
    }

    /**
     * Gets the value of the confidentialityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the confidentialityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfidentialityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getConfidentialityCode() {
        if (confidentialityCode == null) {
            confidentialityCode = new ArrayList<CE>();
        }
        return this.confidentialityCode;
    }

    /**
     * Gets the value of the admissionReferralSourceCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getAdmissionReferralSourceCode() {
        return admissionReferralSourceCode;
    }

    /**
     * Sets the value of the admissionReferralSourceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setAdmissionReferralSourceCode(CE value) {
        this.admissionReferralSourceCode = value;
    }

    /**
     * Gets the value of the lengthOfStayQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQ }
     *     
     */
    public PQ getLengthOfStayQuantity() {
        return lengthOfStayQuantity;
    }

    /**
     * Sets the value of the lengthOfStayQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQ }
     *     
     */
    public void setLengthOfStayQuantity(PQ value) {
        this.lengthOfStayQuantity = value;
    }

    /**
     * Gets the value of the dischargeDispositionCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getDischargeDispositionCode() {
        return dischargeDispositionCode;
    }

    /**
     * Sets the value of the dischargeDispositionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setDischargeDispositionCode(CE value) {
        this.dischargeDispositionCode = value;
    }

    /**
     * Gets the value of the preAdmitTestInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getPreAdmitTestInd() {
        return preAdmitTestInd;
    }

    /**
     * Sets the value of the preAdmitTestInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setPreAdmitTestInd(BL value) {
        this.preAdmitTestInd = value;
    }

    /**
     * Gets the value of the specialCourtesiesCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialCourtesiesCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialCourtesiesCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getSpecialCourtesiesCode() {
        if (specialCourtesiesCode == null) {
            specialCourtesiesCode = new ArrayList<CE>();
        }
        return this.specialCourtesiesCode;
    }

    /**
     * Gets the value of the specialArrangementCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialArrangementCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialArrangementCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getSpecialArrangementCode() {
        if (specialArrangementCode == null) {
            specialArrangementCode = new ArrayList<CE>();
        }
        return this.specialArrangementCode;
    }

    /**
     * Gets the value of the subject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Subject4 }
     * 
     * 
     */
    public List<REPCMT000100UV01Subject4> getSubject() {
        if (subject == null) {
            subject = new ArrayList<REPCMT000100UV01Subject4>();
        }
        return this.subject;
    }

    /**
     * Gets the value of the recordTarget property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01RecordTarget }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01RecordTarget> getRecordTarget() {
        return recordTarget;
    }

    /**
     * Sets the value of the recordTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01RecordTarget }{@code >}
     *     
     */
    public void setRecordTarget(JAXBElement<REPCMT000100UV01RecordTarget> value) {
        this.recordTarget = value;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ResponsibleParty }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01ResponsibleParty> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ResponsibleParty }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<REPCMT000100UV01ResponsibleParty> value) {
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
     * {@link REPCMT000100UV01Performer3 }
     * 
     * 
     */
    public List<REPCMT000100UV01Performer3> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT000100UV01Performer3>();
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
     * {@link REPCMT000100UV01Author3 }
     * 
     * 
     */
    public List<REPCMT000100UV01Author3> getAuthor() {
        if (author == null) {
            author = new ArrayList<REPCMT000100UV01Author3>();
        }
        return this.author;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01DataEnterer }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01DataEnterer> getDataEnterer() {
        return dataEnterer;
    }

    /**
     * Sets the value of the dataEnterer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01DataEnterer }{@code >}
     *     
     */
    public void setDataEnterer(JAXBElement<REPCMT000100UV01DataEnterer> value) {
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
     * {@link REPCMT000100UV01Informant12 }
     * 
     * 
     */
    public List<REPCMT000100UV01Informant12> getInformant() {
        if (informant == null) {
            informant = new ArrayList<REPCMT000100UV01Informant12>();
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
     * {@link REPCMT000100UV01Verifier }
     * 
     * 
     */
    public List<REPCMT000100UV01Verifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT000100UV01Verifier>();
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
     * {@link REPCMT000100UV01Location }
     * 
     * 
     */
    public List<REPCMT000100UV01Location> getLocation() {
        if (location == null) {
            location = new ArrayList<REPCMT000100UV01Location>();
        }
        return this.location;
    }

    /**
     * Gets the value of the definition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Definition2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Definition2> getDefinition() {
        return definition;
    }

    /**
     * Sets the value of the definition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Definition2 }{@code >}
     *     
     */
    public void setDefinition(JAXBElement<REPCMT000100UV01Definition2> value) {
        this.definition = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conditions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConditions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Conditions }
     * 
     * 
     */
    public List<REPCMT000100UV01Conditions> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<REPCMT000100UV01Conditions>();
        }
        return this.conditions;
    }

    /**
     * Gets the value of the sourceOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01SourceOf3 }
     * 
     * 
     */
    public List<REPCMT000100UV01SourceOf3> getSourceOf() {
        if (sourceOf == null) {
            sourceOf = new ArrayList<REPCMT000100UV01SourceOf3>();
        }
        return this.sourceOf;
    }

    /**
     * Gets the value of the targetOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01SourceOf }
     * 
     * 
     */
    public List<REPCMT000100UV01SourceOf> getTargetOf() {
        if (targetOf == null) {
            targetOf = new ArrayList<REPCMT000100UV01SourceOf>();
        }
        return this.targetOf;
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClassCode() {
        if (classCode == null) {
            classCode = new ArrayList<String>();
        }
        return this.classCode;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * @return
     *     possible object is
     *     {@link XClinicalStatementEncounterMood }
     *     
     */
    public XClinicalStatementEncounterMood getMoodCode() {
        return moodCode;
    }

    /**
     * Sets the value of the moodCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XClinicalStatementEncounterMood }
     *     
     */
    public void setMoodCode(XClinicalStatementEncounterMood value) {
        this.moodCode = value;
    }

}
