package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

public class ResourceLoader
{
    
    public static MessageResource getMessageResource(String sourceDataFile)
        throws IOException, EncodingNotSupportedException, HL7Exception
    {
        String messageText = loadResource(sourceDataFile);
        MessageResource messageResource = new MessageResource(messageText);
        messageResource.init();
        return messageResource;
    }
    
    private static String loadResource(String resourceFile)
        throws IOException
    {
        return getClassResourceStream(resourceFile);
    }
    
    private static String getClassResourceStream(String string)
        throws IOException
    {
        return IOUtils
            .toString(
                ResourceLoader.class.getClassLoader().getResourceAsStream(
                    string))
            .replace('\n', '\r')
            .replace("\r\r", "\r");
    }
}
