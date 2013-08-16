package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;
import uk.nhs.kch.rassyeyanie.framework.Util;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

/*
 * Message contexts:
 * Input message -- a read only SPoT
 * for translations to refer to while processing messages
 * Expected message -- a read only SPoT for
 * expected messages in comparisons
 * Output message -- this is a mutable object that eventually becomes
 * the 'actual' in asserts
 * 
 */
public class TestContext<T extends AbstractMessage>
{
    private final String inputSource;
    private final String outputSource;
    private final String expectedSource;
    private final Processor[] processors;
    private Class<? extends AbstractMessage> messageType;
    private T outputMessage;
    
    private String inputText;
    private String outputText;
    private String expectedText;
    private String originalInputText;
    
    private T inputMessage;
    private T expectedMessage;
    
    private PipeParser pipeParser;
    // exposed for instances where tests want to manually call translations
    private Exchange exchange;
    
    public TestContext(String inputSource,
                       String expectedSource,
                       Class<T> messageType,
                       T outputMessage,
                       Processor... processors)
    {
        this.inputSource = inputSource;
        this.outputSource = null;
        this.expectedSource = expectedSource;
        this.processors = processors;
        this.messageType = messageType;
        this.outputMessage = outputMessage;
    }
    
    public TestContext(String inputSource,
                       String expectedSource,
                       T outputMessage,
                       Processor... processors)
    {
        this.inputSource = inputSource;
        this.outputSource = null;
        this.expectedSource = expectedSource;
        this.processors = processors;
        this.outputMessage = outputMessage;
    }
    
    @SuppressWarnings("rawtypes")
    public static TestContextFactory getFactory()
    {
        return new TestContextFactory();
    }
    
    private static String getClassResourceStream(Class<?> streamClass,
                                                 String string)
        throws IOException
    {
        return IOUtils
            .toString(streamClass.getClassLoader().getResourceAsStream(string))
            .replace('\n', '\r')
            .replace("\r\r", "\r");
    }
    
    public void prepare()
        throws IOException, EncodingNotSupportedException, HL7Exception
    {
        this.pipeParser = Util.createVersionedParser("2.4");
        
        this.loadResources();
        this.parseMessages();
        this.prepareExchange();
        this.originalInputText = this.inputText;
        this.inputText = this.inputMessage.encode();
    }
    
    private void loadResources()
        throws IOException
    {
        this.inputText = this.loadResource(this.inputSource);
        if (this.outputSource != null)
        {
            this.setOutputText(this.loadResource(this.outputSource));
        }
        this.expectedText = this.loadResource(this.expectedSource);
    }
    
    private String loadResource(String resourceFile)
        throws IOException
    {
        return getClassResourceStream(this.getClass(), resourceFile);
    }
    
    @SuppressWarnings("unchecked")
    private void parseMessages()
        throws HL7Exception, EncodingNotSupportedException
    {
        this.inputMessage = (T) this.pipeParser.parse(this.inputText);
        // if we haven't been given an output container -- use intermediate
        // message
        if (this.outputMessage == null)
        {
            // for backwards compatibility -- this allows old translations
            // to be tested with new framework while we transition
            this.outputMessage =
                (T) this.pipeParser.parse(this.getOutputText() == null
                    ? this.inputText
                    : this.getOutputText());
            
        }
        
        if (this.messageType == null)
        {
            this.messageType = this.outputMessage.getClass();
        }
        this.expectedMessage = (T) this.pipeParser.parse(this.expectedText);
    }
    
    private void prepareExchange()
    {
        this.setExchange(new TestExchange());
        this.getExchange().getIn().setBody(this.outputMessage);
        this
            .getExchange()
            .getIn()
            .setHeader(
                HL7AdditionalConstants.HL7_SOURCE_MESSAGE,
                this.inputMessage);
    }
    
    public void runTest()
        throws Exception
    {
        for (Processor processor : this.processors)
        {
            processor.process(this.getExchange());
        }
    }
    
    @Override
    public String toString()
    {
        int processorLength = this.processors.length;
        String[] processorSimpleNames = new String[processorLength];
        for (int i = 0; i < processorLength; i++)
            processorSimpleNames[i] =
                this.processors[i].getClass().getSimpleName();
        
        String processorChain = StringUtils.join(processorSimpleNames, " -> ");
        return processorChain + " (" + this.messageType.getSimpleName() + ")";
    }
    
    public String getInputSource()
    {
        return this.inputSource;
    }
    
    public String getInputText()
    {
        return this.inputText;
    }
    
    public String getExpectedText()
    {
        return this.expectedText;
    }
    
    public T getInputMessage()
    {
        return this.inputMessage;
    }
    
    public T getExpectedMessage()
    {
        return this.expectedMessage;
    }
    
    // Alias to aide writing test code
    public T getActualResult()
    {
        return this.getOutputMessage();
    }
    
    public AbstractMessage getAbstractExpectedMessage()
    {
        return this.getExpectedMessage();
    }
    
    public AbstractMessage getAbstactActualResult()
    {
        return this.getOutputMessage();
    }
    
    public T getOutputMessage()
    {
        return this.outputMessage;
    }
    
    public void setOutputMessage(T outputMessage)
    {
        this.outputMessage = outputMessage;
    }
    
    public WrappedMessage getExpectedMessageQueryable()
    {
        return new WrappedMessage(this.getExpectedMessage());
    }
    
    public WrappedMessage getActualResultQueryable()
    {
        return new WrappedMessage(this.getActualResult());
    }
    
    public Exchange getExchange()
    {
        return this.exchange;
    }
    
    public void setExchange(Exchange exchange)
    {
        this.exchange = exchange;
    }
    
    public String getOutputText()
    {
        return this.outputText;
    }
    
    public void setOutputText(String outputText)
    {
        this.outputText = outputText;
    }
    
    public String getOriginalInputText()
    {
        return this.originalInputText;
    }
    
    public void setOriginalInputText(String originalInputText)
    {
        this.originalInputText = originalInputText;
    }
    
}
