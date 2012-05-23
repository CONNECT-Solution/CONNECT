
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
 * <p>Java class for COCT_MT030007UV.Person complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT030007UV.Person">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="name" type="{urn:hl7-org:v3}EN_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="desc" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="administrativeGenderCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="birthTime" type="{urn:hl7-org:v3}TS_explicit" minOccurs="0"/>
 *         &lt;element name="deceasedInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="deceasedTime" type="{urn:hl7-org:v3}TS_explicit" minOccurs="0"/>
 *         &lt;element name="multipleBirthInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="multipleBirthOrderNumber" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="organDonorInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;element name="maritalStatusCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="educationLevelCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="disabilityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="livingArrangementCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="religiousAffiliationCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="raceCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ethnicGroupCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asEmployment" type="{urn:hl7-org:v3}COCT_MT030007UV.Employment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asCitizen" type="{urn:hl7-org:v3}COCT_MT030007UV.Citizen" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asStudent" type="{urn:hl7-org:v3}COCT_MT030007UV.Student" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asMember" type="{urn:hl7-org:v3}COCT_MT030007UV.Member" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asOtherIDs" type="{urn:hl7-org:v3}COCT_MT030007UV.OtherIDs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactParty" type="{urn:hl7-org:v3}COCT_MT030007UV.ContactParty" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="guardian" type="{urn:hl7-org:v3}COCT_MT030007UV.Guardian" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="guarantor" type="{urn:hl7-org:v3}COCT_MT030007UV.Guarantor" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="birthPlace" type="{urn:hl7-org:v3}COCT_MT030007UV.BirthPlace" minOccurs="0"/>
 *         &lt;element name="languageCommunication" type="{urn:hl7-org:v3}COCT_MT030007UV.LanguageCommunication" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}EntityClass" fixed="PSN" />
 *       &lt;attribute name="determinerCode" use="required" type="{urn:hl7-org:v3}x_DeterminerInstanceKind" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT030007UV.Person", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "name",
    "desc",
    "statusCode",
    "administrativeGenderCode",
    "birthTime",
    "deceasedInd",
    "deceasedTime",
    "multipleBirthInd",
    "multipleBirthOrderNumber",
    "organDonorInd",
    "maritalStatusCode",
    "educationLevelCode",
    "disabilityCode",
    "livingArrangementCode",
    "religiousAffiliationCode",
    "raceCode",
    "ethnicGroupCode",
    "asEmployment",
    "asCitizen",
    "asStudent",
    "asMember",
    "asOtherIDs",
    "contactParty",
    "guardian",
    "guarantor",
    "birthPlace",
    "languageCommunication"
})
public class COCTMT030007UVPerson {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected List<ENExplicit> name;
    protected EDExplicit desc;
    protected CS statusCode;
    protected CE administrativeGenderCode;
    protected TSExplicit birthTime;
    protected BL deceasedInd;
    protected TSExplicit deceasedTime;
    protected BL multipleBirthInd;
    protected INT multipleBirthOrderNumber;
    protected BL organDonorInd;
    protected CE maritalStatusCode;
    protected CE educationLevelCode;
    protected List<CE> disabilityCode;
    protected CE livingArrangementCode;
    protected CE religiousAffiliationCode;
    protected List<CE> raceCode;
    protected List<CE> ethnicGroupCode;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVEmployment> asEmployment;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVCitizen> asCitizen;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVStudent> asStudent;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVMember> asMember;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVOtherIDs> asOtherIDs;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVContactParty> contactParty;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVGuardian> guardian;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVGuarantor> guarantor;
    @XmlElementRef(name = "birthPlace", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT030007UVBirthPlace> birthPlace;
    @XmlElement(nillable = true)
    protected List<COCTMT030007UVLanguageCommunication> languageCommunication;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;
    @XmlAttribute(name = "determinerCode", required = true)
    protected XDeterminerInstanceKind determinerCode;

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
     * Gets the value of the name property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the name property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ENExplicit }
     * 
     * 
     */
    public List<ENExplicit> getName() {
        if (name == null) {
            name = new ArrayList<ENExplicit>();
        }
        return this.name;
    }

    /**
     * Gets the value of the desc property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getDesc() {
        return desc;
    }

    /**
     * Sets the value of the desc property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setDesc(EDExplicit value) {
        this.desc = value;
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
     * Gets the value of the administrativeGenderCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getAdministrativeGenderCode() {
        return administrativeGenderCode;
    }

    /**
     * Sets the value of the administrativeGenderCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setAdministrativeGenderCode(CE value) {
        this.administrativeGenderCode = value;
    }

    /**
     * Gets the value of the birthTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getBirthTime() {
        return birthTime;
    }

    /**
     * Sets the value of the birthTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setBirthTime(TSExplicit value) {
        this.birthTime = value;
    }

    /**
     * Gets the value of the deceasedInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getDeceasedInd() {
        return deceasedInd;
    }

    /**
     * Sets the value of the deceasedInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setDeceasedInd(BL value) {
        this.deceasedInd = value;
    }

    /**
     * Gets the value of the deceasedTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getDeceasedTime() {
        return deceasedTime;
    }

    /**
     * Sets the value of the deceasedTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setDeceasedTime(TSExplicit value) {
        this.deceasedTime = value;
    }

    /**
     * Gets the value of the multipleBirthInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getMultipleBirthInd() {
        return multipleBirthInd;
    }

    /**
     * Sets the value of the multipleBirthInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setMultipleBirthInd(BL value) {
        this.multipleBirthInd = value;
    }

    /**
     * Gets the value of the multipleBirthOrderNumber property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getMultipleBirthOrderNumber() {
        return multipleBirthOrderNumber;
    }

    /**
     * Sets the value of the multipleBirthOrderNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setMultipleBirthOrderNumber(INT value) {
        this.multipleBirthOrderNumber = value;
    }

    /**
     * Gets the value of the organDonorInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getOrganDonorInd() {
        return organDonorInd;
    }

    /**
     * Sets the value of the organDonorInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setOrganDonorInd(BL value) {
        this.organDonorInd = value;
    }

    /**
     * Gets the value of the maritalStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getMaritalStatusCode() {
        return maritalStatusCode;
    }

    /**
     * Sets the value of the maritalStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setMaritalStatusCode(CE value) {
        this.maritalStatusCode = value;
    }

    /**
     * Gets the value of the educationLevelCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getEducationLevelCode() {
        return educationLevelCode;
    }

    /**
     * Sets the value of the educationLevelCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setEducationLevelCode(CE value) {
        this.educationLevelCode = value;
    }

    /**
     * Gets the value of the disabilityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disabilityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisabilityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getDisabilityCode() {
        if (disabilityCode == null) {
            disabilityCode = new ArrayList<CE>();
        }
        return this.disabilityCode;
    }

    /**
     * Gets the value of the livingArrangementCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getLivingArrangementCode() {
        return livingArrangementCode;
    }

    /**
     * Sets the value of the livingArrangementCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setLivingArrangementCode(CE value) {
        this.livingArrangementCode = value;
    }

    /**
     * Gets the value of the religiousAffiliationCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getReligiousAffiliationCode() {
        return religiousAffiliationCode;
    }

    /**
     * Sets the value of the religiousAffiliationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setReligiousAffiliationCode(CE value) {
        this.religiousAffiliationCode = value;
    }

    /**
     * Gets the value of the raceCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the raceCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRaceCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getRaceCode() {
        if (raceCode == null) {
            raceCode = new ArrayList<CE>();
        }
        return this.raceCode;
    }

    /**
     * Gets the value of the ethnicGroupCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ethnicGroupCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEthnicGroupCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getEthnicGroupCode() {
        if (ethnicGroupCode == null) {
            ethnicGroupCode = new ArrayList<CE>();
        }
        return this.ethnicGroupCode;
    }

    /**
     * Gets the value of the asEmployment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asEmployment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsEmployment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVEmployment }
     * 
     * 
     */
    public List<COCTMT030007UVEmployment> getAsEmployment() {
        if (asEmployment == null) {
            asEmployment = new ArrayList<COCTMT030007UVEmployment>();
        }
        return this.asEmployment;
    }

    /**
     * Gets the value of the asCitizen property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asCitizen property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsCitizen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVCitizen }
     * 
     * 
     */
    public List<COCTMT030007UVCitizen> getAsCitizen() {
        if (asCitizen == null) {
            asCitizen = new ArrayList<COCTMT030007UVCitizen>();
        }
        return this.asCitizen;
    }

    /**
     * Gets the value of the asStudent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asStudent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsStudent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVStudent }
     * 
     * 
     */
    public List<COCTMT030007UVStudent> getAsStudent() {
        if (asStudent == null) {
            asStudent = new ArrayList<COCTMT030007UVStudent>();
        }
        return this.asStudent;
    }

    /**
     * Gets the value of the asMember property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asMember property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsMember().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVMember }
     * 
     * 
     */
    public List<COCTMT030007UVMember> getAsMember() {
        if (asMember == null) {
            asMember = new ArrayList<COCTMT030007UVMember>();
        }
        return this.asMember;
    }

    /**
     * Gets the value of the asOtherIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asOtherIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsOtherIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVOtherIDs }
     * 
     * 
     */
    public List<COCTMT030007UVOtherIDs> getAsOtherIDs() {
        if (asOtherIDs == null) {
            asOtherIDs = new ArrayList<COCTMT030007UVOtherIDs>();
        }
        return this.asOtherIDs;
    }

    /**
     * Gets the value of the contactParty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactParty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactParty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVContactParty }
     * 
     * 
     */
    public List<COCTMT030007UVContactParty> getContactParty() {
        if (contactParty == null) {
            contactParty = new ArrayList<COCTMT030007UVContactParty>();
        }
        return this.contactParty;
    }

    /**
     * Gets the value of the guardian property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guardian property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuardian().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVGuardian }
     * 
     * 
     */
    public List<COCTMT030007UVGuardian> getGuardian() {
        if (guardian == null) {
            guardian = new ArrayList<COCTMT030007UVGuardian>();
        }
        return this.guardian;
    }

    /**
     * Gets the value of the guarantor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guarantor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuarantor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVGuarantor }
     * 
     * 
     */
    public List<COCTMT030007UVGuarantor> getGuarantor() {
        if (guarantor == null) {
            guarantor = new ArrayList<COCTMT030007UVGuarantor>();
        }
        return this.guarantor;
    }

    /**
     * Gets the value of the birthPlace property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVBirthPlace }{@code >}
     *     
     */
    public JAXBElement<COCTMT030007UVBirthPlace> getBirthPlace() {
        return birthPlace;
    }

    /**
     * Sets the value of the birthPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVBirthPlace }{@code >}
     *     
     */
    public void setBirthPlace(JAXBElement<COCTMT030007UVBirthPlace> value) {
        this.birthPlace = value;
    }

    /**
     * Gets the value of the languageCommunication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the languageCommunication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguageCommunication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT030007UVLanguageCommunication }
     * 
     * 
     */
    public List<COCTMT030007UVLanguageCommunication> getLanguageCommunication() {
        if (languageCommunication == null) {
            languageCommunication = new ArrayList<COCTMT030007UVLanguageCommunication>();
        }
        return this.languageCommunication;
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
     * Gets the value of the determinerCode property.
     * 
     * @return
     *     possible object is
     *     {@link XDeterminerInstanceKind }
     *     
     */
    public XDeterminerInstanceKind getDeterminerCode() {
        return determinerCode;
    }

    /**
     * Sets the value of the determinerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XDeterminerInstanceKind }
     *     
     */
    public void setDeterminerCode(XDeterminerInstanceKind value) {
        this.determinerCode = value;
    }

}
