/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

/**
 *
 * @author dunnek
 */
public class NhincAdminDistUnsecuredWebserviceImplTest {

    private Mockery context;
    public NhincAdminDistUnsecuredWebserviceImplTest() {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }


    @Test
    public void testSendAlertMessage() {
        System.out.println("sendAlertMessage");
        final Log mockLogger = context.mock(Log.class);
        final AdminDistributionHelper mockHelper = context.mock(AdminDistributionHelper.class);
        final NhincAdminDistService mockService = context.mock(NhincAdminDistService.class);
        
        EDXLDistribution body = null;
        AssertionType assertion = null;
        NhinTargetSystemType target = null;
        Exception unsupported = null;

        PassthruAdminDistributionProxyWebServiceUnsecuredImpl instance = new PassthruAdminDistributionProxyWebServiceUnsecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected AdminDistributionHelper getHelper() {
                return mockHelper;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockService).getNhincAdminDistPortType();
                allowing(mockHelper).getLocalCommunityId();
                allowing(mockHelper).getUrl(with(any(String.class)), with(any(String.class)), with(any(NhincConstants.GATEWAY_API_LEVEL.class)));

                will(returnValue(null));
            }
        });

        try
        {
            instance.sendAlertMessage(body, assertion, target, NhincConstants.GATEWAY_API_LEVEL.LEVEL_g0);
        }
        catch(Exception ex)
        {
            unsupported = ex;
        }

        context.assertIsSatisfied();

    }

}