package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PID;

public class ValidPatientIDFilter {

	public boolean shouldProcessMessage(@Body AbstractMessage body)
			throws HL7Exception {
		PID pid = HapiUtil.getWithTerser(body, PID.class);

		return this.checkPtIdInternalConforms(StringUtils.defaultString(pid
				.getPid3_PatientIdentifierList(0).getCx1_ID().getValue()));

	}

	private boolean checkPtIdInternalConforms(String patientIdInternal) {

		return (StringUtils.length(patientIdInternal) > 7
				&& (StringUtils.left(patientIdInternal, 2).equals("DH") || StringUtils
						.left(patientIdInternal, 2).equals("NS"))

				&& StringUtils.isNumeric(StringUtils.mid(patientIdInternal, 2,
						6)) || (StringUtils.length(patientIdInternal) > 6
				&& (StringUtils.left(patientIdInternal, 1).equals("0") || StringUtils
						.isAllUpperCase(StringUtils.left(patientIdInternal, 1))) && StringUtils
					.isNumeric(StringUtils.mid(patientIdInternal, 1, 6))));

	}

}
