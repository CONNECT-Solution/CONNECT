/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.perfrepo.model;

import java.util.List;

/**
 *
 * @author richard.ettema
 */
public class PerformanceResponse {

    private List<CountData> countDataList;
    private List<DetailData> detailDataList;

    public List<CountData> getCountDataList() {
        return countDataList;
    }

    public void setCountDataList(List<CountData> countDataList) {
        this.countDataList = countDataList;
    }

    public List<DetailData> getDetailDataList() {
        return detailDataList;
    }

    public void setDetailDataList(List<DetailData> detailDataList) {
        this.detailDataList = detailDataList;
    }

}
