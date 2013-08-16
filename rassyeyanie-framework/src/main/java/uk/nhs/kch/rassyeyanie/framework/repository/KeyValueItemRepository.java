package uk.nhs.kch.rassyeyanie.framework.repository;

import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import uk.nhs.kch.rassyeyanie.framework.dto.KeyValuePairItem;

@Deprecated
public class KeyValueItemRepository implements
	RepositoryInterface<KeyValuePairItem> {

    private final ConfigurationService configurationService;

    public KeyValueItemRepository(ConfigurationService configurationService) {
	this.configurationService = configurationService;
    }

    @Override
    public KeyValuePairItem Get(String context, String key) {
	int contextId = this.configurationService.findContextIdByName(context);
	String value = this.configurationService.findValue(contextId, key);
	KeyValuePairItem kvpi = new KeyValuePairItem();
	kvpi.setItemKey(key);
	kvpi.setItemValue(value);
	return kvpi;
    }
}
