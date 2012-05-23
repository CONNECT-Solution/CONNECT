
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                         A code for a common (periodical) activity of daily
 *                         living based on which the event related periodic
 *                         interval is specified.
 *                      
 * 
 * <p>Java class for EIVL.event complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EIVL.event">
 *   &lt;complexContent>
 *     &lt;restriction base="{urn:hl7-org:v3}CE">
 *       &lt;attribute name="code" type="{urn:hl7-org:v3}TimingEvent" />
 *       &lt;attribute name="codeSystem" type="{urn:hl7-org:v3}uid" fixed="2.16.840.1.113883.5.139" />
 *       &lt;attribute name="codeSystemName" type="{urn:hl7-org:v3}st" fixed="TimingEvent" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EIVL.event")
public class EIVLEvent
    extends CE
{


}
