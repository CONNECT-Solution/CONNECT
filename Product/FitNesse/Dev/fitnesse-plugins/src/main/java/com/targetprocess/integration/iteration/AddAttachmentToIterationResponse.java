
package com.targetprocess.integration.iteration;

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
 *         &lt;element name="AddAttachmentToIterationResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addAttachmentToIterationResult"
})
@XmlRootElement(name = "AddAttachmentToIterationResponse")
public class AddAttachmentToIterationResponse {

    @XmlElement(name = "AddAttachmentToIterationResult")
    protected int addAttachmentToIterationResult;

    /**
     * Gets the value of the addAttachmentToIterationResult property.
     * 
     */
    public int getAddAttachmentToIterationResult() {
        return addAttachmentToIterationResult;
    }

    /**
     * Sets the value of the addAttachmentToIterationResult property.
     * 
     */
    public void setAddAttachmentToIterationResult(int value) {
        this.addAttachmentToIterationResult = value;
    }

}
