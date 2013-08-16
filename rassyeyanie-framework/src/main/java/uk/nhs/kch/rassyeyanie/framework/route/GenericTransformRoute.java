package uk.nhs.kch.rassyeyanie.framework.route;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.component.hl7.HL7DataFormat;
import org.apache.camel.model.FilterDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.HL7AdditionalConstants;
import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.model.AbstractMessage;

public class GenericTransformRoute
    extends AbstractConsumer
{
    
    private String outboundQueue;
    private List<Object> identifiers;
    private List<Object> processors;
    // TODO: change to boolean -- currently checking for not null
    private AbstractMessage outputPlaceholder;
    private final Logger logger;
    private String[] workerIds;
    
    private HL7DataFormat hl7DataFormat;
    
    public AbstractMessage getOutputPlaceholder()
    {
        return this.outputPlaceholder;
    }
    
    public void setOutputPlaceholder(AbstractMessage outputPlaceholder)
    {
        this.outputPlaceholder = outputPlaceholder;
    }
    
    public GenericTransformRoute()
    {
        this.identifiers = Collections.emptyList();
        this.processors = Collections.emptyList();
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public String getOutboundQueue()
    {
        return this.outboundQueue;
    }
    
    public void setOutboundQueue(String outboundQueue)
    {
        this.outboundQueue = outboundQueue;
    }
    
    @Override
    public void configure()
        throws Exception
    {
        final String routeName = this.getRouteName();
        String inboundQueue = this.getInboundQueue();
        if (this.outputPlaceholder != null &&
            (this.processors.isEmpty() || this.processors.size() == 0))
        {
            Exception ex =
                new Exception(
                    String
                        .format(
                            "outputPlaceholder is set but no processors configured for %s",
                            routeName));
            throw ex;
            
        }
        
        if (this.getWorkerIds() != null)
            for (String workerid : this.getWorkerIds())
            {
                final String loopRouteName = String.format(routeName, workerid);
                this.logger.info(String.format(
                    "Creating route %s",
                    loopRouteName));
                RouteDefinition routeDefinition =
                    this.from(inboundQueue + "-" + workerid);
                ProcessorDefinition<RouteDefinition> processDefinition =
                    routeDefinition.routeId(loopRouteName);
                
                FilterDefinition rootChoice =
                    processDefinition.filter(new Predicate() {
                        @Override
                        public boolean matches(Exchange exchange)
                        {
                            return true;
                        }
                    });
                
                FilterDefinition filterDefinition = rootChoice;
                
                for (Object identifier : this.getIdentifiers())
                {
                    filterDefinition =
                        filterDefinition.filter().method(identifier);
                    filterDefinition.id(identifier.toString());
                }
                
                this.attachFilterProcessAndDestination(
                    loopRouteName,
                    filterDefinition,
                    workerid);
                
                routeDefinition
                    .choice()
                    .when()
                    .property(Exchange.FILTER_MATCHED)
                    .process(new Processor() {
                        
                        @Override
                        public void process(Exchange exchange)
                            throws Exception
                        {
                            String exchangedFilterMatch =
                                "Exchanges matching filter";
                            CamelContext camelContext = exchange.getContext();
                            Route camelRoute =
                                camelContext.getRoute(loopRouteName);
                            Map<String, Object> routeProperties =
                                camelRoute.getProperties();
                            
                            int totalExchangesFiltered = 1;
                            if (routeProperties
                                .containsKey(exchangedFilterMatch))
                                totalExchangesFiltered =
                                    ((Integer) routeProperties
                                        .get(exchangedFilterMatch)) + 1;
                            
                            routeProperties.put(
                                exchangedFilterMatch,
                                totalExchangesFiltered);
                            
                        }
                    })
                    .end();
            }
        
    }
    
    private void
        attachFilterProcessAndDestination(String routeName,
                                          FilterDefinition filterDefinition,
                                          String workerId)
    {
        filterDefinition
            .setHeader(HL7AdditionalConstants.HL7_SOURCE_MESSAGE)
            .body();
        
        if (this.outputPlaceholder != null)
        {
            filterDefinition.process(new Processor() {
                @Override
                public void process(Exchange exchange)
                    throws Exception
                {
                    AbstractMessage message =
                        exchange.getIn().getBody(AbstractMessage.class);
                    
                    exchange.getIn().setBody(
                        HapiUtil.createEmptyMessage(message, "2.4"));
                }
            });
            
            // clear message for placeholder to be reused
            filterDefinition.process(new Processor() {
                @Override
                public void process(Exchange exchange)
                    throws Exception
                {
                    AbstractMessage message =
                        exchange.getIn().getBody(AbstractMessage.class);
                    message.clear();
                }
            });
        }
        
        for (Object processor : this.processors)
            filterDefinition.bean(processor).id(processor.toString());
        
        // remove from header before going to sender
        filterDefinition
            .removeHeader(HL7AdditionalConstants.HL7_SOURCE_MESSAGE);
        
        filterDefinition.log(
            LoggingLevel.DEBUG,
            this.getBeginProcessMessage(routeName));
        
        filterDefinition.to(
            ExchangePattern.InOnly,
            this.outboundQueue + "-" + workerId).log(
            LoggingLevel.DEBUG,
            this.getEndProcessMessage(routeName));
    }
    
    private String getEndProcessMessage(String routeName)
    {
        return String
            .format(
                "GenericTransformRoute: End of processing route \"%s\" to outbound Queue \"%s\"",
                routeName,
                this.outboundQueue);
    }
    
    private String getBeginProcessMessage(String routeName)
    {
        return String.format(
            "GenericTransformRoute: Start of processing route \"%s\"",
            routeName);
    }
    
    public List<Object> getIdentifiers()
    {
        return this.identifiers;
    }
    
    public void setIdentifiers(List<Object> identifiers)
    {
        this.identifiers = identifiers;
    }
    
    public List<Object> getProcessors()
    {
        return this.processors;
    }
    
    public void setProcessors(List<Object> processors)
    {
        this.processors = processors;
    }
    
    public String[] getWorkerIds()
    {
        return this.workerIds;
    }
    
    public void setWorkerIds(String[] workerIds)
    {
        this.workerIds = workerIds;
    }
    
    public HL7DataFormat getHl7DataFormat()
    {
        return this.hl7DataFormat;
    }
    
    @Autowired
    public void setHl7DataFormat(HL7DataFormat hl7DataFormat)
    {
        this.hl7DataFormat = hl7DataFormat;
    }
    
}
