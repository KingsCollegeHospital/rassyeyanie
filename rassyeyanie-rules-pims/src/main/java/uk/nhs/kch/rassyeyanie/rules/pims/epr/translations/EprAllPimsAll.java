package uk.nhs.kch.rassyeyanie.rules.pims.epr.translations;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class EprAllPimsAll {
	@Autowired
	private ConfigurationService configurationService;

	public void processMessage(@Body AbstractMessage message)
			throws HL7Exception {
		MSH msh = HapiUtil.getWithTerser(message, MSH.class);
		PV1 pv1 = HapiUtil.getWithTerser(message, PV1.class);

		String visitPrefix = StringUtils.substring(pv1.getVisitNumber().getID()
				.getValue(), 0, 2);

		int contextId = this.configurationService
				.findContextIdByName("EPR_PIMS_SENDINGAPP");
		String value = this.configurationService.findValue(contextId,
				visitPrefix);
		msh.getSendingApplication().getNamespaceID().setValue(value);
	}
}
