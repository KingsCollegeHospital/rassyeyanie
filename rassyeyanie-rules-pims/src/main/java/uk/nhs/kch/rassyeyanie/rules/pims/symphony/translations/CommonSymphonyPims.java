package uk.nhs.kch.rassyeyanie.rules.pims.symphony.translations;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import uk.nhs.kch.rassyeyanie.framework.configuration.ConfigurationService;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.PV2;

public class CommonSymphonyPims {
	@Autowired
	private ConfigurationService configurationService;

	public static final String SYM_PIMS_SEX = "SYM_PIMS_SEX";

	public void transform(EVN evn) throws HL7Exception {

		evn.getEventOccurred().clear();
	}

	public void transform(MSH msh) throws HL7Exception {
		msh.getSendingApplication().getNamespaceID().setValue("SYMPHONY");
		msh.getContinuationPointer().clear();
	}

	public void transform(PID pid) throws HL7Exception {
		if (StringUtils.isNotEmpty(pid.getPatientID().getID().getValue())) {
			pid.getPatientID().getIdentifierTypeCode().setValue("NHS");
		}
		pid.getPatientIdentifierList(0).getAssigningAuthority().clear();
		pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("PAS");
		pid.getPatientName(0)
				.getPrefixEgDR()
				.setValue(
						StringUtils.capitalize(StringUtils.lowerCase(pid
								.getPatientName(0).getPrefixEgDR().getValue())));

		int contextId = this.configurationService
				.findContextIdByName(SYM_PIMS_SEX);

		String sex = this.configurationService.findValue(contextId, StringUtils
				.defaultString(pid.getAdministrativeSex().getValue()));

		pid.getAdministrativeSex().setValue(sex);
	}

	public void transform(PV1 pv1) throws HL7Exception {

	}

	public void transform(PV2 pv2) throws HL7Exception {
		pv2.clear();
	}

	public void dispatchProcessFixture(@Body AbstractMessage to)
			throws HL7Exception {
		this.transform(HapiUtil.get(to, MSH.class));
		this.transform(HapiUtil.get(to, PID.class));
		this.transform(HapiUtil.get(to, PV1.class));
		this.transform(HapiUtil.get(to, PV2.class));
		this.transform(HapiUtil.get(to, EVN.class));
	}

}
