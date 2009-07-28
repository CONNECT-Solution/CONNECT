
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
 *         &lt;element name="AddCommentToReleaseResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addCommentToReleaseResult"
})
@XmlRootElement(name = "AddCommentToReleaseResponse")
public class AddCommentToReleaseResponse {

    @XmlElement(name = "AddCommentToReleaseResult")
    protected int addCommentToReleaseResult;

    /**
     * Gets the value of the addCommentToReleaseResult property.
     * 
     */
    public int getAddCommentToReleaseResult() {
        return addCommentToReleaseResult;
    }

    /**
     * Sets the value of the addCommentToReleaseResult property.
     * 
     */
    public void setAddCommentToReleaseResult(int value) {
        this.addCommentToReleaseResult = value;
    }

}
