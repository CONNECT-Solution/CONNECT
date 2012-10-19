package gov.hhs.fha.nhinc.event;

import gov.hhs.fha.nhinc.proxy.ComponentProxyFactory;

public class EventFactoryContext {

    private static final String CONFIG_FILE_NAME = "EventFactoryConfig.xml";
    private static final String BEAN_NAME = "eventfactory";
        
    /**
     * Getter method for the factory declared by the spring proxy bean.
     * 
     * @return an instance of the event factory
     */
    public static EventFactory getInstance() {
        return new ComponentProxyFactory(CONFIG_FILE_NAME).getInstance(BEAN_NAME, EventFactory.class);
    }
}
