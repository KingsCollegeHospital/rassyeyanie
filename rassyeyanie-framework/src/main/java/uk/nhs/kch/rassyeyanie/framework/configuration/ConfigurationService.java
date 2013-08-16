package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 */
public interface ConfigurationService {

	@Deprecated
	List<String> findAllContexts();

	@Deprecated
	String findDefaultValue(String context);

	@Deprecated
	Map<String, String> findAllValues(String context);

	@Deprecated
	Map<String, String> findAllValuesByDate(String context, Date date);

	@Deprecated
	String findValue(String context, String key);

	@Deprecated
	String findValueByDate(String context, String key, Date date);

	String findValue(int contextId, String key);

	String findValueByDate(int contextId, String key, Date date);

	String findDefaultValue(int contextId);

	String findDefaultValueById(int context);

	int findContextIdByName(String context);

	boolean doesKeyExist(int contextId, String key);

	boolean doesKeyExistByDate(int contextId, String key);

	void updatePatientByNumber(String nhsNo, String hospitalNo,
			Calendar dateOfBirth, String givenName, String surname);

	void createPatientByNumber(String nhsNo, String hospitalNo,
			Calendar dateOfBirth, String givenName, String surname);

	String findPatientNhsNumberByHospitalNumber(String kingsReference,
			Calendar dateOfBirth, String surname, String givenName);
}
