package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PID;

public class PatientIDNotBlankFilter {



	public boolean shouldProcessMessage(@Body AbstractMessage body)
			throws HL7Exception {
		PID pid = HapiUtil.getWithTerser(body, PID.class);
		return StringUtils.isNotEmpty(pid
				.getPid3_PatientIdentifierList(0).getCx1_ID().getValue());

	}
}
