package uk.nhs.kch.rassyeyanie.framework.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

@Component
public class TestUtils
{
    @Value("${hl7.version}")
    private String hl7Version;
    
    @Value("${hl7.validate}")
    private boolean hl7Validate;
    
    @Value("${hl7.baseDir}")
    private String hl7Dir;
    
    public String readHL7File(String location)
        throws IOException
    {
        
        InputStream inputStream =
            this.getClass().getResourceAsStream("/hl7data/" + location);
        
        try
        {
            String fileContents = IOUtils.toString(inputStream);
            
            return fileContents.replaceAll("\n", "\r");
        }
        finally
        {
            inputStream.close();
        }
    }
    
    public static PipeParser getParser()
    {
        PipeParser pipeParser =
            new PipeParser(new CanonicalModelClassFactory("2.4"));
        pipeParser.setValidationContext(new NoValidation());
        return pipeParser;
    }
    
    public String parseHL7File(String location)
        throws IOException, HL7Exception
    {
        String unparsedString = this.readHL7File(location);
        Message message = parse(unparsedString, getParser());
        return encode(message, getParser());
    }
    
    static Message parse(String body, Parser parser)
        throws HL7Exception
    {
        return parser.parse(body);
    }
    
    static String encode(Message message, Parser parser)
        throws HL7Exception
    {
        return parser.encode(message);
    }
    
    public void assertAckEquals(String expectedAck, String actualAck)
    {
        String strippedExpectedAck = this.stripEntropyFromAck(expectedAck);
        String strippedActualAck = this.stripEntropyFromAck(actualAck);
        assertEquals(strippedExpectedAck, strippedActualAck);
    }
    
    private String stripEntropyFromAck(String ackMessage)
    {
        int firstEntropyStart = this.findNthString(5, "|", ackMessage);
        int firstEntropyEnd = this.findNthString(6, "|", ackMessage);
        int secondEntropyStart = this.findNthString(8, "|", ackMessage);
        int secondEntropyEnd = this.findNthString(9, "|", ackMessage);
        String strippedMessage =
            ackMessage.substring(0, firstEntropyStart + 1) +
                ackMessage.substring(firstEntropyEnd, secondEntropyStart + 1) +
                ackMessage.substring(secondEntropyEnd);
        
        return strippedMessage;
    }
    
    private int findNthString(int n, String search, String text)
    {
        int index = text.indexOf(search, 0);
        while (n-- > 0 && index != -1)
        {
            index = text.indexOf(search, index + 1);
        }
        return index;
    }
}
