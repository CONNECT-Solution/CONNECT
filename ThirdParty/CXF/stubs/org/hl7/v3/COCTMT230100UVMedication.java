
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
 * <p>Java class for COCT_MT230100UV.Medication complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT230100UV.Medication">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="administerableMedicine" type="{urn:hl7-org:v3}COCT_MT230100UV.Medicine"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}COCT_MT230100UV.Subject2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}COCT_MT230100UV.Subject1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf3" type="{urn:hl7-org:v3}COCT_MT230100UV.Subject22" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf4" type="{urn:hl7-org:v3}COCT_MT230100UV.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf5" type="{urn:hl7-org:v3}COCT_MT230100UV.Subject7" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}RoleClass" fixed="ADMM" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT230100UV.Medication", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "administerableMedicine",
    "subjectOf1",
    "subjectOf2",
    "subjectOf3",
    "subjectOf4",
    "subjectOf5"
})
public class COCTMT230100UVMedication {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected COCTMT230100UVMedicine administerableMedicine;
    @XmlElement(nillable = true)
    protected List<COCTMT230100UVSubject2> subjectOf1;
    @XmlElement(nillable = true)
    protected List<COCTMT230100UVSubject1> subjectOf2;
    @XmlElement(nillable = true)
    protected List<COCTMT230100UVSubject22> subjectOf3;
    @XmlElement(nillable = true)
    protected List<COCTMT230100UVSubject3> subjectOf4;
    @XmlElementRef(name = "subjectOf5", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT230100UVSubject7> subjectOf5;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;

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
     * Gets the value of the administerableMedicine property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT230100UVMedicine }
     *     
     */
    public COCTMT230100UVMedicine getAdministerableMedicine() {
        return administerableMedicine;
    }

    /**
     * Sets the value of the administerableMedicine property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT230100UVMedicine }
     *     
     */
    public void setAdministerableMedicine(COCTMT230100UVMedicine value) {
        this.administerableMedicine = value;
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
     * {@link COCTMT230100UVSubject2 }
     * 
     * 
     */
    public List<COCTMT230100UVSubject2> getSubjectOf1() {
        if (subjectOf1 == null) {
            subjectOf1 = new ArrayList<COCTMT230100UVSubject2>();
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
     * {@link COCTMT230100UVSubject1 }
     * 
     * 
     */
    public List<COCTMT230100UVSubject1> getSubjectOf2() {
        if (subjectOf2 == null) {
            subjectOf2 = new ArrayList<COCTMT230100UVSubject1>();
        }
        return this.subjectOf2;
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
     * {@link COCTMT230100UVSubject22 }
     * 
     * 
     */
    public List<COCTMT230100UVSubject22> getSubjectOf3() {
        if (subjectOf3 == null) {
            subjectOf3 = new ArrayList<COCTMT230100UVSubject22>();
        }
        return this.subjectOf3;
    }

    /**
     * Gets the value of the subjectOf4 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjectOf4 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubjectOf4().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT230100UVSubject3 }
     * 
     * 
     */
    public List<COCTMT230100UVSubject3> getSubjectOf4() {
        if (subjectOf4 == null) {
            subjectOf4 = new ArrayList<COCTMT230100UVSubject3>();
        }
        return this.subjectOf4;
    }

    /**
     * Gets the value of the subjectOf5 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT230100UVSubject7 }{@code >}
     *     
     */
    public JAXBElement<COCTMT230100UVSubject7> getSubjectOf5() {
        return subjectOf5;
    }

    /**
     * Sets the value of the subjectOf5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT230100UVSubject7 }{@code >}
     *     
     */
    public void setSubjectOf5(JAXBElement<COCTMT230100UVSubject7> value) {
        this.subjectOf5 = value;
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

}
