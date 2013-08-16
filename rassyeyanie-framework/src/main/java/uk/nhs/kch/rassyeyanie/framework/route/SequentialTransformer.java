package uk.nhs.kch.rassyeyanie.framework.route;

import java.util.Collections;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.hl7.HL7Constants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.model.OnExceptionDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SequentialTransformer
    extends SpringRouteBuilder
{
    
    private List<GenericTransformRoute> transformers;
    private String inboundQueue;
    private String senderQueue;
    private String routeName;
    private String errorQueue;
    private String receiverEndpoint;
    private int redeliveryAttempts;
    private long redeliveryDelay;
    private final Logger logger;
    private GenericSender sender;
    private String[] workerIds;
    private boolean senderEnabled;
    private int delay;
    
    public void processInTurn()
    {
        
    }
    
    public String getInboundQueue()
    {
        return this.inboundQueue;
    }
    
    public void setInboundQueue(String inboundQueue)
    {
        this.inboundQueue = inboundQueue;
    }
    
    public String getRouteName()
    {
        return this.routeName;
    }
    
    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }
    
    public String getErrorQueue()
    {
        return this.errorQueue;
    }
    
    public void setErrorQueue(String errorQueue)
    {
        this.errorQueue = errorQueue;
    }
    
    public SequentialTransformer()
    {
        this.transformers = Collections.emptyList();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.setSenderEnabled(true);
    }
    
    public List<GenericTransformRoute> getTransformers()
    {
        return this.transformers;
    }
    
    @Autowired
    public void setTransformers(List<GenericTransformRoute> transformers)
    {
        this.transformers = transformers;
    }
    
    public void setRedeliveryAttempts(int redeliveryAttempts)
    {
        this.redeliveryAttempts = redeliveryAttempts;
    }
    
    public int getRedeliveryAttempts()
    {
        return this.redeliveryAttempts;
    }
    
    public void setRedeliveryDelay(long redeliveryDelay)
    {
        this.redeliveryDelay = redeliveryDelay;
    }
    
    @Override
    public void addRoutesToCamelContext(CamelContext context)
        throws Exception
    {
        if (context instanceof DefaultCamelContext)
        {
            ((DefaultCamelContext) context).setName(this.getRouteName());
        }
        String catcherName = String.format("direct:%s", this.getRouteName());
        
        for (GenericTransformRoute transform : this.transformers)
        {
            String transformerInboundQueue =
                String.format("direct:%s", transform.getRouteName());
            // override source
            transform.setInboundQueue(transformerInboundQueue);
            transform.setOutboundQueue(catcherName);
            transform.setRouteName(this.routeName + "-" + "%s" + "-" +
                                   transform.getRouteName());
            transform.setWorkerIds(this.getWorkerIds());
            context.addRoutes(transform);
        }
        this.getSender().setInboundQueue(this.getSenderQueue());
        this.getSender().setRouteName(this.routeName + "-sender");
        
        this.getSender().setReceiverEndpoint(this.getReceiverEndpoint());
        this.getSender().setErrorQueue(this.getErrorQueue());
        this.getSender().setDelay(this.getDelay());
        
        if (this.isSenderEnabled())
        {
            
            context.addRoutes(this.getSender());
        }
        
        super.addRoutesToCamelContext(context);
    }
    
    @Override
    public void configure()
        throws Exception
    {
        if (StringUtils.isEmpty(this.routeName)) { throw new IllegalArgumentException(
            "routeName"); }
        
        if (StringUtils.isEmpty(this.inboundQueue)) { throw new IllegalArgumentException(
            "inboundQueue"); }
        
        String catcherName = String.format("direct:%s", this.routeName);
        
        if (this.getWorkerIds() == null)
            this.logger
                .info(String.format("Creating route %s", this.routeName));
        
        this.defineExceptionHandlers();
        
        if (this.getWorkerIds() != null)
            for (String workerId : this.getWorkerIds())
            {
                this.logger.info(String.format(
                    "Creating route %s %s",
                    this.getInboundQueue(),
                    workerId));
                
                ProcessorDefinition<RouteDefinition> policyDefinition =
                    this
                        .fromF(this.getInboundQueue(), workerId)
                        .routeId(this.routeName + "-" + workerId + "-start")
                        .unmarshal()
                        .custom("hl7dataformat")
                /* .transacted("PROPAGATION_REQUIRED") */;
                
                MulticastDefinition multicast = policyDefinition.multicast();
                
                for (GenericTransformRoute transform : this.transformers)
                {
                    multicast
                        .to(transform.getInboundQueue() + "-" + workerId)
                        .id(transform.getRouteName() + "-" + workerId);
                }
                
                this
                    .from(catcherName + "-" + workerId)
                    .routeId(this.routeName + "-" + workerId + "-end")
                    .log(
                        String.format(
                            "Processing MsgId: ${header.%s} / ${id}",
                            HL7Constants.HL7_MESSAGE_CONTROL))
                    // send to sender as flat string and not marshaled java
                    // object
                    .convertBodyTo(String.class)
                    .to(this.getSender().getInboundQueue());
                
            }
    }
    
    protected void defineExceptionHandlers()
    {
        OnExceptionDefinition exceptionDefinition =
            this.onException(Exception.class);
        
        if (this.redeliveryAttempts != 0)
        {
            exceptionDefinition.maximumRedeliveries(this.redeliveryAttempts);
        }
        else
            exceptionDefinition.maximumRedeliveries(-1);
        
        if (this.redeliveryDelay != 0L)
        {
            exceptionDefinition.redeliveryDelay(this.redeliveryDelay);
        }
        else
            exceptionDefinition.redeliveryDelay(1000L);
        
        exceptionDefinition
            .retryAttemptedLogLevel(LoggingLevel.ERROR)
            .logRetryStackTrace(true)
            .logRetryAttempted(true)
            .maximumRedeliveryDelay(60000)
            .backOffMultiplier(2)
            .to(AbstractRouteBuilder.ERROR_LOG)
            .to(this.errorQueue);
    }
    
    public GenericSender getSender()
    {
        return this.sender;
    }
    
    @Autowired
    public void setSender(GenericSender sender)
    {
        this.sender = sender;
    }
    
    public String getSenderQueue()
    {
        return this.senderQueue;
    }
    
    public void setSenderQueue(String senderQueue)
    {
        this.senderQueue = senderQueue;
    }
    
    public String getReceiverEndpoint()
    {
        return this.receiverEndpoint;
    }
    
    public void setReceiverEndpoint(String receiverEndpoint)
    {
        this.receiverEndpoint = receiverEndpoint;
    }
    
    public String[] getWorkerIds()
    {
        return this.workerIds;
    }
    
    public void setWorkerIds(String[] workerIds)
    {
        this.workerIds = workerIds;
    }
    
    public boolean isSenderEnabled()
    {
        return this.senderEnabled;
    }
    
    public void setSenderEnabled(boolean senderEnabled)
    {
        this.senderEnabled = senderEnabled;
    }
    
    public int getDelay()
    {
        return this.delay;
    }
    
    public void setDelay(int delay)
    {
        this.delay = delay;
    }
}
