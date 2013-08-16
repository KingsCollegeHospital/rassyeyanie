package uk.nhs.kch.rassyeyanie.framework.processor;

import java.util.Collections;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import uk.nhs.kch.rassyeyanie.framework.util.CollectionUtils;

/**
 * Adapter class that adds message selection functionality to a Processor implementation.
 */
public class MessageSelectionAdapter extends AbstractHeaderMatchingTransformer {

    private List<Processor> processorList = Collections.emptyList();

    public List<Processor> getProcessorList() {
        return processorList;
    }

    public void setProcessorList(List<Processor> processorList) {
        this.processorList = CollectionUtils.unmodifiableList(processorList);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
    	for (Processor processor : processorList) {
          processor.process(exchange);
        }
    }
}
