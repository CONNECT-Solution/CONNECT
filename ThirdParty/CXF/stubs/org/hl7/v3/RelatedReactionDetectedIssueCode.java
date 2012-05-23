
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelatedReactionDetectedIssueCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RelatedReactionDetectedIssueCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RREACT"/>
 *     &lt;enumeration value="RALG"/>
 *     &lt;enumeration value="RINT"/>
 *     &lt;enumeration value="RAR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RelatedReactionDetectedIssueCode")
@XmlEnum
public enum RelatedReactionDetectedIssueCode {

    RREACT,
    RALG,
    RINT,
    RAR;

    public String value() {
        return name();
    }

    public static RelatedReactionDetectedIssueCode fromValue(String v) {
        return valueOf(v);
    }

}
