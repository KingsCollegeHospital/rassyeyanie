package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;

public class IpmBadHeaderFix
    implements Processor
{
    
    @Override
    public void process(Exchange exchange)
        throws Exception
    {
        String message = exchange.getIn().getBody(String.class);
        
        if (StringUtils.startsWith(message, "MSH|||^~\\&"))
        {
            String output = StringUtils.replace(message, "MSH|||", "MSH|");
            exchange.getIn().setBody(output);
        }
    }
}
