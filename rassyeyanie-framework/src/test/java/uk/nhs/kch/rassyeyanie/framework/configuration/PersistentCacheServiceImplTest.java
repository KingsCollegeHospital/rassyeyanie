package uk.nhs.kch.rassyeyanie.framework.configuration;

import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 */
public class PersistentCacheServiceImplTest {

    private static final String CACHE_NAME = "cache";
    private static final String VALUE = "123456";

    private CacheManager cacheManager;
    private PersistentCacheServiceImpl cacheService;

    @Before
    public void setUp() {
        cacheManager = new CacheManager();

        cacheService = new PersistentCacheServiceImpl();
        cacheService.setCacheManager(cacheManager);
        cacheService.setCacheName("CACHE_NAME");
    }

    @After
    public void teardown() {
        cacheManager.shutdown();
    }

    @Test
    public void testCachesAreNeverNull() {
        Set<String> before = cacheService.getCachedValues(CACHE_NAME);
        assertNotNull(before);
        assertTrue(before.isEmpty());
    }

    @Test
    public void testAddCachedValue() {
        Set<String> before = cacheService.getCachedValues(CACHE_NAME);
        assertFalse(before.contains(VALUE));

        cacheService.addCachedValue(CACHE_NAME, VALUE);

        Set<String> after = cacheService.getCachedValues(CACHE_NAME);
        assertTrue(after.contains(VALUE));
    }

    @Test
    public void testOnlyCachesValueOnce() {
        Set<String> before = cacheService.getCachedValues(CACHE_NAME);
        assertFalse(before.contains(VALUE));

        cacheService.addCachedValue(CACHE_NAME, VALUE);

        Set<String> afterFirst = cacheService.getCachedValues(CACHE_NAME);
        assertTrue(afterFirst.contains(VALUE));
        assertEquals(1, afterFirst.size());

        cacheService.addCachedValue(CACHE_NAME, VALUE);

        Set<String> afterSecond = cacheService.getCachedValues(CACHE_NAME);
        assertTrue(afterSecond.contains(VALUE));
        assertEquals(1, afterSecond.size());
    }

    @Test
    public void testRemoveCachedValue() {
        cacheService.addCachedValue(CACHE_NAME, VALUE);

        Set<String> before = cacheService.getCachedValues(CACHE_NAME);
        assertTrue(before.contains(VALUE));

        cacheService.removeCachedValue(CACHE_NAME, VALUE);

        Set<String> after = cacheService.getCachedValues(CACHE_NAME);
        assertFalse(after.contains(VALUE));
    }

    @Test
    public void testRemovedCachedValueThatHasNotBeenCached() {
        Set<String> before = cacheService.getCachedValues(CACHE_NAME);

        cacheService.removeCachedValue(CACHE_NAME, VALUE);

        Set<String> after = cacheService.getCachedValues(CACHE_NAME);

        assertEquals(before.size(), after.size());
    }
}
