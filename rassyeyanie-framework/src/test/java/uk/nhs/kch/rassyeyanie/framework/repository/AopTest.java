package uk.nhs.kch.rassyeyanie.framework.repository;

import org.junit.Ignore;
import org.junit.Test;
import uk.nhs.kch.rassyeyanie.framework.configuration.PersistentCacheServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Deprecated
public class AopTest {
    @Test
    @Ignore
    public void configservicetest() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    	PersistentCacheServiceImpl cacheServiceImpl = new PersistentCacheServiceImpl();
    	
    	Class<?> clazz = Class.forName("uk.nhs.kch.rassyeyanie.framework.configuration.CacheService");
    	Method[] methods = clazz.getMethods();
    	for(Method method : methods) {
    		if(method.getName().equals("getCachedValues")) {
    			System.out.println(method);
    			System.out.println(cacheServiceImpl);
    			method.invoke(cacheServiceImpl, "");
    		}
    	}
    }
}
