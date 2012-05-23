
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvoiceElementPaid.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InvoiceElementPaid">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PDNPPPELAT"/>
 *     &lt;enumeration value="PDNPPPELCT"/>
 *     &lt;enumeration value="PDNPPPMNAT"/>
 *     &lt;enumeration value="PDNPPPMNCT"/>
 *     &lt;enumeration value="PDNPSPELAT"/>
 *     &lt;enumeration value="PDNPSPELCT"/>
 *     &lt;enumeration value="PDNPSPMNAT"/>
 *     &lt;enumeration value="PDNPSPMNCT"/>
 *     &lt;enumeration value="PDNFPPELAT"/>
 *     &lt;enumeration value="PDNFPPELCT"/>
 *     &lt;enumeration value="PDNFPPMNAT"/>
 *     &lt;enumeration value="PDNFPPMNCT"/>
 *     &lt;enumeration value="PDNFSPELAT"/>
 *     &lt;enumeration value="PDNFSPELCT"/>
 *     &lt;enumeration value="PDNFSPMNAT"/>
 *     &lt;enumeration value="PDNFSPMNCT"/>
 *     &lt;enumeration value="PDPPPPELAT"/>
 *     &lt;enumeration value="PDPPPPELCT"/>
 *     &lt;enumeration value="PDPPPPMNAT"/>
 *     &lt;enumeration value="PDPPPPMNCT"/>
 *     &lt;enumeration value="PDPPSPELAT"/>
 *     &lt;enumeration value="PDPPSPELCT"/>
 *     &lt;enumeration value="PDPPSPMNAT"/>
 *     &lt;enumeration value="PDPPSPMNCT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InvoiceElementPaid")
@XmlEnum
public enum InvoiceElementPaid {

    PDNPPPELAT,
    PDNPPPELCT,
    PDNPPPMNAT,
    PDNPPPMNCT,
    PDNPSPELAT,
    PDNPSPELCT,
    PDNPSPMNAT,
    PDNPSPMNCT,
    PDNFPPELAT,
    PDNFPPELCT,
    PDNFPPMNAT,
    PDNFPPMNCT,
    PDNFSPELAT,
    PDNFSPELCT,
    PDNFSPMNAT,
    PDNFSPMNCT,
    PDPPPPELAT,
    PDPPPPELCT,
    PDPPPPMNAT,
    PDPPPPMNCT,
    PDPPSPELAT,
    PDPPSPELCT,
    PDPPSPMNAT,
    PDPPSPMNCT;

    public String value() {
        return name();
    }

    public static InvoiceElementPaid fromValue(String v) {
        return valueOf(v);
    }

}
