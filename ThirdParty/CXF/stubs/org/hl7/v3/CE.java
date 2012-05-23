
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Coded data, consists of a coded value (CV)
 *             and, optionally, coded value(s) from other coding systems
 *             that identify the same concept. Used when alternative
 *             codes may exist.
 *          
 * 
 * <p>Java class for CE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CE">
 *   &lt;complexContent>
 *     &lt;restriction base="{urn:hl7-org:v3}CD">
 *       &lt;sequence>
 *         &lt;element name="originalText" type="{urn:hl7-org:v3}ED" minOccurs="0"/>
 *         &lt;element name="qualifier" type="{urn:hl7-org:v3}CR" maxOccurs="0" minOccurs="0"/>
 *         &lt;element name="translation" type="{urn:hl7-org:v3}CD" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="code" type="{urn:hl7-org:v3}cs" />
 *       &lt;attribute name="codeSystem" type="{urn:hl7-org:v3}uid" />
 *       &lt;attribute name="codeSystemName" type="{urn:hl7-org:v3}st" />
 *       &lt;attribute name="codeSystemVersion" type="{urn:hl7-org:v3}st" />
 *       &lt;attribute name="displayName" type="{urn:hl7-org:v3}st" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CE")
@XmlSeeAlso({
    CV.class,
    HXITCE.class,
    EIVLEvent.class
})
public class CE
    extends CD
{


}
