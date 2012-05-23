
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IssueFilterCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IssueFilterCode">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ISSFA"/>
 *     &lt;enumeration value="ISSFI"/>
 *     &lt;enumeration value="ISSFU"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IssueFilterCode")
@XmlEnum
public enum IssueFilterCode {

    ISSFA,
    ISSFI,
    ISSFU;

    public String value() {
        return name();
    }

    public static IssueFilterCode fromValue(String v) {
        return valueOf(v);
    }

}
