package uk.nhs.kch.rassyeyanie.framework.processor;

public interface MessageIdentifier
{
    boolean shouldProcess(String sendingApplication,
                          String sendingFacility,
                          String receivingApplication,
                          String receivingFacility,
                          String messageType,
                          String triggerEvent,
                          String externalPatientId,
                          String internalPatientId);
    
    public boolean getVerifyMode();
    
    public void setVerifyMode(boolean verifyMode);
}
