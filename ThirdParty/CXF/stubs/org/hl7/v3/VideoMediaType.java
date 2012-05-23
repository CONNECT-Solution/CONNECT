
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VideoMediaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VideoMediaType">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="video/mpeg"/>
 *     &lt;enumeration value="video/x-avi"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VideoMediaType")
@XmlEnum
public enum VideoMediaType {

    @XmlEnumValue("video/mpeg")
    VIDEO_MPEG("video/mpeg"),
    @XmlEnumValue("video/x-avi")
    VIDEO_X_AVI("video/x-avi");
    private final String value;

    VideoMediaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VideoMediaType fromValue(String v) {
        for (VideoMediaType c: VideoMediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
