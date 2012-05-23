
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
 * <p>Java class for COCT_MT080000UV.Specimen complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT080000UV.Specimen">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CE"/>
 *         &lt;choice>
 *           &lt;element name="specimenNatural" type="{urn:hl7-org:v3}COCT_MT080000UV.Natural"/>
 *           &lt;element name="specimenManufactured" type="{urn:hl7-org:v3}COCT_MT080000UV.Manufactured"/>
 *           &lt;element name="specimenNonPersonLivingSubject" type="{urn:hl7-org:v3}COCT_MT080000UV.NonPersonLivingSubject"/>
 *           &lt;element name="specimenPerson" type="{urn:hl7-org:v3}COCT_MT080000UV.Person"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="sourceNatural" type="{urn:hl7-org:v3}COCT_MT080000UV.Natural" minOccurs="0"/>
 *           &lt;element name="sourceManufactured" type="{urn:hl7-org:v3}COCT_MT080000UV.Manufactured" minOccurs="0"/>
 *           &lt;element name="sourceNonPersonLivingSubject" type="{urn:hl7-org:v3}COCT_MT080000UV.NonPersonLivingSubject" minOccurs="0"/>
 *           &lt;element name="sourcePerson" type="{urn:hl7-org:v3}COCT_MT080000UV.Person" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}COCT_MT080000UV.Subject4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}COCT_MT080000UV.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="productOf" type="{urn:hl7-org:v3}COCT_MT080000UV.Product" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}RoleClassSpecimen" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT080000UV.Specimen", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "specimenNatural",
    "specimenManufactured",
    "specimenNonPersonLivingSubject",
    "specimenPerson",
    "sourceNatural",
    "sourceManufactured",
    "sourceNonPersonLivingSubject",
    "sourcePerson",
    "subjectOf1",
    "subjectOf2",
    "productOf"
})
public class COCTMT080000UVSpecimen {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected II id;
    @XmlElement(required = true)
    protected CE code;
    protected COCTMT080000UVNatural specimenNatural;
    protected COCTMT080000UVManufactured specimenManufactured;
    protected COCTMT080000UVNonPersonLivingSubject specimenNonPersonLivingSubject;
    protected COCTMT080000UVPerson specimenPerson;
    @XmlElementRef(name = "sourceNatural", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVNatural> sourceNatural;
    @XmlElementRef(name = "sourceManufactured", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVManufactured> sourceManufactured;
    @XmlElementRef(name = "sourceNonPersonLivingSubject", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVNonPersonLivingSubject> sourceNonPersonLivingSubject;
    @XmlElementRef(name = "sourcePerson", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVPerson> sourcePerson;
    @XmlElement(nillable = true)
    protected List<COCTMT080000UVSubject4> subjectOf1;
    @XmlElement(nillable = true)
    protected List<COCTMT080000UVSubject3> subjectOf2;
    @XmlElementRef(name = "productOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVProduct> productOf;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected RoleClassSpecimen classCode;

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
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setCode(CE value) {
        this.code = value;
    }

    /**
     * Gets the value of the specimenNatural property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT080000UVNatural }
     *     
     */
    public COCTMT080000UVNatural getSpecimenNatural() {
        return specimenNatural;
    }

    /**
     * Sets the value of the specimenNatural property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT080000UVNatural }
     *     
     */
    public void setSpecimenNatural(COCTMT080000UVNatural value) {
        this.specimenNatural = value;
    }

    /**
     * Gets the value of the specimenManufactured property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT080000UVManufactured }
     *     
     */
    public COCTMT080000UVManufactured getSpecimenManufactured() {
        return specimenManufactured;
    }

    /**
     * Sets the value of the specimenManufactured property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT080000UVManufactured }
     *     
     */
    public void setSpecimenManufactured(COCTMT080000UVManufactured value) {
        this.specimenManufactured = value;
    }

    /**
     * Gets the value of the specimenNonPersonLivingSubject property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT080000UVNonPersonLivingSubject }
     *     
     */
    public COCTMT080000UVNonPersonLivingSubject getSpecimenNonPersonLivingSubject() {
        return specimenNonPersonLivingSubject;
    }

    /**
     * Sets the value of the specimenNonPersonLivingSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT080000UVNonPersonLivingSubject }
     *     
     */
    public void setSpecimenNonPersonLivingSubject(COCTMT080000UVNonPersonLivingSubject value) {
        this.specimenNonPersonLivingSubject = value;
    }

    /**
     * Gets the value of the specimenPerson property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT080000UVPerson }
     *     
     */
    public COCTMT080000UVPerson getSpecimenPerson() {
        return specimenPerson;
    }

    /**
     * Sets the value of the specimenPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT080000UVPerson }
     *     
     */
    public void setSpecimenPerson(COCTMT080000UVPerson value) {
        this.specimenPerson = value;
    }

    /**
     * Gets the value of the sourceNatural property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVNatural }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVNatural> getSourceNatural() {
        return sourceNatural;
    }

    /**
     * Sets the value of the sourceNatural property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVNatural }{@code >}
     *     
     */
    public void setSourceNatural(JAXBElement<COCTMT080000UVNatural> value) {
        this.sourceNatural = value;
    }

    /**
     * Gets the value of the sourceManufactured property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVManufactured }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVManufactured> getSourceManufactured() {
        return sourceManufactured;
    }

    /**
     * Sets the value of the sourceManufactured property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVManufactured }{@code >}
     *     
     */
    public void setSourceManufactured(JAXBElement<COCTMT080000UVManufactured> value) {
        this.sourceManufactured = value;
    }

    /**
     * Gets the value of the sourceNonPersonLivingSubject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVNonPersonLivingSubject }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVNonPersonLivingSubject> getSourceNonPersonLivingSubject() {
        return sourceNonPersonLivingSubject;
    }

    /**
     * Sets the value of the sourceNonPersonLivingSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVNonPersonLivingSubject }{@code >}
     *     
     */
    public void setSourceNonPersonLivingSubject(JAXBElement<COCTMT080000UVNonPersonLivingSubject> value) {
        this.sourceNonPersonLivingSubject = value;
    }

    /**
     * Gets the value of the sourcePerson property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVPerson }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVPerson> getSourcePerson() {
        return sourcePerson;
    }

    /**
     * Sets the value of the sourcePerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVPerson }{@code >}
     *     
     */
    public void setSourcePerson(JAXBElement<COCTMT080000UVPerson> value) {
        this.sourcePerson = value;
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
     * {@link COCTMT080000UVSubject4 }
     * 
     * 
     */
    public List<COCTMT080000UVSubject4> getSubjectOf1() {
        if (subjectOf1 == null) {
            subjectOf1 = new ArrayList<COCTMT080000UVSubject4>();
        }
        return this.subjectOf1;
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
     * {@link COCTMT080000UVSubject3 }
     * 
     * 
     */
    public List<COCTMT080000UVSubject3> getSubjectOf2() {
        if (subjectOf2 == null) {
            subjectOf2 = new ArrayList<COCTMT080000UVSubject3>();
        }
        return this.subjectOf2;
    }

    /**
     * Gets the value of the productOf property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVProduct }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVProduct> getProductOf() {
        return productOf;
    }

    /**
     * Sets the value of the productOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVProduct }{@code >}
     *     
     */
    public void setProductOf(JAXBElement<COCTMT080000UVProduct> value) {
        this.productOf = value;
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
     *     {@link RoleClassSpecimen }
     *     
     */
    public RoleClassSpecimen getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleClassSpecimen }
     *     
     */
    public void setClassCode(RoleClassSpecimen value) {
        this.classCode = value;
    }

}
