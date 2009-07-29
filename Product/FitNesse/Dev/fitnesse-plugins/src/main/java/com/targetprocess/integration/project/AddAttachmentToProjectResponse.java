
package com.targetprocess.integration.project;

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
 *         &lt;element name="AddAttachmentToProjectResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addAttachmentToProjectResult"
})
@XmlRootElement(name = "AddAttachmentToProjectResponse")
public class AddAttachmentToProjectResponse {

    @XmlElement(name = "AddAttachmentToProjectResult")
    protected int addAttachmentToProjectResult;

    /**
     * Gets the value of the addAttachmentToProjectResult property.
     * 
     */
    public int getAddAttachmentToProjectResult() {
        return addAttachmentToProjectResult;
    }

    /**
     * Sets the value of the addAttachmentToProjectResult property.
     * 
     */
    public void setAddAttachmentToProjectResult(int value) {
        this.addAttachmentToProjectResult = value;
    }

}
