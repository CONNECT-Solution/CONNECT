
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoleLinkRelated.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RoleLinkRelated">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="REL"/>
 *     &lt;enumeration value="IDENT"/>
 *     &lt;enumeration value="DIRAUTH"/>
 *     &lt;enumeration value="INDAUTH"/>
 *     &lt;enumeration value="PART"/>
 *     &lt;enumeration value="BACKUP"/>
 *     &lt;enumeration value="REPL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RoleLinkRelated")
@XmlEnum
public enum RoleLinkRelated {

    REL,
    IDENT,
    DIRAUTH,
    INDAUTH,
    PART,
    BACKUP,
    REPL;

    public String value() {
        return name();
    }

    public static RoleLinkRelated fromValue(String v) {
        return valueOf(v);
    }

}
