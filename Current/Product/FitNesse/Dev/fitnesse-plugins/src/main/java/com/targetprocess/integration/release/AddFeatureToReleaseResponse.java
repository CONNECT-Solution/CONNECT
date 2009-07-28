
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
 *         &lt;element name="AddFeatureToReleaseResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addFeatureToReleaseResult"
})
@XmlRootElement(name = "AddFeatureToReleaseResponse")
public class AddFeatureToReleaseResponse {

    @XmlElement(name = "AddFeatureToReleaseResult")
    protected int addFeatureToReleaseResult;

    /**
     * Gets the value of the addFeatureToReleaseResult property.
     * 
     */
    public int getAddFeatureToReleaseResult() {
        return addFeatureToReleaseResult;
    }

    /**
     * Sets the value of the addFeatureToReleaseResult property.
     * 
     */
    public void setAddFeatureToReleaseResult(int value) {
        this.addFeatureToReleaseResult = value;
    }

}
