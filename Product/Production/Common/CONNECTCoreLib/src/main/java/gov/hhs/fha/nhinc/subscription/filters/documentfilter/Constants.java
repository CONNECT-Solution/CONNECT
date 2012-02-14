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
package gov.hhs.fha.nhinc.subscription.filters.documentfilter;

/**
 * 
 * @author rayj
 */
public class Constants {

    public static final String AdhocQueryId = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    public static final String PatientIdSlotName = "$XDSDocumentEntryPatientId";
    public static final String PatientIdIdentificationScheme = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";

    public static final String DocumentClassCodeSlotName = "$XDSDocumentEntryClassCode";
    public static final String DocumentClassCodeClassificationScheme = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";

    public static final String DocumentIdIdentificationScheme = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    public static final String RepositoryIdSlotName = "repositoryUniqueId";
    public static final String ADDRESS_ELEM_NAME = "Address";
    public static final String USER_ADDRESS_ELEM_NAME = "nhin:UserAddress";
}
