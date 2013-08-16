package uk.nhs.kch.rassyeyanie.common.testing.unit;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

import java.io.IOException;

public interface TestMessage
{
    String getName();
    
    String getInputMessage();
    
    String getOutputMessage();
    
    void LoadResources(Class<?> testClass, PipeParser pipeParser)
        throws IOException, EncodingNotSupportedException, HL7Exception;
}
