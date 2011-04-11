/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.perfrepo.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author richard.ettema
 */
public class Perfrepository implements Serializable {

    private Long id;
    private Timestamp starttime;
    private Timestamp stoptime;
    private Long duration;
    private String servicetype;
    private String messagetype;
    private String direction;
    private String communityid;
    private Integer status;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id new value for id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return starttime
     */
    public Timestamp getStarttime() {
        return starttime;
    }

    /**
     * @param starttime new value for starttime
     */
    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    /**
     * @return stoptime
     */
    public Timestamp getStoptime() {
        return stoptime;
    }

    /**
     * @param stoptime new value for stoptime
     */
    public void setStoptime(Timestamp stoptime) {
        this.stoptime = stoptime;
    }

    /**
     * @return duration
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * @param duration new value for duration
     */
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    /**
     * @return servicetype
     */
    public String getServicetype() {
        return servicetype;
    }

    /**
     * @param servicetype new value for servicetype
     */
    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    /**
     * @return messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * @param messagetype new value for messgtetype
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    /**
     * @return direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction new value for direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return communityid
     */
    public String getCommunityid() {
        return communityid;
    }

    /**
     * @param communityid new value for communityid
     */
    public void setCommunityid(String communityid) {
        this.communityid = communityid;
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status new value for status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
