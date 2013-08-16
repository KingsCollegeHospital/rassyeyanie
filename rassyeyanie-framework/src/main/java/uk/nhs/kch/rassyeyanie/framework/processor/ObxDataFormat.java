package uk.nhs.kch.rassyeyanie.framework.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.model.Varies;

@Deprecated
public class ObxDataFormat
    implements Processor
{
    
    int numberOfObx;
    String output;
    String message;
    
    @Override
    public void process(Exchange exchange)
        throws Exception
    {
        // System.setProperty(Varies.INVALID_OBX2_TYPE_PROP, "ST");
        System.setProperty(Varies.DEFAULT_OBX2_TYPE_PROP, "TX");
        
        message = exchange.getIn().getBody(String.class);
        
        if (StringUtils.contains(message, "OBX|"))
        {
            this.output = message;
            for (numberOfObx = 0; numberOfObx <= (StringUtils.countMatches(
                message,
                "OBX|")); numberOfObx++)
            {
                
                if (message.contains("OBX|" + numberOfObx + "||"))
                    this.output =
                        StringUtils.defaultString(output.replace(
                            "OBX|" + numberOfObx + "||",
                            "OBX|" + numberOfObx + "|TX|"));
            }
            
            exchange.getIn().setBody(this.output);
        }
        // exchange.getIn().setBody(this.output);
    }
    
}
