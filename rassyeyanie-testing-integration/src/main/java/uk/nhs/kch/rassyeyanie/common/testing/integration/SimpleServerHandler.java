package uk.nhs.kch.rassyeyanie.common.testing.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class SimpleServerHandler extends BaseHandler implements ServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleServerHandler.class);

    private static final int TIMEOUT = 10;

    private final List<String> messagesReceived = Collections.synchronizedList(new ArrayList<String>());

    private final AtomicInteger count = new AtomicInteger(0);
    private AtomicInteger expected = new AtomicInteger(0);

    public List<String> getMessagesReceived() {
        return messagesReceived;
    }

    @Override
    public void messageReceived(IoSession session, Object message) {

        count.incrementAndGet();
        expected.decrementAndGet();

        try {
            if (message instanceof IoBuffer) {

                logger.debug("Receiver on port {} received message", getLocalPort(session));

                String s = readMessage((IoBuffer)message);

                messagesReceived.add(s);

                sendAck(session, s);

                return;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        sendNack(session);
    }

    @Override
    public void sessionCreated(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, TIMEOUT);
    }

    @Override
    public void messageSent(IoSession session, Object message) {
        count.decrementAndGet();
    }

    @Override
    public boolean isStillWaitingToSendMessages() {
        return count.intValue() > 0;
    }

    @Override
    public void setExpectedMessageCount(int expected) {
        this.expected = new AtomicInteger(expected);
    }

    @Override
    public int getMessageCount() {
        return messagesReceived.size();
    }

    public void waitForMessages(int seconds) throws InterruptedException {

        int timeout = seconds * 2;

        while (expected.intValue() > 0 && timeout > 0) {
            Thread.sleep(500L);
            timeout--;
        }
    }

    @Override
    public void waitForMessages() throws InterruptedException {
        waitForMessages(TIMEOUT);
    }

    public void reset() {
        messagesReceived.clear();
    }
}
