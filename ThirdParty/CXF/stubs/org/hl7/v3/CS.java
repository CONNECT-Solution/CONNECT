
package org.hl7.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *             Coded data, consists of a code, display name, code system,
 *             and original text. Used when a single code value must be sent.
 *          
 * 
 * <p>Java class for CS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CS">
 *   &lt;complexContent>
 *     &lt;restriction base="{urn:hl7-org:v3}CV">
 *       &lt;attribute name="code" type="{urn:hl7-org:v3}cs" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CS")
public class CS
    extends CV
{


}
