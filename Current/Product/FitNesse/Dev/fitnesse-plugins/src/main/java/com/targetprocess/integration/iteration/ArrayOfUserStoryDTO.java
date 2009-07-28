
package com.targetprocess.integration.iteration;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfUserStoryDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfUserStoryDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserStoryDTO" type="{http://targetprocess.com}UserStoryDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfUserStoryDTO", propOrder = {
    "userStoryDTO"
})
public class ArrayOfUserStoryDTO {

    @XmlElement(name = "UserStoryDTO", required = true, nillable = true)
    protected List<UserStoryDTO> userStoryDTO;

    /**
     * Gets the value of the userStoryDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userStoryDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserStoryDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserStoryDTO }
     * 
     * 
     */
    public List<UserStoryDTO> getUserStoryDTO() {
        if (userStoryDTO == null) {
            userStoryDTO = new ArrayList<UserStoryDTO>();
        }
        return this.userStoryDTO;
    }

}
