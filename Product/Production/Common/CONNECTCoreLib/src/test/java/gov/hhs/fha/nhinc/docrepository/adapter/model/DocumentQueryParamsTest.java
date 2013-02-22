package gov.hhs.fha.nhinc.docrepository.adapter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentQueryParamsTest {

	private DocumentQueryParams getNewDQParams() {
		DocumentQueryParams docQueryParams = new DocumentQueryParams();

		String patientId = "Patient_1";
		docQueryParams.setPatientId(patientId);

		List<String> classCodeList = new ArrayList<String>();
		String classCode = "classCode";
		classCodeList.add(classCode);
		docQueryParams.setClassCodes(classCodeList);

		String classCodeScheme = "class code scheme";
		docQueryParams.setClassCodeScheme(classCodeScheme);

		Date createTimeFrom = new Date();
		createTimeFrom.setTime(11111L);
		docQueryParams.setCreationTimeFrom(createTimeFrom);

		Date createTimeTo = new Date();
		createTimeTo.setTime(11111L);
		docQueryParams.setCreationTimeTo(createTimeTo);

		List<String> docUniqueIdList = new ArrayList<String>();
		String docUniqueId = "Doc_Unique_Id 1";
		docUniqueIdList.add(docUniqueId);
		docQueryParams.setDocumentUniqueId(docUniqueIdList);

		List<String> statusList = new ArrayList<String>();
		String status = "status";
		statusList.add(status);
		docQueryParams.setStatuses(statusList);

		Date serviceStartTimeFrom = new Date();
		serviceStartTimeFrom.setTime(11111L);
		docQueryParams.setServiceStartTimeFrom(serviceStartTimeFrom);

		Date serviceStartTimeTo = new Date();
		serviceStartTimeTo.setTime(11111L);
		docQueryParams.setServiceStartTimeTo(serviceStartTimeTo);

		Date serviceStopTimeFrom = new Date();
		serviceStopTimeFrom.setTime(11111L);
		docQueryParams.setServiceStopTimeFrom(serviceStopTimeFrom);

		Date serviceStopTimeTo = new Date();
		serviceStopTimeTo.setTime(11111L);
		docQueryParams.setServiceStopTimeTo(serviceStopTimeTo);

		List<EventCodeParam> eventCodeParamList = new ArrayList<EventCodeParam>();
		EventCodeParam eventCodeParam = new EventCodeParam();
		eventCodeParam.setEventCode("event code");
		eventCodeParam.setEventCodeScheme("event code scheme");
		eventCodeParamList.add(eventCodeParam);
		docQueryParams.setEventCodeParams(eventCodeParamList);

		return docQueryParams;
	}

	@Test
	public void equalsTest() {
		DocumentQueryParams param1 = getNewDQParams();
		DocumentQueryParams param2 = getNewDQParams();

		assertTrue(param1.equals(param2));
	}

	@Test
	public void hashCodeTest() {
		DocumentQueryParams param = new DocumentQueryParams();

		String patientId = "Patient ID 1";
		param.setPatientId(patientId);

		assertEquals(param.hashCode(), patientId.hashCode());
	}
}
