/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.perfrepo.model;

/**
 * Represents one record of data used for detail statistics
 * @author richard.ettema
 */
public class DetailData {

    private String servicetype;
    private String messagetype;
    private String direction;
    private Double avgduration;
    private Long minduration;
    private Long maxduration;
    private Long count;

    public Double getAvgduration() {
        return avgduration;
    }

    public void setAvgduration(Double avgduration) {
        this.avgduration = avgduration;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getMaxduration() {
        return maxduration;
    }

    public void setMaxduration(Long maxduration) {
        this.maxduration = maxduration;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public Long getMinduration() {
        return minduration;
    }

    public void setMinduration(Long minduration) {
        this.minduration = minduration;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

}
