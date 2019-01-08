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
package gov.hhs.fha.nhinc.admingui.managed.direct;

import gov.hhs.fha.nhinc.admingui.services.DirectService;
import gov.hhs.fha.nhinc.direct.config.Setting;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jasonasmith / sadusumilli
 */
@ManagedBean(name = "directAgentBean")
@ViewScoped
@Component
public class DirectAgentBean {

    @Autowired
    private DirectService directService;

    private String agentName;
    private String agentValue;

    private Setting selectSetting;
    private List<Setting> settings;

    private static final Logger LOG = LoggerFactory.getLogger(DirectAgentBean.class);

    /**
     *
     * @return
     */
    public List<Setting> getSettings() {
        if (settings == null) {
            refreshSetting();
        }
        return settings;
    }

    /**
     *
     * @param event
     */
    public void addSetting(ActionEvent event) {
        try {
            directService.addSetting(agentName, agentValue);
            agentName = null;
            agentValue = null;
            refreshSetting();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("agentErrorMessages", new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Setting Add Error: Duplicate Key", ex.getLocalizedMessage()));
            LOG.error("Not able to load the document types from the property file: {}", ex.getLocalizedMessage(), ex);
        }
    }

    /**
     *
     */
    public void deleteSetting() {
        if (selectSetting != null) {
            List<String> deleteNames = new ArrayList<>();
            deleteNames.add(selectSetting.getName());
            directService.deleteSetting(deleteNames);
            selectSetting = null;
            refreshSetting();
        }
    }

    /**
     *
     */
    protected void refreshSetting() {
        settings = directService.getSetting();
    }

    /**
     *
     * @return
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     *
     * @param agentName
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     *
     * @return
     */
    public String getAgentValue() {
        return agentValue;
    }

    /**
     *
     * @param agentValue
     */
    public void setAgentValue(String agentValue) {
        this.agentValue = agentValue;
    }

    /**
     *
     * @return
     */
    public Setting getSelectSetting() {
        return selectSetting;
    }

    /**
     *
     * @param selectSetting
     */
    public void setSelectSetting(Setting selectSetting) {
        this.selectSetting = selectSetting;
    }

}
