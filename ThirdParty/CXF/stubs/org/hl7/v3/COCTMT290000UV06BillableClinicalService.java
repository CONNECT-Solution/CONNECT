
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
 * <p>Java class for COCT_MT290000UV06.BillableClinicalService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT290000UV06.BillableClinicalService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="repeatNumber" type="{urn:hl7-org:v3}IVL_INT" minOccurs="0"/>
 *         &lt;element name="reasonCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}COCT_MT290000UV06.Subject5" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reusableDevice" type="{urn:hl7-org:v3}COCT_MT290000UV06.ReusableDevice" minOccurs="0"/>
 *         &lt;element name="product" type="{urn:hl7-org:v3}COCT_MT290000UV06.Product1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="responsibleParty" type="{urn:hl7-org:v3}COCT_MT290000UV06.ResponsibleParty" minOccurs="0"/>
 *         &lt;element name="secondaryPerformer" type="{urn:hl7-org:v3}COCT_MT290000UV06.SecondaryPerformer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}COCT_MT290000UV06.Performer" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}COCT_MT290000UV06.Author" minOccurs="0"/>
 *         &lt;element name="consultant" type="{urn:hl7-org:v3}COCT_MT290000UV06.Consultant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}COCT_MT290000UV06.Location" minOccurs="0"/>
 *         &lt;element name="inFulfillmentOf" type="{urn:hl7-org:v3}COCT_MT290000UV06.InFulfillmentOf" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reason1" type="{urn:hl7-org:v3}COCT_MT290000UV06.Reason1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reason2" type="{urn:hl7-org:v3}COCT_MT290000UV06.Reason4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reason3" type="{urn:hl7-org:v3}COCT_MT290000UV06.Reason3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component" type="{urn:hl7-org:v3}COCT_MT290000UV06.Component2" minOccurs="0"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}COCT_MT290000UV06.Subject2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}COCT_MT290000UV06.Subject" minOccurs="0"/>
 *         &lt;element name="componentOf" type="{urn:hl7-org:v3}COCT_MT290000UV06.Component1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassRoot" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}ActMoodCompletionTrack" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT290000UV06.BillableClinicalService", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "effectiveTime",
    "priorityCode",
    "confidentialityCode",
    "repeatNumber",
    "reasonCode",
    "subject",
    "reusableDevice",
    "product",
    "responsibleParty",
    "secondaryPerformer",
    "performer",
    "author",
    "consultant",
    "location",
    "inFulfillmentOf",
    "reason1",
    "reason2",
    "reason3",
    "component",
    "subjectOf1",
    "subjectOf2",
    "componentOf"
})
public class COCTMT290000UV06BillableClinicalService {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    @XmlElement(required = true)
    protected CD code;
    @XmlElement(required = true)
    protected IVLTSExplicit effectiveTime;
    protected List<CE> priorityCode;
    protected List<CE> confidentialityCode;
    protected IVLINT repeatNumber;
    protected List<CE> reasonCode;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Subject5> subject;
    @XmlElementRef(name = "reusableDevice", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06ReusableDevice> reusableDevice;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Product1> product;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06ResponsibleParty> responsibleParty;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06SecondaryPerformer> secondaryPerformer;
    @XmlElementRef(name = "performer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06Performer> performer;
    @XmlElementRef(name = "author", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06Author> author;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Consultant> consultant;
    @XmlElementRef(name = "location", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06Location> location;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06InFulfillmentOf> inFulfillmentOf;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Reason1> reason1;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Reason4> reason2;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Reason3> reason3;
    @XmlElementRef(name = "component", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06Component2> component;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Subject2> subjectOf1;
    @XmlElementRef(name = "subjectOf2", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT290000UV06Subject> subjectOf2;
    @XmlElement(nillable = true)
    protected List<COCTMT290000UV06Component1> componentOf;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected List<String> moodCode;

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
     * Gets the value of the repeatNumber property.
     * 
     * @return
     *     possible object is
     *     {@link IVLINT }
     *     
     */
    public IVLINT getRepeatNumber() {
        return repeatNumber;
    }

    /**
     * Sets the value of the repeatNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLINT }
     *     
     */
    public void setRepeatNumber(IVLINT value) {
        this.repeatNumber = value;
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
     * {@link COCTMT290000UV06Subject5 }
     * 
     * 
     */
    public List<COCTMT290000UV06Subject5> getSubject() {
        if (subject == null) {
            subject = new ArrayList<COCTMT290000UV06Subject5>();
        }
        return this.subject;
    }

    /**
     * Gets the value of the reusableDevice property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06ReusableDevice }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06ReusableDevice> getReusableDevice() {
        return reusableDevice;
    }

    /**
     * Sets the value of the reusableDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06ReusableDevice }{@code >}
     *     
     */
    public void setReusableDevice(JAXBElement<COCTMT290000UV06ReusableDevice> value) {
        this.reusableDevice = value;
    }

    /**
     * Gets the value of the product property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Product1 }
     * 
     * 
     */
    public List<COCTMT290000UV06Product1> getProduct() {
        if (product == null) {
            product = new ArrayList<COCTMT290000UV06Product1>();
        }
        return this.product;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06ResponsibleParty }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06ResponsibleParty> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06ResponsibleParty }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<COCTMT290000UV06ResponsibleParty> value) {
        this.responsibleParty = value;
    }

    /**
     * Gets the value of the secondaryPerformer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the secondaryPerformer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSecondaryPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06SecondaryPerformer }
     * 
     * 
     */
    public List<COCTMT290000UV06SecondaryPerformer> getSecondaryPerformer() {
        if (secondaryPerformer == null) {
            secondaryPerformer = new ArrayList<COCTMT290000UV06SecondaryPerformer>();
        }
        return this.secondaryPerformer;
    }

    /**
     * Gets the value of the performer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Performer }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06Performer> getPerformer() {
        return performer;
    }

    /**
     * Sets the value of the performer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Performer }{@code >}
     *     
     */
    public void setPerformer(JAXBElement<COCTMT290000UV06Performer> value) {
        this.performer = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Author }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06Author> getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Author }{@code >}
     *     
     */
    public void setAuthor(JAXBElement<COCTMT290000UV06Author> value) {
        this.author = value;
    }

    /**
     * Gets the value of the consultant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consultant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConsultant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Consultant }
     * 
     * 
     */
    public List<COCTMT290000UV06Consultant> getConsultant() {
        if (consultant == null) {
            consultant = new ArrayList<COCTMT290000UV06Consultant>();
        }
        return this.consultant;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Location }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06Location> getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Location }{@code >}
     *     
     */
    public void setLocation(JAXBElement<COCTMT290000UV06Location> value) {
        this.location = value;
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
     * {@link COCTMT290000UV06InFulfillmentOf }
     * 
     * 
     */
    public List<COCTMT290000UV06InFulfillmentOf> getInFulfillmentOf() {
        if (inFulfillmentOf == null) {
            inFulfillmentOf = new ArrayList<COCTMT290000UV06InFulfillmentOf>();
        }
        return this.inFulfillmentOf;
    }

    /**
     * Gets the value of the reason1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Reason1 }
     * 
     * 
     */
    public List<COCTMT290000UV06Reason1> getReason1() {
        if (reason1 == null) {
            reason1 = new ArrayList<COCTMT290000UV06Reason1>();
        }
        return this.reason1;
    }

    /**
     * Gets the value of the reason2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Reason4 }
     * 
     * 
     */
    public List<COCTMT290000UV06Reason4> getReason2() {
        if (reason2 == null) {
            reason2 = new ArrayList<COCTMT290000UV06Reason4>();
        }
        return this.reason2;
    }

    /**
     * Gets the value of the reason3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Reason3 }
     * 
     * 
     */
    public List<COCTMT290000UV06Reason3> getReason3() {
        if (reason3 == null) {
            reason3 = new ArrayList<COCTMT290000UV06Reason3>();
        }
        return this.reason3;
    }

    /**
     * Gets the value of the component property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Component2 }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06Component2> getComponent() {
        return component;
    }

    /**
     * Sets the value of the component property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Component2 }{@code >}
     *     
     */
    public void setComponent(JAXBElement<COCTMT290000UV06Component2> value) {
        this.component = value;
    }

    /**
     * Gets the value of the subjectOf1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Subject2 }
     * 
     * 
     */
    public List<COCTMT290000UV06Subject2> getSubjectOf1() {
        if (subjectOf1 == null) {
            subjectOf1 = new ArrayList<COCTMT290000UV06Subject2>();
        }
        return this.subjectOf1;
    }

    /**
     * Gets the value of the subjectOf2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Subject }{@code >}
     *     
     */
    public JAXBElement<COCTMT290000UV06Subject> getSubjectOf2() {
        return subjectOf2;
    }

    /**
     * Sets the value of the subjectOf2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT290000UV06Subject }{@code >}
     *     
     */
    public void setSubjectOf2(JAXBElement<COCTMT290000UV06Subject> value) {
        this.subjectOf2 = value;
    }

    /**
     * Gets the value of the componentOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT290000UV06Component1 }
     * 
     * 
     */
    public List<COCTMT290000UV06Component1> getComponentOf() {
        if (componentOf == null) {
            componentOf = new ArrayList<COCTMT290000UV06Component1>();
        }
        return this.componentOf;
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

}
