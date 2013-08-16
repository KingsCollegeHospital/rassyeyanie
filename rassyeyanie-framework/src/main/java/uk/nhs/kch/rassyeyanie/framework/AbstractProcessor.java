package uk.nhs.kch.rassyeyanie.framework;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.dto.KeyValuePairItem;
import uk.nhs.kch.rassyeyanie.framework.repository.RepositoryFactoryInterface;
import uk.nhs.kch.rassyeyanie.framework.repository.RepositoryInterface;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.DeepCopy;

@Deprecated
public abstract class AbstractProcessor
    implements Processor
{
    @Autowired
    private RepositoryFactoryInterface repositoryFactory;
    
    public PipeParser getParser()
    {
        return Util.createVersionedParser("2.4");
    }
    
    public RepositoryFactoryInterface getRepositoryFactory()
    {
        return this.repositoryFactory;
    }
    
    protected String lookupContextValueFromKey(String context, String key)
    {
        RepositoryInterface<KeyValuePairItem> keyValueItemRepository =
            this.getRepositoryFactory().getKeyValuePairItemRepository();
        
        KeyValuePairItem keyValuePairItem =
            keyValueItemRepository.Get(context, key);
        
        if (keyValuePairItem == null)
            return null;
        return keyValuePairItem.getItemValue();
    }
    
    @Override
    public void process(Exchange exchange)
        throws Exception
    {
        AbstractMessage workingMessage =
            exchange.getIn().getBody(AbstractMessage.class);
        
        this.dispatchProcessFixture(workingMessage);
        
    }
    
    protected abstract void
        dispatchProcessFixture(AbstractMessage workingMessage)
            throws HL7Exception;
    
    public void
        setRepositoryFactory(RepositoryFactoryInterface repositoryFactory)
    {
        this.repositoryFactory = repositoryFactory;
    }
    
    protected <T extends Segment> T copySegment(T originalSegment,
                                                T clonedSegment)
        throws HL7Exception
    {
        DeepCopy.copy(originalSegment, clonedSegment);
        return clonedSegment;
    }
    
}
