
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
 * <p>Java class for REPC_MT000200UV.InFulfillmentOf2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000200UV.InFulfillmentOf2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;choice>
 *           &lt;element name="actDefinition" type="{urn:hl7-org:v3}REPC_MT000200UV.ActDefinition"/>
 *           &lt;element name="encounterDefinition" type="{urn:hl7-org:v3}REPC_MT000200UV.EncounterDefinition"/>
 *           &lt;element name="observationDefinition" type="{urn:hl7-org:v3}REPC_MT000200UV.ObservationDefinition"/>
 *           &lt;element name="procedureDefinition" type="{urn:hl7-org:v3}REPC_MT000200UV.ProcedureDefinition"/>
 *           &lt;element name="substanceAdministrationDefinition" type="{urn:hl7-org:v3}REPC_MT000200UV.SubstanceAdministrationDefinition"/>
 *           &lt;element name="supplyEvent" type="{urn:hl7-org:v3}REPC_MT000200UV.SupplyEvent"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="typeCode" use="required" type="{urn:hl7-org:v3}ActRelationshipFulfills" />
 *       &lt;attribute name="contextControlCode" type="{urn:hl7-org:v3}ContextControl" default="ON" />
 *       &lt;attribute name="contextConductionInd" type="{urn:hl7-org:v3}bl" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000200UV.InFulfillmentOf2", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "actDefinition",
    "encounterDefinition",
    "observationDefinition",
    "procedureDefinition",
    "substanceAdministrationDefinition",
    "supplyEvent"
})
public class REPCMT000200UVInFulfillmentOf2 {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElementRef(name = "actDefinition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVActDefinition> actDefinition;
    @XmlElementRef(name = "encounterDefinition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVEncounterDefinition> encounterDefinition;
    @XmlElementRef(name = "observationDefinition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVObservationDefinition> observationDefinition;
    @XmlElementRef(name = "procedureDefinition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVProcedureDefinition> procedureDefinition;
    @XmlElementRef(name = "substanceAdministrationDefinition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVSubstanceAdministrationDefinition> substanceAdministrationDefinition;
    @XmlElementRef(name = "supplyEvent", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000200UVSupplyEvent> supplyEvent;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "typeCode", required = true)
    protected ActRelationshipFulfills typeCode;
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
     * Gets the value of the actDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVActDefinition }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVActDefinition> getActDefinition() {
        return actDefinition;
    }

    /**
     * Sets the value of the actDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVActDefinition }{@code >}
     *     
     */
    public void setActDefinition(JAXBElement<REPCMT000200UVActDefinition> value) {
        this.actDefinition = value;
    }

    /**
     * Gets the value of the encounterDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVEncounterDefinition }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVEncounterDefinition> getEncounterDefinition() {
        return encounterDefinition;
    }

    /**
     * Sets the value of the encounterDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVEncounterDefinition }{@code >}
     *     
     */
    public void setEncounterDefinition(JAXBElement<REPCMT000200UVEncounterDefinition> value) {
        this.encounterDefinition = value;
    }

    /**
     * Gets the value of the observationDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVObservationDefinition }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVObservationDefinition> getObservationDefinition() {
        return observationDefinition;
    }

    /**
     * Sets the value of the observationDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVObservationDefinition }{@code >}
     *     
     */
    public void setObservationDefinition(JAXBElement<REPCMT000200UVObservationDefinition> value) {
        this.observationDefinition = value;
    }

    /**
     * Gets the value of the procedureDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVProcedureDefinition }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVProcedureDefinition> getProcedureDefinition() {
        return procedureDefinition;
    }

    /**
     * Sets the value of the procedureDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVProcedureDefinition }{@code >}
     *     
     */
    public void setProcedureDefinition(JAXBElement<REPCMT000200UVProcedureDefinition> value) {
        this.procedureDefinition = value;
    }

    /**
     * Gets the value of the substanceAdministrationDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVSubstanceAdministrationDefinition }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVSubstanceAdministrationDefinition> getSubstanceAdministrationDefinition() {
        return substanceAdministrationDefinition;
    }

    /**
     * Sets the value of the substanceAdministrationDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVSubstanceAdministrationDefinition }{@code >}
     *     
     */
    public void setSubstanceAdministrationDefinition(JAXBElement<REPCMT000200UVSubstanceAdministrationDefinition> value) {
        this.substanceAdministrationDefinition = value;
    }

    /**
     * Gets the value of the supplyEvent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVSupplyEvent }{@code >}
     *     
     */
    public JAXBElement<REPCMT000200UVSupplyEvent> getSupplyEvent() {
        return supplyEvent;
    }

    /**
     * Sets the value of the supplyEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000200UVSupplyEvent }{@code >}
     *     
     */
    public void setSupplyEvent(JAXBElement<REPCMT000200UVSupplyEvent> value) {
        this.supplyEvent = value;
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
     *     {@link ActRelationshipFulfills }
     *     
     */
    public ActRelationshipFulfills getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActRelationshipFulfills }
     *     
     */
    public void setTypeCode(ActRelationshipFulfills value) {
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
            return "ON";
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
            return false;
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
