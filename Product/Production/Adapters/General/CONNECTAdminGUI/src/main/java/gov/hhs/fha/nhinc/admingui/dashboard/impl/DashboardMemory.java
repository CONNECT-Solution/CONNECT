/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.dashboard.impl;

import gov.hhs.fha.nhinc.admingui.dashboard.DashboardObserver;
import gov.hhs.fha.nhinc.admingui.dashboard.DashboardPanel;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 *
 * @author jasonasmith
 */
public class DashboardMemory extends DashboardPanelAbstract implements DashboardPanel {

    private final String type = "Memory";
    private String title;
    private String description;

    private static final long MB_VALUE = 1048576;

    /**
     *
     */
    public DashboardMemory() {

    }

    /**
     *
     * @param observer
     * @param closed
     */
    public DashboardMemory(DashboardObserver observer, boolean closed) {
        setObserver(observer);
        setClosed(closed);
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    @Override
    public DashboardPanel setData() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapMemUsed = memoryBean.getHeapMemoryUsage().getUsed();
        long otherMemUsed = memoryBean.getNonHeapMemoryUsage().getUsed();
        long totalMemoryUsed = (heapMemUsed + otherMemUsed) / MB_VALUE;

        if (totalMemoryUsed > 0) {
            title = Long.toString(totalMemoryUsed) + " MB";
        } else {
            title = "Unknown";
        }
        return this;
    }

    /**
     *
     */
    @Override
    public void close() {
        setClosed(true);
        getObserver().closePanel(DashboardMemory.class);
    }

    /**
     *
     */
    @Override
    public void open() {
        setClosed(false);
        getObserver().openPanel(DashboardMemory.class);
    }

}
