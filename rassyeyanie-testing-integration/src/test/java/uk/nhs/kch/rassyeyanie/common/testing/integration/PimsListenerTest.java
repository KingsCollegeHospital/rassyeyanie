package uk.nhs.kch.rassyeyanie.common.testing.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;

import org.apache.camel.component.hl7.HL7Converter;
import org.apache.mina.core.service.IoHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 */
public class PimsListenerTest extends AbstractListenerTest {

    private static InetSocketAddress FUSE_LISTENER_PORT;
    private static InetSocketAddress RECEIVER_PORT;

    private static SimpleServerHandler serverHandler;
    private SimpleClientHandler clientHandler;

    @BeforeClass
    public static void init() {
        serverHandler = new SimpleServerHandler();
        FUSE_LISTENER_PORT = new InetSocketAddress("localhost", 7270);
        RECEIVER_PORT = new InetSocketAddress("localhost", 4050);

        startReceivers(serverHandler, RECEIVER_PORT);
    }

    @AfterClass
    public static void exit() throws Exception {
        stopReceivers();
    }

    @Before
    public void setUp() {
        serverHandler.reset();
    }

    @Test
    public void testRouteWithValidMessage() throws Exception {

        String message = loadFile("/hl7data/pims.txt");

        sendMessage(FUSE_LISTENER_PORT, message, serverHandler, 1);

        String received = serverHandler.getMessagesReceived().get(0);

        String messageStructure = HL7Converter.toMessage(message).printStructure();
        String receivedStructure = HL7Converter.toMessage(received).printStructure();

        assertEquals(messageStructure, receivedStructure);

        String response = clientHandler.getMessageReceived();
        assertTrue(response.endsWith("MSA|AA|Q123456789T123456789X123456"));
    }

    @Test
    public void testRouteWithInvalidMessage() throws Exception {

        String message = loadFile("/hl7data/broken.txt");

        sendMessage(FUSE_LISTENER_PORT, message, serverHandler, 0);

        String response = clientHandler.getMessageReceived();
        assertTrue(response.startsWith("MSH|^~\\&|FUSE|SERVICEMIX|ERR|ERR|"));
        assertTrue(response.endsWith("MSA|AE||HL7 NACK created"));
    }

    @Test
    public void testRouteWithMessageNotForApas() throws Exception {

        String message = loadFile("/hl7data/random.txt");

        sendMessage(FUSE_LISTENER_PORT, message, serverHandler, 0);

        String response = clientHandler.getMessageReceived();
        assertTrue(response.endsWith("MSA|AA|Q123456789T123456789X123456"));
    }

    @Override
    protected IoHandler createClientHandler(String message) {
        clientHandler = new SimpleClientHandler(message);
        return clientHandler;
    }
}
