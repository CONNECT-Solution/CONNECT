
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for en_explicit.suffix complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="en_explicit.suffix">
 *   &lt;complexContent>
 *     &lt;restriction base="{urn:hl7-org:v3}ENXP_explicit">
 *       &lt;attribute name="partType" type="{urn:hl7-org:v3}EntityNamePartType" fixed="SFX" />
 *       &lt;attribute name="qualifier" type="{urn:hl7-org:v3}set_EntityNamePartQualifier" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "en_explicit.suffix")
public class EnExplicitSuffix
    extends ENXPExplicit
{


}
