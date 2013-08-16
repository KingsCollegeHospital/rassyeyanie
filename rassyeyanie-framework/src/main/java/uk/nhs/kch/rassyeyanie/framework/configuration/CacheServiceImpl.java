package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class CacheServiceImpl implements CacheService {

    private final Map<String,Set<String>> cache = new HashMap<>();

    @Override
    public Set<String> getCachedValues(String key) {

        Set<String> result = cache.get(key);
        if (result == null) {
            return Collections.emptySet();
        }

        return result;
    }

    @Override
    public void addCachedValue(String key, String value) {

        Set<String> values = cache.get(key);
        if (values == null) {
            values = new HashSet<String>();
            cache.put(key, values);
        }

        values.add(value);
    }

    @Override
    public void removeCachedValue(String key, String value) {

        Set<String> values = cache.get(key);
        if (values != null) {
            values.remove(value);
        }
    }
}
