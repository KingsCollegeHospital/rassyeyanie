package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;

import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.processor.ZrefDataFormat;

public class GenericSender
    extends AbstractConsumer
{
    private ZrefDataFormat postFormatter;
    private String receiverEndpoint;
    private final Logger logger;
    private int delay;
    private SenderResponseProcessorInterface senderResponseProcessor;
    
    public GenericSender()
    {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public String getReceiverEndpoint()
    {
        return this.receiverEndpoint;
    }
    
    public void setReceiverEndpoint(String receiverEndpoint)
    {
        this.receiverEndpoint = receiverEndpoint;
    }
    
    public int getDelay()
    {
        return this.delay;
    }
    
    public void setDelay(int delay)
    {
        this.delay = delay;
    }
    
    @Override
    public void configure()
    {
        
        final String routeName = this.getRouteName();
        String inboundQueue = this.getInboundQueue();
        
        if (StringUtils.isEmpty(routeName)) { throw new IllegalArgumentException(
            "routeName"); }
        
        if (StringUtils.isEmpty(inboundQueue)) { throw new IllegalArgumentException(
            "inboundQueue"); }
        
        this.logger.info("Attempting to create sender route {}", routeName);
        
        this.defineExceptionHandlers();
        
        RouteDefinition policyDefinition = this.from(inboundQueue)
        /* .transacted("PROPAGATION_REQUIRED") */.routeId(routeName);
        
        // log out bound
        
        policyDefinition.log(String.format(
            "Processing MsgId: ${header.%s} / ${id}",
            HL7Constants.HL7_MESSAGE_CONTROL));
        
        if (!StringUtils.isEmpty(this.receiverEndpoint))
        {
            if (this.getPostFormatter() != null)
            {
                policyDefinition.marshal(this.getPostFormatter());
            }
            policyDefinition.log("body");
            policyDefinition.to(ExchangePattern.InOut, this.receiverEndpoint);
            
            Processor responseProcessor = this.getSenderResponseProcessor();
            
            policyDefinition// log acknowledgement
                .log("body")
                .process(responseProcessor);
            if (this.delay > 0)
            {
                policyDefinition.delay(this.getDelay()).id("delayer");
            }
        }
    }
    
    public ZrefDataFormat getPostFormatter()
    {
        return this.postFormatter;
    }
    
    @Autowired(required = false)
    public void setPostFormatter(ZrefDataFormat postFormatter)
    {
        this.postFormatter = postFormatter;
    }
    
    public SenderResponseProcessorInterface getSenderResponseProcessor()
    {
        return this.senderResponseProcessor;
    }
    
    @Autowired
    public
        void
        setSenderResponseProcessor(SenderResponseProcessorInterface senderResponseProcessor)
    {
        this.senderResponseProcessor = senderResponseProcessor;
    }
}
