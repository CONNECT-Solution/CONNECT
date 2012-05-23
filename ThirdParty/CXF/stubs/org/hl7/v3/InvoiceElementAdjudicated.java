
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvoiceElementAdjudicated.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InvoiceElementAdjudicated">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ADNPPPELAT"/>
 *     &lt;enumeration value="ADNPPPELCT"/>
 *     &lt;enumeration value="ADNPPPMNAT"/>
 *     &lt;enumeration value="ADNPPPMNCT"/>
 *     &lt;enumeration value="ADNPSPELAT"/>
 *     &lt;enumeration value="ADNPSPELCT"/>
 *     &lt;enumeration value="ADNPSPMNAT"/>
 *     &lt;enumeration value="ADNPSPMNCT"/>
 *     &lt;enumeration value="ADNFPPELAT"/>
 *     &lt;enumeration value="ADNFPPELCT"/>
 *     &lt;enumeration value="ADNFPPMNAT"/>
 *     &lt;enumeration value="ADNFPPMNCT"/>
 *     &lt;enumeration value="ADNFSPELAT"/>
 *     &lt;enumeration value="ADNFSPELCT"/>
 *     &lt;enumeration value="ADNFSPMNAT"/>
 *     &lt;enumeration value="ADNFSPMNCT"/>
 *     &lt;enumeration value="ADPPPPELAT"/>
 *     &lt;enumeration value="ADPPPPELCT"/>
 *     &lt;enumeration value="ADPPPPMNAT"/>
 *     &lt;enumeration value="ADPPPPMNCT"/>
 *     &lt;enumeration value="ADPPSPELAT"/>
 *     &lt;enumeration value="ADPPSPELCT"/>
 *     &lt;enumeration value="ADPPSPMNAT"/>
 *     &lt;enumeration value="ADPPSPMNCT"/>
 *     &lt;enumeration value="ADRFPPELAT"/>
 *     &lt;enumeration value="ADRFPPELCT"/>
 *     &lt;enumeration value="ADRFPPMNAT"/>
 *     &lt;enumeration value="ADRFPPMNCT"/>
 *     &lt;enumeration value="ADRFSPELAT"/>
 *     &lt;enumeration value="ADRFSPELCT"/>
 *     &lt;enumeration value="ADRFSPMNAT"/>
 *     &lt;enumeration value="ADRFSPMNCT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InvoiceElementAdjudicated")
@XmlEnum
public enum InvoiceElementAdjudicated {

    ADNPPPELAT,
    ADNPPPELCT,
    ADNPPPMNAT,
    ADNPPPMNCT,
    ADNPSPELAT,
    ADNPSPELCT,
    ADNPSPMNAT,
    ADNPSPMNCT,
    ADNFPPELAT,
    ADNFPPELCT,
    ADNFPPMNAT,
    ADNFPPMNCT,
    ADNFSPELAT,
    ADNFSPELCT,
    ADNFSPMNAT,
    ADNFSPMNCT,
    ADPPPPELAT,
    ADPPPPELCT,
    ADPPPPMNAT,
    ADPPPPMNCT,
    ADPPSPELAT,
    ADPPSPELCT,
    ADPPSPMNAT,
    ADPPSPMNCT,
    ADRFPPELAT,
    ADRFPPELCT,
    ADRFPPMNAT,
    ADRFPPMNCT,
    ADRFSPELAT,
    ADRFSPELCT,
    ADRFSPMNAT,
    ADRFSPMNCT;

    public String value() {
        return name();
    }

    public static InvoiceElementAdjudicated fromValue(String v) {
        return valueOf(v);
    }

}
