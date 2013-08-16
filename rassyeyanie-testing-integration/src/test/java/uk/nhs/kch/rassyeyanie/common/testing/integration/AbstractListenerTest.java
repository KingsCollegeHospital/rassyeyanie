package uk.nhs.kch.rassyeyanie.common.testing.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public abstract class AbstractListenerTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractListenerTest.class);

    private static final int CONNECT_TIMEOUT = 10000;

    private static final Map<InetSocketAddress,NioSocketAcceptor> receivers = new HashMap<InetSocketAddress,NioSocketAcceptor>();

    protected abstract IoHandler createClientHandler(String message);

    /**
     * Loads the contents of a specified file into a String.  The file must be on the current classpath.
     */
    protected String loadFile(String filename) throws IOException {

        InputStream inputStream = getClass().getResourceAsStream(filename);

        try {
            String fileContents = IOUtils.toString(inputStream);

            return fileContents.replaceAll("\n", "\r");
        } finally {
            inputStream.close();
        }
    }

    /**
     * Starts one or more listeners.  There will be one unique listener for each port.
     * Each listener will be assigned a handler defined by the createServerHandler() method.
     * If the handler is shared then it should be made thread-safe.
     */
    protected static void startReceivers(IoHandler serverHandler, InetSocketAddress... addresses) {

        for (InetSocketAddress address : addresses) {

            NioSocketAcceptor acceptor = receivers.get(address);
            if (acceptor == null) {
                createReceiver(serverHandler, address);
            }
        }
    }

    private static void createReceiver(IoHandler serverHandler, InetSocketAddress address) {

        try {
            NioSocketAcceptor acceptor = new NioSocketAcceptor();
            acceptor.setReuseAddress(true);
            acceptor.setHandler(serverHandler);
            acceptor.bind(address);

            logger.info("Receiver started on port {}", address.getPort());

            receivers.put(address, acceptor);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            fail("Failed to start listener on port " + address.getPort());
        }
    }

    /**
     * Stops any currently running listeners.  This method can be blocked by the sendMessage() method.
     * Once all messages have been sent the listeners can be stopped.
     */
    protected static void stopReceivers() {

        // Dispose of any active listeners
        for (Map.Entry<InetSocketAddress,NioSocketAcceptor> entry : receivers.entrySet()) {
            IoAcceptor acceptor = entry.getValue();

            ServerHandler handler = (ServerHandler)acceptor.getHandler();

            try {
                while (handler.isStillWaitingToSendMessages()) {
                    Thread.sleep(500L);
                }
            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
            }

            acceptor.unbind();
            acceptor.dispose();

            logger.info("Receiver stopped on port {}", entry.getKey());
        }
    }

    protected void sendMessage(InetSocketAddress address, String message, ServerHandler serverHandler, int expected) throws InterruptedException {

        serverHandler.setExpectedMessageCount(expected);

        sendMessage(message, address);

        serverHandler.waitForMessages();

        int actual = serverHandler.getMessageCount();

        assertEquals("Number of messages received by server", expected, actual);
    }

    /**
     * Sends a message to a specified port.  The message itself is handled by the IoHandler
     * created by the createClientHandler() method.
     */
    private void sendMessage(String message, InetSocketAddress address) throws InterruptedException {

        IoHandler clientHandler = createClientHandler(message);

        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        connector.setHandler(clientHandler);

        IoSession session = createClientSession(connector, address);

        // wait until the message has been sent
        session.getCloseFuture().awaitUninterruptibly();

        connector.dispose();
    }

    private IoSession createClientSession(NioSocketConnector connector, InetSocketAddress address) throws InterruptedException {
        while (true) {
            try {
                ConnectFuture future = connector.connect(address);
                future.awaitUninterruptibly();
                return future.getSession();
            } catch (RuntimeIoException e) {
                logger.error(e.getMessage(), e);
                Thread.sleep(5000);
            }
        }
    }
}
