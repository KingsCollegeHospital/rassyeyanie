package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class TranslatedTestMessage
    implements TestMessage
{
    
    private final String name;
    private String inputMessage;
    private String outputMessage;
    private final String output;
    
    public TranslatedTestMessage(String name, String output)
    {
        this.name = name;
        this.output = output;
    }
    
    @Override
    public void LoadResources(Class<?> testClass, PipeParser pipeParser)
        throws IOException, EncodingNotSupportedException, HL7Exception
    {
        this.inputMessage =
            TestUtils.getClassResourceStream(testClass, this.name);
        pipeParser.setValidationContext(new NoValidation());
        this.outputMessage =
            pipeParser
                .parse(TestUtils.getClassResourceStream(testClass, this.output))
                .encode();
        // (AbstractMessage)pipeParser.parse(
        // TestUtils.getClassResourceStream(testClass,
        // this.output)).getFieldSeparatorValue("|");
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
        return "Translated: " + this.name;
    }
    
}
