package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Predicate;
import org.apache.camel.Processor;

/**
 */
public interface Transformer extends Processor {

    Predicate shouldProcessMessage();
}
