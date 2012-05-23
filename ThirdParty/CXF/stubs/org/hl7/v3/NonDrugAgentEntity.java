
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NonDrugAgentEntity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NonDrugAgentEntity">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="NDA16"/>
 *     &lt;enumeration value="NDA17"/>
 *     &lt;enumeration value="NDA01"/>
 *     &lt;enumeration value="NDA02"/>
 *     &lt;enumeration value="NDA08"/>
 *     &lt;enumeration value="NDA03"/>
 *     &lt;enumeration value="NDA12"/>
 *     &lt;enumeration value="NDA10"/>
 *     &lt;enumeration value="NDA04"/>
 *     &lt;enumeration value="NDA13"/>
 *     &lt;enumeration value="NDA09"/>
 *     &lt;enumeration value="NDA05"/>
 *     &lt;enumeration value="NDA14"/>
 *     &lt;enumeration value="NDA06"/>
 *     &lt;enumeration value="NDA15"/>
 *     &lt;enumeration value="NDA11"/>
 *     &lt;enumeration value="NDA07"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NonDrugAgentEntity")
@XmlEnum
public enum NonDrugAgentEntity {

    @XmlEnumValue("NDA16")
    NDA_16("NDA16"),
    @XmlEnumValue("NDA17")
    NDA_17("NDA17"),
    @XmlEnumValue("NDA01")
    NDA_01("NDA01"),
    @XmlEnumValue("NDA02")
    NDA_02("NDA02"),
    @XmlEnumValue("NDA08")
    NDA_08("NDA08"),
    @XmlEnumValue("NDA03")
    NDA_03("NDA03"),
    @XmlEnumValue("NDA12")
    NDA_12("NDA12"),
    @XmlEnumValue("NDA10")
    NDA_10("NDA10"),
    @XmlEnumValue("NDA04")
    NDA_04("NDA04"),
    @XmlEnumValue("NDA13")
    NDA_13("NDA13"),
    @XmlEnumValue("NDA09")
    NDA_09("NDA09"),
    @XmlEnumValue("NDA05")
    NDA_05("NDA05"),
    @XmlEnumValue("NDA14")
    NDA_14("NDA14"),
    @XmlEnumValue("NDA06")
    NDA_06("NDA06"),
    @XmlEnumValue("NDA15")
    NDA_15("NDA15"),
    @XmlEnumValue("NDA11")
    NDA_11("NDA11"),
    @XmlEnumValue("NDA07")
    NDA_07("NDA07");
    private final String value;

    NonDrugAgentEntity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NonDrugAgentEntity fromValue(String v) {
        for (NonDrugAgentEntity c: NonDrugAgentEntity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
