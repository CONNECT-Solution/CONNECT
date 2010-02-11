package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class is used to serialize/deserialize teh XACML documents.
 *
 * @author Les Westberg
 */
public class XACMLSerializer
{
    private Log log = null;
//    private static JAXBContext oJaxbContextXACML = null;
//    private static Marshaller oXACMLMarshaller = null;
//    private static Unmarshaller oXACMLUnmarshaller = null;

    /**
     * Default constructor.
     */
    public XACMLSerializer()
    {
        log = createLogger();
    }

    /**
     * Sets up the logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));

    }


    /**
     * This method takes in an object representation of the XACML Policy
     * and serializes it to a text string representation of the document.
     *
     * @param oConsentXACML The object representation of the XACML Consent Policy.
     * @return The textual string representation of the XACML Consent document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This exception is thrown if an error occurs.
     */
    public String serializeConsentXACMLDoc(PolicyType oConsentXACML)
        throws AdapterPIPException
    {
        String sConsentXACML = "";

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("oasis.names.tc.xacml._2_0.policy.schema.os");
            Marshaller oMarshaller = oJaxbContext.createMarshaller();

            StringWriter swXML = new StringWriter();

            oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory oXACMLObjectFactory = new oasis.names.tc.xacml._2_0.policy.schema.os.ObjectFactory();
            JAXBElement oJaxbElement = oXACMLObjectFactory.createPolicy(oConsentXACML);

            oMarshaller.marshal(oJaxbElement, swXML);
            sConsentXACML = swXML.toString();
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to serialize the XACML document to a string.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return sConsentXACML;
    }

    /**
     * This method takes a string version of the Patient Pref document and
     * creates the JAXB object version of the same document.
     *
     * @param sConsentXACML The string version of the patient preference XACML document.
     * @return The JAXB object version of the patient preferences XACML document.
     * @throws gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException
     *         This is thrown if there is an error deserializing the string.
     */
    public PolicyType deserializeConsentXACMLDoc(String sConsentXACML)
        throws AdapterPIPException
    {
        PolicyType oConsentXACML = null;

        try
        {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext oJaxbContext = oHandler.getJAXBContext("oasis.names.tc.xacml._2_0.policy.schema.os");
            Unmarshaller oUnmarshaller = oJaxbContext.createUnmarshaller();

            StringReader srXML = new StringReader(sConsentXACML);

            JAXBElement oJAXBElementConsentXACML = (JAXBElement) oUnmarshaller.unmarshal(srXML);
            if (oJAXBElementConsentXACML.getValue() instanceof PolicyType)
            {
                oConsentXACML = (PolicyType) oJAXBElementConsentXACML.getValue();
            }
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to deserialize the XACML consent string: " + sConsentXACML + "  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return oConsentXACML;
    }




}
