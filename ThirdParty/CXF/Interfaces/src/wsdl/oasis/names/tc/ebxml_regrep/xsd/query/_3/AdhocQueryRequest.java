
package oasis.names.tc.ebxml_regrep.xsd.query._3;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryRequestType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0}RegistryRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0}ResponseOption"/>
 *         &lt;element ref="{urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0}AdhocQuery"/>
 *       &lt;/sequence>
 *       &lt;attribute name="federated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="federation" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="startIndex" type="{http://www.w3.org/2001/XMLSchema}integer" default="0" />
 *       &lt;attribute name="maxResults" type="{http://www.w3.org/2001/XMLSchema}integer" default="-1" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "responseOption",
    "adhocQuery"
})
@XmlRootElement(name = "AdhocQueryRequest")
public class AdhocQueryRequest
    extends RegistryRequestType
{

    @XmlElement(name = "ResponseOption", required = true)
    protected ResponseOptionType responseOption;
    @XmlElement(name = "AdhocQuery", namespace = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", required = true)
    protected AdhocQueryType adhocQuery;
    @XmlAttribute(name = "federated")
    protected Boolean federated;
    @XmlAttribute(name = "federation")
    @XmlSchemaType(name = "anyURI")
    protected String federation;
    @XmlAttribute(name = "startIndex")
    protected BigInteger startIndex;
    @XmlAttribute(name = "maxResults")
    protected BigInteger maxResults;

    /**
     * Gets the value of the responseOption property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseOptionType }
     *     
     */
    public ResponseOptionType getResponseOption() {
        return responseOption;
    }

    /**
     * Sets the value of the responseOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseOptionType }
     *     
     */
    public void setResponseOption(ResponseOptionType value) {
        this.responseOption = value;
    }

    /**
     * Gets the value of the adhocQuery property.
     * 
     * @return
     *     possible object is
     *     {@link AdhocQueryType }
     *     
     */
    public AdhocQueryType getAdhocQuery() {
        return adhocQuery;
    }

    /**
     * Sets the value of the adhocQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdhocQueryType }
     *     
     */
    public void setAdhocQuery(AdhocQueryType value) {
        this.adhocQuery = value;
    }

    /**
     * Gets the value of the federated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFederated() {
        if (federated == null) {
            return false;
        } else {
            return federated;
        }
    }

    /**
     * Sets the value of the federated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFederated(Boolean value) {
        this.federated = value;
    }

    /**
     * Gets the value of the federation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFederation() {
        return federation;
    }

    /**
     * Sets the value of the federation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFederation(String value) {
        this.federation = value;
    }

    /**
     * Gets the value of the startIndex property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStartIndex() {
        if (startIndex == null) {
            return new BigInteger("0");
        } else {
            return startIndex;
        }
    }

    /**
     * Sets the value of the startIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStartIndex(BigInteger value) {
        this.startIndex = value;
    }

    /**
     * Gets the value of the maxResults property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxResults() {
        if (maxResults == null) {
            return new BigInteger("-1");
        } else {
            return maxResults;
        }
    }

    /**
     * Sets the value of the maxResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxResults(BigInteger value) {
        this.maxResults = value;
    }

}
