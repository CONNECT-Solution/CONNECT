
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
 * <p>Java class for COCT_MT600000UV06.SupplyEvent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCT_MT600000UV06.SupplyEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}IVL_TS_explicit" minOccurs="0"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="product" type="{urn:hl7-org:v3}COCT_MT600000UV06.Product" maxOccurs="2"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}COCT_MT600000UV06.Performer" minOccurs="0"/>
 *         &lt;element name="origin" type="{urn:hl7-org:v3}COCT_MT600000UV06.Origin" minOccurs="0"/>
 *         &lt;element name="destination" type="{urn:hl7-org:v3}COCT_MT600000UV06.Destination" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}COCT_MT600000UV06.Location"/>
 *         &lt;element name="pertinentInformation1" type="{urn:hl7-org:v3}COCT_MT600000UV06.PertinentInformation1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="pertinentInformation2" type="{urn:hl7-org:v3}COCT_MT600000UV06.PertinentInformation2"/>
 *         &lt;element name="pertinentInformation3" type="{urn:hl7-org:v3}COCT_MT600000UV06.PertinentInformation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassSupply" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}ActMood" fixed="EVN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT600000UV06.SupplyEvent", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "code",
    "effectiveTime",
    "priorityCode",
    "product",
    "performer",
    "origin",
    "destination",
    "location",
    "pertinentInformation1",
    "pertinentInformation2",
    "pertinentInformation3"
})
public class COCTMT600000UV06SupplyEvent {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    @XmlElement(required = true)
    protected CD code;
    protected IVLTSExplicit effectiveTime;
    protected List<CE> priorityCode;
    @XmlElement(required = true)
    protected List<COCTMT600000UV06Product> product;
    @XmlElementRef(name = "performer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT600000UV06Performer> performer;
    @XmlElementRef(name = "origin", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT600000UV06Origin> origin;
    @XmlElementRef(name = "destination", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<COCTMT600000UV06Destination> destination;
    @XmlElement(required = true)
    protected COCTMT600000UV06Location location;
    @XmlElement(nillable = true)
    protected List<COCTMT600000UV06PertinentInformation1> pertinentInformation1;
    @XmlElement(required = true, nillable = true)
    protected COCTMT600000UV06PertinentInformation2 pertinentInformation2;
    @XmlElement(nillable = true)
    protected List<COCTMT600000UV06PertinentInformation> pertinentInformation3;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected ActClassSupply classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected List<String> moodCode;

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
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CD }
     *     
     */
    public CD getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CD }
     *     
     */
    public void setCode(CD value) {
        this.code = value;
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
     * Gets the value of the priorityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the priorityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPriorityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getPriorityCode() {
        if (priorityCode == null) {
            priorityCode = new ArrayList<CE>();
        }
        return this.priorityCode;
    }

    /**
     * Gets the value of the product property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the product property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduct().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT600000UV06Product }
     * 
     * 
     */
    public List<COCTMT600000UV06Product> getProduct() {
        if (product == null) {
            product = new ArrayList<COCTMT600000UV06Product>();
        }
        return this.product;
    }

    /**
     * Gets the value of the performer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Performer }{@code >}
     *     
     */
    public JAXBElement<COCTMT600000UV06Performer> getPerformer() {
        return performer;
    }

    /**
     * Sets the value of the performer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Performer }{@code >}
     *     
     */
    public void setPerformer(JAXBElement<COCTMT600000UV06Performer> value) {
        this.performer = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Origin }{@code >}
     *     
     */
    public JAXBElement<COCTMT600000UV06Origin> getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Origin }{@code >}
     *     
     */
    public void setOrigin(JAXBElement<COCTMT600000UV06Origin> value) {
        this.origin = value;
    }

    /**
     * Gets the value of the destination property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Destination }{@code >}
     *     
     */
    public JAXBElement<COCTMT600000UV06Destination> getDestination() {
        return destination;
    }

    /**
     * Sets the value of the destination property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link COCTMT600000UV06Destination }{@code >}
     *     
     */
    public void setDestination(JAXBElement<COCTMT600000UV06Destination> value) {
        this.destination = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT600000UV06Location }
     *     
     */
    public COCTMT600000UV06Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT600000UV06Location }
     *     
     */
    public void setLocation(COCTMT600000UV06Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the pertinentInformation1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pertinentInformation1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPertinentInformation1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT600000UV06PertinentInformation1 }
     * 
     * 
     */
    public List<COCTMT600000UV06PertinentInformation1> getPertinentInformation1() {
        if (pertinentInformation1 == null) {
            pertinentInformation1 = new ArrayList<COCTMT600000UV06PertinentInformation1>();
        }
        return this.pertinentInformation1;
    }

    /**
     * Gets the value of the pertinentInformation2 property.
     * 
     * @return
     *     possible object is
     *     {@link COCTMT600000UV06PertinentInformation2 }
     *     
     */
    public COCTMT600000UV06PertinentInformation2 getPertinentInformation2() {
        return pertinentInformation2;
    }

    /**
     * Sets the value of the pertinentInformation2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCTMT600000UV06PertinentInformation2 }
     *     
     */
    public void setPertinentInformation2(COCTMT600000UV06PertinentInformation2 value) {
        this.pertinentInformation2 = value;
    }

    /**
     * Gets the value of the pertinentInformation3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pertinentInformation3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPertinentInformation3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COCTMT600000UV06PertinentInformation }
     * 
     * 
     */
    public List<COCTMT600000UV06PertinentInformation> getPertinentInformation3() {
        if (pertinentInformation3 == null) {
            pertinentInformation3 = new ArrayList<COCTMT600000UV06PertinentInformation>();
        }
        return this.pertinentInformation3;
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
     *     {@link ActClassSupply }
     *     
     */
    public ActClassSupply getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActClassSupply }
     *     
     */
    public void setClassCode(ActClassSupply value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the moodCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMoodCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMoodCode() {
        if (moodCode == null) {
            moodCode = new ArrayList<String>();
        }
        return this.moodCode;
    }

}
