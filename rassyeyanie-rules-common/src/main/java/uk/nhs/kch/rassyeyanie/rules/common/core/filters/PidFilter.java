package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PID;

/*

 (define WPSCM_O01
 (let ((input (make-message-structure WNPTORM-delm WNPTORM-struct))
 )
 (lambda (message-string)
 (message-parse input message-string)
 (let ((result
 (and
 (verify ~input%WNPTORM.MSH.MSH-9-message_type.MSH-9-1_message_type "ORM")
 (verify ~input%WNPTORM.MSH.MSH-9-message_type.MSH-9-2_trigger_event "O01")
 (if (> (string-length ~input%WNPTORM.patient.PID.PID-3-patient_ID_internal[0].CX.ID_number) 7)
 (begin
 (verify ~input%WNPTORM.patient.PID.PID-3-patient_ID_internal[0].CX.ID_number "\[DH\|NS\]\{2\}\[0-9\]\{6\}")
 )
 (begin
 (verify ~input%WNPTORM.patient.PID.PID-3-patient_ID_internal[0].CX.ID_number "\[0\|A-Z\]\{1\}\[0-9\]\{6\}")
 )
 )


 */

public class PidFilter {

	public boolean shouldProcessMessage(@Body AbstractMessage body)
			throws HL7Exception {
		PID pid = HapiUtil.getWithTerser(body, PID.class);
		return this.checkPtIdInternalConforms(StringUtils.defaultString(pid
				.getPid3_PatientIdentifierList(0).getCx1_ID().getValue()));

	}

	private boolean checkPtIdInternalConforms(String patientIdInternal) {

		if (StringUtils.isEmpty(patientIdInternal))
			return false;

		if (patientIdInternal.length() > 7) {
			return patientIdInternal.matches("(DH|NS){1}[0-9]{6}");
		} else {
			return patientIdInternal.matches("[0|A-Z]{1}[0-9]{6}");
		}

	}

}
