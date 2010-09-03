/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.fta;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ejb.Stateless;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
@WebService()
@Stateless()
public class FTAService {
    private static Log log = LogFactory.getLog(FTAService.class);

    public FTAService()
    {
        log.info("FTA Service Starting.");
        this.start();
    }
    /**
     * Web service operation
     */
    @WebMethod(operationName = "start")
    public Boolean start() {
        boolean result = true;
        log.info("begin start()");
        try
        {
            FTATimer.startTimer();
        }
        catch (Exception ex)
        {
            result = false;
        }
        log.info("end start()");
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "stop")
    public Boolean stop() {
        FTATimer.stopTimer();

        return true;
    }
    @Override
    protected void finalize() throws Throwable
    {
      FTATimer.stopTimer();
      super.finalize(); //not necessary if extending Object.
    }
}
