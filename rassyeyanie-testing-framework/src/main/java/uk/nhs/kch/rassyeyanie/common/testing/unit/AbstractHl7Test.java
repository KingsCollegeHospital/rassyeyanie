package uk.nhs.kch.rassyeyanie.common.testing.unit;

import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;

import uk.nhs.kch.rassyeyanie.framework.AbstractProcessor;
import uk.nhs.kch.rassyeyanie.framework.HL7VersionedDataFormat;
import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import uk.nhs.kch.rassyeyanie.framework.dto.KeyValuePairItem;
import uk.nhs.kch.rassyeyanie.framework.repository.KeyValueItemRepository;
import uk.nhs.kch.rassyeyanie.framework.repository.RepositoryFactoryInterface;
import uk.nhs.kch.rassyeyanie.framework.repository.RepositoryInterface;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;

public class AbstractHl7Test
    extends CamelTestSupport
{
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint endpoint;
    
    @Produce(uri = "direct:start")
    protected ProducerTemplate template;
    protected String actualSource;
    protected String expected;
    protected AbstractProcessor[] processors;
    protected PipeParser pipeParser;
    
    final String actualSourcePath;
    final String expectedPath;
    
    protected static RepositoryFactoryInterface repositoryFactory;
    protected static RepositoryInterface<KeyValuePairItem> keyValueRepository;
    protected static ConfigurationService configurationService;
    protected AbstractMessage expectedMessage;
    protected AbstractMessage actualMessage;
    
    private String sendingApplication;
    private String sendingFacility;
    private String receivingApplication;
    private String receivingFacility;
    private String timestamp;
    private String security;
    private String messageType;
    private String triggerEvent;
    private String messageControl;
    private String processingId;
    private String versionId;
    private String patientInternalId;
    private String patientExternalId;
    
    protected String getSendingApplication()
    {
        return this.sendingApplication;
    }
    
    protected void setSendingApplication(String sendingApplication)
    {
        this.sendingApplication = sendingApplication;
    }
    
    protected String getSendingFacility()
    {
        return this.sendingFacility;
    }
    
    protected void setSendingFacility(String sendingFacility)
    {
        this.sendingFacility = sendingFacility;
    }
    
    protected String getReceivingApplication()
    {
        return this.receivingApplication;
    }
    
    protected void setReceivingApplication(String receivingApplication)
    {
        this.receivingApplication = receivingApplication;
    }
    
    protected String getReceivingFacility()
    {
        return this.receivingFacility;
    }
    
    protected void setReceivingFacility(String receivingFacility)
    {
        this.receivingFacility = receivingFacility;
    }
    
    protected String getTimestamp()
    {
        return this.timestamp;
    }
    
    protected void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }
    
    protected String getSecurity()
    {
        return this.security;
    }
    
    protected void setSecurity(String security)
    {
        this.security = security;
    }
    
    protected String getMessageType()
    {
        return this.messageType;
    }
    
    protected void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }
    
    protected String getTriggerEvent()
    {
        return this.triggerEvent;
    }
    
    protected void setTriggerEvent(String triggerEvent)
    {
        this.triggerEvent = triggerEvent;
    }
    
    protected String getMessageControl()
    {
        return this.messageControl;
    }
    
    protected void setMessageControl(String messageControl)
    {
        this.messageControl = messageControl;
    }
    
    protected String getProcessingId()
    {
        return this.processingId;
    }
    
    protected void setProcessingId(String processingId)
    {
        this.processingId = processingId;
    }
    
    protected String getVersionId()
    {
        return this.versionId;
    }
    
    protected void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }
    
    protected String getPatientInternalId()
    {
        return this.patientInternalId;
    }
    
    protected void setPatientInternalId(String patientInternalId)
    {
        this.patientInternalId = patientInternalId;
    }
    
    protected String getPatientExternalId()
    {
        return this.patientExternalId;
    }
    
    protected void setPatientExternalId(String patientExternalId)
    {
        this.patientExternalId = patientExternalId;
    }
    
    public AbstractHl7Test(String actualSourcePath, String expectedPath)
    {
        this.actualSourcePath = actualSourcePath;
        this.expectedPath = expectedPath;
        
        repositoryFactory = Mockito.mock(RepositoryFactoryInterface.class);
        keyValueRepository = Mockito.mock(KeyValueItemRepository.class);
        Mockito
            .stub(repositoryFactory.getKeyValuePairItemRepository())
            .toReturn(keyValueRepository);
    }
    
    public static RouteBuilder
        createRouteBuilder(final AbstractProcessor... processors)
    {
        return new RouteBuilder() {
            
            @Override
            public void configure()
            {
                HL7VersionedDataFormat hl7 = new HL7VersionedDataFormat();
                hl7.setValidate(false);
                hl7.setVersion("2.4");
                
                RouteDefinition routeDefinition =
                    this.from("direct:start").unmarshal(hl7);
                
                for (AbstractProcessor processor : processors)
                {
                    processor.setRepositoryFactory(repositoryFactory);
                    routeDefinition.process(processor);
                }
                
                routeDefinition.to("mock:result");
            }
        };
    }
    
    public static String getClassResourceStream(Class<?> streamClass,
                                                String string)
        throws IOException
    {
        return IOUtils.toString(
            streamClass.getClassLoader().getResourceAsStream(string)).replace(
            '\n',
            '\r');
    }
    
    public void setProcessors(AbstractProcessor... processors)
    {
        for (AbstractProcessor processor : processors)
            this.pipeParser = processor.getParser();
        this.processors = processors;
    }
    
    protected <T extends AbstractSegment> void
        assertEqualFields(AbstractMessage expected,
                          AbstractMessage actual,
                          Class<T> clazz)
            throws HL7Exception
    {
        Assert.assertEquals(
            HapiUtil.getWithTerser(expected, clazz).encode(),
            HapiUtil.getWithTerser(actual, clazz).encode());
    }
    
    @Override
    protected RouteBuilder createRouteBuilder()
    {
        return createRouteBuilder(this.processors);
    }
    
    private AbstractMessage getExpectedMessage()
        throws HL7Exception, EncodingNotSupportedException
    {
        return (AbstractMessage) this.pipeParser.parse(this.expected);
    }
    
    @Override
    public void setUp()
        throws Exception
    {
        super.setUp();
        this.actualSource =
            getClassResourceStream(this.getClass(), this.actualSourcePath);
        this.expected =
            getClassResourceStream(this.getClass(), this.expectedPath);
        
        this.expectedMessage = this.getExpectedMessage();
        this.endpoint.expectedBodiesReceived(this.expectedMessage.encode());
        this.template.sendBody(this.actualSource);
        this.actualMessage =
            this.endpoint
                .getExchanges()
                .get(0)
                .getIn()
                .getBody(AbstractMessage.class);
    }
    
    public <T extends AbstractSegment> void segment_tester(Class<T> clazz)
        throws Exception
    {
        this.assertEqualFields(this.expectedMessage, this.actualMessage, clazz);
    }
    
    protected <T extends Structure> void testStructureByIndex(int index,
                                                              Class<T> clazz)
        throws HL7Exception
    {
        
        List<T> exepectedSegments =
            HapiUtil.getAll(this.expectedMessage, clazz);
        // re-eval to remove cleared segments
        List<T> actualSegments =
            HapiUtil.getAll((AbstractMessage) this.pipeParser
                .parse(this.actualMessage.encode()), clazz);
        
        if (exepectedSegments.size() < index + 1 ||
            actualSegments.size() < index + 1)
            return;
        
        assertEquals(
            ((AbstractSegment) exepectedSegments.get(index)).encode(),
            ((AbstractSegment) actualSegments.get(index)).encode());
    }
    
    protected <T extends Structure> void
        testStructureByIndex(String groupName,
                             int groupIndex,
                             Class<T> segmentClass,
                             int segmentIndex)
            throws HL7Exception
    {
        Structure expectedSegment =
            getGroupSegment(
                groupIndex,
                segmentIndex,
                segmentClass,
                groupName,
                this.expectedMessage);
        Structure actualSegment =
            getGroupSegment(
                groupIndex,
                segmentIndex,
                segmentClass,
                groupName,
                this.actualMessage);
        assertEquals(
            ((AbstractSegment) expectedSegment).encode(),
            ((AbstractSegment) actualSegment).encode());
    }
    
    private static <T> Structure
        getGroupSegment(int groupIndex,
                        int segmentInfex,
                        Class<T> clazz,
                        String string,
                        AbstractMessage expectedMessage)
            throws HL7Exception
    {
        return ((AbstractGroup) expectedMessage.get(string, groupIndex)).get(
            clazz.getSimpleName(),
            segmentInfex);
    }
    
    private void header_tester(String headerId, String expectedHeaderValue)
    {
        assertEquals(expectedHeaderValue, this.endpoint
            .getExchanges()
            .get(0)
            .getOut()
            .getHeader(headerId));
    }
    
    @Test
    public void test_message_to_message()
        throws Exception
    {
        this.assertMockEndpointsSatisfied();
    }
    
    @Test
    public void test_message_jms_header_msh_sending_application()
    {
        this.header_tester(
            HL7Constants.HL7_SENDING_APPLICATION,
            this.getSendingApplication());
    }
    
    @Test
    public void test_message_jms_header_msh_sending_facility()
    {
        this.header_tester(
            HL7Constants.HL7_SENDING_FACILITY,
            this.getSendingFacility());
    }
    
    @Test
    public void test_message_jms_header_msh_receiving_application()
    {
        this.header_tester(
            HL7Constants.HL7_RECEIVING_APPLICATION,
            this.getReceivingApplication());
    }
    
    @Test
    public void test_message_jms_header_msh_receiving_facility()
    {
        this.header_tester(
            HL7Constants.HL7_RECEIVING_FACILITY,
            this.getReceivingFacility());
    }
    
    @Test
    public void test_message_jms_header_msh_timestamp()
    {
        this.header_tester(HL7Constants.HL7_TIMESTAMP, this.getTimestamp());
    }
    
    @Test
    public void test_message_jms_header_msh_security()
    {
        this.header_tester(HL7Constants.HL7_SECURITY, this.getSecurity());
    }
    
    @Test
    public void test_message_jms_header_msh_messageType()
    {
        this
            .header_tester(HL7Constants.HL7_MESSAGE_TYPE, this.getMessageType());
    }
    
    @Test
    public void test_message_jms_header_msh_triggerEvent()
    {
        this.header_tester(
            HL7Constants.HL7_TRIGGER_EVENT,
            this.getTriggerEvent());
    }
    
    @Test
    public void test_message_jms_header_msh_messageControl()
    {
        this.header_tester(
            HL7Constants.HL7_MESSAGE_CONTROL,
            this.getMessageControl());
    }
    
    @Test
    public void test_message_jms_header_msh_processingId()
    {
        this.header_tester(
            HL7Constants.HL7_PROCESSING_ID,
            this.getProcessingId());
    }
    
    @Test
    public void test_message_jms_header_msh_versionId()
    {
        this.header_tester(HL7Constants.HL7_VERSION_ID, this.getVersionId());
    }
    
    @Test
    public void test_message_jms_header_pid_internal_Id()
    {
        this.header_tester(
            "CamelHL7PatientInternalId",
            this.getPatientInternalId());
    }
    
    @Test
    public void test_message_jms_header_pid_external_Id()
    {
        this.header_tester(
            "CamelHL7PatientExternalId",
            this.getPatientExternalId());
    }
    
    protected WrappedMessage getExpected()
    {
        return new WrappedMessage(this.expectedMessage);
    }
    
    protected WrappedMessage getActual()
    {
        return new WrappedMessage(this.actualMessage);
    }
    
    protected void assertEquals(Structure expectedSegment,
                                Structure actualSegment)
        throws HL7Exception
    {
        assertEquals(
            ((AbstractSegment) expectedSegment).encode(),
            ((AbstractSegment) actualSegment).encode());
    }
}
