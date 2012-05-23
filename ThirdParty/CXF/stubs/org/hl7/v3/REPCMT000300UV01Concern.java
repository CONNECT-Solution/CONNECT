
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
 * <p>Java class for REPC_MT000300UV01.Concern complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000300UV01.Concern">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CV" minOccurs="0"/>
 *         &lt;element name="subject1" type="{urn:hl7-org:v3}REPC_MT000300UV01.Subject2" minOccurs="0"/>
 *         &lt;element name="recordTarget" type="{urn:hl7-org:v3}REPC_MT000300UV01.RecordTarget" minOccurs="0"/>
 *         &lt;element name="responsibleParty" type="{urn:hl7-org:v3}REPC_MT000300UV01.ResponsibleParty2" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT000300UV01.Performer" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT000300UV01.Author" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT000300UV01.DataEnterer" minOccurs="0"/>
 *         &lt;element name="informant" type="{urn:hl7-org:v3}REPC_MT000300UV01.Informant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT000300UV01.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}REPC_MT000300UV01.Location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="links" type="{urn:hl7-org:v3}REPC_MT000300UV01.Links" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="replacementOf" type="{urn:hl7-org:v3}REPC_MT000300UV01.ReplacementOf" minOccurs="0"/>
 *         &lt;element name="sequelTo" type="{urn:hl7-org:v3}REPC_MT000300UV01.SequelTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject2" type="{urn:hl7-org:v3}REPC_MT000300UV01.Subject4" maxOccurs="unbounded"/>
 *         &lt;element name="reference" type="{urn:hl7-org:v3}REPC_MT000300UV01.Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="component" type="{urn:hl7-org:v3}REPC_MT000300UV01.Component" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reasonOf" type="{urn:hl7-org:v3}REPC_MT000300UV01.Reason" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}REPC_MT000300UV01.Subject5" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}REPC_MT000300UV01.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassRoot" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}ActMood" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000300UV01.Concern", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "statusCode",
    "effectiveTime",
    "confidentialityCode",
    "subject1",
    "recordTarget",
    "responsibleParty",
    "performer",
    "author",
    "dataEnterer",
    "informant",
    "verifier",
    "location",
    "links",
    "replacementOf",
    "sequelTo",
    "subject2",
    "reference",
    "component",
    "reasonOf",
    "subjectOf1",
    "subjectOf2"
})
public class REPCMT000300UV01Concern {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    @XmlElement(required = true)
    protected CS statusCode;
    protected IVLTSExplicit effectiveTime;
    protected CV confidentialityCode;
    @XmlElementRef(name = "subject1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01Subject2> subject1;
    @XmlElementRef(name = "recordTarget", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01RecordTarget> recordTarget;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01ResponsibleParty2> responsibleParty;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Performer> performer;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Author> author;
    @XmlElementRef(name = "dataEnterer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01DataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Informant> informant;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Verifier> verifier;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Location> location;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Links> links;
    @XmlElementRef(name = "replacementOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01ReplacementOf> replacementOf;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01SequelTo> sequelTo;
    @XmlElement(required = true)
    protected List<REPCMT000300UV01Subject4> subject2;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Reference> reference;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Component> component;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Reason> reasonOf;
    @XmlElementRef(name = "subjectOf1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000300UV01Subject5> subjectOf1;
    @XmlElement(nillable = true)
    protected List<REPCMT000300UV01Subject3> subjectOf2;
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
     * Gets the value of the subject1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01Subject2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01Subject2> getSubject1() {
        return subject1;
    }

    /**
     * Sets the value of the subject1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01Subject2 }{@code >}
     *     
     */
    public void setSubject1(JAXBElement<REPCMT000300UV01Subject2> value) {
        this.subject1 = value;
    }

    /**
     * Gets the value of the recordTarget property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01RecordTarget }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01RecordTarget> getRecordTarget() {
        return recordTarget;
    }

    /**
     * Sets the value of the recordTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01RecordTarget }{@code >}
     *     
     */
    public void setRecordTarget(JAXBElement<REPCMT000300UV01RecordTarget> value) {
        this.recordTarget = value;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01ResponsibleParty2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01ResponsibleParty2> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01ResponsibleParty2 }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<REPCMT000300UV01ResponsibleParty2> value) {
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
     * {@link REPCMT000300UV01Performer }
     * 
     * 
     */
    public List<REPCMT000300UV01Performer> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT000300UV01Performer>();
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
     * {@link REPCMT000300UV01Author }
     * 
     * 
     */
    public List<REPCMT000300UV01Author> getAuthor() {
        if (author == null) {
            author = new ArrayList<REPCMT000300UV01Author>();
        }
        return this.author;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01DataEnterer }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01DataEnterer> getDataEnterer() {
        return dataEnterer;
    }

    /**
     * Sets the value of the dataEnterer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01DataEnterer }{@code >}
     *     
     */
    public void setDataEnterer(JAXBElement<REPCMT000300UV01DataEnterer> value) {
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
     * {@link REPCMT000300UV01Informant }
     * 
     * 
     */
    public List<REPCMT000300UV01Informant> getInformant() {
        if (informant == null) {
            informant = new ArrayList<REPCMT000300UV01Informant>();
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
     * {@link REPCMT000300UV01Verifier }
     * 
     * 
     */
    public List<REPCMT000300UV01Verifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT000300UV01Verifier>();
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
     * {@link REPCMT000300UV01Location }
     * 
     * 
     */
    public List<REPCMT000300UV01Location> getLocation() {
        if (location == null) {
            location = new ArrayList<REPCMT000300UV01Location>();
        }
        return this.location;
    }

    /**
     * Gets the value of the links property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the links property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinks().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000300UV01Links }
     * 
     * 
     */
    public List<REPCMT000300UV01Links> getLinks() {
        if (links == null) {
            links = new ArrayList<REPCMT000300UV01Links>();
        }
        return this.links;
    }

    /**
     * Gets the value of the replacementOf property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01ReplacementOf }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01ReplacementOf> getReplacementOf() {
        return replacementOf;
    }

    /**
     * Sets the value of the replacementOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01ReplacementOf }{@code >}
     *     
     */
    public void setReplacementOf(JAXBElement<REPCMT000300UV01ReplacementOf> value) {
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
     * {@link REPCMT000300UV01SequelTo }
     * 
     * 
     */
    public List<REPCMT000300UV01SequelTo> getSequelTo() {
        if (sequelTo == null) {
            sequelTo = new ArrayList<REPCMT000300UV01SequelTo>();
        }
        return this.sequelTo;
    }

    /**
     * Gets the value of the subject2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subject2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubject2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000300UV01Subject4 }
     * 
     * 
     */
    public List<REPCMT000300UV01Subject4> getSubject2() {
        if (subject2 == null) {
            subject2 = new ArrayList<REPCMT000300UV01Subject4>();
        }
        return this.subject2;
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
     * {@link REPCMT000300UV01Reference }
     * 
     * 
     */
    public List<REPCMT000300UV01Reference> getReference() {
        if (reference == null) {
            reference = new ArrayList<REPCMT000300UV01Reference>();
        }
        return this.reference;
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
     * {@link REPCMT000300UV01Component }
     * 
     * 
     */
    public List<REPCMT000300UV01Component> getComponent() {
        if (component == null) {
            component = new ArrayList<REPCMT000300UV01Component>();
        }
        return this.component;
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
     * {@link REPCMT000300UV01Reason }
     * 
     * 
     */
    public List<REPCMT000300UV01Reason> getReasonOf() {
        if (reasonOf == null) {
            reasonOf = new ArrayList<REPCMT000300UV01Reason>();
        }
        return this.reasonOf;
    }

    /**
     * Gets the value of the subjectOf1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01Subject5 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000300UV01Subject5> getSubjectOf1() {
        return subjectOf1;
    }

    /**
     * Sets the value of the subjectOf1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000300UV01Subject5 }{@code >}
     *     
     */
    public void setSubjectOf1(JAXBElement<REPCMT000300UV01Subject5> value) {
        this.subjectOf1 = value;
    }

    /**
     * Gets the value of the subjectOf2 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf2 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf2().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000300UV01Subject3 }
     * 
     * 
     */
    public List<REPCMT000300UV01Subject3> getSubjectOf2() {
        if (subjectOf2 == null) {
            subjectOf2 = new ArrayList<REPCMT000300UV01Subject3>();
        }
        return this.subjectOf2;
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
