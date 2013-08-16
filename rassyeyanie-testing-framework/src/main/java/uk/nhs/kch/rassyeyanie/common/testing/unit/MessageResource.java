package uk.nhs.kch.rassyeyanie.common.testing.unit;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import uk.nhs.kch.rassyeyanie.framework.Util;

public class MessageResource
{
    private final String originalMessageText;
    private PipeParser pipeParser;
    private AbstractMessage message;
    
    public MessageResource(String messageText)
    {
        this.originalMessageText = messageText;
    }
    
    public MessageResource init()
        throws EncodingNotSupportedException, HL7Exception
    {
        this.pipeParser = Util.createVersionedParser("2.4");
        this.message = this.parseMessage();
        return this;
    }
    
    public AbstractMessage getMessage()
    {
        return this.message;
    }
    
    public String getOriginalMessageText()
    {
        return this.originalMessageText;
    }
    
    public String getMessageText()
        throws HL7Exception
    {
        return this.message.encode();
    }
    
    private AbstractMessage parseMessage()
        throws HL7Exception
    {
        return (AbstractMessage) this.pipeParser
            .parse(this.originalMessageText);
    }
}
