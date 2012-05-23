
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExpectedSubset.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExpectedSubset">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="FUTURE"/>
 *     &lt;enumeration value="LAST"/>
 *     &lt;enumeration value="NEXT"/>
 *     &lt;enumeration value="FUTSUM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExpectedSubset")
@XmlEnum
public enum ExpectedSubset {

    FUTURE,
    LAST,
    NEXT,
    FUTSUM;

    public String value() {
        return name();
    }

    public static ExpectedSubset fromValue(String v) {
        return valueOf(v);
    }

}
