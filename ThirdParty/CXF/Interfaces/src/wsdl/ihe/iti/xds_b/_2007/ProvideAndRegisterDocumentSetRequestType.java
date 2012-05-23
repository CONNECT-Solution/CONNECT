
package ihe.iti.xds_b._2007;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;


/**
 * <p>Java class for ProvideAndRegisterDocumentSetRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProvideAndRegisterDocumentSetRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0}SubmitObjectsRequest"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="Document" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
 *                   &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProvideAndRegisterDocumentSetRequestType", propOrder = {
    "submitObjectsRequest",
    "document"
})
public class ProvideAndRegisterDocumentSetRequestType {

    @XmlElement(name = "SubmitObjectsRequest", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0", required = true)
    protected SubmitObjectsRequest submitObjectsRequest;
    @XmlElement(name = "Document")
    protected List<ProvideAndRegisterDocumentSetRequestType.Document> document;

    /**
     * Gets the value of the submitObjectsRequest property.
     * 
     * @return
     *     possible object is
     *     {@link SubmitObjectsRequest }
     *     
     */
    public SubmitObjectsRequest getSubmitObjectsRequest() {
        return submitObjectsRequest;
    }

    /**
     * Sets the value of the submitObjectsRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubmitObjectsRequest }
     *     
     */
    public void setSubmitObjectsRequest(SubmitObjectsRequest value) {
        this.submitObjectsRequest = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the document property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocument().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProvideAndRegisterDocumentSetRequestType.Document }
     * 
     * 
     */
    public List<ProvideAndRegisterDocumentSetRequestType.Document> getDocument() {
        if (document == null) {
            document = new ArrayList<ProvideAndRegisterDocumentSetRequestType.Document>();
        }
        return this.document;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
     *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Document {

        @XmlValue
        protected byte[] value;
        @XmlAttribute(name = "id", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String id;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setValue(byte[] value) {
            this.value = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

    }

}
