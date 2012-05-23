
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActReplaceOrRevise.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActReplaceOrRevise">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MOD"/>
 *     &lt;enumeration value="RPLC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActReplaceOrRevise")
@XmlEnum
public enum XActReplaceOrRevise {

    MOD,
    RPLC;

    public String value() {
        return name();
    }

    public static XActReplaceOrRevise fromValue(String v) {
        return valueOf(v);
    }

}
