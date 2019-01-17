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
package gov.hhs.fha.nhinc.exchangemgr.util;

import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStatusType;
import gov.hhs.fha.nhinc.common.exchangemanagement.ExchangeDownloadStepStatusType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessor;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessorException;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransformException;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransforms;
import gov.hhs.fha.nhinc.exchange.transform.fhir.FHIRTransform;
import gov.hhs.fha.nhinc.exchange.transform.uddi.UDDITransform;
import gov.hhs.fha.nhinc.exchangemgr.fhir.FHIRDataParserException;
import gov.hhs.fha.nhinc.exchangemgr.fhir.FhirClient;
import gov.hhs.fha.nhinc.exchangemgr.fhir.FhirClientException;
import gov.hhs.fha.nhinc.exchangemgr.fhir.MimeType;
import gov.hhs.fha.nhinc.exchangemgr.fhir.ResponseBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EXCHANGE_TYPE;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author tjafri
 */
public class ExchangeDataUpdateMgr {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeDataUpdateMgr.class);
    private static final String JSON_QUERY_PARAM = "_format=json";
    private boolean hasDownloadOccurred = false;
    private static final String DOWNLOAD_SUCCESS_MSG = "DOWNLOAD_SUCCESS";
    private static final String TRANSFORM_SUCCESS_MSG = "TRANSFORM_SUCCESS";
    private static final String DOWNLOAD_FAILED_MSG = "DOWNLOAD_FAILURE";
    private static final String TRANSFORM_FAILED_MSG = "TRANSFORM_FAILURE";
    private static final String SCHEMA_VAL_FAILED_MSG = "SCHEMA_VAL_FAILED";
    private static final String SCHEMA_VAL_SUCCESS_MSG = "SCHEMA_VAL_SUCCESS";
    private static final String SCHEMA_VAL_SKIP_MSG = "SCHEMA_VAL_SKIP";
    private static final boolean EXCHANGE_REFRESH_IN_PROGRESS = true;
    private static final boolean EXCHANGE_REFRESH_COMPLETED = false;

    public List<ExchangeDownloadStatusType> task() {
        LOG.trace("Starting ExchangeScheduleTask with DAO-locked");
        List<ExchangeDownloadStatusType> status = new ArrayList<>();
        getExchangeDAO().setRefreshLocked(EXCHANGE_REFRESH_IN_PROGRESS);
        boolean result;
        try {
            ExchangeInfoType exInfo = getExchangeDAO().loadExchangeInfo();
            if (null != exInfo) {
                for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                    result = fetchExchangeData(ex, status);
                    hasDownloadOccurred = hasDownloadOccurred || result;
                }
                if (hasDownloadOccurred) {
                    createFileBackupByRenaming(exInfo.getMaxNumberOfBackups());
                    getExchangeDAO().saveExchangeInfo(exInfo);
                }
            }
        } catch (Exception ex) {
            LOG.error("Unable to read/write to exchangeInfo file:  {}", ex.getLocalizedMessage(), ex);
        } finally {
            getExchangeDAO().setRefreshLocked(EXCHANGE_REFRESH_COMPLETED);
        }
        LOG.trace("Ending ExchangeScheduleTask with DAO-unlocked");
        return status;
    }

    protected UDDIAccessor getUDDIAccessor() {
        return new UDDIAccessor();
    }

    protected FhirClient getFhirClient() {
        return new FhirClient();
    }

    protected ExchangeInfoDAOFileImpl getExchangeDAO() {
        return ExchangeInfoDAOFileImpl.getInstance();
    }

    protected boolean fetchExchangeData(ExchangeType exchange, List<ExchangeDownloadStatusType> statues) {
        if (exchange != null && StringUtils.isNotEmpty(exchange.getUrl()) && !exchange.isDisabled()) {
            ExchangeDownloadStatusType status = null;
            if (EXCHANGE_TYPE.UDDI.toString().equalsIgnoreCase(exchange.getType())) {
                status = fetchUDDIData(exchange);
            } else if (EXCHANGE_TYPE.FHIR.toString().equalsIgnoreCase(exchange.getType())) {
                status = fetchFHIRData(exchange);
            }

            if (null != status) {
                statues.add(status);
                return status.isSuccess();
            }
        }
        return false;
    }

    private static XMLGregorianCalendar getTimestamp() {
        return XMLDateUtil.long2Gregorian(System.currentTimeMillis());
    }

    private void createFileBackupByRenaming(BigInteger noOfBackups) {
        ExchangeFileUtils fileUtils = new ExchangeFileUtils();
        String fileLocation = getExchangeDAO().getExchangeFileLocation();
        File exFile = new File(fileLocation);
        int allowedBackups = null != noOfBackups ? noOfBackups.intValue() : 0;
        fileUtils.deleteOldBackups(exFile.getParentFile(), allowedBackups);
        if (allowedBackups > 0) {
            String backupFileLocation = fileUtils.generateUniqueFilename(fileLocation);
            try {
                File newBackupFile = new File(backupFileLocation);
                if (exFile.exists()) {
                    exFile.renameTo(newBackupFile);
                }
            } catch (Exception e) {
                LOG.error("Failed to rename the current file {} to {}: {}", fileLocation, backupFileLocation,
                    e.getLocalizedMessage(), e);
            }
        }
    }

    private static MimeType getMimeType(String url) {
        if (containsIgnoreCaseOf(url, JSON_QUERY_PARAM)) {
            return MimeType.JSON;
        }
        return MimeType.XML;
    }

    private static boolean containsIgnoreCaseOf(final String stringOf, final String charSequence) {
        return stringOf.toLowerCase().contains(charSequence.toLowerCase());
    }

    private ExchangeDownloadStatusType fetchUDDIData(ExchangeType exchange) {
        String exName = exchange.getName();
        ExchangeDownloadStatusType exStatus = new ExchangeDownloadStatusType();
        exStatus.setExchangeName(exName);

        try {
            LOG.info("Starting UDDI download from {}", exchange.getUrl());
            BusinessDetail bDetail = getUDDIAccessor().retrieveFromUDDIServer(exchange);
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, DOWNLOAD_SUCCESS_MSG, null, null));
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, SCHEMA_VAL_SKIP_MSG, null, null));
            ExchangeTransforms<BusinessDetail> transformer = new UDDITransform();
            LOG.info("Transforming UDDI data to exchange schema");
            exchange.setOrganizationList(transformer.transform(bDetail));
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, TRANSFORM_SUCCESS_MSG, null, null));
            exchange.setLastUpdated(getTimestamp());
            exStatus.setSuccess(true);
        } catch (UDDIAccessorException ex) {
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, DOWNLOAD_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, SCHEMA_VAL_SKIP_MSG, null, null));
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, TRANSFORM_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.setSuccess(false);
        } catch (ExchangeTransformException ex) {
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, TRANSFORM_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.setSuccess(false);
        }
        return exStatus;
    }

    private ExchangeDownloadStatusType fetchFHIRData(ExchangeType exchange) {
        String exName = exchange.getName();
        ExchangeDownloadStatusType exStatus = new ExchangeDownloadStatusType();
        exStatus.setExchangeName(exName);

        try {
            LOG.info("Starting FHIR download from {}", exchange.getUrl());
            MimeType format = getMimeType(exchange.getUrl());
            FhirClient client = getFhirClient();
            String response = client.doGet(exchange.getUrl(), exchange.getSniName(), format);
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, DOWNLOAD_SUCCESS_MSG, null, null));
            Bundle bundle = ResponseBuilder.build(response, format);
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, SCHEMA_VAL_SUCCESS_MSG, null, null));
            ExchangeTransforms<Bundle> transformer = new FHIRTransform();
            LOG.info("Transforming FHIR data to exchange schema");
            exchange.setOrganizationList(transformer.transform(bundle));
            exStatus.getStepStatus().add(buildExchangeDownloadStatusMsg(true, TRANSFORM_SUCCESS_MSG, null, null));
            exchange.setLastUpdated(getTimestamp());
            exStatus.setSuccess(true);
        } catch (FhirClientException ex) {
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, DOWNLOAD_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, SCHEMA_VAL_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, TRANSFORM_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.setSuccess(false);
        } catch (FHIRDataParserException ex) {
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, SCHEMA_VAL_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, TRANSFORM_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.setSuccess(false);
        } catch (ExchangeTransformException ex) {
            exStatus.getStepStatus()
                .add(buildExchangeDownloadStatusMsg(false, TRANSFORM_FAILED_MSG, ex.getLocalizedMessage(), ex));
            exStatus.setSuccess(false);
        }
        return exStatus;
    }

    private static String getMessage(String key) {
        try {
            return PropertyAccessor.getInstance().getProperty(NhincConstants.MESSAGES_PROPERTY_FILE, key);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to read {} from file {}: {}", key, NhincConstants.MESSAGES_PROPERTY_FILE,
                ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    private static ExchangeDownloadStepStatusType buildExchangeDownloadStatusMsg(boolean success, String msgKey,
        String exceptionMsg, Exception exception) {
        ExchangeDownloadStepStatusType stepStatus = new ExchangeDownloadStepStatusType();
        String message = getMessage(msgKey);
        if (success) {
            LOG.info(message);
        } else {
            LOG.error(message, exceptionMsg, exception);
        }
        stepStatus.setMessage(message);
        stepStatus.setStepSuccessful(success);
        return stepStatus;
    }
}
