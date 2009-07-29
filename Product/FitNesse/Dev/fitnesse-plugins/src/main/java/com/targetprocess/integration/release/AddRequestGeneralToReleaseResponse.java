
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
 *         &lt;element name="AddRequestGeneralToReleaseResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addRequestGeneralToReleaseResult"
})
@XmlRootElement(name = "AddRequestGeneralToReleaseResponse")
public class AddRequestGeneralToReleaseResponse {

    @XmlElement(name = "AddRequestGeneralToReleaseResult")
    protected int addRequestGeneralToReleaseResult;

    /**
     * Gets the value of the addRequestGeneralToReleaseResult property.
     * 
     */
    public int getAddRequestGeneralToReleaseResult() {
        return addRequestGeneralToReleaseResult;
    }

    /**
     * Sets the value of the addRequestGeneralToReleaseResult property.
     * 
     */
    public void setAddRequestGeneralToReleaseResult(int value) {
        this.addRequestGeneralToReleaseResult = value;
    }

}
