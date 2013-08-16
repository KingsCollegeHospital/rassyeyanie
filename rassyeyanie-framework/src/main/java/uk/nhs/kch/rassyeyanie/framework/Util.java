package uk.nhs.kch.rassyeyanie.framework;

import ca.uhn.hl7v2.parser.CanonicalModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class Util
{
    
    public static PipeParser createVersionedParser(String version)
    {
        CanonicalModelClassFactory canonicalModelClassFactory =
            new CanonicalModelClassFactory(version);
        PipeParser pipeParser = new PipeParser(canonicalModelClassFactory);
        pipeParser.setValidationContext(new NoValidation());
        return pipeParser;
    }
    
    public static String filterStringNumbers(String str)
    {
        if (str == null || str.isEmpty())
        {
            return str;
        }
        else
        {
            return str
                .replaceAll("[,;=\\*\\-\\+/]", " ")
                .replaceAll("[^0-9 ]", "")
                .replaceAll(" +", " ")
                .trim();
        }
    }
}
