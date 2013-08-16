package uk.nhs.kch.rassyeyanie.framework.route;

import static org.mockito.Mockito.when;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.nhs.kch.rassyeyanie.framework.util.TestUtils;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class SenderResponseProcessorTest
{
    
    @Autowired
    private SenderResponseProcessor processor;
    
    @Mock
    private Exchange mockExchange;
    @Mock
    private Message mockMessage;
    
    @Autowired
    private TestUtils testUtils;
    
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = ResponseException.class)
    public void testResponseBodyNotAString()
    {
        when(this.mockExchange.getIn()).thenReturn(this.mockMessage);
        when(this.mockMessage.getBody()).thenReturn(Integer.valueOf(7));
        
        this.processor.process(this.mockExchange);
    }
    
    @Test(expected = ResponseException.class)
    public void testResponseBodyIsNotAValidHL7String()
    {
        when(this.mockExchange.getIn()).thenReturn(this.mockMessage);
        when(this.mockMessage.getBody()).thenReturn("invalid");
        
        this.processor.process(this.mockExchange);
    }
    
    @Test
    public void testResponseBodyIsACKString()
        throws Exception
    {
        
        String expectedAck =
            this.testUtils.readHL7File("PimsInTcpHL7_A31_ACK_494268264.dat");
        
        when(this.mockExchange.getIn()).thenReturn(this.mockMessage);
        when(this.mockMessage.getBody()).thenReturn(expectedAck);
        
        this.processor.process(this.mockExchange);
    }
    
    @Test(expected = ResponseException.class)
    public void testResponseBodyIsNACKString()
        throws Exception
    {
        
        String expectedAck =
            this.testUtils.readHL7File("PimsInTcpHL7_A31_NACK_494268264.dat");
        
        when(this.mockExchange.getIn()).thenReturn(this.mockMessage);
        when(this.mockMessage.getBody()).thenReturn(expectedAck);
        
        this.processor.process(this.mockExchange);
    }
}
