
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
 * <p>Java class for REPC_MT004000UV01.CareProvisionEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT004000UV01.CareProvisionEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reasonCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}REPC_MT004000UV01.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="recordTarget" type="{urn:hl7-org:v3}REPC_MT004000UV01.RecordTarget" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT004000UV01.Performer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT004000UV01.Author" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT004000UV01.DataEnterer" minOccurs="0"/>
 *         &lt;element name="primaryInformationRecipient" type="{urn:hl7-org:v3}REPC_MT004000UV01.PrimaryInformationRecipient" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT004000UV01.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="inFulfillmentOf" type="{urn:hl7-org:v3}REPC_MT004000UV01.InFulfillmentOf2" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="replacementOf" type="{urn:hl7-org:v3}REPC_MT004000UV01.ReplacementOf" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reason" type="{urn:hl7-org:v3}REPC_MT004000UV01.Reason" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reference" type="{urn:hl7-org:v3}REPC_MT004000UV01.Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pertinentInformation1" type="{urn:hl7-org:v3}REPC_MT004000UV01.PertinentInformation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pertinentInformation2" type="{urn:hl7-org:v3}REPC_MT004000UV01.PertinentInformation4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pertinentInformation3" type="{urn:hl7-org:v3}REPC_MT004000UV01.PertinentInformation5" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component" type="{urn:hl7-org:v3}REPC_MT004000UV01.Component" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassCareProvision" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}ActMood" fixed="EVN" />
 *       &lt;attribute name="negationInd" type="{urn:hl7-org:v3}bl" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT004000UV01.CareProvisionEvent", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "text",
    "statusCode",
    "effectiveTime",
    "priorityCode",
    "confidentialityCode",
    "reasonCode",
    "subject",
    "recordTarget",
    "performer",
    "author",
    "dataEnterer",
    "primaryInformationRecipient",
    "verifier",
    "inFulfillmentOf",
    "replacementOf",
    "reason",
    "reference",
    "pertinentInformation1",
    "pertinentInformation2",
    "pertinentInformation3",
    "component"
})
public class REPCMT004000UV01CareProvisionEvent {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CD code;
    protected EDExplicit text;
    protected CS statusCode;
    protected IVLTSExplicit effectiveTime;
    protected CE priorityCode;
    protected List<CE> confidentialityCode;
    protected List<CE> reasonCode;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Subject3> subject;
    @XmlElementRef(name = "recordTarget", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT004000UV01RecordTarget> recordTarget;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Performer> performer;
    @XmlElementRef(name = "author", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT004000UV01Author> author;
    @XmlElementRef(name = "dataEnterer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT004000UV01DataEnterer> dataEnterer;
    @XmlElementRef(name = "primaryInformationRecipient", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT004000UV01PrimaryInformationRecipient> primaryInformationRecipient;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Verifier> verifier;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01InFulfillmentOf2> inFulfillmentOf;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01ReplacementOf> replacementOf;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Reason> reason;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Reference> reference;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01PertinentInformation> pertinentInformation1;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01PertinentInformation4> pertinentInformation2;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01PertinentInformation5> pertinentInformation3;
    @XmlElement(nillable = true)
    protected List<REPCMT004000UV01Component> component;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected ActClassCareProvision classCode;
    @XmlAttribute(name = "moodCode", required = true)
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
     * {@link REPCMT004000UV01Subject3 }
     * 
     * 
     */
    public List<REPCMT004000UV01Subject3> getSubject() {
        if (subject == null) {
            subject = new ArrayList<REPCMT004000UV01Subject3>();
        }
        return this.subject;
    }

    /**
     * Gets the value of the recordTarget property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01RecordTarget }{@code >}
     *     
     */
    public JAXBElement<REPCMT004000UV01RecordTarget> getRecordTarget() {
        return recordTarget;
    }

    /**
     * Sets the value of the recordTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01RecordTarget }{@code >}
     *     
     */
    public void setRecordTarget(JAXBElement<REPCMT004000UV01RecordTarget> value) {
        this.recordTarget = value;
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
     * {@link REPCMT004000UV01Performer }
     * 
     * 
     */
    public List<REPCMT004000UV01Performer> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT004000UV01Performer>();
        }
        return this.performer;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01Author }{@code >}
     *     
     */
    public JAXBElement<REPCMT004000UV01Author> getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01Author }{@code >}
     *     
     */
    public void setAuthor(JAXBElement<REPCMT004000UV01Author> value) {
        this.author = value;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01DataEnterer }{@code >}
     *     
     */
    public JAXBElement<REPCMT004000UV01DataEnterer> getDataEnterer() {
        return dataEnterer;
    }

    /**
     * Sets the value of the dataEnterer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01DataEnterer }{@code >}
     *     
     */
    public void setDataEnterer(JAXBElement<REPCMT004000UV01DataEnterer> value) {
        this.dataEnterer = value;
    }

    /**
     * Gets the value of the primaryInformationRecipient property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01PrimaryInformationRecipient }{@code >}
     *     
     */
    public JAXBElement<REPCMT004000UV01PrimaryInformationRecipient> getPrimaryInformationRecipient() {
        return primaryInformationRecipient;
    }

    /**
     * Sets the value of the primaryInformationRecipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT004000UV01PrimaryInformationRecipient }{@code >}
     *     
     */
    public void setPrimaryInformationRecipient(JAXBElement<REPCMT004000UV01PrimaryInformationRecipient> value) {
        this.primaryInformationRecipient = value;
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
     * {@link REPCMT004000UV01Verifier }
     * 
     * 
     */
    public List<REPCMT004000UV01Verifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT004000UV01Verifier>();
        }
        return this.verifier;
    }

    /**
     * Gets the value of the inFulfillmentOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inFulfillmentOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInFulfillmentOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01InFulfillmentOf2 }
     * 
     * 
     */
    public List<REPCMT004000UV01InFulfillmentOf2> getInFulfillmentOf() {
        if (inFulfillmentOf == null) {
            inFulfillmentOf = new ArrayList<REPCMT004000UV01InFulfillmentOf2>();
        }
        return this.inFulfillmentOf;
    }

    /**
     * Gets the value of the replacementOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the replacementOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReplacementOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01ReplacementOf }
     * 
     * 
     */
    public List<REPCMT004000UV01ReplacementOf> getReplacementOf() {
        if (replacementOf == null) {
            replacementOf = new ArrayList<REPCMT004000UV01ReplacementOf>();
        }
        return this.replacementOf;
    }

    /**
     * Gets the value of the reason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01Reason }
     * 
     * 
     */
    public List<REPCMT004000UV01Reason> getReason() {
        if (reason == null) {
            reason = new ArrayList<REPCMT004000UV01Reason>();
        }
        return this.reason;
    }

    /**
     * Gets the value of the reference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01Reference }
     * 
     * 
     */
    public List<REPCMT004000UV01Reference> getReference() {
        if (reference == null) {
            reference = new ArrayList<REPCMT004000UV01Reference>();
        }
        return this.reference;
    }

    /**
     * Gets the value of the pertinentInformation1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pertinentInformation1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPertinentInformation1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01PertinentInformation }
     * 
     * 
     */
    public List<REPCMT004000UV01PertinentInformation> getPertinentInformation1() {
        if (pertinentInformation1 == null) {
            pertinentInformation1 = new ArrayList<REPCMT004000UV01PertinentInformation>();
        }
        return this.pertinentInformation1;
    }

    /**
     * Gets the value of the pertinentInformation2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pertinentInformation2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPertinentInformation2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01PertinentInformation4 }
     * 
     * 
     */
    public List<REPCMT004000UV01PertinentInformation4> getPertinentInformation2() {
        if (pertinentInformation2 == null) {
            pertinentInformation2 = new ArrayList<REPCMT004000UV01PertinentInformation4>();
        }
        return this.pertinentInformation2;
    }

    /**
     * Gets the value of the pertinentInformation3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pertinentInformation3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPertinentInformation3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT004000UV01PertinentInformation5 }
     * 
     * 
     */
    public List<REPCMT004000UV01PertinentInformation5> getPertinentInformation3() {
        if (pertinentInformation3 == null) {
            pertinentInformation3 = new ArrayList<REPCMT004000UV01PertinentInformation5>();
        }
        return this.pertinentInformation3;
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
     * {@link REPCMT004000UV01Component }
     * 
     * 
     */
    public List<REPCMT004000UV01Component> getComponent() {
        if (component == null) {
            component = new ArrayList<REPCMT004000UV01Component>();
        }
        return this.component;
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
     *     {@link ActClassCareProvision }
     *     
     */
    public ActClassCareProvision getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActClassCareProvision }
     *     
     */
    public void setClassCode(ActClassCareProvision value) {
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
