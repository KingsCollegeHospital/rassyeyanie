package uk.nhs.kch.rassyeyanie.common.testing.integration;

/**
 */
public interface ServerHandler {

    boolean isStillWaitingToSendMessages();

    void waitForMessages() throws InterruptedException;

    void setExpectedMessageCount(int expected);

    int getMessageCount();
}
