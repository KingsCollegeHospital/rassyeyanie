package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

public class PassthroughTestMessage
    implements TestMessage
{
    
    private final String name;
    private String inputMessage;
    private String outputMessage;
    
    public PassthroughTestMessage(String name)
    {
        this.name = name;
    }
    
    @Override
    public void LoadResources(Class<?> testClass, PipeParser pipeParser)
        throws IOException, EncodingNotSupportedException, HL7Exception
    {
        this.inputMessage =
            TestUtils.getClassResourceStream(testClass, this.name);
        this.outputMessage = pipeParser.parse(this.inputMessage).encode();
    }
    
    @Override
    public String getName()
    {
        return this.name;
    }
    
    @Override
    public String getInputMessage()
    {
        return this.inputMessage;
    }
    
    @Override
    public String getOutputMessage()
    {
        return this.outputMessage;
    }
    
    @Override
    public String toString()
    {
        return "Passthrough: " + this.name;
    }
    
}
