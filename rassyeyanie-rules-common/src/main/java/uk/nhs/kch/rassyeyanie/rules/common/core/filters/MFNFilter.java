package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.MFN_M02;

public class MFNFilter {

	public boolean shouldProcessMessage(@Body MFN_M02 body) throws HL7Exception {

		return (StringUtils.equals(body.getMF_STAFF().getSTF()
				.getStf2_StaffIDCode(0).getCx1_ID().getValue(), "NATGP"));

	}
}
