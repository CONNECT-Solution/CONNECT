/**
 * 
 */
package gov.hhs.fha.nhinc.patientdiscovery.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean;

import javax.servlet.ServletContext;

/**
 * @author achidambaram
 * 
 */
public abstract class AbstractPDDeferredResponseWebServicesMXBean extends AbstractWebServicesMXBean {
    /** The Constant NHIN_PD_BEAN_NAME. */
    private static final String NHIN_PD_BEAN_NAME = "nhinPDResp";

    /** The Constant ENTITY_UNSECURED_PD_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_PD_BEAN_NAME = "entityPDRespUnsecured";

    /** The Constant ENTITY_SECURED_PD_BEAN_NAME. */
    private static final String ENTITY_SECURED_PD_BEAN_NAME = "entityPDRespSecured";

    /** The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.StandardOutboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.PassthroughInboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.StandardInboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.PassthroughOutboundPatientDiscoveryDeferredResponse";

    /** The Constant Standard_OutboundOrch_PDDeferredResponse_BEAN_NAME. */
    private static final String StdOutbound_PD_Bean_Name = "stdPDRespOutbound";

    /** The Constant Passthrough_OutboundOrch_PDDeferredResponse_BEAN_NAME. */
    private static final String PtOutbound_PD_Bean_Name = "ptPDRespOutbound";

    /** The Constant Standard_InboundOrch_PDDeferredResponse_BEAN_NAME. */
    private static final String StdInbound_PD_Bean_Name = "stdPDRespInbound";

    /** The Constant Passthrough_InboundOrch_PDDeferredResponse_BEAN_NAME. */
    private static final String PtInbound_PD_Bean_Name = "ptPDRespInbound";

    /**
     * Constructor for AbstractPDWebServicesMXBean.
     * 
     * @param sc the sc
     */
    public AbstractPDDeferredResponseWebServicesMXBean(ServletContext sc) {
        super(sc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_PD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardOutboundBeanName()
     */
    @Override
    protected String getStandardOutboundBeanName() {
        return StdOutbound_PD_Bean_Name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughOutboundBeanName()
     */
    @Override
    protected String getPassthroughOutboundBeanName() {
        return PtOutbound_PD_Bean_Name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardInboundBeanName()
     */
    @Override
    protected String getStandardInboundBeanName() {
        return StdInbound_PD_Bean_Name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughInboundBeanName()
     */
    @Override
    protected String getPassthroughInboundBeanName() {
        return PtInbound_PD_Bean_Name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_PD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_PD_BEAN_NAME;
    }

    /**
     * Configure outbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure outbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure Inbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure outbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     * 
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public abstract boolean isInboundPassthru();

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public abstract boolean isOutboundPassthru();

}
