/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.managed;

import gov.hhs.fha.nhinc.admingui.model.AvailableService;
import gov.hhs.fha.nhinc.admingui.services.StatusEvent;
import gov.hhs.fha.nhinc.admingui.services.StatusService;
import gov.hhs.fha.nhinc.admingui.services.impl.StatusEventImpl;
import gov.hhs.fha.nhinc.admingui.services.impl.StatusServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "statusBean")
@ViewScoped
public class StatusBean {

    private final StatusService sService = new StatusServiceImpl();
    private final StatusEvent eventService = new StatusEventImpl();
    private List<AvailableService> services = new ArrayList<>();
    private CartesianChartModel eventBarChart;
    private PieChartModel eventPieChart;

    @PostConstruct
    public void initServices() {
        services = sService.buildServices();
        eventService.setCounts();
        initBarChart();
        initPieChart();
    }

    public String getOs() {
        return sService.getOperatingSystem();
    }

    public String getJava() {
        return sService.getJavaVersion();
    }

    public String getMemory() {
        return sService.getMemory();
    }

    public String getAppServer() {
        return sService.getApplicationServer();
    }

    public long getTotalInboundRequests() {
        return eventService.getTotalInboundRequests();
    }
    
    public long getTotalOutboundRequests() {
        return eventService.getTotatOutboundRequests();
    }

    public List<AvailableService> getServices() {
        return services;
    }

    public CartesianChartModel getEventBarChart() {
        return eventBarChart;
    }
    
    public PieChartModel getEventPieChart() {
        if(eventPieChart != null) {
            refreshPieChart();
        }
        return eventPieChart;
    }

    private void refreshPieChart() {
        Map<String, Integer> serviceMap = eventService.getServiceList();       
        for(Entry<String, Integer> serviceEntry : serviceMap.entrySet()) {
            eventPieChart.getData().put(serviceEntry.getKey(), serviceEntry.getValue());
        }
    }

    private void initBarChart() {
        eventBarChart = new CartesianChartModel();

        ChartSeries inboundSeries = createChart("Inbound", eventService.getInboundEventCounts());
        ChartSeries outboundSeries = createChart("Outbound", eventService.getOutboundEventCounts());

        eventBarChart.addSeries(inboundSeries);
        eventBarChart.addSeries(outboundSeries);
    }

    private static ChartSeries createChart(String label, Map<String, Integer> eventCounts) {
        ChartSeries series = new ChartSeries();
        series.setLabel(label);
        for (Entry<String, Integer> serviceEntry : eventCounts.entrySet()) {
            series.set(serviceEntry.getKey(), serviceEntry.getValue());
        }
        return series;
    }
    
    private void initPieChart() {
        eventPieChart = new PieChartModel();
        refreshPieChart();
    }

}
