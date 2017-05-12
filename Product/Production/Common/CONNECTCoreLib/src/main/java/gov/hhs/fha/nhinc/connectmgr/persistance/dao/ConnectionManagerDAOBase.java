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

import java.io.File;
import java.io.FileReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.ObjectFactory;
import javax.xml.parsers.ParserConfigurationException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.Reader;
import java.io.FileNotFoundException;

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

				SAXParserFactory spf = SAXParserFactory.newInstance();
				spf.setNamespaceAware(true);
				spf.setFeature(NhincConstants.FEATURE_GENERAL_ENTITIES, false);
				spf.setFeature(NhincConstants.FEATURE_DISALLOW_DOCTYPE, true);
				spf.setFeature(NhincConstants.FEATURE_PARAMETER_ENTITIES, false);

				XMLReader xmlReader = spf.newSAXParser().getXMLReader();
				InputSource inputSource = new InputSource(new FileReader(file));
				SAXSource source = new SAXSource(xmlReader, inputSource);

				JAXBContext context = JAXBContext.newInstance(BusinessDetail.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();

				InputSource stream = source.getInputSource();

				Reader reader = stream.getCharacterStream();

				JAXBElement<BusinessDetail> jaxbElement = unmarshaller.unmarshal(new StreamSource(reader),
						BusinessDetail.class);

				resp = jaxbElement.getValue();

			}
		} catch (ParserConfigurationException e) {
			LOG.error("unable to load XDRConfiguration file", e);
		} catch (SAXException e) {
			LOG.error("SAX Exception", e);
		} catch (FileNotFoundException e) {
			LOG.error("File Not Found", e);
		}

		return resp;

	}

	protected void saveBusinessDetail(BusinessDetail BusinessDetail, File file) {
		try {
			synchronized (file) {
				JAXBContext context = JAXBContext.newInstance(BusinessDetail.class);
				ObjectFactory factory = new ObjectFactory();
				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(factory.createBusinessDetail(BusinessDetail), file);
			}
		} catch (JAXBException ex) {
			throw new RuntimeException("Unable to save to Connection Information File " + file.getName(), ex);
		}

		LOG.info("Connection info saved to " + file.getName());
	}

}
