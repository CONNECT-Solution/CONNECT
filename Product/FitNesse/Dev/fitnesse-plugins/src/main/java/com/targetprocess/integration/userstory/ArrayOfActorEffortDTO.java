
package com.targetprocess.integration.userstory;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfActorEffortDTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfActorEffortDTO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActorEffortDTO" type="{http://targetprocess.com}ActorEffortDTO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfActorEffortDTO", propOrder = {
    "actorEffortDTO"
})
public class ArrayOfActorEffortDTO {

    @XmlElement(name = "ActorEffortDTO", required = true, nillable = true)
    protected List<ActorEffortDTO> actorEffortDTO;

    /**
     * Gets the value of the actorEffortDTO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actorEffortDTO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActorEffortDTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActorEffortDTO }
     * 
     * 
     */
    public List<ActorEffortDTO> getActorEffortDTO() {
        if (actorEffortDTO == null) {
            actorEffortDTO = new ArrayList<ActorEffortDTO>();
        }
        return this.actorEffortDTO;
    }

}
