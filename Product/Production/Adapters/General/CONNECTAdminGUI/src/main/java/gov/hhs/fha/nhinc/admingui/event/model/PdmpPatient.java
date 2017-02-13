/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.event.model;

import java.util.List;

/**
 *
 * @author jassmit
 */
public class PdmpPatient extends Patient {
    
    private String narcoticsScore;
    private String stimulantsScore;
    private String sedativesScore;
    
    private String reportUrl;
    private List<String> disallowedPmps;

    public String getNarcoticsScore() {
        return narcoticsScore;
    }

    public void setNarcoticsScore(String narcoticsScore) {
        this.narcoticsScore = narcoticsScore;
    }

    public String getStimulantsScore() {
        return stimulantsScore;
    }

    public void setStimulantsScore(String stimulantsScore) {
        this.stimulantsScore = stimulantsScore;
    }

    public String getSedativesScore() {
        return sedativesScore;
    }

    public void setSedativesScore(String sedativesScore) {
        this.sedativesScore = sedativesScore;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public List<String> getDisallowedPmps() {
        return disallowedPmps;
    }

    public void setDisallowedPmps(List<String> disallowedPmps) {
        this.disallowedPmps = disallowedPmps;
    }
    
}
