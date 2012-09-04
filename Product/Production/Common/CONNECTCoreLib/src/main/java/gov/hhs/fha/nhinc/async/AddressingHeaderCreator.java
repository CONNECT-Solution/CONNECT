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
package gov.hhs.fha.nhinc.async;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

/**
 * This class manages the creation of the header section of the soap message
 * that defines WS-Addressing.
 */
public class AddressingHeaderCreator {
    private static String UUID_TAG = "urn:uuid:";

	private final Log log = LogFactory.getLog(getClass());

	String url;
	String action;
	String messageId;
	List<String> relatesToIds;
	ElementBuilder elementBuilder;

	/**
	 * Parameterized constructor.
	 *
	 * @param url
	 *            The Endpoint URL used to set the To header element
	 * @param action
	 *            The WS-Addressing action used to set the Action header element
	 *            and should match the wsaw:Action as defined in the port
	 *            definition section of the corresponding wsdl
	 * @param messageId
	 *            The UUID uniquely identifying the message used to set the
	 *            MessageID header
	 * @param relatesToIds
	 *            A optional listing of ids specifying messages this one relates
	 *            to. This listing may be null or empty if no such relationships
	 *            exist.
	 **/
	public AddressingHeaderCreator(String url, String action, String messageId,
			List<String> relatesToIds) {
		this.url = url;
		this.action = action;
		this.messageId = messageId;
		this.relatesToIds = relatesToIds;

		if (action == null) {
			log.warn("Provided endpoint URL is null");
		}

		// The To header is required
		if (url == null) {
			log.warn("Provided endpoint URL is null");
		}

		elementBuilder = ElementBuilder.newInstance();
	}

	/**
	 *
	 * Creates the WS-Addressing headers. Specifically the To, Action, ReplyTo,
	 * Address, and MessageID elements are created as Header objects and
	 * packaged in a List. This listing can be used to define a
	 * WSBindingProvider's outbound headers.
	 *
	 * @return A listing of the WS-Addressing headers
	 **/
	public List<Header> build() {
		List<Header> headers = new ArrayList<Header>();

		headers.add(buildHeaderTo());

		headers.add(buildHeaderAction());

		headers.add(buildHeaderReplyTo());

		if (messageId != null) {
			headers.add(buildHeaderMessageId());
		}
		if (relatesToIds != null) {
			buildHeaderRelatesTo(relatesToIds, headers);
		}

		return headers;
	}

	void buildHeaderRelatesTo(List<String> relatesToIds, List<Header> headers) {
		// The RelatesTo header is an optional but potentially repeating element
		for (String id : relatesToIds) {
			log.debug("Set WS-Addressing <RelatesTo> " + id);

			Header header = buildRelatesToHeader(id);
			headers.add(header);
		}
	}

	/**
	 * @param id
	 * @return
	 */
	Header buildRelatesToHeader(String id) {
		return buildAddressingHeader(NhincConstants.HEADER_RELATESTO, id);
	}

	Header buildHeaderMessageId() {
	    if (hasProperMessageIDPrefix(this.messageId) == false) {
            this.messageId = fixMessageIDPrefix(this.messageId);
        }
		return buildAddressingHeader(NhincConstants.WS_SOAP_HEADER_MESSAGE_ID,
				messageId);
	}

	Header buildHeaderReplyTo() {
		// The optional ReplyTo header contains the Address element

		Element elemReplyTo = elementBuilder.buildElement(
				NhincConstants.WS_ADDRESSING_URL,
				NhincConstants.WS_SOAP_HEADER_REPLYTO, null, true);

		Element elemAddr = elementBuilder.buildElement(
				NhincConstants.WS_ADDRESSING_URL,
				NhincConstants.WS_SOAP_HEADER_ADDRESS,
				NhincConstants.WS_ADDRESSING_URL_ANONYMOUS, null);

		elemReplyTo.getOwnerDocument().adoptNode(elemAddr);

		elemReplyTo.appendChild(elemAddr);

		Header replyToHdr = Headers.create(elemReplyTo);

		return replyToHdr;

	}

	Header buildHeaderAction() {
		// The Action header is required

		return buildAddressingHeader(NhincConstants.WS_SOAP_HEADER_ACTION,
				action);
	}

	Header buildHeaderTo() {

		return buildAddressingHeader(NhincConstants.WS_SOAP_HEADER_TO, url);
	}

	Header buildAddressingHeader(final String name, final String value) {
		Element element = elementBuilder.buildElement(
				NhincConstants.WS_ADDRESSING_URL, name, value, true);

		Header header = Headers.create(element);
		return header;
	}

	 /**
     * @param messageId
     * @return
     */
    private boolean hasProperMessageIDPrefix(String messageId) {
        return messageId.trim().startsWith(UUID_TAG);
    }

    /**
     * @param messageId
     */
    private String fixMessageIDPrefix(String messageId) {
        if (illegalUUID(messageId, "uuid:")) {
            return "urn:" + messageId;
        } else {
            return UUID_TAG + messageId;
        }
    }

    /**
     * @param messageId
     * @param string
     * @return
     */
    private boolean illegalUUID(String messageId, String illegalPrefix) {
        return messageId.trim().startsWith(illegalPrefix);
    }

    public static String generateMessageId() {
        return UUID_TAG + UUID.randomUUID().toString();
    }
}
