
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QueryQuantityUnit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="QueryQuantityUnit">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CH"/>
 *     &lt;enumeration value="LI"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="RD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "QueryQuantityUnit")
@XmlEnum
public enum QueryQuantityUnit {

    CH,
    LI,
    PG,
    RD;

    public String value() {
        return name();
    }

    public static QueryQuantityUnit fromValue(String v) {
        return valueOf(v);
    }

}
