
package com.targetprocess.integration.userstory;

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
 *         &lt;element name="AddAttachmentToUserStoryResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addAttachmentToUserStoryResult"
})
@XmlRootElement(name = "AddAttachmentToUserStoryResponse")
public class AddAttachmentToUserStoryResponse {

    @XmlElement(name = "AddAttachmentToUserStoryResult")
    protected int addAttachmentToUserStoryResult;

    /**
     * Gets the value of the addAttachmentToUserStoryResult property.
     * 
     */
    public int getAddAttachmentToUserStoryResult() {
        return addAttachmentToUserStoryResult;
    }

    /**
     * Sets the value of the addAttachmentToUserStoryResult property.
     * 
     */
    public void setAddAttachmentToUserStoryResult(int value) {
        this.addAttachmentToUserStoryResult = value;
    }

}
