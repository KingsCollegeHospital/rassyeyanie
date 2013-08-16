package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;

/**
 */
public class HeaderOverrideProcessor
    implements Processor
{
    
    private final ListenerConfig listenerConfig;
    private final int workerTotal;
    
    public HeaderOverrideProcessor(ListenerConfig listenerConfig,
                                   int workerTotal)
    {
        this.listenerConfig = listenerConfig;
        this.workerTotal = workerTotal;
    }
    
    @Override
    public void process(Exchange exchange)
    {
        
        Message in = exchange.getIn();
        
        this.addHeader(
            in,
            HL7Constants.HL7_SENDING_FACILITY,
            this.listenerConfig.getSendingFacility());
        this.addHeader(
            in,
            HL7Constants.HL7_SENDING_APPLICATION,
            this.listenerConfig.getSendingApplication());
        this.addHeader(
            in,
            HL7Constants.HL7_RECEIVING_FACILITY,
            this.listenerConfig.getReceivingFacility());
        this.addHeader(
            in,
            HL7Constants.HL7_RECEIVING_APPLICATION,
            this.listenerConfig.getReceivingApplication());
        
        String patientInternalId =
            StringUtils.defaultString(in.getHeader(
                HL7AdditionalConstants.HL7_INTERNAL_PATIENT_ID,
                String.class));
        
        String patientExternalId =
            StringUtils.defaultString(in.getHeader(
                HL7AdditionalConstants.HL7_EXTERNAL_PATIENT_ID,
                String.class));
        
        this.addHeader(
            in,
            HL7AdditionalConstants.HL7_PATIENT_GROUP,
            this.getGroupId(
                patientInternalId + patientExternalId,
                this.workerTotal));
    }
    
    public String getGroupId(String field, int workerTotal)
    {
        if (StringUtils.isBlank(field))
        {
            return ((int) (Math.random() * workerTotal)) + "";
        }
        else
        {
            return Math.abs(field.hashCode() % workerTotal) + "";
        }
    }
    
    private void addHeader(Message message, String header, String value)
    {
        if (StringUtils.isNotEmpty(value))
        {
            message.setHeader(header, value);
        }
    }
}
