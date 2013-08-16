package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Set;

/**
 */
public interface CacheService {

	Set<String> getCachedValues(String key);

	void addCachedValue(String key, String value);

	void removeCachedValue(String key, String value);
}
