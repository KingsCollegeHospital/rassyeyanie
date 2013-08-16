package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.model.OnExceptionDefinition;

/**
 * Base class for any process that takes something off a queue.
 */
public abstract class AbstractConsumer
    extends AbstractRouteBuilder
{
    
    private String routeName;
    private String inboundQueue;
    private String errorQueue;
    private int redeliveryAttempts;
    private long redeliveryDelay;
    private int nackRedeliveryAttempts;
    private long nackRedeliveryDelay;
    
    public String getRouteName()
    {
        return this.routeName;
    }
    
    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }
    
    public String getInboundQueue()
    {
        return this.inboundQueue;
    }
    
    public void setInboundQueue(String inboundQueue)
    {
        this.inboundQueue = inboundQueue;
    }
    
    public String getErrorQueue()
    {
        return this.errorQueue;
    }
    
    public void setErrorQueue(String errorQueue)
    {
        this.errorQueue = errorQueue;
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
    
    /**
     * Override this method to change the error handling for this transform. The
     * default handler will catch all RuntimeExceptions and send the message to
     * the defined error queue.
     */
    protected void defineExceptionHandlers()
    {
        defineGeneralExceptionDefinition(
            this.onException(ResponseException.class),
            this.redeliveryAttempts == 0 ? 30 : this.redeliveryAttempts,
            this.redeliveryDelay,
            this.errorQueue,
            true);
        
        /*
         * this
         * .onException(Exception.class)
         * .log(LoggingLevel.ERROR, "exception.stacktrace")
         * .rollback();
         */
        
        /*
         * defineGeneralExceptionDefinition(
         * this.onException(Exception.class),
         * this.nackRedeliveryAttempts == 0 ? -1 : this.nackRedeliveryAttempts,
         * this.nackRedeliveryDelay == 0L ? 1000L : this.nackRedeliveryDelay,
         * this.errorQueue,
         * false);
         */
    }
    
    private static
        void
        defineGeneralExceptionDefinition(OnExceptionDefinition exceptionDefinition,
                                         int redeliveryAttempts,
                                         long redeliveryDelay,
                                         String errorQueue,
                                         boolean logRetryAttempted)
    {
        exceptionDefinition.maximumRedeliveries(redeliveryAttempts);
        
        exceptionDefinition.redeliveryDelay(redeliveryDelay);
        
        exceptionDefinition.handled(true);
        
        exceptionDefinition
            .retryAttemptedLogLevel(LoggingLevel.ERROR)
            .logRetryAttempted(logRetryAttempted)
            .maximumRedeliveryDelay(60000)
            .backOffMultiplier(10)
            .to(ERROR_LOG)
            .to(errorQueue);
    }
    
    public int getNackRedeliveryAttempts()
    {
        return this.nackRedeliveryAttempts;
    }
    
    public void setNackRedeliveryAttempts(int nackRedeliveryAttempts)
    {
        this.nackRedeliveryAttempts = nackRedeliveryAttempts;
    }
    
    public long getNackRedeliveryDelay()
    {
        return this.nackRedeliveryDelay;
    }
    
    public void setNackRedeliveryDelay(long nackRedeliveryDelay)
    {
        this.nackRedeliveryDelay = nackRedeliveryDelay;
    }
}
