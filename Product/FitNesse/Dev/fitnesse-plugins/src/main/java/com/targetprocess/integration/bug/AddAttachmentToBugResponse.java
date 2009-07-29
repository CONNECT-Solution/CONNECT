
package com.targetprocess.integration.bug;

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
 *         &lt;element name="AddAttachmentToBugResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addAttachmentToBugResult"
})
@XmlRootElement(name = "AddAttachmentToBugResponse")
public class AddAttachmentToBugResponse {

    @XmlElement(name = "AddAttachmentToBugResult")
    protected int addAttachmentToBugResult;

    /**
     * Gets the value of the addAttachmentToBugResult property.
     * 
     */
    public int getAddAttachmentToBugResult() {
        return addAttachmentToBugResult;
    }

    /**
     * Sets the value of the addAttachmentToBugResult property.
     * 
     */
    public void setAddAttachmentToBugResult(int value) {
        this.addAttachmentToBugResult = value;
    }

}
