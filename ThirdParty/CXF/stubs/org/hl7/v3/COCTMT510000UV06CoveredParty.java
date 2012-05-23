
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
 * <p>Java class for COCT_MT510000UV06.CoveredParty complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT510000UV06.CoveredParty">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="addr" type="{urn:hl7-org:v3}AD_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="telecom" type="{urn:hl7-org:v3}TEL_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="coveredOrganization1" type="{urn:hl7-org:v3}COCT_MT150000UV02.Organization" minOccurs="0"/>
 *           &lt;choice>
 *             &lt;element name="coveredPerson" type="{urn:hl7-org:v3}COCT_MT030007UV.Person" minOccurs="0"/>
 *             &lt;element name="coveredNonPersonLivingSubject" type="{urn:hl7-org:v3}COCT_MT030007UV.NonPersonLivingSubject" minOccurs="0"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *         &lt;element name="underwritingOrganization" type="{urn:hl7-org:v3}COCT_MT150000UV02.Organization" minOccurs="0"/>
 *         &lt;element name="subjectOf1" type="{urn:hl7-org:v3}COCT_MT510000UV06.Subject" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subjectOf2" type="{urn:hl7-org:v3}COCT_MT510000UV06.Subject3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="part" type="{urn:hl7-org:v3}COCT_MT510000UV06.Part" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="indirectAuthority1" type="{urn:hl7-org:v3}COCT_MT510000UV06.IndirectAuthorithyOver" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="indirectAuthority2" type="{urn:hl7-org:v3}COCT_MT510000UV06.IndirectAuthorithyOver2" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}RoleClassCoveredParty" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT510000UV06.CoveredParty", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "addr",
    "telecom",
    "effectiveTime",
    "coveredOrganization1",
    "coveredPerson",
    "coveredNonPersonLivingSubject",
    "underwritingOrganization",
    "subjectOf1",
    "subjectOf2",
    "part",
    "indirectAuthority1",
    "indirectAuthority2"
})
public class COCTMT510000UV06CoveredParty {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected List<II> id;
    protected CE code;
    protected List<ADExplicit> addr;
    protected List<TELExplicit> telecom;
    protected IVLTSExplicit effectiveTime;
    @XmlElementRef(name = "coveredOrganization1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT150000UV02Organization> coveredOrganization1;
    @XmlElementRef(name = "coveredPerson", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT030007UVPerson> coveredPerson;
    @XmlElementRef(name = "coveredNonPersonLivingSubject", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT030007UVNonPersonLivingSubject> coveredNonPersonLivingSubject;
    @XmlElementRef(name = "underwritingOrganization", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT150000UV02Organization> underwritingOrganization;
    @XmlElement(nillable = true)
    protected List<COCTMT510000UV06Subject> subjectOf1;
    @XmlElement(nillable = true)
    protected List<COCTMT510000UV06Subject3> subjectOf2;
    @XmlElement(nillable = true)
    protected List<COCTMT510000UV06Part> part;
    @XmlElement(nillable = true)
    protected List<COCTMT510000UV06IndirectAuthorithyOver> indirectAuthority1;
    @XmlElementRef(name = "indirectAuthority2", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT510000UV06IndirectAuthorithyOver2> indirectAuthority2;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected String classCode;

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
     * Gets the value of the addr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ADExplicit }
     * 
     * 
     */
    public List<ADExplicit> getAddr() {
        if (addr == null) {
            addr = new ArrayList<ADExplicit>();
        }
        return this.addr;
    }

    /**
     * Gets the value of the telecom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telecom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelecom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TELExplicit }
     * 
     * 
     */
    public List<TELExplicit> getTelecom() {
        if (telecom == null) {
            telecom = new ArrayList<TELExplicit>();
        }
        return this.telecom;
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
     * Gets the value of the coveredOrganization1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT150000UV02Organization }{@code >}
     *     
     */
    public JAXBElement<COCTMT150000UV02Organization> getCoveredOrganization1() {
        return coveredOrganization1;
    }

    /**
     * Sets the value of the coveredOrganization1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT150000UV02Organization }{@code >}
     *     
     */
    public void setCoveredOrganization1(JAXBElement<COCTMT150000UV02Organization> value) {
        this.coveredOrganization1 = value;
    }

    /**
     * Gets the value of the coveredPerson property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVPerson }{@code >}
     *     
     */
    public JAXBElement<COCTMT030007UVPerson> getCoveredPerson() {
        return coveredPerson;
    }

    /**
     * Sets the value of the coveredPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVPerson }{@code >}
     *     
     */
    public void setCoveredPerson(JAXBElement<COCTMT030007UVPerson> value) {
        this.coveredPerson = value;
    }

    /**
     * Gets the value of the coveredNonPersonLivingSubject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVNonPersonLivingSubject }{@code >}
     *     
     */
    public JAXBElement<COCTMT030007UVNonPersonLivingSubject> getCoveredNonPersonLivingSubject() {
        return coveredNonPersonLivingSubject;
    }

    /**
     * Sets the value of the coveredNonPersonLivingSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT030007UVNonPersonLivingSubject }{@code >}
     *     
     */
    public void setCoveredNonPersonLivingSubject(JAXBElement<COCTMT030007UVNonPersonLivingSubject> value) {
        this.coveredNonPersonLivingSubject = value;
    }

    /**
     * Gets the value of the underwritingOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT150000UV02Organization }{@code >}
     *     
     */
    public JAXBElement<COCTMT150000UV02Organization> getUnderwritingOrganization() {
        return underwritingOrganization;
    }

    /**
     * Sets the value of the underwritingOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT150000UV02Organization }{@code >}
     *     
     */
    public void setUnderwritingOrganization(JAXBElement<COCTMT150000UV02Organization> value) {
        this.underwritingOrganization = value;
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
     * {@link COCTMT510000UV06Subject }
     * 
     * 
     */
    public List<COCTMT510000UV06Subject> getSubjectOf1() {
        if (subjectOf1 == null) {
            subjectOf1 = new ArrayList<COCTMT510000UV06Subject>();
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
     * {@link COCTMT510000UV06Subject3 }
     * 
     * 
     */
    public List<COCTMT510000UV06Subject3> getSubjectOf2() {
        if (subjectOf2 == null) {
            subjectOf2 = new ArrayList<COCTMT510000UV06Subject3>();
        }
        return this.subjectOf2;
    }

    /**
     * Gets the value of the part property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the part property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPart().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT510000UV06Part }
     * 
     * 
     */
    public List<COCTMT510000UV06Part> getPart() {
        if (part == null) {
            part = new ArrayList<COCTMT510000UV06Part>();
        }
        return this.part;
    }

    /**
     * Gets the value of the indirectAuthority1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indirectAuthority1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndirectAuthority1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT510000UV06IndirectAuthorithyOver }
     * 
     * 
     */
    public List<COCTMT510000UV06IndirectAuthorithyOver> getIndirectAuthority1() {
        if (indirectAuthority1 == null) {
            indirectAuthority1 = new ArrayList<COCTMT510000UV06IndirectAuthorithyOver>();
        }
        return this.indirectAuthority1;
    }

    /**
     * Gets the value of the indirectAuthority2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06IndirectAuthorithyOver2 }{@code >}
     *     
     */
    public JAXBElement<COCTMT510000UV06IndirectAuthorithyOver2> getIndirectAuthority2() {
        return indirectAuthority2;
    }

    /**
     * Sets the value of the indirectAuthority2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT510000UV06IndirectAuthorithyOver2 }{@code >}
     *     
     */
    public void setIndirectAuthority2(JAXBElement<COCTMT510000UV06IndirectAuthorithyOver2> value) {
        this.indirectAuthority2 = value;
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
        return classCode;
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

}
