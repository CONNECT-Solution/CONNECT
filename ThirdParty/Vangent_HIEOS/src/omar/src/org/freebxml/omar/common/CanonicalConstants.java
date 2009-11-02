/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/CanonicalConstants.java,v 1.26 2007/05/25 23:26:38 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

/**
 * This interface should contains all Canonical Constants defined by the
 * regrep spec, including by extension the auto-generated Canonical Schemes.
 *
 * @author Diego Ballve / Digital Artefacts Europe
 */
public interface CanonicalConstants extends CanonicalSchemes {

    //Canonical Slot names
    public final static String CANONICAL_SLOT_LCM_DO_NOT_COMMIT =
        "urn:oasis:names:tc:ebxml-regrep:rs:RegistryRequest:doNotCommit";
    public final static String CANONICAL_SLOT_LCM_OWNER =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:owner";
    public final static String CANONICAL_SLOT_LCM_DONT_VERSION =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:dontVersion";
    public final static String CANONICAL_SLOT_LCM_DONT_VERSION_CONTENT =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:dontVersionContent";
    public static final String CANONICAL_SLOT_CONTENT_LOCATOR =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:contentLocator";
    public static final String CANONICAL_SLOT_LOCATOR =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:locator";
    public final static String CANONICAL_SLOT_REGISTRY_OBJECT_EXPIRATION =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:expiration";
    public final static String CANONICAL_SLOT_REGISTRY_OBJECT_STABILITY =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:stability";
    public static final String CANONICAL_SLOT_QUERY_ID =
        "urn:oasis:names:tc:ebxml-regrep:rs:AdhocQueryRequest:queryId";
    public static final String CANONICAL_SLOT_QUERY_PARAMS =
        "urn:oasis:names:tc:ebxml-regrep:rs:AdhocQueryRequest:queryParams";
    public static final String CANONICAL_SLOT_EVENTS_SRC_REGISTRY =
        "urn:oasis:names:tc:ebxml-regrep:rs:events:sourceRegistry";
    public static final String CANONICAL_SLOT_EVENTS_TARGET_REGISTRY =
        "urn:oasis:names:tc:ebxml-regrep:rs:events:targetRegistry";
    public static final String CANONICAL_SLOT_EVENTS_CORRELATION_ID =
        "urn:oasis:names:tc:ebxml-regrep:rs:events:correlationId";
    public final static String CANONICAL_SLOT_EXTRINSIC_OBJECT_REPOSITORYITEM_URL =
        "urn:oasis:names:tc:ebxml-regrep:rim:ExtrinsicObject:repositoryItemURL:transientslot";
    public final static String CANONICAL_SLOT_NODE_PARENT_CHILD_NODE_COUNT =
        "urn:oasis:names:tc:ebxml-regrep:rim:ClassificationNode:childNodeCount:transientslot";

    //Canonical slots that need to be added to regrep 3.01 spec.
    public final static String CANONICAL_SLOT_IDENTIFIABLE_ID =
        "urn:oasis:names:tc:ebxml-regrep:rim:Identifiable:id";
    public final static String CANONICAL_SLOT_REGISTRY_OBJECT_NAME =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:name";
    public final static String CANONICAL_SLOT_REGISTRY_OBJECT_DESCRIPTION =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:description";
    public final static String CANONICAL_SLOT_REGISTRY_OBJECT_OBJECTTYPE =
        "urn:oasis:names:tc:ebxml-regrep:rim:RegistryObject:objectType";
    public final static String CANONICAL_SLOT_EXTRINSIC_OBJECT_MIMETYPE =
        "urn:oasis:names:tc:ebxml-regrep:rim:ExtrinsicObject:mimeType";
    public final static String CANONICAL_SLOT_SUBSCRIPTION_NOTIFICATION_FORMATTER =
        "urn:oasis:names:tc:ebxml-regrep:rim:Subscription:notificationFormatter";
    public final static String CANONICAL_SLOT_GET_CHILD_OBJECTS =
        "urn:oasis:names:tc:ebxml-regrep:rs:getChildObjects";
    
    //This request option instructs the server to delete objects even if references exist to them
    public final static String CANONICAL_SLOT_DELETE_MODE_FORCE =
        "urn:oasis:names:tc:ebxml-regrep:rs:deleteMode:force";

    //This request option instructs the server to also delete the network of objects reachable by
    //reference from the objects being deleted. This option is not implemented yet.
    public final static String CANONICAL_SLOT_DELETE_MODE_CASCADE =
        "urn:oasis:names:tc:ebxml-regrep:rs:deleteMode:cascade";

    //Impl specific Slot names for JAXR API ebXML Registry mapping
    //These should be added to JAXR 2.0
    public static final String IMPL_SLOT_ASSOCIATION_IS_CONFIRMED_BY_SRC_OWNER =
        "urn:javax:xml:registry:infomodel:Association:isConfirmedBySourceOwner";
    public static final String IMPL_SLOT_ASSOCIATION_IS_CONFIRMED_BY_TARGET_OWNER =
        "urn:javax:xml:registry:infomodel:Association:isConfirmedByTargetOwner";
    public static final String IMPL_SLOT_ASSOCIATION_IS_BEING_CONFIRMED =
        "urn:javax:xml:registry:infomodel:Association:isBeingConfirmed";
    public static final String IMPL_SLOT_ASSOCIATION_IS_BEING_UNCONFIRMED =
        "urn:javax:xml:registry:infomodel:Association:isBeingUnconfirmed";
    public static final String IMPL_SLOT_USER_TYPE =
        "urn:javax:xml:registry:infomodel:User:type";
    public static final String IMPL_SLOT_CREATE_HTTP_SESSION =
        "urn:javax:xml:registry:connection:createHttpSession";
    public static final String IMPL_SLOT_PERSON_URL =
        "urn:javax:xml:registry:infomodel:Person:url";

    //Other Canonical URIs
    public final static String CANONICAL_URI_EFFECTIVE_REQUESTOR =
        "urn:oasis:names:tc:ebxml-regrep:rs:request:effectiveRequestor";
    public final static String CANONICAL_URI_SENDER_CERT =
        "urn:oasis:names:tc:ebxml-regrep:rs:security:SenderCert";
    public static final String CANONICAL_PRINCIPAL_NAME_URI =
        "urn:oasis:names:tc:ebxml-regrep:3.0:rim:User:principalName";

    public static final String CANONICAL_QUERY_GetCallersUser =
        "urn:oasis:names:tc:ebxml-regrep:query:GetCallersUser";
    public static final String CANONICAL_QUERY_ArbitraryQuery =
        "urn:oasis:names:tc:ebxml-regrep:query:ArbitraryQuery";

    //??Add following queries to regrep spec
    public static final String CANONICAL_QUERY_FindAllMyObjects =
        "urn:oasis:names:tc:ebxml-regrep:query:FindAllMyObjects";
    public static final String CANONICAL_QUERY_FindObjectByIdAndType =
        "urn:oasis:names:tc:ebxml-regrep:query:FindObjectByIdAndType";
    public static final String CANONICAL_QUERY_GetAuditTrailForRegistryObject =
        "urn:oasis:names:tc:ebxml-regrep:query:GetAuditTrailForRegistryObject";
    public static final String CANONICAL_QUERY_GetRegistryPackagesByMemberId =
        "urn:oasis:names:tc:ebxml-regrep:query:GetRegistryPackagesByMemberId";
    public static final String CANONICAL_QUERY_GetMembersByRegistryPackageId =
        "urn:oasis:names:tc:ebxml-regrep:query:GetMembersByRegistryPackageId";
    public static final String CANONICAL_QUERY_GetClassificationNodeByPath =
        "urn:oasis:names:tc:ebxml-regrep:query:GetClassificationNodeByPath";    
    
    public static final String CANONICAL_QUERY_GetClassificationSchemesById =
        "urn:oasis:names:tc:ebxml-regrep:query:GetClassificationSchemesById";
    public static final String CANONICAL_QUERY_GetClassificationNodesByParentId =
        "urn:oasis:names:tc:ebxml-regrep:query:GetClassificationNodesByParentId";
    public static final String CANONICAL_QUERY_Export =
        "urn:oasis:names:tc:ebxml-regrep:query:Export";
    public static final String CANONICAL_QUERY_GET_REFERENCE_SOURCES =
        "urn:oasis:names:tc:ebxml-regrep:query:GetReferenceSources";

    //Following may need id to be fixed to be BasicQuery before including in spec.
    public static final String CANONICAL_QUERY_BasicQuery =
        "urn:freebxml:registry:query:BusinessQuery";
    //Following may need id to be fixed to be BasicQuery before including in spec.
    public static final String CANONICAL_QUERY_BasicQueryCaseSensitive =
        "urn:freebxml:registry:query:BusinessQueryCaseSensitive";


    public static final String SOAP_FAULT_PREFIX =
        "urn:oasis:names:tc:ebxml-regrep:rs:exception";

    public final static String CANONICAL_ROOT_FOLDER_ID = "urn:oasis:names:tc:ebxml-regrep:RegistryPackage:registry";
    public final static String CANONICAL_ROOT_FOLDER_LOCATOR = "/registry";
    public final static String CANONICAL_USERDATA_FOLDER_ID = "urn:oasis:names:tc:ebxml-regrep:RegistryPackage:userData";
    public final static String CANONICAL_USERDATA_FOLDER_LOCATOR = "/registry/userData";
    public static final String CANONICAL_DEFAULT_XML_CATALOGING_SERVICE_ID = "urn:oasis:names:tc:ebxml-regrep:Service:CanonicalXMLCatalogingService";
    public static final String CANONICAL_DEFAULT_XML_CATALOGING_SERVICE_NAME = "CanonicalXMLCatalogingService";

    public final static String CANONICAL_SEARCH_DEPTH_PARAMETER = "$urn:oasis:names:tc:ebxml-regrep:rs:depthParameter";
    
    //Canonocal constants for registry Actions

    /** The create action from V3 spec.*/
    public static final String ACTION_CREATE = "create";

    /** The read action from V3 spec.*/
    public static final String ACTION_READ = "read";

    /** The update action from V3 spec.*/
    public static final String ACTION_UPDATE = "update";

    /** The delete action from V3 spec.*/
    public static final String ACTION_DELETE = "delete";

    /** The approve action from V3 spec.*/
    public static final String ACTION_APPROVE = "approve";

    /** The reference action from V3 spec.*/
    public static final String ACTION_REFERENCE = "reference";

    /** The deprecate action from V3 spec.*/
    public static final String ACTION_DEPRECATE = "deprecate";

    /** The undeprecate action from V3 spec.*/
    public static final String ACTION_UNDEPRECATE = "undeprecate";

    /** The relocate action is mising from V3 spec??*/
    public static final String ACTION_RELOCATE = "relocate";

    /** The setStatus action is mising from V3 spec??*/
    public static final String ACTION_SET_STATUS = "setStatus";

    /** The extension-request action is mising from V3 spec??*/
    public static final String ACTION_EXTENSION_REQUEST = "extension-request";
    
}
