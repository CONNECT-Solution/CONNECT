
package com.targetprocess.integration.release;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddAttachmentToReleaseResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "addAttachmentToReleaseResult"
})
@XmlRootElement(name = "AddAttachmentToReleaseResponse")
public class AddAttachmentToReleaseResponse {

    @XmlElement(name = "AddAttachmentToReleaseResult")
    protected int addAttachmentToReleaseResult;

    /**
     * Gets the value of the addAttachmentToReleaseResult property.
     * 
     */
    public int getAddAttachmentToReleaseResult() {
        return addAttachmentToReleaseResult;
    }

    /**
     * Sets the value of the addAttachmentToReleaseResult property.
     * 
     */
    public void setAddAttachmentToReleaseResult(int value) {
        this.addAttachmentToReleaseResult = value;
    }

}
