/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.File;
import java.io.FileWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
/**
 *
 * @author dunnek
 */
public class XDRMessageHelper {
    private static final String XML_FILENAME = "ProvideAndRegisterDocumentSet-bRequest.xml";

    public ProvideAndRegisterDocumentSetRequestType getSampleMessage()
    {
       return getSampleMessage(XML_FILENAME);
    }
    public ProvideAndRegisterDocumentSetRequestType getSampleMessage(String fileName)
    {
        ProvideAndRegisterDocumentSetRequestType result = null;
        Object o = null;

        try
        {
           //JAXBContext jc = JAXBContext.newInstance( "oasis.names.tc.ebxml_regrep.xsd.rim._3" );
           JAXBContext jc = JAXBContext.newInstance(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.class);

           javax.xml.bind.Marshaller m = jc.createMarshaller();

           Unmarshaller u = jc.createUnmarshaller();

           JAXBElement<ProvideAndRegisterDocumentSetRequestType> element = u.unmarshal(new StreamSource( fileName), ProvideAndRegisterDocumentSetRequestType.class);
           result = element.getValue();

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }


       return result;

    }

    public void saveSampleMessage(ProvideAndRegisterDocumentSetRequestType msg)
    {
        try
        {
           //JAXBContext jc = JAXBContext.newInstance( "oasis.names.tc.ebxml_regrep.xsd.rim._3" );
           JAXBContext jc = JAXBContext.newInstance(msg.getClass());

           javax.xml.bind.Marshaller m = jc.createMarshaller();

           

           Unmarshaller u = jc.createUnmarshaller();
           JAXBElement<ProvideAndRegisterDocumentSetRequestType> element = u.unmarshal(new StreamSource( XML_FILENAME), ProvideAndRegisterDocumentSetRequestType.class);

           element.setValue(msg);
           m.marshal(element,new FileWriter("C:\\temp\\test2.xml"));

           System.out.println("all good");
        }
        catch(Exception ex)
        {

        }

    }

}