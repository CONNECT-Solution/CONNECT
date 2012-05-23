
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
 * <p>Java class for COCT_MT080000UV.Container complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT080000UV.Container">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="desc" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="riskCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="handlingCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="capacityQuantity" type="{urn:hl7-org:v3}PQ_explicit" minOccurs="0"/>
 *         &lt;element name="heightQuantity" type="{urn:hl7-org:v3}PQ_explicit" minOccurs="0"/>
 *         &lt;element name="diameterQuantity" type="{urn:hl7-org:v3}PQ_explicit" minOccurs="0"/>
 *         &lt;element name="capTypeCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="separatorTypeCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="barrierDeltaQuantity" type="{urn:hl7-org:v3}PQ_explicit" minOccurs="0"/>
 *         &lt;element name="bottomDeltaQuantity" type="{urn:hl7-org:v3}PQ_explicit" minOccurs="0"/>
 *         &lt;element name="asIdentifiedContainer" type="{urn:hl7-org:v3}COCT_MT080000UV.IdentifiedContainer" minOccurs="0"/>
 *         &lt;element name="asContent" type="{urn:hl7-org:v3}COCT_MT080000UV.Content3" minOccurs="0"/>
 *         &lt;element name="asLocatedEntity" type="{urn:hl7-org:v3}COCT_MT070000UV01.LocatedEntity" minOccurs="0"/>
 *         &lt;element name="additive" type="{urn:hl7-org:v3}COCT_MT080000UV.Additive2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}EntityClassContainer" />
 *       &lt;attribute name="determinerCode" use="required" type="{urn:hl7-org:v3}EntityDeterminer" fixed="INSTANCE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT080000UV.Container", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "desc",
    "riskCode",
    "handlingCode",
    "capacityQuantity",
    "heightQuantity",
    "diameterQuantity",
    "capTypeCode",
    "separatorTypeCode",
    "barrierDeltaQuantity",
    "bottomDeltaQuantity",
    "asIdentifiedContainer",
    "asContent",
    "asLocatedEntity",
    "additive"
})
public class COCTMT080000UVContainer {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected II id;
    protected CE code;
    protected EDExplicit desc;
    protected CE riskCode;
    protected CE handlingCode;
    protected PQExplicit capacityQuantity;
    protected PQExplicit heightQuantity;
    protected PQExplicit diameterQuantity;
    protected CE capTypeCode;
    protected CE separatorTypeCode;
    protected PQExplicit barrierDeltaQuantity;
    protected PQExplicit bottomDeltaQuantity;
    @XmlElementRef(name = "asIdentifiedContainer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVIdentifiedContainer> asIdentifiedContainer;
    @XmlElementRef(name = "asContent", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT080000UVContent3> asContent;
    @XmlElementRef(name = "asLocatedEntity", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT070000UV01LocatedEntity> asLocatedEntity;
    @XmlElement(nillable = true)
    protected List<COCTMT080000UVAdditive2> additive;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected EntityClassContainer classCode;
    @XmlAttribute(name = "determinerCode", required = true)
    protected String determinerCode;

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
     * Gets the value of the riskCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getRiskCode() {
        return riskCode;
    }

    /**
     * Sets the value of the riskCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setRiskCode(CE value) {
        this.riskCode = value;
    }

    /**
     * Gets the value of the handlingCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getHandlingCode() {
        return handlingCode;
    }

    /**
     * Sets the value of the handlingCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setHandlingCode(CE value) {
        this.handlingCode = value;
    }

    /**
     * Gets the value of the capacityQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQExplicit }
     *     
     */
    public PQExplicit getCapacityQuantity() {
        return capacityQuantity;
    }

    /**
     * Sets the value of the capacityQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQExplicit }
     *     
     */
    public void setCapacityQuantity(PQExplicit value) {
        this.capacityQuantity = value;
    }

    /**
     * Gets the value of the heightQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQExplicit }
     *     
     */
    public PQExplicit getHeightQuantity() {
        return heightQuantity;
    }

    /**
     * Sets the value of the heightQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQExplicit }
     *     
     */
    public void setHeightQuantity(PQExplicit value) {
        this.heightQuantity = value;
    }

    /**
     * Gets the value of the diameterQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQExplicit }
     *     
     */
    public PQExplicit getDiameterQuantity() {
        return diameterQuantity;
    }

    /**
     * Sets the value of the diameterQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQExplicit }
     *     
     */
    public void setDiameterQuantity(PQExplicit value) {
        this.diameterQuantity = value;
    }

    /**
     * Gets the value of the capTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getCapTypeCode() {
        return capTypeCode;
    }

    /**
     * Sets the value of the capTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setCapTypeCode(CE value) {
        this.capTypeCode = value;
    }

    /**
     * Gets the value of the separatorTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getSeparatorTypeCode() {
        return separatorTypeCode;
    }

    /**
     * Sets the value of the separatorTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setSeparatorTypeCode(CE value) {
        this.separatorTypeCode = value;
    }

    /**
     * Gets the value of the barrierDeltaQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQExplicit }
     *     
     */
    public PQExplicit getBarrierDeltaQuantity() {
        return barrierDeltaQuantity;
    }

    /**
     * Sets the value of the barrierDeltaQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQExplicit }
     *     
     */
    public void setBarrierDeltaQuantity(PQExplicit value) {
        this.barrierDeltaQuantity = value;
    }

    /**
     * Gets the value of the bottomDeltaQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link PQExplicit }
     *     
     */
    public PQExplicit getBottomDeltaQuantity() {
        return bottomDeltaQuantity;
    }

    /**
     * Sets the value of the bottomDeltaQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link PQExplicit }
     *     
     */
    public void setBottomDeltaQuantity(PQExplicit value) {
        this.bottomDeltaQuantity = value;
    }

    /**
     * Gets the value of the asIdentifiedContainer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVIdentifiedContainer }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVIdentifiedContainer> getAsIdentifiedContainer() {
        return asIdentifiedContainer;
    }

    /**
     * Sets the value of the asIdentifiedContainer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVIdentifiedContainer }{@code >}
     *     
     */
    public void setAsIdentifiedContainer(JAXBElement<COCTMT080000UVIdentifiedContainer> value) {
        this.asIdentifiedContainer = value;
    }

    /**
     * Gets the value of the asContent property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVContent3 }{@code >}
     *     
     */
    public JAXBElement<COCTMT080000UVContent3> getAsContent() {
        return asContent;
    }

    /**
     * Sets the value of the asContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT080000UVContent3 }{@code >}
     *     
     */
    public void setAsContent(JAXBElement<COCTMT080000UVContent3> value) {
        this.asContent = value;
    }

    /**
     * Gets the value of the asLocatedEntity property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT070000UV01LocatedEntity }{@code >}
     *     
     */
    public JAXBElement<COCTMT070000UV01LocatedEntity> getAsLocatedEntity() {
        return asLocatedEntity;
    }

    /**
     * Sets the value of the asLocatedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT070000UV01LocatedEntity }{@code >}
     *     
     */
    public void setAsLocatedEntity(JAXBElement<COCTMT070000UV01LocatedEntity> value) {
        this.asLocatedEntity = value;
    }

    /**
     * Gets the value of the additive property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additive property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditive().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT080000UVAdditive2 }
     * 
     * 
     */
    public List<COCTMT080000UVAdditive2> getAdditive() {
        if (additive == null) {
            additive = new ArrayList<COCTMT080000UVAdditive2>();
        }
        return this.additive;
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
     *     {@link EntityClassContainer }
     *     
     */
    public EntityClassContainer getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityClassContainer }
     *     
     */
    public void setClassCode(EntityClassContainer value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the determinerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeterminerCode() {
        if (determinerCode == null) {
            return "INSTANCE";
        } else {
            return determinerCode;
        }
    }

    /**
     * Sets the value of the determinerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeterminerCode(String value) {
        this.determinerCode = value;
    }

}
