package uk.nhs.kch.rassyeyanie.framework.repository;

import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;

@Deprecated
public class DatamapLookup implements
		DatamapLookupInterface {

	private ConfigurationService configService;

	public DatamapLookup(ConfigurationService configService){
		this.configService = configService;
	}
	
	@Override
	public String LookupValue(String context, String key) {
		return configService.findValue(context, key);
	}

}
