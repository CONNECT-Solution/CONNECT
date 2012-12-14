/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.direct.transform;

import gov.hhs.fha.nhinc.direct.DirectException;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.nhindirect.xd.transform.MimeXdsTransformer;
import org.nhindirect.xd.transform.exception.TransformationException;
import org.nhindirect.xd.transform.impl.DefaultMimeXdsTransformer;

/**
 * @author mweaver
 *
 */
public class MimeMessageTransformer {
    
    private static final Logger LOG = Logger.getLogger(MimeMessageTransformer.class);
    private MimeXdsTransformer transformer = null;
    private String errorMessage = "Error transforming message to XDR";
    
    public MimeMessageTransformer() {
        transformer = getMimeXdsTransformer();
    }
    
    public ProvideAndRegisterDocumentSetRequestType transform(MimeMessage message) {
        ProvideAndRegisterDocumentSetRequestType request = null;
        try {
            request = transformer.transform(message);
        } catch (TransformationException e) {
            LOG.error(errorMessage, e);
            throw new DirectException(errorMessage, e);
        }
        return request;
    }
    
    private MimeXdsTransformer getMimeXdsTransformer() {
        return new DefaultMimeXdsTransformer();
    }

}
