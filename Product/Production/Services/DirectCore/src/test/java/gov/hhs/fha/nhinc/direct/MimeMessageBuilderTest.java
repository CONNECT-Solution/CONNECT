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
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMailServerProps;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.getMimeMessageBuilder;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.mail.Session;

import org.junit.Test;
import org.nhindirect.xd.common.DirectDocument2;
import org.nhindirect.xd.common.DirectDocuments;
import org.nhindirect.xd.common.type.ClassCodeEnum;
import org.nhindirect.xd.common.type.FormatCodeEnum;
import org.nhindirect.xd.common.type.HealthcareFacilityTypeCodeEnum;
import org.nhindirect.xd.common.type.LoincEnum;
import org.nhindirect.xd.common.type.PracticeSettingCodeEnum;
import org.nhindirect.xd.transform.pojo.SimplePerson;
import org.nhindirect.xd.transform.util.type.MimeType;

/**
 * Test {@link MimeMessageBuilder}.
 */
public class MimeMessageBuilderTest {

	/**
	 * Java mail session.
	 */
	private final Session session = Session.getInstance(getMailServerProps(
			3456, 3143));

	/**
	 * Test that we can build a message with all of the properties of the mime
	 * message set.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test
	public void canBuildMessage() throws IOException {
		assertNotNull(getBuilder().build());
	}

	/**
	 * Test that we can build a message with all of the properties of the mime
	 * message set.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test
	public void canBuildMessageWithDirectDocuments() throws IOException {
		assertNotNull(getBuilder().attachment(null).attachmentName(null)
				.build());
	}

	/**
	 * Throw an exception when the text of the message is missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test(expected = DirectException.class)
	public void willThrowExceptionWhenTextIsMissing() throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().text(null);
		testBuilder.build();
	}

	/**
	 * Allow message to be built if the subject is missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test
	public void canBuildMesageWithoutSubject() throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().subject(null);
		testBuilder.build();
	}

	/**
	 * Throw an exception when the attachment of the message is missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test(expected = DirectException.class)
	public void willThrowExceptionWhenAttachmentIsMissing() throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().documents(null)
				.messageId(null).attachment(null);
		testBuilder.build();
	}

	/**
	 * Throw an exception when the attachment name of the message is missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test(expected = DirectException.class)
	public void willThrowExceptionWhenAttachmentNameIsMissing()
			throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().documents(null)
				.messageId(null).attachmentName(null);
		testBuilder.build();
	}

	/**
	 * Throw an exception when the direct documents are missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test(expected = DirectException.class)
	public void willThrowExceptionWhenDocumentsAreMissing() throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().attachment(null)
				.attachmentName(null).documents(null);
		testBuilder.build();
	}

	/**
	 * Throw an exception when the direct documents messageId is missing.
	 * 
	 * @throws IOException
	 *             is a possible error.
	 */
	@Test(expected = DirectException.class)
	public void willThrowExceptionWhenMessageIdIsMissing() throws IOException {
		MimeMessageBuilder testBuilder = getBuilder().attachment(null)
				.attachmentName(null).messageId(null);
		testBuilder.build();
	}
	
	@Test
	public void buildWithMockDirectDocs() {
		MimeMessageBuilder testBuilder = getBuilder().attachment(null)
				.attachmentName(null).messageId(null).documents(mockDirectDocs()).messageId("1234");
		testBuilder.build();
	}

	private MimeMessageBuilder getBuilder() {
		MimeMessageBuilder builder = null;
		try {
			builder = getMimeMessageBuilder(session);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		return builder;
	}

	private DirectDocuments mockDirectDocs() {

		// Create a collection of documents
		DirectDocuments documents = new DirectDocuments();

		documents.getSubmissionSet().setId("1");
		documents.getSubmissionSet().setName("2");
		documents.getSubmissionSet().setDescription("3");
		documents.getSubmissionSet().setSubmissionTime(new Date());
		documents.getSubmissionSet().setIntendedRecipient(
				Arrays.asList("5.1", "5.2"));
		documents.getSubmissionSet().setAuthorPerson("6");
		documents.getSubmissionSet().setAuthorInstitution(
				Arrays.asList("7.1", "7.2"));
		documents.getSubmissionSet().setAuthorRole("8");
		documents.getSubmissionSet().setAuthorSpecialty("9");
		documents.getSubmissionSet().setAuthorTelecommunication("10");
		documents.getSubmissionSet().setContentTypeCode("11");
		documents.getSubmissionSet().setContentTypeCode_localized("12");
		documents.getSubmissionSet().setUniqueId("13");
		documents.getSubmissionSet().setSourceId("14");
		documents.getSubmissionSet().setPatientId("xxx");

		DirectDocument2 doc1 = new DirectDocument2();
		doc1.setData(new String("data1").getBytes());

		DirectDocument2.Metadata metadata1 = doc1.getMetadata();
		metadata1.setMimeType(MimeType.TEXT_PLAIN.getType());
		metadata1.setId("1.2");
		metadata1.setDescription("1.3");
		metadata1.setCreationTime(new Date());
		metadata1.setLanguageCode("1.5");
		metadata1.setServiceStartTime(new Date());
		metadata1.setServiceStopTime(new Date());
		metadata1.setSourcePatient(new SimplePerson("1.Bob", "1.Smith"));
		metadata1.setAuthorPerson("1.10");
		metadata1.setAuthorInstitution(Arrays.asList("1.11.1", "1.11.2"));
		metadata1.setAuthorRole("1.12");
		metadata1.setAuthorSpecialty("1.13");
		metadata1.setClassCode(ClassCodeEnum.HISTORY_AND_PHYSICAL.getValue());
		metadata1.setClassCode_localized(ClassCodeEnum.HISTORY_AND_PHYSICAL
				.getValue());
		metadata1.setConfidentialityCode("1.16");
		metadata1.setConfidentialityCode_localized("1.17");
		metadata1.setFormatCode(FormatCodeEnum.CARE_MANAGEMENT_CM);
		metadata1
				.setHealthcareFacilityTypeCode(HealthcareFacilityTypeCodeEnum.OF
						.getValue());
		metadata1
				.setHealthcareFacilityTypeCode_localized(HealthcareFacilityTypeCodeEnum.OF
						.getValue());
		metadata1
				.setPracticeSettingCode(PracticeSettingCodeEnum.MULTIDISCIPLINARY
						.getValue());
		metadata1
				.setPracticeSettingCode_localized(PracticeSettingCodeEnum.MULTIDISCIPLINARY
						.getValue());
		metadata1.setLoinc(LoincEnum.LOINC_34133_9.getValue());
		metadata1.setLoinc_localized(LoincEnum.LOINC_34133_9.getValue());
		metadata1.setPatientId("xxx");
		metadata1.setUniqueId("1.27");

		DirectDocument2 doc2 = new DirectDocument2();
		doc2.setData(new String("doc2").getBytes());

		DirectDocument2.Metadata metadata2 = doc2.getMetadata();
		metadata2.setMimeType(MimeType.TEXT_XML.getType());
		metadata2.setId("2.2");
		metadata2.setDescription("2.3");
		metadata2.setCreationTime(new Date());
		metadata2.setLanguageCode("2.5");
		metadata2.setServiceStartTime(new Date());
		metadata2.setServiceStopTime(new Date());
		metadata2.setSourcePatient(new SimplePerson("2.Bob", "2.Smith"));
		metadata2.setAuthorPerson("2.10");
		metadata2.setAuthorInstitution(Arrays.asList("2.11.1", "2.11.2"));
		metadata2.setAuthorRole("2.12");
		metadata2.setAuthorSpecialty("2.13");
		metadata2.setClassCode(ClassCodeEnum.HISTORY_AND_PHYSICAL.getValue());
		metadata2.setClassCode_localized(ClassCodeEnum.HISTORY_AND_PHYSICAL
				.getValue());
		metadata2.setConfidentialityCode("2.16");
		metadata2.setConfidentialityCode_localized("2.17");
		metadata2.setFormatCode(FormatCodeEnum.CDA_LABORATORY_REPORT);
		metadata2
				.setHealthcareFacilityTypeCode(HealthcareFacilityTypeCodeEnum.OF
						.getValue());
		metadata2
				.setHealthcareFacilityTypeCode_localized(HealthcareFacilityTypeCodeEnum.OF
						.getValue());
		metadata2
				.setPracticeSettingCode(PracticeSettingCodeEnum.MULTIDISCIPLINARY
						.getValue());
		metadata2
				.setPracticeSettingCode_localized(PracticeSettingCodeEnum.MULTIDISCIPLINARY
						.getValue());
		metadata2.setLoinc(LoincEnum.LOINC_34133_9.getValue());
		metadata2.setLoinc_localized(LoincEnum.LOINC_34133_9.getValue());
		metadata2.setPatientId("xxx");
		metadata2.setUniqueId("2.27");

		documents.getDocuments().add(doc1);
		documents.getDocuments().add(doc2);
		return documents;
	}

}
