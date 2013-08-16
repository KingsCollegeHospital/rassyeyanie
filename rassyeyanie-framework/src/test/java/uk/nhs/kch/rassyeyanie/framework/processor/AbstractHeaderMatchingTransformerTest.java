package uk.nhs.kch.rassyeyanie.framework.processor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.processor.AbstractHeaderMatchingTransformer;

/**
 */
public class AbstractHeaderMatchingTransformerTest {

    private static final String SENDER_1 = "Fred";
    private static final String SENDER_2 = "Bob";

    private static final String TRIGGER_1 = "woke up";
    private static final String TRIGGER_2 = "had a biscuit";

    private AbstractHeaderMatchingTransformer transformer;

    private Exchange exchange;
    private Message message;

    @Before
    public void setUp() {

        DefaultCamelContext camelContext = new DefaultCamelContext();

        message = new DefaultMessage();
        exchange = new DefaultExchange(camelContext);
        exchange.setIn(message);

        transformer = new DummyTransformer();
    }

    @Test
    public void testListsNotSet() {
        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testNullLists() {

    	
    	 transformer.setSendingApplicationFilterList(null);
    	 transformer.setSendingFacilityFilterList(null);
    	 transformer.setReceivingApplicationFilterList(null);
    	 transformer.setReceivingFacilityFilterList(null);
    	 transformer.setMessageTypeFilterList(null);
    	 transformer.setTriggerEventFilterList(null);
    	 
    	Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testEmptyLists() {

    	List<String> emptyList = Collections.emptyList();
	   	
        transformer.setSendingApplicationFilterList(emptyList);
	   	transformer.setSendingFacilityFilterList(emptyList);
	   	transformer.setReceivingApplicationFilterList(emptyList);
	   	transformer.setReceivingFacilityFilterList(emptyList);
	   	transformer.setMessageTypeFilterList(emptyList);
	   	transformer.setTriggerEventFilterList(emptyList);
        
        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testCorrectSender() {

        List<String> senders = Arrays.asList(SENDER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(null);
        
	   	message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testInterestedInSeveralSenders() {

        List<String> senders = Arrays.asList(SENDER_1, SENDER_2);       

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(null);
        
        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testWrongSender() {

        List<String> senders = Arrays.asList(SENDER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(null);
        
        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_2);

        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testNoSenderInHeader() {

        List<String> senders = Arrays.asList(SENDER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(null);
                
        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testCorrectTrigger() {

        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(null);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);
        
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testInterestedInSeveralTriggers() {

        List<String> triggers = Arrays.asList(TRIGGER_1, TRIGGER_2);

        transformer.setSendingApplicationFilterList(null);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);
        
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testWrongTrigger() {

        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(null);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);
	   	
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_2);

        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testNoTriggerInHeader() {

        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(null);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);
        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testCorrectSenderCorrectTrigger() {

        List<String> senders = Arrays.asList(SENDER_1);
        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);
	   	
        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_1);
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertTrue(predicate.matches(exchange));
    }

    @Test
    public void testWrongSenderWrongTrigger() {

        List<String> senders = Arrays.asList(SENDER_1);
        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);

        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_2);
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_2);

        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testCorrectSenderWrongTrigger() {

        List<String> senders = Arrays.asList(SENDER_1);
        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);

        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_1);
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_2);

        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    @Test
    public void testWrongSenderCorrectTrigger() {

        List<String> senders = Arrays.asList(SENDER_1);
        List<String> triggers = Arrays.asList(TRIGGER_1);

        transformer.setSendingApplicationFilterList(senders);
	   	transformer.setSendingFacilityFilterList(null);
	   	transformer.setReceivingApplicationFilterList(null);
	   	transformer.setReceivingFacilityFilterList(null);
	   	transformer.setMessageTypeFilterList(null);
	   	transformer.setTriggerEventFilterList(triggers);

        message.setHeader(HL7Constants.HL7_SENDING_APPLICATION, SENDER_2);
        message.setHeader(HL7Constants.HL7_TRIGGER_EVENT, TRIGGER_1);

        Predicate predicate = transformer.shouldProcessMessage();

        assertFalse(predicate.matches(exchange));
    }

    private static class DummyTransformer extends AbstractHeaderMatchingTransformer {
        @Override
        public void process(Exchange exchange) {
            // Do nothing
        }
    }
}
