
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActMoodOrdPrms.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActMoodOrdPrms">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PRMS"/>
 *     &lt;enumeration value="RQO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActMoodOrdPrms")
@XmlEnum
public enum XActMoodOrdPrms {

    PRMS,
    RQO;

    public String value() {
        return name();
    }

    public static XActMoodOrdPrms fromValue(String v) {
        return valueOf(v);
    }

}
