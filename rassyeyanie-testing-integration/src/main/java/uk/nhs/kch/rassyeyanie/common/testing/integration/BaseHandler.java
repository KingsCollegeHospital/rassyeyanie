package uk.nhs.kch.rassyeyanie.common.testing.integration;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.camel.component.hl7.HL7Converter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.hl7v2.model.Message;

/**
 */
public class BaseHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    private static final byte START_BYTE = 0x0b;
    private static final byte END_BYTE_1 = 0x1c;
    private static final byte END_BYTE_2 = 0x0d;

    protected Integer getLocalPort(IoSession session) {
        InetSocketAddress localAddress = (InetSocketAddress)session.getLocalAddress();
        return Integer.valueOf(localAddress.getPort());
    }

    protected Integer getRemotePort(IoSession session) {
        InetSocketAddress localAddress = (InetSocketAddress)session.getRemoteAddress();
        return Integer.valueOf(localAddress.getPort());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {

        logger.error(cause.getMessage(), cause);

        session.close(true);
    }

    protected void writeMessage(IoSession session, String message) {

        byte[] source = message.getBytes(Charset.defaultCharset());
        byte[] body = new byte[source.length + 3];

        System.arraycopy(source, 0, body, 1, source.length);

        body[0] = START_BYTE;
        body[body.length - 2] = END_BYTE_1;
        body[body.length - 1] = END_BYTE_2;

        IoBuffer buffer = new SimpleBufferAllocator().allocate(body.length, true);
        buffer.put(body);
        buffer.flip();

        session.write(buffer);
    }

    protected String readMessage(IoBuffer buffer) {

    	byte[] original = buffer.array();

        int lastPos = ArrayUtils.lastIndexOf(original, END_BYTE_1);

        byte[] copy = new byte[lastPos - 1];

        System.arraycopy(original, 1, copy, 0, copy.length);

        return new String(copy).replaceAll("\r", "\n").trim();
    }

    protected void sendAck(IoSession session, String body) 
    		throws Exception {

    	Message message = HL7Converter.toMessage(body);
        Message ack = message.generateACK();
        String response = HL7Converter.toString(ack);

        logger.debug("Sending ACK \n\t{}", response.replaceAll("\r", "\n\t").trim());

        writeMessage(session, response);
    }

    protected void sendNack(IoSession session) {
        // TODO: Send proper NACK response
        writeMessage(session, "Not OK");
    }
}
