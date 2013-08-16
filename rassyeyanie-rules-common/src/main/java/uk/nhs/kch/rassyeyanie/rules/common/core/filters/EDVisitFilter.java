package uk.nhs.kch.rassyeyanie.rules.common.core.filters;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;

import uk.nhs.kch.rassyeyanie.framework.HapiUtil;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v24.segment.PV1;

public class EDVisitFilter {

    public boolean shouldProcessMessage(@Body AbstractMessage body)
            throws HL7Exception
        {
            PV1 pv1 = HapiUtil.getWithTerser(body, PV1.class);
            String patientVisit =           		
            		StringUtils.defaultString(pv1.getPv119_VisitNumber().getCx1_ID().getValue());
           return (patientVisit.startsWith("ED"));
        }

}
