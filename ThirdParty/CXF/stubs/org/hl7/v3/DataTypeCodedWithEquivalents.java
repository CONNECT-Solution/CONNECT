
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataTypeCodedWithEquivalents.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataTypeCodedWithEquivalents">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="CE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataTypeCodedWithEquivalents")
@XmlEnum
public enum DataTypeCodedWithEquivalents {

    CE;

    public String value() {
        return name();
    }

    public static DataTypeCodedWithEquivalents fromValue(String v) {
        return valueOf(v);
    }

}
