
package ihe.iti.xds_b._2007;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;


/**
 * <p>Java class for RetrieveDocumentSetResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetrieveDocumentSetResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryResponse"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="DocumentResponse" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="NewRepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="NewDocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
 *                     &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
 *                     &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
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
@XmlType(name = "RetrieveDocumentSetResponseType", propOrder = {
    "registryResponse",
    "documentResponse"
})
public class RetrieveDocumentSetResponseType {

    @XmlElement(name = "RegistryResponse", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0", required = true)
    protected RegistryResponseType registryResponse;
    @XmlElement(name = "DocumentResponse")
    protected List<RetrieveDocumentSetResponseType.DocumentResponse> documentResponse;

    /**
     * Gets the value of the registryResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RegistryResponseType }
     *     
     */
    public RegistryResponseType getRegistryResponse() {
        return registryResponse;
    }

    /**
     * Sets the value of the registryResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistryResponseType }
     *     
     */
    public void setRegistryResponse(RegistryResponseType value) {
        this.registryResponse = value;
    }

    /**
     * Gets the value of the documentResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RetrieveDocumentSetResponseType.DocumentResponse }
     * 
     * 
     */
    public List<RetrieveDocumentSetResponseType.DocumentResponse> getDocumentResponse() {
        if (documentResponse == null) {
            documentResponse = new ArrayList<RetrieveDocumentSetResponseType.DocumentResponse>();
        }
        return this.documentResponse;
    }


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
     *         &lt;element name="HomeCommunityId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="RepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="DocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="NewRepositoryUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="NewDocumentUniqueId" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName" minOccurs="0"/>
     *         &lt;element name="mimeType" type="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}LongName"/>
     *         &lt;element name="Document" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
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
        "homeCommunityId",
        "repositoryUniqueId",
        "documentUniqueId",
        "newRepositoryUniqueId",
        "newDocumentUniqueId",
        "mimeType",
        "document"
    })
    public static class DocumentResponse {

        @XmlElement(name = "HomeCommunityId")
        protected String homeCommunityId;
        @XmlElement(name = "RepositoryUniqueId", required = true)
        protected String repositoryUniqueId;
        @XmlElement(name = "DocumentUniqueId", required = true)
        protected String documentUniqueId;
        @XmlElement(name = "NewRepositoryUniqueId")
        protected String newRepositoryUniqueId;
        @XmlElement(name = "NewDocumentUniqueId")
        protected String newDocumentUniqueId;
        @XmlElement(required = true)
        protected String mimeType;
        @XmlElement(name = "Document", required = true)
        protected byte[] document;

        /**
         * Gets the value of the homeCommunityId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHomeCommunityId() {
            return homeCommunityId;
        }

        /**
         * Sets the value of the homeCommunityId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHomeCommunityId(String value) {
            this.homeCommunityId = value;
        }

        /**
         * Gets the value of the repositoryUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRepositoryUniqueId() {
            return repositoryUniqueId;
        }

        /**
         * Sets the value of the repositoryUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRepositoryUniqueId(String value) {
            this.repositoryUniqueId = value;
        }

        /**
         * Gets the value of the documentUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentUniqueId() {
            return documentUniqueId;
        }

        /**
         * Sets the value of the documentUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentUniqueId(String value) {
            this.documentUniqueId = value;
        }

        /**
         * Gets the value of the newRepositoryUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNewRepositoryUniqueId() {
            return newRepositoryUniqueId;
        }

        /**
         * Sets the value of the newRepositoryUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNewRepositoryUniqueId(String value) {
            this.newRepositoryUniqueId = value;
        }

        /**
         * Gets the value of the newDocumentUniqueId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNewDocumentUniqueId() {
            return newDocumentUniqueId;
        }

        /**
         * Sets the value of the newDocumentUniqueId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNewDocumentUniqueId(String value) {
            this.newDocumentUniqueId = value;
        }

        /**
         * Gets the value of the mimeType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMimeType() {
            return mimeType;
        }

        /**
         * Sets the value of the mimeType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMimeType(String value) {
            this.mimeType = value;
        }

        /**
         * Gets the value of the document property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getDocument() {
            return document;
        }

        /**
         * Sets the value of the document property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setDocument(byte[] value) {
            this.document = value;
        }

    }

}
