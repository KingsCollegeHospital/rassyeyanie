package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

@SuppressWarnings("deprecation")
@Deprecated
public class ObxDataFormatTest
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
                "TqInTcpHL7_O01_expected.dat");
        
        String inputBody =
            getClassResourceString(this.getClass(), "TqInTcpHL7_O01_input.dat");
        
        resultEndpoint.expectedBodiesReceived(expectedBodyZRF);
        template.sendBody(inputBody);
        
        resultEndpoint.assertIsSatisfied();
        
    }
    
    @Override
    protected RouteBuilder createRouteBuilder()
    {
        return new RouteBuilder() {
            @Override
            public void configure()
            {
                from("direct:start").process(new ObxDataFormat()).to(
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
