/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.exchangemgr;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessorException;
import gov.hhs.fha.nhinc.connectmgr.uddi.UDDIUpdateManagerHelper;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransforms;
import gov.hhs.fha.nhinc.exchange.transform.fhir.FHIRTransform;
import gov.hhs.fha.nhinc.exchange.transform.uddi.UDDITransform;
import gov.hhs.fha.nhinc.fhir.FhirClient;
import gov.hhs.fha.nhinc.fhir.FhirClientException;
import gov.hhs.fha.nhinc.fhir.MimeType;
import gov.hhs.fha.nhinc.fhir.RequestBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.EXCHANGE_TYPE;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.io.File;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author tjafri
 */
public class ExchangeScheduledTask {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeScheduledTask.class);
    private static final ArrayList<String> backupFileList = new ArrayList<>();
    private static final String JSON_QUERY_PARAM = "_format=json";
    private boolean hasDownloadOccurred = false;

    public void task() {
        LOG.info("Starting ExchangeSceduleTask");
        boolean result;
        try {
            ExchangeInfoType exInfo = getExchangeDAO().loadExchangeInfo();
            if (null != exInfo) {
                for (ExchangeType ex : exInfo.getExchanges().getExchange()) {
                    result = fetchExchangeData(ex);
                    hasDownloadOccurred = hasDownloadOccurred || result;
                }
                if (hasDownloadOccurred) {
                    createFileBackupByRenaming(exInfo.getMaxNumberOfBackups());
                    getExchangeDAO().saveExchangeInfo(exInfo);
                }
            }
        } catch (ExchangeManagerException ex) {
            LOG.error("Unable to read/write to exchangeInfo file:  {}", ex.getLocalizedMessage(), ex);
        }
    }

    protected UDDIUpdateManagerHelper getUDDIManager() {
        return new UDDIUpdateManagerHelper();
    }

    protected ExchangeInfoDAOFileImpl getExchangeDAO() {
        return ExchangeInfoDAOFileImpl.getInstance();
    }

    private boolean fetchExchangeData(ExchangeType exchange) {
        boolean isDownloaded = false;
        try {
            if (exchange != null) {
                if (EXCHANGE_TYPE.UDDI.toString().equalsIgnoreCase(exchange.getType()) && StringUtils.
                    isNotEmpty(exchange.getUrl())
                    && !exchange.isDisabled()) {
                    LOG.info("Starting UDDI download from {}", exchange.getUrl());
                    ExchangeTransforms<BusinessDetail> transformer = new UDDITransform();
                    exchange.setOrganizationList(transformer.transform(getUDDIManager().forceRefreshUDDIFile(exchange.
                        getUrl())));
                    exchange.setLastUpdated(getTimestamp());
                    isDownloaded = true;
                } else if (EXCHANGE_TYPE.FHIR.toString().equalsIgnoreCase(exchange.getType()) && StringUtils.isNotEmpty(
                    exchange.getUrl()) && !exchange.isDisabled()) {
                    LOG.info("Starting FHIR download from {}", exchange.getUrl());
                    ExchangeTransforms<Bundle> transformer = new FHIRTransform();
                    exchange.setOrganizationList(transformer.transform(fetchFhirDirectoryData(exchange.getUrl())));
                    exchange.setLastUpdated(getTimestamp());
                    isDownloaded = true;
                }
            }
        } catch (UDDIAccessorException | FhirClientException | URISyntaxException ex) {
            LOG.error("Unable to download from Exchange {}", ex.getLocalizedMessage(), ex);
        }
        return isDownloaded;
    }

    private static XMLGregorianCalendar getTimestamp() {
        return XMLDateUtil.long2Gregorian(System.currentTimeMillis());
    }

    private void createFileBackupByRenaming(BigInteger noOfBackups) {
        if (null != noOfBackups && noOfBackups.intValue() > 0) {
            String fileLocation = getExchangeDAO().getExchangeFileLocation();
            String backupFileLocation = generateUniqueFilename(fileLocation);

            try {
                File currentFile = new File(fileLocation);
                File newBackupFile = new File(backupFileLocation);

                if (currentFile.exists()) {
                    currentFile.renameTo(newBackupFile);
                    addToBackupList(backupFileLocation, noOfBackups);
                }
            } catch (Exception e) {
                LOG.error("Failed to rename the current file {} to {}: {}", fileLocation, backupFileLocation,
                    e.getLocalizedMessage(), e);
            }
        }
    }

    private static String generateUniqueFilename(String fileLocation) {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
        return fileLocation + "." + dateFormat.format(currentTime.getTime());
    }

    private static void addToBackupList(String latestFilename, BigInteger maxNumBackup) {
        int noOfBackups = maxNumBackup.intValue();

        String filenameToDelete = null;
        if (backupFileList.size() >= noOfBackups) {
            filenameToDelete = backupFileList.remove(0);
        }
        backupFileList.add(latestFilename);

        if (filenameToDelete != null) {
            try {
                File fileToDelete = new File(filenameToDelete);
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
            } catch (Exception e) {
                LOG.warn("Failed to delete backup file {}: {}", filenameToDelete, e.getLocalizedMessage(), e);
            }
        }
    }

    private static Bundle fetchFhirDirectoryData(String url) throws FhirClientException, URISyntaxException {
        MimeType format = getMimeType(url);
        HttpGet request = RequestBuilder.get(url, format);
        FhirClient client = new FhirClient();
        return client.sendRequest(request, format);
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
}
