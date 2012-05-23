
package org.hl7.v3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Mailing and home or office addresses. A sequence of
 *             address parts, such as street or post office Box, city,
 *             postal code, country, etc.
 *             
 * 
 * <p>Java class for AD_explicit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AD_explicit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="delimiter" type="{urn:hl7-org:v3}adxp_explicit.delimiter"/>
 *           &lt;element name="country" type="{urn:hl7-org:v3}adxp_explicit.country"/>
 *           &lt;element name="state" type="{urn:hl7-org:v3}adxp_explicit.state"/>
 *           &lt;element name="county" type="{urn:hl7-org:v3}adxp_explicit.county"/>
 *           &lt;element name="city" type="{urn:hl7-org:v3}adxp_explicit.city"/>
 *           &lt;element name="postalCode" type="{urn:hl7-org:v3}adxp_explicit.postalCode"/>
 *           &lt;element name="streetAddressLine" type="{urn:hl7-org:v3}adxp_explicit.streetAddressLine"/>
 *           &lt;element name="houseNumber" type="{urn:hl7-org:v3}adxp_explicit.houseNumber"/>
 *           &lt;element name="houseNumberNumeric" type="{urn:hl7-org:v3}adxp_explicit.houseNumberNumeric"/>
 *           &lt;element name="direction" type="{urn:hl7-org:v3}adxp_explicit.direction"/>
 *           &lt;element name="streetName" type="{urn:hl7-org:v3}adxp_explicit.streetName"/>
 *           &lt;element name="streetNameBase" type="{urn:hl7-org:v3}adxp_explicit.streetNameBase"/>
 *           &lt;element name="streetNameType" type="{urn:hl7-org:v3}adxp_explicit.streetNameType1"/>
 *           &lt;element name="additionalLocator" type="{urn:hl7-org:v3}adxp_explicit.additionalLocator"/>
 *           &lt;element name="unitID" type="{urn:hl7-org:v3}adxp_explicit.unitID"/>
 *           &lt;element name="unitType" type="{urn:hl7-org:v3}adxp_explicit.unitType"/>
 *           &lt;element name="careOf" type="{urn:hl7-org:v3}adxp_explicit.careOf"/>
 *           &lt;element name="censusTract" type="{urn:hl7-org:v3}adxp_explicit.censusTract"/>
 *           &lt;element name="deliveryAddressLine" type="{urn:hl7-org:v3}adxp_explicit.deliveryAddressLine"/>
 *           &lt;element name="deliveryInstallationType" type="{urn:hl7-org:v3}adxp_explicit.deliveryInstallationType"/>
 *           &lt;element name="deliveryInstallationArea" type="{urn:hl7-org:v3}adxp_explicit.deliveryInstallationArea"/>
 *           &lt;element name="deliveryInstallationQualifier" type="{urn:hl7-org:v3}adxp_explicit.deliveryInstallationQualifier"/>
 *           &lt;element name="deliveryMode" type="{urn:hl7-org:v3}adxp_explicit.deliveryMode"/>
 *           &lt;element name="deliveryModeIdentifier" type="{urn:hl7-org:v3}adxp_explicit.deliveryModeIdentifier"/>
 *           &lt;element name="buildingNumberSuffix" type="{urn:hl7-org:v3}adxp_explicit.buildingNumberSuffix"/>
 *           &lt;element name="postBox" type="{urn:hl7-org:v3}adxp_explicit.postBox"/>
 *           &lt;element name="precinct" type="{urn:hl7-org:v3}adxp_explicit.precinct"/>
 *         &lt;/choice>
 *         &lt;element name="useablePeriod" type="{urn:hl7-org:v3}SXCM_TS_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="use" type="{urn:hl7-org:v3}set_PostalAddressUse" />
 *       &lt;attribute name="isNotOrdered" type="{urn:hl7-org:v3}bl" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AD_explicit", propOrder = {
    "content"
})
public class ADExplicit {

    @XmlElementRefs({
        @XmlElementRef(name = "additionalLocator", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "unitID", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "state", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "country", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationArea", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "postalCode", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "county", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetNameBase", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "city", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "careOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationQualifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "delimiter", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "houseNumber", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "direction", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryModeIdentifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "buildingNumberSuffix", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetName", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryMode", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "postBox", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "houseNumberNumeric", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "precinct", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "unitType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "censusTract", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetNameType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "useablePeriod", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    })
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "use")
    protected List<String> use;
    @XmlAttribute(name = "isNotOrdered")
    protected Boolean isNotOrdered;

    /**
     * 
     *             Mailing and home or office addresses. A sequence of
     *             address parts, such as street or post office Box, city,
     *             postal code, country, etc.
     *             Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AdxpExplicitAdditionalLocator }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitUnitID }{@code >}
     * {@link String }
     * {@link JAXBElement }{@code <}{@link AdxpExplicitState }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitCountry }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryInstallationArea }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitPostalCode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitCounty }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitStreetNameBase }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitCity }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitCareOf }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryInstallationQualifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDelimiter }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitHouseNumber }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDirection }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryModeIdentifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitBuildingNumberSuffix }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitStreetName }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryMode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitPostBox }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitHouseNumberNumeric }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitStreetAddressLine }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitPrecinct }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitUnitType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitCensusTract }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryInstallationType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitDeliveryAddressLine }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpExplicitStreetNameType1 }{@code >}
     * {@link JAXBElement }{@code <}{@link SXCMTSExplicit }{@code >}
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
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
     * Gets the value of the use property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the use property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUse() {
        if (use == null) {
            use = new ArrayList<String>();
        }
        return this.use;
    }

    /**
     * Gets the value of the isNotOrdered property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNotOrdered() {
        return isNotOrdered;
    }

    /**
     * Sets the value of the isNotOrdered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNotOrdered(Boolean value) {
        this.isNotOrdered = value;
    }

}
