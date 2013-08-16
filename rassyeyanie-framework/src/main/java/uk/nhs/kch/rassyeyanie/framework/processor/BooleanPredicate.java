package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;

/**
 */
public class BooleanPredicate implements Predicate {

    public static final BooleanPredicate TRUE = new BooleanPredicate(true);
    public static final BooleanPredicate FALSE = new BooleanPredicate(false);

    private final boolean value;

    public BooleanPredicate(boolean value) {
        this.value = value;
    }

    @Override
    public boolean matches(Exchange exchange) {
        return value;
    }
}
