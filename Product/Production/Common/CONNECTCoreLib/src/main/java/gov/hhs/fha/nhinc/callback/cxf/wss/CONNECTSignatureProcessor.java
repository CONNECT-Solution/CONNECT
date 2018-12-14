/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.callback.cxf.wss;

import org.apache.cxf.helpers.CastUtils;
import org.apache.wss4j.dom.WSDataRef;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.callback.SamlConstants;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.SHA_TYPE;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.Base64Coder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.activation.DataHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Attachment;
import org.apache.wss4j.common.crypto.AlgorithmSuite;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.ext.WSSecurityException.ErrorCode;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.WSDocInfo;
import org.apache.wss4j.dom.engine.WSSecurityEngineResult;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.processor.SignatureProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class will process the signature element of the Security header. It inherits from the default SignatureProcessor
 * but will inline all digest and signature values in the Security header if they are attached as a reference.
 *
 */
public class CONNECTSignatureProcessor extends SignatureProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CONNECTSignatureProcessor.class);
    private static final LargeFileUtils FILE_UTILS = LargeFileUtils.getInstance();
    private static final String CONTENT_ID_PREFIX = "cid:";
    private static final String HREF_ATTRIBUTE = "href";
    private static final String XOP_NS = "http://www.w3.org/2004/08/xop/include";
    private static final String XOP_INCLUDE_TAG = "Include";
    private static final PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

    @Override
    public List<WSSecurityEngineResult> handleToken(Element signatureElem, RequestData data, WSDocInfo wsDocInfo)
        throws WSSecurityException {
        inlineSignatureAttachments((SoapMessage) data.getMsgContext(), signatureElem);
        List<WSSecurityEngineResult> results = null;
        List<SHA_TYPE> configurableList = getConfigurableSHA();
        if (CollectionUtils.isNotEmpty(configurableList)) {
            // Detect configurable list. If it has only sha1, then do normal workflow
            if (configurableList.size() == 1 && SHA_TYPE.SHA1.equals(configurableList.get(0))) {
                results = super.handleToken(signatureElem, data, wsDocInfo);
            } else { // override default signature check. Follow pattern in AlgorithmSuiteCheck
                AlgorithmSuite oldAlgorithm = data.getAlgorithmSuite();
                for (SHA_TYPE configSHA : configurableList) {
                    oldAlgorithm.addSignatureMethod(configSHA.getSignatureAlgorithm());
                    oldAlgorithm.addDigestAlgorithm(configSHA.getDigestAlgorithm());
                }
                data.setAlgorithmSuite(oldAlgorithm);
                results = super.handleToken(signatureElem, data, wsDocInfo);
                // Revert back default Policy in CXF
                for (WSSecurityEngineResult result : results) {
                    Integer action = (Integer) result.get(WSSecurityEngineResult.TAG_ACTION);
                    if (WSConstants.SIGN == action) {
                        result.put(WSSecurityEngineResult.TAG_SIGNATURE_METHOD, SHA_TYPE.SHA1.getSignatureAlgorithm());
                        List<WSDataRef> dataRefs = CastUtils.cast((List<?>) result
                            .get(WSSecurityEngineResult.TAG_DATA_REF_URIS));
                        for (WSDataRef dataRef : dataRefs) {
                            dataRef.setDigestAlgorithm(SHA_TYPE.SHA1.getDigestAlgorithm());
                        }
                    }
                }
            }
        }
        return results != null ? results : super.handleToken(signatureElem, data, wsDocInfo);
    }



    /**
     * Retrieve a list of configurable SHA from gateway.properties. For example: configurableSHA=SHA1,SHA256,SHA512
     *
     * @return
     */
    private List<SHA_TYPE> getConfigurableSHA() {
        String configurableProperty = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
            NhincConstants.CONFIG_SHA, SHA_TYPE.SHA1.name());
        List<String> configurablePropertyList = Arrays.asList(StringUtils.split(configurableProperty, ","));
        List<SHA_TYPE> SHATypeList = new ArrayList<>();
        for (String sha : configurablePropertyList) {
            SHA_TYPE shaType = SHA_TYPE.getSHAType(sha);
            if (shaType != null) {
                SHATypeList.add(shaType);
            }
        }
        return SHATypeList;

    }

    /**
     * This method will inline all digest and signature values in the Security header if they are attached as a
     * reference. Be warned that this call will directly modify the passed in signature element.
     *
     * @param soapMsg
     * @param signatureElem
     * @throws WSSecurityException
     */
    void inlineSignatureAttachments(SoapMessage soapMsg, Element signatureElem) throws WSSecurityException {
        Collection<Attachment> attachments = soapMsg.getAttachments();

        try {
            inlineDigestValueIncludes(signatureElem, attachments);

            inlineSignatureValueIncludes(signatureElem, attachments);

        } catch (IOException ioe) {
            throw new WSSecurityException(ErrorCode.FAILURE, ioe, "Failed to inline attachments to signature.");
        }
    }

    private static void inlineDigestValueIncludes(Element element, Collection<Attachment> attachments) throws IOException {
        NodeList digestNodes = element.getElementsByTagNameNS(SamlConstants.XML_SIGNATURE_NS,
            SamlConstants.DIGEST_VALUE_TAG);

        inlineIncludes(digestNodes, attachments);
    }

    private static void inlineSignatureValueIncludes(Element element, Collection<Attachment> attachments) throws IOException {
        NodeList signatureNodes = element.getElementsByTagNameNS(SamlConstants.XML_SIGNATURE_NS,
            SamlConstants.SIGNATURE_VALUE_TAG);

        inlineIncludes(signatureNodes, attachments);
    }

    private static void inlineIncludes(NodeList signatureNodes, Collection<Attachment> attachments) throws IOException {
        for (int i = 0; i < signatureNodes.getLength(); ++i) {
            Element sigElement = (Element) signatureNodes.item(i);

            Object child = sigElement.getFirstChild();
            if (child instanceof Element && isIncludeElement((Element) child)) {

                String refId = ((Element) child).getAttribute(HREF_ATTRIBUTE).replaceFirst(CONTENT_ID_PREFIX, "");

                Optional<Attachment> attachment = getAttachment(attachments, refId);
                if (attachment.isPresent()) {
                    String attachmentValue = convertToBase64Data(attachment.get().getDataHandler());
                    sigElement.setTextContent(attachmentValue);
                } else {
                    LOG.warn(
                        "Failed to inline signature/digest element to the header.  Cannot find reference id: {}", refId);
                }
            }
        }
    }

    private static Optional<Attachment> getAttachment(Collection<Attachment> attachments, String id) {
        for (Attachment attachment : attachments) {
            if (attachment.getId().equals(id)) {
                return Optional.of(attachment);
            }
        }

        return Optional.absent();
    }

    private static boolean isIncludeElement(Element elem) {
        String namespace = elem.getNamespaceURI();
        String elemName = elem.getLocalName();

        if (XOP_NS.equals(namespace) && XOP_INCLUDE_TAG.equals(elemName)) {
            return true;
        }

        return false;
    }

    private static String convertToBase64Data(DataHandler dh) throws IOException {
        byte[] attachmentBinaryData = FILE_UTILS.convertToBytes(dh);
        char[] attachmentBase64Data = Base64Coder.encode(attachmentBinaryData);

        return new String(attachmentBase64Data);
    }
}
