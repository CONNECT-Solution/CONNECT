/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.event.model.PdmpPatient;
import gov.hhs.fha.nhinc.admingui.services.PdmpService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.pdmp.DateRangeType;
import gov.hhs.fha.nhinc.pdmp.LocationType;
import gov.hhs.fha.nhinc.pdmp.LocationType.Address;
import gov.hhs.fha.nhinc.pdmp.PatientRequestType;
import gov.hhs.fha.nhinc.pdmp.PatientResponseType;
import gov.hhs.fha.nhinc.pdmp.PatientType;
import gov.hhs.fha.nhinc.pdmp.ProviderType;
import gov.hhs.fha.nhinc.pdmp.ReportRequestType;
import gov.hhs.fha.nhinc.pdmp.ReportResponseType;
import gov.hhs.fha.nhinc.pdmp.RequesterType;
import gov.hhs.fha.nhinc.pdmp.RoleType;
import gov.hhs.fha.nhinc.pdmp.SexCodeType;
import gov.hhs.fha.nhinc.pdmp.USStateCodeType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.common.util.Base64Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jassmit
 */
@Service
public class PdmpServiceImpl implements PdmpService {

    private static final String URL_PROP_NAME = "pmpUrl";
    private static final String USER_PROP_NAME = "pmpUser";
    private static final String PASS_PROP_NAME = "pmpPass";

    gov.hhs.fha.nhinc.pdmp.ObjectFactory of = new gov.hhs.fha.nhinc.pdmp.ObjectFactory();

    private static final Logger LOG = LoggerFactory.getLogger(PdmpServiceImpl.class);

    @Override
    public PdmpPatient searchForPdmpInfo(PatientType patient, DateRangeType dateRange) {

        PdmpPatient resultPatient = null;

        PatientRequestType request = buildPatientRequest(patient, dateRange);

        try {
            String serviceUrl = getUrl();
            String userAndPass = getUserNameAndPassword();

            String authorizationHeader = "Basic " + Base64Utility.encode(userAndPass.getBytes());
            PatientResponseType response = null;

            try {
                response = getWebClient(authorizationHeader, serviceUrl)
                        .post(of.createPatientRequest(request), PatientResponseType.class);
            } catch (Exception ex) {
                LOG.error("Error with PDMP client: {}", ex.getLocalizedMessage(), ex);
            }

            if (allowed(response) && response.getReport() != null) {
                resultPatient = new PdmpPatient();
                if (response.getReport().getReportRequestURLs() != null && response.getReport().getReportRequestURLs().getViewableReport() != null) {
                    ReportRequestType reportRequest = buildReportRequest(response.getReport().getReportRequestURLs().getViewableReport().getValue());
                    ReportResponseType reportResponse
                            = getWebClient(authorizationHeader, response.getReport().getReportRequestURLs().getViewableReport().getValue())
                                    .post(of.createReportRequest(reportRequest), ReportResponseType.class);
                    if (reportResponse != null && NullChecker.isNotNullish(reportResponse.getReportLink())) {
                        resultPatient.setReportUrl(reportResponse.getReportLink());
                    }
                }
            }

        } catch (PropertyAccessException ex) {
            LOG.error("Unable to create client due to property accessor issue.", ex);
        }

        return resultPatient;
    }

    @Override
    public XMLGregorianCalendar getGregorianCalendar(Date date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(df.format(date));
        } catch (DatatypeConfigurationException ex) {
            LOG.warn("Unable to convert Date to XMLGregorianCalendar.", ex);
        }
        return null;
    }

    @Override
    public SexCodeType getSexCodeType(String gender) {
        if ("M".equalsIgnoreCase(gender)) {
            return SexCodeType.M;
        } else if ("F".equalsIgnoreCase(gender)) {
            return SexCodeType.F;
        } else {
            return SexCodeType.U;
        }
    }

    @Override
    public DateRangeType buildDateRange(Date beginRange, Date endRange) {
        DateRangeType dateRange = null;
        Date now = new Date();
        if (beginRange != null && endRange != null
                && beginRange.before(now) && beginRange.before(endRange)) {
            dateRange = new DateRangeType();
            dateRange.setBegin(getGregorianCalendar(beginRange));
            dateRange.setEnd(getGregorianCalendar(endRange));
        }
        return dateRange;
    }

    private String getUrl() throws PropertyAccessException {
        return getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, URL_PROP_NAME);
    }

    private String getUserNameAndPassword() throws PropertyAccessException {
        StringBuilder builder = new StringBuilder();
        builder.append(getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, USER_PROP_NAME));
        builder.append(":");
        builder.append(getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, PASS_PROP_NAME));

        return builder.toString();
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    protected PatientRequestType buildPatientRequest(PatientType patient, DateRangeType dRange) {
        PatientRequestType request = new PatientRequestType();

        PatientRequestType.PrescriptionRequest presRequest = new PatientRequestType.PrescriptionRequest();
        presRequest.setPatient(patient);

        if (dRange != null) {
            presRequest.setDateRange(dRange);
        }
        request.setPrescriptionRequest(presRequest);

        RequesterType requester = buildRequester();
        request.setRequester(requester);

        return request;
    }

    private RequesterType buildRequester() {
        RequesterType requester = new RequesterType();

        requester.setProvider(buildProvider());
        requester.setLocation(buildLocation());

        return requester;
    }

    private LocationType buildLocation() {
        LocationType location = new LocationType();
        Address address = new Address();
        address.setStateCode(USStateCodeType.KS);
        location.getContent().add(of.createLocationTypeName("Federal Agency"));
        location.getContent().add(of.createLocationTypeDEANumber("AB1234579"));
        location.getContent().add(of.createLocationTypeAddress(address));
        return location;
    }

    private ProviderType buildProvider() {
        ProviderType provider = new ProviderType();
        provider.getContent().add(of.createProviderTypeRole(RoleType.PHYSICIAN));
        provider.getContent().add(of.createProviderTypeFirstName("Jason"));
        provider.getContent().add(of.createProviderTypeLastName("Smith"));
        provider.getContent().add(of.createProviderTypeDEANumber("AB1234579"));
        return provider;
    }

    protected InputStream getViewableForm(String formSite, String userPass) {
        try {
            URL url = new URL(formSite);
            String encoding = Base64Utility.encode(userPass.getBytes());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            return (InputStream) connection.getInputStream();
        } catch (IOException ex) {
            LOG.warn("Unable to read viewable report from: {}", formSite, ex);
        }
        return null;
    }

    private boolean allowed(PatientResponseType response) {
        if (response == null) {
            return false;
        }

        return (response.getError() == null || !(response.getResponse() != null && !response.getResponse().isEmpty()
                && response.getResponse().get(0) != null && response.getResponse().get(0).getDisallowed() != null));
    }

    private WebClient getWebClient(String authorizationHeader, String serviceUrl) {
        return WebClient.create(serviceUrl)
                .accept(MediaType.APPLICATION_XML)
                .type(MediaType.APPLICATION_XML)
                .header("Authorization", authorizationHeader);
    }

    private ReportRequestType buildReportRequest(String reportLink) {
        ReportRequestType reportRequest = new ReportRequestType();
        ReportRequestType.Requester requester = new ReportRequestType.Requester();
        requester.setReportLink(reportLink);
        requester.setLocation(buildLocation());
        requester.setProvider(buildProvider());

        reportRequest.setRequester(requester);

        return reportRequest;
    }

}
