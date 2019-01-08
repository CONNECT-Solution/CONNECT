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
package gov.hhs.fha.nhinc.transform.policy;

/**
 *
 * @author rayj
 */
public class Constants {

    public static final String HomeCommunityAttributeId = "urn:gov:hhs:fha:nhinc:home-community-id";
    public static final String AssigningAuthorityAttributeId = "urn:gov:hhs:fha:nhinc:assigning-authority-id";
    public static final String ResourceIdAttributeId = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    public static final String RespositoryAttributeId = "urn:gov:hhs:fha:nhinc:document-repository-id";
    public static final String DocumentAttributeId = "urn:gov:hhs:fha:nhinc:document-id";

    public static final String ATTRIBUTE_ID_SUBSCRIPTION_TOPIC = "urn:gov:hhs:fha:nhinc:subscription-topic";

    public static final String DataTypeString = "http://www.w3.org/2001/XMLSchema#string";
    public static final String DataTypeBinary = "http://www.w3.org/2001/XMLSchema#base64Binary";
    public static final String DataTypeDate = "http://www.w3.org/2001/XMLSchema#date";
    public static final String DataTypeAnyURI = "http://www.w3.org/2001/XMLSchema#anyURI";
    public static final String DataTypeHL7II = "urn:hl7-org:v3#II";
}
