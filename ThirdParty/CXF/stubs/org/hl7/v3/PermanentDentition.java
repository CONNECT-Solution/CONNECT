
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PermanentDentition.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PermanentDentition">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TID1"/>
 *     &lt;enumeration value="TID10"/>
 *     &lt;enumeration value="TID11"/>
 *     &lt;enumeration value="TID12"/>
 *     &lt;enumeration value="TID13"/>
 *     &lt;enumeration value="TID14"/>
 *     &lt;enumeration value="TID15"/>
 *     &lt;enumeration value="TID16"/>
 *     &lt;enumeration value="TID17"/>
 *     &lt;enumeration value="TID17d"/>
 *     &lt;enumeration value="TID17m"/>
 *     &lt;enumeration value="TID18"/>
 *     &lt;enumeration value="TID18d"/>
 *     &lt;enumeration value="TID18m"/>
 *     &lt;enumeration value="TID19"/>
 *     &lt;enumeration value="TID19d"/>
 *     &lt;enumeration value="TID19m"/>
 *     &lt;enumeration value="TID2"/>
 *     &lt;enumeration value="TID20"/>
 *     &lt;enumeration value="TID21"/>
 *     &lt;enumeration value="TID22"/>
 *     &lt;enumeration value="TID23"/>
 *     &lt;enumeration value="TID24"/>
 *     &lt;enumeration value="TID25"/>
 *     &lt;enumeration value="TID26"/>
 *     &lt;enumeration value="TID27"/>
 *     &lt;enumeration value="TID28"/>
 *     &lt;enumeration value="TID29"/>
 *     &lt;enumeration value="TID3"/>
 *     &lt;enumeration value="TID30"/>
 *     &lt;enumeration value="TID30d"/>
 *     &lt;enumeration value="TID30m"/>
 *     &lt;enumeration value="TID31"/>
 *     &lt;enumeration value="TID31d"/>
 *     &lt;enumeration value="TID31m"/>
 *     &lt;enumeration value="TID32"/>
 *     &lt;enumeration value="TID32d"/>
 *     &lt;enumeration value="TID32m"/>
 *     &lt;enumeration value="TID4"/>
 *     &lt;enumeration value="TID5"/>
 *     &lt;enumeration value="TID6"/>
 *     &lt;enumeration value="TID7"/>
 *     &lt;enumeration value="TID8"/>
 *     &lt;enumeration value="TID9"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PermanentDentition")
@XmlEnum
public enum PermanentDentition {

    @XmlEnumValue("TID1")
    TID_1("TID1"),
    @XmlEnumValue("TID10")
    TID_10("TID10"),
    @XmlEnumValue("TID11")
    TID_11("TID11"),
    @XmlEnumValue("TID12")
    TID_12("TID12"),
    @XmlEnumValue("TID13")
    TID_13("TID13"),
    @XmlEnumValue("TID14")
    TID_14("TID14"),
    @XmlEnumValue("TID15")
    TID_15("TID15"),
    @XmlEnumValue("TID16")
    TID_16("TID16"),
    @XmlEnumValue("TID17")
    TID_17("TID17"),
    @XmlEnumValue("TID17d")
    TID_17_D("TID17d"),
    @XmlEnumValue("TID17m")
    TID_17_M("TID17m"),
    @XmlEnumValue("TID18")
    TID_18("TID18"),
    @XmlEnumValue("TID18d")
    TID_18_D("TID18d"),
    @XmlEnumValue("TID18m")
    TID_18_M("TID18m"),
    @XmlEnumValue("TID19")
    TID_19("TID19"),
    @XmlEnumValue("TID19d")
    TID_19_D("TID19d"),
    @XmlEnumValue("TID19m")
    TID_19_M("TID19m"),
    @XmlEnumValue("TID2")
    TID_2("TID2"),
    @XmlEnumValue("TID20")
    TID_20("TID20"),
    @XmlEnumValue("TID21")
    TID_21("TID21"),
    @XmlEnumValue("TID22")
    TID_22("TID22"),
    @XmlEnumValue("TID23")
    TID_23("TID23"),
    @XmlEnumValue("TID24")
    TID_24("TID24"),
    @XmlEnumValue("TID25")
    TID_25("TID25"),
    @XmlEnumValue("TID26")
    TID_26("TID26"),
    @XmlEnumValue("TID27")
    TID_27("TID27"),
    @XmlEnumValue("TID28")
    TID_28("TID28"),
    @XmlEnumValue("TID29")
    TID_29("TID29"),
    @XmlEnumValue("TID3")
    TID_3("TID3"),
    @XmlEnumValue("TID30")
    TID_30("TID30"),
    @XmlEnumValue("TID30d")
    TID_30_D("TID30d"),
    @XmlEnumValue("TID30m")
    TID_30_M("TID30m"),
    @XmlEnumValue("TID31")
    TID_31("TID31"),
    @XmlEnumValue("TID31d")
    TID_31_D("TID31d"),
    @XmlEnumValue("TID31m")
    TID_31_M("TID31m"),
    @XmlEnumValue("TID32")
    TID_32("TID32"),
    @XmlEnumValue("TID32d")
    TID_32_D("TID32d"),
    @XmlEnumValue("TID32m")
    TID_32_M("TID32m"),
    @XmlEnumValue("TID4")
    TID_4("TID4"),
    @XmlEnumValue("TID5")
    TID_5("TID5"),
    @XmlEnumValue("TID6")
    TID_6("TID6"),
    @XmlEnumValue("TID7")
    TID_7("TID7"),
    @XmlEnumValue("TID8")
    TID_8("TID8"),
    @XmlEnumValue("TID9")
    TID_9("TID9");
    private final String value;

    PermanentDentition(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PermanentDentition fromValue(String v) {
        for (PermanentDentition c: PermanentDentition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
