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
package gov.hhs.fha.nhinc.admingui.services.impl;

import static gov.hhs.fha.nhinc.admingui.services.impl.PingServiceImpl.IGNORE_DEADHOST;
import static gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper.getEndpointConfigurationTypeBy;

import gov.hhs.fha.nhinc.admingui.application.ApplicationInfo;
import gov.hhs.fha.nhinc.admingui.model.AvailableService;
import gov.hhs.fha.nhinc.admingui.services.PingService;
import gov.hhs.fha.nhinc.admingui.services.StatusService;
import gov.hhs.fha.nhinc.admingui.util.ConnectionHelper;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jassmit
 */
public class StatusServiceImpl implements StatusService {

    private static final String JAVA_VERSION_KEY = "java.version";
    private static final String JAVA_VENDOR_KEY = "java.vm.vendor";
    private static final String OS_KEY = "os.name";
    private static final String OS_VERSION_KEY = "os.version";
    private static final long MB_VALUE = 1048576;

    private static final PingService PING_SERVICE = new PingServiceImpl();

    private static final Logger LOG = LoggerFactory.getLogger(StatusServiceImpl.class);

    @Override
    public String getOperatingSystem() {
        return MessageFormat.format("{0}, {1}", System.getProperty(OS_KEY), System.getProperty(OS_VERSION_KEY));
    }

    @Override
    public String getJavaVersion() {
        return MessageFormat.format("{0}, {1}", System.getProperty(JAVA_VERSION_KEY),
            System.getProperty(JAVA_VENDOR_KEY));
    }

    @Override
    public String getMemory() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapMemUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long otherMemUsed = memoryBean.getNonHeapMemoryUsage().getUsed();
        long totalMemoryUsed = (heapMemUsed + otherMemUsed) / MB_VALUE;

        if (totalMemoryUsed > 0) {
            return Long.toString(totalMemoryUsed) + " MB";
        }
        return null;
    }

    @Override
    public String getApplicationServer() {
        return ApplicationInfo.getInstance().getServerInfo();
    }

    @Override
    public List<AvailableService> buildServices() {
        List<AvailableService> services = new ArrayList<>();
        PING_SERVICE.resetDeadhostList();

        ConnectionHelper cHelper = new ConnectionHelper();
        OrganizationType localOrg = cHelper.getLocalOrganization();
        List<EndpointType> endpoints = ExchangeManagerHelper.getEndpointTypeBy(localOrg);

        if (localOrg != null && CollectionUtils.isNotEmpty(endpoints)) {
            for (NhincConstants.NHIN_SERVICE_NAMES name : NhincConstants.NHIN_SERVICE_NAMES.values()) {
                services.addAll(getAvailableServiceFrom(localOrg, name.getUDDIServiceName()));
            }
        }
        return services;
    }

    private static List<AvailableService> getAvailableServiceFrom(OrganizationType organization, String serviceName) {
        EndpointType endpoint = ExchangeManagerHelper.findEndpointTypeBy(organization, serviceName);
        if (null != endpoint) {
            return getAvailableServiceBy(endpoint);
        }
        LOG.warn("Error cannot find the service '{}' in organization '{}'", serviceName, organization.getName());
        return new ArrayList<>();
    }

    private static List<AvailableService> getAvailableServiceBy(EndpointType endpoint) {
        List<AvailableService> services = new ArrayList<>();
        if (null != endpoint) {
            for (String serviceName : endpoint.getName()) {
                for (EndpointConfigurationType url : getEndpointConfigurationTypeBy(endpoint)) {
                    AvailableService aService = new AvailableService();
                    aService.setServiceName(MessageFormat.format("{0} - {1}", serviceName, url.getVersion()));
                    aService.setResponseCode(PING_SERVICE.ping(url.getUrl(), !IGNORE_DEADHOST));
                    aService.setServiceMessage(!aService.isAvailable()?HttpStatus.getStatusText(aService.getResponseCode()):"");
                    services.add(aService);
                }
            }
        }
        return services;
    }

}
