package gov.hhs.fha.nhinc.policyengine.adapter.pip.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AddressType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.PhoneType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
public class AdapterPIPProxyTest
{

    @Test
    public void testAdapterPIPProxy()
    {
        System.out.println("Begin testAdapterPIPProxy");
        try
        {
            AdapterPIPProxyObjectFactory oFactory = new AdapterPIPProxyObjectFactory();
            AdapterPIPProxy oAdapterPIPProxy = oFactory.getAdapterPIPProxy();
            assertNotNull(oAdapterPIPProxy);
            assertTrue("Adapter PIP was not the default type.", (oAdapterPIPProxy instanceof AdapterPIPProxyWebServiceUnsecuredImpl));
            RetrievePtConsentByPtIdRequestType request = new RetrievePtConsentByPtIdRequestType();
            request.setPatientId("ADPTPIPTST98769876Z");
            request.setAssigningAuthority("1.1");
            AssertionType assertion = createAssertion();
            request.setAssertion(assertion);

            RetrievePtConsentByPtIdResponseType response = oAdapterPIPProxy.retrievePtConsentByPtId(request, assertion);
            assertNotNull("RetrievePtConsentByPtIdResponseType was null", response);

        }
        catch (Throwable t)
        {
            System.out.println("Exception in testAdapterPIPProxy: " + t.getMessage());
            t.printStackTrace();
            fail(t.getMessage());
        }

        System.out.println("End testAdapterPIPProxy");
    }

    private AssertionType createAssertion()
    {
        AssertionType assertion = new AssertionType();

        AddressType address = new AddressType();
        assertion.setAddress(address);
        CeType addressType = new CeType();
        address.setAddressType(addressType);
        addressType.setCode("Code");
        addressType.setCodeSystem("CodeSystem");
        addressType.setCodeSystemName("CodeSystemName");
        addressType.setCodeSystemVersion("CodeSystemVersion");
        addressType.setDisplayName("DisplayName");
        addressType.setOriginalText("OriginalText");

        assertion.setDateOfBirth("06/04/1959 05:21:00");
        assertion.setExplanationNonClaimantSignature("Electronic");
        assertion.setHaveSecondWitnessSignature(false);
        assertion.setHaveSignature(false);
        assertion.setHaveWitnessSignature(false);

        HomeCommunityType homeCommunity = new HomeCommunityType();
        assertion.setHomeCommunity(homeCommunity);
        homeCommunity.setDescription("InternalTest2");
        homeCommunity.setHomeCommunityId("2.2");
        homeCommunity.setName("InternalTest2");

        PersonNameType personName = new PersonNameType();
        assertion.setPersonName(personName);
        personName.setFamilyName("Smith");
        personName.setGivenName("Sandy");
        CeType personNameType = new CeType();
        address.setAddressType(addressType);
        personNameType.setCode("Code");
        personNameType.setCodeSystem("CodeSystem");
        personNameType.setCodeSystemName("CodeSystemName");
        personNameType.setCodeSystemVersion("CodeSystemVersion");
        personNameType.setDisplayName("DisplayName");
        personNameType.setOriginalText("OriginalText");
        personName.setNameType(personNameType);
        personName.setSecondNameOrInitials("S");
        personName.setFullName("Sandy S. Smith");

        PhoneType phoneType = new PhoneType();
        assertion.setPhoneNumber(phoneType);
        phoneType.setAreaCode("321");
        phoneType.setCountryCode("1");
        phoneType.setExtension("5436");
        phoneType.setLocalNumber("253-6849");

        CeType purposeCoded = new CeType();
        UserType user = new UserType();
        PersonNameType userPerson = new PersonNameType();
        CeType userRole = new CeType();
        HomeCommunityType userHc = new HomeCommunityType();
        user.setPersonName(userPerson);
        user.setOrg(userHc);
        user.setRoleCoded(userRole);
        assertion.setUserInfo(user);
        assertion.setPurposeOfDisclosureCoded(purposeCoded);
        userPerson.setGivenName("UserGivenName");
        userPerson.setFamilyName("UserFamilyName");
        userPerson.setSecondNameOrInitials("J.");
        userHc.setName("UserHCName");
        user.setUserName("UserName");
        userRole.setCode("UserRoleCode");
        userRole.setCodeSystem("UserRoleSystem");
        userRole.setCodeSystemName("UserRoleSystemName");
        userRole.setDisplayName("UserRoleDisplayName");

        purposeCoded.setCode("PurposeCodedCode");
        purposeCoded.setCodeSystem("PurposeCodedSystem");
        purposeCoded.setCodeSystemName("PurposeCodedSystemName");
        purposeCoded.setDisplayName("PurposeCodedDisplayName");

        return assertion;
    }
}