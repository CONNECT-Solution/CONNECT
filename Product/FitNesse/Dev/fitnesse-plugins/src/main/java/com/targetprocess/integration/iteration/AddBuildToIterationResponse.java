
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
 *         &lt;element name="AddBuildToIterationResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addBuildToIterationResult"
})
@XmlRootElement(name = "AddBuildToIterationResponse")
public class AddBuildToIterationResponse {

    @XmlElement(name = "AddBuildToIterationResult")
    protected int addBuildToIterationResult;

    /**
     * Gets the value of the addBuildToIterationResult property.
     * 
     */
    public int getAddBuildToIterationResult() {
        return addBuildToIterationResult;
    }

    /**
     * Sets the value of the addBuildToIterationResult property.
     * 
     */
    public void setAddBuildToIterationResult(int value) {
        this.addBuildToIterationResult = value;
    }

}
