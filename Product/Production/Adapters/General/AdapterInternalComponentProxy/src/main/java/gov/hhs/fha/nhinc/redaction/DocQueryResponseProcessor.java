package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class DocQueryResponseProcessor
{
    private static final String EBXML_DOCENTRY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    private Log log = null;
    private String patientId;
    private String assigningAuthorityId;

    public DocQueryResponseProcessor()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected PatientConsentHelper getPatientConsentHelper()
    {
        return new PatientConsentHelper();
    }
    
    protected void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    protected String getPatientId()
    {
        return patientId;
    }

    protected void setAssigningAuthorityId(String assigningAuthorityId)
    {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    protected String getAssigningAuthorityId()
    {
        return assigningAuthorityId;
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        if(adhocQueryRequest == null)
        {
            log.warn("AdhocQueryRequest was null.");
        }
        else if(adhocQueryResponse == null)
        {
            log.warn("AdhocQueryResponse was null.");

        }
        else
        {
            extractIdentifiers(adhocQueryRequest);
            if((patientId != null) && (!patientId.isEmpty()))
            {
                PatientConsentHelper patientConsentHelper = getPatientConsentHelper();
                if(patientConsentHelper == null)
                {
                    log.warn("PatientConsentHelper was null.");
                }
                else
                {
                    PatientPreferencesType patientPreferences = patientConsentHelper.retrievePatientConsentbyPatientId(patientId, assigningAuthorityId);
                    if(patientPreferences == null)
                    {
                        log.warn("PatientPreferences was null.");
                    }
                    else
                    {
                        response = filterResults(adhocQueryResponse, patientPreferences);
                    }
                }
            }
            else
            {
                log.warn("Not a patient centric query.");
            }
        }
        log.debug("End filterAdhocQueryResults");
        return response;
    }

    protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
    {
        log.debug("Begin extractIdentifiers");
        if(adhocQueryRequest == null)
        {
            log.warn("AdhocQueryRequest was null.");
        }
        else
        {
            AdhocQueryType adhocQuery = adhocQueryRequest.getAdhocQuery();
            List<SlotType1> slots = null;
            if(adhocQuery != null)
            {
                slots = adhocQuery.getSlot();
                List<String> slotValues = extractSlotValues(slots, EBXML_DOCENTRY_PATIENT_ID);
                if((slotValues != null) && (!slotValues.isEmpty()))
                {
                    String formattedPatientId = slotValues.get(0);
                    patientId = PatientIdFormatUtil.parsePatientId(formattedPatientId);
                    assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(formattedPatientId);
                }
            }
        }

        log.debug("End extractIdentifiers");
    }

    protected List<String> extractSlotValues(List<SlotType1> slots, String slotName)
    {
        log.debug("Begin extractSlotValues");
        List<String> returnValues = null;
        if(slots != null)
        {
            for(SlotType1 slot : slots)
            {
                if ((slot.getName() != null) &&
                    (slot.getName().length() > 0) &&
                    (slot.getValueList() != null) &&
                    (slot.getValueList().getValue() != null) &&
                    (slot.getValueList().getValue().size() > 0))
                {

                    if(slot.getName().equals(slotName))
                    {
                        ValueListType valueListType = slot.getValueList();
                        List<String> slotValues = valueListType.getValue();
                        returnValues = new ArrayList<String>();
                        for(String slotValue : slotValues)
                        {
                            returnValues.add(slotValue);
                        }
                    }
                }

            }
        }
        log.debug("End extractSlotValues");
        return returnValues;
    }

    protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
    {
        log.debug("Begin filterResults");
        AdhocQueryResponse response = null;

        log.debug("End filterResults");
        return response;
    }

    protected AdhocQueryResponse filterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterResultsNonPatientCentric");
        AdhocQueryResponse response = null;

        log.debug("End filterResultsNonPatientCentric");
        return response;
    }

}
