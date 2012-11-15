package gov.hhs.fha.nhinc.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.junit.Test;

import com.google.common.base.Optional;

public class JaxbDocumentUtilsTest {

    @Test
    public void emptyEmptyList() {
        List<SlotType1> list = createList();
        Optional<String> result = JaxbDocumentUtils.findSlotType(list, "expected");
        assertEquals(Optional.absent(), result);
    }

    @Test
    public void emptyValueList() {
        List<SlotType1> list = createList();
        list.get(0).setValueList(new ValueListType());

        Optional<String> result = JaxbDocumentUtils.findSlotType(list, "expected");
        assertEquals(Optional.absent(), result);
    }

    @Test
    public void realValueExpected() {
        List<SlotType1> list = createList();
        ValueListType valueList = new ValueListType();
        list.get(0).setValueList(valueList);
        valueList.getValue().add("value");

        Optional<String> result = JaxbDocumentUtils.findSlotType(list, "expected");
        assertEquals(result.get(), "value");
    }

    private List<SlotType1> createList() {
        List<SlotType1> list = new ArrayList<SlotType1>();
        SlotType1 slot = new SlotType1();
        slot.setName("expected");
        list.add(slot);
        return list;
    }
}
