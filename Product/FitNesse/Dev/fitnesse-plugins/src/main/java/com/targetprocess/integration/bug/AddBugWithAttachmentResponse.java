
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
 *         &lt;element name="AddBugWithAttachmentResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addBugWithAttachmentResult"
})
@XmlRootElement(name = "AddBugWithAttachmentResponse")
public class AddBugWithAttachmentResponse {

    @XmlElement(name = "AddBugWithAttachmentResult")
    protected int addBugWithAttachmentResult;

    /**
     * Gets the value of the addBugWithAttachmentResult property.
     * 
     */
    public int getAddBugWithAttachmentResult() {
        return addBugWithAttachmentResult;
    }

    /**
     * Sets the value of the addBugWithAttachmentResult property.
     * 
     */
    public void setAddBugWithAttachmentResult(int value) {
        this.addBugWithAttachmentResult = value;
    }

}
