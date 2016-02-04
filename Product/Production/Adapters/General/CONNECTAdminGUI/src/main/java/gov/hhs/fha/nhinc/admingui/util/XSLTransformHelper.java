/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.XMLConstants;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class XSLTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XSLTransformHelper.class);

    public byte[] convertXMLToHTML(InputStream xml, InputStream xsl) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            tFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Templates template = tFactory.newTemplates(new StreamSource(xsl));
            Transformer transformer = template.newTransformer();
            transformer.transform(new StreamSource(xml), new StreamResult(output));

        } catch (TransformerException e) {
            LOG.error("Exception in transforming from xml to html: {}", e.getLocalizedMessage(), e);
        }

        return output.toByteArray();
    }

}
