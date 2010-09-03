/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.common.connectionmanager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.connectmgr.data.CMAddress;
import gov.hhs.fha.nhinc.connectmgr.data.CMAddresses;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplates;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.connectmgr.data.CMContact;
import gov.hhs.fha.nhinc.connectmgr.data.CMContactDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMContacts;
import gov.hhs.fha.nhinc.connectmgr.data.CMDiscoveryURLs;
import gov.hhs.fha.nhinc.connectmgr.data.CMEmails;
import gov.hhs.fha.nhinc.connectmgr.data.CMEprInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMPersonNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMPhones;
import gov.hhs.fha.nhinc.connectmgr.data.CMStates;

import java.util.List;

import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import gov.hhs.fha.nhinc.connectmgr.CMEprUtil;

import gov.hhs.fha.nhinc.common.connectionmanagerinfo.AddressType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.AddressesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BindingDescriptionsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BindingNamesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BindingTemplateType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BindingTemplatesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessDescriptionsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntitiesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessEntityType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessNamesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessServiceType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.BusinessServicesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoEndpointsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfoType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ConnectionInfosType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ContactDescriptionsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ContactType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ContactsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.DiscoveryURLsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.EmailsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.PersonNamesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.PhonesType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfoEndpointType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfoEndpointsType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfoType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.ServiceConnectionInfosType;
import gov.hhs.fha.nhinc.common.connectionmanagerinfo.StatesType;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.EPRType;
import gov.hhs.fha.nhinc.common.nhinccommon.CreateEPRRequestType;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.ServiceNameType;

/**
 * This class is used to transform from the internal POJO representations of 
 * connection manager information to the Web Service representations.
 * 
 * @author Les Westberg
 */
public class CMTransform 
{
    private static Log log = LogFactory.getLog(CMTransform.class);
    private static final String EPR_PROPERTY_FILE_NAME = "connectionEPR";
    private static final String EPR_NAMESPACE_URI = "NamespaceURI";
    private static final String EPR_PORT_NAME = "PortName";
    private static final String EPR_SERVICE_NAME = "ServiceName";
    private static final String EPR_NAMESPACE_PREFIX = "NamespacePrefix";
    
    /**
     * Transform a POJO representation of a CMHomeCommunity to the web service represenation.
     * 
     * @param oCMHomeCommunity The POJO representation.
     * @return The web service representation.
     */
    public static HomeCommunityType cmHomeCommunityToHomeCommunity(CMHomeCommunity oCMHomeCommunity)
    {
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        
        if (oCMHomeCommunity == null)
        {
            return null;
        }
        
        oHomeCommunity.setDescription(oCMHomeCommunity.getDescription());
        oHomeCommunity.setName(oCMHomeCommunity.getName());
        oHomeCommunity.setHomeCommunityId(oCMHomeCommunity.getHomeCommunityId());
        
        return oHomeCommunity;
        
    }

    /**
     * Transform a POJO representation of a CMBusinessEntity to the web service represenation of a home commnity.
     * 
     * @param oCMBusinessEntity The POJO representation.
     * @return The web service representation.
     */
    public static HomeCommunityType cmBusinessEntityToHomeCommunity(CMBusinessEntity oCMBusinessEntity)
    {
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        
        if (oCMBusinessEntity == null)
        {
            return null;
        }
        
        // Description
        //------------
        if ((oCMBusinessEntity.getDescriptions() != null) &&
            (oCMBusinessEntity.getDescriptions().getBusinessDescription() != null) &&
            (oCMBusinessEntity.getDescriptions().getBusinessDescription().size() > 0) &&
            (oCMBusinessEntity.getDescriptions().getBusinessDescription().get(0) != null) &&
            (oCMBusinessEntity.getDescriptions().getBusinessDescription().get(0).length() > 0))
        {
            oHomeCommunity.setDescription(oCMBusinessEntity.getDescriptions().getBusinessDescription().get(0));
        }
        
        // Name
        //------
        if ((oCMBusinessEntity.getNames() != null) &&
            (oCMBusinessEntity.getNames().getBusinessName() != null) &&
            (oCMBusinessEntity.getNames().getBusinessName().size() > 0) &&
            (oCMBusinessEntity.getNames().getBusinessName().get(0) != null) &&
            (oCMBusinessEntity.getNames().getBusinessName().get(0).length() > 0))
        {
            oHomeCommunity.setName(oCMBusinessEntity.getNames().getBusinessName().get(0));
        }
        

        // Home Community Id
        //-------------------
        oHomeCommunity.setHomeCommunityId(oCMBusinessEntity.getHomeCommunityId());
        
        return oHomeCommunity;
        
    }
    
    /**
     * Transform the information in a list of CMHomeCommunity objects to the web service representation
     * of these data types.
     * 
     * @param oaCMHomeCommunity The POJO home community information.
     * @return The Web service representation of the information.
     */
    public static HomeCommunitiesType listCMHomeCommunityToHomeCommunitiesType(List<CMHomeCommunity> oaCMHomeCommunity)
    {
        HomeCommunitiesType oCommunities = new HomeCommunitiesType();
        
        if ((oaCMHomeCommunity != null) && (oaCMHomeCommunity.size() > 0))
        {
            for (CMHomeCommunity oCMHomeCommunity : oaCMHomeCommunity)
            {
                HomeCommunityType oHomeCommunity = cmHomeCommunityToHomeCommunity(oCMHomeCommunity);
                
                if (oHomeCommunity != null)
                {
                    oCommunities.getHomeCommunity().add(oHomeCommunity);
                }
            }   // for (CMHomeCommunity oCommunity : oaHomeCommunity)
        }
        
        if (oCommunities.getHomeCommunity().size() > 0)
        {
            return oCommunities;
        }
        else
        {
            return null;
        }
        
    }

    /**
     * Transform the information in a CMPhones object to the web service representation
     * of this data type.
     * 
     * @param oCMPhones The POJO information.
     * @return The Web service representation of the information.
     */
    public static PhonesType cmPhonesToPhonesType(CMPhones oCMPhones)
    {
        PhonesType oPhones = new PhonesType();
        
        if ((oCMPhones == null) ||
            (oCMPhones.getPhone() == null) ||
            (oCMPhones.getPhone().size() <= 0))
        {
            return null;
        }

        oPhones.getPhone().addAll(oCMPhones.getPhone());

        return oPhones;
    }
    
    /**
     * Transform the information in a CMPersonNames object to the web service representation
     * of this data type.
     * 
     * @param oCMPersonNames The POJO information.
     * @return The Web service representation of the information.
     */
    public static PersonNamesType cmPersonNamesToPersonNamesType(CMPersonNames oCMPersonNames)
    {
        PersonNamesType oPersonNames = new PersonNamesType();
        
        if ((oCMPersonNames == null) ||
            (oCMPersonNames.getPersonName() == null) ||
            (oCMPersonNames.getPersonName().size() <= 0))
        {
            return null;
        }

        oPersonNames.getPersonName().addAll(oCMPersonNames.getPersonName());

        return oPersonNames;
        
    }
    
    /**
     * Transform the information in a CMEmails object to the web service representation
     * of this data type.
     * 
     * @param oCMEmails The POJO information.
     * @return The Web service representation of the information.
     */
    public static EmailsType cmEmailsToEmailstype(CMEmails oCMEmails)
    {
        EmailsType oEmails = new EmailsType();
        
        if ((oCMEmails == null) ||
            (oCMEmails.getEmail() == null) ||
            (oCMEmails.getEmail().size() <= 0))
        {
            return null;
        }

        oEmails.getEmail().addAll(oCMEmails.getEmail());

        return oEmails;
    }
    
    /**
     * Transform the information in a CMContactDescriptions object to the web service representation
     * of this data type.
     * 
     * @param oCMContactDescriptions The POJO information.
     * @return The Web service representation of the information.
     */
    public static ContactDescriptionsType cmContactDescriptionsToContactDescriptionsType(CMContactDescriptions oCMContactDescriptions)
    {
        ContactDescriptionsType oContactDescriptions = new ContactDescriptionsType();
        
        if ((oCMContactDescriptions == null) ||
            (oCMContactDescriptions.getDescription() == null) ||
            (oCMContactDescriptions.getDescription().size() <= 0))
        {
            return null;
        }

        oContactDescriptions.getDescription().addAll(oCMContactDescriptions.getDescription());

        return oContactDescriptions;
    }
    
    /**
     * Transform the information in a CMAddresses object to the web service representation
     * of this data type.
     * 
     * @param oCMAddresses The POJO information.
     * @return The Web service representation of the information.
     */
    public static AddressesType cmAddressesToAddressesType(CMAddresses oCMAddresses)
    {
        AddressesType oAddresses = new AddressesType();
        
        if ((oCMAddresses == null) ||
            (oCMAddresses.getAddress() == null) ||
            (oCMAddresses.getAddress().size() <= 0))
        {
            return null;
        }
        
        for (CMAddress oCMAddress : oCMAddresses.getAddress())
        {
            AddressType oAddress = new AddressType();
            
            if ((oCMAddress.getAddressLine() != null) &&
                (oCMAddress.getAddressLine().size() > 0))
            {
                oAddress.getAddressLine().addAll(oCMAddress.getAddressLine());
            }
            
            oAddresses.getAddress().add(oAddress);
        }
        
        return oAddresses;
    }

    /**
     * Transform the information in a CMBindingNames object to the web service representation
     * of this data type.
     * 
     * @param oCMBindingNames The POJO information.
     * @return The Web service representation of the information.
     */
    public static List<BindingNamesType> cmBindingNamesToListBindingNamesType(CMBindingNames oCMBindingNames)
    {
        List<BindingNamesType> oaBindingNames = new ArrayList<BindingNamesType>();
        
        if ((oCMBindingNames == null) ||
            (oCMBindingNames.getName() == null) ||
            (oCMBindingNames.getName().size() <= 0))
        {
            return null;
        }
        
        for (String sCMBindingName : oCMBindingNames.getName())
        {
            BindingNamesType oBindingNames = new BindingNamesType();
            oBindingNames.getName().add(sCMBindingName);
            oaBindingNames.add(oBindingNames);
        }

        if (oaBindingNames.size() > 0)
        {
            return oaBindingNames;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Transform the information in a CMBindingDescriptions object to the web service representation
     * of this data type.
     * 
     * @param oCMBindingDescriptions  The POJO information.
     * @return The Web service representation of the information.
     */
    public static List<BindingDescriptionsType> cmBindingDescriptionsToListBindingDescriptionsType(CMBindingDescriptions oCMBindingDescriptions)
    {
        List<BindingDescriptionsType> oaBindingDescriptions = new ArrayList<BindingDescriptionsType>();
        
        if ((oCMBindingDescriptions == null) ||
            (oCMBindingDescriptions.getDescription() == null) ||
            (oCMBindingDescriptions.getDescription().size() <= 0))
        {
            return null;
        }
        
        for (String sCMBindingDescription : oCMBindingDescriptions.getDescription())
        {
            BindingDescriptionsType oBindingDescriptions = new BindingDescriptionsType();
            oBindingDescriptions.getDescription().add(sCMBindingDescription);
            oaBindingDescriptions.add(oBindingDescriptions);
        }

        if (oaBindingDescriptions.size() > 0)
        {
            return oaBindingDescriptions;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Transform the information in a CMBindingTemplates object to the web service representation
     * of this data type.
     * 
     * @param oCMBindingTemplates The POJO information.
     * @return The Web service representation of the information.
     */
    public static BindingTemplatesType cmBindingTemplatesToBindingTemplatesType(CMBindingTemplates oCMBindingTemplates)
    {
        BindingTemplatesType oBindingTemplates = new BindingTemplatesType();
        
        if ((oCMBindingTemplates == null) ||
            (oCMBindingTemplates.getBindingTemplate() == null) ||
            (oCMBindingTemplates.getBindingTemplate().size() <= 0))
        {
            return null;
        }
        
        for (CMBindingTemplate oCMBindingTemplate : oCMBindingTemplates.getBindingTemplate())
        {
            BindingTemplateType oBindingTemplate = new BindingTemplateType();
            
            oBindingTemplate.setBindingKey(oCMBindingTemplate.getBindingKey());
            oBindingTemplate.setEndpointURL(oCMBindingTemplate.getEndpointURL());
            oBindingTemplate.setWsdlURL(oCMBindingTemplate.getWsdlURL());
            oBindingTemplate.setServiceVersion(oCMBindingTemplate.getServiceVersion());
            oBindingTemplates.getBindingTemplate().add(oBindingTemplate);
        }

        if (oBindingTemplates.getBindingTemplate().size() > 0)
        {
            return oBindingTemplates;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Transform the information in a States object to the web service representation
     * of this data type.
     * 
     * @param oCMStates The POJO information.
     * @return The Web service representation of the information.
     */
    public static StatesType cmStatesToStatesType(CMStates oCMStates)
    {
        StatesType oStates = new StatesType();
        
        if ((oCMStates == null) ||
            (oCMStates.getState() == null) ||
            (oCMStates.getState().size() <= 0))
        {
            return null;
        }

        oStates.getState().addAll(oCMStates.getState());

        return oStates;
    }
    
    /**
     * Transform the information in a CMBusinessNames object to the web service representation
     * of this data type.
     * 
     * @param oCMBusinessNames The POJO information.
     * @return The Web service representation of the information.
     */
    public static BusinessNamesType cmBusinessNamesToBusinessNamesType(CMBusinessNames oCMBusinessNames)
    {
        BusinessNamesType oBusinessNames = new BusinessNamesType();
        
        if ((oCMBusinessNames == null) ||
            (oCMBusinessNames.getBusinessName() == null) ||
            (oCMBusinessNames.getBusinessName().size() <= 0))
        {
            return null;
        }
        
        oBusinessNames.getBusinessName().addAll(oCMBusinessNames.getBusinessName());

        return oBusinessNames;
        
    }
    
    /**
     * Transform the information in a CMDiscoveryURLs object to the web service representation
     * of this data type.
     * 
     * @param oCMDiscoveryURLs The POJO information.
     * @return The Web service representation of the information.
     */
    public static DiscoveryURLsType cmDiscoveryURLsToDiscoveryURLsType(CMDiscoveryURLs oCMDiscoveryURLs)
    {
        DiscoveryURLsType oDiscoveryURLs = new DiscoveryURLsType();
        
        if ((oCMDiscoveryURLs == null) ||
            (oCMDiscoveryURLs.getDiscoveryURL() == null) ||
            (oCMDiscoveryURLs.getDiscoveryURL().size() <= 0))
        {
            return null;
        }
        
        oDiscoveryURLs.getDiscoveryURL().addAll(oCMDiscoveryURLs.getDiscoveryURL());

        return oDiscoveryURLs;
        
    }
    
    /**
     * Transform the information in a CMBusinessDescriptions object to the web service representation
     * of this data type.
     * 
     * @param oCMBusinessDescriptions The POJO information.
     * @return The Web service representation of the information.
     */
    public static BusinessDescriptionsType cmBusinessDescriptionsToBusinessDescriptionsType(CMBusinessDescriptions oCMBusinessDescriptions)
    {
        BusinessDescriptionsType oBusinessDescriptions = new BusinessDescriptionsType();
        
        if ((oCMBusinessDescriptions == null) ||
            (oCMBusinessDescriptions.getBusinessDescription() == null) ||
            (oCMBusinessDescriptions.getBusinessDescription().size() <= 0))
        {
            return null;
        }
        
        oBusinessDescriptions.getBusinessDescription().addAll(oCMBusinessDescriptions.getBusinessDescription());

        return oBusinessDescriptions;
    }
    
    /**
     * Transform the information in a CMContacts object to the web service representation
     * of this data type.
     * 
     * @param oCMContacts The POJO information.
     * @return The Web service representation of the information.
     */
    public static ContactsType cmContactsToContactsType(CMContacts oCMContacts)
    {
        ContactsType oContacts = new ContactsType();
        
        if ((oCMContacts == null) ||
            (oCMContacts.getContact() == null) ||
            (oCMContacts.getContact().size() <= 0))
        {
            return null;
        }
        
        for (CMContact oCMContact : oCMContacts.getContact())
        {
            ContactType oContact = new ContactType();
            oContact.setAddresses(cmAddressesToAddressesType(oCMContact.getAddresses()));
            oContact.setDescriptions(cmContactDescriptionsToContactDescriptionsType(oCMContact.getDescriptions()));
            oContact.setEmails(cmEmailsToEmailstype(oCMContact.getEmails()));
            oContact.setPersonNames(cmPersonNamesToPersonNamesType(oCMContact.getPersonNames()));
            oContact.setPhones(cmPhonesToPhonesType(oCMContact.getPhones()));
            oContacts.getContact().add(oContact);
        }
        
        if (oContacts.getContact().size() > 0)
        {
            return oContacts;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Transform the information in a CMBusinessServices object to the web service representation
     * of this data type.
     * 
     * @param oCMBusinessServices The POJO information.
     * @return The Web service representation of the information.
     */
    public static BusinessServicesType cmBusinessServicesToBusinessServicesType(CMBusinessServices oCMBusinessServices)
    {
        BusinessServicesType oBusinessServices = new BusinessServicesType();
        
        if ((oCMBusinessServices == null  ||
            (oCMBusinessServices.getBusinessService() == null) ||
            (oCMBusinessServices.getBusinessService().size() <= 0)))
        {
            return null;
        }
        
        for (CMBusinessService oCMBusinessService : oCMBusinessServices.getBusinessService())
        {
            BusinessServiceType oBusinessService = new BusinessServiceType();
            oBusinessService.setBindingTemplates(cmBindingTemplatesToBindingTemplatesType(oCMBusinessService.getBindingTemplates()));
            oBusinessService.setInternalWebService(oCMBusinessService.isInternalWebService());
            oBusinessService.setServiceKey(oCMBusinessService.getServiceKey());
            oBusinessService.setUniformServiceName(oCMBusinessService.getUniformServiceName());

            List<BindingDescriptionsType> oaBindingDescriptions = cmBindingDescriptionsToListBindingDescriptionsType(oCMBusinessService.getDescriptions());
            if ((oaBindingDescriptions != null) &&
                (oaBindingDescriptions.size() > 0))
            {
                oBusinessService.getDescriptions().addAll(oaBindingDescriptions);
            }
            
            List<BindingNamesType> oaBindingNames = cmBindingNamesToListBindingNamesType(oCMBusinessService.getNames());
            if ((oaBindingNames != null) &&
                (oaBindingNames.size() > 0))
            {
                oBusinessService.getNames().addAll(oaBindingNames);
            }
            
            oBusinessServices.getBusinessService().add(oBusinessService);
        }
        

        if (oBusinessServices.getBusinessService().size() > 0)
        {
            return oBusinessServices;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Transform the information in a CMBusinessEntity objects to the web service representation
     * of these data types.
     * 
     * @param oCMBusinessEntity The POJO information.
     * @return The Web service representation of the information.
     */
    public static BusinessEntityType cmBusinessEntityToBusinessEntityType(CMBusinessEntity oCMBusinessEntity)
    {
        BusinessEntityType oBusinessEntity = new BusinessEntityType();
        
        if (oCMBusinessEntity == null)
        {
            return null;
        }
        
        oBusinessEntity.setBusinessKey(oCMBusinessEntity.getBusinessKey());
        oBusinessEntity.setBusinessServices(cmBusinessServicesToBusinessServicesType(oCMBusinessEntity.getBusinessServices()));
        oBusinessEntity.setContacts(cmContactsToContactsType(oCMBusinessEntity.getContacts()));
        oBusinessEntity.setDescriptions(cmBusinessDescriptionsToBusinessDescriptionsType(oCMBusinessEntity.getDescriptions()));
        oBusinessEntity.setDiscoveryURLs(cmDiscoveryURLsToDiscoveryURLsType(oCMBusinessEntity.getDiscoveryURLs()));
        oBusinessEntity.setFederalHIE(oCMBusinessEntity.isFederalHIE());
        oBusinessEntity.setHomeCommunityId(oCMBusinessEntity.getHomeCommunityId());
        oBusinessEntity.setNames(cmBusinessNamesToBusinessNamesType(oCMBusinessEntity.getNames()));
        oBusinessEntity.setPublicKey(oCMBusinessEntity.getPublicKey());
        oBusinessEntity.setPublicKeyURI(oCMBusinessEntity.getPublicKeyURI());
        oBusinessEntity.setStates(cmStatesToStatesType(oCMBusinessEntity.getStates()));
        
        return oBusinessEntity;
    }
    
    /**
     * Transform the information in a CMBusinessEntities objects to the web service representation
     * of these data types.
     * 
     * @param oCMBusinessEntities The POJO information.
     * @return The Web service representation of the information.
     */
    public static BusinessEntitiesType cmBusinessEntitiesToBusinessEntitiesType(CMBusinessEntities oCMBusinessEntities)
    {
        BusinessEntitiesType oBusinessEntities = new BusinessEntitiesType();
        
        if ((oCMBusinessEntities != null) && 
            (oCMBusinessEntities.getBusinessEntity() != null) &&
            (oCMBusinessEntities.getBusinessEntity().size() > 0))
        {
            for (CMBusinessEntity oCMBusinessEntity : oCMBusinessEntities.getBusinessEntity())
            {
                BusinessEntityType oBusinessEntity = cmBusinessEntityToBusinessEntityType(oCMBusinessEntity);
                if (oBusinessEntity != null)
                {
                    oBusinessEntities.getBusinessEntity().add(oBusinessEntity);
                }
            }   // for (CMHomeCommunity oCommunity : oaHomeCommunity)
        }
        
        if (oBusinessEntities.getBusinessEntity().size() > 0)
        {
            return oBusinessEntities;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * This transforms the information in a CMBusinessServices object to a ServiceConnectionInfosType object.
     * 
     * @param oCMBusinessServices The information to be transformed.
     * @return The transformed information.
     */
    public static ServiceConnectionInfosType cmBusinessServicesToServiceConnectionInfos(CMBusinessServices oCMBusinessServices)
    {
        ServiceConnectionInfosType oServiceConnectionInfos = new ServiceConnectionInfosType();
        
        if ((oCMBusinessServices == null) ||
            (oCMBusinessServices.getBusinessService() == null) ||
            (oCMBusinessServices.getBusinessService().size() <= 0))
        {
            return null;
        }
        
        for (CMBusinessService oCMBusinessService : oCMBusinessServices.getBusinessService())
        {
            ServiceConnectionInfoType oServiceConnectionInfo = new ServiceConnectionInfoType();
            boolean bHaveData = false;
            
            // Uniform Service Name
            //---------------------
            if ((oCMBusinessService.getUniformServiceName() != null) &&
                (oCMBusinessService.getUniformServiceName().length() > 0))
            {
                oServiceConnectionInfo.setServiceName(oCMBusinessService.getUniformServiceName());
                bHaveData = true;
            }

            // URL
            // Note that the UDDI server is set up to have multiple URLs per UniformServiceName.  However
            // there should only be one.  We will use the first one we see...
            //--------------------------------------------------------------------------------------------
            if ((oCMBusinessService != null) &&
                (oCMBusinessService.getBindingTemplates() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().size() > 0) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0) != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL().length() > 0))
            {
                String sURL = oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL();
                oServiceConnectionInfo.setUrl(sURL);
                bHaveData = true;
                
                try
                {
                    URL oURL = new URL(sURL);

                    String sValue = oURL.getFile();
                    if ((sValue != null) && (sValue.length() > 0))
                    {
                        oServiceConnectionInfo.setFile(sValue);
                    }
                    else
                    {
                        oServiceConnectionInfo.setFile("");
                    }

                    sValue = oURL.getHost();
                    if ((sValue != null) && (sValue.length() > 0))
                    {
                        oServiceConnectionInfo.setHost(sValue);
                    }
                    else
                    {
                        oServiceConnectionInfo.setHost("");
                    }

                    sValue = oURL.getPath();
                    if ((sValue != null) && (sValue.length() > 0))
                    {
                        oServiceConnectionInfo.setPath(sValue);
                    }
                    else
                    {
                        oServiceConnectionInfo.setPath("");
                    }

                    sValue = oURL.getPort() + "";
                    if ((sValue != null) && (sValue.length() > 0))
                    {
                        oServiceConnectionInfo.setPort(sValue);
                    }
                    else
                    {
                        oServiceConnectionInfo.setPort("");
                    }

                    sValue = oURL.getProtocol();
                    if ((sValue != null) && (sValue.length() > 0))
                    {
                        oServiceConnectionInfo.setProtocol(sValue);
                    }
                    else
                    {
                        oServiceConnectionInfo.setProtocol("");
                    }
                }
                catch (Throwable t)
                {
                    String sErrorMessage = "Failed to decompose URL into its parts. URL='" +
                                           sURL + "'.  Error: " + t.getMessage();
                    log.error(sErrorMessage, t);
                }
                
            }   // if ((oCMBusinessService != null) &&
            
            if (bHaveData)
            {
                oServiceConnectionInfos.getServiceConnectionInfo().add(oServiceConnectionInfo);
            }
            
        }   // for (CMBusinessService oCMBusinessService : oCMBusinessServices.getBusinessService())
        
        return oServiceConnectionInfos;
    }
    
    /**
     * This transforms the information in a CMBusinessServices object to a ServiceConnectionInfoEndpointsType object.
     * 
     * @param oCMBusinessServices The information to be transformed.
     * @return The transformed information.
     */
    public static ServiceConnectionInfoEndpointsType cmBusinessServicesToServiceConnectionInfoEndpointsType(CMBusinessServices oCMBusinessServices)
    {
        ServiceConnectionInfoEndpointsType oServiceConnectionInfoEndpoints = new ServiceConnectionInfoEndpointsType();
        
        if ((oCMBusinessServices == null) ||
            (oCMBusinessServices.getBusinessService() == null) ||
            (oCMBusinessServices.getBusinessService().size() <= 0))
        {
            return null;
        }
        
        for (CMBusinessService oCMBusinessService : oCMBusinessServices.getBusinessService())
        {
            ServiceConnectionInfoEndpointType oServiceConnectionInfoEndpoint = new ServiceConnectionInfoEndpointType();
            boolean bHaveData = false;
            String sServiceName = "";
            
            // Uniform Service Name
            //---------------------
            if ((oCMBusinessService.getUniformServiceName() != null) &&
                (oCMBusinessService.getUniformServiceName().length() > 0))
            {
                oServiceConnectionInfoEndpoint.setServiceName(oCMBusinessService.getUniformServiceName());
                sServiceName = oCMBusinessService.getUniformServiceName();
                bHaveData = true;
            }

            // URL - Put into an EPR
            // Note that the UDDI server is set up to have multiple URLs per UniformServiceName.  However
            // there should only be one.  We will use the first one we see...
            
            //--------------------------------------------------------------------------------------------
            if ((oCMBusinessService != null) &&
                (oCMBusinessService.getBindingTemplates() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().size() > 0) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0) != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL() != null) &&
                (oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL().length() > 0))
            {
                String sURL = oCMBusinessService.getBindingTemplates().getBindingTemplate().get(0).getEndpointURL();
                EPRType oEpr = createEPR(sServiceName, sURL);
                if (oEpr != null)
                {
                    oServiceConnectionInfoEndpoint.setEPR(oEpr);
                    bHaveData = true;
                }
                
            }   // if ((oCMBusinessService != null) &&
            
            if (bHaveData)
            {
                oServiceConnectionInfoEndpoints.getServiceConnectionInfoEndpoint().add(oServiceConnectionInfoEndpoint);
            }
            
        }   // for (CMBusinessService oCMBusinessService : oCMBusinessServices.getBusinessService())
        
        return oServiceConnectionInfoEndpoints;
    }
    
    /**
     * This method creates an endpoint for the given service name and URL.
     * 
     * @param sServiceName The service name for the service.
     * @param sUrl The URL that is being used for the service.
     * @return The Endpoint reference to be returned.
     */
    public static EPRType createEPR(String sServiceName, String sUrl)
    {
        if ((sServiceName == null) || (sServiceName.length() <= 0))
        {
            return null;
        }
        
        EPRType oEpr = new EPRType();

        EndpointReferenceType oRefType = new EndpointReferenceType();
        
        oEpr.setEndpointReference(oRefType);

        CreateEPRRequestType oEprReq = new CreateEPRRequestType();
        log.info("Generating EPR for service: " + sServiceName);
        
        CMEprInfo oCMEprInfo = null;
        
        try
        {
            oCMEprInfo = CMEprUtil.createEPR(sServiceName);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve EPR info for service name: " + sServiceName + 
                                   ".  Error: " + e.getMessage();
            log.warn(sErrorMessage, e);
            
            // Set to default values.
            //-----------------------
            oCMEprInfo = new CMEprInfo();
            oCMEprInfo.setNamespacePrefix("");
            oCMEprInfo.setNamespaceURI("");
            oCMEprInfo.setPortName("");
            oCMEprInfo.setServiceName("");
            oCMEprInfo.setUniformServiceName("");
        }
        
        oEprReq.setNamespacePrefix(oCMEprInfo.getNamespacePrefix());
        oEprReq.setNamespaceURI(oCMEprInfo.getNamespaceURI());
        oEprReq.setPortName(oCMEprInfo.getPortName());
        oEprReq.setServiceName(oCMEprInfo.getServiceName());

        AttributedURI oAddress = new AttributedURI();
        oAddress.setValue(sUrl);
        oRefType.setAddress(oAddress);
        
        log.debug("address.getValue() -- " + oAddress.getValue());
        
        ServiceNameType oEprServiceName = new ServiceNameType();
        oEprServiceName.setPortName(oEprReq.getPortName());
        log.debug("createEPRIn.getPortName() -- " + oEprReq.getPortName());

        QName serviceNameValue = new QName(oEprReq.getNamespaceURI(), oEprReq.getServiceName(), oEprReq.getNamespacePrefix());
        log.debug("serviceNameValue -- " + oEprReq.getServiceName());
        log.debug("NamespacePrefix -- " + oEprReq.getNamespacePrefix());
        oEprServiceName.setValue(serviceNameValue);
        oRefType.setServiceName(oEprServiceName);

        log.info("createEndpoint() -- End");

        return oEpr;
    }
    
    
    /**
     * This transforms the information in a CMBusinessEntities structure to a ConnectionInfosType structure.
     * 
     * @param oCMBusinessEntities The information to be transformed.
     * @return The transformed information.
     */
    public static ConnectionInfosType cmBusinessEntitiesToConnectionInfosType(CMBusinessEntities oCMBusinessEntities)    
    {
        ConnectionInfosType oConnectionInfos = new ConnectionInfosType();
        
        if ((oCMBusinessEntities == null) ||
            (oCMBusinessEntities.getBusinessEntity() == null) ||
            (oCMBusinessEntities.getBusinessEntity().size() <= 0))
        {
            return null;
        }
        
        for (CMBusinessEntity oCMBusinessEntity : oCMBusinessEntities.getBusinessEntity())
        {
            ConnectionInfoType oConnectionInfo = new ConnectionInfoType();
            boolean bHaveData = false;

            // Home community information
            //---------------------------
            HomeCommunityType oHomeCommunity = cmBusinessEntityToHomeCommunity(oCMBusinessEntity);
            if (oHomeCommunity != null)
            {
                oConnectionInfo.setHomeCommunity(oHomeCommunity);
                bHaveData = true;
            }
            
            ServiceConnectionInfosType oServiceConnectionInfos = null;
            oServiceConnectionInfos = cmBusinessServicesToServiceConnectionInfos(oCMBusinessEntity.getBusinessServices());
            if (oServiceConnectionInfos != null)
            {
                oConnectionInfo.setServiceConnectionInfos(oServiceConnectionInfos);
                bHaveData = true;
            }
            
            if (bHaveData)
            {
                oConnectionInfos.getConnectionInfo().add(oConnectionInfo);
            }
        }
        
        if (oConnectionInfos.getConnectionInfo().size() > 0)
        {
            return oConnectionInfos;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * This transforms a CMBusinessEntities object into a ConectionInfoEndpointsType object.
     * 
     * @param oCMBusinessEntities The object to be transformed.
     * @return The transformed object.
     */
    public static ConnectionInfoEndpointsType cmBusinessEntitiesToConnectionInfoEndpointsType(CMBusinessEntities oCMBusinessEntities)
    {
        ConnectionInfoEndpointsType oConnectionInfoEndpoints = new ConnectionInfoEndpointsType();
        
        if ((oCMBusinessEntities == null) ||
            (oCMBusinessEntities.getBusinessEntity() == null) ||
            (oCMBusinessEntities.getBusinessEntity().size() <= 0))
        {
            return null;
        }
        
        for (CMBusinessEntity oCMBusinessEntity : oCMBusinessEntities.getBusinessEntity())
        {
            ConnectionInfoEndpointType oConnectionInfoEndpoint = new ConnectionInfoEndpointType();
            boolean bHaveData = false;

            // Home community information
            //---------------------------
            HomeCommunityType oHomeCommunity = cmBusinessEntityToHomeCommunity(oCMBusinessEntity);
            if (oHomeCommunity != null)
            {
                oConnectionInfoEndpoint.setHomeCommunity(oHomeCommunity);
                bHaveData = true;
            }
            
            ServiceConnectionInfoEndpointsType oServiceConnectionInfoEndpoints = null;
            oServiceConnectionInfoEndpoints = cmBusinessServicesToServiceConnectionInfoEndpointsType(oCMBusinessEntity.getBusinessServices());
            if (oServiceConnectionInfoEndpoints != null)
            {
                oConnectionInfoEndpoint.setServiceConnectionInfoEndpoints(oServiceConnectionInfoEndpoints);
                bHaveData = true;
            }
            
            if (bHaveData)
            {
                oConnectionInfoEndpoints.getConnectionInfoEndpoint().add(oConnectionInfoEndpoint);
            }
        }
        
        if (oConnectionInfoEndpoints.getConnectionInfoEndpoint().size() > 0)
        {
            return oConnectionInfoEndpoints;
        }
        else
        {
            return null;
        }
        
    }
    /**
     * This transforms a list of HomeCommunities into a list of HomeCommunityIds.
     * 
     * @param oaHomeCommunity The list of home communities.
     * @return The list of home community ids.
     */
    public static List<String> listHomeCommunityToListHomeCommunityId(List<HomeCommunityType> oaHomeCommunity)
    {
        ArrayList<String> saHomeCommunityId = new ArrayList<String>();
        String tempHomeCommunityId = "";
        if ((oaHomeCommunity != null) &&
            (oaHomeCommunity.size() > 0))
        {
            saHomeCommunityId = new ArrayList<String>();

            for (HomeCommunityType oHomeCommunity : oaHomeCommunity)
            {
                if ((oHomeCommunity.getHomeCommunityId() != null) &&
                    (oHomeCommunity.getHomeCommunityId().length() > 0))
                {
                    tempHomeCommunityId = formatHomeCommunityId(oHomeCommunity.getHomeCommunityId());
                    saHomeCommunityId.add(tempHomeCommunityId);
                }
            }
        }
        
        if (saHomeCommunityId.size() > 0)
        {
            return saHomeCommunityId;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * This method take home community id as input verifies if the home community id consists of urn:id: at the begining and strip it off
     * @param homeCommunityId
     * @return String
     */
    public static String formatHomeCommunityId(String homeCommunityId)
    {
        String homeCommId = "";
        if(homeCommunityId.contains("urn:id:"))
        {
            homeCommId = homeCommunityId.substring("urn:id:".length(), homeCommunityId.length());
        } else 
        {
            homeCommId = homeCommunityId;
        }
        return homeCommId;
    }
    
}
