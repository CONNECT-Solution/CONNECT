//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//####################################################################
//********************************************************************
// FILE: AssertionUtil.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: AssertionUtil.java
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//
//Feb 24 2010 PTR xxx R.Robinson   Initial Coding.
//
//********************************************************************
package gov.hhs.fha.nhinc.lift.common.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import gov.hhs.fha.nhinc.common.nhinccommon.SamlAuthzDecisionStatementType;

import java.util.Calendar;

/**
 * @author rrobin20
 *
 */
public class AssertionUtil {
	private static final String DATE_FORMAT_NOW = "MM/dd/yyyy HH:mm:ss";
	
	/**
	 * Currently, this method on makes sure the user names of the two assertions
	 * are the same.  This should be expanded to make sure all of both assertions are identical.
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean compareAssertions(AssertionType a1, AssertionType a2)
	{
		return a1.getUserInfo().getUserName().equals(a2.getUserInfo().getUserName());
	}
	
	/**
	 * Maybe was can just take the Assertion that was passed in the subscribe 
	 * request and reset any time outs it might have.  Then we can just marshal
	 * it back to XML and use that instead of making a new one and trying to 
	 * load data into it.
	 * 
	 * Note: These elements don't seem to exist in 2.3.
	 * @param type
	 */
	public static void updateDates(AssertionType type)
	{
		//TODO need to do more null checking than this
		if(type.getSamlAuthzDecisionStatement() == null)
			type.setSamlAuthzDecisionStatement(createDecisionStatement());
		
		type.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(now());
		type.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(expirationDate());
	}
	
	/**
	 * Will create a new AssertionType but will borrow some values from the 
	 * assertion such as UserInfo, HC, ect.  Can be used to translate an
	 * assertion into an assertion with only as much information in it as an
	 * Assertion built by createAssertion().
	 */
	public static AssertionType createAssertion(AssertionType type)
	{
		AssertionType result = new AssertionType();
		
		result.setAuthorized(type.isAuthorized());
//      result.setDateOfBirth("19800516");
		result.setSamlAuthzDecisionStatement(createDecisionStatement());
		result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(now());
		result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(expirationDate());	
		result.setExplanationNonClaimantSignature(type.getExplanationNonClaimantSignature());
//      result.setHaveSecondWitnessSignature(true);
		result.setHaveSecondWitnessSignature(false);
//      result.setHaveSignature(true);
		result.setHaveSignature(false);
//      result.setHaveWitnessSignature(true);
		result.setHaveWitnessSignature(false);
//      result.setSecondWitnessAddress(createAddress());
//      result.setSecondWitnessName(createPersonName("John", "Q.", "Witness"));
		result.setHomeCommunity(type.getHomeCommunity());
//      result.setPersonName(createPersonName("John", "Q.", "Nhin"));
//      result.setPhoneNumber(createPhoneType());
//      result.setWitnessPhone(createPhoneType());
//      result.setWitnessAddress(createAddress());
		result.setUserInfo(type.getUserInfo());

//      result.setSSN("123456789");
		result.setPurposeOfDisclosureCoded(type.getPurposeOfDisclosureCoded());
		result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy("Ref-Clm-123");
		
		return result;
	}
	
	/**
	 * Creates a dummy Assertion.  Borrowed from the FTA Util class.  I'm not
	 * sure if this is sufficient to create a usable Assertion or not.
	 * @return
	 */
	public static AssertionType createAssertion()
    {
        AssertionType result = new AssertionType();
        
        /*
         * Fields that the Notify outbound self test uses.  This is a more 
         * complete set than what the FTA uses, but it has user information
         * which the LST/FTA do not care about.
         */
//        Address - U
//        DoBirth - U
//        DoSignature
//        expirationDate
//        explanationNonClaimantSignature Electronic - U?
//        haveSecondWitnessSignature false
//        haveSignature false
//        haveWitnessSignature false
//        homeCommunity - U?
//        personName - U
//        phoneNumber - U
//        purposeOfDisclosure TREATMENT - U
//        secondWitnessAddress
//        secondWitnessName
//        secondWitnessPhone
//        SSN - U
//        uniquePatientId - U
//        witnessAddress
//        witnessName
//        witnessPhone
//        userInfo
//        authorized
//        purposeOfDisclosureCoded
//        samlAuthnStatement - not sure what info to put in a lot of these fields
//        samlAuthzDecisionStatement - not sure what info to put in a lot of these fields

        // From 2.2 FTA
//        result.setAuthorized(true);
//        result.setDateOfBirth("19800516");
//        result.setDateOfSignature(now());
//        result.setExpirationDate(expirationDate());
//        result.setExplanationNonClaimantSignature("non-null");
//        result.setHaveSecondWitnessSignature(true);
//        result.setHaveSignature(true);
//        result.setHaveWitnessSignature(true);
//        result.setSecondWitnessAddress(createAddress());
//        result.setSecondWitnessName(createPersonName("John", "Q.", "Witness"));
//        result.setHomeCommunity(createHomeCommunity());
//        result.setPersonName(createPersonName("John", "Q.", "Nhin"));
//        result.setPhoneNumber(createPhoneType());
//        result.setWitnessPhone(createPhoneType());
//        result.setWitnessAddress(createAddress());
//        result.setUserInfo(CreateUser());
//  
//        result.setSSN("123456789");
//        result.setPurposeOfDisclosure("needed for SSA");
//        result.setPurposeOfDisclosureCoded(createPurposeCode());
//        result.setClaimFormRef("Ref-Clm-123");
//        result.setClaimFormRaw("Ref-Clm-123".getBytes());
        
        // From 2.3 FTA with modifications after looking at outbound test
        result.setAuthorized(true);
//        result.setDateOfBirth("19800516");
        result.setSamlAuthzDecisionStatement(createDecisionStatement());
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotBefore(now());
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().setNotOnOrAfter(expirationDate());
        result.setExplanationNonClaimantSignature("non-null");
//        result.setHaveSecondWitnessSignature(true);
        result.setHaveSecondWitnessSignature(false);
//        result.setHaveSignature(true);
        result.setHaveSignature(false);
//        result.setHaveWitnessSignature(true);
        result.setHaveWitnessSignature(false);
//        result.setSecondWitnessAddress(createAddress());
//        result.setSecondWitnessName(createPersonName("John", "Q.", "Witness"));
        result.setHomeCommunity(createHomeCommunity());
//        result.setPersonName(createPersonName("John", "Q.", "Nhin"));
//        result.setPhoneNumber(createPhoneType());
//        result.setWitnessPhone(createPhoneType());
//        result.setWitnessAddress(createAddress());
        result.setUserInfo(CreateUser());

//        result.setSSN("123456789");
        result.setPurposeOfDisclosureCoded(createPurposeCode());
        result.getSamlAuthzDecisionStatement().getEvidence().getAssertion().setAccessConsentPolicy("Ref-Clm-123");
        
        return result;
    }
	
	private static SamlAuthzDecisionStatementType createDecisionStatement()
	{
		SamlAuthzDecisionStatementType dec = new SamlAuthzDecisionStatementType();
		
		SamlAuthzDecisionStatementEvidenceType ev = new SamlAuthzDecisionStatementEvidenceType();
		dec.setEvidence(ev);
		
		SamlAuthzDecisionStatementEvidenceAssertionType assrt = new SamlAuthzDecisionStatementEvidenceAssertionType();
		ev.setAssertion(assrt);
		
		SamlAuthzDecisionStatementEvidenceConditionsType cond = new SamlAuthzDecisionStatementEvidenceConditionsType();
		assrt.setConditions(cond);
		
		return dec;
	}
    
    private static String now() {
        Calendar cal = Calendar.getInstance();

        return formatDate(cal.getTime());
      }
    
    private static String expirationDate()
    {
        Calendar cal = Calendar.getInstance();
        
        cal.add(Calendar.YEAR, 1);

        return formatDate(cal.getTime());
    }
    
    private static String formatDate(java.util.Date time)
    {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT_NOW);

        return sdf.format(time);
    }
    
    private static  gov.hhs.fha.nhinc.common.nhinccommon.AddressType createAddress()
    {
         gov.hhs.fha.nhinc.common.nhinccommon.AddressType address;
         gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

         type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
         address = new  gov.hhs.fha.nhinc.common.nhinccommon.AddressType();
         
         address.setCity("Hometown");
         address.setCountry("USA");
         address.setState("VA");
         address.setZipCode("12345");
         address.setStreetAddress("123 Main Street");

         type.setCode("W");
         type.setCodeSystem("30");
         type.setCodeSystemName("address");
         type.setCodeSystemVersion("1.0");
         type.setDisplayName("White");
         type.setOriginalText("W");

         address.setAddressType(type);
         
         return address;
    }
    
    private static PersonNameType createPersonName(String fName, String mName, String lName)
    {
        PersonNameType result = new PersonNameType();
        gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

        type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();

        type.setCode("G");
        type.setCodeSystem("30");
        type.setCodeSystemName("nameType");
        type.setCodeSystemVersion("1.0");
        type.setDisplayName("G");
        type.setOriginalText("G");

        result.setNameType(type);
        result.setFamilyName(lName);
        result.setGivenName(fName);
        result.setSecondNameOrInitials(mName);
        return result;
    }
    
    private static gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType createHomeCommunity()
    {
        gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType hcType;

        hcType = new gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType();
        hcType.setDescription("Default Community");
        hcType.setHomeCommunityId("1.1");
        hcType.setName("Default Community");

        return hcType;
    }
    
    private static gov.hhs.fha.nhinc.common.nhinccommon.PhoneType createPhoneType()
    {
        gov.hhs.fha.nhinc.common.nhinccommon.PhoneType result;
        gov.hhs.fha.nhinc.common.nhinccommon.CeType type;

        type = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();

        result = new gov.hhs.fha.nhinc.common.nhinccommon.PhoneType();

        result.setAreaCode("703");
        result.setLocalNumber("555");
        result.setExtension("1234");

        type.setCode("W");
        type.setCodeSystem("50");
        type.setCodeSystemName("phoneNumberType");
        type.setCodeSystemVersion("1.0");
        type.setDisplayName("work");
        type.setOriginalText("W");
        result.setPhoneNumberType(type);

        return result;
    }
    
    private static gov.hhs.fha.nhinc.common.nhinccommon.UserType CreateUser()
    {
        gov.hhs.fha.nhinc.common.nhinccommon.UserType result;

        result = new gov.hhs.fha.nhinc.common.nhinccommon.UserType();


        result.setPersonName(createPersonName("Mark", "Q.", "FRANKLIN"));
        result.setOrg(createHomeCommunity());

//        result.setRole("Administrator");
        result.setRoleCoded(createRoleCodeType());
        
        return result;
    }
    
    private static gov.hhs.fha.nhinc.common.nhinccommon.CeType createPurposeCode()
    {
         gov.hhs.fha.nhinc.common.nhinccommon.CeType result;
         result = new  gov.hhs.fha.nhinc.common.nhinccommon.CeType();

         result.setCode("P");
         result.setCodeSystem("45");
         result.setCodeSystemName("purpose");
         result.setCodeSystemVersion("1.0");
         result.setDisplayName("Purpose");
         result.setOriginalText("P");

         return result;
    }
    
    private static CeType createRoleCodeType()
    {
        CeType result = new CeType();

        result.setCode("307969004");
        result.setCodeSystem("2.16.840.1.113883.6.96");
        result.setCodeSystemName("SNOMED_CT");
        result.setCodeSystemVersion("1.0");
        result.setDisplayName("Public Health");
        result.setOriginalText("Public Health");

        return result;
    }
}
