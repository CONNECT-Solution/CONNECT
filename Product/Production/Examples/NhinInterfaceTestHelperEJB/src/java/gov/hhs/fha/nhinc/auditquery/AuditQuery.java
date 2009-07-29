package gov.hhs.fha.nhinc.auditquery;

import com.nhin.services.wsdl.auditlogquery.AuditLogQuery;
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "findAuditEvents", portName = "AuditLogQuery", endpointInterface = "com.nhin.services.wsdl.auditlogquery.AuditLogQuery", targetNamespace = "http://services.nhin.com/wsdl/auditlogquery", wsdlLocation = "META-INF/wsdl/AuditQuery/NhinAuditLogQuery.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class AuditQuery implements AuditLogQuery {

    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(AuditQuery.class);

    public java.util.List<com.services.nhinc.schema.auditmessage.AuditMessageType> findAuditEvents(java.lang.String patientId, java.lang.String userId, javax.xml.datatype.XMLGregorianCalendar beginDateTime, javax.xml.datatype.XMLGregorianCalendar endDateTime) {
        FindAuditEventsType query = new FindAuditEventsType();
        
        if (NullChecker.isNotNullish(userId)) {
            log.info("User: " + userId);
            query.setUserId(userId);
        }

        if (NullChecker.isNotNullish(patientId)) {
            log.info("Patient: " + patientId);
            query.setPatientId(patientId);
        }

        if (beginDateTime != null) {
            log.info("Begin Time: " + beginDateTime.toString());
            query.setBeginDateTime(beginDateTime);
        }

        if (endDateTime != null) {
            log.info("End Time: " + endDateTime.toString());
            query.setEndDateTime(endDateTime);
        }

        FindAuditEventsResponseType resp = AuditQueryImpl.auditQuery(query, context);

        if (resp != null &&
                NullChecker.isNotNullish(resp.getFindAuditEventsReturn())) {
            return resp.getFindAuditEventsReturn();
        } else {
            return null;
        }
    }
}
