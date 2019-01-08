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
package gov.hhs.fha.nhinc.admingui.managed;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author jasonasmith
 */
@ManagedBean
@RequestScoped
public class ChartBean {

    private CartesianChartModel model;

    /**
     *
     */
    public ChartBean() {
        model = new CartesianChartModel();
        ChartSeries inbound = new ChartSeries();
        inbound.setLabel("Inbound Messages");
        inbound.set("Jan 1", 10);
        inbound.set("Jan 2", 15);
        inbound.set("Jan 3", 17);
        inbound.set("Jan 4", 13);
        inbound.set("Jan 5", 18);

        ChartSeries outbound = new ChartSeries();
        outbound.setLabel("Outbound Messages");
        outbound.set("Jan 1", 20);
        outbound.set("Jan 2", 5);
        outbound.set("Jan 3", 6);
        outbound.set("Jan 4", 10);
        outbound.set("Jan 5", 12);

        ChartSeries error = new ChartSeries();
        error.setLabel("Error Messages");
        error.set("Jan 1", 2);
        error.set("Jan 2", 3);
        error.set("Jan 3", 0);
        error.set("Jan 4", 1);
        error.set("Jan 5", 0);

        model.addSeries(inbound);
        model.addSeries(outbound);
        model.addSeries(error);

    }

    /**
     *
     * @return
     */
    public CartesianChartModel getModel() {
        return model;
    }
}
