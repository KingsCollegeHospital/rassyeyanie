package uk.nhs.kch.rassyeyanie.framework.processor;

import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class InvalidEnvelopeFormatterRouteTest
    extends CamelTestSupport
{
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    
    @Test
    public void testIfUnmarshalIsWorkingandAssertIsSatisfied()
        throws Exception
    {
        String expectedBodyZRF =
            getClassResourceString(
                this.getClass(),
                "Correct_MidasDishMsg_Test.dat");
        
        String inputBody =
            getClassResourceString(
                this.getClass(),
                "Incorrect_MidasDishMsg_Test.dat");
        
        resultEndpoint.expectedBodiesReceived(expectedBodyZRF);
        template.sendBody(inputBody);
        
        resultEndpoint.assertIsSatisfied();
        
    }
    
    @Override
    protected RouteBuilder createRouteBuilder()
    {
        final InvalidEnvelopeFormatter invaliEnvelopeFormatter =
            new InvalidEnvelopeFormatter();
        
        return new RouteBuilder() {
            @Override
            public void configure()
            {
                from("direct:start").unmarshal(invaliEnvelopeFormatter).to(
                    "mock:result");
            }
        };
    }
    
    private static String getClassResourceString(Class<?> streamClass,
                                                 String string)
        throws IOException
    {
        return IOUtils
            .toString(streamClass.getClassLoader().getResourceAsStream(string))
            .replace('\n', '\r')
            .replace("\r\r", "\r");
    }
    
}
