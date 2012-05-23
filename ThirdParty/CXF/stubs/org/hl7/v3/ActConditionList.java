
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActConditionList.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActConditionList">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CONDLIST"/>
 *     &lt;enumeration value="INTOLIST"/>
 *     &lt;enumeration value="PROBLIST"/>
 *     &lt;enumeration value="RISKLIST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActConditionList")
@XmlEnum
public enum ActConditionList {

    CONDLIST,
    INTOLIST,
    PROBLIST,
    RISKLIST;

    public String value() {
        return name();
    }

    public static ActConditionList fromValue(String v) {
        return valueOf(v);
    }

}
