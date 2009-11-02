/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/common/BindingUtility.java,v 1.124 2007/04/19 16:46:48 psterk Exp $
 * ====================================================================
 */
package org.freebxml.omar.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.oasis.ebxml.registry.bindings.lcm.ApproveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.DeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RelocateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.RemoveObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SetStatusOnObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.SubmitObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UndeprecateObjectsRequest;
import org.oasis.ebxml.registry.bindings.lcm.UpdateObjectsRequest;
import org.oasis.ebxml.registry.bindings.query.AdhocQueryRequest;
import org.oasis.ebxml.registry.bindings.query.ResponseOption;
import org.oasis.ebxml.registry.bindings.query.ReturnType;
import org.oasis.ebxml.registry.bindings.rim.AdhocQuery;
import org.oasis.ebxml.registry.bindings.rim.Association;
import org.oasis.ebxml.registry.bindings.rim.Classification;
import org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType;
import org.oasis.ebxml.registry.bindings.rim.ClassificationType;
import org.oasis.ebxml.registry.bindings.rim.Description;
import org.oasis.ebxml.registry.bindings.rim.ExternalIdentifierType;
import org.oasis.ebxml.registry.bindings.rim.IdentifiableType;
import org.oasis.ebxml.registry.bindings.rim.InternationalStringType;
import org.oasis.ebxml.registry.bindings.rim.LocalizedString;
import org.oasis.ebxml.registry.bindings.rim.LocalizedStringType;
import org.oasis.ebxml.registry.bindings.rim.Name;
import org.oasis.ebxml.registry.bindings.rim.ObjectRef;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefList;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefListType;
import org.oasis.ebxml.registry.bindings.rim.ObjectRefType;
import org.oasis.ebxml.registry.bindings.rim.QueryExpressionType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectType;
import org.oasis.ebxml.registry.bindings.rim.RegistryPackageType;
import org.oasis.ebxml.registry.bindings.rim.ServiceBindingType;
import org.oasis.ebxml.registry.bindings.rim.ServiceType;
import org.oasis.ebxml.registry.bindings.rim.Slot;
import org.oasis.ebxml.registry.bindings.rim.SlotListType;
import org.oasis.ebxml.registry.bindings.rim.SlotType1;
import org.oasis.ebxml.registry.bindings.rim.SpecificationLinkType;
import org.oasis.ebxml.registry.bindings.rim.Value;
import org.oasis.ebxml.registry.bindings.rim.ValueList;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequest;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponseType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for using JAXB bindings.
 *
 * @author Farrukh S. Najmi
 * @version   1.2, 05/02/00
 */
public class BindingUtility implements CanonicalConstants {
    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();
    private Log log = LogFactory.getLog(BindingUtility.class);

    //Implementation-specific constants
    public static final String ASSOCIATION_TYPE_ID_ProviderOf = "urn:oasis:names:tc:ebxml-regrep:AssociationType:ProviderOf";
    public static final String ASSOCIATION_TYPE_LID_ProviderOf = "urn:oasis:names:tc:ebxml-regrep:AssociationType:ProviderOf";
    public static final String ASSOCIATION_TYPE_CODE_ProviderOf = "ProviderOf";
    public static final String DEMO_DB_LID_PREFIX = "urn:freebxml:registry:demoDB:";
    public static final String FEDERATION_TEST_DATA_LID_PREFIX = DEMO_DB_LID_PREFIX + "federation:";
    public static final String CPP_CLASSIFICATION_NODE_ID = "urn:freebxml:registry:sample:profile:cpp:objectType:cppa:CPP";
    public static final String FREEBXML_REGISTRY_ORGANIZATION_ID = "urn:freebxml:registry:Organization:freebXMLRegistry";
    public static final String FREEBXML_REGISTRY_PROTOCOL_SIGNCERT = "urn:freebxml:registry:protocol:signCert";
    public static final String FREEBXML_REGISTRY_PROTOCOL_SETSTATUS = "urn:freebxml:registry:protocol:setStatus";
    public static final String FREEBXML_REGISTRY_PROTOCOL_NAME = "urn:freebxml:registry:protocol:name";
    public static final String FREEBXML_REGISTRY_FILTER_QUERY_COMPRESSCONTENT = "urn:freebxml:registry:query:filter:CompressContent";
    public static final String FREEBXML_REGISTRY_FILTER_QUERY_COMPRESSCONTENT_FILENAME = "urn:freebxml:registry:query:filter:CompressContent:filename";
    
    //Slot names defined for singleton Registry instance
    //The interceptors slot name
    public static final String FREEBXML_REGISTRY_REGISTRY_INTERCEPTORS = "urn:freebxml:registry:Registry:interceptors";

    //Constants shared between CertificateAuthority on server side and CertificateUtil on client side
    public static final String FREEBXML_REGISTRY_USERCERT_ALIAS_REQ = "usercertreq";
    public static final String FREEBXML_REGISTRY_USERCERT_ALIAS_RESP = "usercertresp";
    public static final String FREEBXML_REGISTRY_CACERT_ALIAS = "cacert";
    public static final String FREEBXML_REGISTRY_KS_PASS_REQ = "kspassreq";
    public static final String FREEBXML_REGISTRY_KS_PASS_RESP = "kspassresp";

    public static final String FREEBXML_REGISTRY_DEFAULT_NOTIFICATION_FORMATTER = "urn:freebxml:registry:xslt:notificationToHTML.xsl";

    // client to server communication of SOAP capabilities (sent in each
    // message): names one or more SOAP Header element(s), each containing
    // a capability URI
    public static final String SOAP_CAPABILITY_HEADER_LocalName =
    "capabilities";
    public static final String SOAP_CAPABILITY_HEADER_Namespace =
    "urn:freebxml:registry:soap";

    // client supports "modern" (SOAP 1.1 compliant) fault code mapping
    public static final String SOAP_CAPABILITY_ModernFaultCodes =
    "urn:freebxml:registry:soap:modernFaultCodes";

    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /* # private BindingUtility _utility; */
    private static BindingUtility instance = null;
    public org.oasis.ebxml.registry.bindings.rim.ObjectFactory rimFac;
    public org.oasis.ebxml.registry.bindings.rs.ObjectFactory rsFac;
    public org.oasis.ebxml.registry.bindings.lcm.ObjectFactory lcmFac;
    public org.oasis.ebxml.registry.bindings.query.ObjectFactory queryFac;
    public org.oasis.ebxml.registry.bindings.cms.ObjectFactory cmsFac;
    //public org.oasis.saml.bindings._20.protocol.ObjectFactory samlProtocolFac;
    //public org.oasis.saml.bindings._20.assertion.ObjectFactory samlAssertionFac;
    JAXBContext jaxbContext = null;

    /**
     * Class Constructor. Protected and only used by getInstance()
     *
     */
    protected BindingUtility() {
        try {
            getJAXBContext();
            rimFac = new org.oasis.ebxml.registry.bindings.rim.ObjectFactory();
            rsFac = new org.oasis.ebxml.registry.bindings.rs.ObjectFactory();
            lcmFac = new org.oasis.ebxml.registry.bindings.lcm.ObjectFactory();
            queryFac = new org.oasis.ebxml.registry.bindings.query.ObjectFactory();
            cmsFac = new org.oasis.ebxml.registry.bindings.cms.ObjectFactory();
            //samlProtocolFac = new org.oasis.saml.bindings._20.protocol.ObjectFactory();
            //samlAssertionFac = new org.oasis.saml.bindings._20.assertion.ObjectFactory();
        } catch (JAXBException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public JAXBContext getJAXBContext() throws JAXBException {
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(
                    "org.oasis.ebxml.registry.bindings.rim:org.oasis.ebxml.registry.bindings.rs:org.oasis.ebxml.registry.bindings.lcm:org.oasis.ebxml.registry.bindings.query:org.oasis.ebxml.registry.bindings.cms",
                    this.getClass().getClassLoader());
            ;
        }

        return jaxbContext;
    }

    /*
     * Gets the id for the objectType for specified RIM class.
     *
     * @retun Return the canonical id for the ClassificationNode representing the
     * objectType for specified RIM class.
     */
    public String getObjectTypeId(String rimClassName) throws JAXRException {
        String objectTypeId = null;

        try {
            Class clazz = this.getClass();
            Field field = clazz.getField("CANONICAL_OBJECT_TYPE_ID_" + rimClassName);
            Object obj = field.get(this);
            objectTypeId = (String)obj;
        }
        catch (NoSuchFieldException e) {
            throw new JAXRException(e);
        }
        catch (SecurityException e) {
            throw new JAXRException(e);
        }
        catch (IllegalAccessException e) {
            throw new JAXRException(e);
        }

        return objectTypeId;
    }

    /**
     * Gets the objectType for the specified RegistryObject.
     */
    public String getObjectType(RegistryObjectType ro) throws JAXRException {
        String objectType = ro.getObjectType();

        if (objectType == null) {
            String className = ro.getClass().getName();

            //Make sure className is not package qualified and does not end in Impl
            int index = className.lastIndexOf('.');
            if (index >=0 ) {
                className = className.substring(index+1,className.length()-4);
            }


            objectType = getObjectTypeId(className);
        }

        return objectType;
    }


    /**
     * Get Map of id key and RegistryObject value.
     * Does not get composed objects.
     */
    public Map getRegistryObjectMap(Collection objs) throws JAXRException {
        Map map = new HashMap();

        if (objs != null) {
            Iterator iter = objs.iterator();

            while (iter.hasNext()) {
                Object obj = iter.next();

                if (obj instanceof RegistryObjectType) {
                    RegistryObjectType ro = (RegistryObjectType)obj;
                    map.put(ro.getId(), ro);
                }
            }
        }

        return map;
    }

    /**
     * Get List of RegistryObject after filtering out ObjectRef from RegistryObjectList.
     * Does not get composed objects.
     */
    public List getRegistryObjectList(RegistryObjectListType objs) throws JAXRException {
        List al = new ArrayList();

        if (objs != null) {
            List identifiables = objs.getIdentifiable();
            Iterator iter = identifiables.iterator();

            while (iter.hasNext()) {
                Object obj = iter.next();

                IdentifiableType identifiable = (IdentifiableType) obj;

                //Identifiable obj1 = (Identifiable)obj;
                //IdentifiableType identifiable = obj1.getValueObject();
                if (!(identifiable instanceof ObjectRefType)) {
                    al.add(identifiable);
                }
            }
        }

        return al;
    }

    /**
     * Get separate List of RegistryObjects and ObjectRefs.
     * Does not get composed objects.
     */
    public void getObjectRefsAndRegistryObjects(RegistryObjectListType objs, Map objectsMap, Map orefMap) throws JAXRException {

        if (objs != null) {
            List identifiables = objs.getIdentifiable();
            Iterator iter = identifiables.iterator();

            while (iter.hasNext()) {
                Object obj = iter.next();

                if (obj instanceof IdentifiableType) {
                    IdentifiableType identifiable = (IdentifiableType) obj;

                    if (identifiable instanceof RegistryObjectType) {
                        objectsMap.put(identifiable.getId(), identifiable);
                    }
                    else if (identifiable instanceof ObjectRefType) {
                        orefMap.put(identifiable.getId(), identifiable);
                    }
                } else {
                    throw new JAXRException("Excpecting an IdentifiableType, got:" + obj);
                }
            }
        }

    }

    /**
     * Get the id from an object that could either an ObjectRef or RegistryObject
     */
    public String getObjectId(Object obj) throws JAXRException {
        String id = null;

        if (obj != null) {
            if (obj instanceof ObjectRefType) {
                id = ((ObjectRefType) obj).getId();
            } else if (obj instanceof RegistryObjectType) {
                id = ((RegistryObjectType) obj).getId();
            } else if (obj instanceof String) {
                id = (String) obj;
            } else {
                throw new JAXRException(resourceBundle.getString("message.unexpectedObjectType",
                            new String[] {obj.getClass().toString(), "java.lang.String, org.oasis.ebxml.registry.bindings.rim.ObjectRefType, org.oasis.ebxml.registry.bindings.rim.RegistryObjectType"}));
            }
        }

        return id;
    }

    /**
     * Set the id for an object that could either an ObjectRef or RegistryObject
     */
    public void setObjectId(Object obj, String id) throws JAXRException {
        if (obj != null) {
            if (obj instanceof ObjectRefType) {
                ((ObjectRefType) obj).setId(id);
            } else if (obj instanceof RegistryObjectType) {
                ((RegistryObjectType) obj).setId(id);
            } else {
                throw new JAXRException(resourceBundle.getString("message.unexpectedObjectType",
                            new String[] {obj.getClass().toString(), "org.oasis.ebxml.registry.bindings.rim.ObjectRefType, org.oasis.ebxml.registry.bindings.rim.RegistryObjectType"}));
            }
        }
    }

    /**
     * Gets trhe root element for a registry request
     * @return the root element as a String
     */
    public String getRequestRootElement(InputStream request)
        throws JAXRException {
        String rootElementName = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(request);

            Element root = doc.getDocumentElement();
            rootElementName = root.getLocalName();
        } catch (IOException e) {
            throw new JAXRException(e);
        } catch (ParserConfigurationException e) {
            throw new JAXRException(e);
        } catch (SAXException e) {
            throw new JAXRException(e);
        }

        return rootElementName;
    }

    /**
     * Gets the binding object representing the request from specufied XML file.
     */
    public Object getRequestObject(File file) throws JAXRException {
        Object req = null;

        try {
            Unmarshaller unmarshaller = getUnmarshaller();
            req = unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new JAXRException(resourceBundle.getString("message.unmarshallRequest"), e);
        }

        return req;
    }

    public Object getRequestObject(String rootElement, String message)
        throws JAXRException {
        //TODO: Consider removing String rootElement. Currently not used.
        Object req = null;

        try {
            StreamSource ss = new StreamSource(new StringReader(message));
            Unmarshaller unmarshaller = getUnmarshaller();
            req = unmarshaller.unmarshal(ss);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new JAXRException(resourceBundle.getString("message.unmarshallRequest"), e);
        }

        return req;
    }

    public Unmarshaller getUnmarshaller() throws JAXBException {
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        //unmarshaller.setValidating(true);
        unmarshaller.setEventHandler(new ValidationEventHandler() {
                public boolean handleEvent(ValidationEvent event) {
                    boolean keepOn = false;

                    return keepOn;
                }
            });

        return unmarshaller;
    }

    /**
     * Gets a String representation of a list ids from a Collection of RegistryObjects.
     */
    public StringBuffer getIdListFromRegistryObjects(List objs) {
        StringBuffer idList = new StringBuffer();

        Iterator iter = objs.iterator();

        while (iter.hasNext()) {
            RegistryObjectType obj = (RegistryObjectType) iter.next();
            String id = obj.getId();
            idList.append("'" + id + "'");

            if (iter.hasNext()) {
                idList.append(", ");
            }
        }

        return idList;
    }

    /**
     * Get List of id of RegistryObjects
     */
    public List getIdsFromRegistryObjects(Collection objs) {
        List ids = new ArrayList();

        if (objs.size() > 0) {
            Iterator iter = objs.iterator();

            while (iter.hasNext()) {
                IdentifiableType ro = (IdentifiableType) iter.next();
                ids.add(ro.getId());
            }
        }

        return ids;
    }

    /**
     * Get List of ObjectRefs for specified RegistryObjects
     */
    public List getObjectRefsFromRegistryObjects(List objs) throws JAXRException {
        List refs = new ArrayList();

        try {
            if (objs.size() > 0) {
                Iterator iter = objs.iterator();

                while (iter.hasNext()) {
                    IdentifiableType ro = (IdentifiableType) iter.next();
                    ObjectRef ref = rimFac.createObjectRef();
                    ref.setId(ro.getId());
                    refs.add(ref);
                }
            }
        }
        catch (JAXBException e) {
            throw new JAXRException(e);
        }

        return refs;
    }

        /**
     * Get List of ObjectRefs for specified RegistryObjects
     */
    public List getObjectRefsFromRegistryObjectIds(List ids) throws JAXRException {
        List refs = new ArrayList();

        try {
            if (ids.size() > 0) {
                Iterator iter = ids.iterator();

                while (iter.hasNext()) {
                    ObjectRef ref = rimFac.createObjectRef();
                    ref.setId((String)iter.next());
                    refs.add(ref);
                }
            }
        }
        catch (JAXBException e) {
            throw new JAXRException(e);
        }

        return refs;
    }


    /**
     * Filter out those RegistryObjects whose id are in the List ids
     */
    public List getRegistryObjectsFromIds(List objs, List ids) {
        List ros = new ArrayList();

        if ((ids.size() > 0) && (objs.size() > 0)) {
            Iterator iter = objs.iterator();

            while (iter.hasNext()) {
                RegistryObjectType ro = (RegistryObjectType) iter.next();

                if (ids.contains(ro.getId())) {
                    ros.add(ro);
                }
            }
        }

        return ros;
    }

    /**
     * Gets a String representation of a list of ids from an ObjectRefList.
     */
    public StringBuffer getIdListFromObjectRefList(ObjectRefList refList) {
        StringBuffer idList = new StringBuffer();

        List refs = refList.getObjectRef();
        Iterator iter = refs.iterator();
        int cnt = refs.size();
        int i = 0;

        while (iter.hasNext()) {
            ObjectRefType ref = (ObjectRefType) iter.next();
            String id = ref.getId();
            idList.append("'" + id + "'");

            if (i < (cnt - 1)) {
                idList.append(", ");
            }

            i++;
        }

        return idList;
    }

    /**
     * Get comma delimited list of quoted id from List of ids.
     */
    public StringBuffer getIdListFromIds(List ids) {
        StringBuffer idList = new StringBuffer();
        Iterator iter = ids.iterator();

        while (iter.hasNext()) {
            String id = (String) iter.next();
            idList.append("'" + id + "'");

            if (iter.hasNext()) {
                idList.append(",");
            }
        }

        return idList;
    }

    /**
     * Get List of id of ObjectRef under ObjectRefList.
     */
    public List getIdsFromObjectRefList(ObjectRefListType refList) {
        List ids = new ArrayList();

        if (refList != null) {
            List refs = refList.getObjectRef();
            Iterator iter = refs.iterator();

            while (iter.hasNext()) {
                ObjectRefType ref = (ObjectRefType) iter.next();
                ids.add(ref.getId());
            }
        }

        return ids;
    }

    /**
     * Get List of ObjectRefs of ObjectRef under ObjectRefList.
     */
    public List getObjectRefsFromObjectRefList(ObjectRefListType refList) {
        List orefs;

        if (refList == null) {
            orefs = new ArrayList();
        } else {
            orefs = refList.getObjectRef();
        }

        return orefs;
    }

    /**
     * Get the first-level RegistryObject by id from SubmitObjectsRequest.
     */
    public Object getObjectFromRequest(SubmitObjectsRequest registryRequest,
        String id) throws JAXRException {
        Object result = null;
        RegistryObjectListType objList = registryRequest.getRegistryObjectList();
        List objs = getRegistryObjectList(objList);

        Iterator iter = objs.iterator();

        while (iter.hasNext()) {
            Object obj = iter.next();
            String objId = getObjectId(obj);

            if (id.equalsIgnoreCase(objId)) {
                result = obj;

                break;
            }
        }

        return result;
    }

    /**
     * Get List of Id of first-level RegistryObject or ObjectRef in a request. For
     * those kinds of request having RegistryObject and ObjectRef (e.g. SubmitObjectsRequest),
     * only the id of RegistryObject elements are returned.
     */
    public List getIdsFromRequest(Object registryRequest)
        throws JAXRException
    {
        List ids = new ArrayList();

        if (registryRequest instanceof AdhocQueryRequest) {
        }
        else if (registryRequest instanceof ApproveObjectsRequest) {
            ObjectRefListType refList = ((ApproveObjectsRequest)registryRequest).getObjectRefList();
            ids.addAll(getIdsFromObjectRefList(refList));
        }
        else if (registryRequest instanceof SetStatusOnObjectsRequest) {
            ObjectRefListType refList = ((SetStatusOnObjectsRequest) registryRequest).getObjectRefList();
            ids.addAll(getIdsFromObjectRefList(refList));
        }
        else if (registryRequest instanceof DeprecateObjectsRequest) {
            ObjectRefListType refList = ((DeprecateObjectsRequest) registryRequest).getObjectRefList();
            ids.addAll(getIdsFromObjectRefList(refList));
        }
        else if (registryRequest instanceof UndeprecateObjectsRequest) {
            ObjectRefListType refList = ((UndeprecateObjectsRequest) registryRequest).getObjectRefList();
            ids.addAll(getIdsFromObjectRefList(refList));
        }
        else if (registryRequest instanceof RemoveObjectsRequest) {
            ObjectRefListType refList = ((RemoveObjectsRequest) registryRequest).getObjectRefList();
            ids.addAll(getIdsFromObjectRefList(refList));
        }
        else if (registryRequest instanceof SubmitObjectsRequest) {
            RegistryObjectListType objList =
                ((SubmitObjectsRequest) registryRequest).getRegistryObjectList();
            List objs = getRegistryObjectList(objList);
            ids.addAll(getIdsFromRegistryObjects(objs));
        }
        else if (registryRequest instanceof UpdateObjectsRequest) {
            RegistryObjectListType objList =
                ((UpdateObjectsRequest) registryRequest).getRegistryObjectList();
            List objs = getRegistryObjectList(objList);
            ids.addAll(getIdsFromRegistryObjects(objs));
        }
        else if (registryRequest instanceof RelocateObjectsRequest) {
            // Do nothing.
        }
        else {
            throw new JAXRException(resourceBundle.getString("message.invalidRequest",
                    new String[]{registryRequest.getClass().getName()}));
        }

        return ids;
    }

    public Name getName(String name) throws JAXRException {
        Name internationalName = null;

        try {
            internationalName = rimFac.createName();

            LocalizedString ls = rimFac.createLocalizedString();
            ls.setValue(name);
            internationalName.getLocalizedString().add(ls);
        } catch (JAXBException e) {
            throw new JAXRException(e);
        }

        return internationalName;
    }

    public Description getDescription(String desc) throws JAXRException {
        Description internationalDesc = null;

        try {
            internationalDesc = rimFac.createDescription();

            LocalizedString ls = rimFac.createLocalizedString();
            ls.setValue(desc);
            internationalDesc.getLocalizedString().add(ls);
        } catch (JAXBException e) {
            throw new JAXRException(e);
        }

        return internationalDesc;
    }

    public String getInternationalStringAsString(InternationalStringType is) throws JAXRException {
        String str = "";
        List localizedStrings = is.getLocalizedString();
        if (localizedStrings.size() > 0) {
            LocalizedStringType ls = (LocalizedStringType)localizedStrings.get(0); //TODO: Need to do getClosestValue() in future
            str = ls.getValue();
        }
        return str;
    }

    public SOAPElement getSOAPElementFromBindingObject(Object obj) throws JAXRException {
        SOAPElement soapElem = null;

        try {
            SOAPElement parent =
                SOAPFactory.newInstance().createElement("dummy");

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal( obj, new DOMResult(parent) );
            soapElem = (SOAPElement)parent.getChildElements().next();

        }
        catch (Exception e) {
            throw new JAXRException(e);
        }

        return soapElem;
    }

    public Object getBindingObjectFromSOAPElement(SOAPElement soapElem) throws JAXRException {
        Object obj = null;

        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            obj = unmarshaller.unmarshal(soapElem);

        }
        catch (Exception e) {
            throw new JAXRException(e);
        }

        return obj;
    }

    public void checkRegistryResponse(RegistryResponseType resp) throws JAXRException {
        if (!(resp.getStatus().equals(CANONICAL_RESPONSE_STATUS_TYPE_ID_Success))) {
            StringWriter sw = new StringWriter();
            try {
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);

                marshaller.marshal(resp, sw);
                throw new JAXRException(sw.toString());
            }
            catch (Exception e) {
                throw new JAXRException(e);
            }
        }
    }

    /**
     * Gets the Set of ReferenceInfo for all object references within specified RegistryObject.
     * TODO: replace with reflections API when JAXB bindings use special class for ReferenceURI.
     *
     * Reference attributes based on scanning rim.xsd for anyURI.
     *
     * @param ro specifies the RegistryObject whose ObjectRefs are being sought.
     *
     * @param idMap The Map with old temporary id to new permanent id mapping.
     *
     */
    public Set getObjectRefsInRegistryObject(RegistryObjectType ro, Map idMap, Set processedObjects, int depth) throws JAXRException {
        HashSet refInfos = new HashSet();

        if ((ro != null) && (!processedObjects.contains(ro))) {
            processedObjects.add(ro);
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.RegistryObjectType", refInfos, idMap, "ObjectType");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType", refInfos, idMap, "Parent");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ClassificationType", refInfos, idMap, "ClassificationNode");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ClassificationType", refInfos, idMap, "ClassificationScheme");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ClassificationType", refInfos, idMap, "ClassifiedObject");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ExternalIdentifierType", refInfos, idMap, "IdentificationScheme");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ExternalIdentifierType", refInfos, idMap, "RegistryObject");

            //FederationType fed = (FederationType)ro;
            //TODO: Fix so it adds only Strings not ObjectRefType
            //refInfos.addAll(fed.getMembers().getObjectRef());

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.AssociationType1", refInfos, idMap, "AssociationType");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.AssociationType1", refInfos, idMap, "SourceObject");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.AssociationType1", refInfos, idMap, "TargetObject");


            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.AuditableEventType", refInfos, idMap, "User");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.AuditableEventType", refInfos, idMap, "RequestId");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.OrganizationType", refInfos, idMap, "Parent");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.RegistryType", refInfos, idMap, "Operator");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ServiceBindingType", refInfos, idMap, "Service");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.ServiceBindingType", refInfos, idMap, "TargetBinding");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.SpecificationLinkType", refInfos, idMap, "ServiceBinding");
            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.SpecificationLinkType", refInfos, idMap, "SpecificationObject");

            processRefAttribute(ro, "org.oasis.ebxml.registry.bindings.rim.SubscriptionType", refInfos, idMap, "Selector");

            --depth;

            //Now process composed objects
            if (depth != 0) {
                Set composedObjects = getComposedRegistryObjects(ro, 1);
                Iterator iter = composedObjects.iterator();
                while (iter.hasNext()) {
                    Object obj = iter.next();
                    if (obj instanceof RegistryObjectType) {
                        RegistryObjectType composedObject = (RegistryObjectType)obj;
                        Set composedRefInfos = getObjectRefsInRegistryObject(composedObject, idMap, processedObjects, depth);
                        refInfos.addAll(composedRefInfos);
                    }
                }
            }
        }

        return refInfos;
    }

    /**
     * Gets the Set of ReferenceInfo for specified reference attribute within RegistryObject.
     *
     * Reference attributes based on scanning rim.xsd for anyURI.
     *
     * @param ro specifies the RegistryObject whose reference attribute is being sought.
     *
     * @param idMap The HashMap with old temporary id to new permanent id mapping.
     *
     */
    private void processRefAttribute(RegistryObjectType ro, String className, Set refInfos, Map idMap, String attribute) throws JAXRException {
        try {
            //Use reflections API to get the attribute value, check if it needs to be mapped
            //and set it with mapped value if needed and add the final value to refInfos
            Class clazz = Class.forName(className);
            if (!(clazz.isInstance(ro))) {
                return;
            }

            //Get the attribute value by calling get method
            String getMethodName = "get" + attribute;
            Method getMethod = clazz.getMethod(getMethodName, (java.lang.Class[])null);

            //Invoke getMethod to get the reference target object's id
            String targetObjectId = (String)getMethod.invoke(ro, (java.lang.Object[])null);

            if (targetObjectId != null) {
                //Check if id has been mapped to a new id
                if (idMap.containsKey(targetObjectId)) {
                    //Replace old id with new id
                    targetObjectId = (String)idMap.get(targetObjectId);

                    //Use set method to set new value on ro
                    Class[] parameterTypes = new Class[1];
                    Object[] parameterValues = new Object[1];
                    parameterTypes[0] = Class.forName("java.lang.String");
                    parameterValues[0] = targetObjectId;
                    String setMethodName = "set" + attribute;
                    Method setMethod = clazz.getMethod(setMethodName, parameterTypes);
                    setMethod.invoke(ro, parameterValues);
                }

                ReferenceInfo refInfo = new ReferenceInfo(ro.getId(), targetObjectId, attribute);
                refInfos.add(refInfo);
            }

        }
        catch (Exception e) {
            //throw new OMARExeption("Class = " ro.getClass() + " attribute = " + attribute", e);
            log.error(CommonResourceBundle.getInstance().getString("message.ErrorClassAttribute", new Object[]{ro.getClass(), attribute}));
            e.printStackTrace();
        }

    }


    /**
     * Gets the composed RegistryObjects within specified RegistryObject.
     * Based on scanning rim.xsd for </sequence>.
     *
     * @param registryObjects specifies the RegistryObjects whose composed objects are being sought.
     * @param depth specifies depth of fetch. -1 implies fetch all levels. 1 implies fetch immediate composed objects.
     */
    public Set getComposedRegistryObjects(Collection registryObjects, int depth) {
        HashSet composedObjects = new HashSet();

        Iterator iter = registryObjects.iterator();
        while (iter.hasNext()) {
            RegistryObjectType ro = (RegistryObjectType)iter.next();
            composedObjects.addAll(getComposedRegistryObjects(ro, depth));
        }

        return composedObjects;
    }

    public static boolean isComposedObject(RegistryObjectType ro) {
        boolean isComposed = false;

        if ((ro instanceof ClassificationType)
            || (ro instanceof ExternalIdentifierType)
            || (ro instanceof ServiceBindingType)
            || (ro instanceof SpecificationLinkType)) {

            isComposed = true;
        }

        return isComposed;
    }

    public static String getParentIdForComposedObject(RegistryObjectType ro) {
        String parentId = null;

        if (ro instanceof ClassificationType) {
            parentId = ((ClassificationType)ro).getClassifiedObject();
        } else if (ro instanceof ExternalIdentifierType) {
            parentId = ((ExternalIdentifierType)ro).getRegistryObject();
        } else if (ro instanceof ServiceBindingType) {
            parentId = ((ServiceBindingType)ro).getService();
        } else if (ro instanceof SpecificationLinkType) {
            parentId = ((SpecificationLinkType)ro).getServiceBinding();
        }

        return parentId;
    }
    
    
    public static void setParentIdForComposedObject( RegistryObjectType ro, String parentId ) 
    {
        if (ro instanceof ClassificationType) {
             ((ClassificationType)ro).setClassifiedObject( parentId );
        } else if (ro instanceof ExternalIdentifierType) {
            ((ExternalIdentifierType)ro).setRegistryObject( parentId );
        } else if (ro instanceof ServiceBindingType) {
            ((ServiceBindingType)ro).setService( parentId );
        } else if (ro instanceof SpecificationLinkType) {
            ((SpecificationLinkType)ro).setServiceBinding( parentId );
        }
    }


    /**
     * Gets the composed RegistryObjects within specified RegistryObject.
     * Based on scanning rim.xsd for </sequence>.
     *
     * @param ro specifies the RegistryObject whose composed objects are being sought.
     * @param depth specifies depth of fetch. -1 implies fetch all levels. 1 implies fetch immediate composed objects.
     */
    public Set getComposedRegistryObjects(RegistryObjectType ro, int depth) {
        HashSet composedObjects = new HashSet();

        if (ro != null) {
            List immediateComposedObjects = new ArrayList();

            immediateComposedObjects.addAll(ro.getClassification());
            immediateComposedObjects.addAll(ro.getExternalIdentifier());


            if (ro instanceof ClassificationNodeType) {
                ClassificationNodeType node = (ClassificationNodeType)ro;
                immediateComposedObjects.addAll(node.getClassificationNode());
            }
            else if (ro instanceof ClassificationSchemeType) {
                ClassificationSchemeType scheme = (ClassificationSchemeType)ro;
                immediateComposedObjects.addAll(scheme.getClassificationNode());
            }
            else if (ro instanceof ServiceBindingType) {
                ServiceBindingType binding = (ServiceBindingType)ro;
                immediateComposedObjects.addAll(binding.getSpecificationLink());
            }
            else if (ro instanceof RegistryPackageType) {
                RegistryPackageType pkg = (RegistryPackageType)ro;
                if (pkg.getRegistryObjectList() != null) {
                    immediateComposedObjects.addAll(pkg.getRegistryObjectList().getIdentifiable());
                }
            }
            else if (ro instanceof ServiceType) {
                ServiceType service = (ServiceType)ro;
                immediateComposedObjects.addAll(service.getServiceBinding());
            }

            --depth;

            //Add each immediate composedObject
            Iterator iter = immediateComposedObjects.iterator();
            while (iter.hasNext()) {
                RegistryObjectType composedObject = (RegistryObjectType)iter.next();
                composedObjects.add(composedObject);

                //If depth != 0 then recurse and add descendant composed objects
                if (depth != 0) {
                    composedObjects.addAll(getComposedRegistryObjects(composedObject, depth));
                }
            }
        }

        return composedObjects;
    }

    public String marshalObject(Object obj) throws JAXBException {

        StringWriter sw = new StringWriter();
        javax.xml.bind.Marshaller marshaller = rsFac.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
            Boolean.TRUE);
        marshaller.marshal(obj, sw);

        //Now get the object as a String
        String str = sw.toString();
        return str;
    }

    public void printObject(Object obj) throws JAXBException {

        StringWriter sw = new StringWriter();
        javax.xml.bind.Marshaller marshaller = rsFac.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
            Boolean.TRUE);
        marshaller.marshal(obj, sw);

        //Now get the object as a String
        String str = sw.toString();
        System.err.println(str);
    }

    /**
     * This method is used for adding a java.util.Map of slots to a RegistryObjectType
     *
     * @param ro
     *   The RegistryObjectType on which to set slots
     * @param slotsMap
     *   The java.util.Map of slots to set on the RegistryObjectType. This Map
     *   must only contain java.lang.String and java.util.Collection values
     * @throws IllegalArgumentException
     *   This RuntimeException is thrown if the slotsMap argument contains
     *   neither a java.lang.String nor a java.util.Collection
     * @throws JAXBException
     *   Thrown when there is a problem marshalling/unmarshalling JAXB objects
     */
    public void addSlotsToRegistryObject(RegistryObjectType ro, Map slotsMap)
        throws JAXBException {

        ArrayList slots = new ArrayList();

        Iterator iter = slotsMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object slotValue = slotsMap.get(key);

            Slot slot = rimFac.createSlot();
            slot.setName(key.toString());
            ValueList valueList = rimFac.createValueList();

            //slotValue must either be a String or a Collection of Strings
            if (slotValue instanceof String) {
                Value value = rimFac.createValue();
                value.setValue(slotValue.toString());
                valueList.getValue().add(value);

            } else if (slotValue instanceof Collection) {
                Collection c = (Collection)slotValue;
                Iterator citer = c.iterator();
                while (citer.hasNext()) {
                    String str = citer.next().toString();
                    Value value = rimFac.createValue();
                    value.setValue(str);
                    valueList.getValue().add(value);
                }
            } else {
                throw new IllegalArgumentException(resourceBundle.getString("message.addingParameter",
                        new String[]{slotValue.getClass().getName()}));
            }
            slot.setValueList(valueList);
            slots.add(slot);
        }

        ro.getSlot().addAll(slots);
    }

    /**
     * This method is used to add a single slot to a RegistryObject.
     *
     * @param ro
     *   The RegistryObjectType on which to set slots
     * @param slotName
     *   The slotname
     * @param slotType
     *   The slotType
     * @param values
     *   The slot values
     * @throws IllegalArgumentException
     *   This RuntimeException is thrown if the slotsMap argument contains
     *   neither a java.lang.String nor a java.util.Collection
     * @throws JAXBException
     *   Thrown when there is a problem marshalling/unmarshalling JAXB objects
     */
    public void addSlotToRegistryObject(RegistryObjectType ro, String slotName, String slotType, List values)
        throws JAXBException {

        Slot slot = rimFac.createSlot();
        slot.setName(slotName);
        slot.setSlotType(slotType);
        ValueList valueList = rimFac.createValueList();
        
        Iterator valuesIter = values.iterator();
        while (valuesIter.hasNext()) {
            String str = valuesIter.next().toString();
            Value value = rimFac.createValue();
            value.setValue(str);
            valueList.getValue().add(value);
        }

        slot.setValueList(valueList);
        ro.getSlot().add(slot);
    }
    
    public HashMap getSlotsFromRegistryObject(RegistryObjectType ro) throws JAXBException {
        HashMap slotsMap = new HashMap();

        List slots = ro.getSlot();

        if (slots == null) {
            return slotsMap;
        }

        Iterator iter = slots.iterator();
        while (iter.hasNext()) {
            SlotType1 slot = (SlotType1)iter.next();
            String slotName = slot.getName();
            List values = slot.getValueList().getValue();

            Object slotValue = null;
            if (values.size() == 1) {
                Value value = (Value)(values).get(0);
                slotValue = value.getValue();;
                slotsMap.put(slotName, slotValue);
            } else if (values.size() > 1) {
                ArrayList al = new ArrayList();
                Iterator valuesIter = values.iterator();
                while (valuesIter.hasNext()) {
                    Value value = (Value)valuesIter.next();
                    String str = value.getValue();
                    al.add(str);
                }
                slotsMap.put(slotName, al);
            }
        }


        return slotsMap;
    }

    /**
     * This method is used for adding a java.util.Map of slots to a RegistryRequestType
     *
     * @param req
     *   The RegistryRequestType on which to set slots
     * @param slotsMap
     *   The java.util.Map of slots to set on the RegistryRequestType. This Map
     *   must only contain java.lang.String and java.util.Collection values
     * @throws IllegalArgumentException
     *   This RuntimeException is thrown if the slotsMap argument contains
     *   neither a java.lang.String nor a java.util.Collection
     * @throws JAXBException
     *   Thrown when there is a problem marshalling/unmarshalling JAXB objects
     */
    public void addSlotsToRequest(RegistryRequestType req, Map slotsMap) throws JAXBException {

        // Get SlotListType from RegistryRequestType, and add new slot to this list
        SlotListType slotList = req.getRequestSlotList();
        if (slotList == null) {
            slotList = rimFac.createSlotListType();
        }
        Iterator iter = slotsMap.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object slotValue = slotsMap.get(key);

            Slot slot = rimFac.createSlot();
            slot.setName(key.toString());
            ValueList valueList = rimFac.createValueList();

            //slotValue must either be a String or a Collection of Strings
            if (slotValue instanceof String) {
                Value value = rimFac.createValue();
                value.setValue(slotValue.toString());
                valueList.getValue().add(value);

            } else if (slotValue instanceof Collection) {
                Collection c = (Collection)slotValue;
                Iterator citer = c.iterator();
                while (citer.hasNext()) {
                    String str = citer.next().toString();
                    Value value = rimFac.createValue();
                    value.setValue(str);
                    valueList.getValue().add(value);
                }
            } else {
                throw new IllegalArgumentException(resourceBundle.getString("message.addingParameter",
                        new String[]{slotValue.getClass().getName()}));
            }
            slot.setValueList(valueList);
            slotList.getSlot().add(slot);
        }


        req.setRequestSlotList(slotList);
    }

    public HashMap getSlotsFromRequest(RegistryRequestType req) throws JAXBException {
        HashMap slotsMap = new HashMap();

        if (req != null) {
            SlotListType slotList = req.getRequestSlotList();

            if (slotList == null) {
                return slotsMap;
            }

            List slots = slotList.getSlot();

            Iterator iter = slots.iterator();
            while (iter.hasNext()) {
                SlotType1 slot = (SlotType1)iter.next();
                String slotName = slot.getName();
                List values = slot.getValueList().getValue();

                Object slotValue = null;
                if (values.size() == 1) {
                    Value value = (Value)(values).get(0);
                    slotValue = value.getValue();;
                    slotsMap.put(slotName, slotValue);
                } else if (values.size() > 1) {
                    ArrayList al = new ArrayList();
                    Iterator valuesIter = values.iterator();
                    while (valuesIter.hasNext()) {
                        Value value = (Value)valuesIter.next();
                        String str = value.getValue();
                        al.add(str);
                    }
                    slotsMap.put(slotName, al);
                }
            }
        }

        return slotsMap;
    }

    public static String getSchemeIdFromNodePath(String nodePath) {
        String[] pathElements = nodePath.split("/");
        if (pathElements.length < 2) {
            return null;
        } else {
            return pathElements[1];
        }
    }

    public String getSchemeIdForRegistryObject(RegistryObjectType ro) throws JAXRException {
        String schemeId = null;

        if (ro instanceof ClassificationSchemeType) {
            schemeId = ro.getId();
        } else if (ro instanceof ClassificationNodeType) {
            ClassificationNodeType node = (ClassificationNodeType)ro;
            String path = node.getPath();
            schemeId = getSchemeIdFromNodePath(path);
        } else {
            throw new JAXRException(resourceBundle.getString("message.unexpectedObjectType",
                        new String[] {ro.getClass().toString(), "org.oasis.ebxml.registry.bindings.rim.ClassificationSchemeType, org.oasis.ebxml.registry.bindings.rim.ClassificationNodeType"}));
        }

        return schemeId;
    }

    /**
     * Makes an identical clone of a RegistryObjectType as a new java object.
     */
    public RegistryObjectType cloneRegistryObject(RegistryObjectType ro) throws JAXRException {
        RegistryObjectType roNew = null;
        try {
            StringWriter sw = new StringWriter();
            rimFac.createMarshaller().marshal(ro, sw);
            roNew = (RegistryObjectType)rimFac.createUnmarshaller().unmarshal(
                new StreamSource( new StringReader( sw.toString())) );
        } catch (javax.xml.bind.JAXBException e) {
            throw new JAXRException(e);
        }

        return roNew;
    }

    public InternationalStringType createInternationalStringType(String val) throws JAXRException {
        InternationalStringType is = null;

        try {
            is = rimFac.createInternationalStringType();
            LocalizedString ls = rimFac.createLocalizedString();
            ls.setValue(val);
            is.getLocalizedString().add(ls);
            //Use default values for lang and charset
        } catch (javax.xml.bind.JAXBException e) {
            throw new JAXRException(e);
        }

        return is;
    }

    //Creates an AdhocQueryRequest for a parameterized stored query invocation
    public AdhocQueryRequest createAdhocQueryRequest(String queryId, Map queryParams) throws JAXBException {
        AdhocQueryRequest req = createAdhocQueryRequest("");

        HashMap slotsMap = new HashMap(queryParams);
        slotsMap.put(CANONICAL_SLOT_QUERY_ID, queryId);

        this.addSlotsToRequest(req, slotsMap);

        return req;
    }

    //Creates an AdhocQueryRequest for a normal (non-parameterized query
    public AdhocQueryRequest createAdhocQueryRequest(String queryStr) throws JAXBException {
        AdhocQueryRequest req = null;

        AdhocQuery adhocQuery = createAdhocQuery(queryStr);
        req = queryFac.createAdhocQueryRequest();
        req.setId(org.freebxml.omar.common.Utility.getInstance().createId());
        req.setAdhocQuery(adhocQuery);

        ResponseOption ro = queryFac.createResponseOption();
        ro.setReturnComposedObjects(true);
        ro.setReturnType(ReturnType.LEAF_CLASS_WITH_REPOSITORY_ITEM);
        req.setResponseOption(ro);

        return req;
    }

    public AdhocQuery createAdhocQuery(String queryStr) throws JAXBException {
        AdhocQuery adhocQuery = null;

        adhocQuery = rimFac.createAdhocQuery();
        adhocQuery.setId(org.freebxml.omar.common.Utility.getInstance().createId());

        QueryExpressionType queryExp = rimFac.createQueryExpressionType();
        adhocQuery.setQueryExpression(queryExp);
        queryExp.setQueryLanguage(CANONICAL_QUERY_LANGUAGE_ID_SQL_92);
        queryExp.getContent().add(queryStr);

        return adhocQuery;
    }

    public SubmitObjectsRequest createSubmitRequest(boolean dontVersion, boolean dontVersionContent, List ros) throws Exception {
        SubmitObjectsRequest request = lcmFac.createSubmitObjectsRequest();

        HashMap slotsMap = new HashMap();
        if (dontVersion) {
            slotsMap.put(CANONICAL_SLOT_LCM_DONT_VERSION, "true");
        }

        if (dontVersionContent) {
            slotsMap.put(CANONICAL_SLOT_LCM_DONT_VERSION_CONTENT, "true");
        }

        if (!slotsMap.isEmpty()) {
            addSlotsToRequest(request, slotsMap);
        }

        if ((ros != null) && (ros.size() > 0)) {
            org.oasis.ebxml.registry.bindings.rim.RegistryObjectList roList = rimFac.createRegistryObjectList();
            roList.getIdentifiable().addAll(ros);
            request.setRegistryObjectList(roList);
        }

        return request;
    }

    public void addRegistryObjectToSubmitRequest(SubmitObjectsRequest submitRequest, RegistryObjectType ro) throws JAXRException {
        try {
            ArrayList ros = new ArrayList();
            ros.add(ro);
            org.oasis.ebxml.registry.bindings.rim.RegistryObjectList roList = rimFac.createRegistryObjectList();
            roList.getIdentifiable().addAll(ros);
            submitRequest.setRegistryObjectList(roList);
        } catch (javax.xml.bind.JAXBException e) {
            throw new JAXRException(e);
        }
    }

    public Classification createClassification(String classifiedObjectId, String classificationNodeId) throws JAXRException {
        Classification classification = null;
        try {
            classification = rimFac.createClassification();
            classification.setId(org.freebxml.omar.common.Utility.getInstance().createId());
            classification.setClassifiedObject(classifiedObjectId);
            classification.setClassificationNode(classificationNodeId);
        } catch (javax.xml.bind.JAXBException e) {
            throw new JAXRException(e);
        }

        return classification;
    }

    public void addClassificationToRegistryObject(RegistryObjectType ro, String classificationNodeId) throws JAXRException {
        Classification classification = createClassification(ro.getId(), classificationNodeId);
        ro.getClassification().add(classification);
    }

    public Association createAssociation(String src, String target, String associationTypeNodeId) throws JAXRException {
        String associationId = org.freebxml.omar.common.Utility.getInstance().createId();
        return createAssociation(src, target, associationTypeNodeId, associationId);
    }

    public Association createAssociation(String src, String target, String associationTypeNodeId, String assocId) throws JAXRException {
        Association ass = null;
        try {
            ass = rimFac.createAssociation();
            ass.setId(assocId);
            ass.setAssociationType(associationTypeNodeId);
            ass.setSourceObject(src);
            ass.setTargetObject(target);
        } catch (JAXBException e) {
            throw new JAXRException(e);
        }
        return ass;
    }

    /**
     * Creates as id for an Association based upon the srcId, targetId and associationTypeNodeId.
     * Care should be taken to only use this when there is supposed to be only one Association
     * between two objects of the given associationType.
     */
    public String createAssociationId(String srcId, String targetId, String associationTypeNodeId) throws JAXRException {
        String associationId = null;

        if (associationTypeNodeId != null) {
            associationId = srcId + ":" + associationTypeNodeId + ":" + targetId;
        }


        //Fallback to generated id if id is not valid (usually because it is too long.
        if (!Utility.getInstance().isValidRegistryId(associationId)) {
            associationId = org.freebxml.omar.common.Utility.getInstance().createId();
        }

        return associationId;
    }

    public static String mapJAXRNameToEbXMLName(String className) {
        String newName = null;

        if (className.equalsIgnoreCase("Concept")) {
            newName = "ClassificationNode";
        } else {
            newName = className;
        }

        return newName;
    }

    public static String mapEbXMLNameToJAXRName(String ebXMLName) {
        String newName = null;

        if (ebXMLName.equalsIgnoreCase("ClassificationNode")) {
            newName = "Concept";
        } else {
            newName = ebXMLName;
        }

        return newName;
    }

    public static String getActionFromRequest(RegistryRequestType registryRequest) throws JAXRException {
        String action = null;
        if (registryRequest instanceof AdhocQueryRequest) {
            action = ACTION_READ;
        }
        else if (registryRequest instanceof SubmitObjectsRequest) {
            action = ACTION_CREATE;
        }
        else if (registryRequest instanceof ApproveObjectsRequest) {
            action = ACTION_APPROVE;
        }
        else if (registryRequest instanceof DeprecateObjectsRequest) {
            action = ACTION_DEPRECATE;
        }
        else if (registryRequest instanceof UndeprecateObjectsRequest) {
            action = ACTION_UNDEPRECATE;
        }
        else if (registryRequest instanceof UpdateObjectsRequest) {
            action = ACTION_UPDATE;
        }
        else if (registryRequest instanceof RemoveObjectsRequest) {
            action = ACTION_DELETE;
        }
        else if (registryRequest instanceof RelocateObjectsRequest) {
            action = ACTION_RELOCATE;
        }
        else if (registryRequest instanceof SetStatusOnObjectsRequest) {
            action = ACTION_SET_STATUS;
        }
        else if (registryRequest instanceof RegistryRequestType) {
            //??Get slotName and return the extension protocol name here
            action = ACTION_EXTENSION_REQUEST;
        }
        else {
            throw new InvalidRequestException(CommonResourceBundle.getInstance().getString("message.unexpectedObjectType",
                    new Object[]{registryRequest.getClass().getName(), "org.oasis.ebxml.registry.bindings.rs.RegistryRequestType"}));
        }

        return action;
    }

    /**
     * Client uses Datahandler as repository item while server uses RepositoryItemImpl
     * Need to convert.
     */
    public void convertRepositoryItemMapForServer(Map idToRepositoryItemMap) throws RegistryException {
        Set keys = idToRepositoryItemMap.keySet();
        Iterator keysIter = keys.iterator();
        while (keysIter.hasNext()) {
            Object key = keysIter.next();
            DataHandler dh = (DataHandler)idToRepositoryItemMap.get(key);
            RepositoryItem ri = new RepositoryItemImpl((String)key, dh);
            idToRepositoryItemMap.put(key, ri);
        }
    }

    /**
     * Client uses Datahandler as repository item while server uses RepositoryItemImpl
     * Need to convert.
     */
    public void convertRepositoryItemMapForClient(Map idToRepositoryItemMap) throws RegistryException {
        Set keys = idToRepositoryItemMap.keySet();
        Iterator keysIter = keys.iterator();
        while (keysIter.hasNext()) {
            Object key = keysIter.next();
            RepositoryItem ri = (RepositoryItem)idToRepositoryItemMap.get(key);
            DataHandler dh = ri.getDataHandler();
            idToRepositoryItemMap.put(key, dh);
        }
    }


    /**
     * Gets the singleton instance as defined by Singleton pattern.
     *
     * @return the singleton instance
     *
     */
    public synchronized static BindingUtility getInstance() {
        if (instance == null) {
            instance = new BindingUtility();
        }

        return instance;
    }
}
