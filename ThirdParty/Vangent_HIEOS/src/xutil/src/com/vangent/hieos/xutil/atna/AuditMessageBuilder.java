/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xutil.atna;

import com.vangent.hieos.xutil.xconfig.XConfig;

import com.sun.openesb.ihe.arr.syslogclient.AuditRecordRepoClient;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author Vincent Lewis
 */
public class AuditMessageBuilder {
    private final static Logger logger = Logger.getLogger(AuditMessageBuilder.class);

    private String sourceId;
    private String sourceType;
    private CodedValueType eventId;
    private CodedValueType eventType;
    private String eventAction;
    private String eventOutcome;
    ArrayList apts = new ArrayList();
    ArrayList pois = new ArrayList();
    ArrayList ass = new ArrayList();
    private static String syslogHost = null;
    private static int syslogPort = 0;


    static {
        try {
            syslogHost = XConfig.getInstance().getHomeCommunityProperty("ATNAsyslogHost");//props.getProperty("syslogHost");
            syslogPort = new Integer(XConfig.getInstance().getHomeCommunityProperty("ATNAsyslogPort")).intValue();
            logger.info("XATNALogger: using syslogHost " + syslogHost + " port " + syslogPort);
        } catch (Exception e) {
            logger.error("**** CAN NOT LOAD ATNA properties from XConfig ***", e);
        }
    }

    /**
     * 
     * @param sourceId
     * @param sourceType
     * @param eventId
     * @param eventType
     * @param eventAction
     * @param eventOutcome
     */
    public AuditMessageBuilder(String sourceId, String sourceType, CodedValueType eventId, CodedValueType eventType, String eventAction, String eventOutcome) {
        this.sourceId = sourceId;
        this.sourceType = sourceType;
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventOutcome = eventOutcome;
        this.eventAction = eventAction;
    }

    /**
     * 
     * @param userId
     * @param alternativeUserID
     * @param userName
     * @param userIsRequestor
     * @param roleIDCode
     * @param networkAccessPointTypeCode
     * @param networkAccessPointID
     */
    public void setActiveParticipant(String userId, String alternativeUserID, String userName, String userIsRequestor, CodedValueType roleIDCode, String networkAccessPointTypeCode, String networkAccessPointID) {
        ActiveParticipantType apt = new ActiveParticipantType();
        if (userId != null) {
            apt.setUserID(userId);
        }
        if (alternativeUserID != null) {
            apt.setAlternativeUserID(alternativeUserID);
        }
        if (userName != null) {
            apt.setUserName(userName);
        }
        if (userIsRequestor != null) {
            apt.setUserIsRequestor(new Boolean(userIsRequestor));
        }
        if (roleIDCode != null) {
            List rids = apt.getRoleIDCode();
            rids.add(roleIDCode);
        }
        if (networkAccessPointTypeCode != null) {
            short val = new Short(networkAccessPointTypeCode).shortValue();
            apt.setNetworkAccessPointTypeCode(val);
        }
        if (networkAccessPointID != null) {
            apt.setNetworkAccessPointID(networkAccessPointID);
        }
        apts.add(apt);
    }

    /**
     * 
     * @param participantObjectTypeCode
     * @param participantObjectTypeCodeRole
     * @param participantObjectDataLifeCycle
     * @param poicd
     * @param participantObjectSensitivity
     * @param participantObjectID
     * @param participantObjectName
     * @param participantObjectQuery
     * @param participantObjectDetailName
     * @param participantObjectDetailValue
     */
    public void setParticipantObject(String participantObjectTypeCode, String participantObjectTypeCodeRole, String participantObjectDataLifeCycle, CodedValueType poicd, String participantObjectSensitivity, String participantObjectID, String participantObjectName, byte[] participantObjectQuery, String participantObjectDetailName, byte[] participantObjectDetailValue) {
        ParticipantObjectIdentificationType poi = new ParticipantObjectIdentificationType();
        if (participantObjectTypeCode != null) {
            short val = new Short(participantObjectTypeCode).shortValue();
            poi.setParticipantObjectTypeCode(val);
        }
        if (participantObjectTypeCodeRole != null) {
            short val = new Short(participantObjectTypeCodeRole).shortValue();
            poi.setParticipantObjectTypeCodeRole(val);
        }
        if (participantObjectDataLifeCycle != null) {
            // Do nothing.
        }
        if (poicd != null) {
            poi.setParticipantObjectIDTypeCode(poicd);
        }
        if (participantObjectSensitivity != null) {
            poi.setParticipantObjectSensitivity(participantObjectSensitivity);
        }
        if (participantObjectID != null) {
            poi.setParticipantObjectID(participantObjectID);
        }
        if (participantObjectName != null) {
            poi.setParticipantObjectName(participantObjectName);
        }
        if (participantObjectQuery != null) {
            poi.setParticipantObjectQuery(participantObjectQuery);
        }
        List pods = poi.getParticipantObjectDetail();

        if (participantObjectDetailName != null) {
            TypeValuePairType tvp = new TypeValuePairType();
            tvp.setType(participantObjectDetailName);
            tvp.setValue(participantObjectDetailValue);
            pods.add(tvp);
        }
        pois.add(poi);
    }

    /**
     * 
     * @param sourceId
     * @param auditEnterpriseSiteID
     * @param sourceType
     */
    public void setAuditSource(String sourceId, String auditEnterpriseSiteID, String sourceType) {
        AuditSourceIdentificationType as = new AuditSourceIdentificationType();
        if (sourceId != null) {
            as.setAuditSourceID(sourceId);
        }
        if (auditEnterpriseSiteID != null) {
            as.setAuditEnterpriseSiteID(auditEnterpriseSiteID);
        }
        if (sourceType != null) {
            List las = as.getAuditSourceTypeCode();
            CodedValueType cd = new CodedValueType();
            cd.setCode(sourceType);
            cd.setCodeSystem("");
            cd.setCodeSystemName("");
            las.add(cd);
        }
        ass.add(as);
    }

    /**
     * 
     */
    public void persistMessage() {
        AuditMessage msg = new AuditMessage();
        String ret = null;
        try {
            //   javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(msg.getClass().getPackage().getName());
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(ObjectFactory.class);
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();

            EventIdentificationType ei = new EventIdentificationType();
            if (eventId != null) {

                ei.setEventID(eventId);
            }
            if (eventType != null) {
                List leit = ei.getEventTypeCode();
                leit.add(eventType);
            }
            if (eventAction != null) {
                ei.setEventActionCode(eventAction);
            }

            XMLGregorianCalendar gc = this.getXMLGregorianCalendar();

            //       XMLGregorianCalendar  xgc =  new XMLGregorianCalendar(new Date());

            ei.setEventDateTime(gc);
            if (eventOutcome != null) {
                long leoi = new Long(eventOutcome).longValue();
                BigInteger eoi = BigInteger.valueOf(leoi);
                ei.setEventOutcomeIndicator(eoi);
            }
            msg.setEventIdentification(ei);

            if (ass.size() > 0) {
                Iterator it = ass.iterator();
                List mass = msg.getAuditSourceIdentification();
                while (it.hasNext()) {
                    mass.add((AuditSourceIdentificationType) it.next());
                }
            }
            if (apts.size() > 0) {
                Iterator it = apts.iterator();
                List mapts = msg.getActiveParticipant();
                while (it.hasNext()) {
                    mapts.add((ActiveParticipantType) it.next());
                }
            }
            if (pois.size() > 0) {
                Iterator it = pois.iterator();
                List mpois = msg.getParticipantObjectIdentification();
                while (it.hasNext()) {
                    mpois.add((ParticipantObjectIdentificationType) it.next());
                }
            }

            StringWriter sw = new StringWriter();
            marshaller.marshal(msg, sw);
            StringBuffer sb = sw.getBuffer();
            ret = new String(sb);
            ret = ret.replaceAll("-05:00", "");
            //     int start = ret.indexOf("<AuditMessage>");
            //     int end = ret.length();
            //     ret = ret.substring(start, end);
            //      ret = ret.replaceFirst("<AuditMessage>", "<AuditMessage xmlns:tns=\"http://xml.netbeans.org/schema/rfc3881\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
            AuditRecordRepoClient client = new AuditRecordRepoClient(syslogHost, syslogPort);
            client.sendAuditMessage(ret);
        } catch (Exception e) {
            logger.error("xxx: persistAuditRecord(): Failed to Unmarshall.", e);
        }
    }

    /**
     *
     * @return
     */
    private XMLGregorianCalendar getXMLGregorianCalendar() {
        GregorianCalendar now = new GregorianCalendar();
        DatatypeFactory factory = null;
        XMLGregorianCalendar calendar = null;
        try {
            factory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            logger.error("ATNA: TIMESTAMP PROBLEM in AuditMessageBuilder!", e);
        }
        if (factory != null) {
            calendar = factory.newXMLGregorianCalendar(now);
        }
        return calendar;
    }

    /**
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return the sourceType
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * @param sourceType the sourceType to set
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return the eventId
     */
    public CodedValueType getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(CodedValueType eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the eventType
     */
    public CodedValueType getEventType() {
        return eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(CodedValueType eventType) {
        this.eventType = eventType;
    }

    /**
     * @return the eventAction
     */
    public String getEventAction() {
        return eventAction;
    }

    /**
     * @param eventAction the eventAction to set
     */
    public void setEventAction(String eventAction) {
        this.eventAction = eventAction;
    }

    /**
     * @return the eventOutcome
     */
    public String getEventOutcome() {
        return eventOutcome;
    }

    /**
     * @param eventOutcome the eventOutcome to set
     */
    public void setEventOutcome(String eventOutcome) {
        this.eventOutcome = eventOutcome;
    }
}
