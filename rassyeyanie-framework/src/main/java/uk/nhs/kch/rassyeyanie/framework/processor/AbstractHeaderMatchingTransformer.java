package uk.nhs.kch.rassyeyanie.framework.processor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.camel.Predicate;
import org.apache.camel.builder.Builder;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.component.hl7.HL7Constants;

import uk.nhs.kch.rassyeyanie.framework.util.CollectionUtils;

/**
 * Abstract base class for Transformer.  This class provides functionality for matching
 * sending applications and trigger events from HL7 message headers.
 */
public abstract class AbstractHeaderMatchingTransformer implements Transformer {

    private List<String> sendingApplicationFilterList = Collections.emptyList();
    private List<String> sendingFacilityFilterList = Collections.emptyList();
    private List<String> receivingApplicationFilterList = Collections.emptyList();
    private List<String> receivingFacilityFilterList = Collections.emptyList();
    private List<String> messageTypeFilterList = Collections.emptyList();
    private List<String> triggerEventFilterList = Collections.emptyList();

    @Override
    public Predicate shouldProcessMessage() {
    	
    	// MSH-3
    	Predicate sendingApplicationFilterPredicate = buildPredicate(HL7Constants.HL7_SENDING_APPLICATION, sendingApplicationFilterList);
    			
		// MSH-4
		Predicate sendingFacilityFilterPredicate = buildPredicate(HL7Constants.HL7_SENDING_FACILITY, sendingFacilityFilterList);

		// MSH-5
		Predicate receivingApplicationFilterPredicate = buildPredicate(HL7Constants.HL7_RECEIVING_APPLICATION, receivingApplicationFilterList);

		// MSH-6
		Predicate receivingFacilityFilterPredicate = buildPredicate(HL7Constants.HL7_RECEIVING_FACILITY, receivingFacilityFilterList);

		// MSH-9-1
		Predicate messageTypeFilterPredicate = buildPredicate(HL7Constants.HL7_MESSAGE_TYPE, messageTypeFilterList);
		
		// MSH-9-2
		Predicate triggerEventPredicate = buildPredicate(HL7Constants.HL7_TRIGGER_EVENT, triggerEventFilterList);

        ArrayList<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(sendingApplicationFilterPredicate);
        predicates.add(sendingFacilityFilterPredicate);
        predicates.add(receivingApplicationFilterPredicate);
        predicates.add(receivingFacilityFilterPredicate);
        predicates.add(messageTypeFilterPredicate);
        predicates.add(triggerEventPredicate);
        return PredicateBuilder.and(predicates);
    }

    private Predicate buildPredicate(String header, List<String> values) {
        if (values.isEmpty()) {
            return BooleanPredicate.TRUE;
        } else {
            return Builder.header(header).in(values.toArray());
        }
    }

	public List<String> getSendingApplicationFilterList() {
		return sendingApplicationFilterList;
	}

	public void setSendingApplicationFilterList(List<String> sendingApplicationFilterList) {
		this.sendingApplicationFilterList = CollectionUtils.unmodifiableList(sendingApplicationFilterList);
	}

	public List<String> getSendingFacilityFilterList() {
		return sendingFacilityFilterList;
	}
	
	public void setSendingFacilityFilterList(List<String> sendingFacilityFilterList) {
		this.sendingFacilityFilterList = CollectionUtils.unmodifiableList(sendingFacilityFilterList);
	}

	public List<String> getReceivingApplicationFilterList() {
		return receivingApplicationFilterList;
	}

	public void setReceivingApplicationFilterList(List<String> receivingApplicationFilterList) {
		this.receivingApplicationFilterList = CollectionUtils.unmodifiableList(receivingApplicationFilterList);
	}

	public List<String> getReceivingFacilityFilterList() {
		return receivingFacilityFilterList;
	}

	public void setReceivingFacilityFilterList(List<String> receivingFacilityFilterList) {
		this.receivingFacilityFilterList = CollectionUtils.unmodifiableList(receivingFacilityFilterList);
	}

	public List<String> getMessageTypeFilterList() {
		return messageTypeFilterList;
	}

	public void setMessageTypeFilterList(List<String> messageTypeFilterList) {
		this.messageTypeFilterList = CollectionUtils.unmodifiableList(messageTypeFilterList);
	}

	public List<String> getTriggerEventFilterList() {
		return triggerEventFilterList;
	}

	public void setTriggerEventFilterList(List<String> triggerEventFilterList) {
		this.triggerEventFilterList = CollectionUtils.unmodifiableList(triggerEventFilterList);
	}
}
