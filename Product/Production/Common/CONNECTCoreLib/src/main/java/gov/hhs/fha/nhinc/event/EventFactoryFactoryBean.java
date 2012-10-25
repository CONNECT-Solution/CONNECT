package gov.hhs.fha.nhinc.event;

import org.springframework.beans.factory.FactoryBean;

import gov.hhs.fha.nhinc.proxy.ComponentProxyFactory;

public class EventFactoryFactoryBean implements FactoryBean<EventFactory> {

    private static final String CONFIG_FILE_NAME = "EventFactoryConfig.xml";
    private static final String BEAN_NAME = "eventfactory";
        
   /**
    *   <bean id="factory"  class="gov.hhs.fha.nhinc.event.EventFactoryFactoryBean" />
    *   
    *   <bean class="doc">
    *     <property name="eventFactory" ref="factory/>
    *   </bean>
    */

    @Override
    public EventFactory getObject() throws Exception {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME, EventFactory.class);
    }

    @Override
    public Class<?> getObjectType() {
        return EventFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
