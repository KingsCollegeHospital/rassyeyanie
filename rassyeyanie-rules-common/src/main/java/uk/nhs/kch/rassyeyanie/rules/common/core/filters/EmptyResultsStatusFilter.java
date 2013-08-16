package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.message.ORU_R01;
import ca.uhn.hl7v2.model.v24.segment.OBX;

public class EmptyResultsStatusFilter {
	
	public boolean shouldProcessMessage(@Body AbstractMessage body)
			throws HL7Exception {
		ORU_R01 r01 = (ORU_R01) body;

		boolean resultsStatus = true;

		int resultCount = r01.getPATIENT_RESULT().getORDER_OBSERVATIONReps();

		for (int i = 0; i < resultCount; i++){

			int obxCount = r01.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATIONReps();
				for (int j = 0; j < obxCount && resultsStatus; j++ ){
					
					OBX obx = r01.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
					
					resultsStatus = this.checkOBX11(obx.getObx11_ObservationResultStatus().getValue());
				}
		}
		return (resultsStatus);

	}

	private boolean checkOBX11(String aResultStatus) {
		return (StringUtils.isNotEmpty(aResultStatus));	
	}

}
