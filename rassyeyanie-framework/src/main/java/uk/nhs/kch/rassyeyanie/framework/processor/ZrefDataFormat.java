package uk.nhs.kch.rassyeyanie.framework.processor;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;

public class ZrefDataFormat
    implements DataFormat
{
    // Post Processor
    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream)
        throws Exception
    {
        String body = exchange.getIn().getBody(String.class);
        body = replaceZRF(body, true);
        byte[] bodyBytes = body.getBytes();
        stream.write(bodyBytes);
    }
    
    // Pre Processor
    @Override
    public Object unmarshal(Exchange exchange, InputStream stream)
        throws Exception
    {
        String body =
            ExchangeHelper.convertToMandatoryType(
                exchange,
                String.class,
                stream);
        
        return this.replaceZRF(body, false);
    }
    
    // TODO: this is basically two functions in one. Please separate!
    public String replaceZRF(String message, boolean isMarshal)
    {
        return isMarshal ? message.replace("ZRF", "ZREF") : message.replace(
            "ZREF",
            "ZRF");
    }
}
