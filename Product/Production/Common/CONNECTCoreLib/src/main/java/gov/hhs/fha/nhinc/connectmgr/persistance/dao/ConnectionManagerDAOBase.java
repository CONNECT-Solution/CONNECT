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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.ObjectFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author mweaver
 */
public class ConnectionManagerDAOBase {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManagerDAOBase.class);

    protected BusinessDetail loadBusinessDetail(final File file) throws JAXBException {
        BusinessDetail resp = null;
        try {

            synchronized (file) {

                final SAXParserFactory spf = SAXParserFactory.newInstance();
                spf.setNamespaceAware(true);
                spf.setFeature(NhincConstants.FEATURE_GENERAL_ENTITIES, false);
                spf.setFeature(NhincConstants.FEATURE_DISALLOW_DOCTYPE, true);
                spf.setFeature(NhincConstants.FEATURE_PARAMETER_ENTITIES, false);

                final XMLReader xmlReader = spf.newSAXParser().getXMLReader();
                final InputSource inputSource = new InputSource(new FileReader(file));
                final SAXSource source = new SAXSource(xmlReader, inputSource);

                final JAXBContext context = JAXBContext.newInstance(BusinessDetail.class);
                final Unmarshaller unmarshaller = context.createUnmarshaller();

                final InputSource stream = source.getInputSource();

                final Reader reader = stream.getCharacterStream();

                final JAXBElement<BusinessDetail> jaxbElement = unmarshaller.unmarshal(new StreamSource(reader),
                        BusinessDetail.class);

                resp = jaxbElement.getValue();

            }
        } catch (ParserConfigurationException | SAXException | FileNotFoundException e) {
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
