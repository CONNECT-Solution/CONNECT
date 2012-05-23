
package org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArtificialDentition.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ArtificialDentition">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="TID10a"/>
 *     &lt;enumeration value="TID10i"/>
 *     &lt;enumeration value="TID10p"/>
 *     &lt;enumeration value="TID10pd"/>
 *     &lt;enumeration value="TID10pm"/>
 *     &lt;enumeration value="TID11a"/>
 *     &lt;enumeration value="TID11i"/>
 *     &lt;enumeration value="TID11p"/>
 *     &lt;enumeration value="TID11pd"/>
 *     &lt;enumeration value="TID11pm"/>
 *     &lt;enumeration value="TID12a"/>
 *     &lt;enumeration value="TID12i"/>
 *     &lt;enumeration value="TID12p"/>
 *     &lt;enumeration value="TID12pd"/>
 *     &lt;enumeration value="TID12pm"/>
 *     &lt;enumeration value="TID13a"/>
 *     &lt;enumeration value="TID13i"/>
 *     &lt;enumeration value="TID13p"/>
 *     &lt;enumeration value="TID13pd"/>
 *     &lt;enumeration value="TID13pm"/>
 *     &lt;enumeration value="TID14a"/>
 *     &lt;enumeration value="TID14i"/>
 *     &lt;enumeration value="TID14p"/>
 *     &lt;enumeration value="TID14pd"/>
 *     &lt;enumeration value="TID14pm"/>
 *     &lt;enumeration value="TID15a"/>
 *     &lt;enumeration value="TID15i"/>
 *     &lt;enumeration value="TID15p"/>
 *     &lt;enumeration value="TID15pd"/>
 *     &lt;enumeration value="TID15pm"/>
 *     &lt;enumeration value="TID16a"/>
 *     &lt;enumeration value="TID16i"/>
 *     &lt;enumeration value="TID16p"/>
 *     &lt;enumeration value="TID16pd"/>
 *     &lt;enumeration value="TID16pm"/>
 *     &lt;enumeration value="TID17a"/>
 *     &lt;enumeration value="TID17ad"/>
 *     &lt;enumeration value="TID17am"/>
 *     &lt;enumeration value="TID17i"/>
 *     &lt;enumeration value="TID17id"/>
 *     &lt;enumeration value="TID17im"/>
 *     &lt;enumeration value="TID17p"/>
 *     &lt;enumeration value="TID17pd"/>
 *     &lt;enumeration value="TID17pm"/>
 *     &lt;enumeration value="TID18a"/>
 *     &lt;enumeration value="TID18ad"/>
 *     &lt;enumeration value="TID18am"/>
 *     &lt;enumeration value="TID18i"/>
 *     &lt;enumeration value="TID18id"/>
 *     &lt;enumeration value="TID18im"/>
 *     &lt;enumeration value="TID18p"/>
 *     &lt;enumeration value="TID18pd"/>
 *     &lt;enumeration value="TID18pm"/>
 *     &lt;enumeration value="TID19a"/>
 *     &lt;enumeration value="TID19ad"/>
 *     &lt;enumeration value="TID19am"/>
 *     &lt;enumeration value="TID19i"/>
 *     &lt;enumeration value="TID19id"/>
 *     &lt;enumeration value="TID19im"/>
 *     &lt;enumeration value="TID19p"/>
 *     &lt;enumeration value="TID19pd"/>
 *     &lt;enumeration value="TID19pm"/>
 *     &lt;enumeration value="TID1a"/>
 *     &lt;enumeration value="TID1i"/>
 *     &lt;enumeration value="TID1p"/>
 *     &lt;enumeration value="TID1pd"/>
 *     &lt;enumeration value="TID1pm"/>
 *     &lt;enumeration value="TID20a"/>
 *     &lt;enumeration value="TID20i"/>
 *     &lt;enumeration value="TID20p"/>
 *     &lt;enumeration value="TID20pd"/>
 *     &lt;enumeration value="TID20pm"/>
 *     &lt;enumeration value="TID21a"/>
 *     &lt;enumeration value="TID21i"/>
 *     &lt;enumeration value="TID21p"/>
 *     &lt;enumeration value="TID21pd"/>
 *     &lt;enumeration value="TID21pm"/>
 *     &lt;enumeration value="TID22a"/>
 *     &lt;enumeration value="TID22i"/>
 *     &lt;enumeration value="TID22p"/>
 *     &lt;enumeration value="TID22pd"/>
 *     &lt;enumeration value="TID22pm"/>
 *     &lt;enumeration value="TID23a"/>
 *     &lt;enumeration value="TID23i"/>
 *     &lt;enumeration value="TID23p"/>
 *     &lt;enumeration value="TID23pd"/>
 *     &lt;enumeration value="TID23pm"/>
 *     &lt;enumeration value="TID24a"/>
 *     &lt;enumeration value="TID24i"/>
 *     &lt;enumeration value="TID24p"/>
 *     &lt;enumeration value="TID24pd"/>
 *     &lt;enumeration value="TID24pm"/>
 *     &lt;enumeration value="TID25a"/>
 *     &lt;enumeration value="TID25i"/>
 *     &lt;enumeration value="TID25p"/>
 *     &lt;enumeration value="TID25pd"/>
 *     &lt;enumeration value="TID25pm"/>
 *     &lt;enumeration value="TID26a"/>
 *     &lt;enumeration value="TID26i"/>
 *     &lt;enumeration value="TID26p"/>
 *     &lt;enumeration value="TID26pd"/>
 *     &lt;enumeration value="TID26pm"/>
 *     &lt;enumeration value="TID27a"/>
 *     &lt;enumeration value="TID27i"/>
 *     &lt;enumeration value="TID27p"/>
 *     &lt;enumeration value="TID27pd"/>
 *     &lt;enumeration value="TID27pm"/>
 *     &lt;enumeration value="TID28a"/>
 *     &lt;enumeration value="TID28i"/>
 *     &lt;enumeration value="TID28p"/>
 *     &lt;enumeration value="TID28pd"/>
 *     &lt;enumeration value="TID28pm"/>
 *     &lt;enumeration value="TID29a"/>
 *     &lt;enumeration value="TID29i"/>
 *     &lt;enumeration value="TID29p"/>
 *     &lt;enumeration value="TID29pd"/>
 *     &lt;enumeration value="TID29pm"/>
 *     &lt;enumeration value="TID2a"/>
 *     &lt;enumeration value="TID2i"/>
 *     &lt;enumeration value="TID2p"/>
 *     &lt;enumeration value="TID2pd"/>
 *     &lt;enumeration value="TID2pm"/>
 *     &lt;enumeration value="TID30a"/>
 *     &lt;enumeration value="TID30ad"/>
 *     &lt;enumeration value="TID30am"/>
 *     &lt;enumeration value="TID30i"/>
 *     &lt;enumeration value="TID30id"/>
 *     &lt;enumeration value="TID30im"/>
 *     &lt;enumeration value="TID30p"/>
 *     &lt;enumeration value="TID30pd"/>
 *     &lt;enumeration value="TID30pm"/>
 *     &lt;enumeration value="TID31a"/>
 *     &lt;enumeration value="TID31ad"/>
 *     &lt;enumeration value="TID31am"/>
 *     &lt;enumeration value="TID31i"/>
 *     &lt;enumeration value="TID31id"/>
 *     &lt;enumeration value="TID31im"/>
 *     &lt;enumeration value="TID31p"/>
 *     &lt;enumeration value="TID31pd"/>
 *     &lt;enumeration value="TID31pm"/>
 *     &lt;enumeration value="TID32a"/>
 *     &lt;enumeration value="TID32ad"/>
 *     &lt;enumeration value="TID32am"/>
 *     &lt;enumeration value="TID32i"/>
 *     &lt;enumeration value="TID32id"/>
 *     &lt;enumeration value="TID32im"/>
 *     &lt;enumeration value="TID32p"/>
 *     &lt;enumeration value="TID32pd"/>
 *     &lt;enumeration value="TID32pm"/>
 *     &lt;enumeration value="TID3a"/>
 *     &lt;enumeration value="TID3i"/>
 *     &lt;enumeration value="TID3p"/>
 *     &lt;enumeration value="TID3pd"/>
 *     &lt;enumeration value="TID3pm"/>
 *     &lt;enumeration value="TID4a"/>
 *     &lt;enumeration value="TID4i"/>
 *     &lt;enumeration value="TID4p"/>
 *     &lt;enumeration value="TID4pd"/>
 *     &lt;enumeration value="TID4pm"/>
 *     &lt;enumeration value="TID5a"/>
 *     &lt;enumeration value="TID5i"/>
 *     &lt;enumeration value="TID5p"/>
 *     &lt;enumeration value="TID5pd"/>
 *     &lt;enumeration value="TID5pm"/>
 *     &lt;enumeration value="TID6a"/>
 *     &lt;enumeration value="TID6i"/>
 *     &lt;enumeration value="TID6p"/>
 *     &lt;enumeration value="TID6pd"/>
 *     &lt;enumeration value="TID6pm"/>
 *     &lt;enumeration value="TID7a"/>
 *     &lt;enumeration value="TID7i"/>
 *     &lt;enumeration value="TID7p"/>
 *     &lt;enumeration value="TID7pd"/>
 *     &lt;enumeration value="TID7pm"/>
 *     &lt;enumeration value="TID8a"/>
 *     &lt;enumeration value="TID8i"/>
 *     &lt;enumeration value="TID8p"/>
 *     &lt;enumeration value="TID8pd"/>
 *     &lt;enumeration value="TID8pm"/>
 *     &lt;enumeration value="TID9a"/>
 *     &lt;enumeration value="TID9i"/>
 *     &lt;enumeration value="TID9p"/>
 *     &lt;enumeration value="TID9pd"/>
 *     &lt;enumeration value="TID9pm"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ArtificialDentition")
@XmlEnum
public enum ArtificialDentition {

    @XmlEnumValue("TID10a")
    TID_10_A("TID10a"),
    @XmlEnumValue("TID10i")
    TID_10_I("TID10i"),
    @XmlEnumValue("TID10p")
    TID_10_P("TID10p"),
    @XmlEnumValue("TID10pd")
    TID_10_PD("TID10pd"),
    @XmlEnumValue("TID10pm")
    TID_10_PM("TID10pm"),
    @XmlEnumValue("TID11a")
    TID_11_A("TID11a"),
    @XmlEnumValue("TID11i")
    TID_11_I("TID11i"),
    @XmlEnumValue("TID11p")
    TID_11_P("TID11p"),
    @XmlEnumValue("TID11pd")
    TID_11_PD("TID11pd"),
    @XmlEnumValue("TID11pm")
    TID_11_PM("TID11pm"),
    @XmlEnumValue("TID12a")
    TID_12_A("TID12a"),
    @XmlEnumValue("TID12i")
    TID_12_I("TID12i"),
    @XmlEnumValue("TID12p")
    TID_12_P("TID12p"),
    @XmlEnumValue("TID12pd")
    TID_12_PD("TID12pd"),
    @XmlEnumValue("TID12pm")
    TID_12_PM("TID12pm"),
    @XmlEnumValue("TID13a")
    TID_13_A("TID13a"),
    @XmlEnumValue("TID13i")
    TID_13_I("TID13i"),
    @XmlEnumValue("TID13p")
    TID_13_P("TID13p"),
    @XmlEnumValue("TID13pd")
    TID_13_PD("TID13pd"),
    @XmlEnumValue("TID13pm")
    TID_13_PM("TID13pm"),
    @XmlEnumValue("TID14a")
    TID_14_A("TID14a"),
    @XmlEnumValue("TID14i")
    TID_14_I("TID14i"),
    @XmlEnumValue("TID14p")
    TID_14_P("TID14p"),
    @XmlEnumValue("TID14pd")
    TID_14_PD("TID14pd"),
    @XmlEnumValue("TID14pm")
    TID_14_PM("TID14pm"),
    @XmlEnumValue("TID15a")
    TID_15_A("TID15a"),
    @XmlEnumValue("TID15i")
    TID_15_I("TID15i"),
    @XmlEnumValue("TID15p")
    TID_15_P("TID15p"),
    @XmlEnumValue("TID15pd")
    TID_15_PD("TID15pd"),
    @XmlEnumValue("TID15pm")
    TID_15_PM("TID15pm"),
    @XmlEnumValue("TID16a")
    TID_16_A("TID16a"),
    @XmlEnumValue("TID16i")
    TID_16_I("TID16i"),
    @XmlEnumValue("TID16p")
    TID_16_P("TID16p"),
    @XmlEnumValue("TID16pd")
    TID_16_PD("TID16pd"),
    @XmlEnumValue("TID16pm")
    TID_16_PM("TID16pm"),
    @XmlEnumValue("TID17a")
    TID_17_A("TID17a"),
    @XmlEnumValue("TID17ad")
    TID_17_AD("TID17ad"),
    @XmlEnumValue("TID17am")
    TID_17_AM("TID17am"),
    @XmlEnumValue("TID17i")
    TID_17_I("TID17i"),
    @XmlEnumValue("TID17id")
    TID_17_ID("TID17id"),
    @XmlEnumValue("TID17im")
    TID_17_IM("TID17im"),
    @XmlEnumValue("TID17p")
    TID_17_P("TID17p"),
    @XmlEnumValue("TID17pd")
    TID_17_PD("TID17pd"),
    @XmlEnumValue("TID17pm")
    TID_17_PM("TID17pm"),
    @XmlEnumValue("TID18a")
    TID_18_A("TID18a"),
    @XmlEnumValue("TID18ad")
    TID_18_AD("TID18ad"),
    @XmlEnumValue("TID18am")
    TID_18_AM("TID18am"),
    @XmlEnumValue("TID18i")
    TID_18_I("TID18i"),
    @XmlEnumValue("TID18id")
    TID_18_ID("TID18id"),
    @XmlEnumValue("TID18im")
    TID_18_IM("TID18im"),
    @XmlEnumValue("TID18p")
    TID_18_P("TID18p"),
    @XmlEnumValue("TID18pd")
    TID_18_PD("TID18pd"),
    @XmlEnumValue("TID18pm")
    TID_18_PM("TID18pm"),
    @XmlEnumValue("TID19a")
    TID_19_A("TID19a"),
    @XmlEnumValue("TID19ad")
    TID_19_AD("TID19ad"),
    @XmlEnumValue("TID19am")
    TID_19_AM("TID19am"),
    @XmlEnumValue("TID19i")
    TID_19_I("TID19i"),
    @XmlEnumValue("TID19id")
    TID_19_ID("TID19id"),
    @XmlEnumValue("TID19im")
    TID_19_IM("TID19im"),
    @XmlEnumValue("TID19p")
    TID_19_P("TID19p"),
    @XmlEnumValue("TID19pd")
    TID_19_PD("TID19pd"),
    @XmlEnumValue("TID19pm")
    TID_19_PM("TID19pm"),
    @XmlEnumValue("TID1a")
    TID_1_A("TID1a"),
    @XmlEnumValue("TID1i")
    TID_1_I("TID1i"),
    @XmlEnumValue("TID1p")
    TID_1_P("TID1p"),
    @XmlEnumValue("TID1pd")
    TID_1_PD("TID1pd"),
    @XmlEnumValue("TID1pm")
    TID_1_PM("TID1pm"),
    @XmlEnumValue("TID20a")
    TID_20_A("TID20a"),
    @XmlEnumValue("TID20i")
    TID_20_I("TID20i"),
    @XmlEnumValue("TID20p")
    TID_20_P("TID20p"),
    @XmlEnumValue("TID20pd")
    TID_20_PD("TID20pd"),
    @XmlEnumValue("TID20pm")
    TID_20_PM("TID20pm"),
    @XmlEnumValue("TID21a")
    TID_21_A("TID21a"),
    @XmlEnumValue("TID21i")
    TID_21_I("TID21i"),
    @XmlEnumValue("TID21p")
    TID_21_P("TID21p"),
    @XmlEnumValue("TID21pd")
    TID_21_PD("TID21pd"),
    @XmlEnumValue("TID21pm")
    TID_21_PM("TID21pm"),
    @XmlEnumValue("TID22a")
    TID_22_A("TID22a"),
    @XmlEnumValue("TID22i")
    TID_22_I("TID22i"),
    @XmlEnumValue("TID22p")
    TID_22_P("TID22p"),
    @XmlEnumValue("TID22pd")
    TID_22_PD("TID22pd"),
    @XmlEnumValue("TID22pm")
    TID_22_PM("TID22pm"),
    @XmlEnumValue("TID23a")
    TID_23_A("TID23a"),
    @XmlEnumValue("TID23i")
    TID_23_I("TID23i"),
    @XmlEnumValue("TID23p")
    TID_23_P("TID23p"),
    @XmlEnumValue("TID23pd")
    TID_23_PD("TID23pd"),
    @XmlEnumValue("TID23pm")
    TID_23_PM("TID23pm"),
    @XmlEnumValue("TID24a")
    TID_24_A("TID24a"),
    @XmlEnumValue("TID24i")
    TID_24_I("TID24i"),
    @XmlEnumValue("TID24p")
    TID_24_P("TID24p"),
    @XmlEnumValue("TID24pd")
    TID_24_PD("TID24pd"),
    @XmlEnumValue("TID24pm")
    TID_24_PM("TID24pm"),
    @XmlEnumValue("TID25a")
    TID_25_A("TID25a"),
    @XmlEnumValue("TID25i")
    TID_25_I("TID25i"),
    @XmlEnumValue("TID25p")
    TID_25_P("TID25p"),
    @XmlEnumValue("TID25pd")
    TID_25_PD("TID25pd"),
    @XmlEnumValue("TID25pm")
    TID_25_PM("TID25pm"),
    @XmlEnumValue("TID26a")
    TID_26_A("TID26a"),
    @XmlEnumValue("TID26i")
    TID_26_I("TID26i"),
    @XmlEnumValue("TID26p")
    TID_26_P("TID26p"),
    @XmlEnumValue("TID26pd")
    TID_26_PD("TID26pd"),
    @XmlEnumValue("TID26pm")
    TID_26_PM("TID26pm"),
    @XmlEnumValue("TID27a")
    TID_27_A("TID27a"),
    @XmlEnumValue("TID27i")
    TID_27_I("TID27i"),
    @XmlEnumValue("TID27p")
    TID_27_P("TID27p"),
    @XmlEnumValue("TID27pd")
    TID_27_PD("TID27pd"),
    @XmlEnumValue("TID27pm")
    TID_27_PM("TID27pm"),
    @XmlEnumValue("TID28a")
    TID_28_A("TID28a"),
    @XmlEnumValue("TID28i")
    TID_28_I("TID28i"),
    @XmlEnumValue("TID28p")
    TID_28_P("TID28p"),
    @XmlEnumValue("TID28pd")
    TID_28_PD("TID28pd"),
    @XmlEnumValue("TID28pm")
    TID_28_PM("TID28pm"),
    @XmlEnumValue("TID29a")
    TID_29_A("TID29a"),
    @XmlEnumValue("TID29i")
    TID_29_I("TID29i"),
    @XmlEnumValue("TID29p")
    TID_29_P("TID29p"),
    @XmlEnumValue("TID29pd")
    TID_29_PD("TID29pd"),
    @XmlEnumValue("TID29pm")
    TID_29_PM("TID29pm"),
    @XmlEnumValue("TID2a")
    TID_2_A("TID2a"),
    @XmlEnumValue("TID2i")
    TID_2_I("TID2i"),
    @XmlEnumValue("TID2p")
    TID_2_P("TID2p"),
    @XmlEnumValue("TID2pd")
    TID_2_PD("TID2pd"),
    @XmlEnumValue("TID2pm")
    TID_2_PM("TID2pm"),
    @XmlEnumValue("TID30a")
    TID_30_A("TID30a"),
    @XmlEnumValue("TID30ad")
    TID_30_AD("TID30ad"),
    @XmlEnumValue("TID30am")
    TID_30_AM("TID30am"),
    @XmlEnumValue("TID30i")
    TID_30_I("TID30i"),
    @XmlEnumValue("TID30id")
    TID_30_ID("TID30id"),
    @XmlEnumValue("TID30im")
    TID_30_IM("TID30im"),
    @XmlEnumValue("TID30p")
    TID_30_P("TID30p"),
    @XmlEnumValue("TID30pd")
    TID_30_PD("TID30pd"),
    @XmlEnumValue("TID30pm")
    TID_30_PM("TID30pm"),
    @XmlEnumValue("TID31a")
    TID_31_A("TID31a"),
    @XmlEnumValue("TID31ad")
    TID_31_AD("TID31ad"),
    @XmlEnumValue("TID31am")
    TID_31_AM("TID31am"),
    @XmlEnumValue("TID31i")
    TID_31_I("TID31i"),
    @XmlEnumValue("TID31id")
    TID_31_ID("TID31id"),
    @XmlEnumValue("TID31im")
    TID_31_IM("TID31im"),
    @XmlEnumValue("TID31p")
    TID_31_P("TID31p"),
    @XmlEnumValue("TID31pd")
    TID_31_PD("TID31pd"),
    @XmlEnumValue("TID31pm")
    TID_31_PM("TID31pm"),
    @XmlEnumValue("TID32a")
    TID_32_A("TID32a"),
    @XmlEnumValue("TID32ad")
    TID_32_AD("TID32ad"),
    @XmlEnumValue("TID32am")
    TID_32_AM("TID32am"),
    @XmlEnumValue("TID32i")
    TID_32_I("TID32i"),
    @XmlEnumValue("TID32id")
    TID_32_ID("TID32id"),
    @XmlEnumValue("TID32im")
    TID_32_IM("TID32im"),
    @XmlEnumValue("TID32p")
    TID_32_P("TID32p"),
    @XmlEnumValue("TID32pd")
    TID_32_PD("TID32pd"),
    @XmlEnumValue("TID32pm")
    TID_32_PM("TID32pm"),
    @XmlEnumValue("TID3a")
    TID_3_A("TID3a"),
    @XmlEnumValue("TID3i")
    TID_3_I("TID3i"),
    @XmlEnumValue("TID3p")
    TID_3_P("TID3p"),
    @XmlEnumValue("TID3pd")
    TID_3_PD("TID3pd"),
    @XmlEnumValue("TID3pm")
    TID_3_PM("TID3pm"),
    @XmlEnumValue("TID4a")
    TID_4_A("TID4a"),
    @XmlEnumValue("TID4i")
    TID_4_I("TID4i"),
    @XmlEnumValue("TID4p")
    TID_4_P("TID4p"),
    @XmlEnumValue("TID4pd")
    TID_4_PD("TID4pd"),
    @XmlEnumValue("TID4pm")
    TID_4_PM("TID4pm"),
    @XmlEnumValue("TID5a")
    TID_5_A("TID5a"),
    @XmlEnumValue("TID5i")
    TID_5_I("TID5i"),
    @XmlEnumValue("TID5p")
    TID_5_P("TID5p"),
    @XmlEnumValue("TID5pd")
    TID_5_PD("TID5pd"),
    @XmlEnumValue("TID5pm")
    TID_5_PM("TID5pm"),
    @XmlEnumValue("TID6a")
    TID_6_A("TID6a"),
    @XmlEnumValue("TID6i")
    TID_6_I("TID6i"),
    @XmlEnumValue("TID6p")
    TID_6_P("TID6p"),
    @XmlEnumValue("TID6pd")
    TID_6_PD("TID6pd"),
    @XmlEnumValue("TID6pm")
    TID_6_PM("TID6pm"),
    @XmlEnumValue("TID7a")
    TID_7_A("TID7a"),
    @XmlEnumValue("TID7i")
    TID_7_I("TID7i"),
    @XmlEnumValue("TID7p")
    TID_7_P("TID7p"),
    @XmlEnumValue("TID7pd")
    TID_7_PD("TID7pd"),
    @XmlEnumValue("TID7pm")
    TID_7_PM("TID7pm"),
    @XmlEnumValue("TID8a")
    TID_8_A("TID8a"),
    @XmlEnumValue("TID8i")
    TID_8_I("TID8i"),
    @XmlEnumValue("TID8p")
    TID_8_P("TID8p"),
    @XmlEnumValue("TID8pd")
    TID_8_PD("TID8pd"),
    @XmlEnumValue("TID8pm")
    TID_8_PM("TID8pm"),
    @XmlEnumValue("TID9a")
    TID_9_A("TID9a"),
    @XmlEnumValue("TID9i")
    TID_9_I("TID9i"),
    @XmlEnumValue("TID9p")
    TID_9_P("TID9p"),
    @XmlEnumValue("TID9pd")
    TID_9_PD("TID9pd"),
    @XmlEnumValue("TID9pm")
    TID_9_PM("TID9pm");
    private final String value;

    ArtificialDentition(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ArtificialDentition fromValue(String v) {
        for (ArtificialDentition c: ArtificialDentition.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
