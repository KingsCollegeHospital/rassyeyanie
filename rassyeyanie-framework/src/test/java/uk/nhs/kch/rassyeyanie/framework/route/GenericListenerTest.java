package uk.nhs.kch.rassyeyanie.framework.route;

import static org.junit.Assert.assertNotNull;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.nhs.kch.rassyeyanie.framework.util.TestUtils;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@Ignore
public class GenericListenerTest
{
    
    @Produce
    private ProducerTemplate template;
    
    @Autowired
    private TestUtils testUtils;
    
    @Autowired
    private GenericListener genericListener;
    
    // slow test
    @Test
    public void testHL7()
        throws Exception
    {
        String messageIn =
            this.testUtils.readHL7File("PimsInTcpHL7_A31_494268264.dat");
        String expectedAck =
            this.testUtils.readHL7File("PimsInTcpHL7_A31_ACK_494268264.dat");
        expectedAck = expectedAck.replaceAll("\r\r", "\r");
        String endpoint = this.getFirstActiveEndpoint();
        assertNotNull("No active endpoints configured", endpoint);
        
        String messageOut =
            (String) this.template.requestBody(endpoint, messageIn);
        this.testUtils.assertAckEquals(expectedAck, messageOut);
    }
    
    private String getFirstActiveEndpoint()
    {
        
        for (ListenerConfig listener : this.genericListener.getListeners())
        {
            if (listener.isAutoStart()) { return listener.getEndpoint(); }
        }
        
        return null;
    }
}
