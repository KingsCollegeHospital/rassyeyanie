package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;

/**
 * Implementation of Transformer interface that does not modify the message in any way.
 */
@Deprecated
public class NullTransformer extends AbstractHeaderMatchingTransformer {

    @Override
    public void process(Exchange exchange) {
        // Do nothing with the Exchange
    }
}
