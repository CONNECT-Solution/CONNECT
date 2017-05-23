/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.ObjectFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mweaver
 */
public class ConnectionManagerDAOBase {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManagerDAOBase.class);

    protected BusinessDetail loadBusinessDetail(File file) throws JAXBException {
        BusinessDetail resp = null;
        try {
            synchronized (file) {

                JAXBContext context = JAXBContext.newInstance(BusinessDetail.class);
                final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                dbf.setFeature(NhincConstants.FEATURE_GENERAL_ENTITIES, false);
                dbf.setFeature(NhincConstants.FEATURE_DISALLOW_DOCTYPE, true);
                dbf.setFeature(NhincConstants.FEATURE_PARAMETER_ENTITIES, false);
                dbf.setExpandEntityReferences(false);

                // Convert File to Document
                final DocumentBuilder db = dbf.newDocumentBuilder();
                final Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                DOMSource source = new DOMSource(doc);

                // Convert Document to StreamSource
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer();
                StreamResult result = new StreamResult();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                result.setOutputStream(out);
                transformer.transform(source, result);
                ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

                // Unmarshal
                Unmarshaller unmarshaller = context.createUnmarshaller();
                JAXBElement<BusinessDetail> jaxbElement = unmarshaller.unmarshal(new StreamSource(in),
                        BusinessDetail.class);
                resp = jaxbElement.getValue();
                in.close();
                out.close();
            }
        } catch (final IOException | ParserConfigurationException | TransformerException | SAXException e) {
            LOG.error("Exception in reading/parsing XDRConfiguration file: {}", e.getLocalizedMessage(), e);
        }

        return resp;

    }

    protected void saveBusinessDetail(final BusinessDetail BusinessDetail, final File file) {
        try {
            synchronized (file) {
                final JAXBContext context = JAXBContext.newInstance(BusinessDetail.class);
                final ObjectFactory factory = new ObjectFactory();
                final Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.marshal(factory.createBusinessDetail(BusinessDetail), file);
            }
        } catch (final JAXBException ex) {
            throw new RuntimeException("Unable to save to Connection Information File " + file.getName(), ex);
        }

        LOG.info("Connection info saved to " + file.getName());
    }

}
