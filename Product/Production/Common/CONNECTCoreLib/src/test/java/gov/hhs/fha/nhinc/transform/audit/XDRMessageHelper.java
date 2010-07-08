/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;

/**
 *
 * @author dunnek
 */
public class XDRMessageHelper {
    private static final String XML_FILENAME = "ProvideAndRegisterDocumentSet-bRequest.xml";

    public ProvideAndRegisterDocumentSetRequestType getSampleMessage() 
    {
        ProvideAndRegisterDocumentSetRequestType result = null;
        Object o = null;

        try
        {
           //JAXBContext jc = JAXBContext.newInstance( "oasis.names.tc.ebxml_regrep.xsd.rim._3" );
           JAXBContext jc = JAXBContext.newInstance(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.class);
           
           javax.xml.bind.Marshaller m = jc.createMarshaller();

           Unmarshaller u = jc.createUnmarshaller();
           
           JAXBElement<ProvideAndRegisterDocumentSetRequestType> element = u.unmarshal(new StreamSource( XML_FILENAME), ProvideAndRegisterDocumentSetRequestType.class);
           result = element.getValue();

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
       return result;
       
    }

}
