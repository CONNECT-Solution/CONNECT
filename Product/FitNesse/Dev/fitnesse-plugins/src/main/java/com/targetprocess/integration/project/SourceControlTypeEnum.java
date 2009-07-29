
package com.targetprocess.integration.project;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * <p>Java class for SourceControlTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SourceControlTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Subversion"/>
 *     &lt;enumeration value="SourceSafe"/>
 *     &lt;enumeration value="Perforce"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum SourceControlTypeEnum {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Perforce")
    PERFORCE("Perforce"),
    @XmlEnumValue("SourceSafe")
    SOURCE_SAFE("SourceSafe"),
    @XmlEnumValue("Subversion")
    SUBVERSION("Subversion");
    private final String value;

    SourceControlTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SourceControlTypeEnum fromValue(String v) {
        for (SourceControlTypeEnum c: SourceControlTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
