package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.sql.Date;
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
@Table(name = "lookup", schema = "kch_lookup", catalog = "")
@Entity
@NamedQueries({
	@NamedQuery(name = LookupEntity.FIND_ALL_VALUES, query = "select l from LookupEntity l, ContextEntity c "
		+ "where l.contextId = c.id "
		+ "and c.context = :context "
		+ "and l.startDate <= :today and (l.endDate > :today or l.endDate is null)"),
	@NamedQuery(name = LookupEntity.FIND_VALUE, query = "select l.value from LookupEntity l, ContextEntity c "
		+ "where l.contextId = c.id "
		+ "and c.context = :context "
		+ "and l.key = :key "
		+ "and l.startDate <= :today and (l.endDate > :today or l.endDate is null)"),
	@NamedQuery(name = LookupEntity.FIND_VALUE_NO_JOIN, query = "select l.value from LookupEntity l "
		+ "where l.contextId = :contextId "
		+ "and l.key = :key "
		+ "and l.startDate <= :today and (l.endDate > :today or l.endDate is null)"),
	@NamedQuery(name = LookupEntity.FIND_KEY, query = "select l.key from LookupEntity l "
				+ "where l.contextId = :contextId "
				+ "and l.key = :key "
				+ "and l.startDate <= :today and (l.endDate > :today or l.endDate is null)")
		

}
		)
public class LookupEntity {

    public static final String FIND_ALL_VALUES = "LookupEntity.findAllValues";
    public static final String FIND_VALUE = "LookupEntity.findValue";
    public static final String FIND_VALUE_NO_JOIN = "LookupEntity.findValueNoJoin";
    public static final String FIND_KEY = "LookupEntity.findKey";
    
    private int id;

    @Column(name = "id")
    @Id
    public int getId() {
	return this.id;
    }

    public void setId(int id) {
	this.id = id;
    }

    private int contextId;

    @Column(name = "contextid")
    @Basic
    public int getContextId() {
	return this.contextId;
    }

    public void setContextId(int contextId) {
	this.contextId = contextId;
    }

    private String key;

    @Column(name = "key")
    @Basic
    public String getKey() {
	return this.key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    private String value;

    @Column(name = "value")
    @Basic
    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    private Date startDate;

    @Column(name = "startdate")
    @Basic
    public Date getStartDate() {
	return this.startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    private Date endDate;

    @Column(name = "enddate")
    @Basic
    public Date getEndDate() {
	return this.endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    private String lastUpdatedBy;

    @Column(name = "lastupdatedby")
    @Basic
    public String getLastUpdatedBy() {
	return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
	this.lastUpdatedBy = lastUpdatedBy;
    }

    private Timestamp lastUpdatedTime;

    @Column(name = "lastupdatedtime")
    @Basic
    public Timestamp getLastUpdatedTime() {
	return this.lastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
	this.lastUpdatedTime = lastUpdatedTime;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || this.getClass() != o.getClass())
	    return false;

	LookupEntity kchLookup = (LookupEntity) o;

	if (this.contextId != kchLookup.contextId)
	    return false;
	if (this.id != kchLookup.id)
	    return false;
	if (this.endDate != null ? !this.endDate.equals(kchLookup.endDate)
		: kchLookup.endDate != null)
	    return false;
	if (this.key != null ? !this.key.equals(kchLookup.key)
		: kchLookup.key != null)
	    return false;
	if (this.lastUpdatedBy != null ? !this.lastUpdatedBy
		.equals(kchLookup.lastUpdatedBy)
		: kchLookup.lastUpdatedBy != null)
	    return false;
	if (this.lastUpdatedTime != null ? !this.lastUpdatedTime
		.equals(kchLookup.lastUpdatedTime)
		: kchLookup.lastUpdatedTime != null)
	    return false;
	if (this.startDate != null ? !this.startDate
		.equals(kchLookup.startDate) : kchLookup.startDate != null)
	    return false;
	if (this.value != null ? !this.value.equals(kchLookup.value)
		: kchLookup.value != null)
	    return false;

	return true;
    }

    @Override
    public int hashCode() {
	int result = this.id;
	result = 31 * result + this.contextId;
	result = 31 * result + (this.key != null ? this.key.hashCode() : 0);
	result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
	result = 31 * result
		+ (this.startDate != null ? this.startDate.hashCode() : 0);
	result = 31 * result
		+ (this.endDate != null ? this.endDate.hashCode() : 0);
	result = 31
		* result
		+ (this.lastUpdatedBy != null ? this.lastUpdatedBy.hashCode()
			: 0);
	result = 31
		* result
		+ (this.lastUpdatedTime != null ? this.lastUpdatedTime
			.hashCode() : 0);
	return result;
    }
}
