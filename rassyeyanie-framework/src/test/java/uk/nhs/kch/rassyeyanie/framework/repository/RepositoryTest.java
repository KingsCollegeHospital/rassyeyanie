package uk.nhs.kch.rassyeyanie.framework.repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:dbtest-context.xml")
@Repository("configurationService")
public class RepositoryTest
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
            RepositoryTest.class.getClassLoader().getResourceAsStream(resource);
        
        try
        {
            return IOUtils.readLines(in);
        }
        finally
        {
            in.close();
        }
    }
    
    @Test
    public void getKeyValuePairItem()
    {
        // assertTrue(true);
        KeyValueItemRepository keyValueItemRepository =
            new KeyValueItemRepository(this.configurationService);
        assertEquals("value1", keyValueItemRepository
            .Get("context1", "key1")
            .getItemValue());
    }
    
    @Before
    public void setUp()
        throws SQLException, IOException
    {
        this.runSQL(setupDatabaseSQL);
    }
    
    private void runSQL(List<String> sqlList)
        throws SQLException
    {
        
        Connection connection = this.dataSource.getConnection();
        
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
        this.runSQL(teardownDatabaseSQL);
    }
    
}
