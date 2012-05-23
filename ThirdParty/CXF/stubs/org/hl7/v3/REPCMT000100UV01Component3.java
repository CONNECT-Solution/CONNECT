
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
 * <p>Java class for REPC_MT000100UV01.Component3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000100UV01.Component3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="sequenceNumber" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;element name="seperatableInd" type="{urn:hl7-org:v3}BL" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="actReference1" type="{urn:hl7-org:v3}REPC_MT000100UV01.ActReference"/>
 *           &lt;choice>
 *             &lt;element name="act" type="{urn:hl7-org:v3}REPC_MT000100UV01.Act"/>
 *             &lt;element name="encounter" type="{urn:hl7-org:v3}REPC_MT000100UV01.Encounter"/>
 *             &lt;element name="observation" type="{urn:hl7-org:v3}REPC_MT000100UV01.Observation"/>
 *             &lt;element name="organizer" type="{urn:hl7-org:v3}REPC_MT000100UV01.Organizer"/>
 *             &lt;element name="procedure" type="{urn:hl7-org:v3}REPC_MT000100UV01.Procedure"/>
 *             &lt;element name="substanceAdministration" type="{urn:hl7-org:v3}REPC_MT000100UV01.SubstanceAdministration"/>
 *             &lt;element name="supply" type="{urn:hl7-org:v3}REPC_MT000100UV01.Supply"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="typeCode" use="required" type="{urn:hl7-org:v3}ActRelationshipHasComponent" />
 *       &lt;attribute name="contextControlCode" type="{urn:hl7-org:v3}ContextControl" />
 *       &lt;attribute name="contextConductionInd" type="{urn:hl7-org:v3}bl" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000100UV01.Component3", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "sequenceNumber",
    "seperatableInd",
    "actReference1",
    "act",
    "encounter",
    "observation",
    "organizer",
    "procedure",
    "substanceAdministration",
    "supply"
})
public class REPCMT000100UV01Component3 {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected INT sequenceNumber;
    protected BL seperatableInd;
    @XmlElementRef(name = "actReference1", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01ActReference> actReference1;
    @XmlElementRef(name = "act", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Act> act;
    @XmlElementRef(name = "encounter", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Encounter> encounter;
    @XmlElementRef(name = "observation", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Observation> observation;
    @XmlElementRef(name = "organizer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Organizer> organizer;
    @XmlElementRef(name = "procedure", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Procedure> procedure;
    @XmlElementRef(name = "substanceAdministration", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01SubstanceAdministration> substanceAdministration;
    @XmlElementRef(name = "supply", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Supply> supply;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "typeCode", required = true)
    protected ActRelationshipHasComponent typeCode;
    @XmlAttribute(name = "contextControlCode")
    protected String contextControlCode;
    @XmlAttribute(name = "contextConductionInd")
    protected Boolean contextConductionInd;

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
     * Gets the value of the sequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link INT }
     *     
     */
    public INT getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the value of the sequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link INT }
     *     
     */
    public void setSequenceNumber(INT value) {
        this.sequenceNumber = value;
    }

    /**
     * Gets the value of the seperatableInd property.
     * 
     * @return
     *     possible object is
     *     {@link BL }
     *     
     */
    public BL getSeperatableInd() {
        return seperatableInd;
    }

    /**
     * Sets the value of the seperatableInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BL }
     *     
     */
    public void setSeperatableInd(BL value) {
        this.seperatableInd = value;
    }

    /**
     * Gets the value of the actReference1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ActReference }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01ActReference> getActReference1() {
        return actReference1;
    }

    /**
     * Sets the value of the actReference1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ActReference }{@code >}
     *     
     */
    public void setActReference1(JAXBElement<REPCMT000100UV01ActReference> value) {
        this.actReference1 = value;
    }

    /**
     * Gets the value of the act property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Act }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Act> getAct() {
        return act;
    }

    /**
     * Sets the value of the act property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Act }{@code >}
     *     
     */
    public void setAct(JAXBElement<REPCMT000100UV01Act> value) {
        this.act = value;
    }

    /**
     * Gets the value of the encounter property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Encounter }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Encounter> getEncounter() {
        return encounter;
    }

    /**
     * Sets the value of the encounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Encounter }{@code >}
     *     
     */
    public void setEncounter(JAXBElement<REPCMT000100UV01Encounter> value) {
        this.encounter = value;
    }

    /**
     * Gets the value of the observation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Observation }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Observation> getObservation() {
        return observation;
    }

    /**
     * Sets the value of the observation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Observation }{@code >}
     *     
     */
    public void setObservation(JAXBElement<REPCMT000100UV01Observation> value) {
        this.observation = value;
    }

    /**
     * Gets the value of the organizer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Organizer }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Organizer> getOrganizer() {
        return organizer;
    }

    /**
     * Sets the value of the organizer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Organizer }{@code >}
     *     
     */
    public void setOrganizer(JAXBElement<REPCMT000100UV01Organizer> value) {
        this.organizer = value;
    }

    /**
     * Gets the value of the procedure property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Procedure }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Procedure> getProcedure() {
        return procedure;
    }

    /**
     * Sets the value of the procedure property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Procedure }{@code >}
     *     
     */
    public void setProcedure(JAXBElement<REPCMT000100UV01Procedure> value) {
        this.procedure = value;
    }

    /**
     * Gets the value of the substanceAdministration property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01SubstanceAdministration }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01SubstanceAdministration> getSubstanceAdministration() {
        return substanceAdministration;
    }

    /**
     * Sets the value of the substanceAdministration property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01SubstanceAdministration }{@code >}
     *     
     */
    public void setSubstanceAdministration(JAXBElement<REPCMT000100UV01SubstanceAdministration> value) {
        this.substanceAdministration = value;
    }

    /**
     * Gets the value of the supply property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Supply }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Supply> getSupply() {
        return supply;
    }

    /**
     * Sets the value of the supply property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Supply }{@code >}
     *     
     */
    public void setSupply(JAXBElement<REPCMT000100UV01Supply> value) {
        this.supply = value;
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
     *     {@link ActRelationshipHasComponent }
     *     
     */
    public ActRelationshipHasComponent getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActRelationshipHasComponent }
     *     
     */
    public void setTypeCode(ActRelationshipHasComponent value) {
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
        return contextControlCode;
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

    /**
     * Gets the value of the contextConductionInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isContextConductionInd() {
        if (contextConductionInd == null) {
            return true;
        } else {
            return contextConductionInd;
        }
    }

    /**
     * Sets the value of the contextConductionInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setContextConductionInd(Boolean value) {
        this.contextConductionInd = value;
    }

}
