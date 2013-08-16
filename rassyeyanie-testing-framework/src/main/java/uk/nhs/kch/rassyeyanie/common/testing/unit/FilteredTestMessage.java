package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import ca.uhn.hl7v2.parser.PipeParser;

public class FilteredTestMessage
    implements TestMessage
{
    
    private final String name;
    private String inputMessage;
    private String outputMessage;
    
    public FilteredTestMessage(String name)
    {
        this.name = name;
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
    public void LoadResources(Class<?> testClass, PipeParser pipeParser)
        throws IOException
    {
        this.inputMessage =
            TestUtils.getClassResourceStream(testClass, this.name);
    }
    
    @Override
    public String toString()
    {
        return "Filtered(" + this.name + ")";
    }
    
}
