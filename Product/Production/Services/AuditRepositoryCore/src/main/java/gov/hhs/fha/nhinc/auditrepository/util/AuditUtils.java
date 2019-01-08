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
package gov.hhs.fha.nhinc.auditrepository.util;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class AuditUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AuditUtils.class);

    private AuditUtils() {
    }

    /**
     * This method un-marshall XML Blob to AuditMessage
     *
     * @param auditBlob
     * @return AuditMessageType
     */
    public static AuditMessageType unMarshallBlobToAuditMessage(Blob auditBlob) {
        LOG.info("Inside unMarshallBlobToAuditMessage");
        AuditMessageType auditMessageType = null;
        InputStream in = null;
        try {
            if (auditBlob != null && (int) auditBlob.length() > 0) {
                JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
                in = auditBlob.getBinaryStream();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(util.
                    getSafeStreamReaderFromInputStream(in));
                if (jaxEle.getValue() != null) {
                    auditMessageType = (AuditMessageType) jaxEle.getValue();
                }
            }
        } catch (SQLException | JAXBException | XMLStreamException e) {
            LOG.error("Blob to Audit Message Conversion Error: {}", e.getLocalizedMessage(), e);
        } finally {
            StreamUtils.closeStreamSilently(in);
        }

        return auditMessageType;
    }

}
