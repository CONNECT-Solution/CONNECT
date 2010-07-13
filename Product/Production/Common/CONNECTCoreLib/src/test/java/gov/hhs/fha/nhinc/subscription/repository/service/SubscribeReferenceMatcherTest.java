/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.repository.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rayj
 */
@Ignore
public class SubscribeReferenceMatcherTest {

    public SubscribeReferenceMatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void basicCompare() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void  compareWithSpaces() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'> 89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0 </nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void compareTwoReferenceParameters() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void compareTwoReferenceParametersWithNamespaces() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin1'>x</nhin:X>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin2'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin1'>x</nhin:X>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin2'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void compareTwoReferenceParametersWithNamespacesNoMatch() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin1'>x</nhin:X>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin2'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin2'>x</nhin:X>" +
                "           <nhin:X xmlns:nhin='http://www.hhs.gov/healthit/nhin1'>y</nhin:X>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void noMatchAddress() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8182/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void noMatchReferenceParameterValue() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>subid-1</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8182/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>subid-2</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void noMatchMissingReferenceParameter() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void noMatchEmptyInput() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void noMatchNullInput() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = null;

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void compareWithChildElement() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'><nhin:InnerValue xmlns:nhin='http://www.hhs.gov/healthit/nhin'>x</nhin:InnerValue></nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'><nhin:InnerValue xmlns:nhin='http://www.hhs.gov/healthit/nhin'>x</nhin:InnerValue></nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void compareWithChildElementNoMatch() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'><nhin:InnerValue xmlns:nhin='http://www.hhs.gov/healthit/nhin'>x</nhin:InnerValue></nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <nhin:SubscriptionId xmlns:nhin='http://www.hhs.gov/healthit/nhin'><b:InnerValue xmlns:b='http://www.hhs.gov/healthit/nhin'>y</b:InnerValue></nhin:SubscriptionId>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }

    @Test
    public void compareWithDifferentNamespaces() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <a:SubscriptionId xmlns:a='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</a:SubscriptionId>" +
                "           <a:Node1 xmlns:a='urn1'>1</a:Node1>" +
                "           <a:Node2 xmlns:a='urn2'>2</a:Node2>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <b:Node2 xmlns:b='urn2'>2</b:Node2>" +
                "           <b:SubscriptionId xmlns:b='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</b:SubscriptionId>" +
                "           <b:Node1 xmlns:b='urn1'>1</b:Node1>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }

    @Test
    public void compareComplexWithDifferentNamespaces() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <a:SubscriptionId xmlns:a='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</a:SubscriptionId>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v1</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v2</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn2'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v3</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Node2 xmlns:a='urn2'>2</a:Node2>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <a:SubscriptionId xmlns:a='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</a:SubscriptionId>" +
                "           <b:Parent xmlns:b='urn1'>" +
                "               <b:Child1 xmlns:b='urn2'>" +
                "                   <b:Child2 xmlns:b='urn2'>v1</b:Child2> " +
                "               </b:Child1>" +
                "           </b:Parent>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <z:Child2 xmlns:z='urn2'>v2</z:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn2'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v3</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Node2 xmlns:a='urn2'>2</a:Node2>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(true, result);
    }
    @Test
    public void compareComplexWithDifferentNamespacesNoMatch() {
        SubscribeReferenceMatcher matcher = new SubscribeReferenceMatcher();

        String subscribeSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <a:SubscriptionId xmlns:a='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</a:SubscriptionId>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v1</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v2</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn2'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v3</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Node2 xmlns:a='urn2'>2</a:Node2>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        String possibleMatchSubscriptionReferenceXml = "" +
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "   <wsnt:SubscriptionReference xmlns:wsa='http://www.w3.org/2005/08/addressing' xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2'>" +
                "       <wsa:Address>https://localhost:8181/SubscriptionManagerService/HiemUnsubscribe</wsa:Address>" +
                "       <wsa:ReferenceParameters>" +
                "           <a:SubscriptionId xmlns:a='http://www.hhs.gov/healthit/nhin'>89a438c1-e4fb-4358-8a1a-b8a2f1a3f0e0</a:SubscriptionId>" +
                "           <b:Parent xmlns:b='urn1'>" +
                "               <b:Child1 xmlns:b='urn2'>" +
                "                   <b:Child2 xmlns:b='urn2'>v1</b:Child2> " +
                "               </b:Child1>" +
                "           </b:Parent>" +
                "           <a:Parent xmlns:a='urn1'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <z:Child2 xmlns:z='urn2'>v2</z:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Parent xmlns:a='urn2'>" +
                "               <a:Child1 xmlns:a='urn2'>" +
                "                   <a:Child2 xmlns:a='urn2'>v</a:Child2> " +
                "               </a:Child1>" +
                "           </a:Parent>" +
                "           <a:Node2 xmlns:a='urn2'>2</a:Node2>" +
                "       </wsa:ReferenceParameters>" +
                "   </wsnt:SubscriptionReference>";

        boolean result = matcher.isSubscriptionReferenceMatch(subscribeSubscriptionReferenceXml, possibleMatchSubscriptionReferenceXml);
        assertEquals(false, result);
    }
}