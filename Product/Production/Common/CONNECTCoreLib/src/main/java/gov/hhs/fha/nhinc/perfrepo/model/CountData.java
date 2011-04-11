/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.perfrepo.model;

/**
 * Represents one record of data used for count statistics
 * @author richard.ettema
 */
public class CountData {

    private String type;
    private Long count;
    private Long expected;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getExpected() {
        return expected;
    }

    public void setExpected(Long expected) {
        this.expected = expected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
