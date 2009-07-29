
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
 *         &lt;element name="AddFeatureToProjectResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addFeatureToProjectResult"
})
@XmlRootElement(name = "AddFeatureToProjectResponse")
public class AddFeatureToProjectResponse {

    @XmlElement(name = "AddFeatureToProjectResult")
    protected int addFeatureToProjectResult;

    /**
     * Gets the value of the addFeatureToProjectResult property.
     * 
     */
    public int getAddFeatureToProjectResult() {
        return addFeatureToProjectResult;
    }

    /**
     * Sets the value of the addFeatureToProjectResult property.
     * 
     */
    public void setAddFeatureToProjectResult(int value) {
        this.addFeatureToProjectResult = value;
    }

}
