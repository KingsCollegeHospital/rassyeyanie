package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for finding configuration values from the database. This
 * service is exposed as an OSGi service so can be accessed from other bundles.
 */
// TODO: decouple caching layer -- add new generic caching layer for both
// blocking patient
// and configuration lookups
@Repository("configurationService")
@Scope("singleton")
public class ConfigurationServiceImpl implements ConfigurationService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CacheManager cacheManager;

	@SuppressWarnings("unchecked")
	private <T> List<T> findList(Query query) {
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private <T> T findSingle(Query query) {
		List<?> resultList = this.findList(query);
		if (resultList.isEmpty()) {
			return null;
		}

		return (T) resultList.get(0);
	}

	private Query createNamedQuery(String queryName) {
		return this.entityManager.createNamedQuery(queryName);
	}

	private Date today() {
		return DateUtils.truncate(new Date(), Calendar.DATE);
	}

	/**
	 * Finds a list of all contexts.
	 */
	@Override
	public List<String> findAllContexts() {
		Query query = this.createNamedQuery(ContextEntity.FIND_ALL_CONTEXTS);

		return this.findList(query);
	}

	/**
	 * Finds the default value for a context.
	 */
	@Override
	public String findDefaultValueById(int contextId) {
		Query query = this
				.createNamedQuery(ContextEntity.FIND_DEFAULT_VALUE_BY_ID);
		query.setParameter("contextId", contextId);

		return this.findSingle(query);
	}

	/**
	 * Finds the default value for a context.
	 */
	@Override
	public String findDefaultValue(String context) {
		Query query = this.createNamedQuery(ContextEntity.FIND_DEFAULT_VALUE);
		query.setParameter("context", context);

		return this.findSingle(query);
	}

	@Override
	public int findContextIdByName(String context) {
		String cacheKey = context;
		Cache cache = this.getCache("context_cache");
		Element cacheElement = cache.get(cacheKey);
		if (cacheElement == null || cacheElement.getObjectValue() == null) {
			Query query = this
					.createNamedQuery(ContextEntity.FIND_CONTEXT_ID_BY_NAME);
			query.setParameter("context", context);
			Integer value = (Integer) this.findSingle(query);
			cache.put(new Element(cacheKey, value));
			return value;
		} else {
			return (Integer) cacheElement.getObjectValue();
		}
	}

	/**
	 * Finds all the current values for a given context.
	 */
	@Override
	public Map<String, String> findAllValues(String context) {
		return this.findAllValuesByDate(context, this.today());
	}

	/**
	 * Finds all the values for a given context on a specific date.
	 */
	@Override
	public Map<String, String> findAllValuesByDate(String context, Date date) {
		Query query = this.createNamedQuery(LookupEntity.FIND_ALL_VALUES);
		query.setParameter("context", context);
		query.setParameter("today", date);

		List<LookupEntity> lookupEntityList = this.findList(query);
		Map<String, String> result = new HashMap<String, String>();

		for (LookupEntity lookupEntity : lookupEntityList) {
			result.put(lookupEntity.getKey(), lookupEntity.getValue());
		}

		return result;
	}

	/**
	 * Finds the current value of a given key in the context. If the key does
	 * not exist then the default value will be returned.
	 */
	@Override
	public String findValue(String context, String key) {
		return this.findValueByDate(context, key, this.today());
	}

	/**
	 * Finds the current value of a given key in the context on a specific date.
	 * If the key does not exist then the default value will be returned.
	 */
	@Override
	public String findValueByDate(String context, String key, Date date) {
		Query query = this.createNamedQuery(LookupEntity.FIND_VALUE);
		query.setParameter("context", context);
		query.setParameter("key", key);
		query.setParameter("today", date);

		String result = this.findSingle(query);
		if (result == null) {
			return this.findDefaultValue(context);
		}

		return result;
	}

	private Cache getCache(String cacheName) {

		if (!this.cacheManager.cacheExists(cacheName)) {
			this.cacheManager.addCache(cacheName);
		}

		return this.cacheManager.getCache(cacheName);
	}

	/**
	 * Finds the current value of a given key in the context. If the key does
	 * not exist then the default value will be returned.
	 */
	@Override
	public String findValue(int contextId, String key) {
		Date today = this.today();
		String cacheKey = contextId + key + today.toString();
		Cache cache = this.getCache("value_cache");
		Element cacheElement = cache.get(cacheKey);

		if (cacheElement == null) {
			String value = this.findValueByDate(contextId, key, today);
			cache.put(new Element(cacheKey, value));
			return value;
		} else
			return (String) cacheElement.getObjectValue();
	}

	/**
	 * Finds the current value of a given key in the context on a specific date.
	 * If the key does not exist then the default value will be returned.
	 */
	@Override
	public String findValueByDate(int contextId, String key, Date date) {
		Query query = this.createNamedQuery(LookupEntity.FIND_VALUE_NO_JOIN);
		query.setParameter("contextId", contextId);
		query.setParameter("key", key);
		query.setParameter("today", date);

		String result = this.findSingle(query);
		if (result == null) {
			return this.findDefaultValue(contextId);
		}

		return result;
	}

	@Override
	public String findDefaultValue(int contextId) {

		String cacheKey = contextId + "";
		Cache cache = this.getCache("context_cache");
		Element cacheElement = cache.get(cacheKey);

		if (cacheElement == null) {
			Query query = this
					.createNamedQuery(ContextEntity.FIND_DEFAULT_VALUE_BY_ID);
			query.setParameter("contextId", contextId);

			String value = this.findSingle(query);
			cache.put(new Element(cacheKey, value));
			return value;
		} else
			return (String) cacheElement.getObjectValue();

	}

	@Override
	public boolean doesKeyExist(int contextId, String key) {

		String cacheKey = contextId + key + "find_key";
		Cache cache = this.getCache("context_cache");
		Element cacheElement = cache.get(cacheKey);

		if (cacheElement == null) {
			boolean doesKeyExist = this.doesKeyExistByDate(contextId, key);
			cache.put(new Element(cacheKey, doesKeyExist));
			return doesKeyExist;
		} else {
			return (Boolean) cacheElement.getObjectValue();
		}
	}

	@Override
	public boolean doesKeyExistByDate(int contextId, String key) {
		Query query = this.createNamedQuery(LookupEntity.FIND_KEY);
		query.setParameter("contextId", contextId);
		query.setParameter("key", key);
		query.setParameter("today", this.today());

		String result = this.findSingle(query);
		if (result == null) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void updatePatientByNumber(String nhsNo, String hospitalNo,
			Calendar dateOfBirth, String givenName, String surname) {
		NhsNumberTrackEntity nhsNumberTrackEntity = this.entityManager.find(
				NhsNumberTrackEntity.class, nhsNo);
		if (nhsNumberTrackEntity == null) {
			nhsNumberTrackEntity = new NhsNumberTrackEntity();
			nhsNumberTrackEntity.setNhsNo(nhsNo);
		}
		nhsNumberTrackEntity.setDateOfBirth(dateOfBirth);
		nhsNumberTrackEntity.setGivenName(givenName);
		nhsNumberTrackEntity.setHospitalNo(hospitalNo);
		nhsNumberTrackEntity.setSurname(surname);

		this.entityManager.persist(nhsNumberTrackEntity);
	}

	@Override
	public void createPatientByNumber(String nhsNo, String hospitalNo,
			Calendar dateOfBirth, String givenName, String surname) {
		updatePatientByNumber(nhsNo, hospitalNo, dateOfBirth, givenName,
				surname);
	}

	@Override
	public String findPatientNhsNumberByHospitalNumber(String kingsReference,
			Calendar dateOfBirth, String surname, String givenName) {

		HospitalNumberTrackEntity hospitalNumberTrackEntity = this.entityManager
				.find(HospitalNumberTrackEntity.class, kingsReference);
		if (hospitalNumberTrackEntity != null
				&& StringUtils.equalsIgnoreCase(givenName,
						hospitalNumberTrackEntity.getGivenName())
				&& StringUtils.equalsIgnoreCase(surname,
						hospitalNumberTrackEntity.getSurname())
				&& dateOfBirth.equals(hospitalNumberTrackEntity
						.getDateOfBirth())) {
			return hospitalNumberTrackEntity.getNhsNo();
		}

		return null;
	}

}
