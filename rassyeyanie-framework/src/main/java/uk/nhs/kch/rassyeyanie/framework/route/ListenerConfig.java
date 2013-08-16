package uk.nhs.kch.rassyeyanie.framework.route;

import java.io.Serializable;

import org.apache.camel.Processor;
import org.apache.camel.spi.DataFormat;

/**
 */
public class ListenerConfig
    implements Serializable
{
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    private String endpoint;
    private String name;
    private boolean autoStart;
    private String sendingFacility;
    private String sendingApplication;
    private String receivingFacility;
    private String receivingApplication;
    private Processor exceptionProcessor;
    private Processor acknowledgementProcessor;
    private DataFormat preFormatter;
    private Processor obxDataFormat;
    private Processor icmAckCleaner;
    
    public String getEndpoint()
    {
        return this.endpoint;
    }
    
    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }
    
    public boolean isAutoStart()
    {
        return this.autoStart;
    }
    
    public void setAutoStart(boolean autoStart)
    {
        this.autoStart = autoStart;
    }
    
    public String getSendingFacility()
    {
        return this.sendingFacility;
    }
    
    public void setSendingFacility(String sendingFacility)
    {
        this.sendingFacility = sendingFacility;
    }
    
    public String getSendingApplication()
    {
        return this.sendingApplication;
    }
    
    public void setSendingApplication(String sendingApplication)
    {
        this.sendingApplication = sendingApplication;
    }
    
    public String getReceivingFacility()
    {
        return this.receivingFacility;
    }
    
    public void setReceivingFacility(String receivingFacility)
    {
        this.receivingFacility = receivingFacility;
    }
    
    public String getReceivingApplication()
    {
        return this.receivingApplication;
    }
    
    public void setReceivingApplication(String receivingApplication)
    {
        this.receivingApplication = receivingApplication;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Processor getExceptionProcessor()
    {
        return this.exceptionProcessor;
    }
    
    public void setExceptionProcessor(Processor exceptionProcessor)
    {
        this.exceptionProcessor = exceptionProcessor;
    }
    
    public Processor getAcknowledgementProcessor()
    {
        return this.acknowledgementProcessor;
    }
    
    public void setAcknowledgementProcessor(Processor acknowledgementProcessor)
    {
        this.acknowledgementProcessor = acknowledgementProcessor;
    }
    
    public DataFormat getPreFormatter()
    {
        return this.preFormatter;
    }
    
    public void setPreFormatter(DataFormat preFormatter)
    {
        this.preFormatter = preFormatter;
    }
    
    public Processor getIcmAckCleaner()
    {
        return this.icmAckCleaner;
    }
    
    public void setIcmAckCleaner(Processor icmAckCleaner)
    {
        this.icmAckCleaner = icmAckCleaner;
    }
    
    public Processor getObxDataFormat()
    {
        return obxDataFormat;
    }
    
    public void setObxDataFormat(Processor obxDataFormat)
    {
        this.obxDataFormat = obxDataFormat;
    }
    
}
