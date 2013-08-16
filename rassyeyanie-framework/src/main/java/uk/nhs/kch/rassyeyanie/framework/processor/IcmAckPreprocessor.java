package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;

public class IcmAckPreprocessor
    implements Processor
{
    // fix icm acks which contain multiline error messages
    @Override
    public void process(Exchange exchange)
        throws Exception
    {
        String message = exchange.getIn().getBody(String.class);
        
        message = message.replace("\n", "");
        
        StringBuilder outputMessage = new StringBuilder();
        
        for (String line : StringUtils.split(message, '\r'))
        {
            if (line.length() > 3 && line.charAt(3) == '|' &&
                outputMessage.length() > 0)
            {
                outputMessage.append('\r');
            }
            else if (outputMessage.length() > 0)
            {
                outputMessage.append("\\.br\\");
            }
            
            outputMessage.append(line);
        }
        exchange.getIn().setBody(outputMessage);
    }
}
