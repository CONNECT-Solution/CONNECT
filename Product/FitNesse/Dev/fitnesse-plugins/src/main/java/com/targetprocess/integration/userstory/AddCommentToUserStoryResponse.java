
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
 *         &lt;element name="AddCommentToUserStoryResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addCommentToUserStoryResult"
})
@XmlRootElement(name = "AddCommentToUserStoryResponse")
public class AddCommentToUserStoryResponse {

    @XmlElement(name = "AddCommentToUserStoryResult")
    protected int addCommentToUserStoryResult;

    /**
     * Gets the value of the addCommentToUserStoryResult property.
     * 
     */
    public int getAddCommentToUserStoryResult() {
        return addCommentToUserStoryResult;
    }

    /**
     * Sets the value of the addCommentToUserStoryResult property.
     * 
     */
    public void setAddCommentToUserStoryResult(int value) {
        this.addCommentToUserStoryResult = value;
    }

}
