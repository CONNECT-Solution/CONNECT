
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
 * <p>Java class for AD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AD">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}ANY">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="delimiter" type="{urn:hl7-org:v3}adxp.delimiter"/>
 *           &lt;element name="country" type="{urn:hl7-org:v3}adxp.country"/>
 *           &lt;element name="state" type="{urn:hl7-org:v3}adxp.state"/>
 *           &lt;element name="county" type="{urn:hl7-org:v3}adxp.county"/>
 *           &lt;element name="city" type="{urn:hl7-org:v3}adxp.city"/>
 *           &lt;element name="postalCode" type="{urn:hl7-org:v3}adxp.postalCode"/>
 *           &lt;element name="streetAddressLine" type="{urn:hl7-org:v3}adxp.streetAddressLine"/>
 *           &lt;element name="houseNumber" type="{urn:hl7-org:v3}adxp.houseNumber"/>
 *           &lt;element name="houseNumberNumeric" type="{urn:hl7-org:v3}adxp.houseNumberNumeric"/>
 *           &lt;element name="direction" type="{urn:hl7-org:v3}adxp.direction"/>
 *           &lt;element name="streetName" type="{urn:hl7-org:v3}adxp.streetName"/>
 *           &lt;element name="streetNameBase" type="{urn:hl7-org:v3}adxp.streetNameBase"/>
 *           &lt;element name="streetNameType" type="{urn:hl7-org:v3}adxp.streetNameType"/>
 *           &lt;element name="additionalLocator" type="{urn:hl7-org:v3}adxp.additionalLocator"/>
 *           &lt;element name="unitID" type="{urn:hl7-org:v3}adxp.unitID"/>
 *           &lt;element name="unitType" type="{urn:hl7-org:v3}adxp.unitType"/>
 *           &lt;element name="careOf" type="{urn:hl7-org:v3}adxp.careOf"/>
 *           &lt;element name="censusTract" type="{urn:hl7-org:v3}adxp.censusTract"/>
 *           &lt;element name="deliveryAddressLine" type="{urn:hl7-org:v3}adxp.deliveryAddressLine"/>
 *           &lt;element name="deliveryInstallationType" type="{urn:hl7-org:v3}adxp.deliveryInstallationType"/>
 *           &lt;element name="deliveryInstallationArea" type="{urn:hl7-org:v3}adxp.deliveryInstallationArea"/>
 *           &lt;element name="deliveryInstallationQualifier" type="{urn:hl7-org:v3}adxp.deliveryInstallationQualifier"/>
 *           &lt;element name="deliveryMode" type="{urn:hl7-org:v3}adxp.deliveryMode"/>
 *           &lt;element name="deliveryModeIdentifier" type="{urn:hl7-org:v3}adxp.deliveryModeIdentifier"/>
 *           &lt;element name="buildingNumberSuffix" type="{urn:hl7-org:v3}adxp.buildingNumberSuffix"/>
 *           &lt;element name="postBox" type="{urn:hl7-org:v3}adxp.postBox"/>
 *           &lt;element name="precinct" type="{urn:hl7-org:v3}adxp.precinct"/>
 *         &lt;/choice>
 *         &lt;element name="useablePeriod" type="{urn:hl7-org:v3}SXCM_TS" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="use" type="{urn:hl7-org:v3}set_PostalAddressUse" />
 *       &lt;attribute name="isNotOrdered" type="{urn:hl7-org:v3}bl" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AD", propOrder = {
    "content"
})
public class AD {

    @XmlElementRefs({
        @XmlElementRef(name = "streetAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryModeIdentifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "city", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "buildingNumberSuffix", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "unitType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "precinct", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "postBox", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "censusTract", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryMode", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "additionalLocator", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "state", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "country", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationArea", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetName", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "houseNumber", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "postalCode", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "delimiter", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "houseNumberNumeric", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "useablePeriod", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryAddressLine", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetNameBase", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "unitID", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "careOf", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "streetNameType", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "direction", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "deliveryInstallationQualifier", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "county", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    })
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "use")
    protected List<String> use;
    @XmlAttribute(name = "isNotOrdered")
    protected Boolean isNotOrdered;

    /**
     * 
     *             Mailing and home or office addresses. A sequence of
     *             address parts, such as street or post office Box, city,
     *             postal code, country, etc.
     *          Gets the value of the content property.
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
     * {@link JAXBElement }{@code <}{@link AdxpStreetAddressLine }{@code >}
     * {@link String }
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryModeIdentifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCity }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpBuildingNumberSuffix }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpUnitType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPrecinct }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPostBox }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCensusTract }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryMode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpAdditionalLocator }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpState }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCountry }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationArea }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetName }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpHouseNumber }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpPostalCode }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDelimiter }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpHouseNumberNumeric }{@code >}
     * {@link JAXBElement }{@code <}{@link SXCMTS }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryAddressLine }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetNameBase }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpUnitID }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCareOf }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDirection }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpStreetNameType }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationQualifier }{@code >}
     * {@link JAXBElement }{@code <}{@link AdxpCounty }{@code >}
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
