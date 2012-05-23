
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
 * <p>Java class for QUPC_MT040300UV01.ParameterList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QUPC_MT040300UV01.ParameterList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="careProvisionCode" type="{urn:hl7-org:v3}QUPC_MT040300UV01.CareProvisionCode" minOccurs="0"/>
 *         &lt;element name="careProvisionReason" type="{urn:hl7-org:v3}QUPC_MT040300UV01.CareProvisionReason" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="careRecordTimePeriod" type="{urn:hl7-org:v3}QUPC_MT040300UV01.CareRecordTimePeriod" minOccurs="0"/>
 *         &lt;element name="clinicalStatementTimePeriod" type="{urn:hl7-org:v3}QUPC_MT040300UV01.ClinicalStatementTimePeriod" minOccurs="0"/>
 *         &lt;element name="includeCarePlanAttachment" type="{urn:hl7-org:v3}QUPC_MT040300UV01.IncludeCarePlanAttachment" minOccurs="0"/>
 *         &lt;element name="maximumHistoryStatements" type="{urn:hl7-org:v3}QUPC_MT040300UV01.MaximumHistoryStatements" minOccurs="0"/>
 *         &lt;element name="patientAdministrativeGender" type="{urn:hl7-org:v3}QUPC_MT040300UV01.PatientAdministrativeGender" minOccurs="0"/>
 *         &lt;element name="patientBirthTime" type="{urn:hl7-org:v3}QUPC_MT040300UV01.PatientBirthTime" minOccurs="0"/>
 *         &lt;element name="patientId" type="{urn:hl7-org:v3}QUPC_MT040300UV01.PatientId"/>
 *         &lt;element name="patientName" type="{urn:hl7-org:v3}QUPC_MT040300UV01.PatientName" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QUPC_MT040300UV01.ParameterList", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "careProvisionCode",
    "careProvisionReason",
    "careRecordTimePeriod",
    "clinicalStatementTimePeriod",
    "includeCarePlanAttachment",
    "maximumHistoryStatements",
    "patientAdministrativeGender",
    "patientBirthTime",
    "patientId",
    "patientName"
})
public class QUPCMT040300UV01ParameterList {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElementRef(name = "careProvisionCode", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01CareProvisionCode> careProvisionCode;
    @XmlElement(nillable = true)
    protected List<QUPCMT040300UV01CareProvisionReason> careProvisionReason;
    @XmlElementRef(name = "careRecordTimePeriod", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01CareRecordTimePeriod> careRecordTimePeriod;
    @XmlElementRef(name = "clinicalStatementTimePeriod", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01ClinicalStatementTimePeriod> clinicalStatementTimePeriod;
    @XmlElementRef(name = "includeCarePlanAttachment", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01IncludeCarePlanAttachment> includeCarePlanAttachment;
    @XmlElementRef(name = "maximumHistoryStatements", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01MaximumHistoryStatements> maximumHistoryStatements;
    @XmlElementRef(name = "patientAdministrativeGender", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01PatientAdministrativeGender> patientAdministrativeGender;
    @XmlElementRef(name = "patientBirthTime", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01PatientBirthTime> patientBirthTime;
    @XmlElement(required = true)
    protected QUPCMT040300UV01PatientId patientId;
    @XmlElementRef(name = "patientName", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<QUPCMT040300UV01PatientName> patientName;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

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
     * Gets the value of the careProvisionCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01CareProvisionCode }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01CareProvisionCode> getCareProvisionCode() {
        return careProvisionCode;
    }

    /**
     * Sets the value of the careProvisionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01CareProvisionCode }{@code >}
     *     
     */
    public void setCareProvisionCode(JAXBElement<QUPCMT040300UV01CareProvisionCode> value) {
        this.careProvisionCode = value;
    }

    /**
     * Gets the value of the careProvisionReason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the careProvisionReason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCareProvisionReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QUPCMT040300UV01CareProvisionReason }
     * 
     * 
     */
    public List<QUPCMT040300UV01CareProvisionReason> getCareProvisionReason() {
        if (careProvisionReason == null) {
            careProvisionReason = new ArrayList<QUPCMT040300UV01CareProvisionReason>();
        }
        return this.careProvisionReason;
    }

    /**
     * Gets the value of the careRecordTimePeriod property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01CareRecordTimePeriod }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01CareRecordTimePeriod> getCareRecordTimePeriod() {
        return careRecordTimePeriod;
    }

    /**
     * Sets the value of the careRecordTimePeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01CareRecordTimePeriod }{@code >}
     *     
     */
    public void setCareRecordTimePeriod(JAXBElement<QUPCMT040300UV01CareRecordTimePeriod> value) {
        this.careRecordTimePeriod = value;
    }

    /**
     * Gets the value of the clinicalStatementTimePeriod property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01ClinicalStatementTimePeriod }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01ClinicalStatementTimePeriod> getClinicalStatementTimePeriod() {
        return clinicalStatementTimePeriod;
    }

    /**
     * Sets the value of the clinicalStatementTimePeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01ClinicalStatementTimePeriod }{@code >}
     *     
     */
    public void setClinicalStatementTimePeriod(JAXBElement<QUPCMT040300UV01ClinicalStatementTimePeriod> value) {
        this.clinicalStatementTimePeriod = value;
    }

    /**
     * Gets the value of the includeCarePlanAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01IncludeCarePlanAttachment }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01IncludeCarePlanAttachment> getIncludeCarePlanAttachment() {
        return includeCarePlanAttachment;
    }

    /**
     * Sets the value of the includeCarePlanAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01IncludeCarePlanAttachment }{@code >}
     *     
     */
    public void setIncludeCarePlanAttachment(JAXBElement<QUPCMT040300UV01IncludeCarePlanAttachment> value) {
        this.includeCarePlanAttachment = value;
    }

    /**
     * Gets the value of the maximumHistoryStatements property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01MaximumHistoryStatements }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01MaximumHistoryStatements> getMaximumHistoryStatements() {
        return maximumHistoryStatements;
    }

    /**
     * Sets the value of the maximumHistoryStatements property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01MaximumHistoryStatements }{@code >}
     *     
     */
    public void setMaximumHistoryStatements(JAXBElement<QUPCMT040300UV01MaximumHistoryStatements> value) {
        this.maximumHistoryStatements = value;
    }

    /**
     * Gets the value of the patientAdministrativeGender property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientAdministrativeGender }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01PatientAdministrativeGender> getPatientAdministrativeGender() {
        return patientAdministrativeGender;
    }

    /**
     * Sets the value of the patientAdministrativeGender property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientAdministrativeGender }{@code >}
     *     
     */
    public void setPatientAdministrativeGender(JAXBElement<QUPCMT040300UV01PatientAdministrativeGender> value) {
        this.patientAdministrativeGender = value;
    }

    /**
     * Gets the value of the patientBirthTime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientBirthTime }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01PatientBirthTime> getPatientBirthTime() {
        return patientBirthTime;
    }

    /**
     * Sets the value of the patientBirthTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientBirthTime }{@code >}
     *     
     */
    public void setPatientBirthTime(JAXBElement<QUPCMT040300UV01PatientBirthTime> value) {
        this.patientBirthTime = value;
    }

    /**
     * Gets the value of the patientId property.
     * 
     * @return
     *     possible object is
     *     {@link QUPCMT040300UV01PatientId }
     *     
     */
    public QUPCMT040300UV01PatientId getPatientId() {
        return patientId;
    }

    /**
     * Sets the value of the patientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link QUPCMT040300UV01PatientId }
     *     
     */
    public void setPatientId(QUPCMT040300UV01PatientId value) {
        this.patientId = value;
    }

    /**
     * Gets the value of the patientName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientName }{@code >}
     *     
     */
    public JAXBElement<QUPCMT040300UV01PatientName> getPatientName() {
        return patientName;
    }

    /**
     * Sets the value of the patientName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link QUPCMT040300UV01PatientName }{@code >}
     *     
     */
    public void setPatientName(JAXBElement<QUPCMT040300UV01PatientName> value) {
        this.patientName = value;
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

}
