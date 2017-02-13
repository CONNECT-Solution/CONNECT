/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.event.model.PdmpPatient;
import gov.hhs.fha.nhinc.pdmp.DateRangeType;
import gov.hhs.fha.nhinc.pdmp.PatientType;
import gov.hhs.fha.nhinc.pdmp.SexCodeType;
import java.util.Date;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author jassmit
 */
public interface PdmpService {
    
    public PdmpPatient searchForPdmpInfo(PatientType patient, DateRangeType dateRange);
    public XMLGregorianCalendar getGregorianCalendar(Date date);
    public SexCodeType getSexCodeType(String gender);
    public DateRangeType buildDateRange(Date beginRange, Date endRange);
}
