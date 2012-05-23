
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
 * <p>Java class for PRPA_MT900300UV02.QueryByParameterPayload complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PRPA_MT900300UV02.QueryByParameterPayload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="queryId" type="{urn:hl7-org:v3}II" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS"/>
 *         &lt;element name="modifyCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="responseElementGroupId" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="responseModalityCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="responsePriorityCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="initialQuantity" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="initialQuantityCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="careEventID" type="{urn:hl7-org:v3}PRPA_MT900300UV02.CareEventID" minOccurs="0"/>
 *         &lt;element name="encounterStatus" type="{urn:hl7-org:v3}PRPA_MT900300UV02.EncounterStatus" minOccurs="0"/>
 *         &lt;element name="encounterTimeframe" type="{urn:hl7-org:v3}PRPA_MT900300UV02.EncounterTimeframe" minOccurs="0"/>
 *         &lt;element name="patientId" type="{urn:hl7-org:v3}PRPA_MT900300UV02.PatientId"/>
 *         &lt;element name="responsibleOrganization" type="{urn:hl7-org:v3}PRPA_MT900300UV02.ResponsibleOrganization" minOccurs="0"/>
 *         &lt;element name="sortControl" type="{urn:hl7-org:v3}PRPA_MT900300UV02.SortControl" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="typeOfEncounter" type="{urn:hl7-org:v3}PRPA_MT900300UV02.TypeOfEncounter" minOccurs="0"/>
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
@XmlType(name = "PRPA_MT900300UV02.QueryByParameterPayload", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "queryId",
    "statusCode",
    "modifyCode",
    "responseElementGroupId",
    "responseModalityCode",
    "responsePriorityCode",
    "initialQuantity",
    "initialQuantityCode",
    "careEventID",
    "encounterStatus",
    "encounterTimeframe",
    "patientId",
    "responsibleOrganization",
    "sortControl",
    "typeOfEncounter"
})
public class PRPAMT900300UV02QueryByParameterPayload {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected II queryId;
    @XmlElement(required = true)
    protected CS statusCode;
    protected CS modifyCode;
    protected List<II> responseElementGroupId;
    protected CS responseModalityCode;
    protected CS responsePriorityCode;
    protected INT initialQuantity;
    protected CE initialQuantityCode;
    @XmlElementRef(name = "careEventID", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT900300UV02CareEventID> careEventID;
    @XmlElementRef(name = "encounterStatus", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT900300UV02EncounterStatus> encounterStatus;
    @XmlElementRef(name = "encounterTimeframe", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT900300UV02EncounterTimeframe> encounterTimeframe;
    @XmlElement(required = true)
    protected PRPAMT900300UV02PatientId patientId;
    @XmlElementRef(name = "responsibleOrganization", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT900300UV02ResponsibleOrganization> responsibleOrganization;
    @XmlElement(nillable = true)
    protected List<PRPAMT900300UV02SortControl> sortControl;
    @XmlElementRef(name = "typeOfEncounter", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<PRPAMT900300UV02TypeOfEncounter> typeOfEncounter;
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
     * Gets the value of the queryId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getQueryId() {
        return queryId;
    }

    /**
     * Sets the value of the queryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setQueryId(II value) {
        this.queryId = value;
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
     * Gets the value of the modifyCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getModifyCode() {
        return modifyCode;
    }

    /**
     * Sets the value of the modifyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setModifyCode(CS value) {
        this.modifyCode = value;
    }

    /**
     * Gets the value of the responseElementGroupId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseElementGroupId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseElementGroupId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getResponseElementGroupId() {
        if (responseElementGroupId == null) {
            responseElementGroupId = new ArrayList<II>();
        }
        return this.responseElementGroupId;
    }

    /**
     * Gets the value of the responseModalityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getResponseModalityCode() {
        return responseModalityCode;
    }

    /**
     * Sets the value of the responseModalityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setResponseModalityCode(CS value) {
        this.responseModalityCode = value;
    }

    /**
     * Gets the value of the responsePriorityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getResponsePriorityCode() {
        return responsePriorityCode;
    }

    /**
     * Sets the value of the responsePriorityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setResponsePriorityCode(CS value) {
        this.responsePriorityCode = value;
    }

    /**
     * Gets the value of the initialQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getInitialQuantity() {
        return initialQuantity;
    }

    /**
     * Sets the value of the initialQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setInitialQuantity(INT value) {
        this.initialQuantity = value;
    }

    /**
     * Gets the value of the initialQuantityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getInitialQuantityCode() {
        return initialQuantityCode;
    }

    /**
     * Sets the value of the initialQuantityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setInitialQuantityCode(CE value) {
        this.initialQuantityCode = value;
    }

    /**
     * Gets the value of the careEventID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02CareEventID }{@code >}
     *     
     */
    public JAXBElement<PRPAMT900300UV02CareEventID> getCareEventID() {
        return careEventID;
    }

    /**
     * Sets the value of the careEventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02CareEventID }{@code >}
     *     
     */
    public void setCareEventID(JAXBElement<PRPAMT900300UV02CareEventID> value) {
        this.careEventID = value;
    }

    /**
     * Gets the value of the encounterStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02EncounterStatus }{@code >}
     *     
     */
    public JAXBElement<PRPAMT900300UV02EncounterStatus> getEncounterStatus() {
        return encounterStatus;
    }

    /**
     * Sets the value of the encounterStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02EncounterStatus }{@code >}
     *     
     */
    public void setEncounterStatus(JAXBElement<PRPAMT900300UV02EncounterStatus> value) {
        this.encounterStatus = value;
    }

    /**
     * Gets the value of the encounterTimeframe property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02EncounterTimeframe }{@code >}
     *     
     */
    public JAXBElement<PRPAMT900300UV02EncounterTimeframe> getEncounterTimeframe() {
        return encounterTimeframe;
    }

    /**
     * Sets the value of the encounterTimeframe property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02EncounterTimeframe }{@code >}
     *     
     */
    public void setEncounterTimeframe(JAXBElement<PRPAMT900300UV02EncounterTimeframe> value) {
        this.encounterTimeframe = value;
    }

    /**
     * Gets the value of the patientId property.
     * 
     * @return
     *     possible object is
     *     {@link PRPAMT900300UV02PatientId }
     *     
     */
    public PRPAMT900300UV02PatientId getPatientId() {
        return patientId;
    }

    /**
     * Sets the value of the patientId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRPAMT900300UV02PatientId }
     *     
     */
    public void setPatientId(PRPAMT900300UV02PatientId value) {
        this.patientId = value;
    }

    /**
     * Gets the value of the responsibleOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02ResponsibleOrganization }{@code >}
     *     
     */
    public JAXBElement<PRPAMT900300UV02ResponsibleOrganization> getResponsibleOrganization() {
        return responsibleOrganization;
    }

    /**
     * Sets the value of the responsibleOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02ResponsibleOrganization }{@code >}
     *     
     */
    public void setResponsibleOrganization(JAXBElement<PRPAMT900300UV02ResponsibleOrganization> value) {
        this.responsibleOrganization = value;
    }

    /**
     * Gets the value of the sortControl property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sortControl property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSortControl().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PRPAMT900300UV02SortControl }
     * 
     * 
     */
    public List<PRPAMT900300UV02SortControl> getSortControl() {
        if (sortControl == null) {
            sortControl = new ArrayList<PRPAMT900300UV02SortControl>();
        }
        return this.sortControl;
    }

    /**
     * Gets the value of the typeOfEncounter property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02TypeOfEncounter }{@code >}
     *     
     */
    public JAXBElement<PRPAMT900300UV02TypeOfEncounter> getTypeOfEncounter() {
        return typeOfEncounter;
    }

    /**
     * Sets the value of the typeOfEncounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PRPAMT900300UV02TypeOfEncounter }{@code >}
     *     
     */
    public void setTypeOfEncounter(JAXBElement<PRPAMT900300UV02TypeOfEncounter> value) {
        this.typeOfEncounter = value;
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
