
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IVL_INT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVL_INT">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:hl7-org:v3}SXCM_INT">
 *       &lt;choice minOccurs="0">
 *         &lt;sequence>
 *           &lt;element name="low" type="{urn:hl7-org:v3}IVXB_INT"/>
 *           &lt;choice minOccurs="0">
 *             &lt;element name="width" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *             &lt;element name="high" type="{urn:hl7-org:v3}IVXB_INT" minOccurs="0"/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *         &lt;element name="high" type="{urn:hl7-org:v3}IVXB_INT"/>
 *         &lt;sequence>
 *           &lt;element name="width" type="{urn:hl7-org:v3}INT"/>
 *           &lt;element name="high" type="{urn:hl7-org:v3}IVXB_INT" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="center" type="{urn:hl7-org:v3}INT"/>
 *           &lt;element name="width" type="{urn:hl7-org:v3}INT" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVL_INT", propOrder = {
    "rest"
})
public class IVLINT
    extends SXCMINT
{

    @XmlElementRefs({
        @XmlElementRef(name = "center", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "high", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "low", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "width", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends INT>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "High" is used by two different parts of a schema. See: 
     * line 797 of file:/Users/akong/Workspace/connect/CONNECT/ThirdParty/CXF/Interfaces/src/schemas/HL7V3/NE2008/coreschemas/datatypes.xsd
     * line 788 of file:/Users/akong/Workspace/connect/CONNECT/ThirdParty/CXF/Interfaces/src/schemas/HL7V3/NE2008/coreschemas/datatypes.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link INT }{@code >}
     * {@link JAXBElement }{@code <}{@link INT }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBINT }{@code >}
     * {@link JAXBElement }{@code <}{@link IVXBINT }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends INT>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<? extends INT>>();
        }
        return this.rest;
    }

}
