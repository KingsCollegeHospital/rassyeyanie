package uk.nhs.kch.rassyeyanie.common.testing.integration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Code examples of how to connect to and query the JMX beans exposed by Fuse.
 */
public class JmxTest {

    private static final Logger logger = LoggerFactory.getLogger(JmxTest.class);

    private MBeanServerConnection connection;

    /**
     * The starting point for querying the JMX properties is the serviceUrl.  It is unique to the Fuse
     * instance you want to connect to and configured in several files in the fuse etc directory.  The
     * defaults are shown below:
     *
     * org.apache.karaf.management.cfg
     *     rmiRegistryPort = 1099
     *     rmiServerPort = 44444
     *     serviceUrl = service:jmx:rmi://localhost:${rmiServerPort}/jndi/rmi://localhost:${rmiRegistryPort}/karaf-${karaf.name}
     *
     * system.properties
     *     karaf.name=root
     */
    private JMXServiceURL getServiceURL() throws MalformedURLException {
        String serviceURL = "service:jmx:rmi://localhost:44444/jndi/rmi://localhost:1099/karaf-root";
        return new JMXServiceURL(serviceURL);
    }

    /**
     * The only required environment information is a username and password for the fuse instance.  By default
     * there is a user called 'smx' with the password 'smx'.  This should be different in a live Fuse instance.
     */
    private Map<String,Object> getEnvironment() {
        Map<String,Object> env = new HashMap<String, Object>();
        env.put(JMXConnector.CREDENTIALS, new String[] {"smx", "smx"});

        return env;
    }

    private MBeanServerConnection connect() throws IOException {
        Map<String,Object> env = getEnvironment();

        JMXServiceURL jmxUrl = getServiceURL();
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);

        return jmxConnector.getMBeanServerConnection();

    }

    @Before
    public void setUp() throws IOException {
        connection = connect();
    }

    /**
     * To find JMX beans you need to specify a domain and a list of attributes to match.  As shown in the code below,
     * these can use wildcards.  To reduce the number of results you can specify a type or the name
     * of a bean you are interested in.  In the example below we are only interested in camel routes.
     * <br/>
     * See the JavaDoc on {@link ObjectName} for more info on the name string
     */
    @Test
    public void testListAllRoutes() throws Exception {
        ObjectName objName = new ObjectName("*:*,type=routes");

        Set<ObjectInstance> mbeans = connection.queryMBeans(objName, null);
        printRouteNames(mbeans);
    }

    /**
     * Wildcards can also be used in the type and name elements.  Below is an example of searching
     * for all routes that contain the word "Transform" in their name.
     */
    @Test
    public void testListAllTransformRoutes() throws Exception {
        ObjectName objName = new ObjectName("*:*,type=routes,name=*Transform*");

        Set<ObjectInstance> mbeans = connection.queryMBeans(objName, null);
        printRouteNames(mbeans);
    }

    /**
     * For some reason the Spring configuration wraps route names in quotes.  To find a specific
     * route name you will have to add the quotes to the search string (or use wildcards).
     */
    @Test
    public void testFindSpecificRoute() throws Exception {
        ObjectName objName = new ObjectName("*:*,type=routes,name=\"ApasListenerRoute\"");

        Set<ObjectInstance> mbeans = connection.queryMBeans(objName, null);
        printRouteNames(mbeans);
    }

    @Test
    public void testListeningPortStatus() throws Exception {
        // Find all the routes that contain the word Listener in the name
        ObjectName listenerRouteQuery = new ObjectName("*:*,type=routes,name=*Listener*");

        Set<ObjectInstance> listenerRoutes = connection.queryMBeans(listenerRouteQuery, null);

        logger.debug("+------------------------------+------------+---------------------------------------------------------+");
        logger.debug("| Route                        | Status     | Endpoint                                                |");
        logger.debug("+------------------------------+------------+---------------------------------------------------------+");


        for (ObjectInstance listenerRoute : listenerRoutes) {
            ObjectName listenerRouteInfo = listenerRoute.getObjectName();

            // Build an ObjectName using information from the listener context and domain to
            // find any mina consumers used by the listener.
            String consumerLocation = listenerRouteInfo.getDomain() + ":*,context=" + listenerRouteInfo.getKeyProperty("context") + ",type=consumers,name=MinaConsumer*";

            ObjectName consumerQuery = new ObjectName(consumerLocation);
            Set<ObjectInstance> consumers = connection.queryMBeans(consumerQuery, null);

            for (ObjectInstance consumer : consumers) {
                ObjectName consumerInfo = consumer.getObjectName();

                String routeId = StringUtils.rightPad(connection.getAttribute(listenerRouteInfo, "RouteId").toString(), 28);
                String status = StringUtils.rightPad(connection.getAttribute(consumerInfo, "State").toString(), 10);
                String endpoint = connection.getAttribute(consumerInfo, "EndpointUri").toString();

                logger.debug("| " + routeId + " | " + status + " | " + endpoint + " |");
            }
        }

        logger.debug("+------------------------------+------------+---------------------------------------------------------+\n");
    }


    @Test
    public void testListTopLevelObjects() throws Exception {

        String[] domains = connection.getDomains();

        logger.debug("+------------------------------+");
        logger.debug("| Domain                       |");
        logger.debug("+------------------------------+");

        for (String domain : domains) {
            logger.debug("| " + StringUtils.rightPad(domain, 28) + " |");
        }

        logger.debug("+------------------------------+\n");
    }

    private void printRouteNames(Set<ObjectInstance> mbeans) throws Exception {

        logger.debug("+------------------------------+");
        logger.debug("| Route                        |");
        logger.debug("+------------------------------+");

        for (ObjectInstance objInstance : mbeans) {
            String routeId = StringUtils.rightPad(connection.getAttribute(objInstance.getObjectName(), "RouteId").toString(), 28);
            logger.debug("| " + routeId + " |");
        }

        logger.debug("+------------------------------+\n");
    }

}
