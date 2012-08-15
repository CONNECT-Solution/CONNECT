package gov.hhs.fha.nhinc.hiem._20.entity.notify;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.xml.ws.WebServiceContext;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.processor.entity.EntityNotifyProcessor;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.oasis_open.docs.wsn.b_2.Notify;

public class EntityNotifyServiceImplTest {

	private Mockery context;

    public EntityNotifyServiceImplTest() {
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
	public void testGetPerformanceManager() {
		EntityNotifyServiceImpl instance = new EntityNotifyServiceImpl();
		PerformanceManager manager = instance.getPerformanceManager();
		assertNotNull(manager);
	}

	@Test
	public void testGetSoapUtil() {
		EntityNotifyServiceImpl instance = new EntityNotifyServiceImpl();
		SoapUtil util = instance.getSoapUtil();
		assertNotNull(util);
	}

	@Test
	public void testGetEntityNotifyProcessor() {
		EntityNotifyServiceImpl instance = new EntityNotifyServiceImpl();
		EntityNotifyProcessor processor = instance.getEntityNotifyProcessor();
		assertNotNull(processor);
	}

	@Test
	public void testNotifyNotifyRequestTypeWebServiceContext() {
		
		
		final PerformanceManager mockManager = context.mock(PerformanceManager.class);
		final SoapUtil mockUtil = context.mock(SoapUtil.class);
		final EntityNotifyProcessor mockProcessor = context.mock(EntityNotifyProcessor.class);
		
		EntityNotifyServiceImpl instance = new EntityNotifyServiceImpl(){
			@Override
			protected PerformanceManager getPerformanceManager(){
				return mockManager;
			}
			
			@Override
			protected SoapUtil getSoapUtil(){
		    	return mockUtil;
		    }
		    
		    @Override
		    protected EntityNotifyProcessor getEntityNotifyProcessor(){
		    	return mockProcessor;
		    }			
		};
		context.checking(new Expectations() {

            {
                allowing(mockManager).logPerformanceStart(with(any(String.class)),with(any(String.class)), 
                		with(any(String.class)), with(any(String.class)));
                allowing(mockUtil).extractSoapMessage(with(any(WebServiceContext.class)), 
                		with(any(String.class)));
                allowing(mockProcessor).processNotify(with(any(Notify.class)), 
                		with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

		WebServiceContext ws_context = context.mock(WebServiceContext.class);
        instance.notify(new NotifyRequestType(), ws_context);
        context.assertIsSatisfied();
	}

	@Test
	public void testNotifyNotifyWebServiceContext() {
		final PerformanceManager mockManager = context.mock(PerformanceManager.class);
		final SoapUtil mockUtil = context.mock(SoapUtil.class);
		final EntityNotifyProcessor mockProcessor = context.mock(EntityNotifyProcessor.class);
		
		EntityNotifyServiceImpl instance = new EntityNotifyServiceImpl(){
			@Override
			protected PerformanceManager getPerformanceManager(){
				return mockManager;
			}
			
			@Override
			protected SoapUtil getSoapUtil(){
		    	return mockUtil;
		    }
		    
		    @Override
		    protected EntityNotifyProcessor getEntityNotifyProcessor(){
		    	return mockProcessor;
		    }			
		};
		context.checking(new Expectations() {

            {
                allowing(mockManager).logPerformanceStart(with(any(String.class)),with(any(String.class)), 
                		with(any(String.class)), with(any(String.class)));
                allowing(mockUtil).extractSoapMessage(with(any(WebServiceContext.class)), 
                		with(any(String.class)));
                allowing(mockProcessor).processNotify(with(any(Notify.class)), 
                		with(any(AssertionType.class)), with(any(String.class)));
                will(returnValue(null));
            }
        });

		WebServiceContext ws_context = context.mock(WebServiceContext.class);
        instance.notify(new NotifyRequestType(), ws_context);
        context.assertIsSatisfied();
	}

}
