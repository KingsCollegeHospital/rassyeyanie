package uk.nhs.kch.rassyeyanie.framework.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.util.CollectionUtils;

/**
 */
public class CollectionUtilsTest {

    @Test
    public void testUnmodifiableListWithNull() {

        List<Object> result = CollectionUtils.unmodifiableList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnmodifiableListWithEmptyList() {
        List<Object> source = new ArrayList<Object>();

        List<Object> result = CollectionUtils.unmodifiableList(source);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUnmodifiableListWithPopulateList() {
        List<String> source = new ArrayList<String>();
        source.add("1");
        source.add("2");
        source.add("3");

        List<String> result = CollectionUtils.unmodifiableList(source);
        assertNotNull(result);
        assertArrayEquals(source.toArray(), result.toArray());
    }
}
