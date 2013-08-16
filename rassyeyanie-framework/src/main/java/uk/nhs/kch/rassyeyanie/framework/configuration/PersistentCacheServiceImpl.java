package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 */
public class PersistentCacheServiceImpl implements CacheService {

    private CacheManager cacheManager;
    private String cacheName;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public Set<String> getCachedValues(String key) {
        return Collections.unmodifiableSet(fetchDataFromCache(key));
    }

    @Override
    public void addCachedValue(String key, String value) {
        Set<String> cachedValues = fetchDataFromCache(key);
        cachedValues.add(value);
    }

    @Override
    public void removeCachedValue(String key, String value) {
        Set<String> cachedValues = fetchDataFromCache(key);
        cachedValues.remove(value);
    }

    private Cache getCache() {
        if (!cacheManager.cacheExists(cacheName)) {
            cacheManager.addCache(cacheName);
        }

        return cacheManager.getCache(cacheName);
    }

    @SuppressWarnings("unchecked")
	private Set<String> fetchDataFromCache(String key) {

        Cache cache = getCache();

        Element element = cache.get(key);
        if (element == null) {
            element = new Element(key, new HashSet<String>());
            cache.put(element);
        }

        return (Set<String>)element.getObjectValue();
    }
}
