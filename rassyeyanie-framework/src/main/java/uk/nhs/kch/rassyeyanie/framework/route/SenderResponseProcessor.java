package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.parser.Parser;

/**
 * {@link Processor} implementation that deals with responses from receivers. If
 * the response is anything but an ACK a {@link ResponseException} is thrown to
 * roll back any transactions.
 */
public class SenderResponseProcessor
    implements SenderResponseProcessorInterface
{
    
    private Parser parser;
    
    @Override
    public void process(Exchange exchange)
    {
        
        Object body = exchange.getIn().getBody();
        
        if (body instanceof String)
        {
            String acknowledgement = body.toString();
            
            if (acknowledgement.length() > 1)
            {
                try
                {
                    Message message = this.parseMessage(acknowledgement);
                    if (this.messageNotAccepted(message)) { throw new ResponseException(
                        "Message rejected"); }
                }
                catch (ResponseException ex)
                {
                    // Crude hack to deal with versionless acknowledgements
                    if (!StringUtils.contains(acknowledgement, "|AA")) { throw ex; }
                }
                
            }
            else
            {
                if (this.messageNotAccepted(acknowledgement)) { throw new ResponseException(
                    "Message rejected"); }
            }
        }
        else
        {
            // Response is not a HL7 ACK message so throw an exception
            throw new ResponseException("Response is not a HL7 ACK message");
        }
    }
    
    /**
     * Attempts to parse a response message into a HL7 message. Any parsing
     * errors are converted to a {@link ResponseException} and thrown to roll
     * back any transactions.
     */
    private Message parseMessage(String body)
    {
        try
        {
            return this.parser.parse(body);
        }
        catch (HL7Exception ex)
        {
            throw new ResponseException(ex);
        }
    }
    
    /**
     * @return True if the message is anything other than an ACK
     */
    private boolean messageNotAccepted(Message message)
    {
        
        if (message instanceof ACK)
        {
            ACK ack = (ACK) message;
            
            return !(StringUtils.equals(ack
                .getMSA()
                .getAcknowledgementCode()
                .getValue(), "AA") || StringUtils.equals(ack
                .getMSA()
                .getAcknowledgementCode()
                .getValue(), "CA"));
        }
        
        return true;
    }
    
    private boolean messageNotAccepted(String message)
    {
        return message.equals("0x06");
        
    }
    
    public Parser getParser()
    {
        return this.parser;
    }
    
    @Autowired
    public void setParser(Parser parser)
    {
        this.parser = parser;
    }
    
}
