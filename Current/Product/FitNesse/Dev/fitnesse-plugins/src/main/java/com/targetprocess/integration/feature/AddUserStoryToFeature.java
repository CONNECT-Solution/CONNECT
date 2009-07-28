
package com.targetprocess.integration.feature;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="featureID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="userStory" type="{http://targetprocess.com}UserStoryDTO" minOccurs="0"/>
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
    "featureID",
    "userStory"
})
@XmlRootElement(name = "AddUserStoryToFeature")
public class AddUserStoryToFeature {

    protected int featureID;
    protected UserStoryDTO userStory;

    /**
     * Gets the value of the featureID property.
     * 
     */
    public int getFeatureID() {
        return featureID;
    }

    /**
     * Sets the value of the featureID property.
     * 
     */
    public void setFeatureID(int value) {
        this.featureID = value;
    }

    /**
     * Gets the value of the userStory property.
     * 
     * @return
     *     possible object is
     *     {@link UserStoryDTO }
     *     
     */
    public UserStoryDTO getUserStory() {
        return userStory;
    }

    /**
     * Sets the value of the userStory property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserStoryDTO }
     *     
     */
    public void setUserStory(UserStoryDTO value) {
        this.userStory = value;
    }

}
