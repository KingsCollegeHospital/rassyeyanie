package uk.nhs.kch.rassyeyanie.framework.configuration;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "nhsnumbertrack", schema = "kch_lookup", catalog = "")
@Entity
public class NhsNumberTrackEntity {
	public static final String UPDATE_PATIENT_BY_NUMBER = "NhsNumberTrackEntity.updatePatientByNumber";
	public static final String CREATE_PATIENT_BY_NUMBER = "NhsNumberTrackEntity.createPatientByNumber";

	private String nhsNo;
	String hospitalNo;
	String surname;
	String givenName;
	Calendar dateOfBirth;

	@Column(name = "hospitalno")
	@Basic
	public String getHospitalNo() {
		return hospitalNo;
	}

	public void setHospitalNo(String hospitalNo) {
		this.hospitalNo = hospitalNo;
	}

	@Column(name = "dateofbirth")
	@Basic
	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Column(name = "givenname")
	@Basic
	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	@Column(name = "surname")
	@Basic
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(name = "nhsno")
	@Id
	public String getNhsNo() {
		return nhsNo;
	}

	public void setNhsNo(String nhsNo) {
		this.nhsNo = nhsNo;
	}
}
