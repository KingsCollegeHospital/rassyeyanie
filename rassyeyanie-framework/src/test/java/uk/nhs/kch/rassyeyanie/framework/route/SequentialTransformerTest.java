package uk.nhs.kch.rassyeyanie.framework.route;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class SequentialTransformerTest
{
    @Test
    public void process()
        throws Exception
    {
        System.out.println("Searching for resources:");
        String resource = "ca/uhn/hl7v2/parser/eventmap/2.4.properties";
        InputStream in =
            GenericListener.class
                .getClassLoader()
                .getResourceAsStream(resource);
        
        Properties structures = null;
        
        structures = new Properties();
        structures.load(in);
        System.out.println(structures.size());
        
    }
}
