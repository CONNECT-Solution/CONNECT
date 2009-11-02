/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/spi/RegistryPlugin.java,v 1.1 2005/11/28 20:17:33 farrukh_najmi Exp $
 * ====================================================================
 */
package org.freebxml.omar.common.spi;

import java.util.Map;
import java.util.Set;
import javax.xml.registry.RegistryException;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;

/**
 * The interface implemented by all registry plugin classes.
 *
 * Current examples of plugins are: RequestInterceptor, NotificationListener, Validator, Cataloger
 * 
 * @author Farrukh Najmi
 * @author Diego Ballve
 *
 */
public interface RegistryPlugin {
    
    /** 
     * Gets the id of the plugin. 
     * The id MUST follow the rules for RegistryObject id in ebXML Registry specifications.
     *
     */
    String getId();
    
    /** 
     * Gets the name of the plugin. 
     *
     * @return the InternationalStringType representing the name of the plugin.
     */
    InternationalStringType getName();
    
    /** Gets the description of the plugin. 
     *
     * @return the InternationalStringType representing the description of the plugin.
     */
    InternationalStringType getDescription();
    
    /** 
     * Gets the Subject roles that the plugin applies to. 
     *
     * @return the Set of Strings where each string references the id of a Concept 
     * within the canonical SubjectRole ClassificationScheme as defined by ebRIM Access Control Information Model chapter.
     * 
     */
    Set getRoles();
    
    /** 
     * Gets the registry actions that the plugin applies to. 
     *
     * @return the Set of Strings where each string represents a registry action as defined by ebRIM Access Control Information Model chapter.
     * 
     */
    Set getActions();
    
}
