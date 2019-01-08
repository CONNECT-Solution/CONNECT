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
package gov.hhs.fha.nhinc.docquery.xdsb.helper;

/**
 *
 * @author tjafri
 */
public class XDSbConstants {

    /**
     * The Enum ResponseSlotName.
     *
     */
    public enum ResponseSlotName {

        /**
         * The creation time.
         */
        creationTime,
        /**
         * The hash.
         */
        hash,
        /**
         * The language code.
         */
        languageCode,
        /**
         * The legal authenticator.
         */
        legalAuthenticator,
        /**
         * The repository unique id.
         */
        repositoryUniqueId,
        /**
         * The service start time.
         */
        serviceStartTime,
        /**
         * The service stop time.
         */
        serviceStopTime,
        /**
         * The size.
         */
        size,
        /**
         * The source patient id.
         */
        sourcePatientId,
        /**
         * The source patient info.
         */
        sourcePatientInfo,
        /**
         * The uri.
         */
        URI,
        /**
         * The intended Recipient.
         */
        intendedRecipient
    }

    /**
     * The Enum RegistryStoredQueryParameter.
     *
     */
    public enum RegistryStoredQueryParameter {

        /**
         * The $ XDS document entry patient id.
         */
        $XDSDocumentEntryPatientId,
        /**
         * The $ XDS document entry class code.
         */
        $XDSDocumentEntryClassCode,
        /**
         * The $ XDS document entry type code.
         */
        $XDSDocumentEntryTypeCode,
        /**
         * The $ XDS document entry practice setting code.
         */
        $XDSDocumentEntryPracticeSettingCode,
        /**
         * The $ XDS document entry creation time from.
         */
        $XDSDocumentEntryCreationTimeFrom,
        /**
         * The $ XDS document entry creation time to.
         */
        $XDSDocumentEntryCreationTimeTo,
        /**
         * The $ XDS document entry service start time from.
         */
        $XDSDocumentEntryServiceStartTimeFrom,
        /**
         * The $ XDS document entry service start time to.
         */
        $XDSDocumentEntryServiceStartTimeTo,
        /**
         * The $ XDS document entry service stop time from.
         */
        $XDSDocumentEntryServiceStopTimeFrom,
        /**
         * The $ XDS document entry service stop time to.
         */
        $XDSDocumentEntryServiceStopTimeTo,
        /**
         * The $ XDS document entry healthcare facility type code.
         */
        $XDSDocumentEntryHealthcareFacilityTypeCode,
        /**
         * The $ XDS document entry event code list.
         */
        $XDSDocumentEntryEventCodeList,
        /**
         * The $ XDS document entry confidentiality code.
         */
        $XDSDocumentEntryConfidentialityCode,
        /**
         * The $ XDS document entry author person.
         */
        $XDSDocumentEntryAuthorPerson,
        /**
         * The $ XDS document entry format code.
         */
        $XDSDocumentEntryFormatCode,
        /**
         * The $ XDS document entry status.
         */
        $XDSDocumentEntryStatus,
        /**
         * The $ XDS document entry Class Code Scheme
         */
        $XDSDocumentEntryClassCodeScheme
    }

    /**
     * The Enum XDSQueryStatus.
     */
    public static enum XDSQueryStatus {

        /**
         * The submitted.
         */
        SUBMITTED("urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted"),
        /**
         * The approved.
         */
        APPROVED("urn:oasis:names:tc:ebxml-regrep:StatusType:Approved"),
        /**
         * The deprecated.
         */
        DEPRECATED("urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");

        /**
         * The value.
         */
        private String value = null;

        /**
         * Instantiates a new XDS query status.
         *
         * @param value the value
         */
        XDSQueryStatus(String value) {
            this.value = value;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return value;
        }

        /**
         * From string.
         *
         * @param valueString the value string
         * @return the XDS query status
         */
        public static XDSQueryStatus fromString(String valueString) {
            if (valueString != null) {
                for (XDSQueryStatus enumValue : XDSQueryStatus.values()) {
                    if (valueString.equals(enumValue.toString())) {
                        return enumValue;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant " + valueString);
        }
    }

    /**
     * The Enum XDSbStoredQuery.
     */
    public enum XDSbStoredQuery {

        /**
         * The Find documents.
         */
        FindDocuments("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d"),
        /**
         * The Find submission sets.
         */
        FindSubmissionSets("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9"),
        /**
         * The Find folders.
         */
        FindFolders("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9"),
        /**
         * The Get all.
         */
        GetAll("urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3"),
        /**
         * The Get documents.
         */
        GetDocuments("urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3"),
        /**
         * The Get folders.
         */
        GetFolders("urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4"),
        /**
         * The Get associations.
         */
        GetAssociations("urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155"),
        /**
         * The Get documents and associations.
         */
        GetDocumentsAndAssociations("urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155"),
        /**
         * The Get submission sets.
         */
        GetSubmissionSets("urn:uuid:51224314-5390-4169-9b91-b1980040715a"),
        /**
         * The Get submission set and contents.
         */
        GetSubmissionSetAndContents("urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83"),
        /**
         * The Get folder and contents.
         */
        GetFolderAndContents("urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7"),
        /**
         * The Get folders for document.
         */
        GetFoldersForDocument("urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7"),
        /**
         * The Get related documents.
         */
        GetRelatedDocuments("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6"),
        /**
         * The Find documents by reference id.
         */
        FindDocumentsByReferenceId("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6");

        /**
         * The value.
         */
        private String value = null;

        /**
         * Instantiates a new XDSb stored query.
         *
         * @param value the value
         */
        XDSbStoredQuery(String value) {
            this.value = value;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return value;
        }

        /**
         * From string.
         *
         * @param valueString the value string
         * @return the XDSb stored query
         */
        public static XDSbStoredQuery fromString(String valueString) {
            if (valueString != null) {
                for (XDSbStoredQuery enumValue : XDSbStoredQuery.values()) {
                    if (valueString.equals(enumValue.toString())) {
                        return enumValue;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant " + valueString);
        }
    }

    /**
     * The Enum ReturnType.
     */
    public enum ReturnType {

        /**
         * The Leaf class.
         */
        LeafClass,
        /**
         * The Object ref.
         */
        ObjectRef
    }

    /**
     * The Enum ResponseClassification.
     */
    public enum ClassificationScheme {

        /**
         * The Author.
         */
        Author("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d"),

        /**
         * The class code.
         */
        classCode("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a"),
        /**
         * The confidentiality code.
         */
        confidentialityCode("urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f"),
        /**
         * The event code list.
         */
        eventCodeList("urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4"),
        /**
         * The format code.
         */
        formatCode("urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d"),
        /**
         * The healthcare facility type code.
         */
        healthcareFacilityTypeCode("urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1"),
        /**
         * The limited metadata.
         */
        limitedMetadata("urn:uuid:ab9b591b-83ab-4d03-8f5d-f93b1fb92e85"),
        /**
         * The practice setting code.
         */
        practiceSettingCode("urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead"),
        /**
         * The type code.
         */
        typeCode("urn:uuid:f0306f51-975f-434e-a61c-c59651d33983");

        /**
         * The value.
         */
        private String value = null;

        /**
         * Instantiates a new XDSb ClassificationScheme.
         *
         * @param value the value
         */
        ClassificationScheme(String value) {
            this.value = value;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return value;
        }

        /**
         * From string.
         *
         * @param valueString the value string
         * @return the XSDb constant for the given ClassificationScheme value
         */
        public static ClassificationScheme fromString(String valueString) {
            if (valueString != null) {
                for (ClassificationScheme enumValue : ClassificationScheme.values()) {
                    if (valueString.equals(enumValue.toString())) {
                        return enumValue;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant " + valueString);
        }
    }

    public enum IdentificationScheme {

        patientId("urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427"),
        uniqueId("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");

        /**
         * The value.
         */
        private String value = null;

        IdentificationScheme(String value) {
            this.value = value;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return value;
        }

        /**
         * From string.
         *
         * @param valueString the value string
         * @return the XDSb constant for the given identificationScheme value
         */
        public static IdentificationScheme fromString(String valueString) {
            if (valueString != null) {
                for (IdentificationScheme enumValue : IdentificationScheme.values()) {
                    if (valueString.equals(enumValue.toString())) {
                        return enumValue;
                    }
                }
            }
            throw new IllegalArgumentException("No enum constant " + valueString);
        }
    }
}
