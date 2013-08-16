package uk.nhs.kch.rassyeyanie.framework.repository;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import uk.nhs.kch.rassyeyanie.framework.dto.KeyValuePairItem;
import uk.nhs.kch.rassyeyanie.framework.dto.SimpleItem;

@Deprecated
public class RepositoryFactory
    implements RepositoryFactoryInterface
{
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Override
    public Calendar getCurrentDate()
    {
        return Calendar.getInstance();
    }
    
    @Override
    public RepositoryInterface<KeyValuePairItem>
        getKeyValuePairItemRepository()
    {
        return new KeyValueItemRepository(this.configurationService);
    }
    
    @Override
    public RepositoryInterface<SimpleItem> getSimpleItemRepository()
    {
        return null;
    }
    
    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }
    
    public void
        setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
