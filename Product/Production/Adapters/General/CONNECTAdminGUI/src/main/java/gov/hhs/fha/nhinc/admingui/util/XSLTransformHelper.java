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
package gov.hhs.fha.nhinc.admingui.util;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.FileInputStream;
import org.xml.sax.InputSource;
import javax.xml.transform.Result;
import org.xml.sax.SAXException;

/**
 *
 * @author achidamb
 */
public class XSLTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XSLTransformHelper.class);

    public byte[] convertXMLToHTML(final InputStream xml, final InputStream xsl) {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {

            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            reader.setFeature(NhincConstants.FEATURE_GENERAL_ENTITIES, false);
            reader.setFeature(NhincConstants.FEATURE_PARAMETER_ENTITIES, false);
            reader.setFeature(NhincConstants.FEATURE_DISALLOW_DOCTYPE, true);

            Source xmlSource = new SAXSource(reader, new InputSource(xml));
            Source xsltSource = new SAXSource(reader, new InputSource(xsl));
            Result result = new StreamResult(output);
            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer trans = transFact.newTransformer(xsltSource);
            trans.transform(xmlSource, result);

        } catch (final TransformerException | SAXException e) {
            LOG.error("Exception in transforming from xml to html: {}", e.getLocalizedMessage(), e);
        }

        return output.toByteArray();

    }

}
