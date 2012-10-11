/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.

 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information. *
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.docmgr.repository.util;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Utility class for XML processing
 * 
 * @author Neil Webb
 */
public class XmlUtil {

    public static Document getDocumentFromString(String xmlString)
        throws Exception {
        DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            // Do not load the DTD
            oDocumentBuilderFactory.setAttribute(
                "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                Boolean.FALSE);
        } catch (IllegalArgumentException e) {
        }
        DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();

        InputSource inputSource = new InputSource(new StringReader(xmlString));
        return oDocumentBuilder.parse(inputSource);
    }

    public static Document getDocumentFromFile(String absolutePath)
        throws Exception {
        DocumentBuilderFactory oDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder oDocumentBuilder = oDocumentBuilderFactory.newDocumentBuilder();

        Document oDocument = null;

        oDocument = oDocumentBuilder.parse(new File(absolutePath));

        return oDocument;
    }
}
