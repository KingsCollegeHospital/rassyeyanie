package uk.nhs.kch.rassyeyanie.common.testing.integration;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class SimpleClientHandler extends BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleClientHandler.class);

    private final String messageToSend;
    private String messageReceived;

    public String getMessageReceived() {
        return messageReceived;
    }

    public SimpleClientHandler(String messageToSend) {
        this.messageToSend = messageToSend;
    }

    @Override
    public void sessionOpened(IoSession session) {
        logger.debug("Sending message to port {}", getRemotePort(session));

        writeMessage(session, messageToSend);
    }

    @Override
    public void messageReceived(IoSession session, Object message) {

        if (message instanceof IoBuffer) {
            messageReceived = readMessage((IoBuffer)message);

            logger.debug("Sending to port {} received response \n\t{}", getRemotePort(session), messageReceived.replaceAll("\n", "\n\t").trim());
        }

        session.close(false);
    }
}
