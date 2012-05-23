
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActTaskMicrobiologyResultsReviewCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActTaskMicrobiologyResultsReviewCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="MICRORREV"/>
 *     &lt;enumeration value="MICROORGRREV"/>
 *     &lt;enumeration value="MICROSENSRREV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActTaskMicrobiologyResultsReviewCode")
@XmlEnum
public enum ActTaskMicrobiologyResultsReviewCode {

    MICRORREV,
    MICROORGRREV,
    MICROSENSRREV;

    public String value() {
        return name();
    }

    public static ActTaskMicrobiologyResultsReviewCode fromValue(String v) {
        return valueOf(v);
    }

}
