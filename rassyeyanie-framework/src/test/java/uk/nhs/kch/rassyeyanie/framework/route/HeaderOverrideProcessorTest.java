package uk.nhs.kch.rassyeyanie.framework.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;

/**
 */
public class HeaderOverrideProcessorTest
{
    
    private static final String SENDING_SYSTEM = "sending system";
    private static final String SENDING_APPLICATION = "sending application";
    private static final String RECEIVING_SYSTEM = "receiving system";
    private static final String RECEIVING_APPLICATION = "receiving application";
    
    private Exchange exchange;
    
    @Before
    public void setUp()
    {
        CamelContext camelContext = new DefaultCamelContext();
        this.exchange = new DefaultExchange(camelContext);
    }
    
    @Test
    public void testAllOverridesArePlacedInMessageHeader()
    {
        ListenerConfig config = new ListenerConfig();
        config.setSendingFacility(SENDING_SYSTEM);
        config.setSendingApplication(SENDING_APPLICATION);
        config.setReceivingFacility(RECEIVING_SYSTEM);
        config.setReceivingApplication(RECEIVING_APPLICATION);
        
        HeaderOverrideProcessor processor =
            new HeaderOverrideProcessor(config, 1);
        this.exchange.getIn().setHeader(
            HL7AdditionalConstants.HL7_INTERNAL_PATIENT_ID,
            "P123456");
        processor.process(this.exchange);
        
        Message message = this.exchange.getIn();
        
        assertEquals(
            SENDING_SYSTEM,
            message.getHeader(HL7Constants.HL7_SENDING_FACILITY));
        assertEquals(
            SENDING_APPLICATION,
            message.getHeader(HL7Constants.HL7_SENDING_APPLICATION));
        assertEquals(
            RECEIVING_SYSTEM,
            message.getHeader(HL7Constants.HL7_RECEIVING_FACILITY));
        assertEquals(
            RECEIVING_APPLICATION,
            message.getHeader(HL7Constants.HL7_RECEIVING_APPLICATION));
    }
    
    @Test
    public void testOnlySpecifiedOverridesAreUsed()
    {
        ListenerConfig config = new ListenerConfig();
        config.setSendingFacility(SENDING_SYSTEM);
        
        HeaderOverrideProcessor processor =
            new HeaderOverrideProcessor(config, 1);
        this.exchange.getIn().setHeader(
            HL7AdditionalConstants.HL7_INTERNAL_PATIENT_ID,
            "P123456");
        processor.process(this.exchange);
        
        Message message = this.exchange.getIn();
        
        assertEquals(
            SENDING_SYSTEM,
            message.getHeader(HL7Constants.HL7_SENDING_FACILITY));
        assertNull(message.getHeader(HL7Constants.HL7_SENDING_APPLICATION));
        assertNull(message.getHeader(HL7Constants.HL7_RECEIVING_FACILITY));
        assertNull(message.getHeader(HL7Constants.HL7_RECEIVING_APPLICATION));
    }
    
    @Test
    public void HashingPatientIdModulo()
    {
        ListenerConfig config = new ListenerConfig();
        HeaderOverrideProcessor processor =
            new HeaderOverrideProcessor(config, 5);
        this.exchange.getIn().setHeader(
            HL7AdditionalConstants.HL7_INTERNAL_PATIENT_ID,
            "P123456");
        processor.process(this.exchange);
        
        Message message = this.exchange.getIn();
        
        assertEquals(
            "3",
            message.getHeader(HL7AdditionalConstants.HL7_PATIENT_GROUP));
    }
    
}
