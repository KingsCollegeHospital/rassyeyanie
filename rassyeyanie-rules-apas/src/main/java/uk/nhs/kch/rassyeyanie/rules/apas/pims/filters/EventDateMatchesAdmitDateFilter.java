package uk.nhs.kch.rassyeyanie.rules.apas.pims.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.EVN;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class EventDateMatchesAdmitDateFilter {

	public boolean shouldProcessMessage(@Body AbstractMessage body)
			throws Exception {

		EVN evn = HapiUtil.get(body, EVN.class);
		PV1 pv1 = HapiUtil.get(body, PV1.class);
		String eventDtm = evn.getEvn2_RecordedDateTime().getTimeOfAnEvent()
				.getValue();
		String admitDtm = pv1.getPv144_AdmitDateTime().getTimeOfAnEvent()
				.getValue();

		return StringUtils.equals(StringUtils.substring(admitDtm, 0, 8),
				StringUtils.substring(eventDtm, 0, 8));

	}
}
