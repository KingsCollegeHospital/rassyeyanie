package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 */
@Table(name = "context", schema = "kch_lookup", catalog = "")
@Entity
@NamedQueries({
    @NamedQuery(name = ContextEntity.FIND_ALL_CONTEXTS,
                query = "select c.context from ContextEntity c "),
    @NamedQuery(name = ContextEntity.FIND_DEFAULT_VALUE,
                query = "select c.defaultValue from ContextEntity c "
                        + "where c.context = :context "),
    @NamedQuery(name = ContextEntity.FIND_CONTEXT_ID_BY_NAME,
                query = "select c.id from ContextEntity c "
                        + "where c.context = :context "),
    @NamedQuery(name = ContextEntity.FIND_DEFAULT_VALUE_BY_ID,
                query = "select c.defaultValue from ContextEntity c "
                        + "where c.id = :contextId ")
})
public class ContextEntity
{
    
    public static final String FIND_ALL_CONTEXTS =
        "ContextEntity.findAllContexts";
    public static final String FIND_DEFAULT_VALUE =
        "ContextEntity.findDefaultValue";
    public static final String FIND_DEFAULT_VALUE_BY_ID =
        "ContextEntity.findDefaultValueById";
    public static final String FIND_CONTEXT_ID_BY_NAME =
        "ContextEntity.findContextIdByName";
    
    private int id;
    
    @Column(name = "id")
    @Id
    public int getId()
    {
        return this.id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    private String context;
    
    @Column(name = "context")
    @Basic
    public String getContext()
    {
        return this.context;
    }
    
    public void setContext(String context)
    {
        this.context = context;
    }
    
    private String defaultValue;
    
    @Column(name = "defaultvalue")
    @Basic
    public String getDefaultValue()
    {
        return this.defaultValue;
    }
    
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    private String lastUpdatedBy;
    
    @Column(name = "lastupdatedby")
    @Basic
    public String getLastUpdatedBy()
    {
        return this.lastUpdatedBy;
    }
    
    public void setLastUpdatedBy(String lastUpdatedBy)
    {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    private Timestamp lastUpdatedTime;
    
    @Column(name = "lastupdatedtime")
    @Basic
    public Timestamp getLastUpdatedTime()
    {
        return this.lastUpdatedTime;
    }
    
    public void setLastUpdatedTime(Timestamp lastUpdatedTime)
    {
        this.lastUpdatedTime = lastUpdatedTime;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        
        ContextEntity that = (ContextEntity) o;
        
        if (this.id != that.id)
            return false;
        if (this.context != null
            ? !this.context.equals(that.context)
            : that.context != null)
            return false;
        if (this.defaultValue != null ? !this.defaultValue
            .equals(that.defaultValue) : that.defaultValue != null)
            return false;
        if (this.lastUpdatedBy != null ? !this.lastUpdatedBy
            .equals(that.lastUpdatedBy) : that.lastUpdatedBy != null)
            return false;
        if (this.lastUpdatedTime != null ? !this.lastUpdatedTime
            .equals(that.lastUpdatedTime) : that.lastUpdatedTime != null)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        int result = this.id;
        result =
            31 * result + (this.context != null ? this.context.hashCode() : 0);
        result =
            31 * result +
                (this.defaultValue != null ? this.defaultValue.hashCode() : 0);
        result =
            31 *
                result +
                (this.lastUpdatedBy != null ? this.lastUpdatedBy.hashCode() : 0);
        result =
            31 *
                result +
                (this.lastUpdatedTime != null
                    ? this.lastUpdatedTime.hashCode()
                    : 0);
        return result;
    }
}
