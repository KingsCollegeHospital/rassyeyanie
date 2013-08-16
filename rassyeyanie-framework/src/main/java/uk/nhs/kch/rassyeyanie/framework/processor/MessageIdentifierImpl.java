package uk.nhs.kch.rassyeyanie.framework.processor;

import java.util.Collections;
import java.util.List;

import org.apache.camel.Header;
import org.apache.camel.component.hl7.HL7Constants;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;

public class MessageIdentifierImpl
    implements MessageIdentifier
{
    private List<String> sendingApplicationFilterList;
    private List<String> sendingFacilityFilterList;
    private List<String> receivingApplicationFilterList;
    private List<String> receivingFacilityFilterList;
    private List<String> messageTypeFilterList;
    private List<String> triggerEventFilterList;
    private List<String> externalPatientIdFilterList;
    private List<String> internalPatientIdFilterList;
    private boolean verifyMode;
    
    public MessageIdentifierImpl()
    {
        this.sendingApplicationFilterList = Collections.emptyList();
        this.sendingFacilityFilterList = Collections.emptyList();
        this.receivingApplicationFilterList = Collections.emptyList();
        this.receivingFacilityFilterList = Collections.emptyList();
        this.messageTypeFilterList = Collections.emptyList();
        this.triggerEventFilterList = Collections.emptyList();
        this.externalPatientIdFilterList = Collections.emptyList();
        this.internalPatientIdFilterList = Collections.emptyList();
        this.verifyMode = true;
    }
    
    @Override
    public
        boolean
        shouldProcess(@Header(HL7Constants.HL7_SENDING_APPLICATION) String sendingApplication,
                      @Header(HL7Constants.HL7_SENDING_FACILITY) String sendingFacility,
                      @Header(HL7Constants.HL7_RECEIVING_APPLICATION) String receivingApplication,
                      @Header(HL7Constants.HL7_RECEIVING_FACILITY) String receivingFacility,
                      @Header(HL7Constants.HL7_MESSAGE_TYPE) String messageType,
                      @Header(HL7Constants.HL7_TRIGGER_EVENT) String triggerEvent,
                      @Header(HL7AdditionalConstants.HL7_EXTERNAL_PATIENT_ID) String externalPatientId,
                      @Header(HL7AdditionalConstants.HL7_INTERNAL_PATIENT_ID) String internalPatientId)
    {
        return this.isFiltered(
            sendingApplication,
            sendingFacility,
            receivingApplication,
            receivingFacility,
            messageType,
            triggerEvent,
            externalPatientId,
            internalPatientId);
    }
    
    private boolean isFiltered(String sendingApplication,
                               String sendingFacility,
                               String receivingApplication,
                               String receivingFacility,
                               String messageType,
                               String triggerEvent,
                               String externalPatientId,
                               String internalPatientId)
    {
        if (FilterRule.isRejected(
            sendingApplication,
            this.sendingApplicationFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            sendingFacility,
            this.sendingFacilityFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            receivingApplication,
            this.receivingApplicationFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            receivingFacility,
            this.receivingFacilityFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            messageType,
            this.messageTypeFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            triggerEvent,
            this.triggerEventFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            externalPatientId,
            this.externalPatientIdFilterList,
            this.getVerifyMode()))
            return false;
        
        if (FilterRule.isRejected(
            internalPatientId,
            this.internalPatientIdFilterList,
            this.getVerifyMode()))
            return false;
        
        return true;
    }
    
    public List<String> getSendingApplicationFilterList()
    {
        return this.sendingApplicationFilterList;
    }
    
    public
        void
        setSendingApplicationFilterList(List<String> sendingApplicationFilterList)
    {
        this.sendingApplicationFilterList = sendingApplicationFilterList;
    }
    
    public List<String> getSendingFacilityFilterList()
    {
        return this.sendingFacilityFilterList;
    }
    
    public void
        setSendingFacilityFilterList(List<String> sendingFacilityFilterList)
    {
        this.sendingFacilityFilterList = sendingFacilityFilterList;
    }
    
    public List<String> getReceivingApplicationFilterList()
    {
        return this.receivingApplicationFilterList;
    }
    
    public
        void
        setReceivingApplicationFilterList(List<String> receivingApplicationFilterList)
    {
        this.receivingApplicationFilterList = receivingApplicationFilterList;
    }
    
    public List<String> getReceivingFacilityFilterList()
    {
        return this.receivingFacilityFilterList;
    }
    
    public
        void
        setReceivingFacilityFilterList(List<String> receivingFacilityFilterList)
    {
        this.receivingFacilityFilterList = receivingFacilityFilterList;
    }
    
    public List<String> getMessageTypeFilterList()
    {
        return this.messageTypeFilterList;
    }
    
    public void setMessageTypeFilterList(List<String> messageTypeFilterList)
    {
        this.messageTypeFilterList = messageTypeFilterList;
    }
    
    public List<String> getTriggerEventFilterList()
    {
        return this.triggerEventFilterList;
    }
    
    public void setTriggerEventFilterList(List<String> triggerEventFilterList)
    {
        this.triggerEventFilterList = triggerEventFilterList;
    }
    
    public List<String> getExternalPatientIdFilterList()
    {
        return this.externalPatientIdFilterList;
    }
    
    public
        void
        setExternalPatientIdFilterList(List<String> externalPatientIdFilterList)
    {
        this.externalPatientIdFilterList = externalPatientIdFilterList;
    }
    
    public List<String> getInternalPatientIdFilterList()
    {
        return this.internalPatientIdFilterList;
    }
    
    public
        void
        setInternalPatientIdFilterList(List<String> internalPatientIdFilterList)
    {
        this.internalPatientIdFilterList = internalPatientIdFilterList;
    }
    
    @Override
    public boolean getVerifyMode()
    {
        return this.verifyMode;
    }
    
    @Override
    public void setVerifyMode(boolean verifyMode)
    {
        this.verifyMode = verifyMode;
    }
    
}
