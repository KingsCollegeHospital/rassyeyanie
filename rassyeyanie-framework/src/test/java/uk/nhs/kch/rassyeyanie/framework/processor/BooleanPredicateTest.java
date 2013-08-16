package uk.nhs.kch.rassyeyanie.framework.processor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.camel.Predicate;
import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.processor.BooleanPredicate;

/**
 */
public class BooleanPredicateTest {

    @Test
    public void testTrue() {
        Predicate predicate = new BooleanPredicate(true);

        assertTrue(predicate.matches(null));
    }

    @Test
    public void testFalse() {
        Predicate predicate = new BooleanPredicate(false);

        assertFalse(predicate.matches(null));
    }
}
