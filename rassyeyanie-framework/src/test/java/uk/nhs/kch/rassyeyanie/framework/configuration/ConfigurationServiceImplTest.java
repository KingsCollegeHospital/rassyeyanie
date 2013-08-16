package uk.nhs.kch.rassyeyanie.framework.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:dbtest-context.xml")
public class ConfigurationServiceImplTest
{
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private DataSource dataSource;
    
    private static List<String> setupDatabaseSQL;
    private static List<String> teardownDatabaseSQL;
    
    @BeforeClass
    public static void init()
        throws IOException
    {
        setupDatabaseSQL = readLines("hsqldb/setup_db.sql");
        teardownDatabaseSQL = readLines("hsqldb/teardown_db.sql");
    }
    
    private static List<String> readLines(String resource)
        throws IOException
    {
        InputStream in =
            ConfigurationServiceImplTest.class
                .getClassLoader()
                .getResourceAsStream(resource);
        
        try
        {
            return IOUtils.readLines(in);
        }
        finally
        {
            in.close();
        }
    }
    
    @Before
    public void setUp()
        throws SQLException
    {
        // MockitoAnnotations.initMocks(this);
        
        runSQL(setupDatabaseSQL);
    }
    
    private void runSQL(List<String> sqlList)
        throws SQLException
    {
        
        Connection connection = dataSource.getConnection();
        
        try
        {
            Statement statement = connection.createStatement();
            
            try
            {
                for (String sql : sqlList)
                {
                    statement.addBatch(sql);
                }
                statement.executeBatch();
            }
            finally
            {
                statement.close();
            }
            
        }
        finally
        {
            connection.close();
        }
    }
    
    @After
    public void teardown()
        throws SQLException
    {
        runSQL(teardownDatabaseSQL);
    }
    
    @Test
    public void testFindAllContexts()
    {
        List<String> contextList = configurationService.findAllContexts();
        assertNotNull(contextList);
        assertFalse(contextList.isEmpty());
    }
    
    @Test
    public void testFindDefaultValue()
    {
        String value = configurationService.findDefaultValue("context1");
        assertEquals("default for context 1", value);
    }
    
    @Test
    public void testFindDefaultValueInvalidContext()
    {
        String value = configurationService.findDefaultValue("invalid");
        assertNull(value);
    }
    
    @Test
    public void testFindAllValues()
    {
        Map<String, String> contextMap =
            configurationService.findAllValues("context1");
        assertNotNull(contextMap);
        assertFalse(contextMap.isEmpty());
    }
    
    @Test
    public void testFindAllValuesInvalidContext()
    {
        Map<String, String> contextMap =
            configurationService.findAllValues("invalid");
        assertNotNull(contextMap);
        assertTrue(contextMap.isEmpty());
    }
    
    @Test
    public void testFindValue()
    {
        String value = configurationService.findValue("context1", "key1");
        assertNotNull(value);
        assertEquals("value1", value);
    }
    
    @Test
    public void testFindValueInvalidKey()
    {
        String defaultValue = configurationService.findDefaultValue("context1");
        String value = configurationService.findValue("context1", "invalid");
        
        assertNotNull(value);
        assertEquals(defaultValue, value);
    }
    
    @Test
    public void testDoesKeyExist()
    {
        boolean value = configurationService.doesKeyExist(1, "key1");
        assertEquals(true, value);
    }
    
    /**
     * @Override
     *           public boolean doesKeyExistByDate(int contextId, String key)
     *           {
     *           Query query = this.createNamedQuery(LookupEntity.FIND_KEY);
     *           query.setParameter("contextId", contextId);
     *           query.setParameter("key", key);
     *           query.setParameter("today", this.today());
     * 
     *           String result = this.findSingle(query);
     *           if (result == null) { return false; }
     *           return true;
     *           }
     */
    
}
