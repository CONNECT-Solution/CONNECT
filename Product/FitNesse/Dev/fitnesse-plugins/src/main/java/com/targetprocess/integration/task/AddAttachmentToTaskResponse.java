
package com.targetprocess.integration.task;

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
 *         &lt;element name="AddAttachmentToTaskResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addAttachmentToTaskResult"
})
@XmlRootElement(name = "AddAttachmentToTaskResponse")
public class AddAttachmentToTaskResponse {

    @XmlElement(name = "AddAttachmentToTaskResult")
    protected int addAttachmentToTaskResult;

    /**
     * Gets the value of the addAttachmentToTaskResult property.
     * 
     */
    public int getAddAttachmentToTaskResult() {
        return addAttachmentToTaskResult;
    }

    /**
     * Sets the value of the addAttachmentToTaskResult property.
     * 
     */
    public void setAddAttachmentToTaskResult(int value) {
        this.addAttachmentToTaskResult = value;
    }

}
