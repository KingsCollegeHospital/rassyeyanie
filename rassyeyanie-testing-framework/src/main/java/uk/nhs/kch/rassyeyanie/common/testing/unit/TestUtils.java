package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class TestUtils
{
    public static String getClassResourceStream(Class<?> testClass,
                                                String string)
        throws IOException
    {
        return IOUtils
            .toString(testClass.getClassLoader().getResourceAsStream(string))
            .replace('\n', '\r')
            .replace("\r\r", "\r");
    }
}
