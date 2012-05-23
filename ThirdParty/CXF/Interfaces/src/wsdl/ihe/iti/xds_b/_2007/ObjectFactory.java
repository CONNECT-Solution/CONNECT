
package ihe.iti.xds_b._2007;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ihe.iti.xds_b._2007 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RetrieveDocumentSetRequest_QNAME = new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetRequest");
    private final static QName _ProvideAndRegisterDocumentSetRequest_QNAME = new QName("urn:ihe:iti:xds-b:2007", "ProvideAndRegisterDocumentSetRequest");
    private final static QName _RetrieveDocumentSetResponse_QNAME = new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ihe.iti.xds_b._2007
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequestType }
     * 
     */
    public ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequestType() {
        return new ProvideAndRegisterDocumentSetRequestType();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequestType }
     * 
     */
    public RetrieveDocumentSetRequestType createRetrieveDocumentSetRequestType() {
        return new RetrieveDocumentSetRequestType();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponseType }
     * 
     */
    public RetrieveDocumentSetResponseType createRetrieveDocumentSetResponseType() {
        return new RetrieveDocumentSetResponseType();
    }

    /**
     * Create an instance of {@link ProvideAndRegisterDocumentSetRequestType.Document }
     * 
     */
    public ProvideAndRegisterDocumentSetRequestType.Document createProvideAndRegisterDocumentSetRequestTypeDocument() {
        return new ProvideAndRegisterDocumentSetRequestType.Document();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetRequestType.DocumentRequest }
     * 
     */
    public RetrieveDocumentSetRequestType.DocumentRequest createRetrieveDocumentSetRequestTypeDocumentRequest() {
        return new RetrieveDocumentSetRequestType.DocumentRequest();
    }

    /**
     * Create an instance of {@link RetrieveDocumentSetResponseType.DocumentResponse }
     * 
     */
    public RetrieveDocumentSetResponseType.DocumentResponse createRetrieveDocumentSetResponseTypeDocumentResponse() {
        return new RetrieveDocumentSetResponseType.DocumentResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveDocumentSetRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "RetrieveDocumentSetRequest")
    public JAXBElement<RetrieveDocumentSetRequestType> createRetrieveDocumentSetRequest(RetrieveDocumentSetRequestType value) {
        return new JAXBElement<RetrieveDocumentSetRequestType>(_RetrieveDocumentSetRequest_QNAME, RetrieveDocumentSetRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvideAndRegisterDocumentSetRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "ProvideAndRegisterDocumentSetRequest")
    public JAXBElement<ProvideAndRegisterDocumentSetRequestType> createProvideAndRegisterDocumentSetRequest(ProvideAndRegisterDocumentSetRequestType value) {
        return new JAXBElement<ProvideAndRegisterDocumentSetRequestType>(_ProvideAndRegisterDocumentSetRequest_QNAME, ProvideAndRegisterDocumentSetRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveDocumentSetResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ihe:iti:xds-b:2007", name = "RetrieveDocumentSetResponse")
    public JAXBElement<RetrieveDocumentSetResponseType> createRetrieveDocumentSetResponse(RetrieveDocumentSetResponseType value) {
        return new JAXBElement<RetrieveDocumentSetResponseType>(_RetrieveDocumentSetResponse_QNAME, RetrieveDocumentSetResponseType.class, null, value);
    }

}
