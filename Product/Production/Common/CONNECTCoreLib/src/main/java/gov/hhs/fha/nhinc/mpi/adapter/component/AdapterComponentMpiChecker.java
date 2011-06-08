/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author richard.ettema
 */
public interface AdapterComponentMpiChecker {
    public PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query);

    public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query);
}
