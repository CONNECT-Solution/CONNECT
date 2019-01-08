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
package gov.hhs.fha.nhinc.corex12.ds.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import com.services.nhinc.schema.auditmessage.TypeValuePairType;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.corex12.ds.audit.X12AuditDataTransformConstants;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class which implements methods common across CORE X12 RealTime and BatchRequest and Response services.
 *
 * @author achidamb
 * @param <T>
 * @param <K>
 */
public abstract class X12AuditTransforms<T, K> extends AuditTransforms<T, K> {

    private static final Logger LOG = LoggerFactory.getLogger(X12AuditTransforms.class);

    @Override
    protected final AuditMessageType getParticipantObjectIdentificationForRequest(T request,
        AssertionType assertion, AuditMessageType auditMsg) {

        ParticipantObjectIdentificationType participantOid
            = buildBaseParticipantObjectIdentificationType(getPayloadFromRequest(request));

        participantOid.getParticipantObjectDetail().add(getTypeValuePair(marshallToByteArrayFromRequest(request)));
        auditMsg.getParticipantObjectIdentification().add(participantOid);
        return auditMsg;
    }

    @Override
    protected final AuditMessageType getParticipantObjectIdentificationForResponse(T request, K response,
        AssertionType assertion, AuditMessageType auditMsg) {

        ParticipantObjectIdentificationType participantOid
            = buildBaseParticipantObjectIdentificationType(getPayloadFromResponse(response));

        participantOid.getParticipantObjectDetail().add(getTypeValuePair(marshallToByteArrayFromResponse(response)));
        auditMsg.getParticipantObjectIdentification().add(participantOid);
        return auditMsg;
    }

    /**
     *
     * @param request
     * @return
     */
    protected abstract byte[] marshallToByteArrayFromRequest(T request);

    /**
     *
     * @param response
     * @return
     */
    protected abstract byte[] marshallToByteArrayFromResponse(K response);

    /**
     *
     * @param request
     * @return request payload
     */
    protected abstract String getPayloadFromRequest(T request);

    /**
     *
     * @param response
     * @return response payload
     */
    protected abstract String getPayloadFromResponse(K response);

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return X12AuditDataTransformConstants.EVENT_ID_CODE_X12;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return X12AuditDataTransformConstants.EVENT_ID_CODE_X12;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return X12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return X12AuditDataTransformConstants.EVENT_ID_CODE_SYS_CODE_X12;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return X12AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_X12;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12REALTIME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return X12AuditDataTransformConstants.EVENT_ACTION_CODE_READ;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return X12AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE;
    }

    /**
     *
     * @param payload
     * @return participant object
     */
    protected ParticipantObjectIdentificationType buildBaseParticipantObjectIdentificationType(String payload) {
        ParticipantObjectIdentificationType participantObject = createParticipantObjectIdentification(
            X12AuditDataTransformConstants.PARTICIPANT_OJB_TYPE_CODE_SYSTEM,
            X12AuditDataTransformConstants.PARTICIPANT_OBJ_TYPE_CODE_ROLE_X12,
            payload, X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_NAME,
            X12AuditDataTransformConstants.CAQH_X12_CONNECTIVITY_CODED_SYS_DISPLAY_NAME);
        participantObject.setParticipantObjectID(payload);
        return participantObject;
    }

    /**
     *
     * @return
     */
    protected Marshaller getMarshaller() {
        Marshaller marshaller = null;

        try {
            marshaller = new JAXBContextHandler().getJAXBContext(X12AuditDataTransformConstants.CORE_X12_JAXB_CONTEXT)
                .createMarshaller();
        } catch (JAXBException ex) {
            LOG.error("JAXB Marshall error: {}", ex.getMessage(), ex);
        }

        return marshaller;
    }

    /**
     *
     * @param nameSpaceURI
     * @param localPart
     * @return QName
     */
    protected QName getQname(String nameSpaceURI, String localPart) {
        return new QName(nameSpaceURI, localPart);
    }

    /**
     *
     * @param byteArray
     * @return type value pair
     */
    private TypeValuePairType getTypeValuePair(byte[] byteArray) {
        TypeValuePairType typeValuePair = new TypeValuePairType();
        typeValuePair.setValue(byteArray);
        return typeValuePair;
    }
}
