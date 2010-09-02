/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
