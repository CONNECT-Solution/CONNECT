
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for REPC_MT000300UV01.Subject2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000300UV01.Subject2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="awarenessCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="patient1" type="{urn:hl7-org:v3}COCT_MT050000UV01.Patient"/>
 *           &lt;element name="maintainedEntity1" type="{urn:hl7-org:v3}REPC_MT000700UV01.MaintainedEntity"/>
 *           &lt;choice>
 *             &lt;element name="employee" type="{urn:hl7-org:v3}COCT_MT910000UV.Employee"/>
 *             &lt;element name="student" type="{urn:hl7-org:v3}COCT_MT910000UV.Student"/>
 *             &lt;element name="personalRelationship" type="{urn:hl7-org:v3}COCT_MT910000UV.PersonalRelationship"/>
 *             &lt;element name="careGiver" type="{urn:hl7-org:v3}COCT_MT910000UV.CareGiver"/>
 *             &lt;element name="responsibleParty" type="{urn:hl7-org:v3}COCT_MT040200UV01.ResponsibleParty"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="typeCode" type="{urn:hl7-org:v3}ParticipationTargetSubject" fixed="SBJ" />
 *       &lt;attribute name="contextControlCode" type="{urn:hl7-org:v3}ContextControl" default="OP" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000300UV01.Subject2", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "awarenessCode",
    "patient1",
    "maintainedEntity1",
    "employee",
    "student",
    "personalRelationship",
    "careGiver",
    "responsibleParty"
})
public class REPCMT000300UV01Subject2 {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected CE awarenessCode;
    @XmlElementRef(name = "patient1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT050000UV01Patient> patient1;
    @XmlElementRef(name = "maintainedEntity1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000700UV01MaintainedEntity> maintainedEntity1;
    @XmlElementRef(name = "employee", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT910000UVEmployee> employee;
    @XmlElementRef(name = "student", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT910000UVStudent> student;
    @XmlElementRef(name = "personalRelationship", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT910000UVPersonalRelationship> personalRelationship;
    @XmlElementRef(name = "careGiver", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT910000UVCareGiver> careGiver;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT040200UV01ResponsibleParty> responsibleParty;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "typeCode")
    protected ParticipationTargetSubject typeCode;
    @XmlAttribute(name = "contextControlCode")
    protected String contextControlCode;

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
     * Gets the value of the awarenessCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getAwarenessCode() {
        return awarenessCode;
    }

    /**
     * Sets the value of the awarenessCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setAwarenessCode(CE value) {
        this.awarenessCode = value;
    }

    /**
     * Gets the value of the patient1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT050000UV01Patient }{@code >}
     *     
     */
    public JAXBElement<COCTMT050000UV01Patient> getPatient1() {
        return patient1;
    }

    /**
     * Sets the value of the patient1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT050000UV01Patient }{@code >}
     *     
     */
    public void setPatient1(JAXBElement<COCTMT050000UV01Patient> value) {
        this.patient1 = value;
    }

    /**
     * Gets the value of the maintainedEntity1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000700UV01MaintainedEntity }{@code >}
     *     
     */
    public JAXBElement<REPCMT000700UV01MaintainedEntity> getMaintainedEntity1() {
        return maintainedEntity1;
    }

    /**
     * Sets the value of the maintainedEntity1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000700UV01MaintainedEntity }{@code >}
     *     
     */
    public void setMaintainedEntity1(JAXBElement<REPCMT000700UV01MaintainedEntity> value) {
        this.maintainedEntity1 = value;
    }

    /**
     * Gets the value of the employee property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVEmployee }{@code >}
     *     
     */
    public JAXBElement<COCTMT910000UVEmployee> getEmployee() {
        return employee;
    }

    /**
     * Sets the value of the employee property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVEmployee }{@code >}
     *     
     */
    public void setEmployee(JAXBElement<COCTMT910000UVEmployee> value) {
        this.employee = value;
    }

    /**
     * Gets the value of the student property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVStudent }{@code >}
     *     
     */
    public JAXBElement<COCTMT910000UVStudent> getStudent() {
        return student;
    }

    /**
     * Sets the value of the student property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVStudent }{@code >}
     *     
     */
    public void setStudent(JAXBElement<COCTMT910000UVStudent> value) {
        this.student = value;
    }

    /**
     * Gets the value of the personalRelationship property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVPersonalRelationship }{@code >}
     *     
     */
    public JAXBElement<COCTMT910000UVPersonalRelationship> getPersonalRelationship() {
        return personalRelationship;
    }

    /**
     * Sets the value of the personalRelationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVPersonalRelationship }{@code >}
     *     
     */
    public void setPersonalRelationship(JAXBElement<COCTMT910000UVPersonalRelationship> value) {
        this.personalRelationship = value;
    }

    /**
     * Gets the value of the careGiver property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVCareGiver }{@code >}
     *     
     */
    public JAXBElement<COCTMT910000UVCareGiver> getCareGiver() {
        return careGiver;
    }

    /**
     * Sets the value of the careGiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT910000UVCareGiver }{@code >}
     *     
     */
    public void setCareGiver(JAXBElement<COCTMT910000UVCareGiver> value) {
        this.careGiver = value;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT040200UV01ResponsibleParty }{@code >}
     *     
     */
    public JAXBElement<COCTMT040200UV01ResponsibleParty> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT040200UV01ResponsibleParty }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<COCTMT040200UV01ResponsibleParty> value) {
        this.responsibleParty = value;
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
     * Gets the value of the typeCode property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipationTargetSubject }
     *     
     */
    public ParticipationTargetSubject getTypeCode() {
        if (typeCode == null) {
            return ParticipationTargetSubject.SBJ;
        } else {
            return typeCode;
        }
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipationTargetSubject }
     *     
     */
    public void setTypeCode(ParticipationTargetSubject value) {
        this.typeCode = value;
    }

    /**
     * Gets the value of the contextControlCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextControlCode() {
        if (contextControlCode == null) {
            return "OP";
        } else {
            return contextControlCode;
        }
    }

    /**
     * Sets the value of the contextControlCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextControlCode(String value) {
        this.contextControlCode = value;
    }

}
