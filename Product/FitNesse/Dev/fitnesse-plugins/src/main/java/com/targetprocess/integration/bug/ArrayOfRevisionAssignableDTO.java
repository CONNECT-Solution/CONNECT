
package com.targetprocess.integration.bug;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRevisionAssignableDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRevisionAssignableDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RevisionAssignableDTO" type="{http://targetprocess.com}RevisionAssignableDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRevisionAssignableDTO", propOrder = {
    "revisionAssignableDTO"
})
public class ArrayOfRevisionAssignableDTO {

    @XmlElement(name = "RevisionAssignableDTO", required = true, nillable = true)
    protected List<RevisionAssignableDTO> revisionAssignableDTO;

    /**
     * Gets the value of the revisionAssignableDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the revisionAssignableDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRevisionAssignableDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RevisionAssignableDTO }
     * 
     * 
     */
    public List<RevisionAssignableDTO> getRevisionAssignableDTO() {
        if (revisionAssignableDTO == null) {
            revisionAssignableDTO = new ArrayList<RevisionAssignableDTO>();
        }
        return this.revisionAssignableDTO;
    }

}
