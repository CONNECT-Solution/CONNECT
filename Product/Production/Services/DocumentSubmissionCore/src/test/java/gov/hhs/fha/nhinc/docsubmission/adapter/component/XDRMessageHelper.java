/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.FileWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author dunnek
 */
public class XDRMessageHelper {
    private static final String XML_FILENAME = "ProvideAndRegisterDocumentSet-bRequest.xml";

    public static ProvideAndRegisterDocumentSetRequestType getSampleMessage() {
        return getSampleMessage(XML_FILENAME);
    }

    public static ProvideAndRegisterDocumentSetRequestType getSampleMessage(String fileName) {
        ProvideAndRegisterDocumentSetRequestType result = null;

        try {
            // JAXBContext jc = JAXBContext.newInstance( "oasis.names.tc.ebxml_regrep.xsd.rim._3" );
            JAXBContext jc = JAXBContext
                .newInstance(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.class);

            jc.createMarshaller();

            Unmarshaller u = jc.createUnmarshaller();

            JAXBElement<ProvideAndRegisterDocumentSetRequestType> element = u.unmarshal(new StreamSource(fileName),
                ProvideAndRegisterDocumentSetRequestType.class);
            result = element.getValue();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result;

    }

    public void saveSampleMessage(ProvideAndRegisterDocumentSetRequestType msg) {
        try {
            // JAXBContext jc = JAXBContext.newInstance( "oasis.names.tc.ebxml_regrep.xsd.rim._3" );
            JAXBContext jc = JAXBContext.newInstance(msg.getClass());

            javax.xml.bind.Marshaller m = jc.createMarshaller();

            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<ProvideAndRegisterDocumentSetRequestType> element = u.unmarshal(new StreamSource(XML_FILENAME),
                ProvideAndRegisterDocumentSetRequestType.class);

            element.setValue(msg);
            m.marshal(element, new FileWriter("C:\\temp\\test2.xml"));

            System.out.println("all good");
        } catch (Exception ex) {

        }

    }

}
