package uk.nhs.kch.rassyeyanie.framework.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class InvalidEnvelopeFormatterTest

{
    private String incorrectMidasMessage;
    private String correctMidas;
    private String incorrectDishMessage;
    private String correctDish;
    
    @Before
    public void prepare()
        throws Exception
    {
        incorrectMidasMessage =
            getClassResourceString(
                this.getClass(),
                "Incorrect_MidasDishMsg_Test.dat");
        
        incorrectDishMessage =
            getClassResourceString(
                this.getClass(),
                "Incorrect_MidasDishMsg_Test_2.dat");
        
        correctMidas =
            getClassResourceString(
                this.getClass(),
                "Correct_MidasDishMsg_Test.dat");
        
        correctDish =
            getClassResourceString(
                this.getClass(),
                "Correct_MidasDishMsg_Test_2.dat");
    }
    
    @Test
    public void testMidasIncomingMessage()
        throws Exception
    {
        InvalidEnvelopeFormatter invalidEnvelopeFormatter =
            new InvalidEnvelopeFormatter();
        
        assertEquals(
            correctMidas,
            invalidEnvelopeFormatter.formatMessage(incorrectMidasMessage));
        
        assertEquals(
            correctDish,
            invalidEnvelopeFormatter.formatMessage(incorrectDishMessage));
        
    }
    
    private static String getClassResourceString(Class<?> streamClass,
                                                 String string)
        throws IOException
    {
        return IOUtils
            .toString(streamClass.getClassLoader().getResourceAsStream(string))
            .replace('\n', '\r')
            .replace("\r\r", "\r");
    }
    
}
