
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClassificationNodeQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClassificationNodeQueryType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType">
 *       &lt;sequence>
 *         &lt;element name="ParentQuery" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}RegistryObjectQueryType" minOccurs="0"/>
 *         &lt;element name="ChildrenQuery" type="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ClassificationNodeQueryType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClassificationNodeQueryType", propOrder = {
    "parentQuery",
    "childrenQuery"
})
public class ClassificationNodeQueryType
    extends RegistryObjectQueryType
{

    @XmlElement(name = "ParentQuery")
    protected RegistryObjectQueryType parentQuery;
    @XmlElement(name = "ChildrenQuery")
    protected List<ClassificationNodeQueryType> childrenQuery;

    /**
     * Gets the value of the parentQuery property.
     * 
     * @return
     *     possible object is
     *     {@link RegistryObjectQueryType }
     *     
     */
    public RegistryObjectQueryType getParentQuery() {
        return parentQuery;
    }

    /**
     * Sets the value of the parentQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistryObjectQueryType }
     *     
     */
    public void setParentQuery(RegistryObjectQueryType value) {
        this.parentQuery = value;
    }

    /**
     * Gets the value of the childrenQuery property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the childrenQuery property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChildrenQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClassificationNodeQueryType }
     * 
     * 
     */
    public List<ClassificationNodeQueryType> getChildrenQuery() {
        if (childrenQuery == null) {
            childrenQuery = new ArrayList<ClassificationNodeQueryType>();
        }
        return this.childrenQuery;
    }

}
