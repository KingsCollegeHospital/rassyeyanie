package uk.nhs.kch.rassyeyanie.framework.processor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.nhs.kch.rassyeyanie.framework.processor.MessageIdentifierImpl;

public class MessageIdentifierTest
{
    @Test
    public void should_be_true_if_3of10_fields_match()
    {
        List<String> sendingApplication = new ArrayList<String>();
        List<String> receivingApplicationFilterList = new ArrayList<String>();
        List<String> triggerEventFilterList = new ArrayList<String>();
        sendingApplication.add("TEST");
        sendingApplication.add("TEST1");
        receivingApplicationFilterList.add("TEST2");
        triggerEventFilterList.add("TEST3");
        MessageIdentifierImpl messageIdentifier = new MessageIdentifierImpl();
        messageIdentifier.setSendingApplicationFilterList(sendingApplication);
        messageIdentifier
            .setReceivingApplicationFilterList(receivingApplicationFilterList);
        messageIdentifier.setTriggerEventFilterList(triggerEventFilterList);
        boolean result =
            messageIdentifier.shouldProcess(
                "TEST",
                null,
                "TEST2",
                null,
                null,
                "TEST3",
                null,
                null);
        assertEquals(result, true);
    }
    
    @Test
    public void should_be_false_if_3of10_fields_match_and_1_does_not()
    {
        List<String> sendingApplication = new ArrayList<String>();
        List<String> receivingApplicationFilterList = new ArrayList<String>();
        List<String> triggerEventFilterList = new ArrayList<String>();
        List<String> messageTypeFilterList = new ArrayList<String>();
        sendingApplication.add("TEST");
        receivingApplicationFilterList.add("TEST2");
        triggerEventFilterList.add("TEST3");
        messageTypeFilterList.add("ROAR");
        MessageIdentifierImpl messageIdentifier = new MessageIdentifierImpl();
        messageIdentifier.setSendingApplicationFilterList(sendingApplication);
        messageIdentifier
            .setReceivingApplicationFilterList(receivingApplicationFilterList);
        messageIdentifier.setTriggerEventFilterList(triggerEventFilterList);
        
        messageIdentifier.setMessageTypeFilterList(messageTypeFilterList);
        boolean result =
            messageIdentifier.shouldProcess(
                "TEST",
                null,
                "TEST2",
                null,
                "MOO",
                "TEST3",
                null,
                null);
        assertEquals(result, false);
    }
    
    @Test
    public void
        should_be_false_if_3of10_fields_match_and_1_does_not_in_xor_mode()
    {
        List<String> sendingApplication = new ArrayList<String>();
        List<String> receivingApplicationFilterList = new ArrayList<String>();
        List<String> triggerEventFilterList = new ArrayList<String>();
        List<String> messageTypeFilterList = new ArrayList<String>();
        sendingApplication.add("TEST");
        receivingApplicationFilterList.add("TEST2");
        triggerEventFilterList.add("TEST3");
        messageTypeFilterList.add("ROAR");
        MessageIdentifierImpl messageIdentifier = new MessageIdentifierImpl();
        messageIdentifier.setVerifyMode(true);
        messageIdentifier.setSendingApplicationFilterList(sendingApplication);
        messageIdentifier
            .setReceivingApplicationFilterList(receivingApplicationFilterList);
        messageIdentifier.setTriggerEventFilterList(triggerEventFilterList);
        
        messageIdentifier.setMessageTypeFilterList(messageTypeFilterList);
        boolean result =
            messageIdentifier.shouldProcess(
                "TEST",
                null,
                "TEST2",
                null,
                "MOO",
                "TEST3",
                null,
                null);
        assertEquals(result, false);
    }
    
    @Test
    public void should_be_true_if_1of10_fields_do_not_match_in_xor_mode()
    {
        List<String> sendingApplication = new ArrayList<String>();
        sendingApplication.add("TEST1");
        MessageIdentifierImpl messageIdentifier = new MessageIdentifierImpl();
        messageIdentifier.setVerifyMode(false);
        messageIdentifier.setSendingApplicationFilterList(sendingApplication);
        boolean result =
            messageIdentifier.shouldProcess(
                "TEST",
                null,
                "TEST2",
                null,
                "MOO",
                "TEST3",
                null,
                null);
        assertEquals(result, true);
    }
    
}
