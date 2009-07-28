
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
 *         &lt;element name="AddIterationToReleaseResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addIterationToReleaseResult"
})
@XmlRootElement(name = "AddIterationToReleaseResponse")
public class AddIterationToReleaseResponse {

    @XmlElement(name = "AddIterationToReleaseResult")
    protected int addIterationToReleaseResult;

    /**
     * Gets the value of the addIterationToReleaseResult property.
     * 
     */
    public int getAddIterationToReleaseResult() {
        return addIterationToReleaseResult;
    }

    /**
     * Sets the value of the addIterationToReleaseResult property.
     * 
     */
    public void setAddIterationToReleaseResult(int value) {
        this.addIterationToReleaseResult = value;
    }

}
