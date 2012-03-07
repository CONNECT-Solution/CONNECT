/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.adapter.commondatalayer;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 * 
 * @author kim
 */
@WebService(serviceName = "CommonDataLayerService", portName = "CommonDataLayerPort", endpointInterface = "gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapter:commondatalayer", wsdlLocation = "META-INF/wsdl/AdapterCommonDataLayer/AdapterCommonDataLayer.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Stateless
public class AdapterCommonDataLayer implements CommonDataLayerPortType {

    public org.hl7.v3.FindEncountersPRPAMT900350UV02ResponseType getEncounters(
            org.hl7.v3.FindEncountersPRPAIN900300UV02RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getProcedures(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getMedications(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().getMedications(param0);
    }

    @Override
    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAllergies(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().getAllergies(param0);
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getVitals(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getProblems(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().getProblems(param0);
    }

    @Override
    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getTestResults(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().getTestResults(param0);
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAdmissions(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getAppointments(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getImmunizations(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getFamilyHistory(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getSocialHistory(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getInsurances(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType getOrders(
            org.hl7.v3.CareRecordQUPCIN043100UV01RequestType param0) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public org.hl7.v3.PatientDemographicsPRPAMT201303UV02ResponseType getPatienInfo(
            org.hl7.v3.PatientDemographicsPRPAIN201307UV02RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().getPatienInfo(param0);
    }

    @Override
    public org.hl7.v3.FindPatientsPRPAMT201310UV02ResponseType findPatients(
            org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType param0) {
        return AdapterCommonDataLayerImpl.getInstance().findPatients(param0);
    }

}
