
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_SubstitutionConditionNoneOrUnconditional.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_SubstitutionConditionNoneOrUnconditional">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NOSUB"/>
 *     &lt;enumeration value="UNCOND"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_SubstitutionConditionNoneOrUnconditional")
@XmlEnum
public enum XSubstitutionConditionNoneOrUnconditional {

    NOSUB,
    UNCOND;

    public String value() {
        return name();
    }

    public static XSubstitutionConditionNoneOrUnconditional fromValue(String v) {
        return valueOf(v);
    }

}
