package uk.nhs.kch.rassyeyanie.framework.route;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.configuration.CacheService;

/**
 */
public abstract class AbstractRouteBuilder
    extends SpringRouteBuilder
{
    protected static final String ERROR_LOG =
        "log:uk.nhs.kch.rassyeyanie.framework?level=ERROR&showCaughtException=true&showStackTrace=true";
    
    @Autowired
    private CacheService cacheService;
    
    private static final String LINE_SEPARATOR = System
        .getProperty("line.separator");
    private static final String FILE_SEPARATOR = System
        .getProperty("file.separator");
    
    private String archiveFileLocation;
    
    public void setArchiveFileLocation(String archiveFileLocation)
    {
        this.archiveFileLocation = archiveFileLocation;
    }
    
    protected String getMessageArchive(String routeName)
    {
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("file:");
        buffer.append(this.archiveFileLocation).append(FILE_SEPARATOR);
        buffer.append(routeName).append(FILE_SEPARATOR);
        buffer
            .append("?fileName=")
            .append(routeName)
            .append("-${date:now:yyyyMMdd}.txt");
        buffer.append("&fileExist=Append");
        
        return buffer.toString();
    }
    
    /**
     * Returns the message format for the archive files. <br/>
     * NOTE: There is a bug in camel that trims whitespace from the format
     * string. This makes it
     * difficult to put whitespace around each message. That's why there is a
     * &lt;&gt;
     * symbol at the start and end. Apparently this is fixed in camel 2.10.x.
     */
    protected String getMessageArchiveFormat(String routeName, String event)
    {
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("<>");
        buffer.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
        buffer
            .append("JMS Message Id: ")
            .append("${headers.JMSMessageID}")
            .append(LINE_SEPARATOR);
        buffer.append("Route Name: ").append(routeName).append(LINE_SEPARATOR);
        buffer
            .append("Remote Address: ")
            .append("${headers.CamelMinaRemoteAddress}")
            .append(LINE_SEPARATOR);
        buffer
            .append("Local Address: ")
            .append("${headers.CamelMinaLocalAddress}")
            .append(LINE_SEPARATOR);
        buffer.append("Event: ").append(event).append(LINE_SEPARATOR);
        buffer
            .append("Timestamp: ")
            .append("${date:now:HH:mm:ss dd-MM-yyyy}")
            .append(LINE_SEPARATOR);
        buffer.append("Body: ").append(LINE_SEPARATOR);
        buffer.append("${body.toString}").append(LINE_SEPARATOR);
        buffer.append(LINE_SEPARATOR);
        buffer.append("<>");
        
        return buffer.toString();
    }
    
    public CacheService getCacheService()
    {
        return this.cacheService;
    }
    
    public void setCacheService(CacheService cacheService)
    {
        this.cacheService = cacheService;
    }
}
