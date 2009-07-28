package gov.hhs.fha.nhinc.subscription.repository;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.subscription.KeyValuePairListType;
import gov.hhs.fha.nhinc.common.subscription.KeyValuePairType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType;
import gov.hhs.fha.nhinc.common.subscription.ReferenceParametersType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType;
import gov.hhs.fha.nhinc.common.subscription.SubscriptionPolicyType;
import gov.hhs.fha.nhinc.common.subscription.TopicExpressionType;
import gov.hhs.fha.nhinc.subscription.repository.data.Community;
import gov.hhs.fha.nhinc.subscription.repository.data.Criterion;
import gov.hhs.fha.nhinc.subscription.repository.data.Patient;
import gov.hhs.fha.nhinc.subscription.repository.data.ReferenceParameter;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionCriteria;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionItem;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecord;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionReference;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionParticipant;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionPolicy;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionPolicyItem;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionRecordList;
import gov.hhs.fha.nhinc.subscription.repository.data.SubscriptionType;
import gov.hhs.fha.nhinc.subscription.repository.data.TopicExpression;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryFactory;
import gov.hhs.fha.nhinc.subscription.repository.service.SubscriptionRepositoryService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for subscription repository helpers
 * 
 * @author Neil Webb
 */
public abstract class BaseSubscriptionRepositoryHelper
{
    private static Log log = LogFactory.getLog(BaseSubscriptionRepositoryHelper.class);
    protected SubscriptionRepositoryService subscriptionRepositoryService = null;
    protected gov.hhs.fha.nhinc.common.subscription.ObjectFactory subscriptionRepositoryObjFact = null;
    public BaseSubscriptionRepositoryHelper() throws SubscriptionRepositoryException
    {
        subscriptionRepositoryObjFact = new gov.hhs.fha.nhinc.common.subscription.ObjectFactory();
        subscriptionRepositoryService = new SubscriptionRepositoryFactory().getSubscriptionRepositoryService();
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType storeSubscription(gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItem)
    {
        // Create subscription record
        SubscriptionRecord record = loadSubscriptionRecord(subscriptionItem);

        // Store the record
        SubscriptionReference subscriptionReference = subscriptionRepositoryService.storeSubscription(record);

        // Transform to subscription reference type
        gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReferenceType = loadSubscriptionReferenceType(subscriptionReference);

        return subscriptionReferenceType;
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType deleteSubscription(gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        // Create the subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(subscriptionReferenceType);

        // Retrieve the subscription record
        SubscriptionRecord record = subscriptionRepositoryService.retrieveBySubscriptionReference(subscriptionReference, getSubscriptionType());
        if (record != null)
        {
            // Delete the subscription
            subscriptionRepositoryService.deleteSubscription(record);
        }

        // Create the ack
        gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType ack = new AcknowledgementType();

        return ack;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType retrieveByCriteria(gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemsType subscriptionItemsType = new SubscriptionItemsType();
        if (subscriptionCriteriaType != null)
        {
            // Transform criteria
            SubscriptionCriteria subscriptionCriteria = loadSubscriptionCriteria(subscriptionCriteriaType);

            // Retrieve by criteria
            SubscriptionRecordList subscriptions = subscriptionRepositoryService.retrieveByCriteria(subscriptionCriteria, getSubscriptionType());

            // Transform results
            if (subscriptions != null)
            {
                for (SubscriptionRecord record : subscriptions)
                {
                    if ((record != null) && record.getSubscription() != null)
                    {
                        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
                        if (subscriptionItemType != null)
                        {
                            subscriptionItemsType.getSubscriptionItem().add(subscriptionItemType);
                        }
                    }
                }
            }
        }
        return subscriptionItemsType;
    }

    public gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType retrieveBySubscriptionReference(gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType = null;

        // Transform to subscription reference
        SubscriptionReference subscriptionReference = loadSubscriptionReference(subscriptionReferenceType);

        if(subscriptionReference != null)
        {
            if(log.isDebugEnabled())
            {
                log.debug("#### Retrieving subscription item by subscription reference.");
                log.debug("Subscription manager endpoint address: " + subscriptionReference.getSubscriptionManagerEndpointAddress());
                if(subscriptionReference.getReferenceParameters() != null)
                {
                    for(ReferenceParameter refParam : subscriptionReference.getReferenceParameters())
                    {
                        log.debug("Ref param namespace: " + refParam.getNamespace());
                        log.debug("Ref param namespace prefix: " + refParam.getNamespacePrefix());
                        log.debug("Ref param element name: " + refParam.getElementName());
                        log.debug("Ref param value: " + refParam.getValue());
                    }
                    
                }
            }
            
        }
        // Retrieve the subscription item
        SubscriptionRecord record = subscriptionRepositoryService.retrieveBySubscriptionReference(subscriptionReference, getSubscriptionType());

        // Transform to subscription item type
        if ((record != null) && (record.getSubscription() != null))
        {
            subscriptionItemType = loadSubscriptionItemType(record.getSubscription());
            log.debug("Subscription item retrieved");
        }
        else
        {
            // Return an empty subscription item until fault handling is added
            subscriptionItemType = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType();
            log.debug("No subscription item was retrieved");
        }
        return subscriptionItemType;
    }

    protected SubscriptionRecord loadSubscriptionRecord(gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType)
    {
        SubscriptionRecord record = new SubscriptionRecord();
        record.setType(getSubscriptionType());
        record.setSubscription(loadSubscriptionItem(subscriptionItemType));
        return record;
    }

    protected SubscriptionItem loadSubscriptionItem(gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType)
    {
        SubscriptionItem subscriptionItem = null;
        if (subscriptionItemType != null)
        {
            subscriptionItem = new SubscriptionItem();
            subscriptionItem.setSubscriber(loadSubscriber(subscriptionItemType.getSubscriber()));
            subscriptionItem.setSubscribee(loadSubscribee(subscriptionItemType.getSubscribee()));
            subscriptionItem.setSubscriptionCriteria(loadSubscriptionCriteria(subscriptionItemType.getSubscriptionCriteria()));
            subscriptionItem.setSubscriptionReference(loadSubscriptionReference(subscriptionItemType.getSubscriptionReference()));
            subscriptionItem.setParentSubscriptionReference(loadSubscriptionReference(subscriptionItemType.getParentSubscriptionReference()));
        }
        return subscriptionItem;
    }

    protected SubscriptionParticipant loadSubscriber(gov.hhs.fha.nhinc.common.subscription.SubscriberType subscriberType)
    {
        SubscriptionParticipant subscribee = null;
        if (subscriberType != null)
        {
            subscribee = new SubscriptionParticipant();
            subscribee.setNotificationEndpointAddress(subscriberType.getNotificationConsumerEndpointAddress());
            subscribee.setCommunity(loadCommunity(subscriberType.getCommunity()));
            subscribee.setUserAddress(subscriberType.getUserAddress());
        }
        return subscribee;
    }

    protected SubscriptionParticipant loadSubscribee(gov.hhs.fha.nhinc.common.subscription.SubscribeeType subscribeeType)
    {
        SubscriptionParticipant subscribee = null;
        if (subscribeeType != null)
        {
            subscribee = new SubscriptionParticipant();
            subscribee.setNotificationEndpointAddress(subscribeeType.getNotificationProducerEndpointAddress());
            subscribee.setCommunity(loadCommunity(subscribeeType.getCommunity()));
            subscribee.setUserAddress(subscribeeType.getUserAddress());
        }
        return subscribee;
    }

    protected SubscriptionCriteria loadSubscriptionCriteria(gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType)
    {
        SubscriptionCriteria subscriptionCriteria = null;
        if (subscriptionCriteriaType != null)
        {
            subscriptionCriteria = new SubscriptionCriteria();
            subscriptionCriteria.setSubscriberPatient(loadPatient(subscriptionCriteriaType.getSubscriberPatient()));
            subscriptionCriteria.setSubscribeePatient(loadPatient(subscriptionCriteriaType.getSubscribeePatient()));
            subscriptionCriteria.setCriteria(loadCriteria(subscriptionCriteriaType.getCriteria()));
            subscriptionCriteria.setTopicExpression(loadTopicExpression(subscriptionCriteriaType.getTopicExpression()));
            subscriptionCriteria.setSubscriptionPolicy(loadSubscriptionPolicy(subscriptionCriteriaType.getSubscriptionPolicy()));
        }
        return subscriptionCriteria;
    }
    
    protected TopicExpression loadTopicExpression(TopicExpressionType topicExpressionType)
    {
        TopicExpression topicExpression = null;
        if(topicExpressionType != null)
        {
            topicExpression = new TopicExpression();
            topicExpression.setDialect(topicExpressionType.getDialect());
            topicExpression.setTopicExpressionValue(topicExpressionType.getValue());
        }
        return topicExpression;
    }
    
    protected SubscriptionPolicy loadSubscriptionPolicy(SubscriptionPolicyType subscriptionPolicyType)
    {
        SubscriptionPolicy subscriptionPolicy = null;
        if((subscriptionPolicyType != null) && (subscriptionPolicyType.getGenericPolicyItems() != null) && (subscriptionPolicyType.getGenericPolicyItems().getKeyValuePair() != null))
        {
            subscriptionPolicy = new SubscriptionPolicy();
            for(KeyValuePairType keyValPairType : subscriptionPolicyType.getGenericPolicyItems().getKeyValuePair())
            {
                SubscriptionPolicyItem policyItem = new SubscriptionPolicyItem();
                policyItem.setKey(keyValPairType.getKey());
                policyItem.setValue(keyValPairType.getValue());
                subscriptionPolicy.getPolicyItems().add(policyItem);
            }
            
        }
        return subscriptionPolicy;
    }

    protected Patient loadPatient(gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType qualSubjectId)
    {
        Patient patient = null;
        if (qualSubjectId != null)
        {
            patient = new Patient();
            patient.setPatientId(qualSubjectId.getSubjectIdentifier());
            if(qualSubjectId.getAssigningAuthorityIdentifier() != null)
            {
                patient.setAssigningAuthority(loadAssigningAuthority(qualSubjectId.getAssigningAuthorityIdentifier()));
            }
        }
        return patient;
    }

    protected Community loadAssigningAuthority(String assigningAuthorityId)
    {
        Community assigningAuthority = null;
        if (assigningAuthorityId != null)
        {
            assigningAuthority = new Community();
            assigningAuthority.setCommunityId(assigningAuthorityId);
        }
        return assigningAuthority;
    }

    protected List<Criterion> loadCriteria(gov.hhs.fha.nhinc.common.subscription.CriteriaType criteriaType)
    {
        List<Criterion> criteria = null;
        if (criteriaType != null)
        {
            criteria = new ArrayList<Criterion>();
            List<gov.hhs.fha.nhinc.common.subscription.CriterionType> criterionTypeList = criteriaType.getCriterion();
            if (criterionTypeList != null)
            {
                for (gov.hhs.fha.nhinc.common.subscription.CriterionType criterionType : criterionTypeList)
                {
                    Criterion criterion = new Criterion();
                    criterion.setKey(criterionType.getKey());
                    criterion.setValue(criterionType.getValue());
                    criteria.add(criterion);
                }
            }
        }
        return criteria;
    }

    protected SubscriptionReference loadSubscriptionReference(gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReferenceType)
    {
        SubscriptionReference subscriptionReference = null;
        if (subscriptionReferenceType != null)
        {
            subscriptionReference = new SubscriptionReference();
            subscriptionReference.setSubscriptionManagerEndpointAddress(subscriptionReferenceType.getSubscriptionManagerEndpointAddress());
            if ((subscriptionReferenceType.getReferenceParameters() != null) && (subscriptionReferenceType.getReferenceParameters().getReferenceParameter() != null))
            {
                List<ReferenceParameter> referenceParameters = new ArrayList<ReferenceParameter>();
                for (gov.hhs.fha.nhinc.common.subscription.ReferenceParameterType refParamType : subscriptionReferenceType.getReferenceParameters().getReferenceParameter())
                {
                    ReferenceParameter refParam = new ReferenceParameter();
                    refParam.setNamespace(refParamType.getNamespace());
                    refParam.setNamespacePrefix(refParamType.getPrefix());
                    refParam.setElementName(refParamType.getElementName());
                    refParam.setValue(refParamType.getValue());
                    referenceParameters.add(refParam);
                }
                subscriptionReference.setReferenceParameters(referenceParameters);
            }
        }
        return subscriptionReference;
    }

    protected Community loadCommunity(gov.hhs.fha.nhinc.common.subscription.CommunityType communityType)
    {
        Community community = null;
        if (communityType != null)
        {
            community = new Community();
            community.setCommunityId(communityType.getId());
            community.setCommunityName(communityType.getName());
        }
        return community;
    }

    protected gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType loadSubscriptionReferenceType(SubscriptionReference subscriptionReference)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionReferenceType subscriptionReferenceType = null;
        if (subscriptionReference != null)
        {
            subscriptionReferenceType = subscriptionRepositoryObjFact.createSubscriptionReferenceType();
            subscriptionReferenceType.setSubscriptionManagerEndpointAddress(subscriptionReference.getSubscriptionManagerEndpointAddress());
            if (subscriptionReference.getReferenceParameters() != null)
            {
                ReferenceParametersType refParametersType = subscriptionRepositoryObjFact.createReferenceParametersType();
                subscriptionReferenceType.setReferenceParameters(refParametersType);
                List<ReferenceParameterType> refParameterTypeList = refParametersType.getReferenceParameter();
                for (ReferenceParameter refParam : subscriptionReference.getReferenceParameters())
                {
                    ReferenceParameterType refParamType = subscriptionRepositoryObjFact.createReferenceParameterType();
                    refParamType.setNamespace(refParam.getNamespace());
                    refParamType.setPrefix(refParam.getNamespacePrefix());
                    refParamType.setElementName(refParam.getElementName());
                    refParamType.setValue(refParam.getValue());
                    refParameterTypeList.add(refParamType);
                }
            }
        }
        return subscriptionReferenceType;
    }

    protected gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType loadSubscriptionItemType(SubscriptionItem subscriptionItem)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType subscriptionItemType = null;
        if (subscriptionItem != null)
        {
            subscriptionItemType = new gov.hhs.fha.nhinc.common.subscription.SubscriptionItemType();
            subscriptionItemType.setSubscriber(loadSubscriberType(subscriptionItem.getSubscriber()));
            subscriptionItemType.setSubscribee(loadSubscribeeType(subscriptionItem.getSubscribee()));
            subscriptionItemType.setSubscriptionCriteria(loadSubscriptionCriteriaType(subscriptionItem.getSubscriptionCriteria()));
            subscriptionItemType.setSubscriptionReference(loadSubscriptionReferenceType(subscriptionItem.getSubscriptionReference()));
            subscriptionItemType.setParentSubscriptionReference(loadSubscriptionReferenceType(subscriptionItem.getParentSubscriptionReference()));
        }
        return subscriptionItemType;
    }

    protected gov.hhs.fha.nhinc.common.subscription.SubscriberType loadSubscriberType(SubscriptionParticipant subscriber)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriberType subscriberType = null;
        if (subscriber != null)
        {
            subscriberType = new gov.hhs.fha.nhinc.common.subscription.SubscriberType();
            subscriberType.setNotificationConsumerEndpointAddress(subscriber.getNotificationEndpointAddress());
            subscriberType.setUserAddress(subscriber.getUserAddress());
            subscriberType.setCommunity(loadCommunityType(subscriber.getCommunity()));
        }
        return subscriberType;
    }

    protected gov.hhs.fha.nhinc.common.subscription.SubscribeeType loadSubscribeeType(SubscriptionParticipant subscriber)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscribeeType subscribeeType = null;
        if (subscriber != null)
        {
            subscribeeType = new gov.hhs.fha.nhinc.common.subscription.SubscribeeType();
            subscribeeType.setNotificationProducerEndpointAddress(subscriber.getNotificationEndpointAddress());
            subscribeeType.setUserAddress(subscriber.getUserAddress());
            subscribeeType.setCommunity(loadCommunityType(subscriber.getCommunity()));
        }
        return subscribeeType;
    }

    protected gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType loadSubscriptionCriteriaType(SubscriptionCriteria subscriptionCriteria)
    {
        gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType subscriptionCriteriaType = null;
        if (subscriptionCriteria != null)
        {
            subscriptionCriteriaType = new gov.hhs.fha.nhinc.common.subscription.SubscriptionCriteriaType();
            subscriptionCriteriaType.setSubscriberPatient(loadQualifiedSubjectId(subscriptionCriteria.getSubscriberPatient()));
            subscriptionCriteriaType.setSubscribeePatient(loadQualifiedSubjectId(subscriptionCriteria.getSubscribeePatient()));
            subscriptionCriteriaType.setCriteria(loadCriteriaType(subscriptionCriteria.getCriteria()));
            subscriptionCriteriaType.setTopicExpression(loadTopicExpressionType(subscriptionCriteria.getTopicExpression()));
            subscriptionCriteriaType.setSubscriptionPolicy(loadSubscriptionPolicyType(subscriptionCriteria.getSubscriptionPolicy()));
        }
        return subscriptionCriteriaType;
    }

    protected gov.hhs.fha.nhinc.common.subscription.CriteriaType loadCriteriaType(List<Criterion> criteria)
    {
        gov.hhs.fha.nhinc.common.subscription.CriteriaType criteriaType = null;
        if (criteria != null)
        {
            criteriaType = new gov.hhs.fha.nhinc.common.subscription.CriteriaType();
            List<gov.hhs.fha.nhinc.common.subscription.CriterionType> criterionTypeList = criteriaType.getCriterion();
            for (Criterion criterion : criteria)
            {
                gov.hhs.fha.nhinc.common.subscription.CriterionType criterionType = new gov.hhs.fha.nhinc.common.subscription.CriterionType();
                criterionType.setKey(criterion.getKey());
                criterionType.setValue(criterion.getValue());
                criterionTypeList.add(criterionType);
            }
        }
        return criteriaType;
    }
    
    protected TopicExpressionType loadTopicExpressionType(TopicExpression topicExpression)
    {
        TopicExpressionType topicExpressionType = null;
        if(topicExpression != null)
        {
            topicExpressionType = new TopicExpressionType();
            topicExpressionType.setDialect(topicExpression.getDialect());
            topicExpressionType.setValue(topicExpression.getTopicExpressionValue());
        }
        return topicExpressionType;
    }
    
    protected SubscriptionPolicyType loadSubscriptionPolicyType(SubscriptionPolicy subscriptionPolicy)
    {
        SubscriptionPolicyType subscriptionPolicyType = null;
        if((subscriptionPolicy != null) && (subscriptionPolicy.getPolicyItems() != null) && (!subscriptionPolicy.getPolicyItems().isEmpty()))
        {
            subscriptionPolicyType = new SubscriptionPolicyType();
            KeyValuePairListType pairsType = new KeyValuePairListType();
            subscriptionPolicyType.setGenericPolicyItems(pairsType);
            
            for(SubscriptionPolicyItem policyItem : subscriptionPolicy.getPolicyItems())
            {
                KeyValuePairType keyValPairType = new KeyValuePairType();
                keyValPairType.setKey(policyItem.getKey());
                keyValPairType.setValue(policyItem.getValue());
                pairsType.getKeyValuePair().add(keyValPairType);
            }
        }
        return subscriptionPolicyType;
    }

    protected gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType loadQualifiedSubjectId(Patient patient)
    {
        gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType qualSubjId = null;
        if (patient != null)
        {
            qualSubjId = new gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType();
            qualSubjId.setSubjectIdentifier(patient.getPatientId());
            if(patient.getAssigningAuthority() != null)
            {
                qualSubjId.setAssigningAuthorityIdentifier(patient.getAssigningAuthority().getCommunityId());
            }
        }
        return qualSubjId;
    }

    protected gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType loadHomeCommunity(Community community)
    {
        gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType homeCommunity = null;
        if (community != null)
        {
            homeCommunity = new gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType();
            homeCommunity.setHomeCommunityId(community.getCommunityId());
            homeCommunity.setName(community.getCommunityName());
        }
        return homeCommunity;
    }

    protected gov.hhs.fha.nhinc.common.subscription.CommunityType loadCommunityType(Community community)
    {
        gov.hhs.fha.nhinc.common.subscription.CommunityType communityType = null;
        if (community != null)
        {
            communityType = new gov.hhs.fha.nhinc.common.subscription.CommunityType();
            communityType.setId(community.getCommunityId());
            communityType.setName(community.getCommunityName());
        }
        return communityType;
    }

    protected abstract SubscriptionType getSubscriptionType();
}
